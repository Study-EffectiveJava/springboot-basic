package prgms.vouchermanagementapp.customer;

public class Customer {

    private Long id;
    private String customerName;

    public Customer(String customerName) {
        this.customerName = customerName;
    }

    public Customer(long id, String customerName) {
        this.id = id;
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }
}
