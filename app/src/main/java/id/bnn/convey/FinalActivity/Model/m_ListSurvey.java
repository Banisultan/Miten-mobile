package id.bnn.convey.FinalActivity.Model;

public class m_ListSurvey {
    String surveyid;
    String nocontainer;
    String condition;
    String time;

    public m_ListSurvey(String surveyid, String nocontainer, String condition, String time){
        this.surveyid = surveyid;
        this.nocontainer = nocontainer;
        this.condition = condition;
        this.time = time;
    }

    public String getSurveyid() {
        return surveyid;
    }

    public String getCondition() {
        return condition;
    }

    public String getNocontainer() {
        return nocontainer;
    }

    public String getTime() {
        return time;
    }
}
