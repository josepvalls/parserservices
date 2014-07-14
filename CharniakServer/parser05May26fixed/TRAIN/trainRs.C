/*
 * Copyright 1999, 2005 Brown University, Providence, RI.
 * 
 *                         All Rights Reserved
 * 
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose other than its incorporation into a
 * commercial product is hereby granted without fee, provided that the
 * above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation, and that the name of Brown University not be used in
 * advertising or publicity pertaining to distribution of the software
 * without specific, written prior permission.
 * 
 * BROWN UNIVERSITY DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY
 * PARTICULAR PURPOSE.  IN NO EVENT SHALL BROWN UNIVERSITY BE LIABLE FOR
 * ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

#include <fstream>
#include <sys/resource.h>
#include <iostream>
#include <unistd.h>
#include <set>
#include "ECArgs.h"
#include "Feature.h"
#include "FeatureTree.h"
#include "InputTree.h"
#include "headFinder.h"
#include "treeHistSf.h"
#include "Pst.h"
#include "Smoother.h"
#include "TreeHist.h"
#include "Term.h"
#include "ClassRule.h"
#include <cmath>

int pass;
int whichInt;
int sentenceCount;
int c_Val;
bool procGSwitch = false;

/* for a given history, as specified by a tree, for each feature f_i record
   how often it was used. */

FeatureTree* tRoot = NULL;
ECString conditionedType;
InputTree* curTree = NULL;
void goThroughSents(InputTree* trainingData[1301], int sc);

struct TrData 
{
  float c;
  float pm;
};

TrData trData[MAXNUMFS][15];

float unsmoothedPs[MAXNUMFS];
float lambdas[MAXNUMFS];
int bucketVals[MAXNUMFS];
bool prune = false;
typedef set<ECString, less<ECString> > StringSet;
StringSet wordSet;
bool
inWordSet(int i)
{
  const ECString& w = Pst::fromInt(i);
  if(wordSet.find(w) == wordSet.end()) return false;
  return true;
}

InputTree* sentence[256];
int endPos;
void wordsFromTree(InputTree* tree);

void
makeSent(InputTree* tree)
{
  endPos = 0;
  for(int i = 0 ; i < 128 ; i++) sentence[i] = NULL;
  wordsFromTree(tree);
  assert(endPos == tree->finish());
}

void
wordsFromTree(InputTree* tree)
{
  if(tree->word() != "")
    {
      sentence[endPos++] = tree;
      return;
    }
  InputTreesIter subTreeIter = tree->subTrees().begin();
  for( ; subTreeIter != tree->subTrees().end() ; subTreeIter++ )
    {
      InputTree* subTree = *subTreeIter;
      wordsFromTree(subTree);
    }
}

void
processG(int i, FeatureTree* ginfo[], TreeHist* treeh, int cVal)
{
  ginfo[i] = NULL;
  Feature* feat = Feature::fromInt(i, whichInt); 
  /* e.g., g(rtlu) starts from where g(rtl) left off (after tl)*/
  int searchStartInd = feat->startPos;
  FeatureTree* strt = ginfo[searchStartInd];
  bucketVals[i] = 0;
  lambdas[i] = 0;
  unsmoothedPs[i] = 0;
  if(!strt)
    {
      if(i == 1) cerr << "no start for i = 1\n" << *curTree << endl;
      return;
    }
  SubFeature* sf = SubFeature::fromInt(feat->subFeat, whichInt);
  int nfeatV = (*(sf->fun))(treeh);
  //if(procGSwitch) cerr << "pg " << i << ", " << cVal << ", " << nfeatV << endl;
  FeatureTree* histPt = strt->follow(nfeatV, feat->auxCnt); 
  ginfo[i] = histPt;
  if(i == 1)
    {
      unsmoothedPs[0] = 0;

      FeatMap::iterator fti1 = histPt->feats.find(cVal);
      float unsmoothedVal1;
      if(fti1 == histPt->feats.end()) unsmoothedVal1 = 0;
      else unsmoothedVal1 = (*fti1).second.g();

      unsmoothedPs[1] = unsmoothedVal1;
      lambdas[1] = 1;
      if(unsmoothedPs[1] == 0)
	{
	  /*
	  if(pass == 0)
	    cerr << "Zero at level 1 " << treeh->pos
		 << "\n" << *curTree << endl;
	  */
	  unsmoothedPs[1] = .0001;
	}
      return;
    }
  if(!histPt)
    {
      return;
    }
  int b;
  if(Feature::isLM)
    {
      int sz = histPt->feats.size();
      assert(sz > 0);
      float estm = ((float)histPt->count / (float)sz);
      b = Smoother::bucket(estm,i);
    }
  else
    {
      float estm = histPt->count * unsmoothedPs[1]; 
      b = Smoother::bucket(estm);
    }

  FeatMap::iterator fti = histPt->feats.find(cVal);
  float unsmoothedVal;
  if(fti == histPt->feats.end()) unsmoothedVal = 0;
  else unsmoothedVal = (*fti).second.g();
  float lam = Feature::getLambda(whichInt, i, b);
  lambdas[i] = lam;
  unsmoothedPs[i] = unsmoothedVal;
  bucketVals[i] = b;
  //if(procGSwitch)   cerr << i << " " << nfeatV << " " << histPt->featureInt << " "
  // << estm << " " << b << " " << unsmoothedVal << endl;
}

void
callProcG(TreeHist* treeh)
{
  FeatureTree* ginfo[MAXNUMFS];
  ginfo[0] = FeatureTree::root(); 
  int cVal = (*Feature::conditionedEvent)(treeh);
  if(cVal < 0) return;
  int i;
  for(i = 1 ; i <= Feature::total[Feature::whichInt] ; i++)
    processG(i, ginfo, treeh, cVal);
  double total = 0;
  double postLam[MAXNUMFS];
  double percents[MAXNUMFS];
  double remainingProb = 1.0;
  for(i = Feature::total[Feature::whichInt] ; i > 0 ; i--)
    {
      postLam[i] = unsmoothedPs[i]*(remainingProb*lambdas[i]);
      total += postLam[i];
      remainingProb *= 1-lambdas[i];
    }
  remainingProb = 1.0;
  for(i = Feature::total[Feature::whichInt] ; i > 0 ; i--)
    {
      int b = bucketVals[i];
      if(!procGSwitch)
	{
	  trData[i][b].c++;
	  continue;
	}
      trData[i][b].c +=remainingProb;
      double incr = 0;
      if(total*remainingProb > 0)
	incr = postLam[i]/total*remainingProb;
      assert(incr >= 0);
      trData[i][b].pm += incr;
      remainingProb *= 1-lambdas[i];
      total -= postLam[i];
      if(total < 0)
	{
	  //cerr << "Bad total " << total << endl;
	  total = 0;
	}
    }
}

void
gatherFfCounts(InputTree* tree, int inHpos)
{
  InputTrees& st = tree->subTrees();;
  InputTrees::iterator  subTreeIter= st.begin();
  InputTree  *subTree;
  int hpos = 0;
  if(st.size() != 0) hpos = headPosFromTree(tree);
  //cerr << hpos << *tree << endl;
  int pos = 0;
  for( ; subTreeIter != st.end() ; subTreeIter++ )
    {
      subTree = *subTreeIter;
      gatherFfCounts(subTree, pos==hpos ? 1 : 0);
      pos++;
    }
  //cerr << "g " << *tree << endl;
  curTree = tree;
  TreeHist treeh(tree, 0);
  treeh.pos = pos;
  treeh.hpos = hpos;
  const Term* lhsTerm = Term::get(tree->term());
  if(lhsTerm->terminal_p())
    {
      if(Feature::whichInt == TTCALC) callProcG(&treeh);
      return;
    }
  if(Feature::whichInt == HCALC || Feature::whichInt == UCALC)
    {
      if(!inHpos) callProcG(&treeh);
      return;
    }
  //if(procGSwitch) cerr << "gff " << *tree << endl;
  if(st.size() == 1 && st.front()->term() == tree->term()) return;
  subTreeIter = st.begin();
  int cVal;
  treeh.pos = -1;
  if(Feature::whichInt == LMCALC) callProcG(&treeh);
  if(Feature::whichInt == LCALC) callProcG(&treeh);
  pos = 0;
  for( ; subTreeIter != st.end() ; subTreeIter++)
    {
      treeh.pos = pos;
      if(pos == hpos && Feature::whichInt == MCALC) callProcG(&treeh);
      if(pos < hpos && Feature::whichInt == LCALC) callProcG(&treeh);
      if(pos > hpos && Feature::whichInt == RCALC) callProcG(&treeh);
      if(pos == hpos && Feature::whichInt == RUCALC) callProcG(&treeh);
      if(pos >= hpos && Feature::whichInt == RMCALC) callProcG(&treeh);
      if(pos <= hpos && Feature::whichInt == LMCALC) callProcG(&treeh);
      pos++;
    }
  //cerr << "gg " << *tree << endl;
  treeh.pos = pos;
  if(Feature::whichInt == RCALC) callProcG(&treeh);
  if(Feature::whichInt == RMCALC) callProcG(&treeh);
}

float curLogBases[20];
float totForFeat[20];
float prevMeanSq[20];
float curMeanSq[20];
int done[20] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

void
printCounts()
{
   int b,f;
   cerr << "\n";
   for(f = 2 ; f <= Feature::total[whichInt] ; f++)
     cerr << "\t" << curMeanSq[f];
   cerr << "\n";
   for(b = 1; b < 15 ; b++)
     {
       cerr << b ;
       for(f = 2; f <= Feature::total[whichInt] ; f++)
	 {
	   cerr << "\t" << trData[f][b].c;
	 }
       cerr << "\n";
     }
}

void
printLambdas(ostream& res)
{
   int b,f;
   if(Feature::isLM)
     for(f = 2 ; f <= Feature::total[whichInt] ; f++)
       res << "\t" << curLogBases[f];
   res << "\n";
   for(b = 1; b < 15 ; b++)
     {
       res << b ;
       for(f = 2; f <= Feature::total[whichInt] ; f++)
	 {
	   res << "\t";
	   float val = Feature::getLambda(whichInt, f, b);
	   if(val > .998) val = .998;  //nothing too close to 1; 
	   res << val;
	 }
       res << "\n";
     }
}

void
updateLambdas()
{
   int b,f;
   for(b = 1; b < 15 ; b++)
     {
       for(f = 2; f <= Feature::total[whichInt] ; f++)
	 {
	   float denom = trData[f][b].c;
	   float num = trData[f][b].pm;
	   float ans = num/denom;
	   if(denom == 0) ans = ((float)b/15.0);
	   else if(denom < 5 && trData[f][b+1].pm == 0 &&
		   (b == 0 ||  trData[f][b-1].pm == 0))
	     ans = 0;
	   Feature::setLambda(whichInt,f,b,ans);
	 }
     }
}

int
resetLogBases(int n)
{
  int i,j;
  int stillWorking = 0;
  for(i = 2 ; i <= Feature::total[whichInt] ; i++)
    {
      if(done[i]) continue;
      stillWorking = 1;
      curLogBases[i] += .1;
      curMeanSq[i] = 0;
      totForFeat[i] = 0;
      for(j = 1 ; j < 15 ; j++) totForFeat[i] += trData[i][j].c;
      for(j = 1 ; j < 15 ; j++)
	{
	  curMeanSq[i] += pow((trData[i][j].c - (totForFeat[i]/13.0)),2);
	}
      if((prevMeanSq[i] > 0 && curMeanSq[i] >= prevMeanSq[i])
	 || trData[i][14].c == 0)
	{
	  //cerr << "rlb " << i << " "
	  // << curLogBases[i]-.1 << " " << curMeanSq[i]
	  // << " " << trData[i][14].c << " " << prevMeanSq[i] << endl;
	  done[i] = 1;
	  curLogBases[i] -= 0.1;
	}
      prevMeanSq[i] = curMeanSq[i];
    }
  return stillWorking;
}

void
zeroData()
{
   int b,f;
   for(b = 0; b < 15 ; b++)
     {
       for(f = 1; f <= Feature::total[whichInt] ; f++)
	 {
	   trData[f][b].c = 0.0;
	   trData[f][b].pm = 0.0;
	 }
     }
}

void
pickLogBases(InputTree** trainingData, int sc)
{
  int i;
  for( i = 0 ; i < 20 ; i++)
    {
      curLogBases[i] = (whichInt == HCALC) ? 1.1 : 1.5 ;
    }
  for(i = 0 ; i < Feature::total[whichInt] ; i++) prevMeanSq[i] = 0;
  for(i = 0 ; ; i++)
    {
      goThroughSents(trainingData,sc);
      //printCounts();
      int contp = resetLogBases(i);
      if(!contp) break;
      zeroData();
    }
  zeroData();
}  

void
lamInit()
{
   int b,f;
   float factor = 1.0/15.0;
   int numFs = Feature::total[whichInt];
   for(b = 0; b < 15 ; b++)
     {
       for(f = 1; f <= numFs ; f++)
	 {
	   Feature* feat = Feature::fromInt(f, whichInt); 
	   int strtpos = feat->startPos;
	   /* we are trying b/15 */
	   float num = factor* b;
	   Feature::setLambda(whichInt,f,b,num);
	   trData[f][b].c = 0;
	   trData[f][b].pm = 0.0;
	 }
     }
   //printLambdas(cout);
}

int
main(int argc, char *argv[])
{
   struct rlimit 	core_limits;
   core_limits.rlim_cur = 0;
   core_limits.rlim_max = 0;
   setrlimit( RLIMIT_CORE, &core_limits );

   ECArgs args( argc, argv );
   assert(args.nargs() == 2);
   conditionedType = args.arg(0);
   cerr << "start trainRs: " << conditionedType << endl;

   ECString  path( args.arg( 1 ) );
   if(args.isset('M')) Feature::setLM();
   if(args.isset('L')) Term::Language = args.value('L');

   Term::init(path);

   readHeadInfo(path);
   Pst pst(path);
   if(Feature::isLM) ClassRule::readCRules(path);

   addSubFeatureFns();
   Feature::init(path, conditionedType); 

   whichInt = Feature::whichInt;
   int ceFunInt = Feature::conditionedFeatureInt[Feature::whichInt];
   Feature::conditionedEvent
     = SubFeature::Funs[ceFunInt];

   Feat::Usage = PARSE;
   ECString ftstr(path);
   ftstr += conditionedType;
   ftstr += ".g";
   ifstream fts(ftstr.c_str());
   if(!fts)
     {
       cerr << "Could not find " << ftstr << endl;
       assert(fts);
     }
   tRoot = new FeatureTree(fts); //puts it in root;

   cout.precision(3);
   cerr.precision(3);

   lamInit();

   InputTree* trainingData[1001];
   int usedCount = 0;
   sentenceCount = 0;
   for( ;  ; sentenceCount++)
     {
       if(sentenceCount%10000 == 1)
	 {
	   // cerr << conditionedType << ".tr "
	   //<< sentenceCount << endl;
	 }
       if(usedCount >= 1000) break;
       InputTree*     correct = new InputTree;  
       cin >> (*correct);
       if(correct->length() == 0) break;
       if(!cin) break;
       EcSPairs wtList;
       correct->make(wtList); 
       InputTree* par;
       par = correct;
       trainingData[usedCount++] = par;
     }
   if(Feature::isLM) pickLogBases(trainingData,sentenceCount);
   procGSwitch = true;
   for(pass = 0 ; pass < 10 ; pass++)
     {
       if(pass%2 == 1) cout << "Pass " << pass << endl;
       goThroughSents(trainingData, sentenceCount);
       updateLambdas();
       //printLambdas(cout);
       zeroData();
     }
   ECString resS(path);
   resS += conditionedType;
   resS += ".lambdas";
   ofstream res(resS.c_str());
   res.precision(3);
   printLambdas(res);
   printLambdas(cout);
   cout << "Total params = " << FeatureTree::totParams << endl;
   cout << "Done: " << (int)sbrk(0) << endl;
}


void
goThroughSents(InputTree* trainingData[1301], int sc)
{
  int sentenceCount;
  for(sentenceCount = 0 ; sentenceCount < sc ; sentenceCount++)
    {
      InputTree* par = trainingData[sentenceCount];
      //if(sentenceCount%50 == 1)
      //cerr << sentenceCount << endl;
      makeSent(par);
      gatherFfCounts(par,0);
      if(whichInt == TTCALC)
	{
	  list<InputTree*> dummy2;
	  InputTree stopInputTree(par->finish(),par->finish(),
				  whichInt==TTCALC ? "" : "^^",
				  "STOP","",
				  dummy2,NULL,NULL);
	  stopInputTree.headTree() = &stopInputTree;
	  TreeHist treeh(&stopInputTree,0);
	  treeh.hpos = 0;
	  callProcG(&treeh);
	}
    }
}
