package controller;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.Controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AgeRange;
import model.CinecardType;
import model.Rate;

public class PriceController implements Initializable {

    @FXML
    private TableColumn<Rate,String> category;

    @FXML
    private TableView<CinecardType> cineCards;

    @FXML
    private TableColumn<CinecardType,String> monthsValidity;

    @FXML
    private TableColumn<CinecardType,Integer> numberEntrances;

    @FXML
    private TableColumn<CinecardType,String> priceCinecard;

    @FXML
    private TableColumn<Rate,String> priceTicket;

    @FXML
    private TableView<Rate> prices;

    @FXML
    private TableColumn<Rate,String> years;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        numberEntrances.setCellValueFactory(new PropertyValueFactory<>("entrancesNumber"));
        priceCinecard.setCellValueFactory(x-> new SimpleObjectProperty<>(String.valueOf(x.getValue().getPrice()).concat("0 €")));
        monthsValidity.setCellValueFactory(new PropertyValueFactory<>("validityMonths"));

        cineCards.setItems(FXCollections.observableArrayList(Controller.getCinecardTypeTable().findAll()));

        category.setCellValueFactory(x-> new SimpleObjectProperty<>(x.getValue().getCategoria().getName().concat(" "+ x.getValue().getTipo())));
        priceTicket.setCellValueFactory(x-> new SimpleObjectProperty<>(String.valueOf(x.getValue().getPrezzo()).concat("0 €")));
        years.setCellValueFactory(x->new SimpleObjectProperty<String>(getEta(Controller.getAgeRangeTable().getFromCategory(x.getValue().getCategoria()))));

        prices.setItems(FXCollections.observableArrayList(Controller.getRateTable().findAll()));
    }

    private String getEta(final List<AgeRange> range) {
        return range.stream().map(x->x.getEtaMin() + "-" + x.getEtaMax() + " anni\n").reduce((x,y)->x+y).get();
        
    }
}
