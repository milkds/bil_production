package bilstein;

import bilstein.entities.Car;
import bilstein.entities.Fitment;
import bilstein.entities.Shock;
import bilstein.entities.preparse.Ym;
import bilstein.entities.preparse.Ymm;
import bilstein.entities.preparse.Ymms;
import bilstein.parsers.CarParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
            logger.info("Cars successfully saved ");
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
            for (Car car: parsedCars){
                try {
                    saveCarsPr(parsedCars);
                } catch (NoSelectOptionAvailableException e) {
                  logger.info("Error while saving car " + car);
                  return;
                }
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
}
