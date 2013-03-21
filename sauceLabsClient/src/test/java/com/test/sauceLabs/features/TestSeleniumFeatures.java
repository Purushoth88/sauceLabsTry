package com.test.sauceLabs.features;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.saucelabs.junit.Parallelized;
import com.test.poc.core.parent.ParametrizedParentTest;

/**
 * Paralellization tests.
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
@RunWith(Parallelized.class)
public class TestSeleniumFeatures extends ParametrizedParentTest {
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

	/**
	 * Junit Parallel test .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParallel1() throws Exception {
		paralelTest();
	}

	/**
	 * Junit Parallel test .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParallel2() throws Exception {
		paralelTest();
	}

	/**
	 * Junit Parallel test .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParallel3() throws Exception {
		paralelTest();
	}

	/**
	 * parallel test body.
	 */
	private void paralelTest() {
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
		// driver.findElement(By.tagName("body")).getText().contains("");
		driver.switchTo().frame(2);
		System.out.println(driver.getPageSource());

		// WebDriverWait wait = new WebDriverWait(driver, 30);
		// wait.until(ExpectedConditions.)
		driver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[2]")).click();
		// driver.findElement(By.xpath("//form[@id='form-roundabout-creditcard']/ul/li[2]/div[2]")).click();
		driver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input")).clear();
		driver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input")).sendKeys("123");
		driver.findElement(By.xpath("(.//*[@id='form-roundabout-creditcard']/ul/li[2]/div[4]/div[2]/button[1]")).click();
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
