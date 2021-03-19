package edu.quinnipiac.ser210.fallscrier;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class SearchAPIHandler extends AsyncTask<String,Void,LinkedList<String>> {

    private static final int MAX_PAGE_SIZE = 175; // API returns only 175 cards at a time.
    private Context context;
    private RecyclerView rv;
    private String shareText;

    public SearchAPIHandler(Context context, RecyclerView rv) {
        this.context = context;
        this.rv = rv;
    }

    @Override
    protected LinkedList<String> doInBackground(String... strings) {

        String jsonBlob = null;
        URL url = null;
        try {
            url = new URL(null,"https://api.scryfall.com/cards/search?q="+strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(httpURLConnection.getInputStream());
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer results = new StringBuffer();
            String line;
            while((line = br.readLine())!= null){
                results.append(line);
            }
            jsonBlob = results.toString();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinkedList<String> formattedCardList = new LinkedList<String>();
        try {
            JSONObject returnedCards = new JSONObject(jsonBlob);
            int objectSize = Math.min(MAX_PAGE_SIZE,returnedCards.getInt("total_cards"));
            JSONArray returnedCardsArray = returnedCards.getJSONArray("data");
            for (int i = 0; i < objectSize; i++) {
                JSONObject card = returnedCardsArray.getJSONObject(i);
                StringBuffer cardsb = new StringBuffer();
                cardsb.append(card.getString("name")+"\n");
                if (card.getString("mana_cost") != "") cardsb.append(card.getString("mana_cost")+"\n");
                cardsb.append(card.getString("type_line")+"\n");
                cardsb.append(card.getString("oracle_text")+"\n");
                if (card.has("power")) cardsb.append(card.getString("power") + "/" + card.getString("toughness")+"\n");
                if (card.has("loyalty")) cardsb.append("Loyalty:"+card.getString("loyalty")+"\n");
                formattedCardList.add(cardsb.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        shareText = formattedCardList.getFirst();
        return formattedCardList;
    }

    public String getShareText() { return shareText; }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        CardListAdapter cla = new CardListAdapter(context,strings);
        rv.setAdapter(cla);
    }
}
