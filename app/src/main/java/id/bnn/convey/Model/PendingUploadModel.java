package id.bnn.convey.Model;

public class PendingUploadModel {

    String nocontainer;
    String keterangan;

    public PendingUploadModel(
            String nocontainer,
            String keterangan
    ){
        this.nocontainer = nocontainer;
        this.keterangan = keterangan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getNocontainer() {
        return nocontainer;
    }
}
