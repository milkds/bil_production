package bilstein;

import bilstein.entities.*;
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


    public static void joinEqualCars(){
        Session session = HibernateUtil.getSession();
        List<Car> allCars = BilsteinDao.getAllCars();
        List<Car> allCarsWithFits = BilsteinDao.getAllCarsWithFits(session);
        List<Car> checkedCars = new ArrayList<>();
        int size = allCarsWithFits.size();
        int counter = 0;
        for (Car fitCar: allCarsWithFits){
            counter++;
            if (!checkedCars.contains(fitCar)){
                List<Car> equalCars = BilsteinPostProcessDao.getEqualCars(session, fitCar);
                FinalCar finalCar = new FinalCar(fitCar);
                List<FinalFitment> finalFits = getFinalFits(fitCar, session);
                finalCar.setFitments(finalFits);
                BilsteinPostProcessDao.saveFinalCar(finalCar);
                checkedCars.addAll(equalCars);
            }
            logger.info("processed car with fits " + counter+ " of total " + size);
        }
        allCars.removeAll(checkedCars);
        checkedCars = new ArrayList<>();
         size = allCars.size();
         counter = 0;
        for (Car noFitCar: allCars){
            counter++;
            if (!checkedCars.contains(noFitCar)){
                List<Car> equalCars = BilsteinPostProcessDao.getEqualCars(session, noFitCar);
                FinalCar finalCar = new FinalCar(noFitCar);
                BilsteinPostProcessDao.saveFinalCar(finalCar);
                checkedCars.addAll(equalCars);
            }
            logger.info("processed car without" +
                    " fits " + counter+ " of total " + size);
        }
        session.close();
    }

    private static List<FinalFitment> getFinalFits(Car fitCar, Session session) {
        List<FinalFitment> result = new ArrayList<>();
        List<Fitment> fits = BilsteinDao.getFitmentsByCar(fitCar, session);
        fits.forEach(fitment -> result.add(new FinalFitment(fitment)));
        return result;
    }

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
        List<Car> cars = BilsteinDao.getAllCarsWithFits(session);
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
        BilsteinDao.postProcessCars();
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
      //  BilsteinDao.reworkDodge();
        BilsteinDao.postProcess();
        HibernateUtil.shutdown();
    }

    private static List<BuyersGuide> getGuides(String bGuideStr, Set<String> makes, Shock shock) {
     //   System.out.println(bGuideStr + "------" + shock.getPartNo());
        List<BuyersGuide> result = new ArrayList<>();
        String[] split = bGuideStr.split(",");
        String currentMake = getCurrentMake(split[0], makes);
        //Dodge
        Set<String>currentModels = BilsteinDao.getModels(currentMake);
        for (String guideStr: split){
            guideStr = guideStr.trim();
            String yearStr = getYearStr(guideStr);
            String modelStr = guideStr.replace(yearStr, "");
            if (modelStr.startsWith(currentMake)){
                modelStr = modelStr.replace(currentMake,"");
            }
            else {
                //this is for exceptional case when up to 2010 Ram was model of Dodge, and from 2011 became a make.
                if (currentMake.equals("Dodge")){
                    if (modelStr.startsWith("Ram")){
                        if(itsRam(yearStr)){
                            currentMake = "Ram";
                            modelStr = modelStr.replace(currentMake,"");
                        }
                    }
                }
            }
            modelStr = modelStr.trim();
            if (modelStr.equals("GLE550")){
                modelStr = "GLE550e";
            }
            BuyersGuide bGuide = null;
            //https://cart.bilsteinus.com/details?id=4384947449518677067
            //here we get modelStr 3500 - but there is no such model for Dodge. So it tries to get new make
            //which equals 3500. It can't get it, so returns zero make.
            if (!currentModels.contains(modelStr)){

                //for test purposes - will be removed later
                if (currentMake.equals("Mercedes-Benz")){
                        logger.error("No Model for Mercedes " + modelStr + " for shock " + shock.getPartNo());
                }

                currentMake = getCurrentMake(guideStr, makes);
                currentModels = BilsteinDao.getModels(currentMake);
                modelStr = modelStr.replace(currentMake, "");
                modelStr = modelStr.trim();
            }
            bGuide = getBuyersGuide(currentMake, modelStr, yearStr, shock);
            result.add(bGuide);
        }

       result = removeDupes(result);

        return result;
    }

    private static List<BuyersGuide> removeDupes(List<BuyersGuide> rawGuides) {
        List<BuyersGuide> uniqueGuides = new ArrayList<>();
        List<BuyersGuide> dupeGuides = new ArrayList<>();
        for (BuyersGuide rawGuide: rawGuides){
            for (BuyersGuide uniqueGuide: uniqueGuides){
                if (guidesEqual(rawGuide, uniqueGuide)){
                    dupeGuides.add(rawGuide);
                }
            }
            uniqueGuides.add(rawGuide);
        }
        dupeGuides.forEach(uniqueGuides::remove);

        return uniqueGuides;
    }

    private static boolean guidesEqual(BuyersGuide guide, BuyersGuide anotherGuide) {
        return guide.getMake().equals(anotherGuide.getMake()) &&
                guide.getModel().equals(anotherGuide.getModel()) &&
                guide.getYearStart().equals(anotherGuide.getYearStart()) &&
                guide.getYearFinish().equals(anotherGuide.getYearFinish());


    }

    private static boolean itsRam(String yearStr) {
        int strLength = yearStr.length();
        String yearStartStr = yearStr.substring(strLength-4);
        int yearStart;
        try {
            yearStart = Integer.parseInt(yearStartStr);
        }
        catch (NumberFormatException e){
            return false;
        }
        return yearStart > 2010;

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
            yearString = guideStr.substring(length-4);
        }

        return yearString;
    }

    private static String getCurrentMake(String makeString, Set<String> makes) {
        for (String make: makes){
            if (makeString.startsWith(make)){
               return make;
            }
        }
        logger.error("Unknown make " + makeString);
        return "";
    }
}
