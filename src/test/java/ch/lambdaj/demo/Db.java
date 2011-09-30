package ch.lambdaj.demo;

import java.util.*;

public class Db {

	public final List<Person> persons = new ArrayList<Person>();
	public final List<Car> cars = new ArrayList<Car>();
	public final List<Sale> sales = new ArrayList<Sale>();
	
	private static final Db DB_INSTANCE = new Db();
	
	public static Db getInstance() {
		return DB_INSTANCE;
	}
	
	public List<Person> getPersons() {
		return persons;
	}
	
	public List<Car> getCars() {
		return cars;
	}
	
	public List<Sale> getSales() {
		return sales;
	}
	
	private Db() {
		createPersons();
		createCars();
		createSales();
	}
	
	private void createPersons() {
		persons.add(new Person("Mario", "Fusco", true, "18/03/1974"));
		persons.add(new Person("Irma", "Fusco", false, "04/06/1978"));
		persons.add(new Person("Domenico", "Fusco", true, "10/06/1939"));
		persons.add(new Person("Ernestina", "Di Landa", false, "06/10/1949"));
		persons.add(new Person("Marilena", "Marrese", false, "14/04/1972"));
		persons.add(new Person("Biagio", "Beatrice", true, "19/04/1970"));
		persons.add(new Person("Celestino", "Bellone", true, "03/01/1981"));
		persons.add(new Person("Luca", "Marrocco", true, "07/12/1980"));
	}

	private void createCars() {
		cars.add(new Car("Fiat", "Panda", 2003, 7500.00));
		cars.add(new Car("Lancia", "Delta", 1992, 19000.00));
		cars.add(new Car("Alfa Romeo", "8c", 2007, 145000.00));
		cars.add(new Car("Ferrari", "Enzo", 2004, 650000.00));
		cars.add(new Car("Lamborghini", "Diablo", 2001, 175000.00));
		cars.add(new Car("Maserati", "Quattroporte", 1999, 90000.00));
		cars.add(new Car("Bugatti", "EB110", 2005, 990000.00));
		cars.add(new Car("Audi", "R8", 2008, 105000.00));
		cars.add(new Car("Volkswagen", "Golf", 1979, 12000.00));
		cars.add(new Car("BMW", "850", 1994, 45000.00));
		cars.add(new Car("Porsche", "911", 2006, 115000.00));
		cars.add(new Car("Mercedes", "SLR", 2004, 345000.00));
		cars.add(new Car("Honda", "Type-R", 2005, 25000.00));
		cars.add(new Car("Toyota", "Celica", 1993, 20000.00));
		cars.add(new Car("Mitsubishi", "Lancer", 2006, 40000.00));
		cars.add(new Car("Mazda", "RX-8", 2006, 39000.00));
		cars.add(new Car("Hummer", "H1", 2007, 80000.00));
		cars.add(new Car("Saleen", "S7", 2008, 680000.00));
		cars.add(new Car("Chrysler", "Voyager", 2007, 35000.00));
		cars.add(new Car("Chrysler", "Voyager", 2007, 35000.00));
	}

	private void createSales() {
		Random random = new Random(System.currentTimeMillis());
		
		for (Person buyer : persons) {
			for (Person seller : persons) {
				if (buyer.equals(seller)) continue;
				Car car = cars.get(random.nextInt(cars.size()));
				double cost = car.getOriginalValue() * random.nextDouble();
				sales.add(new Sale(seller, buyer, car, cost));
			}
		}
	}
}
