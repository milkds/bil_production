package bilstein;

import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ym;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class BilsteinUtil {

    private static final String BILSTEIN_URL = "https://cart.bilsteinus.com/";
    private static final Logger logger = LogManager.getLogger(BilsteinUtil.class.getName());


    public static StartPoint getStartPoint() {
        //todo: implement
        return null;
    }

    public static String buildCarLink(PrepInfoKeeper keepr) {
        //todo: implement
        return null;
    }
}
