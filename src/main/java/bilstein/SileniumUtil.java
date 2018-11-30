package bilstein;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class SileniumUtil {

    private static final String BILSTEIN_URL = "https://cart.bilsteinus.com/";
    private static final Logger logger = LogManager.getLogger(SileniumUtil.class.getName());

    /**
     * We assume that "engineSelector-year" element available means that driver got
     * right bilstein webpage.
     *
     * @return Webdriver with "engineSelector-year" element present.
     */
    public static WebDriver initBaseDriver() {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(BILSTEIN_URL);
        By by = By.id("engineSelector-year");
        WebElement yearEl = waitForElement(by, driver);

        //if yearEl isNotNull, it means that Driver successfully got starting page.
        if (yearEl!=null){
            logger.info("driver successfully initiated");
            return driver;
        }
        logger.error("Could not find element by id [engineSelector-year]. Check internet connection or site code.");

        return initBaseDriver();
    }

    public static int getElementID(List<WebElement> options, String elementName) {
        int elementID = 0;

        for (int i = 1; i < options.size(); i++) {
            WebElement option = options.get(i);
            String elText = option.getText();
            if (elText.equals(elementName)){
                elementID = i;
                break;
            }
        }

        if (elementID==0){
            logger.error("No requested value available in option list");
        }

        return elementID;
    }

    /**
     * Usually Bilstein dropdown lists have 1 constant option (Year, Make, Model etc.) and others
     * are loaded upon request. Thus this options are not always available right after opening dropdown element.
     *
     * @param select - select element.
     * @return - list of options for requested select. If option list size is 1 - it means that no options are available.
     */
    public static List<WebElement> waitForSelect(Select select) {
        List<WebElement> options = select.getOptions();
        int retries = 0;
        while (options.size()<2){
            options = select.getOptions();
            try {
                Thread.sleep(100);
            }
            catch (Exception ignored) {
            }
            retries++;
            //we*ve waited for select more than a minute
            if (retries>600){
                if (hasConnection()){
                    logger.error("Requested Select element has no available options.");
                    break;
                }
                else {
                    logger.error("Bilstein page is not available, retrying");
                    retries = 0;
                }
            }
        }

        return options;
    }

    /**
     *
     * @param driver - driver, where we expect engineSelector-year element be present.
     * @return dropdown element with years to be selected.
     */
    public static Select getYearSelect(WebDriver driver) {

        return getSelectByID(driver, "engineSelector-year");
    }

    private static WebElement waitForElement(By by, WebDriver driver) {
        int retries = 0;
        WebElement result = null;
        while (true){
            try {
                WebDriverWait wait = new WebDriverWait(driver, 10);
                result = wait.until(ExpectedConditions.presenceOfElementLocated(by));
                break;
            }
            catch (TimeoutException e){
                retries++;
                if (retries>6){
                    logger.error("No internet connection, or no WebElement available at searched location");
                    break;
                }
            }
        }

        return result;
    }

    public static boolean hasConnection(){
        URL url= null;
        try {
            url = new URL(BILSTEIN_URL);
            URLConnection con=url.openConnection();
            con.getInputStream();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String getElementLinkID(WebElement element) {

        return element.getAttribute("value");
    }

    public static List<WebElement> getMakeEls(WebDriver driver, String year) {
        Select ySelect = getYearSelect(driver);
        waitForSelect(ySelect);
        ySelect.selectByVisibleText(year);

        return getElementsByID(driver, "engineSelector-make");
    }

    public static List<WebElement> getYearElements(WebDriver driver) {
        String selectorID = "engineSelector-year";

        return getElementsByID(driver, selectorID);
    }

    private static List<WebElement> getElementsByID(WebDriver driver, String elemID){
        By by = By.id(elemID);
        WebElement drop = driver.findElement(by);
        Select select = new Select(drop);

        return waitForSelect(select);
    }

    /***
     *
     * @param elements - elements from drop down lists
     * @return Map where key is text in drop down list, and value is element id for building webLink for
     * current car.
     */
    public static Map<String,String> getElementMap(List<WebElement> elements) {
        Map<String, String> elementMap = new TreeMap<>();
        for (WebElement element: elements){
            String val = element.getText();
            String id = getElementLinkID(element);
            elementMap.put(val, id);
        }
        return elementMap;
    }

    public static List<WebElement> getModelEls(WebDriver driver, String year, String make) {
        getMakeEls(driver, year);
        Select select = getSelectByID(driver, "engineSelector-make");
        select.selectByVisibleText(make);
        waitForSelect(select);
        select = getSelectByID(driver,"engineSelector-model");

        return waitForSelect(select);
    }

    private static Select getSelectByID(WebDriver driver, String id){
        WebElement drop = driver.findElement(By.id(id));

        return new Select(drop);
    }

}
