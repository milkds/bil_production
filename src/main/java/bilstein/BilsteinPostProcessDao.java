package bilstein;

import bilstein.entities.BuyersGuide;
import bilstein.entities.Car;
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
}
