package bilstein;

import bilstein.entities.*;
import bilstein.entities.preparse.Ym;
import bilstein.entities.preparse.Ymm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class BilsteinDao {
    private static final Logger logger = LogManager.getLogger(BilsteinDao.class.getName());
    private static void saveCarsPr(List<Car> parsedCars) throws NoSelectOptionAvailableException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            for (Car car: parsedCars){
               saveCar(session, car);
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw new NoSelectOptionAvailableException();
        }
//
    }

    private static void saveCar(Session session, Car car) {
        int id = (Integer)session.save(car);
        if (car.hasShocks()){
            car.setCarID(id);
            List<Fitment> fitments = car.getFitments();
            for (Fitment fitment: fitments){
                fitment.setCar(car);
                Shock shock = fitment.getShock();
                if (!shockExists(shock, session)){
                    session.persist(shock);
                    logger.info("shock saved "+shock);
                }
                session.persist(fitment);
            }
        }
    }

    private static boolean shockExists(Shock shock, Session session) {
        String subQuery = "'"+shock.getPartNo()+"'";
        String query = "select count(partNo) from Shock where partNo = "+subQuery;
        Long count = (Long) session.createQuery( query ).getSingleResult();
        return ( ( count.equals( 0L ) ) ? false : true );
    }

    public static void saveYms(List<Ym> yms) {
        Session session = HibernateUtil.getSession();

        //checking if information about YM have been already added. As we save in bulk for whole year - its
        //enough for us to check by single YM (we just need year)
        Ym basicYm = yms.get(0);
        if (!yMexists(basicYm, session)){
            Transaction transaction = null;
            try {
                transaction = session.getTransaction();
                transaction.begin();
                for (Ym ym: yms){
                    session.persist(ym);
                }
                transaction.commit();
                session.close();
                logger.info("yms saved");
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        else {
            session.close();
            logger.info("Yms exist for year " + basicYm.getYear());
        }
    }
    
    private static boolean yMexists(Ym ym, Session session) {
        String query = "select count(make) from Ym where year = "+ym.getYear()+" and make = '"+ym.getMake()+"'";
        Long count = (Long) session.createQuery( query ).getSingleResult();
        return ( ( count.equals( 0L ) ) ? false : true );
    }

    public static void saveYmms(List<Ymm> ymms) throws NoSelectOptionAvailableException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            for (Ymm ym: ymms){
                session.persist(ym);
            }
            transaction.commit();
            session.close();
            logger.info("yms saved");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw new NoSelectOptionAvailableException();
        }
    }

    public static void saveCars(List<Car> parsedCars) {
        new Thread(() -> {
                try {
                    saveCarsPr(parsedCars);
                } catch (NoSelectOptionAvailableException e) {
                    logger.info("Error while saving " + parsedCars.get(0).getModelYear() + " " + parsedCars.get(0).getMake()  );
                  return;
                }
            Car baseCar = parsedCars.get(0);
            setYearMakeParsed(baseCar);
        }).run();

    }

    private static void setYearMakeParsed(Car baseCar) {
        Integer year = baseCar.getModelYear();
        String make = baseCar.getMake();

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Ym ym = getYm(year, make, session);
            ym.setMakeParsed(true);
            session.update(ym);
            transaction.commit();
            session.close();
            logger.info("year+make marked as parsed");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private static Ym getYm(Integer year, String make, Session session) {
        Ym ym;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ym> crQ = builder.createQuery(Ym.class);
        Root<Ym> root = crQ.from(Ym.class);
        crQ.where(builder.and(builder.equal(root.get("year"), year),
                builder.equal(root.get("make"), make)));
        Query q = session.createQuery(crQ);
        ym = (Ym) q.getSingleResult();

        return ym;
    }

    public static Ym testMethod(Integer year, String make){
        Session session = HibernateUtil.getSession();
        Ym ym = getYm(year, make, session);
        session.close();
        return ym;
    }

    public static List<Ym> getYmsByYear(int year) {
        Session session = HibernateUtil.getSession();
        List<Ym> yms;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ym> crQ = builder.createQuery(Ym.class);
        Root<Ym> root = crQ.from(Ym.class);
        crQ.where(builder.equal(root.get("year"), year));
        Query q = session.createQuery(crQ);
        yms = q.getResultList();

        return yms;
    }

    public static List<Shock> getRawShocks() {
        Session session = HibernateUtil.getSession();
        List<Shock> shocks;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Shock> crQ = builder.createQuery(Shock.class);
        Root<Shock> root = crQ.from(Shock.class);
        crQ.where(builder.equal(root.get("detailsParsed"), false));
        Query q = session.createQuery(crQ);
        shocks = q.getResultList();
        session.close();

        return shocks;
    }

    public static List<Shock> getAllShocks() {
        Session session = HibernateUtil.getSession();
        List<Shock> shocks;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Shock> crQ = builder.createQuery(Shock.class);
        Root<Shock> root = crQ.from(Shock.class);
        Query q = session.createQuery(crQ);
        shocks = q.getResultList();
        session.close();

        return shocks;
    }

    /**
     *
     * @param session
     * @return All Shocks
     */
    public static List<Shock> getRawShocks2(Session session) {
        List<Shock> shocks;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Shock> crQ = builder.createQuery(Shock.class);
        Root<Shock> root = crQ.from(Shock.class);
        crQ.where(builder.equal(root.get("detailsParsed"), false));
        Query q = session.createQuery(crQ);
        shocks = q.getResultList();

        return shocks;
    }

    public static void updateShock(Shock detailedShock) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Shock shock = getShockByID(detailedShock.getShockID(), session);
            BilsteinUtil.fillAdditionalFields(shock, detailedShock);
            List<Spec> specs = shock.getSpecs();
            for (Spec spec: specs){
//                logger.info(spec);
                session.persist(spec);
            }
            List<Detail> details = shock.getDetails();
            for (Detail detail: details){
                session.persist(detail);
            }
            List<ProductInfo> pInfos = shock.getpInfos();
            for (ProductInfo productInfo: pInfos){
                session.persist(productInfo);
            }
            session.update(shock);
            transaction.commit();
            session.close();
            logger.info("Updated shock # " + detailedShock.getPartNo());
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    private static Shock getShockByID(Integer shockID, Session session) {
        Shock shock;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Shock> crQ = builder.createQuery(Shock.class);
        Root<Shock> root = crQ.from(Shock.class);
        crQ.where(builder.equal(root.get("shockID"), shockID));
        Query q = session.createQuery(crQ);
        shock = (Shock) q.getSingleResult();

        return shock;
    }

    public static void updShocks(){
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            List<Shock> shocks = getRawShocks2(session);
            for (Shock shock: shocks){
                shock.setDetailsParsed(false);
                session.update(shock);
                logger.info("Updated shock # " + shock.getPartNo());
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        HibernateUtil.shutdown();
    }

    /**
     * This method needed for manual parse result reprocessing.
     * @return Shocks with specs, for spec reparsing.
     */
    public static List<Shock> getRawShocks3() {
        Session session = HibernateUtil.getSession();
        // List<Shock> shocks = getRawShocks2(session);
        List<Spec> specs = getSpecs(session);
        session.close();
        Map<Integer, Shock> shockMap = new HashMap<>();
        for (Spec spec: specs){
            if (spec.getSpecName().length()==0){
                Shock shock = spec.getShock();
                System.out.println(shock);
                shockMap.put(shock.getShockID(), shock);
            }
        }
        List<Shock> result = new ArrayList<>(shockMap.values());

        return result;
    }

    private static List<Spec> getSpecs(Session session) {
        List<Spec> specs;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Spec> crQ = builder.createQuery(Spec.class);
        Root<Spec> root = crQ.from(Spec.class);
       /*
        //crQ.where(builder.equal(root.get("detailsParsed"), false));*/
        Query q = session.createQuery(crQ);
        specs = q.getResultList();

        return specs;
    }

    public static List<Shock> getRawShocks4() {
        Session session = HibernateUtil.getSession();
        List<Shock> shocks = getRawShocks2(session);
        session.close();

        return shocks;
    }

    public static List<Spec> getAllSpecs(){
        Session session = HibernateUtil.getSession();
        List<Spec> specs = getSpecs(session);
        session.close();
        
        return specs;
    }

    public static void processSpecs() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            List<Spec> specs = getSpecs(session);
            for (Spec spec: specs){
                Shock shock  = spec.getShock();
                AfterParseProcessor.setSpecValue(shock, spec.getSpecName(), spec.getSpecValue());
                session.update(shock);
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void processDetails() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            List<Detail> details = getDetails(session);
            for (Detail detail: details){
                Shock shock  = detail.getShock();
                AfterParseProcessor.setDetailValue(shock, detail.getDetailName(), detail.getDetailValue());
                session.update(shock);
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private static List<Detail> getDetails(Session session) {
        List<Detail> details;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Detail> crQ = builder.createQuery(Detail.class);
        Root<Detail> root = crQ.from(Detail.class);
        Query q = session.createQuery(crQ);
        details = q.getResultList();

        return details;
    }

    public static Set<String> getMakes() {
        Session session = HibernateUtil.getSession();
        List<String> makes = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<String> crQ = builder.createQuery(String.class);
        Root<Car> root = crQ.from(Car.class);
        crQ.select(root.get("make")).distinct(true);
        Query q = session.createQuery(crQ);
        makes = q.getResultList();
        session.close();
        Set<String> makeSet = new HashSet<>(makes);

        return makeSet;
    }

    public static Set<String> getModels(String currentMake) {
        Session session = HibernateUtil.getSession();
        List<String> models = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<String> crQ = builder.createQuery(String.class);
        Root<Car> root = crQ.from(Car.class);
        crQ.where(builder.equal(root.get("make"),currentMake)).select(root.get("model")).distinct(true);
        Query q = session.createQuery(crQ);
        models = q.getResultList();
        session.close();
        Set<String> modelSet = new HashSet<>(models);

        return modelSet;
    }

    public static void saveGuides(List<BuyersGuide> guides) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            for (BuyersGuide bGuide: guides){
                session.persist(bGuide);
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static List<Car> getAllCarsWithFits(Session session) {
        List<Car> cars;
        List<Integer> carIDwithFits = getCarsIDsWithFits(session);
        Set<Integer> idSet = new HashSet<>(carIDwithFits);
        logger.info("got car ids");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> crQ = builder.createQuery(Car.class);
        Root<Car> root = crQ.from(Car.class);
     //   crQ.where(builder.equal(root.get("yearStart"), 0));
        Query q = session.createQuery(crQ);
        cars = q.getResultList();
        List<Car> carsWithFits = new ArrayList<>();
        cars.forEach(car -> {
            if (idSet.contains(car.getCarID())){
                carsWithFits.add(car);
            }
        });
        logger.info("got Cars");

        return carsWithFits;
    }

    private static List<Integer> getCarsIDsWithFits(Session session) {
        List<Integer> ids = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> crQ = builder.createQuery(Integer.class);
        Root<Fitment> root = crQ.from(Fitment.class);
        crQ.select(root.get("car")).distinct(true);
        Query q = session.createQuery(crQ);
        List<Car> cars = q.getResultList();
        cars.forEach(car -> {
            ids.add(car.getCarID());
        });
        return ids;
    }

    public static BuyersGuide getBuyersGuideByShockAndCar(Shock shock, Car car, Session session) {
        BuyersGuide guide;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BuyersGuide> crQ = builder.createQuery(BuyersGuide.class);
        Root<BuyersGuide> root = crQ.from(BuyersGuide.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("shock"), shock));
        predicates.add(builder.equal(root.get("make"), car.getMake()));
        predicates.add(builder.equal(root.get("model"), car.getModel()));
        predicates.add(builder.greaterThanOrEqualTo(root.get("yearFinish"), car.getModelYear()));
        predicates.add(builder.lessThanOrEqualTo(root.get("yearStart"), car.getModelYear()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        try {
            guide = (BuyersGuide) q.getSingleResult();
        }
        catch (NoResultException e){
            logger.error("NO Result for combo: ");
            logger.info(car);
            logger.info(shock);
            HibernateUtil.shutdown();
            System.exit(1);
            return null;
        }
        catch (NonUniqueResultException e1){
            logger.error("duped Buyers Guide ");
            logger.info(car);
            logger.info(shock);
            HibernateUtil.shutdown();
            System.exit(1);
            return null;
        }

        return guide;
    }

    public static void updateCar(Car car, BuyersGuide bGuide, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Car dbCar = session.get(Car.class, car.getCarID());
            dbCar.setYearStart(bGuide.getYearStart());
            dbCar.setYearFinish(bGuide.getYearFinish());
            session.update(dbCar);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static List<Fitment> getFitmentsByCar(Car car, Session session) {
        List<Fitment> fitments;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Fitment> crQ = builder.createQuery(Fitment.class);
        Root<Fitment> root = crQ.from(Fitment.class);
        crQ.where(builder.equal(root.get("car"), car));
        Query q = session.createQuery(crQ);
        fitments = q.getResultList();

        return fitments;
    }

    public static void reworkDodge() {
        Session session = HibernateUtil.getSession();

        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            List<BuyersGuide> guides;
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BuyersGuide> crQ = builder.createQuery(BuyersGuide.class);
            Root<BuyersGuide> root = crQ.from(BuyersGuide.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("make"), "Dodge"));
            predicates.add(builder.like(root.get("model"), "%Ram%"));
            predicates.add(builder.greaterThan(root.get("yearStart"), "2010"));
            Predicate[] preds = predicates.toArray(new Predicate[0]);
            crQ.where(builder.and(preds));
            Query q = session.createQuery(crQ);
            guides = q.getResultList();
            guides.forEach(guide->{
                String model = guide.getModel();
                model = model.replace("Ram ", "");
                guide.setMake("Ram");
                guide.setModel(model);
                session.update(guide);
            });
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        HibernateUtil.shutdown();
    }

    public static void postProcess() {
        BilsteinPostProcessDao.processJeepCJ5();
    }

    public static void postProcessCars() {
        BilsteinPostProcessDao.processJeepCJ5InCarList();
    }

    public static List<Car> getAllCars() {
        Session session = HibernateUtil.getSession();
        List<Car> cars;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> crQ = builder.createQuery(Car.class);
        Root<Car> root = crQ.from(Car.class);
        Query q = session.createQuery(crQ);
        cars = q.getResultList();

        return cars;
    }
}
