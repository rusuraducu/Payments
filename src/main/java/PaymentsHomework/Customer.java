package PaymentsHomework;

import java.util.Objects;

public class Customer {
    private String surname;
    private String name;

    public Customer(String surname, String name) {
        this.surname = surname;
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(surname, customer.surname) &&
                Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(surname, name);
    }

    @Override
    public String toString() {
        return " Customer: " +
                " Surname: " + surname  +
                " Name: " + name;
    }
}
