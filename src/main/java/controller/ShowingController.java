package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class ShowingController implements Initializable {

    @FXML
    private TableColumn<?, ?> allSeats;

    @FXML
    private TableColumn<?, ?> booked;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<?> date3;

    @FXML
    private TableColumn<?, ?> date_column;

    @FXML
    private TextField filmID;

    @FXML
    private TextField filmID2;

    @FXML
    private TextField filmID3;

    @FXML
    private TableColumn<?, ?> film_column;

    @FXML
    private Button insertShow;

    @FXML
    private TableColumn<?, ?> line_column;

    @FXML
    private TableColumn<?, ?> seat_column;

    @FXML
    private Button show;

    @FXML
    private Button showAll;

    @FXML
    private Button showSeats;

    @FXML
    private TextField startTime;

    @FXML
    private TextField theaterID;

    @FXML
    private TableColumn<?, ?> theater_column;

    @FXML
    private TableColumn<?, ?> theater_column2;

    @FXML
    private ComboBox<?> time;

    @FXML
    private TableColumn<?, ?> time_column;

    @FXML
    void deleteFilm(ActionEvent event) {

    }

    @FXML
    void insertShow(ActionEvent event) {

    }

    @FXML
    void searchFilm(ActionEvent event) {

    }

    @FXML
    void show(ActionEvent event) {

    }

    @FXML
    void showAll(ActionEvent event) {

    }

    @FXML
    void showSeats(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }

}
