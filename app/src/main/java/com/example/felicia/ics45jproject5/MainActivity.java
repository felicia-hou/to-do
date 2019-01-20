package com.example.felicia.ics45jproject5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import android.widget.SearchView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ListView dataListView;
    private EditText itemText;
    private Button addButton;
    private Button deleteButton;

    private Boolean itemSelected = false;

    private int position = 0;
    private int count = 0;

    ArrayList<String> itemsList = new ArrayList<String>();
    ArrayList<String> keysList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("todo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListView = (ListView) findViewById(R.id.taskListView);
        itemText = (EditText) findViewById(R.id.taskText);
        addButton = (Button) findViewById(R.id.addTaskButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                count++;
                String item = itemText.getText().toString();
                Task task = new Task(count, item);
                String key = dbRef.push().getKey();

                itemText.setText("");
                dbRef.child(key).child("description").setValue(task.getTask());

                adapter.notifyDataSetChanged();
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setEnabled(false);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                dataListView.setItemChecked(position, false);
                dbRef.child(keysList.get(position)).removeValue();
                deleteButton.setEnabled(false);
                finish();
                startActivity(getIntent());
            }
        });


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                itemsList);
        dataListView.setAdapter(adapter);
        dataListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        dataListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {

                        itemSelected = true;
                        deleteButton.setEnabled(true);
                    }
                });

        addChildEventListener();
    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(
                        (String) dataSnapshot.child("description").getValue());

                keysList.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);

                if (index != -1) {
                    itemsList.remove(index);
                    keysList.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        dbRef.addChildEventListener(childListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_tasks);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                deleteButton.setEnabled(false);
                return false;

            }

        });

        return super.onCreateOptionsMenu(menu);


    }

}




