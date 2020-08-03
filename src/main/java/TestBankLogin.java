import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBankLogin {
    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver84.exe");

        ChromeDriver driver=new ChromeDriver();
        driver.get("http://localhost:8080/bank/login");
        WebElement element=driver.findElement(By.xpath("//input[@name='username']"));
        element.sendKeys("jsmith@demo.io");

        element=driver.findElement(By.xpath("//input[@name='password']"));
        element.sendKeys("Demo123!");

        WebElement button=driver.findElement(By.id("submit"));
        button.click();

        driver.close();
    }
}
