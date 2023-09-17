import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.*;

public class AmazonTests {
    WebDriver DRIVER;

    @BeforeClass
    public void invokeBrowser() {
        System.setProperty("webdriver.chrome.driver", "D:\\Projects\\Drivers\\selenium_driver\\116.0.5845.96\\chromedriver.exe");
        DRIVER = new ChromeDriver();
        DRIVER.manage().window().maximize();
        DRIVER.manage().deleteAllCookies();
        DRIVER.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        DRIVER.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        DRIVER.get("https://amazon.com");
    }

    @Test(priority = 0)
    public void verifyTitleOfPage() {
        String expectedTitle, actualTitle;
        expectedTitle = "Amazon.com. Spend less. Smile more.";
        actualTitle = DRIVER.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Test(priority = 100)
    public void searchProduct() {
        String productItem = "Apple Watch";
        String category = "Electronics";
        WebElement selDropdown = DRIVER.findElement(id("searchDropdownBox"));
        Select selCategory = new Select(selDropdown);
        selCategory.selectByVisibleText(category);
        DRIVER.findElement(id("twotabsearchtextbox")).sendKeys(productItem);
        DRIVER.findElement(xpath("//input[@value='Go']")).click();
    }

    @Test(priority = 200)
    public void getNthProduct() {
        int productItemNumber = 2;
        String xpathExpression = String.format("//div[@data-component-type='s-search-result'][%d]", productItemNumber);
        WebElement nthProduct = DRIVER.findElement(xpath(xpathExpression));
        String nthProductResult = nthProduct.getText();
        System.out.println(nthProductResult);
    }

    @Test(priority = 300)
    public void getAllProducts() {
        List<WebElement> allProducts = DRIVER.findElements(xpath("//div[@data-component-type='s-search-result']"));
        String productResult;
        for(WebElement product : allProducts) {
            productResult = product.getText();
            System.out.println(productResult + "---------------------------------------");
        }
    }
    @Test(priority = 400)
    public void searchAllProductsViaScrollDown() {
        List<WebElement> allProducts = DRIVER.findElements(xpath("//div[@data-component-type='s-search-result']"));
        String productResult;
        Actions action = new Actions(DRIVER);
        for(WebElement product : allProducts) {
            action.moveToElement(product).build().perform();
            productResult = product.getText();
            System.out.println(productResult + "---------------------------------------");
        }
    }

    @Test(priority = 500, enabled = false)
    public void getAllProductsViaScrollDOwnUsingJS() {
        List<WebElement> allProducts = DRIVER.findElements(xpath("//div[@data-component-type='s-search-result']"));
        String productResult;
        int xCordinate, yCordinate;
        for(WebElement product : allProducts) {
            xCordinate = product.getLocation().x;
            yCordinate = product.getLocation().y;
            scrollDown(xCordinate, yCordinate);
            productResult = product.getText();
            System.out.println(productResult + "---------------------------------------");
        }
    }

    private void scrollDown(int x, int y) {
        JavascriptExecutor jsEngine;
        jsEngine = (JavascriptExecutor) DRIVER;
        String jsCommand;
        jsCommand = String.format("window.ScrollBy(%d,%d)", x, y);
        jsEngine.executeScript(jsCommand);
    }

    @AfterClass
    public void closeBrowser() {
        DRIVER.quit();
    }
}
