package bilstein.entities;

public class StartPoint {
   private int yearID;
   private int makeID;
   private int modelID;
   private int subModelID;

    public int getYearID() {
        return yearID;
    }

    public void setYearID(int yearID) {
        this.yearID = yearID;
    }

    public int getMakeID() {
        return makeID;
    }

    public void setMakeID(int makeID) {
        this.makeID = makeID;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public int getSubModelID() {
        return subModelID;
    }

    public void setSubModelID(int subModelID) {
        this.subModelID = subModelID;
    }
}
