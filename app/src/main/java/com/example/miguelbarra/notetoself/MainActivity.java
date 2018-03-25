package com.example.miguelbarra.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter mNoteAdapter;
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;

    public void createNewNote(Note n) {
        mNoteAdapter.addNote(n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mNoteAdapter = new NoteAdapter();

        ListView listNote = findViewById(R.id.listView);

        listNote.setAdapter(mNoteAdapter);

        // Handle clicks on the listView
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                    Create a temporary Note
                    Which is a reference to the Note
                    that has just been clicked
                 */
                Note tempNote = mNoteAdapter.getItem(i);

                // Create a new dialog window
                DialogShowNote dialog = new DialogShowNote();
                // Send in a reference to the note to be shown
                dialog.sendNoteSelected(tempNote);
                // Show the dialog window with the note in it
                dialog.show(getFragmentManager(), "");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_add) {
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NoteAdapter extends BaseAdapter {
        List<Note> noteList = new ArrayList<>();

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Note getItem(int i) {
            return noteList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // Implement this method next
            // has view been inflated already
            if (view == null) {
                // If not, do so here
                // First create a LayoutInflater
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Now instantiate view using inflater inflate
                view = inflater.inflate(R.layout.list_item, viewGroup, false);
                // The false parameter is necessary
                // because of the way that we want to use list_item
            } // End if

            // Grab a reference to all our TextView and ImageView widgets
            TextView txtTitle = view.findViewById(R.id.txtTitle);
            TextView txtDescription = view.findViewById(R.id.txtDescription);
            ImageView ivImportant = view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea = view.findViewById(R.id.imageViewIdea);

            // Hide any ImageView widgets that are not relevant
            Note tempNote = getItem(i);

            if (!tempNote.isImportant()) {
                ivImportant.setVisibility(View.GONE);
            }
            if (!tempNote.isTodo()) {
                ivTodo.setVisibility(View.GONE);
            }
            if (!tempNote.isIdea()) {
                ivIdea.setVisibility(View.GONE);
            }

            // Add the text to the heading and description
            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());

            return view;
        }

        void addNote(Note n) {
            noteList.add(n);
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        mSound = mPrefs.getBoolean("sound", true);
        mAnimOption = mPrefs.getInt("anim option", SettingsActivity.FAST);
    }
}
