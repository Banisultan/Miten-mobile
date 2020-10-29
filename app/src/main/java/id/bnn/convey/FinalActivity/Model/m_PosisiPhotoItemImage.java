package id.bnn.convey.FinalActivity.Model;

public class m_PosisiPhotoItemImage {
    String image_dict;
    String image_url;
    String id_foto;
    String action;

    public m_PosisiPhotoItemImage(
            String image_dict,
            String image_url,
            String id_foto,
            String action
    ){
        this.image_dict = image_dict;
        this.image_url = image_url;
        this.id_foto = id_foto;
        this.action = action;
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

    public String getAction() {
        return action;
    }
}
