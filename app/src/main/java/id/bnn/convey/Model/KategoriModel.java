package id.bnn.convey.Model;

public class KategoriModel {

    int id;
    String nama;
    String url;

    public KategoriModel(
            int id,
            String nama,
            String url
    ){
        this.id = id;
        this.nama = nama;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUrl() {
        return url;
    }
}
