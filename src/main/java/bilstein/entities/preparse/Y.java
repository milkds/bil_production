package bilstein.entities.preparse;

import bilstein.entities.StartPoint;

public class Y {

    private Integer year;
    private StartPoint startPoint;

    public Y(Integer year) {
        this.year = year;
    }

    public StartPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(StartPoint startPoint) {
        this.startPoint = startPoint;
    }
}
