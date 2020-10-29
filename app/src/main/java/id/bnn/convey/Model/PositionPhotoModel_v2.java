package id.bnn.convey.Model;

import java.util.ArrayList;
import java.util.List;

public class PositionPhotoModel_v2 {
    String position;
    ArrayList<String> list_photo;

    public PositionPhotoModel_v2(
            String position,
            ArrayList<String> list_photo
    ){
        this.position = position;
        this.list_photo = list_photo;
    }

    public String getPosition() {
        return position;
    }

    public ArrayList<String> getList_photo() {
        return list_photo;
    }
}
