package id.bnn.convey.Model;

public class PhotoModel {

    String image_dict;
    String image_url;
    String id_foto;

    public PhotoModel(
            String image_dict,
            String image_url,
            String id_foto
    ){
        this.image_dict = image_dict;
        this.image_url = image_url;
        this.id_foto = id_foto;
    }

    public String getImage_dict() {
        return image_dict;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getId_foto() {
        return id_foto;
    }
}
