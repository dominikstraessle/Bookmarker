package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class Environment {

    private SimpleIntegerProperty idEnv;
    private SimpleStringProperty name;
    private SimpleStringProperty desc;
    private Color color;


    public Environment(SimpleStringProperty name, SimpleStringProperty desc) {
        this.name = name;
        this.desc = desc;
    }

    public Environment(SimpleIntegerProperty idEnv, SimpleStringProperty name, SimpleStringProperty desc, Color color) {
        this.idEnv = idEnv;
        this.name = name;
        this.desc = desc;
        this.color = color;
    }

    public int getIdEnv() {
        return idEnv.get();
    }

    public SimpleIntegerProperty idEnvProperty() {
        return idEnv;
    }

    public void setIdEnv(int idEnv) {
        this.idEnv.set(idEnv);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDesc() {
        return desc.get();
    }

    public SimpleStringProperty descProperty() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public Color getColor() {
        return color;
    }


    public void setColor(Color color) {
        this.color = color;
    }
}
