package id.bnn.convey.FinalActivity.Model;

public class m_Damage {

    String mobid_damage;
    String component;
    int count;

    public m_Damage(String mobid_damage, String component, int count){
        this.mobid_damage = mobid_damage;
        this.component = component;
        this.count = count;
    }

    public String getMobid_damage() {
        return mobid_damage;
    }

    public String getComponent() {
        return component;
    }

    public int getCount() {
        return count;
    }
}
