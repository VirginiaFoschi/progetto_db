package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.input.MouseEvent;
import model.Film;
import model.Seat;
import model.Showing;
import utils.Utils;

public class ShowingController implements Initializable {

    @FXML
    private TableColumn<Showing,Integer> allSeats;

    @FXML
    private TableColumn<Showing,Integer> booked;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<Date> date3;

    @FXML
    private TableColumn<Showing,Date> date_column;

    @FXML
    private TextField filmID;

    @FXML
    private TextField filmID2;

    @FXML
    private TextField filmID3;

    @FXML
    private TableColumn<Showing,Integer> film_column;

    @FXML
    private Button insertShow;

    @FXML
    private TableColumn<Seat,String> line_column;

    @FXML
    private TableColumn<Seat,Integer> seat_column;

    @FXML
    private Button show;

    @FXML
    private Button showAll;

    @FXML
    private Button showSeats;

    @FXML
    private TextField startTime;

    @FXML
    private TableView<Showing> table1;

    @FXML
    private TableView<Seat> table2;

    @FXML
    private ComboBox<Integer> theaters;

    @FXML
    private TableColumn<Showing,Integer> theater_column;

    @FXML
    private TableColumn<Seat,Integer> theater_column2;

    @FXML
    private ComboBox<String> time;

    @FXML
    private ComboBox<String> modProg;

    @FXML
    private TableColumn<Showing,String> time_column;

    /*private void clear() {
        date.getEditor().clear();
        date3.getItems().clear();
        filmID.clear();
        filmID2.clear();
        filmID3.clear();
        startTime.clear();
        theaters.getItems().clear();
        time.getItems().clear();
        modProg.getItems().clear();
    }*/

    /*@FXML
    void insertShow(ActionEvent event) {
        String id = filmID.getText();
        LocalDate data = date.getValue();
        String ora = startTime.getText();
        Integer codiceSala = theaters.getSelectionModel().getSelectedItem();
        String programmingMode = modProg.getSelectionModel().getSelectedItem();
        if(!id.isEmpty() && data!= null && !ora.isEmpty() && codiceSala != null && programmingMode !=null) {
            int film = Integer.parseInt(id);
            if(check(film,data) && isTheaterEmpty(codiceSala,data,ora,Controller.getFilmsTable().findByPrimaryKey(film).get().getDuration()) 
            && hasSelectedMode(programmingMode,film) && areNotOtherShowings(film,data,ora) && salaIsCorrect(programmingMode,codiceSala)) {
                Showing showing = new Showing(Utils.localDateToDate(data), ora,0, codiceSala,Integer.parseInt(id),programmingMode);
                Controller.getShowingTable().save(showing);
                table1.setItems(FXCollections.observableArrayList(Controller.getShowingTable().findAll()));
            }
        } else {
            Controller.allert();
        }
    } */

    @FXML
    void insertShow(ActionEvent event) {
        String id = filmID.getText();
        LocalDate data = date.getValue();
        String ora = startTime.getText();
        Integer codiceSala = theaters.getSelectionModel().getSelectedItem();
        String programmingMode = modProg.getSelectionModel().getSelectedItem();
        if(!id.isEmpty() && data!= null && !ora.isEmpty() && codiceSala != null && programmingMode !=null) {
            Optional<Film> film =Controller.getFilmsTable().findByPrimaryKey(Integer.parseInt(id));
            if(film.isPresent()) {
                if(check(film.get(),data) && isTheaterEmpty(codiceSala,data,ora,film.get().getDuration()) 
            && hasSelectedMode(programmingMode,film.get().getId()) && areNotOtherShowings(film.get(),data,ora) && salaIsCorrect(programmingMode,codiceSala)) {
                    Showing showing = new Showing(Utils.localDateToDate(data), ora,0, codiceSala,Integer.parseInt(id),programmingMode);
                    Controller.getShowingTable().save(showing);
                    table1.setItems(FXCollections.observableArrayList(Controller.getShowingTable().findAll()));
                }
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
            //clear();
        } else {
            Controller.allert();
        }
    }

    private boolean salaIsCorrect(String programmingMode, Integer codiceSala) {
        if(programmingMode.equals("2D")) {
            return true;
        } else {
            if(Controller.getTheatreTable().salaIs3D(codiceSala)) {
                return true;
            } else {
                Controller.allertNotExist("la sala selezionata non è adibita per la proiezione di film in 3D");
                return false;
            }
        }
    } 

    /*
    private boolean areNotOtherShowings(int film, LocalDate data, String ora) {
        if(Controller.getShowingTable().areOtherShowings(film,data,ora)) {
            Controller.allertNotExist("il film scelto è già proiettato in un'altra sala");
            return false;
        } else {
            return true;
        }
    } */

    
    private boolean areNotOtherShowings(Film film, LocalDate data, String ora) {
        if(Controller.getShowingTable().areOtherShowings(film,data,ora)) {
            Controller.allertNotExist("il film scelto è già proiettato in un'altra sala");
            return false;
        } else {
            return true;
        }
    }

    private boolean hasSelectedMode(String programmingMode, Integer id) {
        if(Controller.getFilmDetailTable().hasSelectedMode(id,programmingMode)) {
            return true;
        } else {
            Controller.allertNotExist("il film scelto non è disponibile nella modalità di proiezione selezionata");
            return false;
        }
    }

    private boolean isTheaterEmpty(Integer codiceSala, LocalDate data, String ora, int duration) {
        if(Controller.getShowingTable().isTheaterEmpty(codiceSala,data,ora,duration)) {
            return true;
        } else {
            Controller.allertNotExist("la sala selezionata è occupata");
            return false;
        }
    }

    /*private boolean check(int filmID, LocalDate data){
        Optional<Film> film =Controller.getFilmsTable().findByPrimaryKey(filmID);
        if(film.isPresent()) {
            if(Controller.getFilmsTable().checkPeriod(film.get().getId(),data)) {
                return true;
            } else {
                Controller.allertNotExist("la data non appartiene al periodo di proiezione del film scelto");
                return false;
            }
        } else {
            Controller.allertNotExist("non esiste un film con quel codice");
            return false;
        }
    } */

    private boolean check(Film film, LocalDate data){
        if(Controller.getFilmsTable().checkPeriod(film.getId(),data)) {
            return true;
        } else {
            Controller.allertNotExist("la data non appartiene al periodo di proiezione del film scelto");
            return false;
        }
    }

    @FXML
    void show(ActionEvent event) {
        String id = filmID2.getText();
        if(!id.isEmpty()) {
            Integer film = Integer.parseInt(id);
            if(Controller.getFilmsTable().findByPrimaryKey(film).isPresent()) {
                table1.setItems(FXCollections.observableArrayList(Controller.getShowingTable().getFilmShows(film)));
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showAll(ActionEvent event) {
        table1.setItems(FXCollections.observableArrayList(Controller.getShowingTable().findAll()));
    }

    @FXML
    void showSeats(ActionEvent event) {
        String id = filmID3.getText();
        Date data = date3.getSelectionModel().getSelectedItem();
        String ora = time.getSelectionModel().getSelectedItem();
        if(!id.isEmpty() && data != null && ora != null) {
            table2.setItems(FXCollections.observableArrayList(Controller.getSeatTable().showFreeSeats(data, ora,Integer.parseInt(id))));
        } else {
            Controller.allert();
        }
    }

    @FXML
    void updateDate(MouseEvent event) {
        String film = filmID3.getText();
        if(!film.isEmpty()) {
            int id = Integer.parseInt(film);
            if(Controller.getFilmsTable().findByPrimaryKey(id).isPresent()) {
                date3.setItems(FXCollections.observableArrayList(Controller.getShowingTable().getFilmDates(id)));
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
        } else {
            Controller.allertNotExist("inserisci prima il codice del film");
        }
    }

    @FXML
    void updateTime(MouseEvent event) {
        String film = filmID3.getText();
        Date dataShow = date3.getSelectionModel().getSelectedItem();
        if(!film.isEmpty() && dataShow != null) {
            int id = Integer.parseInt(film);
            if(Controller.getFilmsTable().findByPrimaryKey(id).isPresent()) {
                time.setItems(FXCollections.observableArrayList(Controller.getShowingTable().getFilmShowsOnDate(id,dataShow).stream().map(x->x.getStartTime()).collect(Collectors.toList())));
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
        } else {
            Controller.allertNotExist("inserisci prima il codice del film e la data");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        film_column.setCellValueFactory(new PropertyValueFactory<>("filmID"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("data"));
        time_column.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        theater_column.setCellValueFactory(new PropertyValueFactory<>("theaterID"));
        booked.setCellValueFactory(new PropertyValueFactory<>("numberSpectator"));
        allSeats.setCellValueFactory(x->new SimpleObjectProperty<Integer>(Controller.getTheatreTable().findByPrimaryKey(x.getValue().getTheaterID()).get().getCapacity()));
        
        table1.setItems(FXCollections.observableArrayList(Controller.getShowingTable().findAll()));

        theater_column2.setCellValueFactory(new PropertyValueFactory<>("theater"));
        line_column.setCellValueFactory(new PropertyValueFactory<>("line"));
        seat_column.setCellValueFactory(new PropertyValueFactory<>("number"));

        theaters.setItems(FXCollections.observableArrayList(Controller.getTheatreTable().findAll().stream().map(x->x.getId()).collect(Collectors.toList())));
        modProg.setItems(FXCollections.observableArrayList(Controller.getProgrammingModesTable().findAll().stream().map(x->x.getType()).collect(Collectors.toList())));
    }

}
