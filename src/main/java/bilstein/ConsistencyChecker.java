package bilstein;

import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.entities.preparse.Ym;
import bilstein.parsers.MakeParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistencyChecker {
    private static final Logger logger = LogManager.getLogger(ConsistencyChecker.class.getName());

    public static void check(int firstYear, int lastYear){
        for (int i = firstYear; i > lastYear; i--) {
            List<Ym> parsedCars = BilsteinDao.getYmsByYear(i);
            if (parsedCars.size()==0){
                logger.info("No cars parsed for year " + i);
            }
            else {
                for (Ym ym: parsedCars){
                    if (!ym.getMakeParsed()){
                        logger.info("Make not parsed: " + i + " " + ym.getMake());
                    }
                }
            }
        }
        HibernateUtil.shutdown();
    }

    public static void checkAndReparse(int firstYear, int lastYear) throws NoSelectOptionAvailableException {
        for (int i = firstYear; i > lastYear; i--) {
            List<Ym> parsedCars = BilsteinDao.getYmsByYear(i);
            if (parsedCars.size()==0){
                logger.info("No cars parsed for year " + i);
                new ParseLauncher().launchPreParseForYears(i , i-1);
            }
            else {
                for (Ym ym: parsedCars){
                    if (!ym.getMakeParsed()){
                        logger.info("Make not parsed: " + i + " " + ym.getMake());
                       reparseMake(ym);
                    }
                }
            }
        }
    }

    private static void reparseMake(Ym ym) throws NoSelectOptionAvailableException {
        int year = ym.getYear();
        String yearStr = year+"";
        WebDriver driver = SileniumUtil.initBaseDriver();

        List<WebElement> yearEls = SileniumUtil.getYearElements(driver);
        List<WebElement> makeEls = SileniumUtil.getMakeEls(driver, yearStr);

        int yearID = SileniumUtil.getElementID(yearEls, yearStr);
        Map<String, String> yearMap = SileniumUtil.getElementMap(yearEls.subList(yearID,yearID+1));
        //reversing map to get years from 2k, otherwise it starts from 1896
        Map<String, String> sortedYearMap = new TreeMap<>(Collections.<String>reverseOrder());
        sortedYearMap.putAll(yearMap);
        PrepInfoKeeper keepr=null;
        for (Map.Entry<String, String> entry: sortedYearMap.entrySet()){
            keepr = new PrepInfoKeeper();
            keepr.setYear(entry.getKey());
            keepr.setYearID(entry.getValue());
        }
        int makeID = SileniumUtil.getElementID(makeEls, ym.getMake());
        Map<String, String> makeMap = SileniumUtil.getElementMap(makeEls.subList(makeID, makeID+1));
        for (Map.Entry<String, String> entry: makeMap.entrySet()){
            keepr.setMake(entry.getKey());
            keepr.setMakeID(entry.getValue());
        }

        /*PrepInfoKeeper ymKeepr = new PrepInfoKeeper();
        ymKeepr.setYear(yearStr);
        ymKeepr.setYearID(yearID+"");
        ymKeepr.setMake(ym.getMake());
        ymKeepr.setMakeID(makeID+"");*/

        StartPoint startPoint = new StartPoint();

        new MakeParser(driver, keepr, startPoint).parseMake();
        driver.close();
    }
}
