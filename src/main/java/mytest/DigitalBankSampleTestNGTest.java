package mytest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DigitalBankSampleTestNGTest {

    WebDriver driver;
    WebDriverWait wait;
    String url = "http://localhost:8080/bank/login";

    @BeforeTest(alwaysRun = true)
    @Parameters("browser")
    public void setBaseURL(String browser) throws Exception{
        //Check if parameter passed as 'chrome'
        if(browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver84.exe");
            driver = new ChromeDriver();
        }
        /*
        //Check if parameter passed as 'firefox'
        else if(browser.equalsIgnoreCase("firefox")){
            System.setProperty("webdriver.gecko.driver",".\\geckodriver.exe");
            driver = new FirefoxDriver();
        }
        */
        //Check if parameter passed as 'Edge'
        else if(browser.equalsIgnoreCase("Edge")){
            System.setProperty("webdriver.edg.driver",".\\MicrosoftWebDriver.exe");
            driver = new EdgeDriver();
        }
        //If no browser passed throw exception
        else{
            throw new Exception(("Browser is not correct"));
        }

        //Get URL
        driver.get(url);

        wait = new WebDriverWait(driver, 10);
    }

    @Test(groups = {"health_check"})
    @Parameters({"username", "password"})
    public void verifyLogin(String username, String password){

        WebElement element=driver.findElement(By.xpath("//input[@name='username']"));
        element.sendKeys(username);

        element=driver.findElement(By.xpath("//input[@name='password']"));
        element.sendKeys(password);

        WebElement button=driver.findElement(By.id("submit"));
        button.click();

        element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"right-panel\"]/div[1]/div[2]/div/div/ol/li")));

        Assert.assertEquals(element.getText(), "Welcome Josh");
    }

    @Test(priority = 1, groups = {"health_check"})
    public void checkWidget(){
        WebElement element=driver.findElement(By.id("balanceSummary"));
        Assert.assertNotNull(element);
    }

    @Test(priority = 2, groups = {"health_check"}, singleThreaded = true)
    public void checkDeposit(){
        WebElement button=driver.findElement(By.xpath("//*[@id=\"deposit-menu-item\"]"));
        //System.out.println(button.getText());
        button.click();

        //WebDriverWait wait=new WebDriverWait(driver, 5);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className("card-title text-white")));

        Select drpAccount=new Select(driver.findElement(By.xpath("//*[@id=\"id\"]")));
        drpAccount.selectByValue("95");

        String amount = "234.56";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("document.getElementById('amount').value='"+amount+"'");

        //WebElement amountElement=driver.findElement(By.xpath("//*[@id=\"amount\"]"));
        //Assert.assertEquals(amountElement.getText(), "13.45");

        //explicit wait
        WebElement submitButton;
        submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"right-panel\"]/div[2]/div/div/div/div/form/div[2]/button[1]")));
        submitButton.click();

        /*
        Wait<WebDriver> waitClick = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement submitButton = waitClick.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"right-panel\"]/div[2]/div/div/div/div/form/div[2]/button[1]")));

        WebElement clickSubmit = waitClick.until(new Function<WebDriver,WebElement>(){
           public WebElement apply(WebDriver driver){
               return driver.findElement(By.xpath("//*[@id=\"right-panel\"]/div[2]/div/div/div/div/form/div[2]/button[1]"));
           }
        });
        submitButton.click();
        */

        WebDriverWait waitTable=new WebDriverWait(driver, 20);
        waitTable.until(ExpectedConditions.visibilityOfElementLocated(By.id("transactionTable")));

        String amountText=driver.findElement(By.xpath("//*[@id=\"transactionTable\"]/tbody/tr[1]/td[4]")).getText();
        System.out.println(amountText);
        Assert.assertEquals(amountText,"$"+amount);

    }

    @Test(priority = 3, groups={"health_check"}, singleThreaded = true)
    public void checkSaving(){

        WebElement Savings=driver.findElement(By.xpath("//*[@id=\"savings-menu\"]"));
        //System.out.println(Savings.getText());
        Savings.click();

        WebElement viewSaving;
        viewSaving = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"view-savings-menu-item\"]")));
        viewSaving.click();

        WebElement SavingTable=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"transactionTable\"]/tbody/tr[1]/td[2]")));
        String SavingAcc = SavingTable.getText();
        System.out.println(SavingAcc);
        //Assert.assertEquals(SavingAcc,"Income");
    }

    @AfterTest(alwaysRun = true)
    public void endSession(){
        driver.close();
    }
}
