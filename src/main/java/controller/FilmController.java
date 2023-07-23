package controller;

/*import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

public class FilmController {

    @FXML
    private CheckComboBox<?> actors;

    @FXML
    private ComboBox<?> director;

    @FXML
    private TextField duration;

    @FXML
    private DatePicker endDate;

    @FXML
    private CheckComboBox<?> genre;

    @FXML
    private Button insertFilm;

    @FXML
    private TextArea plot;

    @FXML
    private DatePicker startDate;

    @FXML
    private TextField title;

    @FXML
    private TextField year;

}*/

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import app.Controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Film;

public class FilmController implements Initializable {

    @FXML
    private TableView<Film> table;
    
    @FXML
    private TableColumn<Film, Integer> anno;

    @FXML
    private TableColumn<Film, Date> dataFine;

    @FXML
    private TableColumn<Film, Date> dataInizio;

    @FXML
    private TableColumn<Film,Integer> durata;

    @FXML
    private TextField director;

    @FXML
    private TextField duration;

    @FXML
    private TableColumn<Film,String> genere;

    @FXML
    private ComboBox<String> genre;

    @FXML
    private TableColumn<Film,Integer> id;

    @FXML
    private Button insert;

    @FXML
    private TextArea plot;

    @FXML
    private TableColumn<Film,String> regista;

    @FXML
    private TextField title;

    @FXML
    private TableColumn<Film,String> titolo;

    @FXML
    private TableColumn<Film,String> trama;

    @FXML
    private TextField year;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        id.setCellValueFactory(new PropertyValueFactory<Film,Integer>("id"));
        titolo.setCellValueFactory(new PropertyValueFactory<Film,String>("title"));
        regista.setCellValueFactory(new PropertyValueFactory<Film,String>("director"));
        durata.setCellValueFactory(new PropertyValueFactory<Film,Integer>("duration"));
        anno.setCellValueFactory(new PropertyValueFactory<Film,Integer>("year"));
        trama.setCellValueFactory(new PropertyValueFactory<Film,String>("plot"));
        dataInizio.setCellValueFactory(x->new SimpleObjectProperty<Date>(x.getValue().getPeriod().getStartDate()));
        dataFine.setCellValueFactory(x->new SimpleObjectProperty<Date>(x.getValue().getPeriod().getEndDate()));
        //genere.setCellValueFactory(x->new SimpleStringProperty(x.getValue().getGenre().getType()));

        table.setItems(FXCollections.observableArrayList(Controller.getFilmsTable().findAll()));
    }

    @FXML
    void insertFilm(ActionEvent event) {
        /*Film film = new Film(2, "Emily", "Frances O'Connor", 130, 2023,
                                "Cosa si nasconde dietro la creazione di un capolavoro? Emily racconta l'appassionante vita di una delle scrittrici più amate di tutti i tempi, Emily Bronte, mentre trova la sua voce letteraria e scrive uno dei pi\u00F9 importanti classici della letteratura, Cime tempestose.",
                                new Period(Utils.buildDate(3,7,2023).get(), Utils.buildDate(5, 7, 2023).get()),
                                new Genre("Drammatico-Biografico"));
        Controller.getFilmsTable().save(film);
        //table.setItems(FXCollections.observableArrayList(film));
        table.setItems(FXCollections.observableArrayList(Controller.getFilmsTable().findAll()));*/
    }

    

}