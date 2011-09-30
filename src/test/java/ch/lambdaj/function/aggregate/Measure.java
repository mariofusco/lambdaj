package ch.lambdaj.function.aggregate;

/**
 * @author Luca Marrocco
 */
public class Measure {

	protected String name;

	protected String unit;

	protected double value;

	public Measure(double value) {
		init(getDefaultUnit(), value);
	}

	protected Measure(String string) {
		if (string == null) init();
		else {
			string = string.trim();
			int separatorPos = string.indexOf(' ');
			String u = separatorPos > 0 ? string.substring(separatorPos).trim() : getDefaultUnit();
			if (!isValidUnit(u)) u = getDefaultUnit();

			double v = getDefaultValue();
			try {
				String valueAsString = separatorPos > 0 ? string.substring(0, separatorPos) : string;
				valueAsString = valueAsString.replaceAll(",", "");
				v = Double.parseDouble(valueAsString);
			} catch (NumberFormatException nfe) {}

			init(u, v);
		}
	}

	public Measure(String unit, double value) {
		init(unit, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Measure)) return false;
		Measure anotherMeasure = (Measure) obj;
		return unit.equals(anotherMeasure.unit) && value == anotherMeasure.value;
	}

	protected String getDefaultUnit() {
		return "";
	}

	protected double getDefaultValue() {
		return 0.0;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	private void init() {
		init(getDefaultUnit(), getDefaultValue());
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Formatting
	// ////////////////////////////////////////////////////////////////////////

	private void init(String unit, double value) {
		this.unit = unit == null ? getDefaultUnit() : unit;
		this.value = value;
	}

	protected boolean isValidUnit(String unit) {
		return true;
	}

	public void setName(String name) {
		this.name = name;
	}
}
