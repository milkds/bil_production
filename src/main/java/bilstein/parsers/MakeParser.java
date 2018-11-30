package bilstein.parsers;

import bilstein.BilsteinDao;
import bilstein.SileniumUtil;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ymm;
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
        make = keeper.getMake();
        makeID = keeper.getMakeID();
    }

    public int parseMake() {
        int rebootCounter = 0;

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
            carsToParse.addAll(new ModelParser(getDriver(), ymmKeepr, getStartPoint()).parseSubModels());
        }


        return rebootCounter;
    }
}
