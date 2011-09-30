package ch.lambdaj.mock;

public class ValidatingPersonDto extends PersonDto {

    public ValidatingPersonDto(String name, int age) {
        super(name, age);
        if (age > 30) throw new RuntimeException("too young!");
    }
}
