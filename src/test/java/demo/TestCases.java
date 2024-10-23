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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import demo.utils.ExcelDataProvider;
import demo.wrappers.Wrappers;
//import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases extends ExcelDataProvider {

//     WebDriver driver;
//     @BeforeClass
//     public void setup() {
//         System.out.println("Constructor: Driver");
//        // WebDriverManager.chromedriver().timeout(30).setup();
//         //WebDriver.chromedriver().setup();
//       // io.github.bonigarcia.wdm.WebDriverManager.chromedriver().timeout(30).setup();
//         driver = new ChromeDriver();
//         driver.manage().window().maximize();
//         System.out.println("Successfully Created Driver");
//     }
    ChromeDriver driver;

    @BeforeClass
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

    @BeforeMethod
    public void goToYT() throws InterruptedException {
        Wrappers.goToUrlAndWait(driver, "https://youtube.com");
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
    }

   @Test
    public void testCase01() throws InterruptedException {
        System.out.println("Start test Case 01 : launch YouTube and verify URL");
        // driver.get("https://www.youtube.com/");

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.youtube.com/", "Incorrect URL!");
        Wrappers.findElementAndClick(driver, By.xpath("//div[@id='footer']//a[text()='About']"));
        //Wrappers.findElementAndPrint(driver, "//section[@class='ytabout__content']//p");
        String aboutMessage = Wrappers.findElementAndPrint(driver, By.xpath("//section[@class='ytabout__content']//p"), 0);
        System.out.println("Abour Message of Youtube is :" + aboutMessage);
        System.out.println("End test Case 01");

    }

   @Test
    public void testCase02() throws InterruptedException {
        System.out.println("Start test Case 02 : Verify Movie Category and Rating ");
        Wrappers.findElementAndClick(driver, By.xpath("//a[@title='Movies']"));
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        Wrappers.clickTillUnclickable(driver, By.xpath("//div[@id='dismissible']//span[contains(text(),'Top selling')]/ancestor::div[contains(@class,'grid-subheader')]/following-sibling::div//button[@aria-label='Next']"), 5);
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        // Fetch the movie category using the movieCategory locator
        By movieCategory = By.xpath("//span [contains(., 'Top selling')]//ancestor::div[6]//div[@id='contents']//ytd-grid-movie-renderer/a/span");
        String categoryResult = Wrappers.findElementAndPrint(driver, movieCategory, driver.findElements(movieCategory).size() - 1);
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        // Initialize SoftAssert
        SoftAssert sa = new SoftAssert();
        // Extract the actual category by splitting at '•' to ignore the year or other info
        String actualCategory = categoryResult.split("•")[0].trim();
        // Assert that the extracted category is one of the valid categories
        System.out.println("Asserting movie category: " + actualCategory);
        boolean isCategoryValid = actualCategory.matches("Comedy|Animation|Drama|Action & adventure");
        sa.assertTrue(isCategoryValid, "Invalid movie category: " + actualCategory);
        // Fetch the movie rating (marked) using the movieMarked locator
        By movieMarked = By.xpath("//span[contains(., 'Top selling')]//ancestor::div[6]//div[@id='contents']//ytd-grid-movie-renderer/ytd-badge-supported-renderer/div[2]/p");
        String markedResult = Wrappers.findElementAndPrint(driver, movieMarked, driver.findElements(movieMarked).size() - 1);
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        // Assert that the movie rating (marked) is one of the valid ratings
        System.out.println("Asserting movie rating: " + markedResult);
        boolean isRatingValid = markedResult.matches("A|U|A/U");
        sa.assertTrue(isRatingValid, "Invalid movie rating: " + markedResult);
        System.out.println("End test Case 02");
    }

   @Test
    public void testCase03() throws InterruptedException {
        System.out.println("Start test Case 03 : Print Music Playlist Name and verify Track");
        Wrappers.findElementAndClick(driver, By.xpath("//a[@title='Music']"));
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        Wrappers.clickTillUnclickable(driver, By.xpath("//div[@id='dismissible']//span[contains(text(),\"India's Biggest Hits\")]/ancestor::div[contains(@class,'grid-subheader')]/following-sibling::div//button[@aria-label='Next']"), 5);

        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);

        By locator_TrackCount = By.xpath("//span [contains(., 'Biggest Hits')]//ancestor::div[6]//div[@id='contents']//ytd-compact-station-renderer//p[@id='video-count-text']");
        String playlistName = Wrappers.findElementAndPrint(driver, locator_TrackCount, driver.findElements(locator_TrackCount).size() - 1);
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        System.out.println("Asserting movie rating: " + playlistName);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(Wrappers.convertToNumericValue(playlistName.split(" ")[0]) <= 50);
        System.out.println("End Test Case 03");
        
    }
    @Test
    public void testCase04() throws InterruptedException {
        System.out.println("Start test Case 04 : launch News Page and verify Sum of likes");
        Wrappers.findElementAndClick(driver, By.xpath("//a[@title='News']"));
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
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
    }

}
