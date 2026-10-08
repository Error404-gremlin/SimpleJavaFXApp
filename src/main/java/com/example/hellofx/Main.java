package com.example.hellofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");

        Label nameLabel = new Label("Customer name");
        nameLabel.setLabelFor(nameField);

        ComboBox<String> provinceBox = new ComboBox<>();

        provinceBox.getItems().addAll(
                "Central",
                "Lusaka",
                "Copperbelt"
        );

        provinceBox.setPromptText("Choose a province");

        Button saveButton = new Button("Save customer");
        Button deleteButton = new Button("Delete customer");

        Label status = new Label();

        // Create the table
        TableView<Customer> table = new TableView<>();
        table.setItems(customers);

        // Customer name column
        TableColumn<Customer, String> nameCol =
                new TableColumn<>("Customer name");

        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        table.getColumns().add(nameCol);

        // Province column
        TableColumn<Customer, String> provinceCol =
                new TableColumn<>("Province");

        provinceCol.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        table.getColumns().add(provinceCol);

        // Save button
        saveButton.setOnAction(event -> {

            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                status.setText("Enter the customer name.");
                nameField.requestFocus();
                return;
            }

            String province = provinceBox.getValue();

            if (province == null) {
                status.setText("Choose a province.");
                provinceBox.requestFocus();
                return;
            }

            customers.add(new Customer(name, province));

            status.setText("Customer saved.");

            nameField.clear();
            provinceBox.setValue(null);
        });

        // Delete button
        deleteButton.setOnAction(event -> {

            Customer selected =
                    table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                status.setText("Select a customer first.");
                return;
            }

            ButtonType delete =
                    new ButtonType("Delete");

            Alert ask = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Delete the selected customer?",
                    delete,
                    ButtonType.CANCEL
            );

            ask.setHeaderText("Confirm deletion");

            if (ask.showAndWait().orElse(ButtonType.CANCEL)
                    == delete) {

                customers.remove(selected);
                status.setText("Customer deleted.");
            }
        });

        // Layout
        VBox root = new VBox(
                10,
                nameLabel,
                nameField,
                provinceBox,
                saveButton,
                deleteButton,
                status,
                table
        );

        saveButton.setDefaultButton(true);
        nameField.requestFocus();

        Scene scene = new Scene(root, 600, 500);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}