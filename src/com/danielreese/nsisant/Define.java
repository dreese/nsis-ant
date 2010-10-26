/*
 * NSIS Ant Task
 * Copyright (c) 2004 Wayne Grant. Relicensed with permission.
 * Copyright (c) 2005-2010 Daniel Reese
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.danielreese.nsisant;

/**
 * Represents an NSIS definition in the context of the build script.
 */
public class Define
{
	// Fields
	/** Stores the name attribute */
	private String m_sName;

	/** Stores the value attribute */
	private String m_sValue;

	// Methods
	/**
	 * Set the name attribute.
	 * 
	 * @param sName the name attribute
	 */
	public void setName(String sName)
	{
		m_sName = sName;
	}

	/**
	 * Get the name attribute. Deliberately package private.
	 * 
	 * @return the name attribute
	 */
	String getName()
	{
		return m_sName;
	}

	/**
	 * Set the value attribute.
	 * 
	 * @param sValue the value attribute
	 */
	public void setValue(String sValue)
	{
		m_sValue = sValue;
	}

	/**
	 * Get the value attribute.
	 * 
	 * @return the value attribute
	 */
	String getValue()
	{
		return m_sValue;
	}
}
