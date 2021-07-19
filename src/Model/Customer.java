package Model;

import DBAccess.DBCountries;
import DBAccess.DBDivisions;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Model for Customers. Contains a constructor and basic getters and setters.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private User createdBy;
    private LocalDateTime lastUpdate;
    private User lastUpdatedBy;
    private int divisionId;

    // For use in tables
    private Division division;
    private Country country;

    /**
     * Constructor for creating a Customer
     * @param customerId ID of the Customer
     * @param customerName name of the Customer
     * @param address address of the Customer
     * @param postalCode postal code of the Customer
     * @param phone phone of the Customer
     * @param createDate date the customer was created
     * @param createdBy creator of the Customer
     * @param lastUpdate date the customer was updated
     * @param lastUpdatedBy last updater of the Customer
     * @param divisionId division ID of the Customer
     */
    public Customer(int customerId, String customerName, String address,
                    String postalCode, String phone, LocalDateTime createDate,
                    User createdBy, LocalDateTime lastUpdate, User lastUpdatedBy, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;

        try {
            this.division = DBDivisions.getDivision(divisionId);
            assert division != null;
            this.country = DBCountries.getCountry(division.getCountryId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets the Division of the Customer
     * @return the Division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Sets the Division of the Customer
     * @param division the Division
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    /**
     * Gets the Country of the Customer
     * @return the Country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the Country of the Customer
     * @param country the Country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Gets the ID of the Customer
     * @return the ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the ID of the Customer
     * @param customerId Customer's ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the name of the Customer
     * @return the name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the Customer
     * @param customerName the name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the address of the Customer
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the Customer
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the postal code of the Customer
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     * Sets the postal code of the Customer
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the phone number of the Customer
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the Customer
     * @param phone the phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the date the Customer was created
     * @return the date the Customer was created
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date the Customer was created
     * @param createDate the date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the one who created the Customer
     * @return the one who created the Customer
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the Customer
     * @param createdBy the creator of the Customer
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the last update of the Customer
     * @return the last update of the Customer
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the last update of the Customer
     * @param lastUpdate the date
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /**
     * Gets the one who last updated the Customer
     * @return the one who last updated the Customer
     */
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the last updater of the Customer
     * @param lastUpdatedBy the last updater of the Customer
     */
    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the Division ID of the Customer
     * @return the Division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the Division ID of the Customer
     * @param divisionId the Division ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Returns a string that contains both the Customer's ID and name
     * @return String with Customer's ID and name
     */
    public String toStringWithId() {
        return "ID: " + customerId + "  |  Name: " + customerName;
    }

    /**
     * Overrides the toString() method to return the Customer's name
     * @return the Customer's name
     */
    @Override
    public String toString() {
        return customerName;
    }


}