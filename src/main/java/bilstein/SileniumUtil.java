package bilstein;

import bilstein.entities.preparse.AdditionalField;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
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

    public static WebElement waitForElement(By by, WebDriver driver) {
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
            logger.error("No connection available");
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

    public static boolean getCarPage(WebDriver driver, String url) {
        driver.get(url);
        sleepForTimeout(1000);
        By by = By.id("ProductResults");
        WebElement prodResultElem = waitForElement(by,driver);
        by = By.className("searchFeedback");
        WebElement feedBackEl;
        try {
            feedBackEl = waitForElementFromElement(by, prodResultElem);
        }
        catch (NoSuchElementException e){
            logger.error("cannot get results at car page " + url);
            return false;
        }

        return !feedBackEl.getText().contains("No Results");
    }

    private static WebElement waitForElementFromElement(By by, WebElement searchedEl) {
        WebElement result;
        int retries = 0;
        while (true){
            try {
                result = searchedEl.findElement(by);
                break;
            }
            catch (NoSuchElementException e){
                if (retries>600){
                    if (hasConnection()){
                        logger.error("No element in searched location.");
                        throw new NoSuchElementException("No element in searched location.");
                    }
                    else {
                        retries = 0;
                    }
                }
                sleepForTimeout(100);
                retries++;
            }
        }

        return result;
    }

    public static List<WebElement> getShocks(WebDriver driver) {
        List<WebElement> shockEls;
        WebElement resultsBlockEl = driver.findElement(By.id("ProductResults"));
        WebElement resultsContainer = resultsBlockEl.findElement(By.className("searchList"));
        shockEls = resultsContainer.findElements(By.cssSelector("div[class='row backBox']"));

        return shockEls;
    }

    public static List<WebElement> getSubModelEls(WebDriver driver, String year, String make, String model) {
        getModelEls(driver, year, make);
        Select select = getSelectByID(driver, "engineSelector-model");
        select.selectByVisibleText(model);
        waitForSelect(select);
        select = getSelectByID(driver, "engineSelector-submodel");

        return waitForSelect(select);
    }

    public static List<PrepInfoKeeper> getFinalSubs(WebDriver driver, PrepInfoKeeper keepr) throws NoSelectOptionAvailableException {
        List<PrepInfoKeeper> finalSubs = new ArrayList<>();
        String year = keepr.getYear();
        String make = keepr.getMake();
        String model = keepr.getModel();
        String subModel = keepr.getSubModel();
        getSubModelEls(driver, year, make, model);
        Select select = getSelectByID(driver, "engineSelector-submodel");
        select.selectByVisibleText(subModel);
        if (buttonIsPresent(driver, keepr)){
            PrepInfoKeeper newKeepr = new PrepInfoKeeper(keepr);
            finalSubs.add(newKeepr);
        }
        else {
            finalSubs = getSubsWithAdditionalDrops(driver, keepr);
        }

        return finalSubs;
    }

    /**
     * This method is to be called only after we 100% sure that inlineDrop element is available
     * @param driver - driver we select at least to submodel
     * @param keepr - info keeper for submodel
     * @return list of all available selects for this submodel.
     */
    private static List<PrepInfoKeeper> getSubsWithAdditionalDrops(WebDriver driver, PrepInfoKeeper keepr) throws NoSelectOptionAvailableException {
        List<PrepInfoKeeper> subs = new ArrayList<>();
        String drop = "inlineDrop-"+keepr.getDrop();
        Select select = getSelectByID(driver, drop);
        List<WebElement> dropEls = waitForSelect(select);
        String fieldName = dropEls.get(0).getText();//first value in Drop List is a field name also
        for (int i = 1; i < dropEls.size() ; i++) {
            PrepInfoKeeper newKeepr = new PrepInfoKeeper(keepr);
            select.selectByIndex(i);
            AdditionalField field = getAdditionalField(dropEls.get(i), fieldName);
            newKeepr.getFields().add(field);
            newKeepr.incrementDrop();
            if (buttonIsPresent(driver, newKeepr)){
                subs.add(newKeepr);
            }
            else {
                subs.addAll(getSubsWithAdditionalDrops(driver, newKeepr));
            }
        }

        return subs;
    }

    private static AdditionalField getAdditionalField(WebElement element, String fieldName) {
        String value = element.getText();
        String id = element.getAttribute("value");

        return new AdditionalField(fieldName, value, id);
    }

    private static boolean buttonIsPresent(WebDriver driver, PrepInfoKeeper keepr) throws NoSelectOptionAvailableException {
        int retryCount = 0;
        List<WebElement> buttnElForCheck = null;
        List<WebElement> dropElForCheck = null;
        while(true){
            buttnElForCheck = driver.findElements(By.id("fyvCartBtn"));
            dropElForCheck = driver.findElements(By.id("inlineDrop-"+keepr.getDrop()));
            if (buttnElForCheck.size()!=0||dropElForCheck.size()!=0){
                break;
            }
            else {
                retryCount++;
                sleepForTimeout(100);
                if (retryCount>100){
                    if (hasConnection()){
                        logger.error("No Select option available for " + keepr);
                       throw new NoSelectOptionAvailableException();
                    }
                    else {
                        retryCount = 0;
                    }
                }
            }
        }
        //if button element is found - than "find now" button is available.
        return buttnElForCheck.size()>0;
    }

    public static void sleepForTimeout(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }


    public static WebDriver getShockPage (WebDriver driver, String partNo) {
        By searchFieldBy = By.id("partSearchBox");
        int attempts = 0;
        WebElement searchFieldEl = null;
        while (attempts<30){
            searchFieldEl = waitForElement(searchFieldBy, driver);
            try{
                searchFieldEl.sendKeys(Keys.CONTROL + "a");
            }
            catch (StaleElementReferenceException e){
                searchFieldEl = waitForElement(searchFieldBy, driver);
                break;
            }
            attempts++;
        }

        try{
            searchFieldEl.sendKeys(Keys.CONTROL + "a");
            searchFieldEl.sendKeys(Keys.DELETE);
            searchFieldEl.sendKeys(partNo);
        }
        catch (StaleElementReferenceException e){
            searchFieldEl = waitForElement(searchFieldBy, driver);
            searchFieldEl.sendKeys(Keys.CONTROL + "a");
            searchFieldEl.sendKeys(Keys.DELETE);
            searchFieldEl.sendKeys(partNo);
        }


        By searchBtnBy = By.id("prtNmbFindBtn");
        WebElement searchBtn = waitForElementClickable(driver, searchBtnBy);
        if (searchBtn!=null){
            searchBtn.click();
            sleepForTimeout(2000);
        }

        if (severalResults(driver)){
            WebElement blockEl = SileniumUtil.waitForElement(By.id("recommendation"), driver);
            if (blockEl==null){
                logger.error("no recommendation box at " + driver.getCurrentUrl());
                System.exit(1);
            }
            List<WebElement> shockEls = blockEl.findElements(By.cssSelector("div[class='row backBox']"));
            for (WebElement shockEl: shockEls){
                logger.debug(shockEl.getText());
                if (shockEl.getText().contains(partNo)){
                    shockEl.click();
                    logger.debug(" shock link clicked");
                    break;
                }
            }
        }


        By shockPageBy = By.id("productdetails");
        sleepForTimeout(1000);
        waitForElement(shockPageBy, driver);

        //checkIfSingleSearchResult(driver, partNo);

        return driver;
    }

    private static void checkIfSingleSearchResult(WebDriver driver, String partNo) {
        if (driver.getCurrentUrl().contains("details")){
            return;
        }
        By partBy = By.id("recommendation");
        WebElement searchResultBlock = waitForElement(partBy, driver);
        if (searchResultBlock==null){
            logger.error("no search result block at " + driver.getCurrentUrl());
            System.exit(1);
        }
        List<WebElement> searchResultsEl = searchResultBlock.findElements(By.cssSelector("div[class='row backBox']"));
        if (searchResultsEl.size()==0){
            logger.error("no search results in search block at " + driver.getCurrentUrl());
            System.exit(1);
        }
        for (WebElement searchResult : searchResultsEl) {
            if (searchResult.getText().contains(partNo)) {
                WebElement linkEl = searchResult.findElement(By.tagName("a"));
                linkEl.click();
               break;
            }
        }



    }

    private static boolean severalResults(WebDriver driver) {
        return driver.getCurrentUrl().contains("results");

    }

    public static WebElement waitForElementClickable(WebDriver driver, By elementBy) {
        WebElement searchBtnEl = null;
        int counter = 0;
        while(true){
            try {
                WebDriverWait wait = new WebDriverWait(driver, 10);
                searchBtnEl = wait.until(ExpectedConditions.elementToBeClickable(elementBy));
                break;
            }
            catch (TimeoutException e){
                counter++;
                if (counter>12){
                    if (hasConnection()){
                        logger.error("Element is not clickable");
                        return null;
                    }
                    else {
                        counter=0;
                    }
                }
            }
        }

        return searchBtnEl;
    }
}
