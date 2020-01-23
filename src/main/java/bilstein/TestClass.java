package bilstein;


import bilstein.entities.Car;
import bilstein.entities.Fitment;
import bilstein.entities.Shock;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ym;
import bilstein.parsers.ShockParser;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClass {

    public static void testSavePic(){
        Set<String> picUrls = new HashSet<>();
        Scanner s = null;
        try {
            s = new Scanner(new File("src\\main\\resources\\pics.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (s != null) {
            while (s.hasNext()){
                picUrls.add(s.next());
            }
            s.close();
        }
        Set<String>savedPics = new HashSet<>();
        Scanner s1 = null;
        String savedPicsBulk = "";
        try {
            s1 = new Scanner(new File("src\\main\\resources\\savedpics.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (s1 != null) {
            while (s1.hasNext()){
                savedPicsBulk = s1.next();
            }
            s1.close();
        }
        String[] split = savedPicsBulk.split("jpg");
        for (String s2: split){
            savedPics.add(s2+"jpg");
        }
        picUrls.removeAll(savedPics);
        picUrls.remove("https://productdesk.cart.bilsteinus.com/media/products/bilstein/image_generic_02_1.jpg");

        int total = picUrls.size();
        int counter = 0;
        for (String picUrl : picUrls) {
            String fName = getFname(picUrl);
            try {
                savePic(picUrl, fName);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            logPicSaved(picUrl);
            counter++;
            System.out.println("saved pic " + counter + " of total " + total);
        }

/*
        try(InputStream in = new URL("https://productdesk.cart.bilsteinus.com/media/products/bilstein/48-177580_1.jpg").openStream()){
            try {
                Files.copy(in, Paths.get("C:/logs/image.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private static void logPicSaved(String picUrl) {
        try
        {
            String filename= "src\\main\\resources\\savedpics.txt";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write(picUrl);//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    private static void savePic(String picUrl, String fName) throws IOException {
        try(InputStream in = new URL(picUrl).openStream()){
            try {
                Files.copy(in, Paths.get(fName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFname(String picUrl) {
        String pName = StringUtils.substringAfter(picUrl, "bilstein/");

        return "C:/pics/"+pName;
    }


    public static void testConnection(){
        for (int i = 0; i <5 ; i++) {
            System.out.println(SileniumUtil.hasConnection());
        }
    }

    public static void testFindNowButton(){
        WebDriver driver = SileniumUtil.initBaseDriver();
        WebElement yearDrop = driver.findElement(By.id("engineSelector-year"));
        Select yearSelect = new Select(yearDrop);
        List<WebElement> yearEls = SileniumUtil.waitForSelect(yearSelect);
        yearSelect.selectByIndex(2);

        WebElement makeDrop = driver.findElement(By.id("engineSelector-make"));
        Select makeSelect = new Select(makeDrop);
        List<WebElement> makeEls = SileniumUtil.waitForSelect(makeSelect);
        makeSelect.selectByIndex(1);

        WebElement modelDrop = driver.findElement(By.id("engineSelector-model"));
        Select modelSelect = new Select(modelDrop);
        List<WebElement> modelEls = SileniumUtil.waitForSelect(modelSelect);
        modelSelect.selectByIndex(1);

        WebElement subModelDrop = driver.findElement(By.id("engineSelector-submodel"));
        Select subModelSelect = new Select(subModelDrop);
        List<WebElement> subEls = SileniumUtil.waitForSelect(subModelSelect);
        subModelSelect.selectByIndex(1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,"t");

       // driver.findElement(By.cssSelector("body")).sendKeys(selectLinkOpeninNewTab);
        driver.findElement(By.tagName("body")).sendKeys(selectLinkOpeninNewTab);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1)); //switches to new tab
        driver.get("https://cart.bilsteinus.com/results?yearid=8043155490883070947&makeid=5245621076805039561&modelid=4329471338455282440&submodelid=78192055976998175");

        WebElement btn = driver.findElement(By.id("fyvCartBtn"));
        btn.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.switchTo().window(tabs.get(0)); // switch back to main screen
        modelSelect.selectByIndex(2);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();
    }

    public static void testJsoup(){
       // String url = "https://cart.bilsteinus.com/results?yearid=8043155490883070947&makeid=5245621076805039561&modelid=4329471338455282440&submodelid=78192055976998175";
        String url = "https://cart.bilsteinus.com/results?yearid=8728504222979438496&makeid=5258438399650964694&modelid=8696793766038085434&submodelid=4718153873425065578";
        Document doc = null;
        try {
             doc = Jsoup.connect(url).timeout(12000).followRedirects(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.body().select("*");

        for (Element element : elements) {
            System.out.println(element.ownText());
        }


    }

    public static void testKeepr(){
        PrepInfoKeeper keeper = new PrepInfoKeeper();
        System.out.println(keeper.getDrop());
        keeper.incrementDrop();
        System.out.println(keeper.getDrop());
    }

    public static void testShockPage(){
        WebDriver driver = SileniumUtil.initBaseDriver();
        driver.get("https://cart.bilsteinus.com/results?yearid=3431244765800985788&makeid=4050365326053705778&modelid=5583366737204764835&submodelid=5884575516905470520&Dr=2267534288709343027");
        sleepForTimeout(5000);
        List<WebElement> shocks = SileniumUtil.getShocks(driver);
        for (WebElement element: shocks){
            List<WebElement> pTag = element.findElements(By.tagName("p"));
            for (WebElement innEl: pTag){
               try{
                   WebElement strEl = innEl.findElement(By.tagName("strong"));
               }
               catch (NoSuchElementException e){
                  // System.out.println(innEl.getText());
                   Fitment fitment = new Fitment();
                   fitment.setPosition("Front");
                   fitment.setNotes(innEl.getText());

                   System.out.println(fitment);
               }
            }
            WebElement headr = element.findElement(By.tagName("h4"));
        }

        driver.close();
    }

    private static void sleepForTimeout(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    public static void testNotesString(){
        String test = "Fitment{" +
                "fitmentID=" + "123" +
                ", carID=" + "321" +
                ", position='" + "front" + '\'' +
                ", notes='" + "-Without Electronic Suspension" + '\'' +
                '}';

        System.out.println(test);
    }

    public static void saveCarTest(){
        Car car = new Car();
        car.setModelYear(2015);
        car.setMake("Ford");
        car.setModel("F-150");
        car.setSubModel("Lariat");
        car.setDrive("4WD");

        List<Fitment> fitments = new ArrayList<>();
        Fitment fitment1 = new Fitment();
        fitment1.setPosition("Front");
        fitment1.setNotes("first test fitment Note");
        Fitment fitment2 = new Fitment();
        fitment2.setPosition("Rear");
        fitment2.setNotes("second test fitment Note");

        Shock shock1 = new Shock();
        shock1.setSeries("7600");
        shock1.setPartNo("Test Part 3");
        shock1.setMainImgLink("www.leningrad.spb.ru");
        shock1.setProductType("Shock");
        shock1.setIncludesOutTieRods("Yes");
        shock1.setGrossVehicleWeight("3 tonnes");
        shock1.setSuspensionType("test suspension 1");
        shock1.setRebound26("rebound 26");
        shock1.setComp26("compression 26");
        shock1.setRebound52("rebound 52");
        shock1.setComp52("compression 52");
        shock1.setOuterHousingDiameter("100");
        shock1.setAppNote2("Application Note 2");
        shock1.setAppNote1("Application Note 1");
        shock1.setChassisYearRange("2001-2015");
        shock1.setChassisClass("SuperClass");
        shock1.setChassisModel("Best model");
        shock1.setChassisModelExt("Best chassis model for Ford");
        shock1.setChassisManufacturer("Ford");
        shock1.setBodyDiameter("80");
        shock1.setColLength("50");
        shock1.setExtLength("60");

        Shock shock2 = new Shock();
        shock2.setSeries("8600");
        shock2.setPartNo("Test Part 4");
        shock2.setMainImgLink("www.olx.ua");
        shock2.setProductType("Shock");
        shock2.setIncludesOutTieRods("Yes");
        shock2.setGrossVehicleWeight("3 tonnes");
        shock2.setSuspensionType("test suspension 2");
        shock2.setRebound26("rebound 26");
        shock2.setComp26("compression 26");
        shock2.setRebound52("rebound 52");
        shock2.setComp52("compression 52");
        shock2.setOuterHousingDiameter("100");
        shock2.setAppNote2("Test Application Note 2");
        shock2.setAppNote1("Test Application Note 1");
        shock2.setChassisYearRange("2001-2015");
        shock2.setChassisClass("SuperClass");
        shock2.setChassisModelExt("Best chassis model for Ford");
        shock2.setChassisManufacturer("Ford");
        shock2.setBodyDiameter("80");
        shock2.setColLength("50");
        shock2.setExtLength("60");

        fitment1.setShock(shock1);
        fitment2.setShock(shock2);

        fitments.add(fitment1);
        fitments.add(fitment2);

        car.setFitments(fitments);
        car.setHasShocks(true);

        List<Car> cars = new ArrayList<>();
        cars.add(car);
        BilsteinDao.saveCars(cars);

        HibernateUtil.shutdown();
    }

    public static void saveYmsTest(){
        List<Ym> yms = new ArrayList<>();
        Ym ym0 = new Ym();
        ym0.setYear(2012);
        ym0.setMake("Ford");
       /* Ym ym1 = new Ym();
        ym1.setYear(2017);
        ym1.setMake("Toyota");
        Ym ym2 = new Ym();
        ym2.setYear(2017);
        ym2.setMake("Volkswagen");*/

        yms.add(ym0);
       /* yms.add(ym1);
        yms.add(ym2);*/

        BilsteinDao.saveYms(yms);
        HibernateUtil.shutdown();
    }

    public static void testBoolSave(){
        Ym ym = BilsteinDao.testMethod(2012, "Ford");
        System.out.println(ym);
        HibernateUtil.shutdown();
    }

    public static void testLaunch(int year, String make){
        new ParseLauncher().launchPreParseFromPauseTillEnd(year, make);
    }

    public static void proxyTest(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions().addArguments("--proxy-server=http://" + "65.49.137.16:46386");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://superlift.com/");
    }

    public static void shockParse(){
        WebDriver driver = SileniumUtil.initBaseDriver();
        driver = SileniumUtil.getShockPage(driver, "24-188227");
        Shock detailedShock = new ShockParser(driver, new Shock()).parse();
        driver.close();
    }

    public static void getShocks(){
        BilsteinDao.getRawShocks3();
        HibernateUtil.shutdown();
    }

    public static void testMatcherReg(){
        String guideStr = "12005-20101asas";
       String regexp = "(\\d\\d\\d\\d-\\d\\d\\d\\d)";
     //   String regexp = "2005";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(guideStr);
        if (matcher.find()){
            System.out.println("found");
            System.out.println(matcher.group());
        }
    }

    public static void testBuyerGuide(){
     //   AfterParseProcessor.processBuyersGuide();
        AfterParseProcessor.setYearStartFinish();
    }
}
