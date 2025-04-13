package s25.cs151.application;

import javafx.beans.property.*;

public class Course {
    private final StringProperty code;
    private final StringProperty name;
    private final StringProperty section;

    public Course(String code, String name, String section) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.section = new SimpleStringProperty(section);
    }

    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public StringProperty sectionProperty() { return section; }

    public String getCode() { return code.get(); }
    public String getName() { return name.get(); }
    public String getSection() { return section.get(); }
}
