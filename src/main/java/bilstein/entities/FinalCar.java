package bilstein.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bil_cars_final")
public class FinalCar {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carID;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "SUBMODEL")
    private String subModel;

    @Column(name = "BODY")
    private String body;

    @Column(name = "BODY_MANUFACTURER")
    private String bodyMan;

    @Column(name = "DRIVE")
    private String drive;

    @Column(name = "DOORS")
    private String doors;

    @Column(name = "ENGINE")
    private String engine;

    @Column(name = "SUSPENSION")
    private String suspension;

    @Column(name = "TRANSMISSION")
    private String transmission;

    @Column(name = "YEAR_START")
    private Integer yearStart;

    @Column(name = "YEAR_FINISH")
    private Integer yearFinish;

    @Transient
    private Boolean hasShocks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<FinalFitment> fitments;

    public FinalCar() {
    }
    public FinalCar(Car car) {
        this.make = car.getMake();
        this.model = car.getModel();
        this.subModel = car.getSubModel();
        this.body = car.getBody();
        this.bodyMan = car.getBodyMan();
        this.drive = car.getDrive();
        this.doors = car.getDoors();
        this.engine = car.getEngine();
        this.suspension = car.getSuspension();
        this.transmission = car.getTransmission();
        this.yearStart = car.getYearStart();
        this.yearFinish = car.getYearFinish();
    }

    @Override
    public String toString() {
        return "Car{" +
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
    public Integer getCarID() {
        return carID;
    }
    public void setCarID(Integer carID) {
        this.carID = carID;
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
    public Boolean hasShocks() {
        return hasShocks;
    }
    public void setHasShocks(Boolean hasShocks) {
        this.hasShocks = hasShocks;
    }
    public List<FinalFitment> getFitments() {
        return fitments;
    }
    public void setFitments(List<FinalFitment> fitments) {
        this.fitments = fitments;
    }
    public Integer getYearStart() {
        return yearStart;
    }
    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }
    public Integer getYearFinish() {
        return yearFinish;
    }
    public void setYearFinish(Integer yearFinish) {
        this.yearFinish = yearFinish;
    }
}
