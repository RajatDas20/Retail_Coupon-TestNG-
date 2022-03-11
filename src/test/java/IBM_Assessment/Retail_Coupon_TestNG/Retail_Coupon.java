package IBM_Assessment.Retail_Coupon_TestNG;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Retail_Coupon {

	static WebDriver driver;
	WebElement obj;
	String cpncode;

	SoftAssert verify = new SoftAssert();
	Alert alert;
	Actions builder;

	@DataProvider(name = "Coupon Details")
	public Object[][] couponDetails() {

		Object[][] data = new Object[1][4];
		{
			data[0][0] = ("You are great , you deserve this" + rndUserName());
			data[0][1] = ("win" + rndcode());
			data[0][2] = rndcode();
			data[0][3] = rndcode();
		}

		return data;
	}

	@BeforeClass
	public void setProperty() {
		try {
			System.setProperty("webdriver.chrome.driver", ".\\lib\\chromedriver.exe");

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@BeforeMethod
	public void CouponSection() throws InterruptedException {
		try {
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			builder = new Actions(driver);
			driver.get("http://retailm1.upskills.in/admin/");
			System.out.println("Homepage ->");

			driver.findElement(By.id("input-username")).sendKeys("admin");
			driver.findElement(By.id("input-password")).sendKeys("Admin@123");
			Thread.sleep(1000);

			driver.findElement(By.id("input-password")).sendKeys(Keys.ENTER);
			// driver.findElement(By.xpath("//button[@type =
			// 'submit']")).click();
			Thread.sleep(2000);
			System.out.println("Logged in ->");

			driver.findElement(By.id("menu-marketing")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//a[text() ='Coupons']")).click();
			Thread.sleep(1000);
			System.out.println("Coupon Section ->");

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Test(dataProvider = "Coupon Details", priority = 1)
	public void addNew(String couponName, String code, String discount, String total) throws InterruptedException {
		try {
			System.out.println("Generating Coupon ->");
			System.out.println(couponName);
			System.out.println(code);

			driver.findElement(By.xpath("//a[@data-original-title='Add New']")).click();
			driver.findElement(By.xpath("//input[@id='input-name']")).sendKeys(couponName);
			driver.findElement(By.xpath("//input[@id='input-code']")).sendKeys(code);
			driver.findElement(By.xpath("//input[@id='input-discount']")).sendKeys(discount);
			driver.findElement(By.xpath("//input[@id='input-total']")).sendKeys(total);

			driver.findElement(By.xpath("//button[@data-original-title='Save']")).click();
			Thread.sleep(2000);
			System.out.println("Coupon Saved ->");

			cpncode = code;
			String expected = couponName;
			String actual = driver.findElement(By.xpath("//*[text()='" + code + "']/parent::tr/td[2]")).getText();

			verify.assertEquals(expected, actual);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Test(dependsOnMethods = "addNew", priority = 2)
	public void edit() throws InterruptedException {

		try {
			driver.findElement(By.xpath("//*[text()='" + cpncode + "']/parent::tr/td[8]/a")).click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//input[@id='input-name']")).clear();
			driver.findElement(By.xpath("//input[@id='input-name']")).sendKeys("editedtext" + cpncode);
			driver.findElement(By.xpath("//button[@data-original-title='Save']")).click();
			Thread.sleep(2000);

			System.out.println("Coupon edited ->");

			String actual = driver.findElement(By.xpath("//*[text()='" + cpncode + "']/parent::tr/td[2]")).getText();
			String expected = "editedtext" + cpncode;
			verify.assertEquals(expected, actual);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Test(dependsOnMethods = "addNew", priority = 3)
	public void delete() throws InterruptedException {

		try {
			driver.findElement(By.xpath("//*[text()='" + cpncode + "']/parent::tr/td[1]")).click();
			System.out.println("Checkboxes clicked for deletion");
			Thread.sleep(2000);

			driver.findElement(By.xpath("//button[@class='btn btn-danger']")).click();
			Thread.sleep(1000);
			alert = driver.switchTo().alert();
			Thread.sleep(1000);
			alert.accept();
			Thread.sleep(2000);

			boolean exists = driver.findElements(By.xpath("//*[text()='" + cpncode + "']/parent::tr/td[1]")).isEmpty();
			// boolean exists = driver.findElements( By.xpath("//*[text()='" +
			// cpncode + "']/parent::tr/td[1]")).size() != 0;
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			verify.assertTrue(exists);

			System.out.println("Successfully deletes the coupons");

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@AfterMethod
	public void exit() throws InterruptedException {
		try {
			Thread.sleep(3000);
			driver.quit();
			verify.assertAll();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public String rndUserName() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 5) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	public String rndcode() {
		String SALTCHARS = "0123456789";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 4) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

}
