package ch.lambdaj.mock;

/**
 * @author Mario Fusco
 */
public class PersonDto {

    private final String name;
    private final int age;

    public PersonDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
