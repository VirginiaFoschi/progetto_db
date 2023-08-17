package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.CineCard;
import model.FilmDetail;
import model.Seat;
import model.Showing;
import model.Ticket;
import utils.Triplets;

public class ShopController implements Initializable {

    @FXML
    private TableColumn<CineCard,Integer> avilableEntrances;

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
    private TableColumn<CineCard, Date> dateCinecard;

    @FXML
    private TableColumn<Ticket,String> fila;

    @FXML
    private TextField filmID;

    @FXML
    private ComboBox<String> modProg;

    @FXML
    private TableColumn<Ticket,String> ora;

    @FXML
    private TableColumn<Ticket,Integer> posto;

    @FXML
    private TableColumn<Ticket,String> prezzo;

    @FXML
    private TableColumn<CineCard,String> priceCinecard;

    @FXML
    private TableColumn<Ticket,Integer> sala;

    @FXML
    private ComboBox<Seat> seat;

    @FXML
    private Button showCinecard;

    @FXML
    private Button showPrices;

    @FXML
    private Button showTickets;

    @FXML
    private TableView<Ticket> table1;

    @FXML
    private TableView<CineCard> table2;

    @FXML
    private ComboBox<String> time;

    @FXML
    private TableColumn<CineCard, Boolean> validity;

    @FXML
    private TableColumn<CineCard,Integer> totalEntrances;

    @FXML
    private Button buyCinecard;

    @FXML
    private TextField cf4;

    @FXML
    private ComboBox<Integer> entrances;

    /*private void clear() {
        cf.clear();
        cf2.clear();
        cf3.clear();
        cf4.clear();
        filmID.clear();
    }*/

    @FXML
    void buyTicket(ActionEvent event) {
        String filmid = filmID.getText();
        String client = cf.getText();
        Date dataShow = date.getSelectionModel().getSelectedItem();
        String timeShow = time.getSelectionModel().getSelectedItem();
        Seat posto = seat.getSelectionModel().getSelectedItem();
        Boolean cineCard = cinecard.getSelectionModel().getSelectedItem();
        String modprog = modProg.getSelectionModel().getSelectedItem();
        if(!filmid.isEmpty() && !client.isEmpty() && posto != null && cineCard != null) {
            Ticket ticket = new Ticket(dataShow, timeShow, posto.getTheater(), posto.getLine(), posto.getNumber(), new Date(), cineCard, client, Integer.parseInt(filmid), modprog);
            Controller.getTicketTable().save(ticket);
            table1.setItems(FXCollections.observableArrayList(Controller.getTicketTable().findAll()));
            Optional<Showing> show = Controller.getShowingTable().findByPrimaryKey(new Triplets<Date,String,FilmDetail>(dataShow, timeShow, new FilmDetail(modprog, Integer.parseInt(filmid))));
            if(show.isPresent()) {
                Controller.getShowingTable().update(new Showing(dataShow, timeShow, show.get().getNumberSpectator() +1, show.get().getTheaterID(), Integer.parseInt(filmid), modprog));
            }
            if(cineCard) {
                Optional<CineCard> card = Controller.getCinecardTable().getNewer(client);
                if(card.isPresent()) {
                    Controller.getCinecardTable().update(new CineCard(client, card.get().getDataAcquisto(), card.get().getIngressiDisponibili()-1, card.get().getIngressiTotali()));
                }
            }
            //clear();
        } else {
            Controller.allert();
        }
    }

    @FXML
    void buyCinecard(ActionEvent event) {
        String cF = cf4.getText();
        Integer num = entrances.getSelectionModel().getSelectedItem();
        if(!cF.isEmpty() && num!=null) {
            if(Controller.getClientTable().findByPrimaryKey(cF).isPresent()) {
                if(Controller.getCinecardTable().hasCinecardValid(cF).isPresent()) {
                    Controller.allertNotExist("Non \u00E8 possibile acquistare una nuova cineCard perchè il cliente ne possiede gi\u00E0 una valida");
                }
                else {
                    CineCard card = new CineCard(cF, new Date(), num, num);
                    Controller.getCinecardTable().save(card);
                }
            } else {
                Controller.allertNotExist("non esiste un cliente con quel codice fiscale");
            }
            //clear();
        } else {
            Controller.allert();
        }
    }

    @FXML
    void showCineCard(ActionEvent event) {
        String client = cf3.getText();
        if(!client.isEmpty()) {
            if(Controller.getClientTable().findByPrimaryKey(client).isPresent()) {
                table2.setItems(FXCollections.observableArrayList(Controller.getCinecardTable().getCinecardFromClient(client)));
            } else {
                Controller.allertNotExist("non esiste un cliente con quel codice fiscale");
            }
            //clear();
        } else {
            Controller.allertNotExist("inserisci prima il codice fiscale del cliente");
        }
    }

    @FXML
    void showPrices(ActionEvent event) throws IOException {
        Controller.showRates();
    }

    @FXML
    void showTickets(ActionEvent event) {
        String client = cf2.getText();
        if(!client.isEmpty()) {
            table1.setItems(FXCollections.observableArrayList(Controller.getTicketTable().getClientTickets(client)));
            //clear();
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
                date.setItems(FXCollections.observableArrayList(Controller.getShowingTable().getFilmDates(id)));
            } else {
                Controller.allertNotExist("non esiste un film con quel codice");
            }
        } else {
            Controller.allertNotExist("inserisci prima il codice del film");
        }
    }

    @FXML
    void updateModProg(MouseEvent event) {
        String film = filmID.getText();
        if(!film.isEmpty()) {
            int id = Integer.parseInt(film);
            if(Controller.getFilmsTable().findByPrimaryKey(id).isPresent()) {
                modProg.setItems(FXCollections.observableArrayList(Controller.getFilmDetailTable().getFilmModes(id)));
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
            List<Seat> seatAvailable = Controller.getSeatTable().showFreeSeats(dataShow, timeShow, Integer.parseInt(film));
            if(seatAvailable.isEmpty()) {
                Controller.allertNotExist("mi spiace non sono pi\u00F9 disponibili posti per questa proiezione");
            } else {
                seat.setItems(FXCollections.observableArrayList(seatAvailable));
            }
        } else {
            Controller.allertNotExist("Inserisci prima data e ora della proiezione");
        }
    }

    @FXML
    void hasCinecard(MouseEvent event) {
        String client = cf.getText();
        if(!client.isEmpty()) {
            if(Controller.getClientTable().findByPrimaryKey(client).isPresent()) {
                if(Controller.getCinecardTable().hasCinecardValid(client).isPresent()) {
                    cinecard.setItems(FXCollections.observableArrayList(true,false));
                } else {
                    cinecard.setItems(FXCollections.observableArrayList(false));
                }
            } else {
                Controller.allertNotExist("non esiste un cliente con quel codice fiscale");
            }
        } else {
            Controller.allertNotExist("inserisci prima il codice fiscale del cliente");
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
        cf_column.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        data.setCellValueFactory(new PropertyValueFactory<>("dateShow"));
        ora.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        sala.setCellValueFactory(new PropertyValueFactory<>("salaID"));
        fila.setCellValueFactory(new PropertyValueFactory<>("letterLine"));
        posto.setCellValueFactory(new PropertyValueFactory<>("numberSeat"));
        dataAcquisto.setCellValueFactory((new PropertyValueFactory<>("purchaseDate")));
        cinecard_column.setCellValueFactory(new PropertyValueFactory<>("cineCard"));
        prezzo.setCellValueFactory(x->new SimpleObjectProperty<String>(!x.getValue().isCineCard() ? String.valueOf(Controller.getAgeRangeTable().getPriceFromEta(x.getValue().getClientID(),x.getValue().getTypeFilm(), x.getValue().getPurchaseDate())).concat("0 \u20AC") : ""));

        table1.setItems(FXCollections.observableArrayList(Controller.getTicketTable().findAll()));

        totalEntrances.setCellValueFactory(new PropertyValueFactory<>("ingressiTotali"));
        priceCinecard.setCellValueFactory(x-> new SimpleObjectProperty<>(Controller.getCinecardTypeTable().findByPrimaryKey(x.getValue().getIngressiTotali()).map(y->String.valueOf(y.getPrice()).concat("0 \u20AC")).orElse(null)));
        validity.setCellValueFactory(new PropertyValueFactory<>("valid"));
        dateCinecard.setCellValueFactory(new PropertyValueFactory<>("dataAcquisto"));
        avilableEntrances.setCellValueFactory(new PropertyValueFactory<>("ingressiDisponibili"));

        entrances.setItems(FXCollections.observableArrayList(Controller.getCinecardTypeTable().findAll().stream().map(x->x.getEntrancesNumber()).toList()));

    }

}


