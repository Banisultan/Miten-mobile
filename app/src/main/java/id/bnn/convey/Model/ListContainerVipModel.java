package id.bnn.convey.Model;

public class ListContainerVipModel {

    String nocontainer;
    String status;
    String waktu;
    String kondisi;

    public ListContainerVipModel(
            String nocontainer,
            String status,
            String waktu,
            String kondisi
    ){
        this.nocontainer = nocontainer;
        this.status = status;
        this.waktu = waktu;
        this.kondisi = kondisi;
    }

    public String getNocontainer() {
        return nocontainer;
    }

    public String getStatus() {
        return status;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getKondisi() {
        return kondisi;
    }
}
