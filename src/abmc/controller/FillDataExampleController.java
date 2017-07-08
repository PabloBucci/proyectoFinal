/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmc.controller;

import abmc.dao.PersonaDao;
import abmc.model.Persona;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FillDataExampleController implements Initializable {

    @FXML
    private TextField lbl_name_value;
    @FXML
    private TextField lbl_lastname_value;
    @FXML
    private TextField lbl_address_value;
    @FXML
    private TextField lbl_phone_value;
    @FXML
    private TextField lbl_cell_value;
    @FXML
    private TextField lbl_email_value;
    @FXML
    private Button btn_nuevo;
    @FXML
    private Button btn_editar;
    @FXML
    private Button btn_grabar;
    @FXML
    private Button btn_borrar;
    @FXML
    private Button btn_exit;
    @FXML
    private TableView<Persona> tbl_personas;
    @FXML
    private TableColumn<Persona, String> col_nombre;
    @FXML
    private TableColumn<Persona, String> col_apellido;

    private PersonaDao personaDao;
    
    final ObservableList<Persona> data = FXCollections.observableArrayList(
    );

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            personaDao = new PersonaDao();
            
            // Initialize the person table with the two columns.
            col_nombre.setCellValueFactory(cellData -> cellData.getValue().nombre());
            col_apellido.setCellValueFactory(cellData -> cellData.getValue().apellido());
            
            // Listener para detectar el cambio de seleccion
            tbl_personas.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> cargarPersonaSeleccionada(newValue));
            
            tbl_personas.setItems(data);
            llenarTablaDePersonas();
        } catch (SQLException ex) {
            Logger.getLogger(FillDataExampleController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void llenarTablaDePersonas() throws SQLException {
        personaDao.getAll().forEach(p -> tbl_personas.getItems().add(p));
    }

    @FXML
    private void personaNuevo(ActionEvent event) {
        cargarPersonaSeleccionada(null);
    }
    
    @FXML
    private void grabarPersona(ActionEvent event) throws SQLException {
        Persona  p = new Persona(UUID.randomUUID().hashCode(), lbl_name_value.getText(), lbl_lastname_value.getText(), lbl_address_value.getText(),
        lbl_phone_value.getText(), lbl_cell_value.getText(), lbl_email_value.getText());
        personaDao.add(p);
        tbl_personas.getItems().add(p);
    }

    @FXML
    private void editarPersona(ActionEvent event) {
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void eliminarPersona() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmacion");
        alert.setHeaderText("Esta a punto de eliminar el Auto...");
        alert.setContentText("desea continuar?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Persona p = tbl_personas.getSelectionModel().getSelectedItem();

            try {

                int selectedIndex = tbl_personas.getSelectionModel().getSelectedIndex();
                personaDao.delete(p);
                tbl_personas.getItems().remove(selectedIndex);
            } catch (Exception ex) {
                // Muestra el mensaje de error
            }

        }
    }

    private void cargarPersonaSeleccionada(Persona p) {
        if (p != null) {
            lbl_name_value.setText(p.getNombre());
            lbl_lastname_value.setText(p.getApellido());
            lbl_address_value.setText(p.getDireccion());
            lbl_phone_value.setText(p.getTelefono());
            lbl_cell_value.setText(p.getCelular());
            lbl_email_value.setText(p.getEmail());
        } else {
            lbl_name_value.setText("");
            lbl_lastname_value.setText("");
            lbl_address_value.setText("");
            lbl_phone_value.setText("");
            lbl_cell_value.setText("");
            lbl_email_value.setText("");
        }
        btn_editar.setDisable(false);
        btn_borrar.setDisable(false);
    }

    @FXML
    private void handlerBtnExit(ActionEvent event) {
        Platform.exit();
    }

}
