package bilstein;

import bilstein.entities.Fitment;

public class Controller {

    public static void main(String[] args) {
            new PreParseLauncher().launchPreParse();
           // new PreParseLauncher().launchPreParseForYears(2018, 2017);
           // new PreParseLauncher().launchPreParseFromPauseTillEnd(2018, "Ford");
           // new PreParseLauncher().launchPreParseFromPauseTillYear(2018, 2000, "Ford");
       ConsistencyChecker.check(2019, 1900);

    }

}
