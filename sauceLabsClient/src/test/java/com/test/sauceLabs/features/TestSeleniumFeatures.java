package com.test.sauceLabs.features;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.saucelabs.junit.Parallelized;
import com.test.poc.Watcher;
import com.test.poc.core.model.CapabilityConfiguraton;
import com.test.poc.core.parent.ParametrizedParentTest;

/**
 * Paralellization tests.
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
@RunWith(Parallelized.class)
public class TestSeleniumFeatures extends ParametrizedParentTest {

	/**
	 * Selenoum generic web driver.
	 */
	private WebDriver						webDriver;

	/**
	 * String buffer to hold valiadtion errors.
	 */
	private final StringBuffer				verificationErrors			= new StringBuffer();

	/**
	 * Profile related configuration data
	 */
	private final CapabilityConfiguraton	capabilityConfiguraton;

	/**
	 * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds/fails.
	 */
	@Rule
	public Watcher							resultReportingTestWatcher	= new Watcher(this, authentication);

	/**
	 * Will be feed with data provided by
	 * 
	 * @param capabilityConfiguraton
	 */
	public TestSeleniumFeatures(final CapabilityConfiguraton capabilityConfiguraton) {
		this.capabilityConfiguraton = capabilityConfiguraton;
	}

	@Before
	public void setUp() throws Exception {
		webDriver = provideWebDriver(capabilityConfiguraton);
		System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		sessionId = ((RemoteWebDriver) webDriver).getSessionId().toString();
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
		webDriver
				.get("https://api.clickandbuy-d2.com/webservices/automation/pay_1_1_0/LDVYSTJgeKhoEXloJ6b*U5ityFmIKFEWyE6-D8n7sdCjJ9wde5tXFjkjgeEVP31AdCxQCWecF80oPvXW*snXtrE5eMYiw2*3yYTCmCth5a*lPdmrOtnSKgIRJMAGBxTVi2stCBG2bf75d6HcXZA0X8UQ8e4DbBSqKk2ZBIuGsH*nBnCO4flsj6nuObHZE4nIWhbvBpyvzy6aEcwcnWC*v*YMJhcxGh8-eZCamP-M2L7GfeAwbEkl3LBWN8B4f0qZYIathizm350ABfxF9t5aI-cbNLnzh10grL48JbnaMItmeagsAna9*Cq2HWndcMAF3ldAPYARJqSlMgOpRHcWIscKQ7TwvpBFmbQmIQ2TWBBNE3E3XctYoMIQFnsjc7FU");
		webDriver.findElement(By.id("login_email")).clear();
		webDriver.findElement(By.id("login_email")).sendKeys("thijs@consumer.com");
		webDriver.findElement(By.id("login_password")).clear();
		webDriver.findElement(By.id("login_password")).sendKeys("123@test");
		webDriver.findElement(By.id("login_submit")).click();

		Boolean wdw = false;

		while (!wdw) {
			System.out.println("waiting...");
			wdw = new WebDriverWait(webDriver, 10).until(new ExpectedCondition<Boolean>() {

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
		webDriver.switchTo().frame(2);
		System.out.println(webDriver.getPageSource());

		// WebDriverWait wait = new WebDriverWait(driver, 30);
		// wait.until(ExpectedConditions.)
		webDriver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[2]")).click();
		// driver.findElement(By.xpath("//form[@id='form-roundabout-creditcard']/ul/li[2]/div[2]")).click();
		webDriver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input")).clear();
		webDriver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input")).sendKeys("123");
		webDriver.findElement(By.xpath("(.//*[@id='form-roundabout-creditcard']/ul/li[2]/div[4]/div[2]/button[1]")).click();
		webDriver.findElement(By.id("paynow")).click();
	}

	/**
	 * test down method.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		webDriver.quit();
		final String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}
