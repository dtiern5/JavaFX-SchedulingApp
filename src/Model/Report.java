package Model;

import java.time.Month;

/**
 * Model for Reports. Contains a constructor and basic getters and setters.
 */
public class Report {
    private String type;
    private Division division;
    private Integer count;
    private int month;

    /**
     * Constructor for creating a Report that counts the types of Appointments
     * @param type type of the appointment
     * @param count count of the type
     */
    public Report(String type, Integer count) {
        this.type = type;
        this.count = count;
    }

    /**
     * Constructor for creating a Report that counts the type of Appointments by month
     * @param month the month
     * @param type type of the appointment
     * @param count count of the type
     */
    public Report(int month, String type, Integer count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**
     * Gets the type for the Report
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the Report
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the Division for the Report
     * @return the Division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Sets the Division for the Report
     * @param division the Division
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    /**
     * Gets the count for the report
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the count for the report
     * @param count the count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    // Format the month to display in the AppTypeReport

    /**
     * Returns a formatted String of the
     * @return a formatted String of the month
     */
    public String getMonth() {
        String monthString = String.valueOf(Month.of(month));
        return monthString.substring(0, 1) + monthString.substring(1).toLowerCase();
    }

    /**
     * Sets the Month for the Report
     * @param month the Month
     */
    public void setMonth(int month) {
        this.month = month;
    }

}
