package com.example.nora.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText mEditName;
    private EditText mEditScore;
    private Button mButton;

    private DatabaseReference mDatabase;
    private ListView mUserlist;

    private ArrayList<String> mUsername = new ArrayList<>();
    private ArrayList<String> mScore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserlist = (ListView)findViewById(R.id.userlist);

        mEditName = (EditText)findViewById(R.id.edittextname);
        mEditScore = (EditText)findViewById(R.id.edittextscore);
        mButton = (Button)findViewById(R.id.createbutton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mEditName.getText().toString().trim();
                String score = mEditScore.getText().toString().trim();

                if (name.isEmpty() || score.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fill all the blanks", Toast.LENGTH_SHORT).show();
                }else {

                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    hashMap.put("Name", name);
                    hashMap.put("Score", score);

                    mDatabase.push().setValue(hashMap);
                    Toast.makeText(MainActivity.this, "Score ajouté !", Toast.LENGTH_SHORT).show();
                    clearEditText();
                }
            }
        });

        final ArrayList<Person> persons = new ArrayList<Person>();
        mUserlist.setAdapter(new Adapter(this, persons));

        mDatabase.orderByChild("Score").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Person Person = new Person();

                String valueName = dataSnapshot.child("Name").getValue(String.class);
                Person.setName(valueName);

                int valueScore = Integer.parseInt(dataSnapshot.child("Score").getValue(String.class));
                Person.setScore(valueScore);

                persons.add(Person);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clearEditText(){
        mEditName.setText("");
        mEditScore.setText("");
    }
}
