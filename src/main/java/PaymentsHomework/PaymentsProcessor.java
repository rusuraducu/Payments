package PaymentsHomework;

import com.csvreader.CsvReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentsProcessor {
    private String requestsPath;
    private String customersPath;
    private String rejectionsCSV;

    private List<PaymentsRejection> paymentsRejection = new ArrayList<>();
    private Map<String/*IBAN*/, CustomerAccount> customerAccountMap = new HashMap<>();
    private ArrayList<Payments> paymentsRequestList = new ArrayList<>();

    public void getPaymentsRequest(String requestsPath) {
        this.requestsPath = requestsPath;
    }

    public void getCustomersAccount(String customersPath) {
        this.customersPath = customersPath;
    }

    public void setRejectionsCSV(String rejectionsCSV) {
        this.rejectionsCSV = rejectionsCSV;
    }

    public PaymentsProcessor loadPayments() {
        try {
            CsvReader payments = new CsvReader(requestsPath);
            payments.readHeaders();
            while (payments.readRecord()) {
                String surname = payments.get("surname");
                String name = payments.get("name");
                Customer from = new Customer(surname, name);
                String IBAN = payments.get("IBAN");
                double amount = Double.parseDouble(payments.get("amount"));
                String comment = payments.get("comment");
                String TO_IBAN = payments.get("TO_IBAN");
                String beneficiaryName = payments.get("beneficiary name");
                String beneficiarySurname = payments.get("beneficiary surname");
                Customer to = new Customer(beneficiarySurname, beneficiaryName);
                paymentsRequestList.add(new Payments(from, to, IBAN, TO_IBAN, amount, comment));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PaymentsProcessor customerAccount() {
        try {
            CsvReader customers = new CsvReader(customersPath);
            customers.readHeaders();
            while (customers.readRecord()) {
                String surname = customers.get("surname");
                String name = customers.get("name");
                Customer c = new Customer(surname, name);
                String IBAN = customers.get("IBAN");
                double amount = Double.parseDouble(customers.get("amount"));
                CustomerAccount ac = new CustomerAccount(c, IBAN, amount);
                customerAccountMap.put(ac.getIBAN(), ac);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PaymentsProcessor processingPayments() {
        for (Payments p : paymentsRequestList) {
            String validateIBAN = validateIBAN(p.getIBAN_from());
            Boolean theReceiverIsOurCustomer = theReceiverIsOurCustomer(p.getIBAN_to());

            if (!validateIBAN.equals("OK")) {
                paymentsRejection.add(new PaymentsRejection(p, validateIBAN));
                continue;
            }
            else {
                String validateCustomer = validateCustomer(p.getIBAN_from(), p.getFrom());
                if (!validateCustomer.equals("OK")) {
                    paymentsRejection.add(new PaymentsRejection(p, validateCustomer));
                    continue;
                }
            }
            String validateAmount = validateAmount(p.getIBAN_from(), p.getAmount());
            if (!validateAmount.equals("OK")) {
                paymentsRejection.add(new PaymentsRejection(p, validateAmount));
                continue;
            }
            decreaseAccountFrom(p.getIBAN_from(), p.getAmount());
            if (theReceiverIsOurCustomer) {
                increaseAccountTo(p.getIBAN_to(), p.getAmount());
            }
        }
        return this;
    }

    public void getReport() {
        char percentage = '%';
        System.out.println("Total payments request: "+ paymentsRequestList.size());
        System.out.println("===========================");
        System.out.println("Success Rate:        "+successRate()+"%");
        System.out.println("Rejection Rate:      "+rejectionRate()+"%");
        System.out.println("===========================");
        System.out.println("    REJECTION REASONS   ");
        System.out.printf("Wrong customer:     %.2f%c \n",customerRejectionRate(), percentage);
        System.out.printf("Wrong IBAN:         %.2f%c \n",ibanRejectionRate(), percentage);
        System.out.printf("Insufficient funds: %.2f%c \n",insufficientFundsRejectionRate(), percentage);
        System.out.println("===========================");
    }

    private double successRate() {
        double totalPayments = paymentsRequestList.size();
        double acceptedPayments = paymentsRequestList.size() - paymentsRejection.size();
        return (acceptedPayments / totalPayments) * 100;
    }

    private double rejectionRate() {
        if (paymentsRejection.size() > 0) {
            return ((double) paymentsRejection.size() / (double) paymentsRequestList.size()) * 100;
        }
        return 0;
    }

    private double customerRejectionRate() {
        double customerRejection = 0;
        for (PaymentsRejection pr : paymentsRejection) {
            if (pr.getRejectReason().equals("The customer doesn't exist")) {
                customerRejection++;
            }
        }
        return (customerRejection / (double) paymentsRejection.size()) * 100;
    }

    private double ibanRejectionRate() {
        double ibanRejection = 0;
        for (PaymentsRejection pr : paymentsRejection) {
            if (pr.getRejectReason().equals("The IBAN doesn't exist.")) {
                ibanRejection++;
            }
        }
        return (ibanRejection / (double) paymentsRejection.size()) * 100;
    }

    private double insufficientFundsRejectionRate() {
        double insufficientFundsRejection = 0;
        for (PaymentsRejection pr : paymentsRejection) {
            if (pr.getRejectReason().equals("Insufficient funds.")) {
                insufficientFundsRejection++;
            }
        }
        return (insufficientFundsRejection / (double) paymentsRejection.size()) * 100;
    }

    private String validateCustomer(String IBAN_from, Customer c) {
        if (customerAccountMap.get(IBAN_from).getCustomer().equals(c)) {
            return "OK";
        }
        return "The customer doesn't exist";
    }

    private String validateIBAN(String from_IBAN) {
        if (customerAccountMap.containsKey(from_IBAN)) {
            return "OK";
        }
        return "The IBAN doesn't exist.";
    }

    private String validateAmount(String IBAN, double amount) {
        double availableFunds = customerAccountMap.get(IBAN).getAmount();
        if (availableFunds >= amount) {
            return "OK";
        }
        return "Insufficient funds.";
    }

    private Boolean theReceiverIsOurCustomer(String IBAN_to) {
        if (customerAccountMap.containsKey(IBAN_to)) {
            return true;
        }
        return false;
    }

    private void decreaseAccountFrom(String IBAN_from, double payment) {
        double currentSold = customerAccountMap.get(IBAN_from).getAmount();
        double newSold = currentSold - payment;
        customerAccountMap.get(IBAN_from).setAmount(newSold);
    }

    private void increaseAccountTo(String IBAN_to, double income) {
        double currentSold = customerAccountMap.get(IBAN_to).getAmount();
        double newSold = currentSold + income;
        customerAccountMap.get(IBAN_to).setAmount(newSold);
    }

    public PaymentsProcessor writeRejectionsToCSV() {
        String header = "from_IBAN, to_IBAN, Amount, Comment, Reject Reason";
        if (paymentsRejection.size() > 0) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rejectionsCSV))) {
                bw.write(header);
                for (PaymentsRejection pr : paymentsRejection) {
                    bw.newLine();
                    bw.write(pr.toCSV());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
