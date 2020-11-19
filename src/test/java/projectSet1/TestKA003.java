package projectSet1;

import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
/**
 * KA00 To verify whether application displays error message upon entering invalid
 *  details in login page while trying to login using Facebook account
 * @author SaritaKumari
 *
 */
public class TestKA003 {
	AndroidDriver driver;
	   WebDriverWait wait;
  @BeforeTest
  public void beforeTest() throws MalformedURLException {

		DesiredCapabilities capability= new DesiredCapabilities();
	    capability.setCapability("deviceName", "KH003");
	    capability.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
	    capability.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "org.khanacademy.android");
	    capability.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "org.khanacademy.android.ui.library.MainActivity");
	    driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"),capability);
	    driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
	    wait=new WebDriverWait(driver, 5000);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Dismiss\")")));
	    System.out.println("Run Before Test");
  }
  @Test(priority=1, description="Error message validation")
  public void invalidLogin() throws InterruptedException {
	  
	 Thread.sleep(2000);
	 //Click on dismiss button
	 
     driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Dismiss\")")).click();
	  System.out.println("Test method");
	  Thread.sleep(2000);
	  
      //Click on sign in button
	  
      driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign in\")")).click();
      
      //Clickon "Continue with Facebook"
      
      driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Continue with Facebook\")")).click();
      Thread.sleep(1000);
      wait=new WebDriverWait(driver, 3000);
      
      //Wait for element till its visible
      
      wait.until(ExpectedConditions.elementToBeClickable(MobileBy.className("android.widget.EditText")));
      
	   // Enter username
      
      driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys("sari.kum2340@gmail.com");
      
      // Enter Passwrod
      driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Facebook password\")")).sendKeys("Sarita");
      driver.hideKeyboard();
      
      //click on login
      driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Log In\")")).click();
      
      //Wait for email text box appears
      wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"The email address that you've entered\")")));
      try {
          String expectedError="The email address that you've entered doesn't match any account.";
          
    	  //get the actula error from the screen
          String ActualError =driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"The email address that you've entered\")")).getText();
          System.out.println("Error message on the screen: "+ActualError);
          
          //Assertion PASS: if login is un sucessfull and user thrown with error 
          Assert.assertEquals(ActualError.trim(), expectedError.trim(),"Test Fail: Login is successful"); 
          }catch(NoSuchElementException e) {
          //Success message when user log successfully
          String succes=driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Need to add a class?\")")).getText();
          System.out.println("successful login "+succes);
          Thread.sleep(2000);
          }
  }

  @AfterTest
  public void afterTest() {
	  System.out.println("Quiting driver");
	  driver.quit();
  }

}
