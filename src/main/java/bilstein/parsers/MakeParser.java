package bilstein.parsers;

import bilstein.BilsteinDao;
import bilstein.NoSelectOptionAvailableException;
import bilstein.SileniumUtil;
import bilstein.entities.Car;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ymm;
import bilstein.entities.preparse.Ymms;
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
        List<Ymm> ymms = new ArrayList<>();
        int yearInt = Integer.parseInt(getYear());
        for (int i = 1; i <modelEls.size() ; i++) {
            String model = modelEls.get(i).getText();
            Ymm ymm = new Ymm(yearInt, make, model);
            ymms.add(ymm);
        }
        BilsteinDao.saveYmms(ymms);

        int startID = 1;
        int startPointID = getStartPoint().getModelID();
        if (startPointID!=0){
            startID = startPointID;
        }

        List<PrepInfoKeeper> carsToParse = new ArrayList<>();
        List<Ymms> subsToSave = new ArrayList<>();
        //todo: develop possibility of parsing single model
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
            carsToParse.addAll(modelParser.parseSubModels());
            subsToSave.addAll(modelParser.getSubsToSave()); //this needed to make sure we save info about subs only when all models parsed.
        }
        List<Car> parsedCars = new ArrayList<>();

        for (PrepInfoKeeper keepr: carsToParse){
            parsedCars.add(new CarParser().parseCar(getDriver(), keepr));
        }

        BilsteinDao.saveCars(parsedCars, subsToSave);

        return parsedCars.size();
    }

    public String getMake() {
        return make;
    }

    public String getMakeID() {
        return makeID;
    }
}
