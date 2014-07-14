/*
 * Copyright 1997, Brown University, Providence, RI.
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

#ifndef UTILS_H 
#define UTILS_H

#include <stdlib.h>
#include <fstream>
#include <list>
#include "ECString.h"
#include <vector>

void error(const char *s);
void warn(const char *s);
double ran();
char* toLower(const char* str, char* temp);
ECString intToString(int i);
typedef vector<ECString> ECStrings;
typedef ECStrings::iterator ECStringsIter;
bool vECfind(ECString st, ECStrings& sts);
enum DGStatus  { OK, NOT_OK };

void ignoreComment(ifstream& input);
ECString lastCharacter(const ECString& s);
ECString firstCharacter(const ECString& s);

#endif /* ! UTILS_H */

