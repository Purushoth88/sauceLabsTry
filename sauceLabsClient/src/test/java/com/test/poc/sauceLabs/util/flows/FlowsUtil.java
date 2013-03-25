/**
 * 
 */
package com.test.poc.sauceLabs.util.flows;

import static com.test.poc.sauceLabs.util.TestUtil.waitForIframes;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.test.poc.sauceLabs.util.config.JSONConfigurationUtils;

/**
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class FlowsUtil {

	/**
	 * class logger.
	 */
	private static final Logger	logger	= Logger.getLogger(FlowsUtil.class);

	/**
	 * Private C'tor. Prevents instantiation.
	 */
	private FlowsUtil() {
	}

	/**
	 * Click and buy Selenium checkout flow with existing account.
	 * 
	 * @param webDriver
	 */
	public static void cabCheckoutWithExistingAccount(final WebDriver webDriver) {
		logger.info("Starting CaB checkout flow");
		webDriver
				.get("https://api.clickandbuy-d2.com/webservices/automation/pay_1_1_0/LDVYSTJgeKhoEXloJ6b*U5ityFmIKFEWyE6-D8n7sdCjJ9wde5tXFjkjgeEVP31AdCxQCWecF80oPvXW*snXtrE5eMYiw2*3yYTCmCth5a*lPdmrOtnSKgIRJMAGBxTVi2stCBG2bf75d6HcXZA0X8UQ8e4DbBSqKk2ZBIuGsH*nBnCO4flsj6nuObHZE4nIWhbvBpyvzy6aEcwcnWC*v*YMJhcxGh8-eZCamP-M2L7GfeAwbEkl3LBWN8B4f0qZYIathizm350ABfxF9t5aI-cbNLnzh10grL48JbnaMItmeagsAna9*Cq2HWndcMAF3ldAPYARJqSlMgOpRHcWIscKQ7TwvpBFmbQmIQ2TWBBNE3E3XctYoMIQFnsjc7FU");
		webDriver.findElement(By.id("login_email")).clear();
		webDriver.findElement(By.id("login_email")).sendKeys("thijs@consumer.com");
		webDriver.findElement(By.id("login_password")).clear();
		webDriver.findElement(By.id("login_password")).sendKeys("123@test");
		webDriver.findElement(By.id("login_submit")).click();

		logger.info("waiting for page to load, after subit");

		waitForIframes(webDriver, 3);

		// driver.findElement(By.tagName("body")).getText().contains("");
		webDriver.switchTo().frame(2);

		// find page elements.
		webDriver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[2]")).click();
		webDriver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input")).clear();
		webDriver.findElement(By.xpath(".//*[@id='form-roundabout-creditcard']/ul/li[2]/div[3]/input")).sendKeys("123");
		webDriver.findElement(By.xpath("(.//*[@id='form-roundabout-creditcard']/ul/li[2]/div[4]/div[2]/button[1]")).click();
		webDriver.findElement(By.id("paynow")).click();
	}

}
