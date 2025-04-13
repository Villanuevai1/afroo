package s25.cs151.application;

import javafx.beans.property.*;

public class TimeSlot {
    private final StringProperty fromHour;
    private final StringProperty toHour;

    public TimeSlot(String fromHour, String toHour) {
        this.fromHour = new SimpleStringProperty(fromHour);
        this.toHour = new SimpleStringProperty(toHour);
    }

    public StringProperty fromHourProperty() {
        return fromHour;
    }

    public StringProperty toHourProperty() {
        return toHour;
    }

    public String getFromHour() {
        return fromHour.get();
    }

    public String getToHour() {
        return toHour.get();
    }
}
