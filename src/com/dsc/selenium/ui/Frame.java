/**
 * Copyright (c) (2016-2017),VSI and/or its affiliates.All rights reserved.
 * DSC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
**/
package com.dsc.selenium.ui;

import org.openqa.selenium.WebElement;

import com.dsc.selenium.Browser;


/**
 * @Author alex
 * @CreateTime Dec 30, 2014 10:41:11 PM
 * @Version 1.0
 * @Since 1.0
 */
public class Frame extends UIObject
{

	/**
	 * @param wrapee
	 */
	public Frame(Browser browser,WebElement wrapee)
	{
		super(browser,wrapee);
	}

	/* (non-Javadoc)
	 * @see com.dsc.selenium.ui.UIObject#ensureSrcEndWith(java.lang.String)
	 */
	@Override
	public boolean srcEndsWith(String src)
	{
		return super.srcEndsWith(src);
	}

}
