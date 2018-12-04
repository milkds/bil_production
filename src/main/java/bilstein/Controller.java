package bilstein;

public class Controller {

    private void parseAllSite(){
        //get start point

        //iterate years from start point
        //iterate makes from start point
        //iterate model from start point
        //iterate subModel from start point

        //if more fields than standard: recursion method returns object with 2 lists - one of parsed cars for submodel
        //2nd for car for preparse logging

        //save both lists bulk
    }

    public static void main(String[] args) {
        //TestClass.testConnection();
       // TestClass.testFindNowButton();
       new PreParseLauncher().launchPreParse();
       new PreParseLauncher().launchPreParseForYears(2018, 2017);
    }


}
