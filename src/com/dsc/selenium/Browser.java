/**
 * gat * Copyright (c) (2010-2013),Deep Sky Century and/or its affiliates.All
 * rights
 * reserved.
 * DSC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
package com.dsc.selenium;

import static com.dsc.selenium.util.Util.wrap;
import static java.lang.String.format;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dsc.selenium.ui.UIObject;
import com.dsc.selenium.util.Util;
import com.dsc.util.Log;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.cropper.indent.BlurFilter;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Browser
{
	private static final int	DEFAULT_WAIT_SECONDS	= 3;
	private static int			REAL_WAIT_SECONDS		= DEFAULT_WAIT_SECONDS;
	private static final String	UTF_8					= "UTF-8";
	// NP get from configuration file
	private static final String	WHERE_CHROME_DRIVER		= "/usr/local/bin/chromedriver";

	private static final String	WHERE_FIREFOX_BIN		= "/usr/bin/firefox";
	private static final String	WHERE_FIREFOX_DRIVER	= "/usr/local/bin/geckodriver";

	public static DesiredCapabilities cap(DesiredCapabilities caps)
	{
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);

		caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
		return caps;
	}

	public static Browser chrome()
	{
		System.setProperty("webdriver.chrome.driver", WHERE_CHROME_DRIVER);

		return new Browser(new ChromeDriver(cap(DesiredCapabilities.chrome())));
	}

	public static Browser firfox()
	{
		// System.setProperty("webdriver.firefox.driver", WHERE_FIREFOX_DRIVER);
		// System.setProperty("webdriver.firefox.bin", WHERE_FIREFOX_BIN);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		return new Browser(new FirefoxDriver(capabilities));
	}
	// public static Browser firfox()
	// {
	// System.setProperty("webdriver.firefox.driver", WHERE_FIREFOX_DRIVER);
	// return new Browser(new FirefoxDriver(DesiredCapabilities.firefox()));
	// }

	public static Browser ie()
	{
		return new Browser(new InternetExplorerDriver());
	}

	public static Browser safari()
	{
		return new Browser(new SafariDriver());
	}

	/**
	 * @return
	 */
	private static int defaultWaitSeconds()
	{
		return REAL_WAIT_SECONDS;
	}

	WebDriver driver;

	protected Browser(WebDriver driver)
	{
		this.driver = driver;
	}

	public void close()
	{
		driver.quit();
	}

	public void deleteAllCookies()
	{
		manage().deleteAllCookies();
	}

	public void deleteCookie(String key)
	{
		manage().deleteCookieNamed(key);
	}

	public boolean ensureNotPresented(String id)
	{
		if (doFindElemById(id) != null)
		{
			throw new IllegalStateException(String.format("'%s' shouldn't be presented,but it is", id));
		}

		return true;
	}

	public WebElement findDivByText(String text)
	{

		return findDivByText("//div[contains(text(),'" + text + "')]");
	}

	public WebElement findElemById(String id)
	{
		if (id == null || id == "")
		{
			throw new IllegalArgumentException("id mustn't be null");
		}

		try
		{
			waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		} catch (TimeoutException e)
		{
			throw new NoSuchElementException("Can't find the element with ----------------id:---------------" + id, e);
		}

		return doFindElemById(id);
	}

	public WebElement findElemByTag(String tag)
	{
		if (tag == null || tag == "")
		{
			throw new IllegalArgumentException("tag name mustn't be null");
		}

		try
		{
			waitUntil(ExpectedConditions.visibilityOfElementLocated(By.tagName(tag)));
		} catch (TimeoutException e)
		{
			throw new NoSuchElementException("Can't find the element with ----------------tagName:---------------" + tag, e);
		}

		driver.findElement(By.tagName(tag)).getSize();

		return driver.findElement(By.tagName(tag));
	}

	public WebElement findElemByXpath(String xpath)
	{
		if (xpath == null || xpath == "")
		{
			throw new IllegalArgumentException("xpath mustn't be null");
		}

		try
		{
			waitUntil(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		} catch (TimeoutException e)
		{
			throw new NoSuchElementException("Can't find the element with ----------------xpath:---------------" + xpath, e);
		}

		return driver.findElement(By.xpath(xpath));
	}

	public String getCookie(String key)
	{
		Util.requireNotNullOrEmpty(key, "cookie key");

		for (Cookie ck : driver.manage().getCookies())
		{
			if (ck.getName().equals(key))
			{
				try
				{
					return URLDecoder.decode(ck.getValue(), UTF_8);
				} catch (UnsupportedEncodingException e)
				{
					throw new IllegalStateException(
							String.format("'%s''s cookie value:'%s' can't be decoded as %s", key, ck.getValue(), UTF_8), e);
				}
			}
		}

		return null;
	}

	// a method to allow retrieving our driver instance
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * @param id
	 * @return
	 */
	public UIObject getUIObjectById(String id)
	{
		return new UIObject(this, findElemById(id));
	}

	/**
	 * @param target
	 * @deprecated use {@link #moveToElement(WebElement)} instead
	 */
	@Deprecated
	public WebElement mouseMoveTo(WebElement target)
	{
		return moveToElement(target);
	}

	/**
	 * @param target
	 * @param x
	 * @param y
	 * @deprecated use {@link #moveToElement(WebElement, int , int )} instead
	 */
	@Deprecated
	public void mouseMoveTo(WebElement target, int x, int y)
	{
		moveToElement(target, x, y);
	}

	/**
	 * @param target
	 */
	public WebElement moveToElement(WebElement target)
	{
		new Actions(driver).moveToElement(target, 0, 0).perform();

		return target;
	}

	/**
	 * @param target
	 * @param x
	 * @param y
	 */
	public void moveToElement(WebElement target, int x, int y)
	{
		new Actions(driver).moveToElement(target, x, y).perform();
	}

	public void open(String url)
	{
		driver.get(url);
		driver.manage().window().maximize();
	}

	public void refresh()
	{
		driver.navigate().refresh();
	}

	public void resetWaitSeconds()
	{
		REAL_WAIT_SECONDS = DEFAULT_WAIT_SECONDS;
	}

	// a method to obtain screenshots
	public File screenshotOf(WebElement elem)
	{
		if (driver instanceof ChromeDriver)
		{
			return doTakeScreenshotByAShot(elem);
		}

		return takeScreenshotOf(elem);

	}

	public void setCookie(String key, String value)
	{
		setCookie(key, value, "/");
	}

	public void setCookie(String key, String value, String path)
	{
		manage().addCookie(new Cookie(key, value, path));
	}

	public void setWaitSeconds(int defaultWaitSeconds)
	{
		REAL_WAIT_SECONDS = defaultWaitSeconds;
	}

	// a method to obtain screenshots
	public File takeScreenshot()
	{
		// take a screenshot
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}

	public <T> T waitUntil(ExpectedCondition<T> isTrue) throws TimeoutException
	{
		return new WebDriverWait(driver, Browser.defaultWaitSeconds()).until(isTrue);
	}

	public Window window()
	{
		return driver.manage().window();
	}

	Options manage()
	{
		return driver.manage();
	}

	/**
	 * @param id
	 * @return
	 */
	private WebElement doFindElemById(String id)
	{
		try
		{
			return driver.findElement(By.id(id));
		} catch (NoSuchElementException e)
		{
			Log.info(wrap(format("element '%s' not presented in page: '%s'", id, driver.getCurrentUrl())));
			return null;
		}
	}

	/**
	 * @param elem
	 * @return
	 */
	private File doTakeScreenshotByAShot(WebElement elem)
	{
		try
		{
			Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider())
					.imageCropper(new IndentCropper().addIndentFilter(new BlurFilter()))
					.shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver, elem);

			File tempFile = File.createTempFile(screenshotUrl(), ".png");

			ImageIO.write(screenshot.getImage(), "png", tempFile);

			tempFile.deleteOnExit();

			return tempFile;
		} catch (Exception e)
		{
			throw new RuntimeException(wrap("take screenshot failed"), e.getCause());
		}
	}

	/**
	 * @return
	 */
	private String screenshotUrl()
	{
		return "screenshot-" + new Date().getTime() + new Random().nextLong();
	}

	private File takeScreenshotOf(WebElement elem)
	{
		File screenshot = takeScreenshot();

		// Get the location of element on the page
		Point point = elem.getLocation();

		// Get width and height of the element
		int elemWidth = elem.getSize().getWidth();
		int elemHeight = elem.getSize().getHeight();

		try
		{
			BufferedImage fullImg = ImageIO.read(screenshot);
			// Crop the entire page screenshot to get only element screenshot
			BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), elemWidth, elemHeight);
			ImageIO.write(eleScreenshot, "png", screenshot);
		} catch (IOException e)
		{
			throw new RuntimeException(wrap("take screenshot IO failed"), e.getCause());
		}

		return screenshot;
	}
}