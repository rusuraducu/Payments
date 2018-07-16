package PaymentsHomework;

public class Test {
    public static void main(String[] args) {
        PaymentsProcessor paymentsProcessor = new PaymentsProcessor();

        //Files path
        paymentsProcessor.getPaymentsRequest("D:\\requestsCSV.csv");
        paymentsProcessor.getCustomerAccounts("D:\\customers.csv");
        paymentsProcessor.setRejectionsCSV("D:\\rejections.csv");

        //Processing payments and get report.
        paymentsProcessor.customerAccount()
                .loadPayments()
                .processingPayments()
                .writeRejectionsToCSV()
                .getReport();
    }
}
