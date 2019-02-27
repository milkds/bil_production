package bilstein;

import bilstein.entities.BuyersGuide;
import bilstein.entities.Car;
import bilstein.entities.FinalCar;
import bilstein.entities.FinalFitment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BilsteinPostProcessDao {
    public static void processJeepCJ5() {
        Session session = HibernateUtil.getSession();
        List<BuyersGuide> guides = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<BuyersGuide> crQ = builder.createQuery(BuyersGuide.class);
        Root<BuyersGuide> root = crQ.from(BuyersGuide.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("make"), "Jeep"));
        predicates.add(builder.equal(root.get("model"), "CJ5"));
        predicates.add(builder.greaterThan(root.get("yearFinish"), "1975"));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        Transaction transaction = null;
        try {
            guides = q.getResultList();
            transaction = session.getTransaction();
            transaction.begin();
            guides.forEach(guide->{
                guide.setYearStart(1976);
                session.update(guide);
            });
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void processJeepCJ5InCarList() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            List<Car> before76 = getBefore76(session);
            List<Car> after76 = getAfter76(session);
            transaction = session.getTransaction();
            transaction.begin();
            before76.forEach(car->{
                car.setYearStart(1959);
                car.setYearFinish(1975);
                session.update(car);
            });
            after76.forEach(car->{
                car.setYearStart(1976);
                car.setYearFinish(1983);
                session.update(car);
            });
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private static List<Car> getAfter76(Session session) {
        List<Car> cars = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> crQ = builder.createQuery(Car.class);
        Root<Car> root = crQ.from(Car.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("make"), "Jeep"));
        predicates.add(builder.equal(root.get("model"), "CJ5"));
        predicates.add(builder.greaterThan(root.get("modelYear"), "1975"));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        cars = q.getResultList();

        return cars;
    }

    private static List<Car> getBefore76(Session session) {
        List<Car> cars = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> crQ = builder.createQuery(Car.class);
        Root<Car> root = crQ.from(Car.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("make"), "Jeep"));
        predicates.add(builder.equal(root.get("model"), "CJ5"));
        predicates.add(builder.lessThan(root.get("modelYear"), "1976"));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        cars = q.getResultList();

        return cars;
    }

    public static List<Car> getEqualCars(Session session, Car car) {
        List<Car> cars = new ArrayList<>();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> crQ = builder.createQuery(Car.class);
        Root<Car> root = crQ.from(Car.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("make"), car.getMake()));
        predicates.add(builder.equal(root.get("model"), car.getModel()));
        predicates.add(builder.lessThan(root.get("subModel"), car.getSubModel()));
        predicates.add(builder.lessThan(root.get("body"), car.getBody()));
        predicates.add(builder.lessThan(root.get("bodyMan"), car.getBodyMan()));
        predicates.add(builder.lessThan(root.get("drive"), car.getDrive()));
        predicates.add(builder.lessThan(root.get("doors"), car.getDoors()));
        predicates.add(builder.lessThan(root.get("engine"), car.getEngine()));
        predicates.add(builder.lessThan(root.get("suspension"), car.getSuspension()));
        predicates.add(builder.lessThan(root.get("transmission"), car.getTransmission()));
        predicates.add(builder.lessThan(root.get("yearStart"), car.getYearStart()));
        predicates.add(builder.lessThan(root.get("yearFinish"), car.getYearFinish()));
        Predicate[] preds = predicates.toArray(new Predicate[0]);
        crQ.where(builder.and(preds));
        Query q = session.createQuery(crQ);
        cars = q.getResultList();

        return cars;
    }

    public static void saveFinalCar(FinalCar finalCar) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(finalCar);
            List<FinalFitment> fits = finalCar.getFitments();
            if (fits!=null){
                fits.forEach(fit->{
                    fit.setCar(finalCar);
                    session.persist(fit);
                });
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
}
