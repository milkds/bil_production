package bilstein.entities;

import javax.persistence.*;

@Entity
@Table(name = "bil_fitments")
public class Fitment {

    @Id
    @Column(name = "FITMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fitmentID;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private Car car;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "NOTES")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "SHOCK_PART", referencedColumnName = "PART_NO")
    private Shock shock;

    @Override
    public String toString() {
        return "Fitment{" +
                "fitmentID=" + fitmentID +
                ", car=" + car +
                ", position='" + position + '\'' +
                ", notes='" + notes + '\'' +
                ", shock=" + shock +
                '}';
    }

    public void setShock(Shock shock) {
        this.shock = shock;
    }
    public Shock getShock() {
        return shock;
    }
    public Integer getFitmentID() {
        return fitmentID;
    }
    public void setFitmentID(Integer fitmentID) {
        this.fitmentID = fitmentID;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }
}
