package Model;

public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String division;
    private String country;
    private String phone;


    public Customer(int customerId, String customerName, String address, String division, String country, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.division = division;
        this.country = country;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) { this.division = division; }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
