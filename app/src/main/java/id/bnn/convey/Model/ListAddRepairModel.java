package id.bnn.convey.Model;

import java.util.ArrayList;

public class ListAddRepairModel {
    String idsurveydetail;
    String header;
    boolean isHeader;
    String component;
    ArrayList<String> listkerusakan;
    ArrayList<String> listrepair;
    int countfoto;

    public ListAddRepairModel(
            String idsurveydetail,
            String header,
            boolean isHeader,
            String component,
            ArrayList<String> listkerusakan,
            ArrayList<String> listrepair,
            int countfoto
        )
    {
        this.idsurveydetail = idsurveydetail;
        this.header = header;
        this.isHeader = isHeader;
        this.component = component;
        this.listkerusakan = listkerusakan;
        this.listrepair = listrepair;
        this.countfoto = countfoto;
    }

    public String getIdsurveydetail() {
        return idsurveydetail;
    }

    public String getHeader() {
        return header;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public String getComponent() {
        return component;
    }

    public ArrayList<String> getListkerusakan() {
        return listkerusakan;
    }

    public ArrayList<String> getListrepair() {
        return listrepair;
    }

    public int getCountfoto() {
        return countfoto;
    }

    public void setListrepair(ArrayList<String> listrepair) {
        this.listrepair = listrepair;
    }
}
