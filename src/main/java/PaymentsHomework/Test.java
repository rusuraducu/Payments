package PaymentsHomework;

public class Test {
    public static void main(String[] args) {
        PaymentsProcessor paymentsProcessor = new PaymentsProcessor();

        //Files path
        paymentsProcessor.getPaymentsRequest("D:\\requestsCSV.csv");
        paymentsProcessor.getCustomersAccount("D:\\customers.csv");
        paymentsProcessor.setRejectionsCSV("D:\\rejections.csv");

        //Processing payments and get report.
        paymentsProcessor.loadCustomersAccount()
                .loadPayments()
                .processingPayments()
                .writeRejectionsToCSV()
                .getReport();
    }
}
