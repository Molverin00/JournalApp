package com.example.marouen.journalapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.marouen.journalapp.AppExecutors;
import com.example.marouen.journalapp.DB.AppDatabase;
import com.example.marouen.journalapp.R;
import com.example.marouen.journalapp.model.Journal;
import com.example.marouen.journalapp.repository.JournalRepository;
import com.example.marouen.journalapp.viewmodel.AddNewJournalViewModel;
import com.example.marouen.journalapp.viewmodel.AddNewJournalViewModelFactory;

import java.util.Date;

public class AddNewActivity extends AppCompatActivity {
    public final static String EXTRA_ENTRY_ID = "entry_id";
    public final static int DEFAULT_ENTRY_ID = 0;
    private static final String TAG = AddNewActivity.class.getSimpleName();
    private int mJournalId = DEFAULT_ENTRY_ID;
    private FloatingActionButton mFabSave;
    private EditText mEditTextTitle, mEditTextContent;
    private AppDatabase mDb;
    private JournalRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        mEditTextTitle = findViewById(R.id.edit_text_title);
        mEditTextContent = findViewById(R.id.edit_text_content);
        Log.d(TAG, mEditTextTitle.getText().toString());
        mFabSave = findViewById(R.id.fab_save_journal);
        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveFabClicked();
            }
        });
        mDb = AppDatabase.getInstance(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ENTRY_ID)) {
            mJournalId = intent.getIntExtra(EXTRA_ENTRY_ID, DEFAULT_ENTRY_ID);
            mRepository = JournalRepository.getInstance(mDb);
            AddNewJournalViewModelFactory factory
                    = new AddNewJournalViewModelFactory(mRepository, mJournalId);
            final AddNewJournalViewModel viewModel = ViewModelProviders.of(this, factory).get(AddNewJournalViewModel.class);
            // MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            //  viewModel.init(mJournalId);
            viewModel.getJournal().observe(this, new Observer<Journal>() {
                @Override
                public void onChanged(@Nullable Journal journal) {
                    populateUI(journal);
                }
            });
        }

    }

    public void populateUI(Journal journal) {
        if (journal == null) return;

        mEditTextTitle.setText(journal.getTitle());
        mEditTextContent.setText(journal.getContent());
    }

    public void onSaveFabClicked() {
        final String title = mEditTextTitle.getText().toString();
        final String content = mEditTextContent.getText().toString();
        Log.d(TAG, title + "\t" + content);
        final Journal journal = new Journal(title, content, new Date());
        AppExecutors.getInstance().diskIO.execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_ENTRY_ID) {
                    mDb.journalDAO().save(journal);
                } else {
                    journal.setId(mJournalId);
                    mDb.journalDAO().updateJournal(journal);
                }

            }
        });
        finish();
    }
}
