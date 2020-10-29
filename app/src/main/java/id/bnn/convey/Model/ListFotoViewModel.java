package id.bnn.convey.Model;

public class ListFotoViewModel {

    String url;
    String dmg;
    public ListFotoViewModel(
            String url,
            String dmg
    ){
        this.url = url;
        this.dmg = dmg;
    }

    public String getUrl() {
        return url;
    }

    public String getDmg() {
        return dmg;
    }
}
