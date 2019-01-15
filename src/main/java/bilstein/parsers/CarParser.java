package bilstein.parsers;

import bilstein.BilsteinUtil;
import bilstein.CarBuilder;
import bilstein.SileniumUtil;
import bilstein.entities.Car;
import bilstein.entities.Fitment;
import bilstein.entities.Shock;
import bilstein.entities.preparse.AdditionalField;
import bilstein.entities.preparse.PrepInfoKeeper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CarParser {
    private static final Logger logger = LogManager.getLogger(CarParser.class.getName());

    public Car parseCar(WebDriver driver, PrepInfoKeeper keepr){
        Car car = this.getBasicCar(keepr);
        String url = BilsteinUtil.buildCarLink(keepr);
        logger.info("parsing car " + url);
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
        List <Fitment> fitments = new ArrayList<>();
        List<WebElement> shockEls = SileniumUtil.getShocks(driver);

        for (WebElement shockEl: shockEls){
            fitments.add(this.getFitment(shockEl));
        }

        return fitments;
    }

    private Fitment getFitment(WebElement shockEl) {
        Fitment fitment = new Fitment();
        Shock shock = new Shock();
        fitment.setShock(shock);

        StringBuilder noteBuilder = new StringBuilder(); //this needed for case, when notes are present
        List<WebElement> attElems = shockEl.findElements(By.tagName("p"));
        for (WebElement attribute: attElems){
            String attName = "";
            try {
                attName = attribute.findElement(By.tagName("strong")).getText();
                this.setAttribute(fitment, attName, attribute);
            }
            catch (NoSuchElementException e){
                noteBuilder.append(attribute.getText());
                noteBuilder.append(System.lineSeparator());
            }
        }
        int noteLength = noteBuilder.length();
        if (noteLength>0){
           noteBuilder.setLength(noteLength-2);
        }
        fitment.setNotes(noteBuilder.toString());

        this.setItemType(shock, shockEl);
        this.setMainImg(shock, shockEl);

        return fitment;
    }

    private void setMainImg(Shock shock, WebElement shockEl) {
        WebElement imgEl;
        try {
            imgEl = shockEl.findElement(By.tagName("img"));
            String imgLink = imgEl.getAttribute("src");
            imgLink = imgLink.replace("_thu", "_1");
            shock.setMainImgLink(imgLink);
        }
        catch (NoSuchElementException e){
            logger.error("No img for shock - " + shock);
        }
    }

    private void setItemType(Shock shock, WebElement shockEl) {
        WebElement headerEl = shockEl.findElement(By.tagName("h4"));
        String typeLine = headerEl.getText();
        String type = typeLine.replace(shock.getSeries(), "");
        type = type.replace("-", "");
        if (type.length()==0){
            if (typeLine.contains("Steering Damper")){
                type="Steering Damper";
            }
        }
        shock.setProductType(type.trim());
    }

    private void setAttribute(Fitment fitment, String attName, WebElement attribute) {
        //we cannot get att value directly, as its not contained in specific tag. So we remove its name from bulk text.
        String attValue = attribute.getText().replaceAll(attName, "");
        attValue = attValue.trim();
        Shock shock = fitment.getShock();
        switch (attName){
            case "Part Number:": shock.setPartNo(attValue); break;
            case "Series:": shock.setSeries(attValue); break;
            case "Position:": fitment.setPosition(attValue);  break;
            case "Body Diameter:": shock.setBodyDiameter(attValue);  break;
            case "Chassis Manufacturer:": shock.setChassisManufacturer(attValue); break;
            case "Chassis Model:": shock.setChassisModel(attValue); break;
            case "Chassis Model - Extended:": shock.setChassisModelExt(attValue); break;
            case "Chassis Class:": shock.setChassisClass(attValue); break;
            case "Chassis Year Range:": shock.setChassisYearRange(attValue); break;
            case "Application Note 1:": shock.setAppNote1(attValue); break;
            case "Application Note 2:": shock.setAppNote2(attValue); break;
            case "Outer Housing Diameter:": shock.setOuterHousingDiameter(attValue); break;
            case "Compression @0.52m/s:": shock.setComp52(attValue); break;
            case "Rebound @0.52m/s:": shock.setRebound52(attValue); break;
            case "Compression @0.26m/s:": shock.setComp26(attValue); break;
            case "Rebound @0.26m/s:": shock.setRebound26(attValue); break;
            case "Suspension Type:": shock.setSuspensionType(attValue); break;
            case "Gross Vehicle Weight:": shock.setGrossVehicleWeight(attValue); break;
            case "Includes Outer Tie Rods:": shock.setIncludesOutTieRods(attValue); break;
            case "Notes:": break;
            default: logger.error("unexpected attribute in shock list " + attName);
        }
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
