package de.ddkfm.binding;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Person {
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty age = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<LocalDate>();
    private DoubleProperty income = new SimpleDoubleProperty();

    public Person(String name, LocalDate birthday, double income) {
        this.name.set(name);
        this.birthday.addListener(((observable, oldValue, newValue) -> {
            int year = newValue.getYear();
            int age = LocalDate.now().getYear() - year;
            this.age.set(age);
        }));
        this.birthday.set(birthday);
        this.income.set(income);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public double getIncome() {
        return income.get();
    }

    public DoubleProperty incomeProperty() {
        return income;
    }

    public void setIncome(double income) {
        this.income.set(income);
    }
}
