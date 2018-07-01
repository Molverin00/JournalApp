package com.example.marouen.journalapp;

import android.app.Application;

import com.example.marouen.journalapp.DB.AppDatabase;
import com.example.marouen.journalapp.repository.JournalRepository;

public class JournalApp extends Application {

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public JournalRepository getJournalRepository() {
        return JournalRepository.getInstance(getDatabase());
    }
}
