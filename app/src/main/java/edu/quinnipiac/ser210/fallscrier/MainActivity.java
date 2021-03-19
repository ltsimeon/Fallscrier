package edu.quinnipiac.ser210.fallscrier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static int colorPointer = 0;
    public static int[] colors = new int[] {R.color.mtg_white,R.color.mtg_blue,R.color.mtg_black,R.color.mtg_red,R.color.mtg_green};
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        constraintLayout = findViewById(R.id.main_layout);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.mtg_white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void executeSearch(View view) {
        Intent intent = new Intent(this,SearchResultActivity.class);
        intent.putExtra("query",((EditText) findViewById(R.id.search_box)).getText().toString());
        // Gotta send along the search query to make the results activity work
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.colorItem:
                colorPointer = (colorPointer+1)%5;
                constraintLayout.setBackgroundColor(getResources().getColor(colors[colorPointer]));
                return true;
            case R.id.aboutItem:
                Toast.makeText(this,R.string.about,Toast.LENGTH_LONG).show();
                return true;
                // this page, the share button does nothing. there's nothing to share yet
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}