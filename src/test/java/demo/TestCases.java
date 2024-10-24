package demo;

import java.time.Duration;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import demo.utils.ExcelDataProvider;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data

    ChromeDriver driver;

    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");
        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    public void testCase01() {
        System.out.println("Start test Case 01 : launch YouTube and verify URL");
        driver.get("https://www.youtube.com/");
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.youtube.com/", "Incorrect URL!");
        Wrappers.clickOnElementWrapper(driver, By.xpath("//div[@id='footer']//a[text()='About']"));
        Wrappers.printCombinedTextByXPath(driver, "//section[@class='ytabout__content']/h1");
        Wrappers.printCombinedTextByXPath(driver, "//section[@class='ytabout__content']//p[1]");
        System.out.println("End test Case 01");
    }

    @Test
    public void testCase02() {
        System.out.println("Start test Case 02 : Fetch movie details");
        // Navigate to YouTube Home Page
        driver.get("https://www.youtube.com/");
        System.out.println("Navigated to YouTube Home Page");
        // Click on the Movies/Films tab
        Wrappers.clickOnElementWrapper(driver, By.xpath("//div[@id='items']//a[@title='Movies']"));
        // Scroll until the right scroll button disappears
        Wrappers.scrollUntilElementDisappears(driver, By.xpath("//div[@id='right-arrow']//button"));
        // Validate movie details using the reusable method
        Wrappers.validateRightmostMovie(driver, By.xpath("//ytd-grid-movie-renderer"));
        System.out.println("End test Case 02");
    }

    @Test
    public void testCase03() throws InterruptedException {
        System.out.println("Start test Case 03 : Fetch music details");
        // Navigate to YouTube Home Page
        driver.get("https://www.youtube.com/");
        System.out.println("Navigated to YouTube Home Page");
        // Click on the Movies/Films tab
        Wrappers.clickOnElementWrapper(driver, By.xpath("//div[@id='items']//a[@title='Music']"));
        // Scroll until the right scroll button disappears
        Wrappers.scrollUntilElementDisappears(driver, By.xpath("//div[@id='dismissible']//span[contains(text(),\"India's Biggest Hits\")]/ancestor::div[contains(@class,'grid-subheader')]/following-sibling::div//button[@aria-label='Next']"));
        // Validate music details using the reusable method
        Thread.sleep(5000);;
        Wrappers.printPlaylistNameAndValidateTracks(driver, By.xpath("//span [contains(., 'Biggest Hits')]//ancestor::div[6]//div[@id='contents']//ytd-compact-station-renderer"));
        System.out.println("End test Case 03");
    }

    @Test
    public void testCase04() throws InterruptedException {
        System.out.println("Start test Case 04 : Fetch News details");
        // Navigate to YouTube Home Page
        driver.get("https://www.youtube.com/");
        System.out.println("Navigated to YouTube Home Page");
        // Click on the Movies/Films tab
        Wrappers.clickOnElementWrapper(driver, By.xpath("//div[@id='items']//a[@title='News']"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Wait for the element to be clickable
        WebElement contentCards = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='rich-shelf-header-container' and contains(., 'Latest news posts')]//ancestor::div[1]//div[@id='contents']")));
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        long sumOfVotes = 0;

        for (int i = 0; i < 3; i++) {
            System.out.println(Wrappers.findElementAndPrintWE(driver, By.xpath("//div[@id='header']"), contentCards, i));
            System.out.println(Wrappers.findElementAndPrintWE(driver, By.xpath("//div[@id='body']"), contentCards, i));
            try {
                String res = Wrappers.findElementAndPrintWE(driver, By.xpath("//span [@id='vote-count-middle']"), contentCards, i);
                sumOfVotes = sumOfVotes + Wrappers.convertToNumericValue(res);
            } catch (NoSuchElementException e) {
                System.out.println("Vote not present - " + e.getMessage());
            }
            System.out.println(sumOfVotes);
            Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
            System.out.println("End test Case 04");
        }
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();
        System.out.println("THE END OF ALL TEST CASES.");

    }
}
