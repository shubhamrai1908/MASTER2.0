package shubham.latest.master20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Dashboard");
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Value is: " + e.toString());
        }
        getdata();
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        int itemId = item.getItemId();
        if (itemId == R.id.addground) {
            startActivity(new Intent(getApplicationContext(),AddGround.class));
            finish();
        } else if (itemId == R.id.viewground) {
            startActivity(new Intent(getApplicationContext(),ViewGround.class));
            finish();
        } else if (itemId == R.id.viewserver) {
            startActivity(new Intent(getApplicationContext(),ViewServer.class));
            finish();
        }
        else if (itemId == R.id.addserver) {
            startActivity(new Intent(getApplicationContext(), AddServer.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getdata() {

        try {

            MasterDatabase db=new MasterDatabase(getApplicationContext());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ground");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    db.eraseData("groundlist_table");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            Ground ground = ds.getValue(Ground.class);
                            db.grounglistTable(ground.name,ground.phone);
                             Toast.makeText(DashboardActivity.this,"inserted",Toast.LENGTH_SHORT).show();


                        }catch (Exception e)
                        {
                            Toast.makeText(DashboardActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e)
        {
            Toast.makeText(DashboardActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    private void storeserver() {

        try {

            MasterDatabase db=new MasterDatabase(getApplicationContext());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ground");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    db.eraseData("groundlist_table");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            Ground ground = ds.getValue(Ground.class);
                            db.grounglistTable(ground.name,ground.phone);
                            Toast.makeText(DashboardActivity.this,"inserted",Toast.LENGTH_SHORT).show();


                        }catch (Exception e)
                        {
                            Toast.makeText(DashboardActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e)
        {
            Toast.makeText(DashboardActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}