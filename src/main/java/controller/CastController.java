package controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Controller;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private CheckBox actorNo;

    @FXML
    private CheckBox actorYes;

    @FXML
    private CheckBox directorNo;

    @FXML
    private CheckBox directorYes;

    @FXML
    private TableColumn<Cast,String> cognome;

    @FXML
    private ComboBox<Cast> director;

    @FXML
    private TextField filmID;

    @FXML
    private TableColumn<Cast,Integer> id;

    @FXML
    private Button insert;

    @FXML
    private TableColumn<Cast,Boolean> isRegista;

    @FXML
    private TextField name;

    @FXML
    private ComboBox<String> nationality;

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
    void handleActorNo(ActionEvent event) {
        if(actorNo.isSelected()) {
            actorYes.setSelected(false);
        }
    }

    @FXML
    void handleActorYes(ActionEvent event) {
        if(actorYes.isSelected()) {
            actorNo.setSelected(false);
        }
    }

    @FXML
    void handleDirectorNo(ActionEvent event) {
        if(directorNo.isSelected()) {
            directorYes.setSelected(false);
        }
    }

    @FXML
    void handleDirectorYes(ActionEvent event) {
        if(directorYes.isSelected()) {
            directorNo.setSelected(false);
        }
    }

    @FXML
    void fill(MouseEvent event) {
        director.setItems(FXCollections.observableArrayList(Controller.getDirectorTable().findAll()));
    }

    @FXML
    void insert(ActionEvent event) {
        String castName = name.getText();
        String castSurname = surname.getText();
        String castNationality = nationality.getSelectionModel().getSelectedItem();
        if(!castName.isEmpty() && !castSurname.isEmpty() && castNationality != null
        && (actorYes.isSelected() || actorNo.isSelected()) && (directorNo.isSelected() || directorYes.isSelected())) {
            boolean isActor = actorYes.isSelected();
            boolean isDirector = directorYes.isSelected();
            if(isActor==false && isDirector==false) {
                Controller.allertNotExist("Non \u00E8 possibile effettuare l'inserimento perch\u00E8 non \u00E8 ne un attore ne un regista");
            } else {
                int num=getMax()+1;
                if(isActor && isDirector) {
                    Controller.getDirectorTable().save(new Director(num,castName,castSurname,castNationality));
                    Controller.getActorTable().save(new Actor(num,castName,castSurname,castNationality));
                } else if(isActor) {
                    Controller.getActorTable().save(new Actor(num,castName,castSurname,castNationality));
                } else {
                    Controller.getDirectorTable().save(new Director(num,castName,castSurname,castNationality));
                }
            }
            update();
        } else {
            Controller.allert();
        }
    }

    private int getMax() {
        int lastActor = Controller.getActorTable().getLastID();
        int lastDirector = Controller.getDirectorTable().getLastID();
        return Math.max(lastActor, lastDirector);
    }

    @FXML
    void showActors(ActionEvent event) {
        String film = filmID.getText();
        if(!film.isEmpty()) {
            table.setItems(FXCollections.observableArrayList(Controller.getActorTable().getActorFromFilm(Integer.parseInt(film))));
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
        Cast c = director.getSelectionModel().getSelectedItem();
        if(c != null) {
            table.setItems(FXCollections.observableArrayList(Controller.getActorTable().getStrangerActor(c.getId())));
        } else {
            Controller.allert();
        }
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

        nationality.setItems(FXCollections.observableArrayList(Controller.getNationalityTable().findAll().stream().map(x->x.getName()).toList()));
    }

    private void update(){
        List<Cast> list = Controller.getActorTable().findAll();
        list.addAll(Controller.getDirectorTable().findAll());
        table.setItems(FXCollections.observableArrayList(list));
    }

}
