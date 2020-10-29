package id.bnn.convey.Model;

import android.graphics.Bitmap;
import android.net.Uri;

public class ListFotoKananModel {

    Uri uriimage;
    Bitmap bitmap;
    int position_dmg;

    public ListFotoKananModel(
            Uri uriimage,
            Bitmap bitmap,
            int position_dmg
    ){
        this.uriimage = uriimage;
        this.bitmap = bitmap;
        this.position_dmg = position_dmg;
    }

    public Uri getUriimage() {
        return uriimage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getPosition_dmg() {
        return position_dmg;
    }

    public void setPosition_dmg(int position_dmg) {
        this.position_dmg = position_dmg;
    }
}
