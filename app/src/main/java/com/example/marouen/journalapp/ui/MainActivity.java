package com.example.marouen.journalapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.marouen.journalapp.AppExecutors;
import com.example.marouen.journalapp.BaseActivity;
import com.example.marouen.journalapp.DB.AppDatabase;
import com.example.marouen.journalapp.R;
import com.example.marouen.journalapp.Utility.SharedPrefsUtils;
import com.example.marouen.journalapp.adapters.JournalAdapter;
import com.example.marouen.journalapp.model.Journal;
import com.example.marouen.journalapp.viewmodel.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener {
    private RecyclerView mRecyclerView;
    private JournalAdapter mAdapter;
    private AppDatabase mDb;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setSupportActionBar(toolbar);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mRecyclerView = findViewById(R.id.rv_articles);
        mAdapter = new JournalAdapter(this);
        // mAdapter.setJournals(getEntries());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                AppExecutors.getInstance().diskIO.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.journalDAO().deleteJournal(mAdapter.getJournal(position));
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = findViewById(R.id.fab_add_new_journal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addEntryIntent =
                        new Intent(MainActivity.this, AddNewActivity.class);
                startActivity(addEntryIntent);
            }
        });
        mDb = AppDatabase.getInstance(this);
        setupViewModel();

        // get shared pref


        String firebaseId = SharedPrefsUtils.getStringPreference(getApplicationContext(), BaseActivity.ARG_FIREBASE_ID);

        //showSuccessSnackBar(findViewById(android.R.id.content), MainActivity.this, getApplicationContext().getString(R.string.signed_in_as, firebaseId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.action_logout) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles click event on recyclerview item and pass item's index
     *
     * @param clickedItemIndex item index
     */
    @Override
    public void onItemClickListener(int clickedItemIndex) {
        Intent intent = new Intent(this, AddNewActivity.class);
        intent.putExtra(AddNewActivity.EXTRA_ENTRY_ID, mAdapter.getJournal(clickedItemIndex).getId());
        startActivity(intent);

    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getJournals().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable List<Journal> journals) {
                mAdapter.setJournals(journals);
            }
        });
    }

    /**
     * TODO: delete after firebase store functionality added
     */
    private List<Journal> getEntries() {
        ArrayList<Journal> entries = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            entries.add(new Journal("Google ALC 3 Challenge", "Thank you for the amazing experience ! I learned a lot and I hope I get the Nano degree good news from you !", new Date()));
        }
        return entries;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

}