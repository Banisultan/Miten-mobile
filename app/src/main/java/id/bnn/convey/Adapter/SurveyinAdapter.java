package id.bnn.convey.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import id.bnn.convey.FragmentListSurveyin.ApproveFragment;
import id.bnn.convey.FragmentListSurveyin.DoneFragment;
import id.bnn.convey.FragmentListSurveyin.RepairDoneFragment;
import id.bnn.convey.FragmentListSurveyin.WaitingFragment;

public class SurveyinAdapter extends FragmentStatePagerAdapter {

    WaitingFragment waitingFragment = new WaitingFragment();
    ApproveFragment approveFragment = new ApproveFragment();
    RepairDoneFragment repairDoneFragment = new RepairDoneFragment();
    DoneFragment doneFragment = new DoneFragment();

    Fragment[] fragment = new Fragment[]{};

    public SurveyinAdapter(String tipe, FragmentManager fm) {
        super(fm);
        if(tipe.equals("SURVEYOR")){
            fragment = new Fragment[]{waitingFragment, doneFragment};
        }else if(tipe.equals("SUPERVISOR")){
            fragment = new Fragment[]{waitingFragment, approveFragment, repairDoneFragment, doneFragment};
        }else if(tipe.equals("QC_REPAIR")){
            fragment = new Fragment[]{waitingFragment, repairDoneFragment, doneFragment};
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragment[position];
    }

    @Override
    public int getCount() {
        return fragment.length;
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
