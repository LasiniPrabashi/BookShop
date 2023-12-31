package lk.ijse.BookShop.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lk.ijse.BookShop.db.DBConnection;
import lk.ijse.BookShop.dto.Customer;
import lk.ijse.BookShop.dto.Item;
import lk.ijse.BookShop.dto.Order;
import lk.ijse.BookShop.model.CustomerModel;
import lk.ijse.BookShop.model.ItemModel;
import lk.ijse.BookShop.model.OrderModel;
import lk.ijse.BookShop.tm.CustomerTm;
import lk.ijse.BookShop.tm.ItemTm;
import lk.ijse.BookShop.tm.OrderTm;
import lk.ijse.BookShop.util.Navigation;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {
    private static OrderFormController controller;
    public Pane pane;
    public JFXTextField txtSearch;
    public TableView tbl;
    public TableColumn tblid;
    public TableColumn tblCust_id;
    public TableColumn tblDate;
    public TableColumn tblTime;
    public TableColumn tblGrandTotal;
    public TableColumn tblDelete;
    public TableColumn tblView;


public OrderFormController(){
    controller = this;
}
    public static OrderFormController getInstance() {
        return controller;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableProperty();
        loadIds();
    }

    public void loadIds() {
        try {
            ArrayList<String> allId = OrderModel.getAllId();
            for (int i = 0; i < allId.size(); i++) {
                loadData(allId.get(i));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadData(String id) {
        try {
            OrderTm tm = OrderModel.get(id);
            list.add(tm);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    ObservableList<OrderTm> list = FXCollections.observableArrayList();

    private void setTableProperty() {
        tblid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCust_id.setCellValueFactory(new PropertyValueFactory<>("cus_id"));
        tblDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tblTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tblGrandTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tblView.setCellValueFactory(new PropertyValueFactory<>("view"));
        tbl.setItems(list);
    }

    public void btnNewOderOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("NewOrderForm.fxml");

    }

    public void txtsearchKeyReleased(KeyEvent keyEvent) {
        tbl.getItems().clear();
        list.clear();
        try {
            ArrayList<String> ids= OrderModel.getSearchIds(txtSearch.getText());
            for (int i = 0; i < ids.size(); i++) {
                setOrderData(ids.get(i));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
}

    private void setOrderData(String id) {

    }


    public void btnNewOderReportOnAction(ActionEvent actionEvent) {

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\hp\\Desktop\\BookShop\\src\\main\\resources\\report\\CustomerOrder.jrxml");
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(Arrays.asList(new Object()));

            HashMap hm = new HashMap();


            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, connection);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public void loadDataTable() {
        list.clear();
        tbl.getItems().clear();
        loadIds();
    }

}
