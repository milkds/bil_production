package bilstein.entities;

import javax.persistence.*;

@Entity
@Table(name = "bil_fitments_final")
public class FinalFitment {
    @Id
    @Column(name = "FITMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fitmentID;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private FinalCar car;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "NOTES")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "SHOCK_PART", referencedColumnName = "PART_NO")
    private Shock shock;

    public FinalFitment(Fitment fitment) {
        this.position = fitment.getPosition();
        this.notes = fitment.getNotes();
        this.shock = fitment.getShock();
    }
    public FinalFitment() {
    }

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
    public FinalCar getCar() {
        return car;
    }
    public void setCar(FinalCar car) {
        this.car = car;
    }
}
