package de.ddkfm.binding;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

public class PersonPane extends GridPane{
    private Person person;

    private TextField fieldName = new TextField();
    private DatePicker dpBirthday = new DatePicker();
    private TextField fieldAge = new TextField();
    private CheckBox cbIs90Kid = new CheckBox();
    private TextField fieldIncome = new TextField();
    public PersonPane(Person person) {
        this.person = person;

        fieldName.textProperty().bindBidirectional(this.person.nameProperty());
        this.add(new Label("Name: "), 0, 0);
        this.add(fieldName, 1, 0);

        dpBirthday.valueProperty().bindBidirectional(this.person.birthdayProperty());
        this.add(new Label("Geburtstag"), 0, 1);
        this.add(dpBirthday, 1, 1);

        fieldAge.textProperty().bind(this.person.ageProperty().asString());
        this.add(new Label("Alter: "), 0, 2);
        this.add(fieldAge, 1, 2);

        //bind a BooleanProperty to the age-Property of the person model
        cbIs90Kid.selectedProperty().bind(
                this.person.ageProperty().greaterThan(16)
                        .and(
                this.person.ageProperty().lessThan(28))
        );

        this.add(new Label("Kind der 90er"), 0, 3, 3, 1);
        this.add(cbIs90Kid, 1, 3);

        //Doing some JVM-Magic by binding a StringProperty to a DoubleProperty via a corresponding Converter-Class
        Bindings.bindBidirectional(fieldIncome.textProperty(), this.person.incomeProperty(), new NumberStringConverter());

        this.add(new Label("Einkommen: "), 0, 4, 3, 1);
        this.add(fieldIncome, 1, 4);


        Button button = new Button("Person anzeigen");
        button.setOnAction((event) -> this.showPerson());
        this.add(button,0, 5, 2, 2);

        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(20));
    }

    public void showPerson() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Person: " + this.person.getName());
        String content = "Geburtsdatum: " + this.person.getBirthday() + "\n";
        content += "Alter: " + this.person.getAge() + "\n";
        content += "Einkommen: " + this.person.getIncome();
        alert.setContentText(content);
        alert.showAndWait();
    }
}
