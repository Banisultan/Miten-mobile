package id.bnn.convey.FinalActivity.Model;

public class m_PosisiPhoto {

    String code;
    String name;
    int count;

    public m_PosisiPhoto(
            String code,
            String name,
            int count
    ){
        this.code = code;
        this.name = name;
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }
}
