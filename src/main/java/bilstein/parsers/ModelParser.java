package bilstein.parsers;

import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class ModelParser extends MakeParser {
    public ModelParser(WebDriver driver, PrepInfoKeeper keeper, StartPoint startPoint) {
        super(driver, keeper, startPoint);
    }

    public List<PrepInfoKeeper> parseSubModels() {
        //todo: implement
        System.out.println(getKeepr());
        return new ArrayList<>();
    }
}
