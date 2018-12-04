package bilstein.parsers;

import bilstein.BilsteinUtil;
import bilstein.CarBuilder;
import bilstein.SileniumUtil;
import bilstein.entities.Car;
import bilstein.entities.Fitment;
import bilstein.entities.preparse.AdditionalField;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CarParser {
    private static final Logger logger = LogManager.getLogger(CarParser.class.getName());

    public Car parseCar(WebDriver driver, PrepInfoKeeper keepr){
        Car car = this.getBasicCar(keepr);
        String url = BilsteinUtil.buildCarLink(keepr);
        boolean hasShocks = SileniumUtil.getCarPage(driver, url);
        if (hasShocks){
           car.setHasShocks(true);
           List<Fitment> carFitments = getFitments(driver);
           car.setFitments(carFitments);
        }
        else {
            car.setHasShocks(false);
        }

        return car;
    }

    private List<Fitment> getFitments(WebDriver driver) {
        //todo: implement
        return null;
    }

    private Car getBasicCar(PrepInfoKeeper keepr) {
        Car car = new Car();
        car.setModelYear(Integer.parseInt(keepr.getYear()));
        car.setMake(keepr.getMake());
        car.setModel(keepr.getModel());
        car.setSubModel(keepr.getSubModel());

        List<AdditionalField> fields = keepr.getFields();
        if (fields.size()>0){
           for (AdditionalField field: fields){
               setAdditionalField(car, field);
           }
        }

        return car;
    }

    private void setAdditionalField(Car car, AdditionalField field) {
        String fieldName = field.getFieldName();
        String fieldValue = field.getFieldValue();
        switch (fieldName){
            case "Body": car.setBody(fieldValue); break;
            case "BodyManufacturer": car.setBodyMan(fieldValue); break;
            case "Drive": car.setDrive(fieldValue); break;
            case "Doors": car.setDoors(fieldValue); break;
            case "Engine": car.setEngine(fieldValue); break;
            case "Suspension": car.setSuspension(fieldValue); break;
            case "Transmission": car.setTransmission(fieldValue); break;
            default: logger.error("Unknown additional field " + field + " For car " + car);
        }
    }
}
