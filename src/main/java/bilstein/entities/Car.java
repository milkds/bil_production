package bilstein.entities;

import javax.persistence.Column;
import java.util.List;

public class Car {

    private Integer modelYear;
    private String make;
    private String model;
    private String subModel;

    @Column(name = "CAR_BODY")
    private String body;

    @Column(name = "CAR_BODY_MANUFACTURER")
    private String bodyMan;

    @Column(name = "CAR_DRIVE")
    private String drive;

    @Column(name = "CAR_DOORS")
    private String doors;

    @Column(name = "CAR_ENGINE")
    private String engine;

    @Column(name = "CAR_SUSPENSION")
    private String suspension;

    @Column(name = "CAR_TRANSMISSION")
    private String transmission;

    private Boolean hasShocks;

    private List<Fitment> fitments;

    @Override
    public String toString() {
        return "Car{" +
                "modelYear=" + modelYear +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", subModel='" + subModel + '\'' +
                ", body='" + body + '\'' +
                ", bodyMan='" + bodyMan + '\'' +
                ", drive='" + drive + '\'' +
                ", doors='" + doors + '\'' +
                ", engine='" + engine + '\'' +
                ", suspension='" + suspension + '\'' +
                ", transmission='" + transmission + '\'' +
                '}';
    }

    public Integer getModelYear() {
        return modelYear;
    }
    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getSubModel() {
        return subModel;
    }
    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getBodyMan() {
        return bodyMan;
    }
    public void setBodyMan(String bodyMan) {
        this.bodyMan = bodyMan;
    }
    public String getDrive() {
        return drive;
    }
    public void setDrive(String drive) {
        this.drive = drive;
    }
    public String getDoors() {
        return doors;
    }
    public void setDoors(String doors) {
        this.doors = doors;
    }
    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }
    public String getSuspension() {
        return suspension;
    }
    public void setSuspension(String suspension) {
        this.suspension = suspension;
    }
    public String getTransmission() {
        return transmission;
    }
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    public Boolean getHasShocks() {
        return hasShocks;
    }
    public void setHasShocks(Boolean hasShocks) {
        this.hasShocks = hasShocks;
    }
    public List<Fitment> getFitments() {
        return fitments;
    }
    public void setFitments(List<Fitment> fitments) {
        this.fitments = fitments;
    }
}
