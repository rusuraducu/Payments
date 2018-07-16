package PaymentsHomework;

public class Payments {
    private Customer from;
    private Customer to;
    private String IBAN_from;
    private String IBAN_to;
    private double amount;
    private String comment;

    public Payments(Customer from, Customer to, String IBAN_from, String IBAN_to, double amount, String comment) {
        this.from = from;
        this.to = to;
        this.IBAN_from = IBAN_from;
        this.IBAN_to = IBAN_to;
        this.amount = amount;
        this.comment = comment;
    }

    public Customer getFrom() {
        return from;
    }

    public Customer getTo() {
        return to;
    }

    public String getIBAN_from() {
        return IBAN_from;
    }

    public String getIBAN_to() {
        return IBAN_to;
    }

    public double getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public void setFrom(Customer from) {
        this.from = from;
    }

    public void setTo(Customer to) {
        this.to = to;
    }

    public void setIBAN_from(String IBAN_from) {
        this.IBAN_from = IBAN_from;
    }

    public void setIBAN_to(String IBAN_to) {
        this.IBAN_to = IBAN_to;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Payment: " +
                " From: " + from +
                " To: " + to +
                " From IBAN'" + IBAN_from +
                " To IBAN" + IBAN_to +
                " Amount: " + amount +
                " Comment: " + comment;
    }

}
