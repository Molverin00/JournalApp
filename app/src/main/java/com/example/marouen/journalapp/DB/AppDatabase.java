package com.example.marouen.journalapp.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.marouen.journalapp.DAO.JournalDAO;
import com.example.marouen.journalapp.model.Journal;


@Database(entities = {Journal.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "journal-db";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {
                Log.d(LOG_TAG, "new database instance creating");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
            }
        }
        Log.d(LOG_TAG, "Getting database instance");
        return sInstance;
    }

    public abstract JournalDAO journalDAO();
}
