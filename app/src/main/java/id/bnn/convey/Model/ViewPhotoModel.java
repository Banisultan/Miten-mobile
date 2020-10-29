package id.bnn.convey.Model;

public class ViewPhotoModel {
    String image_file;

    public ViewPhotoModel(
            String image_file
    ){
        this.image_file = image_file;
    }

    public String getImage_file() {
        return image_file;
    }
}
