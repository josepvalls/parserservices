#!/bin/bash
cd "$(dirname "$0")"/CharniakServer
export CHARNIAK=$PWD/parser05May26fixed
perl charniak-server.pl
