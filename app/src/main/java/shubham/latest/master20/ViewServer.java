package shubham.latest.master20;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewServer extends AppCompatActivity {
    TextView jio_num,airtel_num,bsnl_num,vi_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_server);
        jio_num=findViewById(R.id.jio_num);
        airtel_num=findViewById(R.id.airtel_num);
        bsnl_num=findViewById(R.id.bsnl_num);
        vi_num=findViewById(R.id.vi_num);
        MasterDatabase db=new MasterDatabase(getApplicationContext());
        db.eraseData("serverlist_table");

        DatabaseReference mDatabasejio = FirebaseDatabase.getInstance().getReference("jio");

                 mDatabasejio.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    jio_num.setText(value);
                    Log.d(TAG, "Value is: " + value);
                   // db.deleteDatasrver("serverlist_table","jio");
                    db.serverlistTable("jio",value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        DatabaseReference mDatabaseairtel = FirebaseDatabase.getInstance().getReference("airtel");

        mDatabaseairtel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                airtel_num.setText(value);
                Log.d(TAG, "Value is: " + value);
              //  db.deleteDatasrver("serverlist_table","airtel");
                db.serverlistTable("airtel",value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference mDatabasebsnl = FirebaseDatabase.getInstance().getReference("bsnl");

        mDatabasebsnl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                bsnl_num.setText(value);
                Log.d(TAG, "Value is: " + value);
              //  db.deleteDatasrver("serverlist_table","bsnl");
                db.serverlistTable("bsnl",value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        DatabaseReference mDatabasevi = FirebaseDatabase.getInstance().getReference("vi");

        mDatabasevi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                vi_num.setText(value);
                Log.d(TAG, "Value is: " + value);
               // db.deleteDatasrver("serverlist_table","vi");
                db.serverlistTable("vi",value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        finish();
    }


}