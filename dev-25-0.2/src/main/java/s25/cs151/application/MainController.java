

package s25.cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.EventObject;

public class MainController {

    @FXML
    public void onManageOfficeHoursClick(ActionEvent event) {
        try {
            System.out.println("Clicked Manage Office Hours"); // Debug line

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/office_hours.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setTitle("Manage Office Hours");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onManageCoursesClick(ActionEvent event) {
            try {
                System.out.println("Clicked Manage Office Hours"); // Debug line

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/courses.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1920, 1080));
                stage.setTitle("Manage Office Hours");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    public void onManageTimeClick(ActionEvent actionEvent) throws IOException {
        try{
            System.out.println("Clicked Time Slots");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/time_slots.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setTitle("Time Slots");
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    @FXML
    public void onManageEntriesClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/manage_office_hour_entries.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setTitle("Manage Office Hour Entries");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

