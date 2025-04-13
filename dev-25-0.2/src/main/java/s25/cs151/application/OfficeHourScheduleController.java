package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;

public class OfficeHourScheduleController {

    @FXML private TextField studentNameField;
    @FXML private DatePicker scheduleDatePicker;
    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TextField reasonField;
    @FXML private TextField commentField;

    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @FXML
    public void initialize() {
        scheduleDatePicker.setValue(LocalDate.now());

        List<TimeSlot> timeSlots = dbHelper.getAllTimeSlotsSorted();
        timeSlots.forEach(ts -> timeSlotComboBox.getItems().add(ts.getFromHour() + " - " + ts.getToHour()));
        if (!timeSlotComboBox.getItems().isEmpty()) {
            timeSlotComboBox.setValue(timeSlotComboBox.getItems().get(0));
        }

        List<Course> courses = dbHelper.getAllCoursesSortedDescByCode();
        courses.forEach(c -> courseComboBox.getItems().add(c.getCode() + "-" + c.getSection()));
        if (!courseComboBox.getItems().isEmpty()) {
            courseComboBox.setValue(courseComboBox.getItems().get(0));
        }
    }

    @FXML
    public void onSaveClick() {
        String studentName = studentNameField.getText().trim();
        LocalDate date = scheduleDatePicker.getValue();
        String timeSlot = timeSlotComboBox.getValue();
        String course = courseComboBox.getValue();
        String reason = reasonField.getText().trim();
        String comment = commentField.getText().trim();

        if (studentName.isEmpty() || date == null || timeSlot == null || course == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all required fields.").showAndWait();
            return;
        }

        dbHelper.insertOfficeHourEntry(studentName, date.toString(), timeSlot, course, reason, comment);
        new Alert(Alert.AlertType.INFORMATION, "Schedule entry saved successfully!").showAndWait();

        studentNameField.clear();
        scheduleDatePicker.setValue(LocalDate.now());
        timeSlotComboBox.getSelectionModel().selectFirst();
        courseComboBox.getSelectionModel().selectFirst();
        reasonField.clear();
        commentField.clear();
    }

}
