package bilstein.parsers;

import bilstein.BilsteinDao;
import bilstein.BilsteinUtil;
import bilstein.NoSelectOptionAvailableException;
import bilstein.SileniumUtil;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ym;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YearParser {

    private WebDriver driver;
    private String year;
    private String yearID;


    private StartPoint startPoint;
    private PrepInfoKeeper keepr;
    private int rebootCounter;

    private static final Logger logger = LogManager.getLogger(YearParser.class.getName());


    public YearParser(WebDriver driver, PrepInfoKeeper keeper, StartPoint startPoint) {
        this.driver = driver;
        this.year = keeper.getYear();
        this.yearID = keeper.getYearID();
        this.startPoint = startPoint;
        this.keepr = keeper;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getYear() {
        return year;
    }

    public String getYearID() {
        return yearID;
    }

    public void parse() {
        Instant start = Instant.now();
        List<WebElement> makeEls = SileniumUtil.getMakeEls(driver, year);
        List<Ym> yms = new ArrayList<>();
        int yearInt = Integer.parseInt(year);
        //saving yms to base, for later check of consistency
        for (int i = 1; i <makeEls.size() ; i++) {
            String make = makeEls.get(i).getText();
            Ym ym = new Ym();
            ym.setYear(yearInt);
            ym.setMake(make);
            yms.add(ym);
        }
        BilsteinDao.saveYms(yms);

        //launching make parse
        int startID = 1;
        int startPointID = startPoint.getMakeID();
        if (startPointID!=0){
            startID = startPointID;
        }

        //Selecting elements with Makes for current year from start point to finish.
        Map<String, String> makeMap = SileniumUtil.getElementMap(makeEls.subList(startID, makeEls.size()));
        for (Map.Entry<String, String> entry: makeMap.entrySet()){
            PrepInfoKeeper ymKeeper = new PrepInfoKeeper();
            ymKeeper.setYear(year);
            ymKeeper.setYearID(yearID);
            ymKeeper.setMake(entry.getKey());
            ymKeeper.setMakeID(entry.getValue());

            MakeParser makeP = new MakeParser(driver, ymKeeper, startPoint);


            int carsParsed = 0;
            try {
                carsParsed = makeP.parseMake();
            } catch (NoSelectOptionAvailableException e) {
               logger.error("Exception occurred while parsing "+ yearID + " " + ymKeeper.getMake() );
            }

           /* //test section - to be deleted in production
            try {
                if (makeP.getKeepr().getMake().equals("Audi")) {
                    carsParsed = makeP.parseMake();
                }
            } catch (NoSelectOptionAvailableException ignored) {

            }
            ///////////////////////*/

            //need to refresh driver in order to prevent it grow till no RAM available.
            rebootCounter = rebootCounter+carsParsed;
            if (rebootCounter>300){
                driver.close();
                driver = SileniumUtil.initBaseDriver();
                rebootCounter = 0;
            }
        }

        driver.close();

        Instant finish = Instant.now();
        logger.info("Year " + year + " parsed in " + Duration.between(start, finish).toMinutes()+ " minutes");
    }
    public StartPoint getStartPoint() {
        return startPoint;
    }

    public PrepInfoKeeper getKeepr() {
        return keepr;
    }
}
