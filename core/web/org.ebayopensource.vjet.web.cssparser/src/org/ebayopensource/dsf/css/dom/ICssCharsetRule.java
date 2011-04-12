/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.css.dom;

import org.w3c.dom.DOMException;

/**
 *  The <code>CSSCharsetRule</code> interface represents a @charset rule in a 
 * CSS style sheet. The value of the <code>encoding</code> attribute does 
 * not affect the encoding of text data in the DOM objects; this encoding is 
 * always UTF-16. After a stylesheet is loaded, the value of the 
 * <code>encoding</code> attribute is the value found in the 
 * <code>@charset</code> rule. If there was no <code>@charset</code> in the 
 * original document, then no <code>CSSCharsetRule</code> is created. The 
 * value of the <code>encoding</code> attribute may also be used as a hint 
 * for the encoding used on serialization of the style sheet. 
 * <p> The value of the @charset rule (and therefore of the 
 * <code>CSSCharsetRule</code>) may not correspond to the encoding the 
 * document actually came in; character encoding information e.g. in an HTTP 
 * header, has priority (see CSS document representation) but this is not 
 * reflected in the <code>CSSCharsetRule</code>. 
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface ICssCharsetRule extends ICssRule {
    /**
     *  The encoding information used in this <code>@charset</code> rule. 
     * @exception DOMException
     *   SYNTAX_ERR: Raised if the specified encoding value has a syntax error 
     *   and is unparsable.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this encoding rule is 
     *   readonly.
     */
    String getEncoding();
    ICssCharsetRule setEncoding(String encoding) throws DOMException;
}