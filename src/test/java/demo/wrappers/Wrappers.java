package demo.wrappers;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class Wrappers {

    // Function to click on an element using JavaScript Executor
    public static void clickOnElementWrapper(ChromeDriver driver, By locator) {
        System.out.println("Clicking on Element");
        Boolean success;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement clickableElement = driver.findElement(locator);
            clickableElement.click();
            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occured!" + e.getMessage());
            success = false;
        }
    }

    // Reusable method to extract and print combined text from multiple elements
    public static void printCombinedTextByXPath(ChromeDriver driver, String xpathExpression) {
        try {
            // Locate all elements matching the given XPath expression
            List<WebElement> elements = driver.findElements(By.xpath(xpathExpression));
            // Use StringBuilder to concatenate their text
            StringBuilder combinedText = new StringBuilder();
            for (WebElement element : elements) {
                combinedText.append(element.getText()).append(" \n");
            }
            // Print the final combined message
            System.out.println("Combined Text: " + combinedText.toString().trim());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void scrollUntilElementDisappears(ChromeDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Set a timeout for waiting
        try {
            int clickCount = 0;
            // Wait for the "Scroll Right" button to be visible
            while (true) {
                try {
                    // Wait for the button to be visible
                    WebElement scrollButton = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

                    // Click the "Scroll Right" button
                    scrollButton.click();
                    clickCount++;
                    System.out.println("Clicked scroll button " + clickCount + " time(s).");

                    // Optional: Wait for a short duration after clicking to allow the UI to update
                    Thread.sleep(500); // Adjust the sleep time as needed

                } catch (TimeoutException e) {
                    // If the button is not visible, break the loop
                    System.out.println("Scroll button is no longer visible. So please Stop scrolling.");
                    break;
                } catch (StaleElementReferenceException e) {
                    // Handle cases where the element is no longer attached to the DOM
                    System.out.println("The scroll button is no longer attached to the DOM. Stopping scrolling.");
                    break;
                } catch (Exception e) {
                    System.out.println("An error occurred while clicking the scroll button: " + e.getMessage());
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Element not found, scrolling stopped.");
        } catch (Exception e) {
            System.out.println("An error occurred while scrolling: " + e.getMessage());
        }
    }

    // Method to validate movie details
    public static void validateRightmostMovie(ChromeDriver driver, By movieLocator) {
        SoftAssert softAssert = new SoftAssert();
        try {
            // Find all movies using the locator
            List<WebElement> movies = driver.findElements(movieLocator);
            if (movies.isEmpty()) {
                System.out.println("No movies found with the provided locator.");
                return;
            }
            // Get the rightmost movie (last element in the list)
            WebElement rightmostMovie = movies.get(movies.size() - 1);
            // Extract movie title and category
            String titleAndCategory = rightmostMovie
                    .findElement(By.xpath(".//span[@id='video-title']"))
                    .getAttribute("aria-label");
            System.out.println("Rightmost Movie Description is : " + titleAndCategory);
            // Extract the maturity rating
            String maturityRating = rightmostMovie
                    .findElement(By.xpath(".//ytd-badge-supported-renderer//div[2]"))
                    .getText();
            System.out.println("Maturity Rating: " + maturityRating);
            // Validate maturity rating
            softAssert.assertTrue(
                    maturityRating.equals("A") || maturityRating.equals("U") || maturityRating.equals("U/A"),
                    "Movie is missing a valid maturity rating! Found: " + maturityRating
            );
            // Validate movie category
            softAssert.assertTrue(
                    titleAndCategory.contains("Comedy") || titleAndCategory.contains("Animation")
                    || titleAndCategory.contains("Drama") || titleAndCategory.contains("Indian cinema")
                    || titleAndCategory.contains("Action & adventure") || titleAndCategory.contains("Horror")
                    || titleAndCategory.contains("Romance"),
                    "Movie category not found in: " + titleAndCategory
            );
        } catch (Exception e) {
            System.out.println("Error validating the rightmost movie: " + e.getMessage());
        }
        // Assert all validations
        softAssert.assertAll();
    }

// Method to validate music details
    public static void printPlaylistNameAndValidateTracks(ChromeDriver driver, By playlistLocator) {
        SoftAssert softAssert = new SoftAssert();
        try {
            // Find all playlists
            List<WebElement> playlists = driver.findElements(playlistLocator);
            if (playlists.isEmpty()) {
                System.out.println("No playlists found.");
                return;
            }
            // Get the rightmost playlist (last element)
            WebElement rightmostPlaylist = playlists.get(playlists.size() - 1);
            // Get the playlist name
            String playlistName = rightmostPlaylist.findElement(By.xpath(".//h3")).getText();
            System.out.println("Playlist Name: " + playlistName);
            // Get the number of tracks
            String trackCountText = rightmostPlaylist.findElement(By.xpath(".//p[@id='video-count-text']")).getText();
            int trackCount = Integer.parseInt(trackCountText.split(" ")[0]); // Extract number from the text
            System.out.println("Number of Tracks: " + trackCount);
            // Soft assert that the number of tracks is less than or equal to 50
            softAssert.assertTrue(trackCount <= 50, "Number of tracks exceeds 50! Found: " + trackCount);
        } catch (NumberFormatException e) {
            System.out.println("Error validating music playlist: " + e.getMessage());
        }
        // Assert all validations
        softAssert.assertAll();
    }

    // public static String findElementAndPrintWE(WebDriver driver, By locator, WebElement we, int elementNo) {
    //     WebElement element = we.findElements(locator).get(elementNo);
    //     String txt = element.getText();
    //     return txt;
    // }
    public static String findElementAndPrintWE(WebDriver driver, By locator, WebElement we, int elementNo) {
        // Find element within the parent WebElement
        List<WebElement> elements = we.findElements(locator);
        
        // Prevent out-of-bound exception
        if (elementNo < elements.size()) {
            WebElement element = elements.get(elementNo);
            String txt = element.getText();
            System.out.println("Fetched Text: " + txt);
            return txt;
        } else {
            System.out.println("Element not found for index: " + elementNo);
            return "";
        }
    }
    

    public static long convertToNumericValue(String value) {
        // Trim the string to remove any leading or trailing spaces
        value = value.trim().toUpperCase();
        // Check if the last character is non-numeric and determine the multiplier 
        char lastChar = value.charAt(value.length() - 1);
        int multiplier = 1;
        switch (lastChar) {
            case 'K':
                multiplier = 1000;
                break;
            case 'M':
                multiplier = 1000000;
                break;
            case 'B':
                multiplier = 1000000000;
                break;
            default:
                // If the last character is numeric, parse the entire string
                if (Character.isDigit(lastChar)) {
                    return Long.parseLong(value);
                }
                throw new IllegalArgumentException("Invalid format: " + value);
        }
        // Extract the numeric part before the last character
        String numericPart = value.substring(0, value.length() - 1);
        double number = Double.parseDouble(numericPart);
        // Calculate the final value
        return (long) (number * multiplier);
    }

    public static void sendKeysWrapper(WebDriver driver, By locator, String textToSend) {
        System.out.println("Sending Keys");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement textInput = driver.findElement(locator);
            textInput.clear();
            textInput.sendKeys(textToSend);
            textInput.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Exception Occured!" + e.getMessage());
        }
    }

    public static String findElementAndPrint(WebDriver driver, By locator, int elementNo) {
        WebElement we = driver.findElements(locator).get(elementNo);
        // Return the result
        String txt = we.getText();
        System.out.println("Output from HTML Page is : " + txt);
        return txt;
    }
}
