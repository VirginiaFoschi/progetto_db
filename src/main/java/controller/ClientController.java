package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Client;
import utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
    private Button search;

    @FXML
    private Button showAll;

    @FXML
    private TableView<Client> table;

    @FXML
    private TableColumn<Client, String> cf_column;

    @FXML
    private TableColumn<Client, String> cognome;

    @FXML
    private TableColumn<Client, String> dataNascita;

    @FXML
    private TableColumn<Client, String> mail_column;

    @FXML
    private TableColumn<Client, String> nome;

    @FXML
    private TableColumn<Client, String> telefono;

    @FXML
    void insertClient(ActionEvent event) {
        String clientID = cf.getText();
        String clientName = name.getText();
        String clienteSurname = surname.getText();
        String clientMail = mail.getText();
        String clientTel = tel.getText();
        LocalDate date = dateOfBirth.getValue();
        if(!clientID.isEmpty() && !clientName.isEmpty() && !clienteSurname.isEmpty() && !clientMail.isEmpty()) {
            if(clientID.length()!=16) {
                Controller.allertNotExist("il codice fiscale deve avere esattamente 16 caratteri");
            } else {
                Client client = new Client(clientID,clientName, clienteSurname, Utils.localDateToDate(date), clientTel.isEmpty() ? Optional.empty() : Optional.of(clientTel), clientMail);
                Controller.getClientTable().save(client);
                table.setItems(FXCollections.observableArrayList(Controller.getClientTable().findAll()));
                clear();
            }
        } else {
            Controller.allert();
        }
    }

    private void clear() {
        cf.clear();
        cf2.clear();
        name.clear();
        surname.clear();
        mail.clear();
        tel.clear();
        dateOfBirth.getEditor().clear();
    }

    @FXML
    void searchClient(ActionEvent event) {
        String client = cf2.getText();
        if(!client.isEmpty()) {
            Controller.getClientTable().findByPrimaryKey(client).ifPresentOrElse(x->table.setItems(FXCollections.observableArrayList(x)),()->table.getItems().clear());
            clear();
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showAll(ActionEvent event) {
        table.setItems(FXCollections.observableArrayList(Controller.getClientTable().findAll()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cf_column.setCellValueFactory(new PropertyValueFactory<Client, String>("cf"));
        nome.setCellValueFactory(new PropertyValueFactory<Client, String>("nome"));
        cognome.setCellValueFactory(new PropertyValueFactory<Client, String>("cognome"));
        dataNascita.setCellValueFactory(x->new SimpleObjectProperty<String>(utils.Utils.printDate(x.getValue().getDataNascita())));//new PropertyValueFactory<Client, Date>("dataNascita"));
        telefono.setCellValueFactory(x->new SimpleObjectProperty<String>(x.getValue().getTelefono().orElse(null)));
        mail_column.setCellValueFactory(new PropertyValueFactory<Client, String>("mail"));

        table.setItems(FXCollections.observableArrayList(Controller.getClientTable().findAll()));
        
    }

}
