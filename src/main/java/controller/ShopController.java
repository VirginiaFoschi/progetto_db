package controller;

import java.net.URL;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.CinecardType;
import model.Seat;
import model.Ticket;

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
    private TableColumn<Ticket, String> cf_column;

    @FXML
    private ComboBox<Boolean> cinecard;

    @FXML
    private TableColumn<Ticket, Boolean> cinecard_column;

    @FXML
    private TableColumn<Ticket, Date> data;

    @FXML
    private TableColumn<Ticket,Date> dataAcquisto;

    @FXML
    private ComboBox<Date> date;

    @FXML
    private TableColumn<Ticket,String> fila;

    @FXML
    private TextField filmID;

    @FXML
    private TableColumn<CinecardType,Integer> months;

    @FXML
    private ComboBox<Integer> numberEntrances;

    @FXML
    private TableColumn<Ticket,String> ora;

    @FXML
    private TableColumn<Ticket,Integer> posto;

    @FXML
    private TableColumn<Ticket,Double> prezzo;

    @FXML
    private TableColumn<CinecardType,String> price;

    @FXML
    private TableColumn<Ticket,Integer> sala;

    @FXML
    private ComboBox<Seat> seat;

    @FXML
    private Button showTickets;

    @FXML
    private TableView<Ticket> table1;

    @FXML
    private TableView<CinecardType> table2;

    @FXML
    private ComboBox<String> time;

    @FXML
    private TableColumn<CinecardType,Integer> totalEntrances;

    @FXML
    void buyCineCard(ActionEvent event) {

    }

    @FXML
    void buyTicket(ActionEvent event) {
        String client = cf.getText();
        Date dataShow = date.getSelectionModel().getSelectedItem();
        String timeShow = time.getSelectionModel().getSelectedItem();
        Seat posto = seat.getSelectionModel().getSelectedItem();
        Boolean cineCard = cinecard.getSelectionModel().getSelectedItem();
        if(!client.isEmpty() && posto != null && cineCard != null) {
            Ticket ticket = new Ticket(dataShow, timeShow, posto.getTheater(), posto.getLine(), posto.getNumber(), cineCard, client);
            Controller.getTicketTable().save(ticket);
            table1.setItems(FXCollections.observableArrayList(Controller.getTicketTable().findAll()));
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showTickets(ActionEvent event) {
        String client = cf2.getText();
        if(!client.isEmpty()) {
            table1.setItems(FXCollections.observableArrayList(Controller.getTicketTable().getClientTickets(client)));
        } else {
            Controller.allert();
        }
    }

    @FXML
    void updateDate(MouseEvent event) {
        String film = filmID.getText();
        if(!film.isEmpty()) {
            int id = Integer.parseInt(film);
            if(Controller.getFilmsTable().findByPrimaryKey(id).isPresent()) {
                date.setItems(FXCollections.observableArrayList(Controller.getShowingTable().getFilmShows(id).stream().map(x->x.getData()).collect(Collectors.toList())));
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
        } else {
            Controller.allertNotExist("inserisci prima il codice del film");
        }
    }

    @FXML
    void updateSeats(MouseEvent event) {
        String film = filmID.getText();
        Date dataShow = date.getSelectionModel().getSelectedItem();
        String timeShow = time.getSelectionModel().getSelectedItem();
        if(!film.isEmpty() && dataShow!=null && timeShow != null) {
            List<Seat> seatAvailable = Controller.getSeatTable().showFreeSeats(dataShow, timeShow);
            if(seatAvailable.isEmpty()) {
                Controller.allertNotExist("mi spiace non sono più disponibili posti per questa proiezione");
            } else {
                seat.setItems(FXCollections.observableArrayList(seatAvailable));
            }
        } else {
            Controller.allert();
        }
    }

    @FXML
    void updateTime(MouseEvent event) {
        String film = filmID.getText();
        Date dataShow = date.getSelectionModel().getSelectedItem();
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
        cf_column.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        data.setCellValueFactory(new PropertyValueFactory<>("dateShow"));
        ora.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        sala.setCellValueFactory(new PropertyValueFactory<>("salaID"));
        fila.setCellValueFactory(new PropertyValueFactory<>("letterLine"));
        posto.setCellValueFactory(new PropertyValueFactory<>("numberSeat"));
        dataAcquisto.setCellValueFactory((new PropertyValueFactory<>("purchaseDate")));
        cinecard_column.setCellValueFactory(new PropertyValueFactory<>("cineCard"));

        table1.setItems(FXCollections.observableArrayList(Controller.getTicketTable().findAll()));

        totalEntrances.setCellValueFactory(new PropertyValueFactory<>("entrancesNumber"));
        price.setCellValueFactory(x-> new SimpleObjectProperty<>(String.valueOf(x.getValue().getPrice()).concat("0 €")));
        months.setCellValueFactory(new PropertyValueFactory<>("validityMonths"));

        table2.setItems(FXCollections.observableArrayList(Controller.getCinecardTypeTable().findAll()));

        cinecard.setItems(FXCollections.observableArrayList(true,false));
        numberEntrances.setItems(FXCollections.observableArrayList(Controller.getCinecardTypeTable().findAll().stream().map(x->x.getEntrancesNumber()).collect(Collectors.toList())));
    }

}

