package bilstein.entities.preparse;

public class AdditionalField {

    private String fieldName;
    private String fieldValue;
    private String linkID;

    public AdditionalField(String fieldName, String fieldValue, String linkID) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.linkID = linkID;
    }

    @Override
    public String toString() {
        return "AdditionalField{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", linkID='" + linkID + '\'' +
                '}';
    }
}
