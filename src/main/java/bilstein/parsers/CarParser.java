package bilstein.parsers;

import bilstein.BilsteinUtil;
import bilstein.CarBuilder;
import bilstein.SileniumUtil;
import bilstein.entities.Car;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.openqa.selenium.WebDriver;

public class CarParser {

    public Car parseCar(WebDriver driver, PrepInfoKeeper keepr){
        Car car = new Car();
        String url = BilsteinUtil.buildCarLink(keepr);
        boolean hasShocks = SileniumUtil.getCarPage(driver, url);
        if (hasShocks){
            car = CarBuilder.buildCar(driver);
        }
        else {
            car = this.getNoShockCar(keepr);
        }

        return car;
    }

    private Car getNoShockCar(PrepInfoKeeper keepr) {
        //todo: implement
        return null;
    }
}
