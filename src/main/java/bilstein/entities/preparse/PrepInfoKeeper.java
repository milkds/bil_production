package bilstein.entities.preparse;

import java.util.ArrayList;
import java.util.List;

public class PrepInfoKeeper {

    private String year;
    private String yearID;
    private String make;
    private String makeID;
    private String model;
    private String modelID;
    private String subModel;
    private String subModelID;

    private Integer drop = 0;
    private List<AdditionalField> fields;

    public PrepInfoKeeper() {
        fields = new ArrayList<>();
    }
    public PrepInfoKeeper(PrepInfoKeeper anotherKeepr) {
        this.year = anotherKeepr.getYear();
        this.yearID = anotherKeepr.getYearID();
        this.make = anotherKeepr.getMake();
        this.makeID = anotherKeepr.getMakeID();
        this.model = anotherKeepr.getModel();
        this.modelID = anotherKeepr.getModelID();
        this.subModel = anotherKeepr.getSubModel();
        this.subModelID = anotherKeepr.getSubModelID();
        fields = new ArrayList<>();
        fields.addAll(anotherKeepr.getFields());
    }

    public void incrementDrop (){
        drop++;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearID() {
        return yearID;
    }

    public void setYearID(String yearID) {
        this.yearID = yearID;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getMakeID() {
        return makeID;
    }

    public void setMakeID(String makeID) {
        this.makeID = makeID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    public String getSubModelID() {
        return subModelID;
    }

    public void setSubModelID(String subModelID) {
        this.subModelID = subModelID;
    }

    public Integer getDrop() {
        return drop;
    }

    public void setDrop(Integer drop) {
        this.drop = drop;
    }

    public List<AdditionalField> getFields() {
        return fields;
    }


    @Override
    public String toString() {
        return "PrepInfoKeeper{" +
                "year='" + year + '\'' +
                ", yearID='" + yearID + '\'' +
                ", make='" + make + '\'' +
                ", makeID='" + makeID + '\'' +
                ", model='" + model + '\'' +
                ", modelID='" + modelID + '\'' +
                ", subModel='" + subModel + '\'' +
                ", subModelID='" + subModelID + '\'' +
                ", drop=" + drop +
                ", fields=" + fields +
                '}';
    }
}
