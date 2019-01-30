package bilstein.entities;

import javax.persistence.*;

@Entity
@Table(name = "bguide")
public class BuyersGuide {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buyID;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "YEAR_START")
    private Integer yearStart;

    @Column(name = "YEAR_FINISH")
    private Integer yearFinish;

    @ManyToOne
    @JoinColumn(name = "SHOCK_PART", referencedColumnName = "PART_NO")
    private Shock shock;

    @Override
    public String toString() {
        return "BuyersGuide{" +
                "buyID=" + buyID +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", yearStart=" + yearStart +
                ", yearFinish=" + yearFinish +
                '}';
    }

    public Integer getBuyID() {
        return buyID;
    }

    public void setBuyID(Integer buyID) {
        this.buyID = buyID;
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

    public Shock getShock() {
        return shock;
    }

    public void setShock(Shock shock) {
        this.shock = shock;
    }
}
