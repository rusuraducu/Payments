package PaymentsHomework;

public class PaymentsRejection extends Payments{
    private String rejectReason;

    public PaymentsRejection(Customer from, Customer to, String IBAN_from, String IBAN_to, double amount, String comment, String rejectReason) {
        super(from, to, IBAN_from, IBAN_to, amount, comment);
        this.rejectReason = rejectReason;
    }

    public PaymentsRejection(Payments p, String rejectReason) {
        super(p.getFrom(), p.getTo(), p.getIBAN_from(), p.getIBAN_to(), p.getAmount(), p.getComment());
        this.rejectReason = rejectReason;
    }
    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    @Override
    public String toString() {
        return "PaymentsRejection: " + super.toString()+
                "RejectReason: " + rejectReason + '\'' +
                '}';
    }

    public  String toCSV(){
        return super.getIBAN_from()+","+super.getIBAN_to()+","+super.getAmount()+","+super.getComment()+","+rejectReason;
    }

}
