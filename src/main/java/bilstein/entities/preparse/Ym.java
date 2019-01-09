package bilstein.entities.preparse;

import bilstein.entities.StartPoint;
import org.hibernate.annotations.Type;
import org.openqa.selenium.WebDriver;

import javax.persistence.*;

@Entity
@Table(name = "preparse")
public class Ym {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "MAKE")
    private String make;

    @Column(name="MAKE_PARSED")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean makeParsed;

    @Transient
    private StartPoint startPoint;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StartPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(StartPoint startPoint) {
        this.startPoint = startPoint;
    }

    public boolean getMakeParsed() {
        return makeParsed;
    }

    public void setMakeParsed(boolean makeParsed) {
        this.makeParsed = makeParsed;
    }

    @Override
    public String toString() {
        return "Ym{" +
                "id=" + id +
                ", year=" + year +
                ", make='" + make + '\'' +
                ", makeParsed=" + makeParsed +
                ", startPoint=" + startPoint +
                '}';
    }
}
