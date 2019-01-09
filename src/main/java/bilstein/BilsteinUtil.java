package bilstein;

import bilstein.entities.StartPoint;
import bilstein.entities.preparse.AdditionalField;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ym;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class BilsteinUtil {

    private static final String BILSTEIN_CAR_URL = "https://cart.bilsteinus.com/results?yearid=";
   // private static final Logger logger = LogManager.getLogger(BilsteinUtil.class.getName());


    public static StartPoint getStartPoint(int year, String make) {
        StartPoint startPoint = new StartPoint();
        String yearString = year+"";
        WebDriver driver = SileniumUtil.initBaseDriver();
        List<WebElement> yearEls = SileniumUtil.getYearElements(driver);
        int yearID = SileniumUtil.getElementID(yearEls, yearString);
        List<WebElement> makeEls = SileniumUtil.getMakeEls(driver, yearString);
        int makeID = SileniumUtil.getElementID(makeEls, make);

        startPoint.setYearID(yearID);
        startPoint.setMakeID(makeID);

        driver.close();

        return startPoint;
    }

    public static String buildCarLink(PrepInfoKeeper keepr) {
        //standard part
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(BILSTEIN_CAR_URL);
        linkBuilder.append(keepr.getYearID());
        linkBuilder.append("&makeid=");
        linkBuilder.append(keepr.getMakeID());
        linkBuilder.append("&modelid=");
        linkBuilder.append(keepr.getModelID());
        linkBuilder.append("&submodelid=");
        linkBuilder.append(keepr.getSubModelID());

        //in case additional fields are present.
        List<AdditionalField> fields = keepr.getFields();
        for (AdditionalField field: fields){
            linkBuilder.append("&");
            String fName = field.getFieldName();
            //BodyManufacturer starts with Bo, but this is reserved for Body attribute - so need to check.
            if (fName.equals("BodyManufacturer")){
                linkBuilder.append("BM");
            }
            else {
                linkBuilder.append(fName, 0, 2);
            }
            linkBuilder.append("=");
            linkBuilder.append(field.getLinkID());
        }

        return linkBuilder.toString();
    }
}
