package Model;

/**
 * Model for Divisions. Contains a constructor and basic getters and setters.
 */
public class Division {

    private int divisionId;
    private String division;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * Constructor for creating a Division
     * @param divisionId ID of the division
     * @param division Division name
     * @param createDate date the Division was created
     * @param createdBy creator of the Division
     * @param lastUpdate date the Division was last updated
     * @param lastUpdatedBy last updater of the Division
     * @param countryId Country ID of the Division
     */
    public Division(int divisionId, String division, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     * Gets the ID of the Division
     * @return the ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the ID of the Division
     * @param divisionId the ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets the Division
     * @return the Division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the Division
     * @param division the Division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the date the Division was created
     * @return the date the Division was created
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date the Division was created
     * @param createDate the date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the one who created the Division
     * @return the one who created the Division
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the Division
     * @param createdBy the creator of the Division
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the last update of the Division
     * @return the last update of the Division
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the last update of the Division
     * @param lastUpdate the date
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the one who last updated the Division
     * @return
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the last updater of the Division
     * @param lastUpdatedBy the last updater of the Division
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the Division's Country ID
     * @return Division's Country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the Division's Country ID
     * @param countryId the Country ID
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Overrides the toString() method to return the division
     * @return
     */
    @Override
    public String toString() {
        return (division);
    }

}