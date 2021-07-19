package Model;

/**
 * Model for Contacts. Contains a constructor and basic getters and setters.
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * Constructor for creating a Contact
     * @param contactId ID of the Contact
     * @param contactName name of the Contact
     * @param contactEmail email of the Contact
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Gets the Contact's ID
     * @return the Contact's ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the Contact's ID
     * @return the Contact's ID
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the Contact's name
     * @return the Contact's name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the Contact's name
     * @return the Contact's name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets the Contact's email
     * @return the Contact's email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets the Contact's email
     * @return the Contact's email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Overrides the toString() method to return the Contact's name
     * @return the Contact's name
     */
    @Override
    public String toString() {
        return contactName;
    }
}
