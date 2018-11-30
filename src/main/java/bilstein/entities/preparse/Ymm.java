package bilstein.entities.preparse;

import org.openqa.selenium.WebDriver;

public class Ymm extends Ym {
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Ymm(Integer year, String make, String model) {
        super(year, make);
        this.model = model;
    }
}
