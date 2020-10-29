package id.bnn.convey.Model;

public class ViewSurveyinModel {
    String nama;
    String value;

    public ViewSurveyinModel(
            String nama,
            String value
    ){
        this.nama = nama;
        this.value =value;
    }

    public String getNama() {
        return nama;
    }

    public String getValue() {
        return value;
    }

}
