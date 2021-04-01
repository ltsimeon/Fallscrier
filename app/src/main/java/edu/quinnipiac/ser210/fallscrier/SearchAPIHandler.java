package edu.quinnipiac.ser210.fallscrier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
    private LinkedList<Bitmap> cardArts = new LinkedList<>();
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
        try { // Grab the JSON that the REST API returns matching the given search URL
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

        LinkedList<String> formattedCardList = new LinkedList<>();
        try { // Now that we have all that JSON, time to make it more legible.
            JSONObject returnedCards = new JSONObject(jsonBlob);
            int objectSize = Math.min(MAX_PAGE_SIZE,returnedCards.getInt("total_cards"));
            JSONArray returnedCardsArray = returnedCards.getJSONArray("data");
            for (int i = 0; i < objectSize; i++) {
                JSONObject card = returnedCardsArray.getJSONObject(i);
                if (card.has("card_faces")) { // Some cards' JSON representations actually contain multiple sides of the card,
                    // and need to be specially handled due to this.
                    JSONArray faces = card.getJSONArray("card_faces");
                    formattedCardList.add(parseCardJsonToString(faces.getJSONObject(0))); // Parse and add the front face,
                    formattedCardList.add(parseCardJsonToString(faces.getJSONObject(1))); // then parse and add the back face.
                } else
                    formattedCardList.add(parseCardJsonToString(card)); // But cards with just one face are easier.
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        shareText = formattedCardList.getFirst();
        return formattedCardList;
    }

    public String getShareText() { return shareText; }

    public String parseCardJsonToString(JSONObject card) throws JSONException, IOException {
        StringBuffer cardsb = new StringBuffer();
        JSONObject imageSources = card.getJSONObject("image_uris"); // Grab the various ways the image for the card can be accessed...
        URL imageUrl = new URL(imageSources.getString("art_crop")); // then take the one with just the art.
        InputStream in = imageUrl.openStream(); // Open a stream to the result,
        cardArts.add(BitmapFactory.decodeStream(in)); // then load it into the art list.
        in.close();
        cardsb.append(card.getString("name")+"\n"); // Here we build the string that represents the card text.
        if (card.getString("mana_cost") != "") cardsb.append(card.getString("mana_cost")+"\n"); // Don't add a line for nonexistent fields.
        cardsb.append(card.getString("type_line")+"\n"); // But every card has a typeline and rules text.
        cardsb.append(card.getString("oracle_text")+"\n");
        if (card.has("power")) cardsb.append(card.getString("power") + "/" + card.getString("toughness")+"\n");
        if (card.has("loyalty")) cardsb.append("Loyalty:"+card.getString("loyalty")+"\n"); // used only for planeswalkers.
        return cardsb.toString();
    }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        CardListAdapter cla = new CardListAdapter(context,strings,cardArts);
        rv.setAdapter(cla);
    }
}
