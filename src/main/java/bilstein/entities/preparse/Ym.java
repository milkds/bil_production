package bilstein.entities.preparse;

import org.openqa.selenium.WebDriver;

public class Ym extends Y {

    private String make;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Ym(Integer year, String make) {
        super(year);
        this.make = make;
    }
}
