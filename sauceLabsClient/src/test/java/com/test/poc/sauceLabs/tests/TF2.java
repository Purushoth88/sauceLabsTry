package com.test.poc.sauceLabs.tests;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TF2 {
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

		Boolean wdw = false;

		while (!wdw) {
			System.out.println("waiting...");
			wdw = new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(final WebDriver driver) {
					final List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
					final Integer size = iframes.size();
					System.out.println("number of current iframes [" + size + "]");
					if (size == 3) {
						for (final WebElement iframe : iframes) {
							System.out.println("iframe ID: [" + iframe.getAttribute("id") + "]");
						}

					}
					return (size == 3);
				}
			});
		}

		// ERROR: Caught exception [ERROR: Unsupported command [selectFrame | easyXDM_default7154_provider | ]]

		// WebElement iframe = driver.findElement(By.id("ifrmPr"));

		// WebElement iframe = driver.findElement(By.cssSelector("*[id ^='easyXDM_default']"));
		// System.out.println("TAG NAME: [" + iframe.getTagName() + "]");
		// System.out.println("iframe containing text [" + iframe.getText() + "]");
		// driver.switchTo().frame(iframe);

		// driver.findElement(By.tagName("body")).getText().contains("");
		driver.switchTo().frame(0);
		// System.out.println(driver.getPageSource());
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// System.out.println(driver.getPageSource());
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		System.out.println("iframe success changed");
		// System.out.println(driver.getPageSource());
		WebElement element1 = null;
		while (element1 == null) {
			element1 = isElementPresent(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[2]"));
			System.out.println("1 not present");
		}
		System.out.println("1 has arrived");
		element1.click();
		System.out.println("1 clicked");
		WebElement elemenr2 = null;
		final boolean isDesplayed = false;
		while (elemenr2 == null || !isDesplayed) {
			System.out.println("2 not present");
			elemenr2 = isElementPresent(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input"));
		}
		System.out.println("2 not present");
		// elemenr2 = isElementPresent(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input"));
		System.out.println("2 has arrived");
		elemenr2.click();
		System.out.println("2 clicked");
		// elemenr2.clear();
		System.out.println("3 arrived");
		elemenr2.sendKeys("123");
		WebElement elemenr3 = null;
		while (elemenr3 == null) {
			elemenr3 = isElementPresent(By.xpath("(.//*[@id='form-roundabout-creditcard']/ul/li[2]/div[4]/div[2]/button[1]"));
			System.out.println("4 not present");
		}
		System.out.println("4 present");
		elemenr3.click();
		System.out.println("4 clicked");
		// ERROR: Caught exception [ERROR: Unsupported command [selectFrame | relative=up | ]]
		driver.findElement(By.id("paynow")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		final String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private WebElement isElementPresent(final By by) {
		try {
			final WebElement element = driver.findElement(by);
			return element;
		} catch (final NoSuchElementException e) {
			return null;
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