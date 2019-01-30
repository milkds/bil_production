package bilstein.entities;

import javax.persistence.*;

@Entity
@Table(name = "product_info")
public class ProductInfo {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int infoID;

    @Column (name = "INFO_NAME")
    private String pName;

    @Column (name = "INFO_VALUE")
    private String pValue;

   @Transient
    private Shock shock;

    @Override
    public String toString() {
        return "ProductInfo{" +
                "infoID=" + infoID +
                ", pName='" + pName + '\'' +
                ", pValue='" + pValue + '\'' +
                '}';
    }

    public int getInfoID() {
        return infoID;
    }

    public void setInfoID(int infoID) {
        this.infoID = infoID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpValue() {
        return pValue;
    }

    public void setpValue(String pValue) {
        this.pValue = pValue;
    }

    public void setShock(Shock rawShock) {
    }

    public Shock getShock() {
        return shock;
    }
}
