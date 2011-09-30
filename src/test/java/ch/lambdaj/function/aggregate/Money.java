package ch.lambdaj.function.aggregate;

import java.text.*;

/**
 * @author Luca Marrocco
 */
public final class Money extends Measure {

	private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

	public static Money money(double value) {
		return new Money(value);
	}

	public static Money money(String money) throws NumberFormatException {
		return new Money(money);
	}

	public static Money money(String currency, double value) {
		return new Money(currency, value);
	}

	public Money(double value) {
		super(value);
	}

	public Money(String money) {
		super(money);
	}

	public Money(String currency, double value) {
		super(currency, value);
	}

	public String getCurrency() {
		return unit;
	}

	@Override
	protected String getDefaultUnit() {
		return "EUR";
	}

	public double getValue() {
		return value;
	}

	public String getFormattedValue() {
		return decimalFormat.format(value);
	}

	@Override
	protected boolean isValidUnit(String currency) {
		return currency.matches("\\w{3}");
	}

	public boolean lesserThan(Money anotherMoney) {
		throwExceptionIfArentComparable(anotherMoney);
		return value < anotherMoney.value;
	}

	private void throwExceptionIfArentComparable(Money anotherMoney) {
		if (value == 0.0 || anotherMoney.value == 0.0) return;
		if (anotherMoney.unit.equals(unit)) return;
		throw new RuntimeException(this + " and " + anotherMoney + " are not compatible");
	}

	public Money sum(Money anotherMoney) {
		throwExceptionIfArentComparable(anotherMoney);
		unit = value == 0.0 ? anotherMoney.unit : unit;
		value += anotherMoney.value;
		return money(unit, value);
	}
	
	public static final class MoneyAggregator extends PairAggregator<Money> {
		public Money aggregate(Money money1, Money money2) {
			return money1.sum(money2);
		}
		
		public Money emptyItem() {
			return new Money(0.0);
		}
	}
}