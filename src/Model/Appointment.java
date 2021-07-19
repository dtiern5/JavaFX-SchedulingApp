package Model;

import DBAccess.DBContacts;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Model for Appointments. Contains a constructor and basic getters and setters.
 */
public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    // For use in tables
    private Contact contact;

    /**
     * Constructor for creating an Appointment
     * @param appointmentId ID of Appointment
     * @param title title of Appointment
     * @param description description of Appointment
     * @param location location of Appointment
     * @param type type of Appointment
     * @param startTime start time of Appointment
     * @param endTime end time of Appointment
     * @param createDate date the Appointment was created
     * @param createdBy creator of the Appointment
     * @param lastUpdate date the Appointment was updated
     * @param lastUpdatedBy updater of the Appointment
     * @param customerId Customer ID of Appointment
     * @param userId User ID of Appointment
     * @param contactId Contact ID of Appointment
     */
    public Appointment(int appointmentId, String title, String description,
                       String location, String type, LocalDateTime startTime, LocalDateTime endTime,
                       LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate,
                       String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;

        try {
            this.contact = DBContacts.getContact(contactId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Gets the Appointment's Contact
     * @return the Appointment's Contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the Appointment's Contact
     * @param contact the Appointment's Contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Gets the Appointment's ID
     * @return the Appointment's ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the Appointment's ID
     * @param appointmentId the Appointment's ID
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the Appointment's title
     * @return the Appointment's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the Appointment's title
     * @param title` the Appointment's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the Appointment's description
     * @return the Appointment's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the Appointment's description
     * @param description the Appointment's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the Appointment's location
     * @return the Appointment's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the Appointment's location
     * @param location the Appointment's location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the Appointment's type
     * @return the Appointment's type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the Appointment's type
     * @param type the Appointment's type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the Appointment's start time
     * @return the Appointment's start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the Appointment's start time
     * @param startTime the Appointment's start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the Appointment's end time
     * @return the Appointment's end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the Appointment's end time
     * @param endTime the Appointment's end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the date the Appointment was created
     * @return the date the Appointment was created
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date the Appointment was created
     * @param createDate the date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the creator of the Appointment
     * @return the creator of the Appointment
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the Appointment
     * @param createdBy the creator of the Appointment
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date of the last update to the Appointment
     * @return the date of the last update to the Appointment
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the date of the last update to the Appointment
     * @param lastUpdate the date of the last update to the Appointment
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the one who last updated the Appointment
     * @return the one who last updated the Appointment
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the one who last updated the Appointment
     * @param lastUpdatedBy the one who last updated the Appointment
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the Customer ID of the Appointment
     * @return the Customer ID of the Appointment
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the Customer ID of the Appointment
     * @param customerId the Customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the User ID of the Appointment
     * @return the User ID of the Appointment
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the Customer ID of the Appointment
     * @param userId the Customer ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the Contact ID of the Appointment
     * @return the Contact ID of the Appointment
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the Contact ID of the Appointment
     * @param contactId the Contact ID to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Overrides the toString() method to return the title of the Appointment
     * @return the title of the Appointment
     */
    @Override
    public String toString() {
        return title;
    }
}