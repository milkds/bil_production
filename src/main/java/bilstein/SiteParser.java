package bilstein;

import bilstein.entities.Car;
import bilstein.entities.StartPoint;
import org.hibernate.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class SiteParser {

    public SiteParser(Session session) {
        this.session = session;
    }

    private Session session;

    public void parseYear(WebDriver driver, StartPoint startPoint){
        WebElement drop = driver.findElement(By.id("engineSelector-year"));
        Select select = new Select(drop);
        List<WebElement> options = waitForSelect(select);
        for (int i = startPoint.getMakeID(); i < options.size(); i++) {
            select.selectByIndex(i);
            parseMake(driver, startPoint);
        }
    }

    public void parseMake(WebDriver driver, StartPoint startPoint) {
        WebElement drop = driver.findElement(By.id("engineSelector-make"));
        Select select = new Select(drop);
        List<WebElement> options = waitForSelect(select);
        for (int i = startPoint.getModelID(); i < options.size(); i++) {
            select.selectByIndex(i);
            parseModel(driver, startPoint);
        }
    }

    private void parseModel(WebDriver driver, StartPoint startPoint) {
        WebElement drop = driver.findElement(By.id("engineSelector-model"));
        Select select = new Select(drop);
        List<WebElement> options = waitForSelect(select);
        for (int i = startPoint.getModelID(); i < options.size(); i++) {
            select.selectByIndex(i);
            parseSubModel(driver, startPoint);
        }
    }

    private void parseSubModel(WebDriver driver, StartPoint startPoint) {
        List<WebElement> subModelOptions = null;
        for (int i = startPoint.getSubModelID(); i < subModelOptions.size(); i++) {
           //implement subModel select
            if (!findNowAvailable(driver)){
                selectAdditionalFields(driver, 0);
            }
            Car car = CarBuilder.buildCar(driver);
            BilsteinDao.saveCar(car, session);
        }
    }

    private void selectAdditionalFields(WebDriver driver, int fieldCount) {

    }


    private boolean findNowAvailable(WebDriver driver) {
        //todo: implement

        return false;
    }

    private static List<WebElement> waitForSelect(Select select){
        List<WebElement> options = select.getOptions();

        while (options.size()<2){
            options = select.getOptions();
        }

        return options;
    }
}
