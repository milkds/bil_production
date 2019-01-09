package bilstein.parsers;

import bilstein.BilsteinDao;
import bilstein.NoSelectOptionAvailableException;
import bilstein.SileniumUtil;
import bilstein.entities.Car;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakeParser extends YearParser {
    private String make;
    private String makeID;

    public MakeParser(WebDriver driver, PrepInfoKeeper keeper, StartPoint startPoint) {
        super(driver, keeper, startPoint);
        this.make = keeper.getMake();
        this.makeID = keeper.getMakeID();
    }

    public int parseMake() throws NoSelectOptionAvailableException {
        List<WebElement> modelEls = SileniumUtil.getModelEls(getDriver(), getYear(), make);
        int startID = 1;
        List<PrepInfoKeeper> carsToParse = new ArrayList<>();
        //todo: develop possibility of parsing single make
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

        //
        for (PrepInfoKeeper keepr: carsToParse){
            parsedCars.add(new CarParser().parseCar(getDriver(), keepr));
        }

        //test section - to be deleted in prod.
        for (Car car: parsedCars){
            if (car.hasShocks()){
                car.getFitments().forEach(System.out::println);
            }
        }
        //////////////////

        //this method also marks current year+make combo as parsed
        BilsteinDao.saveCars(parsedCars);

        return parsedCars.size();
    }

    public String getMake() {
        return make;
    }

    public String getMakeID() {
        return makeID;
    }
}
