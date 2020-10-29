package id.bnn.convey.Model;

public class ListComponentCodeModel {

    String code;
    String name;

    public ListComponentCodeModel(
            String code,
            String name
    ){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
