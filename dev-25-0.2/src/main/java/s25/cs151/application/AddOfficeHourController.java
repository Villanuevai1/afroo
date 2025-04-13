package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddOfficeHourController {

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private TextField yearTextField;
    @FXML private CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox;
    @FXML private TextField timeTextField;

    @FXML
    public void initialize() {
        semesterComboBox.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        semesterComboBox.setValue("Spring"); // Set default semester
    }

    @FXML
    public void onSaveOfficeHourClick() {
        String semester;
        int year;
        StringBuilder days;
        String time;

        try {
            semester = semesterComboBox.getValue();
            year = Integer.parseInt(yearTextField.getText().trim());
            time = timeTextField.getText().trim(); // time is optional

            days = new StringBuilder();
            if (mondayCheckBox.isSelected()) days.append("Monday, ");
            if (tuesdayCheckBox.isSelected()) days.append("Tuesday, ");
            if (wednesdayCheckBox.isSelected()) days.append("Wednesday, ");
            if (thursdayCheckBox.isSelected()) days.append("Thursday, ");
            if (fridayCheckBox.isSelected()) days.append("Friday, ");
            if (days.length() > 0) days.setLength(days.length() - 2);

            // Debug
            System.out.println(" Saving clicked...");
            System.out.println("Semester: " + semester);
            System.out.println("Year: " + year);
            System.out.println("Days: " + days);
            System.out.println("Time: " + time);

            DatabaseHelper dbHelper = new DatabaseHelper();

            //  Check for duplicates before inserting
            if (dbHelper.officeHourExists(semester, year)) {
                new Alert(Alert.AlertType.WARNING,
                        "An office hour for " + semester + " " + year + " already exists.")
                        .showAndWait();
                return;
            }

            //  Safe to insert (even with blank time)
            dbHelper.insertOfficeHour(semester, year, days.toString(), time);
            dbHelper.debugPrintAll();

            //  Close window
            ((Stage) semesterComboBox.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid year.").showAndWait();
        }
    }

    @FXML
    public void onCancelClick() {
        ((Stage) semesterComboBox.getScene().getWindow()).close();
    }


}
