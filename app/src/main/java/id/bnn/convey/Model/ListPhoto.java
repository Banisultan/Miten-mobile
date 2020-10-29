package id.bnn.convey.Model;

import android.graphics.Bitmap;

public class ListPhoto {

    String url;
    Bitmap bitmap;
    String file_dir;
    String id_survey;
    String id_foto;

    public ListPhoto(
            String url,
            Bitmap bitmap,
            String file_dir,
            String id_survey,
            String id_foto
    ){
        this.url = url;
        this.bitmap = bitmap;
        this.file_dir = file_dir;
        this.id_survey = id_survey;
        this.id_foto = id_foto;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getFile_dir() {
        return file_dir;
    }

    public String getId_survey() {
        return id_survey;
    }

    public String getId_foto() {
        return id_foto;
    }
}
