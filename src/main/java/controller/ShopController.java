package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class ShopController implements Initializable {

    @FXML
    private Button buyCinecard;

    @FXML
    private Button buyTicket;

    @FXML
    private TextField cf;

    @FXML
    private TextField cf2;

    @FXML
    private TextField cf3;

    @FXML
    private TableColumn<?, ?> cf_column;

    @FXML
    private ComboBox<?> cinecard;

    @FXML
    private TableColumn<?, ?> cinecard_column;

    @FXML
    private TableColumn<?, ?> data;

    @FXML
    private TableColumn<?, ?> dataAcquisto;

    @FXML
    private ComboBox<?> date;

    @FXML
    private TableColumn<?, ?> fila;

    @FXML
    private TextField filmID;

    @FXML
    private TableColumn<?, ?> months;

    @FXML
    private ComboBox<?> numberEntrances;

    @FXML
    private TableColumn<?, ?> ora;

    @FXML
    private TableColumn<?, ?> posto;

    @FXML
    private TableColumn<?, ?> prezzo;

    @FXML
    private TableColumn<?, ?> price;

    @FXML
    private TableColumn<?, ?> sala;

    @FXML
    private ComboBox<?> seat;

    @FXML
    private Button showTickets;

    @FXML
    private ComboBox<?> time;

    @FXML
    private TableColumn<?, ?> totalEntrances;

    @FXML
    void deleteFilm(ActionEvent event) {

    }

    @FXML
    void searchFilm(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
