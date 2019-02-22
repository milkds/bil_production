package bilstein.parsers;

import bilstein.BilsteinDao;
import bilstein.NoSelectOptionAvailableException;
import bilstein.SileniumUtil;
import bilstein.entities.Car;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakeParser extends YearParser {
    private String make;
    private String makeID;
    private static final Logger logger = LogManager.getLogger(MakeParser.class.getName());

    public MakeParser(WebDriver driver, PrepInfoKeeper keeper, StartPoint startPoint) {
        super(driver, keeper, startPoint);
        this.make = keeper.getMake();
        this.makeID = keeper.getMakeID();
    }

    public int parseMake() throws NoSelectOptionAvailableException {
        Instant start = Instant.now();
        List<WebElement> modelEls = new ArrayList<>();
        try{
            modelEls = SileniumUtil.getModelEls(getDriver(), getYear(), make);
        }
        catch (Exception e){
            logger.error("couldn't get model list for combo: " + getYear() + " " + make);
            throw new NoSelectOptionAvailableException();
        }
        int startID = 1;
        List<PrepInfoKeeper> carsToParse = new ArrayList<>();
        Map<String, String> modelMap = SileniumUtil.getElementMap(modelEls.subList(startID, modelEls.size()));
        for (Map.Entry<String, String> entry: modelMap.entrySet()){
            PrepInfoKeeper ymmKeepr = new PrepInfoKeeper();
            ymmKeepr.setYear(getYear());
            ymmKeepr.setYearID(getYearID());
            ymmKeepr.setMake(make);
            ymmKeepr.setMakeID(makeID);
            ymmKeepr.setModel(entry.getKey());
            ymmKeepr.setModelID(entry.getValue());

            ModelParser modelParser = new ModelParser(getDriver(), ymmKeepr, getStartPoint());
            carsToParse.addAll(modelParser.parseModel());
        }

        List<Car> parsedCars = new ArrayList<>();
        for (PrepInfoKeeper keepr: carsToParse){
            parsedCars.add(new CarParser().parseCar(getDriver(), keepr));
        }

        //this method also marks current year+make combo as parsed
        BilsteinDao.saveCars(parsedCars);

        Instant finish = Instant.now();
        logger.info("Make " + getYear() + " " + make + " parsed in " + Duration.between(start, finish).toMinutes()+ " minutes");

        return parsedCars.size();
    }

    public String getMake() {
        return make;
    }

    public String getMakeID() {
        return makeID;
    }
}
