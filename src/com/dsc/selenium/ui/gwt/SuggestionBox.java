/**
 * Copyright (c) (2016-2017),VSI and/or its affiliates.All rights
 * reserved.
 * DSC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
package com.dsc.selenium.ui.gwt;

import org.openqa.selenium.WebElement;

import com.dsc.selenium.Browser;

/**
 * The Class SuggestionBox.
 *
 * @Author alex
 * @CreateTime Aug 26, 2014 7:56:55 PM
 * @Version 1.0
 * @Since v1.0
 */
public class SuggestionBox extends RETextBox
{
	public SuggestionBox(Browser browser,WebElement wrapee)
	{
		super(browser,wrapee);
	}
}