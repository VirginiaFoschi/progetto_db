package controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import app.Controller;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Actor;
import model.Cast;
import model.Director;
import model.Film;

public class CastController implements Initializable {

    @FXML
    private TableColumn<Cast,String> cognome;

    @FXML
    private ComboBox<String> director;

    @FXML
    private TextField filmID;

    @FXML
    private TableColumn<Cast,Integer> id;

    @FXML
    private Button insertActor;

    @FXML
    private Button insertDirector;

    @FXML
    private TableColumn<Cast,Boolean> isRegista;

    @FXML
    private TextField name;

    @FXML
    private TextField nationality;

    @FXML
    private TableColumn<Cast,String> nazionalita;

    @FXML
    private TableColumn<Cast,String> nome;

    @FXML
    private Button showActors;

    @FXML
    private Button showAll;

    @FXML
    private Button showDirector;

    @FXML
    private Button showInOrder;

    @FXML
    private Button showStrangerActors;

    @FXML
    private TextField surname;

    @FXML
    private TableView<Cast> table;

    @FXML
    void fill(MouseEvent event) {
        List<String> directors = Controller.getDirectorTable().findAll().stream().map(x->x.getId()+ " : " + x.getNome() + " " + x.getCognome()).collect(Collectors.toList());
        director.setItems(FXCollections.observableArrayList(directors));
    }

    @FXML
    void insertActor(ActionEvent event) {
        insert(false);
    }

    @FXML
    void insertDirector(ActionEvent event) {
        insert(true);
    }

    private void insert(boolean isRegista) {
        String castName = name.getText();
        String castSurname = surname.getText();
        String castNationality = nationality.getText();
        if(!castName.isEmpty() && !castSurname.isEmpty() && !castNationality.isEmpty()) {
            if(isRegista) {
                Director director = new Director(castName,castSurname,castNationality);
                Controller.getDirectorTable().save(director);
            } else {
                Actor actor = new Actor(castName,castSurname,castNationality);
                Controller.getActorTable().save(actor);
            }
            update();
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showActors(ActionEvent event) {
        String film = filmID.getText();
        if(!film.isEmpty()) {
            List<Cast> list = Controller.getParticipationTable().getActorFromFilm(Integer.parseInt(film))
                                        .stream()
                                        .map(x->Controller.getActorTable().findByPrimaryKey(x).orElse(null))
                                        .filter(x->x!=null)
                                        .collect(Collectors.toList());
            table.setItems(FXCollections.observableArrayList(list));
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showAll(ActionEvent event) {
        update();
    }

    @FXML
    void showDirector(ActionEvent event) {
        String id = filmID.getText();
        if(!id.isEmpty()) {
            Optional<Film> film =Controller.getFilmsTable().findByPrimaryKey(Integer.parseInt(id));
            if(film.isPresent()) {
                table.setItems(FXCollections.observableArrayList(Controller.getDirectorTable().findByPrimaryKey(film.get().getCodiceRegista()).orElse(null)));
            } else {
                Controller.allertNotExist("Non esiste un film con quel codice");
            }
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showInOrder(ActionEvent event) {
        table.setItems(FXCollections.observableArrayList(Controller.getParticipationTable().getActorInOrder()));
    }

    @FXML
    void showStragerActors(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        id.setCellValueFactory(new PropertyValueFactory<Cast, Integer>("id"));
        nome.setCellValueFactory(new PropertyValueFactory<Cast, String>("nome"));
        cognome.setCellValueFactory(new PropertyValueFactory<Cast,String>("cognome"));
        nazionalita.setCellValueFactory(new PropertyValueFactory<Cast,String>("nazionalita"));
        isRegista.setCellValueFactory(new PropertyValueFactory<Cast,Boolean>("regista"));

        update();
    }

    private void update(){
        List<Cast> list = Controller.getActorTable().findAll();
        list.addAll(Controller.getDirectorTable().findAll());
        table.setItems(FXCollections.observableArrayList(list));
    }

}
