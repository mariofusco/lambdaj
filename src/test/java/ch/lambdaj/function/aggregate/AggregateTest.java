package ch.lambdaj.function.aggregate;

import static ch.lambdaj.function.aggregate.Money.*;
import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class AggregateTest {

	@Test   
	public void testAggregateGrossPremium(){
		Premium endorsementPremium1 = premiumOf("1,311,314,330.89 GBP");
		Premium endorsementPremium2 = premiumOf("96,563,001.19 GBP");
		Premium endorsementPremium3 = premiumOf("4,443,725.16 GBP");
		
		List<Premium> endorsementPremiums = asList(endorsementPremium1, endorsementPremium2, endorsementPremium3);
		
		Premium totalizer = aggregateFrom(endorsementPremiums, Premium.class, new MoneyAggregator());
		
		assertEquals(money("1,412,321,057.24 GBP").getValue(), totalizer.getPremium().getValue(), 0.000001);
	}

	private Premium premiumOf(final String value) {
		return new Premium() {
			public Money getPremium() {
				return money(value);
			}
		};
	}
	
	public interface Premium {
		Money getPremium();
	}
}
