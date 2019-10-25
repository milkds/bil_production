package bilstein;

import bilstein.entities.Fitment;

public class Controller {

    //1. Launch preparse
    //2. Check consistency
    //3. Parse shock details

    //4. Process parsed info
    //5. Process buyers guide
    //6. Set year start year finish
    public static void main(String[] args) throws NoSelectOptionAvailableException {
          //  new ParseLauncher().launchPreParse();
           // new ParseLauncher().launchPreParseForYears(2019, 1900);
           // new ParseLauncher().launchPreParseFromPauseTillEnd(2011, "Freightliner");
           // new ParseLauncher().launchPreParseFromPauseTillYear(2018, 2000, "Ford");
         //   new ParseLauncher().parseShockDetails();

      //  ConsistencyChecker.check(2020, 1901);


      //  AfterParseProcessor.processParsedInfo();
       // AfterParseProcessor.processBuyersGuide();
        AfterParseProcessor.setYearStartFinish();

      //  TestClass.testBuyerGuide();



       // AfterParseProcessor.joinEqualCars();
     //  HibernateUtil.shutdown();
    }




}
