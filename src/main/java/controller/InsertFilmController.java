package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Cast;
import model.Corrispondence;
import model.Film;
import model.FilmDetail;
import model.Genre;
import model.Participation;
import model.Period;
import model.ProgrammingMode;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import app.Controller;

public class InsertFilmController implements Initializable{

    @FXML
    private TextField anno;

    @FXML
    private CheckComboBox<Cast> attori;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField durata;

    @FXML
    private DatePicker finePeriodo;

    @FXML
    private CheckComboBox<String> genere;

    @FXML
    private DatePicker inizioPerido;

    @FXML
    private Button insertFilm;

    @FXML
    private ComboBox<Cast> regista;

    @FXML
    private TextField titolo;

    @FXML
    private CheckComboBox<String> modProg;

    @FXML
    private TextArea trama;

    @FXML
    private void insertFilm(ActionEvent event) throws IOException {
        String title = titolo.getText();
        List<Cast> actors = attori.getCheckModel().getCheckedItems();
        Cast director = regista.getSelectionModel().getSelectedItem();
        LocalDate startDate = inizioPerido.getValue();
        LocalDate endDate = finePeriodo.getValue();
        String duration = durata.getText();
        String year = anno.getText();
        String plot = trama.getText();
        List<Genre> genre = genere.getCheckModel().getCheckedItems().stream().map(x->Controller.getGenreTable().findByPrimaryKey(x).get()).collect(Collectors.toList());
        List<ProgrammingMode> modes = modProg.getCheckModel().getCheckedItems().stream().map(x->Controller.getProgrammingModesTable().findByPrimaryKey(x).get()).collect(Collectors.toList());
        if (!title.isEmpty() && !actors.isEmpty() && director != null 
            && startDate !=null && endDate !=null && startDate.isBefore(endDate) &&
            !duration.isEmpty() && !year.isEmpty() && !genre.isEmpty()) {
                Period period =  new Period(Utils.localDateToDate(startDate),Utils.localDateToDate(endDate));
                Controller.getPeriodTable().save(period);
                Film film = new Film(title, Integer.parseInt(duration), Integer.parseInt(year), Optional.of(plot),period,director.getId());
                Controller.getFilmsTable().save(film);
                int filmID = Controller.getFilmsTable().getLastID();
                genre.forEach(x->Controller.getCorrispondenceTable().save(new Corrispondence(filmID, x.getType())));
                actors.forEach(x->Controller.getParticipationTable().save(new Participation(filmID, x.getId())));
                modes.forEach(x->Controller.getFilmDetailTable().save(new FilmDetail(x.getType(), filmID)));
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }
        else {
            Controller.allert();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        genere.getItems().addAll(FXCollections.observableArrayList(Controller.getGenreTable().findAll().stream().map(x->x.getType()).collect(Collectors.toList())));
        attori.getItems().addAll(FXCollections.observableArrayList(Controller.getActorTable().findAll()));
        regista.getItems().addAll(FXCollections.observableArrayList(Controller.getDirectorTable().findAll()));
        modProg.getItems().setAll(FXCollections.observableArrayList(Controller.getProgrammingModesTable().findAll().stream().map(x->x.getType()).collect(Collectors.toList())));
    }

    
    @FXML
    void setGenre(MouseEvent event) {
        
    }

}