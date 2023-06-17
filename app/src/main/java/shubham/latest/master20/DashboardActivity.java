package shubham.latest.master20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {
    NavigationView nav;
    View headerView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uid,name,email,phone,link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);
        nav = findViewById(R.id.navmenu);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        getdata();
        name = "Name";
        email = "Email ID";
        phone = "Phone Number";
        link = "";
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        nav = findViewById(R.id.navmenu);
        headerView = nav.getHeaderView(0);
        TextView Name = headerView.findViewById(R.id.name);
        TextView Email = headerView.findViewById(R.id.email);
        TextView Phone = headerView.findViewById(R.id.phone);
        ImageView photo = headerView.findViewById(R.id.photo);
      //  Name.setText(name);
        Email.setText(email);
        Phone.setText(phone);
        photo.setImageResource(R.drawable.master);

        drawerLayout = findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Fragment fragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.addground) {
                    startActivity(new Intent(getApplicationContext(), AddGround.class));
                    finish();
                } else if (itemId == R.id.viewground) {
                    startActivity(new Intent(getApplicationContext(), ViewGround.class));
                    finish();
                } else if (itemId == R.id.viewserver) {
                    startActivity(new Intent(getApplicationContext(), ViewServer.class));
                    finish();
                } else if (itemId == R.id.addserver) {
                   // startActivity(new Intent(getApplicationContext(), AddServer.class));
                   // finish();
                }
                return true;

            }
        });


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = new DashboardFragment();

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                String title = (String) item.getTitle();

                if (title.equals("Pending Request")) {
                    fragment = new FirstFragment();
                    toolbar.setTitle("Pending Request");
                } else if (title.equals("Ground Request")) {
                    fragment = new SecondFragment();
                    toolbar.setTitle("Ground Request");
                } else if (title.equals("Server Request")) {
                    fragment = new ThirdFragment();
                    toolbar.setTitle("Server Request");
                } else if (title.equals("Server Reply")) {
                    fragment = new ForthFragment();
                    toolbar.setTitle("Server Reply");
                } else if (title.equals("Ground Reply")) {
                    fragment = new FifthFragment();
                    toolbar.setTitle("Ground Reply");
                } else if (title.equals("Sign Out")) {
                    Toast.makeText(getApplicationContext(),"logging out",Toast.LENGTH_LONG).show();
                    logOut();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    public void logOut()
    {
        Toast.makeText(getApplicationContext(),"logging out",Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();

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
                          //   Toast.makeText(DashboardActivity.this,"inserted",Toast.LENGTH_SHORT).show();


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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

}