package com.example.marouen.journalapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.marouen.journalapp.model.Journal;
import com.example.marouen.journalapp.repository.JournalRepository;

public class AddNewJournalViewModel extends ViewModel {
    private LiveData<Journal> mJournal;

    public AddNewJournalViewModel(/*Application application,*/ JournalRepository journalRepository, final int journalId) {
//        super(application);
        this.mJournal = journalRepository.getJournal(journalId);
    }

    public LiveData<Journal> getJournal() {
        return mJournal;
    }


}
