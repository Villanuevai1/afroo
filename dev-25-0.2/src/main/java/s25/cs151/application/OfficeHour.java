package s25.cs151.application;

import javafx.beans.property.*;

public class OfficeHour {
    private final StringProperty semester;
    private final IntegerProperty year;
    private final StringProperty day;
    private final StringProperty time;

    public OfficeHour(String semester, int year, String day, String time) {
        this.semester = new SimpleStringProperty(semester);
        this.year = new SimpleIntegerProperty(year);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public StringProperty dayProperty() {
        return day;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public String getSemester() {
        return semester.get();
    }

    public int getYear() {
        return year.get();
    }

    public String getDay() {
        return day.get();
    }

    public String getTime() {
        return time.get();
    }
}
