package bilstein.entities;

import javax.persistence.*;

@Entity
@Table(name = "bil_shocks")
public class Shock {

    @Id
    @Column(name = "SH_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shockID;

    @Column(name = "PART_NO", unique = true)
    private String partNo;

    @Column(name = "SERIES")
    private String series;

    @Column(name = "MAIN_IMG_LINK")
    private String mainImgLink;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "MAKE")
    private String make = "Bilstein";

    @Column(name = "COL_LENGTH")
    private String colLength;

    @Column(name = "EXT_LENGTH")
    private String extLength;

    @Column(name = "BODY_DIAMETER")
    private String bodyDiameter;

    @Column(name = "OUTER_HOUSING_DIAMETER")
    private String outerHousingDiameter;

    @Column(name = "CHASSIS_MANUFACTURER")
    private String chassisManufacturer;

    @Column(name = "CHASSIS_MODEL")
    private String chassisModel;

    @Column(name = "CHASSIS_MODEL_EXTENDED")
    private String chassisModelExt;

    @Column(name = "CHASSIS_CLASS")
    private String chassisClass;

    @Column(name = "CHASSIS_YEAR_RANGE")
    private String chassisYearRange;

    @Column(name = "APP_NOTE_1")
    private String appNote1;

    @Column(name = "APP_NOTE_2")
    private String appNote2;

    @Column(name = "COMP_52")
    private String comp52;

    @Column(name = "COMP_26")
    private String comp26;

    @Column(name = "REBOUND_52")
    private String rebound52;

    @Column(name = "REBOUND_26")
    private String rebound26;

    @Column(name = "SUSPENSION_TYPE")
    private String suspensionType;

    @Column(name = "GROSS_VEHICLE_WEIGHT")
    private String grossVehicleWeight;

    @Column(name = "INCL_OUT_TIE_RODS")
    private String includesOutTieRods;


    @Override
    public String toString() {
        return "Shock{" +
                "shockID=" + shockID +
                ", partNo='" + partNo + '\'' +
                ", series='" + series + '\'' +
                ", mainImgLink='" + mainImgLink + '\'' +
                ", productType='" + productType + '\'' +
                ", make='" + make + '\'' +
                ", colLength='" + colLength + '\'' +
                ", extLength='" + extLength + '\'' +
                ", bodyDiameter='" + bodyDiameter + '\'' +
                ", outerHousingDiameter='" + outerHousingDiameter + '\'' +
                ", chassisManufacturer='" + chassisManufacturer + '\'' +
                ", chassisModel='" + chassisModel + '\'' +
                ", chassisModelExt='" + chassisModelExt + '\'' +
                ", chassisClass='" + chassisClass + '\'' +
                ", chassisYearRange='" + chassisYearRange + '\'' +
                ", appNote1='" + appNote1 + '\'' +
                ", appNote2='" + appNote2 + '\'' +
                ", comp52='" + comp52 + '\'' +
                ", comp26='" + comp26 + '\'' +
                ", rebound52='" + rebound52 + '\'' +
                ", rebound26='" + rebound26 + '\'' +
                ", suspensionType='" + suspensionType + '\'' +
                ", grossVehicleWeight='" + grossVehicleWeight + '\'' +
                ", includesOutTieRods='" + includesOutTieRods + '\'' +
                '}';
    }

    public Integer getShockID() {
        return shockID;
    }
    public void setShockID(Integer shockID) {
        this.shockID = shockID;
    }
    public String getPartNo() {
        return partNo;
    }
    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }
    public String getSeries() {
        return series;
    }
    public void setSeries(String series) {
        this.series = series;
    }
    public String getMainImgLink() {
        return mainImgLink;
    }
    public void setMainImgLink(String mainImgLink) {
        this.mainImgLink = mainImgLink;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getColLength() {
        return colLength;
    }
    public void setColLength(String colLength) {
        this.colLength = colLength;
    }
    public String getExtLength() {
        return extLength;
    }
    public void setExtLength(String extLength) {
        this.extLength = extLength;
    }
    public String getBodyDiameter() {
        return bodyDiameter;
    }
    public void setBodyDiameter(String bodyDiameter) {
        this.bodyDiameter = bodyDiameter;
    }
    public String getOuterHousingDiameter() {
        return outerHousingDiameter;
    }
    public void setOuterHousingDiameter(String outerHousingDiameter) {
        this.outerHousingDiameter = outerHousingDiameter;
    }
    public String getChassisManufacturer() {
        return chassisManufacturer;
    }
    public void setChassisManufacturer(String chassisManufacturer) {
        this.chassisManufacturer = chassisManufacturer;
    }
    public String getChassisModel() {
        return chassisModel;
    }
    public void setChassisModel(String chassisModel) {
        this.chassisModel = chassisModel;
    }
    public String getChassisModelExt() {
        return chassisModelExt;
    }
    public void setChassisModelExt(String chassisModelExt) {
        this.chassisModelExt = chassisModelExt;
    }
    public String getChassisClass() {
        return chassisClass;
    }
    public void setChassisClass(String chassisClass) {
        this.chassisClass = chassisClass;
    }
    public String getChassisYearRange() {
        return chassisYearRange;
    }
    public void setChassisYearRange(String chassisYearRange) {
        this.chassisYearRange = chassisYearRange;
    }
    public String getAppNote1() {
        return appNote1;
    }
    public void setAppNote1(String appNote1) {
        this.appNote1 = appNote1;
    }
    public String getAppNote2() {
        return appNote2;
    }
    public void setAppNote2(String appNote2) {
        this.appNote2 = appNote2;
    }
    public String getComp52() {
        return comp52;
    }
    public void setComp52(String comp52) {
        this.comp52 = comp52;
    }
    public String getComp26() {
        return comp26;
    }
    public void setComp26(String comp26) {
        this.comp26 = comp26;
    }
    public String getRebound52() {
        return rebound52;
    }
    public void setRebound52(String rebound52) {
        this.rebound52 = rebound52;
    }
    public String getRebound26() {
        return rebound26;
    }
    public void setRebound26(String rebound26) {
        this.rebound26 = rebound26;
    }
    public String getSuspensionType() {
        return suspensionType;
    }
    public void setSuspensionType(String suspensionType) {
        this.suspensionType = suspensionType;
    }
    public String getGrossVehicleWeight() {
        return grossVehicleWeight;
    }
    public void setGrossVehicleWeight(String grossVehicleWeight) {
        this.grossVehicleWeight = grossVehicleWeight;
    }
    public String getIncludesOutTieRods() {
        return includesOutTieRods;
    }
    public void setIncludesOutTieRods(String includesOutTieRods) {
        this.includesOutTieRods = includesOutTieRods;
    }
}
