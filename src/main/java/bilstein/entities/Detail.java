package bilstein.entities;

import javax.persistence.*;

@Entity
@Table(name = "details")
public class Detail {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailID;

    @Column (name = "DETAIL_NAME")
    private String detailName;

    @Column (name = "DETAIL_VALUE")
    private String detailValue;

    @ManyToOne
    @JoinColumn(name = "SHOCK_ID")
    private Shock shock;

    @Override
    public String toString() {
        return "Detail{" +
                "detailID=" + detailID +
                ", detailName='" + detailName + '\'' +
                ", detailValue='" + detailValue + '\'' +
                '}';
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailValue() {
        return detailValue;
    }

    public void setDetailValue(String detailValue) {
        this.detailValue = detailValue;
    }

    public Shock getShock() {
        return shock;
    }

    public void setShock(Shock shock) {
        this.shock = shock;
    }
}
