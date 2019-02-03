package bilstein;

import bilstein.entities.BuyersGuide;
import bilstein.entities.Car;
import bilstein.entities.Fitment;
import bilstein.entities.Shock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AfterParseProcessor {

    private static final Logger logger = LogManager.getLogger(AfterParseProcessor.class.getName());

    public static void processParsedInfo(){
        BilsteinDao.processSpecs();
        BilsteinDao.processDetails();
        HibernateUtil.shutdown();
    }

    public static void setSpecValue(Shock shock, String specName, String specValue) {
        switch (specName){
            case "Body Diameter": break;
            case "Finish": shock.setFinish(specValue); break;
            case "Body Design": shock.setBodyDesign(specValue); break;
            case "Reservoir": shock.setReservoir(specValue); break;
            case "Optional Reservoir Clamp": shock.setOptResClamp(specValue); break;
            case "Type": shock.setItemTypeSteerRacks(specValue); break;
            case "Kit Contents": shock.setKitContents(specValue); break;
            case "Optional Heavy Load Springs": shock.setOptHLSprings(specValue); break;
            case "Adjustable Damping": shock.setAdjDamping(specValue); break;
            default:
        }
    }

    public static void setDetailValue(Shock shock, String detailName, String detailValue) {
        switch (detailName){
            case "Warranty": shock.setWarranty(detailValue); break;
            case "Quantity per Vehicle": shock.setQtyPerVehicle(detailValue); break;
            case "Applications": break;
            default:
        }
    }

    public static void setYearStartFinish(){
        Session session = HibernateUtil.getSession();
        List<Car> cars = BilsteinDao.getAllCars(session);
        cars.forEach(car -> {
          //  List<Fitment> fitments = car.getFitments();
            List<Fitment> fitments = BilsteinDao.getFitmentsByCar(car, session);
            if (fitments.size()>0){
                Fitment fit = fitments.get(0);
                Shock shock = fit.getShock();
                BuyersGuide bGuide = BilsteinDao.getBuyersGuideByShockAndCar(shock, car, session);
                if (bGuide!=null){
                    BilsteinDao.updateCar(car, bGuide, session);
                    logger.info("updated car " + car);
                }
            }
        });
        session.close();
        HibernateUtil.shutdown();
    }

    public static void processBuyersGuide(){
        List<Shock> shocks = BilsteinDao.getAllShocks();
        Set<String> makes = BilsteinDao.getMakes();
        shocks.forEach(shock->{
            String bGuideStr = shock.getBuyersGuide();
            if (bGuideStr!=null){
                List<BuyersGuide> guides = getGuides(bGuideStr, makes, shock);
                BilsteinDao.saveGuides(guides);
            }
        });
        BilsteinDao.reworkDodge();
        HibernateUtil.shutdown();
    }


    private static List<BuyersGuide> getGuides(String bGuideStr, Set<String> makes, Shock shock) {
     //   System.out.println(bGuideStr + "------" + shock.getPartNo());
        List<BuyersGuide> result = new ArrayList<>();
        String[] split = bGuideStr.split(",");
        String currentMake = getCurrentMake(split[0], makes);
        Set<String>currentModels = BilsteinDao.getModels(currentMake);
        for (String guideStr: split){
            guideStr = guideStr.trim();
            String yearStr = getYearStr(guideStr);
            String modelStr = guideStr.replace(yearStr, "");
            if (modelStr.startsWith(currentMake)){
                modelStr = modelStr.replace(currentMake,"");
            }
            modelStr = modelStr.trim();
            BuyersGuide bGuide = null;
            if (!currentModels.contains(modelStr)){
                currentMake = getCurrentMake(guideStr, makes);
                currentModels = BilsteinDao.getModels(currentMake);
                modelStr = modelStr.replace(currentMake, "");
                modelStr = modelStr.trim();
            }
            bGuide = getBuyersGuide(currentMake, modelStr, yearStr, shock);
            System.out.println(bGuide);
            result.add(bGuide);
        }

        return result;
    }

    private static BuyersGuide getBuyersGuide(String currentMake, String modelStr, String yearStr, Shock shock) {
        BuyersGuide bGuide = new BuyersGuide();
        String split[] = yearStr.split("-");
        Integer yearFinish = Integer.parseInt(split[0]);
        Integer yearStart = 0;
        if (split.length==1){
            yearStart = yearFinish;
        }
        else {
            yearStart = Integer.parseInt(split[1]);
        }
        bGuide.setMake(currentMake);
        bGuide.setModel(modelStr);
        bGuide.setYearStart(yearStart);
        bGuide.setYearFinish(yearFinish);
        bGuide.setShock(shock);

        return bGuide;
    }

    private static String getYearStr(String guideStr) {
        String regexp = "(\\d\\d\\d\\d-\\d\\d\\d\\d)";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(guideStr);

        String yearString = "";
        if (matcher.find()){
            yearString = matcher.group();
        }
        else {
            int length = guideStr.length();
            yearString = guideStr.substring(length-4,length);
        }

        return yearString;
    }

    private static String getCurrentMake(String makeString, Set<String> makes) {
        for (String make: makes){
            if (makeString.startsWith(make)){
               return make;
            }
        }

        return "";
    }
}
