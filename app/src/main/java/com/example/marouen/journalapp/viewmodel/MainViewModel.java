package com.example.marouen.journalapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.marouen.journalapp.JournalApp;
import com.example.marouen.journalapp.model.Journal;
import com.example.marouen.journalapp.repository.JournalRepository;

import java.util.List;


public class MainViewModel extends AndroidViewModel {
    private int journalId;
    private LiveData<Journal> journal;
    private JournalRepository journalRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.journalRepository = new JournalApp().getJournalRepository();
    }

//    @Inject
//    public MainViewModel(JournalRepository journalRepository) {
//        this.journalRepository = journalRepository;
//    }

    public void init(int journalId) {
        if (this.journal != null) {
            return;
        }
        this.journal = this.journalRepository.getJournal(journalId);
    }

    public LiveData<Journal> getJournal() {
        return this.journal;
    }

    public LiveData<List<Journal>> getJournals() {
        return this.journalRepository.getJournals();
    }

}
