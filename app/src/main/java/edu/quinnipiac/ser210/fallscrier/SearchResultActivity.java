package edu.quinnipiac.ser210.fallscrier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.LinkedList;

public class SearchResultActivity extends AppCompatActivity {

    private String query;
    private SearchAPIHandler searchAPIHandler;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setSupportActionBar(findViewById(R.id.toolbar_results));

        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_result);
        rv = (RecyclerView) fragment.getView();
        rv.setBackgroundColor(getResources().getColor(MainActivity.colors[MainActivity.colorPointer]));

        // Call upon the Scryfall API itself to handle our search query
        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        searchAPIHandler = new SearchAPIHandler(getApplicationContext(),rv);
        searchAPIHandler.execute(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.colorItem:
                MainActivity.colorPointer = (MainActivity.colorPointer+1)%5;
                rv.setBackgroundColor(getResources().getColor(MainActivity.colors[MainActivity.colorPointer]));
                return true;
            case R.id.aboutItem:
                Toast.makeText(this,R.string.about,Toast.LENGTH_LONG).show();
                return true;
            case R.id.shareItem:
                String shareText = searchAPIHandler.getShareText();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                shareIntent.setType("text/plain");
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
