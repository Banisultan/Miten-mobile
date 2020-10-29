package id.bnn.convey.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import id.bnn.convey.FinalActivity.HomeFragment.HomeFragment;
import id.bnn.convey.ProfileFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    Fragment[] fragment = {
         homeFragment, profileFragment
    };
    public HomeAdapter(FragmentManager fm){
        super(fm);
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
}
