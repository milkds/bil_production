package bilstein.parsers;

import bilstein.NoSelectOptionAvailableException;
import bilstein.SileniumUtil;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SubModelParser extends ModelParser  {


    public SubModelParser(WebDriver driver, PrepInfoKeeper keeper, StartPoint startPoint) {
        super(driver, keeper, startPoint);
    }


    public List<PrepInfoKeeper> finishPreParse() throws NoSelectOptionAvailableException {
        List<PrepInfoKeeper> completeSubs = SileniumUtil.getFinalSubs(getDriver(), getKeepr());
        for  (PrepInfoKeeper keeper: completeSubs){
            if (keeper.getFields().size()>0){
                System.out.println(keeper);
            }
        }

        return completeSubs;
    }
}
