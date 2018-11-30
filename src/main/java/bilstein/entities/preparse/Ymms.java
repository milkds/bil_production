package bilstein.entities.preparse;

import org.openqa.selenium.WebDriver;

public class Ymms extends Ymm {

    private String subModel;

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    public Ymms(Integer year, String make, String model, String subModel) {
        super(year, make, model);
        this.subModel = subModel;
    }
}
