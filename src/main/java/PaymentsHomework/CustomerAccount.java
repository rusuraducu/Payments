package PaymentsHomework;

public class CustomerAccount {
    private Customer customer;
    private String IBAN;
    private double amount;

    public CustomerAccount(Customer customer, String IBAN, double amount) {
        this.customer = customer;
        this.IBAN = IBAN;
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getIBAN() {
        return IBAN;
    }

    public double getAmount() {
        return amount;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
