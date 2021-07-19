package Model;

import java.time.Month;

public class Report {
    private String type;
    private Division division;
    private Integer count;
    private int month;


    public Report(String type, Integer count) {
        this.type = type;
        this.count = count;
    }

    public Report(int month, String type, Integer count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    // Format the month to display in the AppTypeReport
    public String getMonth() {
        String monthString = String.valueOf(Month.of(month));
        return monthString.substring(0, 1) + monthString.substring(1).toLowerCase();
    }

    public void setMonth(int month) {
        this.month = month;
    }

}
