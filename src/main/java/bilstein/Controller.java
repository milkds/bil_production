package bilstein;

import bilstein.entities.Fitment;

public class Controller {

    public static void main(String[] args) throws NoSelectOptionAvailableException {
            //new ParseLauncher().launchPreParse();
           // new ParseLauncher().launchPreParseForYears(2019, 1900);
           // new ParseLauncher().launchPreParseFromPauseTillEnd(2011, "Freightliner");
           // new ParseLauncher().launchPreParseFromPauseTillYear(2018, 2000, "Ford");
          //  new ParseLauncher().parseShockDetails();

      //  ConsistencyChecker.check(2019, 1901);


      //  AfterParseProcessor.processParsedInfo();
      //  AfterParseProcessor.processBuyersGuide();

     //   AfterParseProcessor.setYearStartFinish();

      //  TestClass.testBuyerGuide();
      //  BilsteinDao.reworkDodge();

        //BilsteinPostProcessDao.processJeepCJ5InCarList();
        AfterParseProcessor.joinEqualCars();
        HibernateUtil.shutdown();
    }




}
