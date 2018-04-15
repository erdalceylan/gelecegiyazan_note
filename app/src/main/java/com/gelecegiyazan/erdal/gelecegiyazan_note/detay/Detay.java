package com.gelecegiyazan.erdal.gelecegiyazan_note.detay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dd.processbutton.iml.GenerateProcessButton;
import com.gelecegiyazan.erdal.gelecegiyazan_note.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class Detay extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button time;
    EditText title;
    EditText content;
    private DatabaseReference mNoteReference;
    private DatabaseReference mNReference;

    private String mPostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPostKey = getIntent().getStringExtra("id");

        if(mPostKey != null){
            mNReference = FirebaseDatabase.getInstance().getReference()
                    .child("posts").child(mPostKey);
        }


        mNoteReference = FirebaseDatabase.getInstance().getReference()
                .child("posts");

         title = (EditText) findViewById(R.id.title);
         content = (EditText) findViewById(R.id.content);
        time = (Button) findViewById(R.id.time);
        GenerateProcessButton save = (GenerateProcessButton) findViewById(R.id.save_button);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Detay.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note note = new Note();
                note.title = title.getText().toString();
                note.content = content.getText().toString();
                note.date=time.getText().toString();

                mNoteReference.push().setValue(note);

//                if(mNReference != null){
//                    mNReference.push().setValue(note);
//                }else{
//                    mNoteReference.push().setValue(note);
//                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Note note = dataSnapshot.getValue(Note.class);
                note.id = dataSnapshot.getKey();
                // [START_EXCLUDE]
                System.out.println(note.title);
                time.setText( note.date);
                title.setText( note.title);
                content.setText( note.content);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("dfv", "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
if(mPostKey != null){

    mNReference.addValueEventListener(postListener);
}
    }

    //
//    @Override
//    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
//        String time = "You picked the following time: "+hourOfDay+"h"+minute+"m"+second;
//        System.out.println(time);
//    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        System.out.println(date);

        time.setText(date);
    }
}
