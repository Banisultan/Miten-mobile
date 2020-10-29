package id.bnn.convey.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class PositionPhotoModel {
    String position;
    List<PhotoModel> list_photo;

    public PositionPhotoModel(
            String position,
            List<PhotoModel> list_photo
    ){
        this.position = position;
        this.list_photo = list_photo;
    }

    public String getPosition() {
        return position;
    }

    public List<PhotoModel> getList_photo() {
        return list_photo;
    }

    public void setList_photo(List<PhotoModel> list_photo) {
        this.list_photo = list_photo;
    }
}
