package ch.lambdaj.demo;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.Map.*;

import ch.lambdaj.group.*;

public class LambdaDemoTestMain {

	private static final int REPETITIONS = 1;
	private static final int ITERATIONS = 100000;
	
	private static final int TESTS_NR = 11;
	
	private static long[][] iterative;
	private static long[][] lambdaj;

	private static final Db db = Db.getInstance();

	public static void main(String[] args) {
		int repetitions = REPETITIONS;
		if (args.length == 1) repetitions = Integer.parseInt(args[0]);
		
		init(repetitions);
		
		for (int i = 0; i < repetitions; i++) {
			collectDurations(testPrintAllBrands(), i, 0);
			collectDurations(testFindAllSalesOfAFerrari(), i, 1);
			collectDurations(testFindAllBuysOfYoungestPerson(), i, 2);
			collectDurations(testFindMostCostlySaleValue(), i, 3);
			collectDurations(testSumSalesCostWhereBothActorsAreAMale(), i, 4);
			collectDurations(testFindYoungestAgeOfWhoBoughtACarForMoreThan50000(), i, 5);
			collectDurations(testSortSalesByCost(), i, 6);
			collectDurations(testExtractCarsOriginalCost(), i, 7);
			collectDurations(testIndexCarsByBrand(), i, 8);
			collectDurations(testGroupSalesByBuyersAndSellers(), i, 9);
			collectDurations(testFindMostBoughtCar(), i, 10);
		}
		
		resume(repetitions);
	}
	
	private static void collectDurations(long[] durations, int repetition, int index) {
		iterative[index][repetition] = durations[0];
		lambdaj[index][repetition] = durations[1];
	}

	private static void init(int repetitions) {
		iterative = new long[TESTS_NR][repetitions];
		lambdaj = new long[TESTS_NR][repetitions];
		
		for (int i = 0; i < TESTS_NR; i++) {
			for (int j = 0; j < repetitions; j++) {
				iterative[i][j] = 0L;
				lambdaj[i][j] = 0L;
			}
		}
	}
	
	private static void resume(int repetitions) {
		for (int i = 0; i < TESTS_NR; i++) {
			long minIterative = (Long)min(asLongList(iterative[i]));
			long maxIterative = (Long)max(asLongList(iterative[i]));
			long avgIterative = sum(asLongList((iterative[i]))).longValue() / repetitions;
			long minLambdaj = (Long)min(asLongList(lambdaj[i]));
			long maxLambdaj = (Long)max(asLongList(lambdaj[i]));
			long avgLambdaj = sum(asLongList(lambdaj[i])).longValue() / repetitions;
			
			System.out.println(minIterative + ";" + maxIterative + ";" + avgIterative + ";" + minLambdaj + ";" + maxLambdaj + ";" + avgLambdaj);
		}
	}
	
	private static List<Long> asLongList(long[] array) {
		List<Long> list = new ArrayList<Long>();
		for (long item : array) list.add(item);
		return list;
	}
	
	private static long[] testPrintAllBrands() {
		System.out.println("*** testPrintAllBrands");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			StringBuilder sb = new StringBuilder();
			for (Car car : db.getCars())
				sb.append(car.getBrand()).append(", ");
			String brandsIterative = sb.substring(0, sb.length() - 2).toString();
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			String brands = joinFrom(db.getCars()).getBrand();
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testFindAllSalesOfAFerrari() {
		System.out.println("*** testFindAllSalesOfAFerrari");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Sale> salesIterative = new ArrayList<Sale>();
			for (Sale sale : db.getSales()) {
				if (sale.getCar().getBrand().equals("Ferrari"))	salesIterative.add(sale);
			}
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Sale> sales = select(db.getSales(), having(on(Sale.class).getCar().getBrand(), equalTo("Ferrari")));
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testFindAllBuysOfYoungestPerson() {
		System.out.println("*** testFindAllBuysOfYoungestPerson");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Person youngest = null;
			for (Person person : db.getPersons()) {
				if (youngest == null || person.getAge() < youngest.getAge()) youngest = person;
			}
			List<Sale> salesIterative = new ArrayList<Sale>();
			for (Sale sale : db.getSales()) {
				if (sale.getBuyer().equals(youngest)) salesIterative.add(sale);
			}
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Sale> sales = select(db.getSales(), having(on(Sale.class).getBuyer(), equalTo(selectMin(db.getPersons(), on(Person.class).getAge()))));
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testFindMostCostlySaleValue() {
		System.out.println("*** testFindMostCostlySaleValue");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			double maxCost = 0.0;
			for (Sale sale : db.getSales()) {
				double cost = sale.getCost();
				if (cost > maxCost)	maxCost = cost;
			}
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			double max = max(db.getSales(), on(Sale.class).getCost());
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testSumSalesCostWhereBothActorsAreAMale() {
		System.out.println("*** testSumSalesCostWhereBothActorsAreAMale");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			double sumIterative = 0.0;
			for (Sale sale : db.getSales()) {
				if (sale.getBuyer().isMale() && sale.getSeller().isMale())
					sumIterative += sale.getCost();
			}
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			double sum = sum(select(db.getSales(), having(on(Sale.class).getBuyer().isMale()).and(having(on(Sale.class).getSeller().isMale()))), on(Sale.class).getCost());
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testFindYoungestAgeOfWhoBoughtACarForMoreThan50000() {
		System.out.println("*** testFindYoungestAgeOfWhoBoughtACarForMoreThan50000");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			int ageIterative = Integer.MAX_VALUE;
			for (Sale sale : db.getSales()) {
				if (sale.getCost() > 50000.00) {
					int buyerAge = sale.getBuyer().getAge();
					if (buyerAge < ageIterative)
						ageIterative = buyerAge;
				}
			}
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			int age = min(forEach(select(db.getSales(), having(on(Sale.class).getCost(), greaterThan(50000.00)))).getBuyer(), on(Person.class).getAge());
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testSortSalesByCost() {
		System.out.println("*** testSortSalesByCost");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Sale> sortedSalesIterative = new ArrayList<Sale>(db.getSales());
			Collections.sort(sortedSalesIterative, new Comparator<Sale>() {
				public int compare(Sale s1, Sale s2) {
					return Double.valueOf(s1.getCost()).compareTo(s2.getCost());
				}
			});
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Sale> sortedSales = sort(db.getSales(), on(Sale.class)
					.getCost());
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testExtractCarsOriginalCost() {
		System.out.println("*** testExtractCarsOriginalCost");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Double> costsIterative = new ArrayList<Double>();
			for (Car car : db.getCars())
				costsIterative.add(car.getOriginalValue());
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			List<Double> costs = extract(db.getCars(), on(Car.class).getOriginalValue());
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testIndexCarsByBrand() {
		System.out.println("*** testIndexCarsByBrand");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Map<String, Car> carsByBrandIterative = new HashMap<String, Car>();
			for (Car car : db.getCars())
				carsByBrandIterative.put(car.getBrand(), car);
		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Map<String, Car> carsByBrand = index(db.getCars(), on(Car.class).getBrand());
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testGroupSalesByBuyersAndSellers() {
		System.out.println("*** testGroupSalesByBuyersAndSellers");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Person youngestPerson = null;
			Person oldestPerson = null;
			for (Person person : db.getPersons()) {
				if (youngestPerson == null || person.getAge() < youngestPerson.getAge())
					youngestPerson = person;
				if (oldestPerson == null || person.getAge() > oldestPerson.getAge())
					oldestPerson = person;
			}
			Map<Person, Map<Person, Sale>> map = new HashMap<Person, Map<Person, Sale>>();
			for (Sale sale : db.getSales()) {
				Person buyer = sale.getBuyer();
				Map<Person, Sale> buyerMap = map.get(buyer);
				if (buyerMap == null) {
					buyerMap = new HashMap<Person, Sale>();
					map.put(buyer, buyerMap);
				}
				buyerMap.put(sale.getSeller(), sale);
			}
			Sale saleIterative = map.get(youngestPerson).get(oldestPerson);

		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Person youngest = selectMin(db.getPersons(), on(Person.class).getAge());
			Person oldest = selectMax(db.getPersons(), on(Person.class).getAge());
			Group<Sale> group = group(db.getSales(), by(on(Sale.class).getBuyer()), by(on(Sale.class).getSeller()));
			Sale sale = group.findGroup(youngest).find(oldest).get(0);
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}

	private static long[] testFindMostBoughtCar() {
		System.out.println("*** testFindMostBoughtCar");
		long start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Map<Car, Integer> carsBought = new LinkedHashMap<Car, Integer>();
			for (Sale sale : db.getSales()) {
				Car car = sale.getCar();
				Integer boughtTimes = carsBought.get(car);
				carsBought.put(car, boughtTimes == null ? 1 : boughtTimes + 1);
			}
			Car mostBoughtCarIterative = null;
			int boughtTimesIterative = 0;
			for (Entry<Car, Integer> entry : carsBought.entrySet()) {
				if (entry.getValue() > boughtTimesIterative) {
					mostBoughtCarIterative = entry.getKey();
					boughtTimesIterative = entry.getValue();
				}
			}

		}

		long duration1 = System.currentTimeMillis() - start;
		System.out.println("iterative: " + duration1 + " msecs");
		start = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Group<Sale> group = selectMax(group(db.getSales(), by(on(Sale.class).getCar())).subgroups(), on(Group.class).getSize());
			Car mostBoughtCar = group.first().getCar();
			int boughtTimes = group.getSize();
		}

		long duration2 = System.currentTimeMillis() - start;
		System.out.println("lambdaj: " + duration2 + " msecs");
		return new long[] {duration1, duration2};
	}
}
