package bilstein.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bil_shocks")
public class Shock implements Serializable {

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

    @Column(name="DETAILS_PARSED")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean detailsParsed;

    @Column(name = "IMG_LINKS")
    private String imgLinks;

    @Column(name = "PRODUCT_DESC")
    private String productDesc;

    @Column(name = "BUYERS_GUIDE")
    private String buyersGuide;

    @Column(name = "DOC_LINKS")
    private String docLinks;

    @Column(name = "QTY_PER_VEHICLE")
    private String qtyPerVehicle;

    @Column(name = "WARRANTY")
    private String warranty;

    @Column(name = "FINISH")
    private String finish;

    @Column(name = "RESERVOIR")
    private String reservoir;

    @Column(name = "BODY_DESIGN")
    private String bodyDesign;

    @Column(name = "OPTIONAL_RES_CLAMP")
    private String optResClamp;

    @Column(name = "KIT_CONTENTS")
    private String kitContents;

    @Column(name = "OPTIONAL_HL_SPRINGS")
    private String optHLSprings;

    @Column(name = "ADJUSTABLE_DAMPING")
    private String adjDamping;

    @Column(name = "ITEM_TYPE_STEERING_RACKS")
    private String itemTypeSteerRacks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shock")
    private List<Spec> specs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shock")
    private List<Detail> details;

    @Transient
    private List<ProductInfo> pInfos;

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
    public boolean detailsParsed() {
        return detailsParsed;
    }
    public void setDetailsParsed(boolean detailsParsed) {
        this.detailsParsed = detailsParsed;
    }
    public String getImgLinks() {
        return imgLinks;
    }
    public void setImgLinks(String imgLinks) {
        this.imgLinks = imgLinks;
    }
    public boolean isDetailsParsed() {
        return detailsParsed;
    }
    public String getProductDesc() {
        return productDesc;
    }
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
    public String getBuyersGuide() {
        return buyersGuide;
    }
    public void setBuyersGuide(String buyersGuide) {
        this.buyersGuide = buyersGuide;
    }
    public String getDocLinks() {
        return docLinks;
    }
    public void setDocLinks(String docLinks) {
        this.docLinks = docLinks;
    }
    public List<Spec> getSpecs() {
        return specs;
    }
    public void setSpecs(List<Spec> specs) {
        this.specs = specs;
    }
    public List<Detail> getDetails() {
        return details;
    }
    public void setDetails(List<Detail> details) {
        this.details = details;
    }
    public List<ProductInfo> getpInfos() {
        return pInfos;
    }
    public void setpInfos(List<ProductInfo> pInfos) {
        this.pInfos = pInfos;
    }
    public String getQtyPerVehicle() {
        return qtyPerVehicle;
    }
    public void setQtyPerVehicle(String qtyPerVehicle) {
        this.qtyPerVehicle = qtyPerVehicle;
    }
    public String getWarranty() {
        return warranty;
    }
    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }
    public String getFinish() {
        return finish;
    }
    public void setFinish(String finish) {
        this.finish = finish;
    }
    public String getReservoir() {
        return reservoir;
    }
    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }
    public String getBodyDesign() {
        return bodyDesign;
    }
    public void setBodyDesign(String bodyDesign) {
        this.bodyDesign = bodyDesign;
    }
    public String getOptResClamp() {
        return optResClamp;
    }
    public void setOptResClamp(String optResClamp) {
        this.optResClamp = optResClamp;
    }
    public String getKitContents() {
        return kitContents;
    }
    public void setKitContents(String kitContents) {
        this.kitContents = kitContents;
    }
    public String getOptHLSprings() {
        return optHLSprings;
    }
    public void setOptHLSprings(String optHLSprings) {
        this.optHLSprings = optHLSprings;
    }
    public String getAdjDamping() {
        return adjDamping;
    }
    public void setAdjDamping(String adjDamping) {
        this.adjDamping = adjDamping;
    }
    public String getItemTypeSteerRacks() {
        return itemTypeSteerRacks;
    }
    public void setItemTypeSteerRacks(String itemTypeSteerRacks) {
        this.itemTypeSteerRacks = itemTypeSteerRacks;
    }
}
