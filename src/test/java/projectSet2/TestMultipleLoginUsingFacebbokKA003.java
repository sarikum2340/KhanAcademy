package projectSet2;

import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;


/**
 * KH003
 * @author SaritaKumari
 *
 */
public class TestMultipleLoginUsingFacebbokKA003 {
	AndroidDriver driver;
	WebDriverWait wait;

	@BeforeTest
	public void beforeTest() throws MalformedURLException {

		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability("deviceName", "KH003");
		capability.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		capability.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "org.khanacademy.android");
		capability.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
				"org.khanacademy.android.ui.library.MainActivity");
		System.out.println("Setup phase: Initialising the driver");
		driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capability);
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
		wait = new WebDriverWait(driver, 5000);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Dismiss\")")));
		
	}

	public void screenShots() throws IOException {
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(System.getProperty("user.dir") +".\\src\\test\\resources\\ScreenShots\\ScreenShot"+System.currentTimeMillis()+".png"));

	}

	@Test
	public void testLogin() throws InterruptedException, IOException {

		System.out.println("Testing phase");
		// Click on dismiss button
		File file = new File(".\\src\\test\\resources\\excelSheets\\KhanAcademyLoginDetails.xlsx");
		FileInputStream fs = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheetAt(0);
		int rc = sheet.getLastRowNum();
		System.out.println("Number of Rows: " + rc);
		for (int i = 1; i <= rc; i++) {
			String userName = sheet.getRow(i).getCell(0).getStringCellValue();
			String Pass = sheet.getRow(i).getCell(1).getStringCellValue();
			String packageName = ((AndroidDriver) driver).getCurrentPackage();
			System.out.println("package name :" + packageName);
			System.out.println("USer name :" + userName);
			driver.activateApp("org.khanacademy.android");
			System.out.println("Test method");
			Thread.sleep(10000);

			// Click on sign in button
			
			driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign in\")")).click();
			Thread.sleep(2000);
			screenShots();
			
			// Clickon "Continue with Facebook"

			driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Continue with Facebook\")")).click();
			Thread.sleep(1000);
			wait = new WebDriverWait(driver, 3000);

			// Wait for element till its visible

			wait.until(ExpectedConditions.elementToBeClickable(MobileBy.className("android.widget.EditText")));

			// Enter username

			driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys(userName.toString());
			screenShots();
			// Enter Passwrod
			driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Facebook password\")")).sendKeys(Pass);
			driver.hideKeyboard();

			// click on login
			driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Log In\")")).click();
			try {
				String expectedError = "The mobile number or email address that you've entered doesn't match any account.";

				// get the actual error from the screen
				Thread.sleep(3000);
				screenShots();
				String ActualError = driver.findElement(MobileBy.AndroidUIAutomator(
						"UiSelector().textContains(\"The mobile number or email address that you've entered\")"))
						.getText();
				System.out.println("Error message on the screen: " + ActualError);

				// Assertion PASS: if login is un sucessfull for invalid user id and user thrown with error
				Assert.assertEquals(ActualError.trim(), expectedError.trim(), "Test Fail: Login is successful");
			} catch (NoSuchElementException e) {
				Thread.sleep(6000);
				// Success message when user log successfully
				String successFulLoginContinue = driver.findElement(MobileBy.className("android.widget.Button"))
						.getText();	
				Thread.sleep(8000);
				System.out.println("Successful login: " + successFulLoginContinue);
				Assert.assertEquals(successFulLoginContinue.trim(), "Continue",
						"Test Fail: Login with valid credential is un successful");
				Thread.sleep(2000);
			}
			driver.terminateApp("org.khanacademy.android");
		}
	}

	@AfterTest
	public void afterTest() {
		System.out.println("Tear down Phase: Quiting the driver");
		driver.quit();

	}

}
