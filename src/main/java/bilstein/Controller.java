package bilstein;

import bilstein.entities.Fitment;

public class Controller {

    public static void main(String[] args) throws NoSelectOptionAvailableException {
           // new PreParseLauncher().launchPreParse();
           // new PreParseLauncher().launchPreParseForYears(2018, 2017);
         //   new PreParseLauncher().launchPreParseFromPauseTillEnd(1945, "Alfa Romeo");
           // new PreParseLauncher().launchPreParseFromPauseTillYear(2018, 2000, "Ford");
        ConsistencyChecker.checkAndReparse(2019, 1900);
        HibernateUtil.shutdown();
    }

}
