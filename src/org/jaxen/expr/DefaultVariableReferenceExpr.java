/*
 * $Header$
 * $Revision: 1367 $
 * $Date: 2012-05-05 12:31:33 -0400 (Sat, 05 May 2012) $
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
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
 * $Id: DefaultVariableReferenceExpr.java 1367 2012-05-05 16:31:33Z elharo $
 */

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.UnresolvableException;

class DefaultVariableReferenceExpr extends DefaultExpr implements VariableReferenceExpr
{
    /**
     * 
     */
    private static final long serialVersionUID = 8832095437149358674L;
    private String prefix;
    private String localName;

    DefaultVariableReferenceExpr(String prefix,
                                        String variableName)
    {
        this.prefix       = prefix;
        this.localName = variableName;
    }

    @Override
	public String getPrefix()
    {
        return this.prefix;
    }

    @Override
	public String getVariableName()
    {
        return this.localName;
    }

    @Override
	public String toString()
    {
        return "[(DefaultVariableReferenceExpr): " + getQName() + "]";
    }
    
    private String getQName() {
        if ( "".equals(prefix) )
        {
            return localName;
        }
        return prefix + ":" + localName;
    }

    @Override
	public String getText()
    {
        return "$" + getQName();
    }

    @Override
	public Object evaluate(Context context) throws UnresolvableException
    {
        String prefix = getPrefix();
        String namespaceURI = null;
        // default namespace is not used within XPath expressions
        if (prefix != null && !"".equals(prefix)) {
            namespaceURI = context.translateNamespacePrefixToUri( prefix );
        }
        
        return context.getVariableValue( namespaceURI,
                                         prefix,
                                     localName );
}
    
}
