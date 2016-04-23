package com.sweet_roll.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by trand_000 on 12/22/2015.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment createFragment();
    @LayoutRes
    protected int getLayoutResId()//child class can override this method
    {
        return R.layout.activity_fragment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();//get fragment manager for this FragmentActivity
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);//this fragment is from support version
        if(fragment == null)//if fragment manager could not find fragment_container's view
        {
            fragment = createFragment();//allocate space for fragment
            /*FragmentManager.beginTransaction() creates and returns a FragmentTransaction instance;
            FragmentTransaction.add() add a fragment to activity state and return the same FragmentTransaction instance;
            FragmentTransaction.commit() method schedule a commit for this transaction
             */
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();//fragment_container is the view where fragment should appear
        }
    }
}

