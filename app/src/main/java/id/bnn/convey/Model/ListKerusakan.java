package id.bnn.convey.Model;

public class ListKerusakan {

    String header;
    boolean isHeader;
    String component;
    int countimage;
    String idsurvey;

    public ListKerusakan(
            String header,
            boolean isHeader,
            String component,
            int countimage,
            String idsurvey
    ){
        this.isHeader = isHeader;
        this.header = header;
        this.component = component;
        this.countimage = countimage;
        this.idsurvey = idsurvey;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public String getHeader() {
        return header;
    }

    public String getComponent() {
        return component;
    }

    public int getCountimage() {
        return countimage;
    }

    public String getIdsurvey() {
        return idsurvey;
    }
}
