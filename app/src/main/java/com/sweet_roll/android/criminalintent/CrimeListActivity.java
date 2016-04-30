package com.sweet_roll.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by trand_000 on 12/22/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }
    @Override
    public void onCrimeSelected(Crime crime)
    {
        if(findViewById(R.id.detail_fragment_container) == null)//if detail_fragment_containier is does not exist in this activity's layout
        {                                                       //it means that the app is on a phone device
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        }
        else//if detail_fragment_container is found, this means the app is on a tablet device
        {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();//replace the fragment container
                                                                                                                       //with the new fragment
        }
    }
    public void onCrimeUpdated(Crime crime)
    {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
