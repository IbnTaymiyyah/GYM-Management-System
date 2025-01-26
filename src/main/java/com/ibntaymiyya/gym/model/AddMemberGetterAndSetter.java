package com.ibntaymiyya.gym.model;

import io.github.palexdev.mfxresources.fonts.IconDescriptor;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AddMemberGetterAndSetter {
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty dateStart;
    private final StringProperty dateEnd ;
    private final StringProperty gender;
    private final StringProperty isOnHold;

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty age;
    private SimpleIntegerProperty weight;
    private SimpleIntegerProperty height;

    private SimpleIntegerProperty daysLeft;
    private SimpleIntegerProperty holdDays;
    private SimpleDoubleProperty pay;
    private SimpleDoubleProperty totalPayed;
    private static String icon;

    public AddMemberGetterAndSetter() {
        this.id = new SimpleIntegerProperty(1);
        this.age = new SimpleIntegerProperty(0);
        this.weight = new SimpleIntegerProperty(0);
        this.height = new SimpleIntegerProperty(0);
        this.daysLeft = new SimpleIntegerProperty(0);
        this.holdDays = new SimpleIntegerProperty(0);
        this.pay = new SimpleDoubleProperty(0.00);
        this.totalPayed = new SimpleDoubleProperty(0.00);
        this.icon = "";

        this.name = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("0");
        this.dateStart = new SimpleStringProperty("");
        this.dateEnd = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
        this.isOnHold = new SimpleStringProperty("");

    }


    /*      SETTER      */

    public void setId(int id) {
        this.id.set(id);
    }


    public void setAge(int age) {
        this.age.set(age);
    }

    public void setWeight(int weight) {
        this.weight.set(weight);
    }

    public void setHeight(int height) {
        this.height.set(height);
    }
    public void setDaysLeft(int daysLeft) {
        this.daysLeft.set(daysLeft);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPay(double pay) {
        this.pay.set(pay);
    }
    public void setTotalPayed(double totalPayed) {
        this.totalPayed.set(totalPayed);
    }
    public void setHoldDays(int holdDays) {
        this.holdDays.set(holdDays);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }


    public void setName(String name) {this.name.set(name);}
    public void setIsOnHold(String isOnHold) {this.isOnHold.set(isOnHold);}

    public void setDateStart(String dateStart) {
        this.dateStart.set(dateStart);
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd.set(dateEnd);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    /*      GETTER      */


    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public int getAge() {
        return age.get();
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public int getWeight() {
        return weight.get();
    }

    public SimpleIntegerProperty weightProperty() {
        return weight;
    }

    public int getHeight() {
        return height.get();
    }
    public int getDaysLeft() {
        return daysLeft.get();
    }

    public String getIcon() {
        return icon;
    }

    public SimpleIntegerProperty heightProperty() {
        return height;
    }
    public SimpleIntegerProperty daysLeftProperty() {
        return daysLeft;
    }

    public double getPay() {
        return pay.get();
    }
    public double getTotalPayed() {
        return totalPayed.get();
    }
    public int getHoldDays() {
        return holdDays.get();
    }

    public SimpleDoubleProperty payProperty() {
        return pay;
    }

    public String getName() {
        return name.get();
    }
    public String getIsOnHold() {
        return isOnHold.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDateStart() {
        return dateStart.get();
    }

    public StringProperty dateStartProperty() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd.get();
    }

    public StringProperty dateEndProperty() {
        return dateEnd;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }


}
