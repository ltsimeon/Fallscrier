package edu.quinnipiac.ser210.fallscrier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

public class SearchResultActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private String query;
    private SearchAPIHandler searchAPIHandler;
    private RecyclerView rv;
    private RecyclerView.Adapter rva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setSupportActionBar(findViewById(R.id.toolbar_results));
        constraintLayout = findViewById(R.id.results_layout);
        constraintLayout.setBackgroundColor(getResources().getColor(MainActivity.colors[MainActivity.colorPointer])); // so search results have same BG color

        rv = (RecyclerView) findViewById(R.id.results_recycler);

        // And now the REST magic begins.
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
                constraintLayout.setBackgroundColor(getResources().getColor(MainActivity.colors[MainActivity.colorPointer]));
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
