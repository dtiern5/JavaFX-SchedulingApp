package Model;

public class Divisions {

    private int divisionId;
    private String division;
    private int COUNTRY_ID;

    public Divisions(int divisionId, String division, int COUNTRY_ID) {
        this.divisionId = divisionId;
        this.division = division;
        this.COUNTRY_ID = COUNTRY_ID;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(int COUNTRY_ID) {
        this.COUNTRY_ID = COUNTRY_ID;
    }
}

