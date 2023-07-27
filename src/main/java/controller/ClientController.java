package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Client;
import utils.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class ClientController implements Initializable {

    @FXML
    private TextField cf;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private TextField mail;

    @FXML
    private TextField tel;

    @FXML
    private Button insert;

    @FXML
    private TextField cf2;

    @FXML
    private Button showCinecards;

    @FXML
    private TableView<Client> table;

    @FXML
    private TableColumn<Client, String> cf_column;

    @FXML
    private TableColumn<Client, String> cognome;

    @FXML
    private TableColumn<Client, Date> dataNascita;

    @FXML
    private TableColumn<Client, String> mail_column;

    @FXML
    private TableColumn<Client, String> nome;

    @FXML
    private TableColumn<Client, Integer> telefono;

    @FXML
    void insertClient(ActionEvent event) {
        String clientID = cf.getText();
        String clientName = name.getText();
        String clienteSurname = surname.getText();
        String clientMail = mail.getText();
        String clientTel = tel.getText();
        LocalDate date = dateOfBirth.getValue();
        if(!clientID.isEmpty() && !clientName.isEmpty() && !clienteSurname.isEmpty() && !clientMail.isEmpty()) {
            Client client = new Client(clientID,clientName, clienteSurname, Utils.localDateToDate(date), clientTel.isEmpty() ? Optional.empty() : Optional.of(Integer.parseInt(clientTel)), clientMail);
            Controller.getClientTable().save(client);
            table.setItems(FXCollections.observableArrayList(Controller.getClientTable().findAll()));
        } else {
            Alert allert = new Alert(AlertType.WARNING);
            allert.setHeaderText("Inserisci tutti i campi contrassegnati da *");
            allert.show();
        }
    }

    @FXML
    void showCinecards(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cf_column.setCellValueFactory(new PropertyValueFactory<Client, String>("cf"));
        nome.setCellValueFactory(new PropertyValueFactory<Client, String>("nome"));
        cognome.setCellValueFactory(new PropertyValueFactory<Client, String>("cognome"));
        dataNascita.setCellValueFactory(new PropertyValueFactory<Client, Date>("dataNascita"));
        telefono.setCellValueFactory(new PropertyValueFactory<Client, Integer>("telefono"));
        mail_column.setCellValueFactory(new PropertyValueFactory<Client, String>("mail"));

        table.setItems(FXCollections.observableArrayList(Controller.getClientTable().findAll()));
        
    }

}
