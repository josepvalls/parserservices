#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
sock = socket.socket()
sock.connect(('localhost',4449))
sock.send('Hello my dearest Esther! How are you?\n')
resp = ''
while True:
  #print resp
  d = sock.recv(1)
  print d,ord(d)
  if not d or resp[-2:]=='\n\n':
   break
  resp += d
sock.send('The green cat is big but the small rat is cleverer. This is my résumé.\n')
resp = ''
while True:
  d = sock.recv(1)
  if not d or resp[-2:]=='\n\n':
   break
  resp += d
print resp