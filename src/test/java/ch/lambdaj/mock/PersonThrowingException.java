package ch.lambdaj.mock;

public class PersonThrowingException extends Person {
    @Override
    public void setLastName(String lastName) {
        throw new RuntimeException("Cannot set last name");
    }
}
