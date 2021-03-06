package com.dsc.selenium.ui.ext;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
public interface Container  {
	public void init(WebElement wrapeeElement);
	public ExpectedCondition<Boolean> isReady(WebDriverWait wait); 
}
