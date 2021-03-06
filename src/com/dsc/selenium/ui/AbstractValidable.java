/**
 * Copyright (c) (2016-2017),VSI and/or its affiliates.All rights reserved.
 * DSC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
**/
package com.dsc.selenium.ui;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.dsc.selenium.Browser;

/**
 * @Author alex
 * @CreateTime Dec 31, 2014 3:09:52 PM
 * @Version 1.0
 * @Since 1.0
 */
public class AbstractValidable extends UIObject implements Validable
{

	/**
	 * @param wrapee
	 */
	public AbstractValidable(Browser browser,WebElement wrapee)
	{
		super(browser,wrapee);
	}

	/* (non-Javadoc)
	 * @see com.dsc.selenium.ui.Validable#ensureValidationAttrsSet()
	 */
	@Override
	public void ensureValidationAttrsSet()
	{
		ensureAttrNotNullOrEmpty(PATTERN);

		if(isNotUniversalPatten()){
			ensureAttrNotNullOrEmpty(WARNING);
		}
	}

	/**
	 * @return
	 */
	private boolean isNotUniversalPatten()
	{
		//NP bad assumption
		//refers to {@link com.dsc.gwt.widget.ValidatorImpl#universalPatternAndWarning()}
		//if a pattern is a upper case string,it should be a universal pattern
		return !StringUtils.isAllUpperCase(attr(PATTERN).replace("_", ""));
	}
}
