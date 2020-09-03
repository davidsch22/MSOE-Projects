package msoe;

public class TrackCourseInfo {
    private String term;
    private String year;
    private String date;

    public TrackCourseInfo(String term, String year, String date) {
        this.term = term;
        this.year = year;
        this.date = date;
    }

    public String getTerm(){
        return this.term;
    }

    public void setTerm(String term){
        this.term = term;
    }

    public String getYear(){
        return this.year;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }
}
