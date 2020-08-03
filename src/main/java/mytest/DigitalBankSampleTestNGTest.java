package mytest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DigitalBankSampleTestNGTest {

    WebDriver driver;
    WebDriverWait wait;
    String url = "http://localhost:8080/bank/login";

    @BeforeTest(alwaysRun = true)
    public void setBaseURL(){
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver84.exe");
        driver = new ChromeDriver();
        driver.get(url);
        wait = new WebDriverWait(driver, 10);
    }

    @Test(priority = 0, groups = {"health_check"})
    public void verifyLogin(){

        WebElement element=driver.findElement(By.xpath("//input[@name='username']"));
        element.sendKeys("jsmith@demo.io");

        element=driver.findElement(By.xpath("//input[@name='password']"));
        element.sendKeys("Demo123!");

        WebElement button=driver.findElement(By.id("submit"));
        button.click();

        element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"right-panel\"]/div[1]/div[2]/div/div/ol/li")));

        Assert.assertEquals(element.getText(), "Welcome Josh");
    }

    @Test(priority = 1, groups = {"health_check"})
    public void checkWidget(){
        WebElement element=driver.findElement(By.xpath("//*[@id=\"balanceSummary\"]"));
        Assert.assertTrue(element.isDisplayed());
    }

    @AfterTest(alwaysRun = true)
    public void endSession(){
        driver.close();
    }
}
