package Model;

/**
 * Model for Users. Contains a constructor and basic getters and setters.
 */
public class User {

    private int userId;
    private String userName;
    private String password;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;

    /**
     * Constructor for creating a User.
     *
     * @param userId ID of the User
     * @param userName Username of the User
     * @param password Password of the User
     * @param createDate Date the User was created
     * @param createdBy Creator of the User
     * @param lastUpdate Date the User was last updated
     * @param lastUpdatedBy Last Updater of the User
     */
    public User(int userId, String userName, String password, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Returns the User's ID
     * @return the ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the User's ID
     * @param userId the ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the User's username
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the User's username
     * @param userName the username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the User's password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the User's password
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the date the User was created
     * @return the date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date the User was created
     * @param createDate the date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the creator of the User
     * @return the creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the User
     * @param createdBy the creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date of the last update to the user
     * @return the date
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the date of the last update to the user
     * @param lastUpdate the date
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the one who last updated the User
     * @return the one who last Updated the User
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the last updater of the USer
     * @param lastUpdatedBy the last updater
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Overrides the toString() method to simply return the Username
     * @return the username
     */
    @Override
    public String toString() {
        return userName;
    }

    /**
     * Returns a string that contains both the User's ID and username
     * @return String with User's ID and username
     */
    public String toStringWithId() {
        return "ID: " + userId + "  |  Name: " + userName;
    }
}
