/*
 * $Header$
 * $Revision: 1128 $
 * $Date: 2006-02-05 13:49:04 -0800 (Sun, 05 Feb 2006) $
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Jaxen Project and was originally
 * created by bob mcwhirter <bob@werken.com> and
 * James Strachan <jstrachan@apache.org>.  For more information on the
 * Jaxen Project, please see <http://www.jaxen.org/>.
 *
 * $Id: DefaultXPathHandler.java 1128 2006-02-05 21:49:04Z elharo $
 */




package org.jaxen.saxpath.helpers;

import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathHandler;

/**

   Default base class for SAXPath event handlers. 

   This class is available as a convenience base class for SAXPath
   applications: it provides a default do-nothing implementation 
   for all of the callbacks in the core SAXPath handler class, {@link
   org.jaxen.saxpath.XPathHandler}.

   Application writers can extend this class when they need to
   implement only part of the <code>XPathHandler</code>
   interface. Parser writers can instantiate
   this class to provide default handlers when the application has not
   supplied its own. */

public class DefaultXPathHandler implements XPathHandler
{

    @Override
	public void startXPath() throws SAXPathException
    {
    }

    @Override
	public void endXPath() throws SAXPathException
    {
    }

    @Override
	public void startPathExpr() throws SAXPathException
    {
    }

    @Override
	public void endPathExpr() throws SAXPathException
    {
    }

    @Override
	public void startAbsoluteLocationPath() throws SAXPathException
    {
    }

    @Override
	public void endAbsoluteLocationPath() throws SAXPathException
    {
    }

    @Override
	public void startRelativeLocationPath() throws SAXPathException
    {
    }

    @Override
	public void endRelativeLocationPath() throws SAXPathException
    {
    }

    @Override
	public void startNameStep(int axis,
                              String prefix,
                              String localName) throws SAXPathException
    {
    }

    @Override
	public void endNameStep() throws SAXPathException
    {
    }

    @Override
	public void startTextNodeStep(int axis) throws SAXPathException
    {
    }
    @Override
	public void endTextNodeStep() throws SAXPathException
    {
    }

    @Override
	public void startCommentNodeStep(int axis) throws SAXPathException
    {
    }

    @Override
	public void endCommentNodeStep() throws SAXPathException
    {
    }

    @Override
	public void startAllNodeStep(int axis) throws SAXPathException
    {
    }

    @Override
	public void endAllNodeStep() throws SAXPathException
    {
    }

    @Override
	public void startProcessingInstructionNodeStep(int axis,
                                                   String name) throws SAXPathException
    {
    }
    @Override
	public void endProcessingInstructionNodeStep() throws SAXPathException
    {
    }

    @Override
	public void startPredicate() throws SAXPathException
    {
    }

    @Override
	public void endPredicate() throws SAXPathException
    {
    }

    @Override
	public void startFilterExpr() throws SAXPathException
    {
    }

    @Override
	public void endFilterExpr() throws SAXPathException
    {
    }

    @Override
	public void startOrExpr() throws SAXPathException
    {
    }

    @Override
	public void endOrExpr(boolean create) throws SAXPathException
    {
    }

    @Override
	public void startAndExpr() throws SAXPathException
    {
    }

    @Override
	public void endAndExpr(boolean create) throws SAXPathException
    {
    }

    @Override
	public void startEqualityExpr() throws SAXPathException
    {
    }

    @Override
	public void endEqualityExpr(int operator) throws SAXPathException
    {
    }

    @Override
	public void startRelationalExpr() throws SAXPathException
    {
    }

    @Override
	public void endRelationalExpr(int operator) throws SAXPathException
    {
    }

    @Override
	public void startAdditiveExpr() throws SAXPathException
    {
    }

    @Override
	public void endAdditiveExpr(int operator) throws SAXPathException
    {
    }

    @Override
	public void startMultiplicativeExpr() throws SAXPathException
    {
    }

    @Override
	public void endMultiplicativeExpr(int operator) throws SAXPathException
    {
    }

    @Override
	public void startUnaryExpr() throws SAXPathException
    {
    }

    @Override
	public void endUnaryExpr(int operator) throws SAXPathException
    {
    }

    @Override
	public void startUnionExpr() throws SAXPathException
    {
    }

    @Override
	public void endUnionExpr(boolean create) throws SAXPathException
    {
    }

    @Override
	public void number(int number) throws SAXPathException
    {
    }

    @Override
	public void number(double number) throws SAXPathException
    {
    }

    @Override
	public void literal(String literal) throws SAXPathException
    {
    }

    @Override
	public void variableReference(String prefix,
                                  String variableName) throws SAXPathException
    {
    }

    @Override
	public void startFunction(String prefix,
                              String functionName) throws SAXPathException
    {
    }

    @Override
	public void endFunction() throws SAXPathException
    {
    }

}
