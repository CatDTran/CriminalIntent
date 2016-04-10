package com.sweet_roll.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.sweet_roll.android.criminalintent.database.CrimeBaseHelper;
import com.sweet_roll.android.criminalintent.database.CrimeCursorWrapper;
import com.sweet_roll.android.criminalintent.database.CrimeDbSchema;
import com.sweet_roll.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by trand_000 on 12/22/2015.
 */
public class CrimeLab{
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context)
    {
        if(sCrimeLab == null)//if no instance of this class exist
        {
            sCrimeLab = new CrimeLab(context);//then create and allocate an instance
        }
        return sCrimeLab;//return an existing instance if it exists
    }
    //CONSTRUCTOR: PRIVATE, CAN ONLY CALLED BY get()
    private CrimeLab (Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();//open or create a database for this context
    }
    //ADD NEW ROW TO DATABASE TABLE
    public void addCrime(Crime c)
    {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }
    //UPDATE A ROW IN DATABASE TABLE
    public void updateCrime(Crime crime)
    {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + "= ?", new String[]{uuidString});//update table_name where uuid = crime's uuid
    }
    //GET CRIMES LIST
    public List<Crime> getCrimes()
    {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();//it's important to close cursor when done
        }
        return crimes;
    }
    //GET CRIME BY ID
    public Crime getCrime(UUID id)
    {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try{
            if (cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }
    //GET PHOTO FILE
    public File getPhotoFile(Crime crime)
    {
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFileDir == null)
            return null;
        return new File(externalFileDir, crime.getPhotoFilename());
    }
    //STORE DATA FOR EACH CRIME IN CONTENTVALUES OBJECT
    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);//put 1 if solved, 0 otherwise
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }
    //CALLED TO QUERY FOR CRIME
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        //parameters: table_name, all columns, where clause, argument for where clause, group by, having, order by
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
}
