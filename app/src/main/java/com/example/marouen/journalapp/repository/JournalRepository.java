package com.example.marouen.journalapp.repository;

import android.arch.lifecycle.LiveData;

import com.example.marouen.journalapp.AppExecutors;
import com.example.marouen.journalapp.DB.AppDatabase;
import com.example.marouen.journalapp.model.Journal;

import java.util.List;


public class JournalRepository {

    private static final Object LOCK = new Object();
    private static JournalRepository sInstance;
    private final AppDatabase mDb;


    private JournalRepository(final AppDatabase database) {
        this.mDb = database;
    }

    public static JournalRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new JournalRepository(database);
            }
        }
        return sInstance;
    }

    public LiveData<Journal> getJournal(int journalId) {

        return mDb.journalDAO().loadJournalById(journalId);
    }

    private void refreshJournals() {
        AppExecutors.getInstance().diskIO.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public LiveData<List<Journal>> getJournals() {
        refreshJournals();
        return mDb.journalDAO().getAllJournals();

    }
}
