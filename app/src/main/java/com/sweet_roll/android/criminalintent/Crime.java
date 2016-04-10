package com.sweet_roll.android.criminalintent;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by trand_000 on 12/19/2015.
 */
public class Crime {
    //member fields declaration
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private DateFormat mDateFormat;
    private boolean mSolved;
    private String mSuspect;
    private String[] DayOfWeek = {"Sunday","Monday",//an array map of days in a week
                                    "Tuesday","Wednesday",
                                    "Thursday","Friday","Saturday"};

    //CONSTRUCTOR
    public Crime()
    {
        this(UUID.randomUUID());
    }
    //CONSTRUCTOR
    public Crime(UUID id)
    {
        mId = id;
        mDate = new Date();
    }
    //get id
    public UUID getId() {
        return mId;
    }
    //get title
    public String getTitle() {
        return mTitle;
    }
    //set title
    public void setTitle(String title) {
        mTitle = title;
    }
    //get crime's date
    public Date getDate() {
        return mDate;
    }
    //set crime's date
    public void setDate(Date date) {
        mDate = date;
    }
    //convert date format to string: Wednesday, mm dd, yyyy
    public String getDateFormatString()
    {
        return DayOfWeek[mDateFormat.getCalendar().get(Calendar.DAY_OF_WEEK) - 1] +
                ", " + mDateFormat.format(mDate);
    }
    //get if crime is solved
    public boolean isSolved() {
        return mSolved;
    }
    //set crime is solved
    public void setSolved(boolean solved) {
        mSolved = solved;
    }
    //get photo image filename
    public String getPhotoFilename()
    {
        return "IMG_" + getId().toString() + ".jpg";
    }
    //get suspect name
    public String getSuspect(){
        return mSuspect;
    }
    //set suspect's name
    public void setSuspect(String suspect){
        mSuspect = suspect;
    }
}