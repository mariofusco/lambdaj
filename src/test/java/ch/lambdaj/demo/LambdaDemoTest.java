package ch.lambdaj.demo;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.demo.Util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;
import java.util.Map.*;

import org.junit.*;

import ch.lambdaj.group.*;

public class LambdaDemoTest {

	private final Db db = Db.getInstance();
	
	@Test
	public void testPrintAllBrands() {
		StringBuilder sb = new StringBuilder();
		for (Car car : db.getCars()) 
			sb.append(car.getBrand()).append(", ");
		String brandsIterative = sb.toString().substring(0, sb.length()-2);

		String brands = joinFrom(db.getCars()).getBrand();

		assertEquals(brandsIterative, brands);
	}
	
	@Test
	public void testFindAllSalesOfAFerrari() {
		List<Sale> salesIterative = new ArrayList<Sale>();
		for (Sale sale : db.getSales()) {
			if (sale.getCar().getBrand().equals("Ferrari")) salesIterative.add(sale);
		}

		List<Sale> sales = select(db.getSales(), having(on(Sale.class).getCar().getBrand(), equalTo("Ferrari")));

		assertTrue(listsAreEqual(sales, salesIterative));
	}
	
	@Test
	public void testFindAllBuysOfYoungestPerson() {
		Person youngest = null;
		for (Person person : db.getPersons()) {
			if (youngest == null || person.getAge() < youngest.getAge()) youngest = person;
		}
		List<Sale> salesIterative = new ArrayList<Sale>();
		for (Sale sale : db.getSales()) {
			if (sale.getBuyer().equals(youngest)) salesIterative.add(sale);
		}

		List<Sale> sales = select(db.getSales(), having(on(Sale.class).getBuyer(), equalTo(selectMin(db.getPersons(), on(Person.class).getAge()))));
		
		assertTrue(listsAreEqual(sales, salesIterative));
	}

	@Test
	public void testFindMostCostlySaleValue() {
		double maxCost = 0.0;
		for (Sale sale : db.getSales()) {
			double cost = sale.getCost();
			if (cost > maxCost) maxCost = cost;
		}

		double max = max(db.getSales(), on(Sale.class).getCost());
		assertEquals(max, maxCost, .001);

		double maxFrom = maxFrom(db.getSales()).getCost();
		assertEquals(maxFrom, maxCost, .001);
	}
	
	@Test
	public void testSumSalesCostWhereBothActorsAreAMale() {
		double sumIterative = 0.0;
		for (Sale sale : db.getSales()) {
			if (sale.getBuyer().isMale() && sale.getSeller().isMale())
				sumIterative += sale.getCost();
		}

		double sum = sum(select(db.getSales(), having(on(Sale.class).getBuyer().isMale()).and(having(on(Sale.class).getSeller().isMale()))), on(Sale.class).getCost());
		assertEquals(sum, sumIterative, .001);

		double sumFrom = sumFrom(select(db.getSales(), having(on(Sale.class).getBuyer().isMale()).and(having(on(Sale.class).getSeller().isMale())))).getCost();
		assertEquals(sumFrom, sumIterative, .001);
	}

	@Test
	public void testFindYoungestAgeOfWhoBoughtACarForMoreThan50000() {
		int ageIterative = Integer.MAX_VALUE;
		for (Sale sale : db.getSales()) {
			if (sale.getCost() > 50000.00) {
				int buyerAge = sale.getBuyer().getAge();
				if (buyerAge < ageIterative) ageIterative = buyerAge;
			}
		}

		int age = min(forEach(select(db.getSales(), having(on(Sale.class).getCost(), greaterThan(50000.00)))).getBuyer(), on(Person.class).getAge());

		assertEquals(age, ageIterative);
	}
	
	@Test
	public void testSortSalesByCost() {
		List<Sale> sortedSalesIterative = new ArrayList<Sale>(db.getSales());
		Collections.sort(sortedSalesIterative, new Comparator<Sale>() {
			public int compare(Sale s1, Sale s2) {
				return Double.valueOf(s1.getCost()).compareTo(s2.getCost());
			}
		});

		List<Sale> sortedSales = sort(db.getSales(), on(Sale.class).getCost());

		assertTrue(listsAreEqual(sortedSales, sortedSalesIterative));
	}
	
	@Test
	public void testExtractCarsOriginalCost() {
		List<Double> costsIterative = new ArrayList<Double>();
		for (Car car : db.getCars()) costsIterative.add(car.getOriginalValue());

		List<Double> costs = extract(db.getCars(), on(Car.class).getOriginalValue());

		assertTrue(listsAreEqual(costs, costsIterative));
	}	

	@Test
	public void testIndexCarsByBrand() {
		Map<String, Car> carsByBrandIterative = new HashMap<String, Car>();
		for (Car car : db.getCars()) carsByBrandIterative.put(car.getBrand(), car);

		Map<String, Car> carsByBrand = index(db.getCars(), on(Car.class).getBrand());

		assertEquals(carsByBrand.get("Ferrari"), carsByBrandIterative.get("Ferrari"));
	}	
	
	@Test
	public void testGroupSalesByBuyersAndSellers() {
		Person youngestPerson = null;
		Person oldestPerson = null;
		for (Person person : db.getPersons()) {
			if (youngestPerson == null || person.getAge() < youngestPerson.getAge()) youngestPerson = person;
			if (oldestPerson == null || person.getAge() > oldestPerson.getAge()) oldestPerson = person;
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
		
		Person youngest = selectMin(db.getPersons(), on(Person.class).getAge());
		Person oldest = selectMax(db.getPersons(), on(Person.class).getAge());
		Group<Sale> group = group(db.getSales(), by(on(Sale.class).getBuyer()), by(on(Sale.class).getSeller()));
		Sale sale = group.findGroup(youngest).findGroup(oldest).first();

		assertEquals(sale, saleIterative);
	}
	
    @Test
    public void testGroupSalesByBuyersSortedByAge() {
        Map<Person, List<Sale>> map = new TreeMap<Person, List<Sale>>(new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.getAge() - p2.getAge();
            }
        });
        for (Sale sale : db.getSales()) {
            Person buyer = sale.getBuyer();
            List<Sale> sales = map.get(buyer);
            if (sales == null) {
                sales = new ArrayList<Sale>();
                map.put(buyer, sales);
            }
            sales.add(sale);
        }

        Group<Sale> group = group(db.getSales(), by(on(Sale.class).getBuyer()).sort(on(Person.class).getAge()));

        Iterator<Group<Sale>> groups = group.subgroups().iterator();
        for (List<Sale> iterativeSales : map.values()) {
            List<Sale> lambdajSales = groups.next().findAll();
            assertTrue(iterativeSales.containsAll(lambdajSales));
            assertTrue(lambdajSales.containsAll(iterativeSales));
        }
    }

	@Test
	public void testFindMostBoughtCar() {
		Map<Car, Integer> carsBought = new LinkedHashMap<Car, Integer>();
		for (Sale sale : db.getSales()) {
			Car car = sale.getCar();
			Integer boughtTimes = carsBought.get(car);
			carsBought.put(car, boughtTimes == null ? 1 : boughtTimes+1);
		}
		Car mostBoughtCarIterative = null;
		int boughtTimesIterative = 0;
		for (Entry<Car, Integer> entry : carsBought.entrySet()) {
			if (entry.getValue() > boughtTimesIterative) {
				mostBoughtCarIterative = entry.getKey();
				boughtTimesIterative = entry.getValue();
			}
		}
		
		Group<Sale> group = selectMax(group(db.getSales(), by(on(Sale.class).getCar())).subgroups(), on(Group.class).getSize());
		Car mostBoughtCar = group.first().getCar();
		int boughtTimes = group.getSize();
		
		assertEquals(boughtTimesIterative, boughtTimes);
		System.out.println("mostBoughtCarIterative = " + mostBoughtCarIterative);
		System.out.println("mostBoughtCar = " + mostBoughtCar);
//		assertEquals(mostBoughtCarIterative, mostBoughtCar);
	}
}
