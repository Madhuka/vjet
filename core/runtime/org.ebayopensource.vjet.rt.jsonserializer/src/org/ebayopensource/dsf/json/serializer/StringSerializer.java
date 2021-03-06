/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.json.serializer;

import java.io.UnsupportedEncodingException;


public class StringSerializer extends AbstractSerializer {
	private static Class[] s_serializableClasses =
		new Class[] {
			String.class,
			char.class,
			Character.class,
			byte[].class,
			char[].class };

	private static Class[] s_JSONClasses =
		new Class[] { String.class, Integer.class };

	public Class[] getSerializableClasses() {
		return s_serializableClasses;
	}
	public Class[] getJSONClasses() {
		return s_JSONClasses;
	}

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class clazz,
		Object jso)
		throws SerializationException {
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object jso)
		throws SerializationException {
		String val = jso instanceof String ? (String) jso : jso.toString();
		if (clazz == char.class) {
			return new Character(val.charAt(0));
		} else if (clazz == byte[].class) {
			try {
				return val.getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				throw new SerializationException("UnsupportedEncodingException");
			}	
		} else if (clazz == char[].class) {
			return val.toCharArray();
		} else {
			return val;
		}
	}
	
	public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		if (o instanceof Character) {
			return o.toString();
		} else if (o instanceof byte[]) {
			return new String((byte[]) o);//NOPMD
		} else if (o instanceof char[]) {
			return String.valueOf((char[]) o);
		} else {
			return o;
		}
	}

}
