package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CoursesController {

    @FXML private TextField courseCodeField;
    @FXML private TextField courseNameField;
    @FXML private TextField sectionNumberField;

    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> sectionColumn;

    private final ObservableList<Course> coursesList = FXCollections.observableArrayList();
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    private static final String DB_PATH = "data/identifier.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        sectionColumn.setCellValueFactory(cellData -> cellData.getValue().sectionProperty());

        loadCourses();
    }

    @FXML
    public void onSaveCourseClick() {
        String code = courseCodeField.getText().trim();
        String name = courseNameField.getText().trim();
        String section = sectionNumberField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || section.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields must be filled.").showAndWait();
            return;
        }

        if (dbHelper.courseExists(code, name, section)) {
            new Alert(Alert.AlertType.WARNING, "This course already exists.").showAndWait();
            return;
        }

        dbHelper.insertCourse(code, name, section);
        new Alert(Alert.AlertType.INFORMATION, "Course saved successfully!").showAndWait();

        courseCodeField.clear();
        courseNameField.clear();
        sectionNumberField.clear();

        loadCourses();
    }

    @FXML
    public void onCancelClick() {
        courseCodeField.clear();
        courseNameField.clear();
        sectionNumberField.clear();
    }

    private void loadCourses() {
        List<Course> sortedCourses = dbHelper.getAllCoursesSortedDescByCode();
        coursesList.setAll(sortedCourses);
        coursesTable.setItems(coursesList);
    }

    @FXML
    public void onBackToDashboardClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
