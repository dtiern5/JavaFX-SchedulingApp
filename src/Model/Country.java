package Model;

/**
 * Model for Countries. Contains a constructor and basic getters and setters.
 */
public class Country {

    private int countryID;
    private String countryName;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;

    /**
     * Constructor for creating a Country
     * @param countryID ID of the Country
     * @param countryName name of the Country
     * @param createDate date the Country was created
     * @param createdBy creator of the Country
     * @param lastUpdate date the Country was updated
     * @param lastUpdatedBy last updater of the Country
     */
    public Country(int countryID, String countryName, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.countryID = countryID;
        this.countryName = countryName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the Country's ID
     * @return the Country's ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Sets the Country's ID
     * @return the Country's ID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Gets the Country's name
     * @return the Country's name
     */
    public String getCountryName() {
        return countryName;
    }
    /**
     * Sets the Country's name
     * @return the Country's name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    /**
     * Gets the date the Country was created
     * @return the date the Country was created
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date the Country was created
     * @return the date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the one who created the Country
     * @return the one who created the Country
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the one who created the Country
     * @return the one who created the Country
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date the Country was updated
     * @return the date the Country was updated
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the date the Country was updated
     * @return the date the Country was updated
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the one who last updated the Country
     * @return the one who last updated the Country
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the one who last updated the Country
     * @param lastUpdatedBy the one who last updated the Country
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Overrides the toString() method to return the name of the Country
     * @return the name of the Country
     */
    @Override
    public String toString() {
        return countryName;
    }
}