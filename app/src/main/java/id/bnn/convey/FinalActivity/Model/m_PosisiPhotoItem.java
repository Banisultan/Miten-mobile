package id.bnn.convey.FinalActivity.Model;

import java.util.List;

import id.bnn.convey.Model.PhotoModel;

public class m_PosisiPhotoItem {
    String position;
    List<PhotoModel> list_photo;

    public m_PosisiPhotoItem(
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
