package bilstein;

import bilstein.entities.Shock;
import bilstein.entities.StartPoint;
import bilstein.entities.preparse.PrepInfoKeeper;
import bilstein.parsers.ShockParser;
import bilstein.parsers.YearParser;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class ParseLauncher {

    /**
     * Launches PreParse from the beginning.
     */
    public void launchPreParse(){
        StartPoint startPoint = new StartPoint();
        launchPreParse(startPoint, 0, 0);
    }

    public void launchPreParseForYears(int yearStart, int yearFinish){
        StartPoint startPoint = new StartPoint();
        launchPreParse(startPoint, yearStart, yearFinish);
    }

    public void launchPreParseFromPauseTillYear(int yearStart, int yearFinish, String make){
        StartPoint startPoint = BilsteinUtil.getStartPoint(yearStart, make);
        launchPreParse(startPoint, 0, yearFinish);
    }

    public void launchPreParseFromPauseTillEnd(int yearStart, String make){
        StartPoint startPoint = BilsteinUtil.getStartPoint(yearStart, make);
        launchPreParse(startPoint, 0, 0);
    }

    private void launchPreParse(StartPoint startPoint, int yearStart, int yearFinish) {
        WebDriver driver = SileniumUtil.initBaseDriver();
        List<WebElement> yearEls = SileniumUtil.getYearElements(driver);

        int startID = startPoint.getYearID();
        if (startID==0){
            if (yearStart==0){
                startID = 1;
            }
            else {
                String yStartStr = yearStart+"";
                startID = SileniumUtil.getElementID(yearEls, yStartStr);
            }
        }
        int finishID = yearEls.size();
        if (yearFinish!=0){
            String yFinishStr = yearFinish+"";
            finishID = SileniumUtil.getElementID(yearEls, yFinishStr);
        }

        //getting Map year and year id for link.
        Map<String, String> yearMap = SileniumUtil.getElementMap(yearEls.subList(startID,finishID));
        //reversing map to get years from 2k, otherwise it starts from 1896
        Map<String, String> sortedYearMap = new TreeMap<>(Collections.<String>reverseOrder());
        sortedYearMap.putAll(yearMap);


        for (Map.Entry<String, String> entry: sortedYearMap.entrySet()){
            PrepInfoKeeper keepr = new PrepInfoKeeper();
            keepr.setYear(entry.getKey());
            keepr.setYearID(entry.getValue());
            YearParser yearParser = new YearParser(driver, keepr, startPoint);
            yearParser.parse();
            if (startPoint.getMakeID()!=1){
                startPoint.setMakeID(1);
            }
            driver = SileniumUtil.initBaseDriver();
        }

        driver.close();
    }

    public void parseShockDetails(){
        WebDriver driver = SileniumUtil.initBaseDriver();
        List<Shock> shocks = BilsteinDao.getRawShocks();
        for (Shock shock: shocks){
            driver = SileniumUtil.getShockPage(driver, shock.getPartNo());
            Shock detailedShock = null;
            while (true){
                try {
                    detailedShock = new ShockParser(driver, shock).parse();
                    break;
                }
                catch (StaleElementReferenceException e){
                }
            }
            BilsteinDao.updateShock(detailedShock);
        }
        driver.close();
        HibernateUtil.shutdown();
    }
}
