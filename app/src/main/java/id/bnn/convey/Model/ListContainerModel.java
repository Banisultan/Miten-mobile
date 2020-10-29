package id.bnn.convey.Model;

public class ListContainerModel {

    String idsurvei;
    String nocontainer;
    String status;
    String waktu;
    String kondisi;
    String keterangan;
    String db_tipe;

    public ListContainerModel(
            String idsurvei,
            String nocontainer,
            String status,
            String waktu,
            String kondisi,
            String keterangan,
            String db_tipe
    ){
        this.idsurvei = idsurvei;
        this.nocontainer = nocontainer;
        this.status = status;
        this.waktu = waktu;
        this.kondisi = kondisi;
        this.keterangan = keterangan;
        this.db_tipe = db_tipe;
    }

    public String getIdsurvei() {
        return idsurvei;
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

    public String getKeterangan() {
        return keterangan;
    }

    public String getDb_tipe() {
        return db_tipe;
    }
}
