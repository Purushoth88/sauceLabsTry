package com.test.nicu;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Ignore
public class TestSeleniumFeatures {
	private WebDriver			driver;
	private boolean				acceptNextAlert		= true;
	private final StringBuffer	verificationErrors	= new StringBuffer();

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public static int getNumberOfElements(final WebDriver webDriver, final String element) {
		return webDriver.findElements(By.tagName(element)).size();
	}

	@Test
	public void testTemp() throws Exception {
		driver.get("https://api.clickandbuy-d2.com/webservices/automation/pay_1_1_0/LDVYSTJgeKhoEXloJ6b*U5ityFmIKFEWyE6-D8n7sdCjJ9wde5tXFjkjgeEVP31AdCxQCWecF80oPvXW*snXtrE5eMYiw2*3yYTCmCth5a*lPdmrOtnSKgIRJMAGBxTVi2stCBG2bf75d6HcXZA0X8UQ8e4DbBSqKk2ZBIuGsH*nBnCO4flsj6nuObHZE4nIWhbvBpyvzy6aEcwcnWC*v*YMJhcxGh8-eZCamP-M2L7GfeAwbEkl3LBWN8B4f0qZYIathizm350ABfxF9t5aI-cbNLnzh10grL48JbnaMItmeagsAna9*Cq2HWndcMAF3ldAPYARJqSlMgOpRHcWIscKQ7TwvpBFmbQmIQ2TWBBNE3E3XctYoMIQFnsjc7FU");
		driver.findElement(By.id("login_email")).clear();
		driver.findElement(By.id("login_email")).sendKeys("thijs@consumer.com");
		driver.findElement(By.id("login_password")).clear();
		driver.findElement(By.id("login_password")).sendKeys("123@test");
		driver.findElement(By.id("login_submit")).click();
		// final int i = getNumberOfElements(driver, "iFrame");
		// final WebElement ifram = driver.findElement(By.id("ifrmPr"));
		// driver.switchTo().frame(1);
		System.out.println(driver.getPageSource());
		// System.out.println(ifram.getText());

		boolean iframeFullyLoaded = false;
		while (!iframeFullyLoaded) {
			driver.switchTo().frame(1);
			iframeFullyLoaded = verifyTextPresent("saved");
			System.out.println(driver.getPageSource());
			driver.switchTo().defaultContent();
		}

		/*
		 * final WebDriverWait wait2 = new WebDriverWait(driver, 15); wait2.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifrmPr"));
		 */
		driver.findElement(By.id("payment-category-selection")).click();
		driver.findElement(By.xpath("//div[@id='payment-category-selection']/div/div")).click();
		/*
		 * driver.findElement(By.xpath("//form[@id='form-roundabout-creditcard']/ul/li[2]/div[2]")).click(); driver.findElement(By.xpath("(//input[@name='roundaboutcvv'])[2]")).clear();
		 * driver.findElement(By.xpath("(//input[@name='roundaboutcvv'])[2]")).sendKeys("123"); driver.findElement(By.xpath("(//button[@type='button'])[3]")).click(); // ERROR: Caught exception [ERROR: Unsupported command [selectFrame |
		 * relative=up | ]] driver.findElement(By.id("paynow")).click();
		 */
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		final String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (final NoSuchElementException e) {
			return false;
		}
	}

	public boolean verifyTextPresent(final String text) {
		return driver.findElement(By.tagName("body")).getText().contains(text);
	}

	private String closeAlertAndGetItsText() {
		try {
			final Alert alert = driver.switchTo().alert();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alert.getText();
		} finally {
			acceptNextAlert = true;
		}
	}
}
