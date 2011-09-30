package ch.lambdaj.demo;

import static ch.lambdaj.demo.Util.*;

import java.util.*;

public class Person {

	private String firstName;
	private String lastName;
	private boolean male;
	private Date birthday;
	
	protected Person() { }
	
	public Person(String firstName, String lastName, boolean male, String birthday) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.male = male;
		this.birthday = formatDate(birthday);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
	
	public boolean isMale() {
		return male;
	}
	
	public int getAge() {
		return getCurrentYear() - getBirthYear();
	}
	
	public int getBirthYear() {
		return getYear(birthday);
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	@Override
	public String toString() {
		return getFullName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return Person.class.isInstance(obj) && getFullName().equals(((Person)obj).getFullName());
	}
	
	@Override
	public int hashCode() {
		return getFullName().hashCode();
	}
}
