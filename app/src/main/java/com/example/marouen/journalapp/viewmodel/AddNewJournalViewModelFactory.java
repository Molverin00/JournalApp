package com.example.marouen.journalapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.marouen.journalapp.repository.JournalRepository;


public class AddNewJournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    //  private final Application mApplication;
    private final int mJournalId;
    private final JournalRepository mRepository;

    public AddNewJournalViewModelFactory(/*Application mApplication,*/JournalRepository journalRepository, int mJournalId) {
        //this.mApplication = mApplication;
        this.mJournalId = mJournalId;
        //this.mRepository = ((JournalApp) mApplication).getJournalRepository();
        this.mRepository = journalRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNewJournalViewModel(/*mApplication,*/ mRepository, mJournalId);
    }
}
