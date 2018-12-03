package bilstein;

import bilstein.entities.Car;
import bilstein.entities.preparse.Ym;
import bilstein.entities.preparse.Ymm;
import bilstein.entities.preparse.Ymms;
import org.hibernate.Session;

import java.util.List;

public class BilsteinDao {
    public static void saveCar(Car car, Session session) {
    }

    public static void saveYms(List<Ym> yms) {
        //todo: implement
    }

    public static void saveYmms(List<Ymm> ymms) {
        //todo: implement
    }

    public static void saveCars(List<Car> parsedCars, List<Ymms> subsToSave) {
        //todo: implement
    }

    public static void saveYmmses(List<Ymms> ymmses) {
        //todo: implement
    }
}
