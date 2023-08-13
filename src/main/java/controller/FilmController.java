package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import app.Controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Film;
import model.FilmExtension;
import utils.Utils;

public class FilmController implements Initializable {

    @FXML
    private TextField actorID;

    @FXML
    private TableColumn<FilmExtension, Integer> anno;

    @FXML
    private TableColumn<FilmExtension, Date> dataFine;

    @FXML
    private TableColumn<FilmExtension, Date> dataInizio;

    @FXML
    private DatePicker date;

    @FXML
    private Button deleteFilm;

    @FXML
    private TableColumn<FilmExtension, Integer> durata;

    @FXML
    private Button film2Dand3D;

    @FXML
    private TextField filmID;

    @FXML
    private TableColumn<FilmExtension, String> genere;

    @FXML
    private TableColumn<FilmExtension, Integer> id;

    @FXML
    private Button insertFilm;

    @FXML
    private Button searchFilm;

    @FXML
    private Button showActorFilm;

    @FXML
    private Button showAll;

    @FXML
    private Button showFilm;

    @FXML
    private TableView<FilmExtension> table;

    @FXML
    private TableColumn<FilmExtension, String> titolo;

    @FXML
    private TableColumn<FilmExtension, String> trama;

    @FXML
    private ComboBox<String> genre;

    @FXML
    private Button showGenreFilm;

    @FXML
    void showGenreFilm(ActionEvent event) {
        String g = genre.getSelectionModel().getSelectedItem();
        if(g!= null) {
            updateTable(Controller.getFilmsTable().getFilmsFromGenre(g));
        } else {
            Controller.allert();
        }
    }

    @FXML
    void deleteFilm(ActionEvent event) {
        String film = filmID.getText();
        if(film.length()!=0) {
            Optional<Film> f = Controller.getFilmsTable().findByPrimaryKey(Integer.parseInt(film));
            if(f.isPresent()) {
                Controller.getCorrispondenceTable().deleteFilm(f.get());
                Controller.getParticipationTable().deleteFilm(f.get());
                Controller.getFilmDetailTable().deleteFilm(f.get());
                Controller.getFilmsTable().delete(f.get().getId());
                updateTable(Controller.getFilmsTable().findAll());
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
        }
    }

    @FXML
    void filmInAllModes(ActionEvent event) {
        List<Film> films = Controller.getFilmsTable().fromIDtoFilm(Controller.getFilmDetailTable().getFilmsInAllModes(Controller.getProgrammingModesTable().getNumberOfProgrammingModes()));
        updateTable(films);
    }

    @FXML
    void insertFilm(ActionEvent event) throws IOException {
        Controller.insertFilm();
    }

    @FXML
    void searchFilm(ActionEvent event) {
        String id = filmID.getText();
        if(!id.isEmpty()) {
            Optional<Film> film = Controller.getFilmsTable().findByPrimaryKey(Integer.parseInt(id));
            updateTable(film.isPresent() ? List.of(film.get()) : new ArrayList<>());
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showActorFilm(ActionEvent event) {
        String actor = actorID.getText();
        if(!actor.isEmpty()) {
            List<Film> films = Controller.getFilmsTable().fromIDtoFilm(Controller.getParticipationTable().getFilmsFromActor(Integer.parseInt(actor)));
            updateTable(films);
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showAll(ActionEvent event) {
        updateTable(Controller.getFilmsTable().findAll());
    }

    @FXML
    void showFilm(ActionEvent event) {
        LocalDate data = date.getValue();
        if(data != null) {
            updateTable(Controller.getFilmsTable().getFilmsOnDate(Utils.localDateToDate(data)));
        } else {
            Controller.allert();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<FilmExtension,Integer>("id"));
        titolo.setCellValueFactory(new PropertyValueFactory<FilmExtension,String>("title"));
        durata.setCellValueFactory(new PropertyValueFactory<FilmExtension,Integer>("duration"));
        anno.setCellValueFactory(new PropertyValueFactory<FilmExtension,Integer>("year"));
        trama.setCellValueFactory(x->new SimpleObjectProperty<String>(x.getValue().getPlot().orElse(null)));
        dataInizio.setCellValueFactory(x->new SimpleObjectProperty<Date>(x.getValue().getPeriod().getStartDate()));
        dataFine.setCellValueFactory(x->new SimpleObjectProperty<Date>(x.getValue().getPeriod().getEndDate()));
        genere.setCellValueFactory(new PropertyValueFactory<FilmExtension,String>("genres"));

        updateTable(Controller.getFilmsTable().findAll());

        genre.setItems(FXCollections.observableArrayList(Controller.getGenreTable().findAll().stream().map(x->x.getType()).toList()));
    }

    public void updateTable(final List<Film> films) {
        table.setItems(FXCollections.observableArrayList(films.stream()
                                                            .map(x->new FilmExtension(x.getId(), x.getTitle(), x.getDuration(), x.getYear(), x.getPlot(), x.getPeriod(), x.getCodiceRegista(), Controller.getCorrispondenceTable().getFilmGenre(x.getId())))
                                                            .collect(Collectors.toList())));
    }

}