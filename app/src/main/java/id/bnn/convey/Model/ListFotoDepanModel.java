package id.bnn.convey.Model;

import android.graphics.Bitmap;
import android.net.Uri;

public class ListFotoDepanModel {

    Uri uriimage;
    Bitmap bitmap;
    int position_dmg;
    String codedmg;
    String url;

    public ListFotoDepanModel(
            Uri uriimage,
            Bitmap bitmap,
            int position_dmg,
            String codedmg,
            String url
    ){
        this.uriimage = uriimage;
        this.bitmap = bitmap;
        this.position_dmg = position_dmg;
        this.codedmg = codedmg;
        this.url = url;
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

    public String getCodedmg() {
        return codedmg;
    }

    public String getUrl() {
        return url;
    }
}
