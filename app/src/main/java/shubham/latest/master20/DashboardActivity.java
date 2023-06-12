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
}