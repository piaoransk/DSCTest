/**
 * Copyright (c) (2016-2017),VSI and/or its affiliates.All rights
 * reserved.
 * DSC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
package com.dsc.selenium.ui;

import static com.dsc.selenium.util.Util.nullIfEmpty;
import static com.dsc.selenium.util.Util.wrap;
import static java.lang.String.format;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.dsc.selenium.Browser;
import com.dsc.selenium.ui.gwt.Consts;
import com.dsc.selenium.util.Util;
import com.dsc.util.Log;
import com.google.common.collect.Lists;

// LP BLOG exception or boolean+log
public class UIObject
{
	protected static final String	VALUE		= "value";
	private static final String		CLASS		= "class";
	private static final String		ERROR_CSS	= "error";
	private static final String		ID			= "id";

	// String PATTERN="pattern";
	private static final String		SRC			= "src";
	private static final String		TITLE		= "title";
	private static final String		WARN_CSS	= "warning";

	protected String				annotatedId;

	protected Browser				browser;
	protected WebElement			wrapee;

	/**
	 * @param browser
	 * @param wrapee
	 */
	public UIObject(Browser browser, WebElement wrapee)
	{
		this.wrapee = wrapee;
		this.browser = browser;
	}

	/**
	 * NP not elegant
	 *
	 * @return
	 */
	public List<UIObject> children()
	{
		List<WebElement> rawList = wrapee.findElements(By.xpath(".//*"));

		List<UIObject> list = Lists.newArrayList();

		// boxing to to UIObject
		for (WebElement we : rawList)
		{
			list.add(new UIObject(browser, we));
		}

		return list;
	}

	public void click()
	{
		wrapee.click();
	}

	public boolean containsCss(String css)
	{
		Log.info(wrap(format("checks whether %s's css:'%s' contains '%s' in #containsCss()", id(), css(), css)));
		return css().contains(css);
	}

	public void doubleClick()
	{
		new Actions(browser.getDriver()).doubleClick(wrapee).perform();
	}

	/**
	 * @return
	 */
	public WebElement element()
	{
		return wrapee;
	}

	public void ensureAvailable()
	{
		try
		{
			waitUntil(ExpectedConditions.visibilityOf(wrapee));
		} catch (Exception e)
		{
			throw new RuntimeException(msgOfNotPresentedInPage(), e.getCause());
		}
	}

	public void ensureHidden()
	{
		browser.ensureNotPresented(getAnnotatedId());
	}

	public void ensureInnerHtmlIs(String html)
	{
		if (!html.equals(html()))
		{
			throw new IllegalStateException(wrap(format("%s's inner html isn't '%s' but '%s'", id(), html, html())));
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public void ensureTitleIs(String title)
	{
		if (!title.equals(title()))
		{
			throw new IllegalStateException(wrap(format("%s's title isn't '%s' but '%s'", id(), title, title())));
		}
	}

	/**
	 * @return the annotatedId
	 */
	public String getAnnotatedId()
	{
		return annotatedId;
	}

	/**
	 * @return
	 */
	public WebElement getWrapped()
	{
		return wrapee;
	}

	public int height()
	{
		return wrapee.getSize().getHeight();
	}

	public String id()
	{
		return id(wrapee);
	}

	public String id(WebElement elem)
	{
		return elem.getAttribute(ID);
	}

	/**
	 * @return
	 */
	public String innerText()
	{
		return stringAttr("innerText");
	}

	public boolean isHidden()
	{
		Log.info(wrap("calls #containsCss(HIDE) in #isHidden()"));
		return containsCss(Consts.HIDE);
	}

	public boolean isShowingError()
	{
		Log.info(wrap("calls #containsCss(ERROR_CSS) in #isShowingError()"));
		return containsCss(ERROR_CSS);
	}

	public boolean isShowingWarning()
	{
		Log.info(wrap("calls #containsCss(WARN_CSS) in #isShowingWarning()"));
		return containsCss(WARN_CSS);
	}

	public boolean isVisible()
	{
		return wrapee.isDisplayed();
	}

	public int left()
	{
		return wrapee.getLocation().getX();
	}

	public void mouseOut()
	{
		moveOffset(-1, -1);
	}

	/**
	 *
	 */
	public void mouseOver()
	{
		moveOffset(0, 0);
	}

	public boolean notHasCss(String css)
	{
		Log.info("calls !#containsCss() in #notContainsCss()");
		return !containsCss(css);
	}

	/**
	 * @param annotatedId
	 *            the annotatedId to set
	 */
	public void setAnnotatedId(String annotatedId)
	{
		this.annotatedId = annotatedId;
	}

	public boolean textIs(String text)
	{
		Log.info(wrap(format("check whether %s's actual text: '%s' equals to expected text: '%s'", id(), text(), text)));
		return text().equals(text);
	}

	public String title()
	{
		return stringAttr(TITLE);
	}

	public int top()
	{
		return wrapee.getLocation().getY();
	}

	public void waitForSeconds(int seconds)
	{
		Util.sleep(seconds, annotatedId);
	}

	public int width()
	{
		return wrapee.getSize().getWidth();
	}

	protected boolean asyncTextPresented(String text)
	{
		Log.info(wrap(format("checks whether async text: '%s' is presented in #asyncTextPresented()", text)));
		// NP tell actual text
		return waitUntil(ExpectedConditions.textToBePresentInElement(wrapee, text));
	}

	protected String attr(String attrName)
	{
		return wrapee.getAttribute(attrName);
	}

	protected UIObject child(int index)
	{
		return children().get(index);
	}

	protected UIObject child(String id)
	{
		for (UIObject child : children())
		{
			if (id.equals(child.id()))
			{
				return child;
			}
		}
		return null;
	}

	protected WebElement childElem(int index)
	{
		return child(index).wrapee;
	}

	protected WebElement childElem(String id)
	{
		return child(id).wrapee;
	}

	protected WebElement childElement(int index)
	{
		return child(index).element();
	}

	/**
	 * @return
	 */
	protected int childrenCount()
	{
		return children().size();
	}

	/**
	 * @param text
	 * @return
	 */
	protected boolean containsText(String text)
	{
		Log.info(wrap(format("checks whether %s's text:'%s' contains '%s' in #containsText()", id(), text(), text)));
		return text().contains(text);
	}

	protected String css()
	{
		return stringAttr(CLASS);
	}

	protected boolean cssHasChangedFrom(final String css)
	{
		Log.info(wrap(format("chech whether %s's css has changed to '%s' from '%s'", id(), css, css())));
		return waitUntil(new ExpectedCondition<Boolean>()
		{
			@Override
			public Boolean apply(WebDriver browser)
			{
				if (!css.equals(css()))
				{
					Log.info(wrap(format("the css of %s has changed to '%s' from '%s'", id(), css, css())));
					return true;
				}
				Log.info(wrap(format("the css of %s is still '%s'", id(), css)));
				return null;
			}

			@Override
			public String toString()
			{
				return wrap(format("css has changed from '%s' to  '%s'", css, css()));
			}
		});
	}

	protected void dragAndDrop(int xOffset, int yOffset)
	{
		Actions slide = new Actions(browser.getDriver());
		slide.build();
		// slide.click(wrapee);
		slide.dragAndDropBy(wrapee, xOffset, yOffset);
		slide.perform();
		// slide.release();
	}

	protected void ensureAttrNotNullOrEmpty(String attr)
	{
		Util.requireNotNullOrEmpty(attr, "attribute name");

		if (stringAttr(attr) == null)
		{
			throw new IllegalStateException(wrap(format("%s's %s  mustn't be null", id(), attr)));
		}
	}

	protected boolean ensureInnerTextIs(String text)
	{
		if (!text.equals(text()))
		{
			throw new IllegalStateException(wrap(format("%s's inner text isn't '%s' but '%s'", id(), text, text())));
		}

		return true;
	}

	/**
	 * @param src
	 * @return
	 */
	protected void ensureSrcEndWith(String src)
	{
		if (!srcEndsWith(src))
		{
			throw new IllegalStateException(wrap(format("%s's src isn't end with '%s' but '%s'", id(), src, src())));
		}
	}

	protected boolean ensureTextEndsWith(String expected)
	{
		if (!text().endsWith(expected))
		{
			throw new IllegalStateException(wrap(
					format("Expect %s's text ends with '%s',but actual text '%s' doesn't end with is", id(), expected, text())));
		}

		return true;
	}

	protected boolean ensureTextIs(int text)
	{
		return ensureTextIs(Integer.toString(text));
	}

	protected boolean ensureTextIs(String expected)
	{
		if (!expected.equals(text()))
		{
			throw new IllegalStateException(wrap(format("Expect %s's text is '%s',but actual is '%s'", id(), expected, text())));
		}

		return true;
	}

	protected boolean ensureTextIsPartOf(String whole)
	{
		return whole.contains(text());
	}

	protected boolean ensureTextStartsWith(String expected)
	{
		if (!text().startsWith(expected))
		{
			throw new IllegalStateException(wrap(format(
					"Expect %s's text starts with '%s',but actual text:'%s' doesn't start with it", id(), expected, text())));
		}

		return true;
	}

	protected boolean isSyncErrorIndicated(final String text)
	{
		Log.info(wrap("#isSyncErrorIndicated() call #containsText() and #isShowingError()"));
		return containsText(text) && isShowingError();
	}

	// protected boolean isPresented(String id)
	// {
	// return browser.isPresented(id);
	// }

	protected boolean isSyncWarningIndicated(final String text)
	{
		Log.info(wrap("#isSyncErrorIndicated() call #containsText() and #isShowWarning()"));
		return containsText(text) && isShowingWarning();
	}

	/**
	 * @param name
	 */
	protected void setText(String text)
	{
		wrapee.sendKeys(text);
	}

	protected String src()
	{
		return stringAttr(SRC);
	}

	protected boolean srcEndsWith(String src)
	{
		Log.info(wrap(format("check whether %s's actual src: '%s' ends with: '%s'", id(), src(), src)));
		return src().endsWith(src);
	}

	/**
	 * @return inner text content or null
	 */
	protected String text()
	{
		if (doGetText() == null)
		{
			return waitUntil(textPresented());
		}

		return doGetText();
	}

	/**
	 * @return
	 */
	protected String textContent()
	{
		return stringAttr("textContent");
	}

	protected boolean textIsNot(String text)
	{
		Log.info(wrap(format("#textIsNot() calls !#textIs()")));
		return !textIs(text);
	}

	protected String value()
	{
		return stringAttr(VALUE);
	}

	protected <T> T waitUntil(ExpectedCondition<T> isTrue)
	{
		return browser.waitUntil(isTrue);
	}

	/**
	 * @return
	 */
	private String doGetText()
	{
		String text = nullIfEmpty(wrapee.getText());

		if (text == null)
		{
			text = innerText();
		}

		if (text == null)
		{
			// for TextArea
			text = textContent();
		}

		if (text == null)
		{
			text = value();
		}

		// Log.info(wrap(format("text is -------------------------%s", id())));
		return text;
	}

	/**
	 * @return
	 */
	private String html()
	{
		return stringAttr("innerHTML");
	}

	/**
	 *
	 */
	private void moveOffset(int x, int y)
	{
		browser.mouseMoveTo(wrapee, x, y);
	}

	private String msgOfNotPresentedInPage()
	{
		return format(
				"no element has id-------------------------'%s'-------------------------in the page,\n"
						+ wrap("Possible reasons:\n") + "① Expected element doesn't existed in the page\n"
						+ "② Element existed,but no id setted\n"
						+ "③ Element existed,but the id isn't the '%s',ensuring @Find(id='actual id of expected element') will fix this problem\n",
						annotatedId, annotatedId);
	}

	private String stringAttr(String attr)
	{
		return nullIfEmpty(attr(attr));
	}

	private ExpectedCondition<String> textPresented()
	{
		return new ExpectedCondition<String>()
		{
			@Override
			public String apply(WebDriver driver)
			{
				Log.info(wrap(format("wait text is presented in-------------------------%s", id())));

				try
				{
					return doGetText();
				} catch (StaleElementReferenceException e)
				{
					return null;
				}
			}

			@Override
			public String toString()
			{
				return String.format("text to be present in element %s", id());
			}
		};
	}
}