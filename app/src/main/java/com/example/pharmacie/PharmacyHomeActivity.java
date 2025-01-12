package com.example.pharmacie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PharmacyHomeActivity extends AppCompatActivity {
    //view
    private FloatingActionButton fab;
    private RecyclerView pharmacieRv;
    //db
    private DbHelper dbHelper;
    //adapter
    private AdapterPharmacie adapterPharmacie;
    //action bar
    private ActionBar actionBar;
    // sort  category
    private String sortByNewest = Constants.C_ADDED_DATE + " ASC";
    private String getSortByOldest = Constants.C_ADDED_DATE + " DESC";
    private String getSortByAZ = Constants.C_NOM + " ASC";
    private String getSortByZA = Constants.C_NOM + " DESC";
    //set cuurrent sort order
    private String currentSort = sortByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pharmacy_home_activity);
        //init action bar
        actionBar = getSupportActionBar();

        //init db
        dbHelper = new DbHelper(this);
        fab = findViewById(R.id.fab);
        pharmacieRv = findViewById(R.id.pharmacieRv);
        pharmacieRv.setHasFixedSize(true);
        //add listenner

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v){
                // move to new activity to add contact
                Intent intent = new Intent(PharmacyHomeActivity.this, AddPharmacie.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });

        loadData(currentSort);

    }

    private void loadData(String currentSort) {
        adapterPharmacie = new AdapterPharmacie(dbHelper.getAllData(currentSort), this);
        pharmacieRv.setAdapter(adapterPharmacie);

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData(currentSort); // refresh data
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu, menu);
        // get search item
        MenuItem item = menu.findItem(R.id.searchMedecine);
        //search area
        SearchView searchView = (SearchView) item.getActionView() ;
        // set max value for width
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPharmacie(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    searchPharmacie(newText);
                return false;
            }
        });

        return true ;
    }
    private void searchPharmacie(String query) {
       adapterPharmacie = new AdapterPharmacie(dbHelper.getSearchPharmacie(query), this);
       pharmacieRv.setAdapter(adapterPharmacie);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllMedecine) {
            dbHelper.deleteAllData();
            onResume();
            return true;
        } else if (item.getItemId() == R.id.sortMedecine ) {
            sortDialog();
            
        }
        {
            
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortDialog() {
        //option for alert dialog
        String [] option =   {"Newest", "Oldest", "A to Z", "Z to A"};
        //Alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
            builder.setItems(option,new DialogInterface.OnClickListener(){
                @Override
        public void onClick (DialogInterface dialog, int which){
                    if (which == 0) {
                        loadData(sortByNewest);
                    } else if (which == 1) {
                        loadData(getSortByOldest);
                }else if (which == 2) {
                        loadData(getSortByAZ);
                    }else if (which == 3) {
                        loadData(getSortByZA);
                    }
                }
            }) ;
            builder.create().show();

    }
    }

