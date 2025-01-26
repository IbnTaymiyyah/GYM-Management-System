package com.ibntaymiyya.gym.model;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DietPlans {
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleIntegerProperty dietId;
    private final SimpleIntegerProperty memberId;

    public DietPlans() {
        this.name = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.dietId = new SimpleIntegerProperty(0);
        this.memberId = new SimpleIntegerProperty(0);
    }

    /*     Setter      */

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setDietId(int dietId) {
        this.dietId.set(dietId);
    }

    public void setMemberId(int memberId) {
        this.memberId.set(memberId);
    }


    /*    Getter   */

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public int getDietId() {
        return dietId.get();
    }

    public SimpleIntegerProperty dietIdProperty() {
        return dietId;
    }

    public int getMemberId() {
        return memberId.get();
    }

    public SimpleIntegerProperty memberIdProperty() {
        return memberId;
    }
}
