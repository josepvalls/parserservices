#!/usr/bin/perl

$MAXCHAR = 799;
$MAXWORD = 400;

$charniakDir = "$ENV{CHARNIAK}";
$command = "$charniakDir/PARSE/parseIt $charniakDir/DATA/EN/ -K -l$MAXWORD";
#$endProtocol = "\n\n\n";
$endProtocol = "\n";
$TIMEOUT = 60;                  # undef if no timeout
$PORT = 4449;			# pick something not in use

#read port
$PORT = $ARGV[0] if (scalar(@ARGV) > 0);

use Expect;

#create main program that will be communicating throught pipe.
$main = NewExpect($command);

sub NewExpect {
  my $command = shift;
  my $main;

  print "[Initializing...]\n";

  $main = new Expect();
  $main->raw_pty(1);     # no local echo 
  $main->log_stdout(0);  # no echo
  $main->spawn($command) or die "Cannot start: $command\n";

  $main->send("<s> This is a test . </s>\n");  #send input to main program
  @res = $main->expect(undef,$endProtocol);  # read output from main program

  print "[Done initializing.]\n";

  return $main;
}

#server initialization matter
use IO::Socket;
use Net::hostent;		# for OO version of gethostbyaddr

$server = IO::Socket::INET->new( Proto     => 'tcp',
                                 LocalPort => $PORT,
                                 Listen    => SOMAXCONN,
                                 Reuse     => 1);

die "Can't setup server\n" unless $server;
#end server initialization

#set autoflush
$old_handle = select(STDOUT);
$| = 1;
select($old_handle);
$old_handle = select(STDERR);
$| = 1;
select($old_handle);

print "[Server $0 accepting clients]\n";

while ($client = $server->accept()) {
  $main->expect(0);  # flush old stuff if any
  $main->clear_accum();  # clear buffer

  $client->autoflush(1);
  $clientinfo = gethostbyaddr($client->peeraddr);
  if (defined($clientinfo)) {
    $clientname = ($clientinfo->name || $client->peerhost);
  } else {
    $clientname = $client->peerhost;
  }
  printf "[Connect from %s]\n", $clientname;

  &RunClient($client);

  shutdown($client,3);
  close($client);
  printf "[Connection closed from %s]\n", $clientname;
}

$main->hard_close();

sub RunClient {
  my $client = shift;
  my $msg;
  my $output;
  my @res;
  my $timeout;
  my $sent;

  while ($sent = <$client>) {
    chomp $sent;
    $sent =~ s/^\s+//;
    $sent =~ s/\s+$//;
    if ($sent =~ /^\s*$/) {  # sending blank line will cause the parser to quit
      $output = "\n\n";
    } elsif (length > $MAXCHAR) {
      $output = "\n\n";
    } else {
      $msg = "<s> $sent </s>\n";
      print "Parse: $msg";
      $main->send("$msg");  #send input to main program
      @res = $main->expect($TIMEOUT,$endProtocol);  # read output from main program

      # @res = ($mp, $er, $ms, $bf, $af);
      # $mp is ???
      # $er is undef or 1:TIMEOUT
      # $ms is the matched message
      # $bf is the message before $ms
      # $af is the message after $ms

      $timeout = $res[1];
      $out = $res[3];
      if ($timeout) { # parser possibly gets stuck, restart it.
        print "Time out!\n";
        $output = "\n\n";  # output blank
        print "Restart parser\n";
        $main->hard_close();
        $main = NewExpect($command);
      } else {

        if ($out =~ /^Parse failed/) {
          print "Parse failed\n";
          $output = "\n\n";
          @res = $main->expect($TIMEOUT,$endProtocol);  # read off the original sentence
          $timeout = $res[1];
          if ($timeout) { # parser possibly gets stuck, restart it.
            print "Time out when reading off the original sentence!\n";
            print "Restart parser\n";
            $main->hard_close();
            $main = NewExpect($command);
          }
        } elsif ($out =~ /^error:|^parseIt.*Assertion.*failed/) { # parser dies
          print "Parser died!\n";
          $output = "\n\n";  # output blank
          print "Restart parser\n";
          $main->hard_close();
          $main = NewExpect($command);
        } else {
          print "Parse ok\n";
          $output = "$out\n";
          if ($out =~ /^\s*$/) { $numBlank = 1; }
          else { $numBlank = 0; }
  
          #normal output should end with 2 blank lines.
          while ($numBlank < 2) {
            @res = $main->expect($TIMEOUT,$endProtocol);  # read output from main program
            $timeout = $res[1];
            $out = $res[3];
            if ($timeout) { # parser possibly gets stuck, restart it.
              print "Time out when reading output!\n";
              $output = "\n\n";  # output blank
              print "Restart parser\n";
              $main->hard_close();
              $main = NewExpect($command);
              last;
            } else {
              $output .= "$out\n";
              if ($out =~ /^\s*$/) { $numBlank++; }
              else { $numBlank = 0; }
            }
          }
        }
      }
    }

    $output = &fixoutput($sent, $output);
    print $client $output;  # send output back to client

    $main->clear_accum();  # clear buffer
  }
}

sub fixoutput {
  my ($input, $output) = @_;
  my @input;
  my @output;
  my ($i, $length, $outlength);

  @input = split /\s+/, replacesymbol($input);
  $length = scalar(@input);

  $outlength = 0;
  while ($output =~ /[^\)]\)/g) { $outlength++; }

  if ($outlength == 0) {
    $output = "(S1 H:0 (X H:0";
    for ($i = 0; $i < $length; $i++) {
      $output .= " (. H:0 $input[$i])";
    }
    $output .= "))\n\n\n";
  } elsif ($length != $outlength) {
    $output =~ s/\)\s*$//;
    for ($i = $outlength; $i < $length; $i++) {
      $output .= " (. H:0 $input[$i])";
    }
    $output .= ")\n\n\n";
  }
  return $output;
}

sub replacesymbol {
  my $input = shift;

  $input =~ s/\(/-LRB-/g;
  $input =~ s/\)/-RRB-/g;
  $input =~ s/\[/-LSB-/g;
  $input =~ s/\]/-RSB-/g;
  $input =~ s/\{/-LCB-/g;
  $input =~ s/\}/-RCB-/g;

  return $input;
}
