package s25.cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OfficeHourEntry {

    private final StringProperty studentName;
    private final StringProperty date;
    private final StringProperty timeSlot;
    private final StringProperty course;
    private final StringProperty reason;
    private final StringProperty comment;

    public OfficeHourEntry(String studentName, String date, String timeSlot, String course, String reason, String comment) {
        this.studentName = new SimpleStringProperty(studentName);
        this.date = new SimpleStringProperty(date);
        this.timeSlot = new SimpleStringProperty(timeSlot);
        this.course = new SimpleStringProperty(course);
        this.reason = new SimpleStringProperty(reason);
        this.comment = new SimpleStringProperty(comment);
    }

    public StringProperty studentNameProperty() { return studentName; }
    public StringProperty dateProperty() { return date; }
    public StringProperty timeSlotProperty() { return timeSlot; }
    public StringProperty courseProperty() { return course; }
    public StringProperty reasonProperty() { return reason; }
    public StringProperty commentProperty() { return comment; }

    public String getStudentName() { return studentName.get(); }
    public String getDate() { return date.get(); }
    public String getTimeSlot() { return timeSlot.get(); }
    public String getCourse() { return course.get(); }
    public String getReason() { return reason.get(); }
    public String getComment() { return comment.get(); }
}
