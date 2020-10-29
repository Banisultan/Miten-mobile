package id.bnn.convey.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewSurveyinKerusakanModel {
    String component;
    String location;
    String quantity;
    String damage;
    String tpc;
    String repair;
    String dimensi;
    String posisi;
    ArrayList<String> listfoto;
    ArrayList<String> listfotorepair;

    public ViewSurveyinKerusakanModel(
            String component,
            String location,
            String quantity,
            String damage,
            String tpc,
            String repair,
            String dimensi,
            String posisi,
            ArrayList<String> listfoto,
            ArrayList<String> listfotorepair
    ){
        this.component = component;
        this.location =location;
        this.quantity = quantity;
        this.damage = damage;
        this.tpc = tpc;
        this.repair = repair;
        this.listfoto = listfoto;
        this.dimensi = dimensi;
        this.posisi = posisi;
        this.listfotorepair = listfotorepair;
    }

    public String getComponent() {
        return component;
    }

    public String getLocation() {
        return location;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDamage() {
        return damage;
    }

    public String getTpc() {
        return tpc;
    }

    public String getRepair() {
        return repair;
    }

    public String getDimensi() {
        return dimensi;
    }

    public String getPosisi() {
        return posisi;
    }

    public ArrayList<String> getListfoto() {
        return listfoto;
    }

    public ArrayList<String> getListfotorepair() {
        return listfotorepair;
    }
}
