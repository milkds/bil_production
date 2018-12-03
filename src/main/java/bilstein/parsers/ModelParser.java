package bilstein.parsers;

import bilstein.BilsteinDao;
import bilstein.NoSelectOptionAvailableException;
import bilstein.SileniumUtil;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ymms;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelParser extends MakeParser {

    private String model;
    private String modelID;
    private List<Ymms> subsToSave;

    public ModelParser(WebDriver driver, PrepInfoKeeper keeper, StartPoint startPoint) {
        super(driver, keeper, startPoint);
        this.model = keeper.getModel();
        this.modelID = keeper.getModelID();
    }

    public List<PrepInfoKeeper> parseSubModels() throws NoSelectOptionAvailableException {
        List<WebElement> subModelEls = SileniumUtil.getSubModelEls(getDriver(), getYear(), getMake(), model);
        List<Ymms>ymmses = new ArrayList<>();
        int yearInt = Integer.parseInt(getYear());
        for (int i = 1; i < subModelEls.size(); i++) {
            String subModel = subModelEls.get(i).getText();
            Ymms ymms = new Ymms(yearInt, getMake(), model, subModel);
            ymmses.add(ymms);
        }
       // BilsteinDao.saveYmmses(ymmses);
        subsToSave = ymmses;

        int startID = 1;
        int startPointID = getStartPoint().getSubModelID();
        if (startPointID!=0){
            startID = startPointID;
        }
        Map<String, String> subModelMap = SileniumUtil.getElementMap(subModelEls.subList(startID, subModelEls.size()));

        List<PrepInfoKeeper> carsToParse = new ArrayList<>();
        for (Map.Entry<String, String> entry: subModelMap.entrySet()){
            PrepInfoKeeper ymmKeepr = new PrepInfoKeeper();
            ymmKeepr.setYear(getYear());
            ymmKeepr.setYearID(getYearID());
            ymmKeepr.setMake(getMake());
            ymmKeepr.setMakeID(getMakeID());
            ymmKeepr.setModel(model);
            ymmKeepr.setModelID(modelID);
            ymmKeepr.setSubModel(entry.getKey());
            ymmKeepr.setSubModelID(entry.getValue());
            carsToParse.addAll(new SubModelParser(getDriver(), ymmKeepr, getStartPoint()).finishPreParse());
        }

        return carsToParse;
    }

    public List<Ymms> getSubsToSave() {
        return subsToSave;
    }
}
