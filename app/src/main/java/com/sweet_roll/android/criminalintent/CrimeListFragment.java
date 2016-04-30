package com.sweet_roll.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by trand_000 on 12/22/2015.
 */
public class CrimeListFragment extends Fragment{
    //////INNER CLASS ViewHolder for this Fragment//////
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;
        //inner class constructor
        public CrimeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }
        //implement onClick() method
        @Override
        public void onClick(View v)
        {
            mCallbacks.onCrimeSelected(mCrime);
        }
        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
    }
    //////INNER CLASS Adapter for this Fragment//////
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        //inner class constructor
        public CrimeAdapter(List <Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent,int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }
        @Override
        public void onBindViewHolder(CrimeHolder holder,int position)
        {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }

    //////RecyclerView for this fragment///////
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE ="subtitle";
    private Callbacks mCallbacks;
    //
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }
    //CALLED BY OS
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    //////CALLED BY OS TO INSTANTIATE Fragment's view//////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
//        LinearLayoutManager mLinearManager = new LinearLayoutManager(getActivity());
//        mCrimeRecyclerView.setLayoutManager(mLinearManager);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null){//get mSubtitleVisiable state across rotation
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }
    //SAVE STATE ACORSS ROTATION
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
       super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
    //ONDETTACH
    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallbacks = null;
    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    //CREATE AND INFLATE MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else
            subtitleItem.setTitle(R.string.show_subtitle);
    }
    //CALL BACK WHEN A MENU ITEM IS SELECTED
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //SHOW SUBTITLE
    private void updateSubtitle()
    {
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
    //method to update UI
    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());//the one and only CrimeLab singleton for this Activity
        List<Crime> crimes = crimeLab.getCrimes();//get the list of crimes
        if(mAdapter == null)//if mAdapter was not previously initialized
        {
            mAdapter = new CrimeAdapter(crimes);//create a CrimeAdapter instance
            mCrimeRecyclerView.setAdapter(mAdapter);//set adapter for this RecyclerView
        }
        else//if mAdapter was already initialized
        {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
}
