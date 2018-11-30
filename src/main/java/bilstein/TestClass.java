package bilstein;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.print;

public class TestClass {

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
}
