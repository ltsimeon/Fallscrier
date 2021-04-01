package edu.quinnipiac.ser210.fallscrier;

// Absolutely lifted this code directly from LS07 slide.

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {
    private final LinkedList<String> mCardList;
    private LayoutInflater mInflater;
    private LinkedList<Bitmap> imageList;

    public CardListAdapter(Context context, LinkedList<String> CardList, LinkedList<Bitmap> imageList) {
        mInflater = LayoutInflater.from(context);
        this.mCardList = CardList;
        this.imageList = imageList;
    }
    @Override
    public CardListAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.resultsrecycler_item,
                parent, false);
        return new CardViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder( CardListAdapter.CardViewHolder holder, int position) {
        // Retrieve the data for that position.
        String mTextCurrent = mCardList.get(position);
        Bitmap mThumbCurrent = imageList.get(position);
        // Add the text to the card view's contained text view...
        ((TextView) holder.CardItemView.findViewById(R.id.formatted_card_text)).setText(mTextCurrent);
        // then do the same for the image.
        ((ImageView) holder.CardItemView.findViewById(R.id.card_thumbnail)).setImageBitmap(mThumbCurrent);
    }

    @Override
    public int getItemCount() {
        return  mCardList.size();
    }

     class CardViewHolder extends RecyclerView.ViewHolder {
         public CardView CardItemView;
         CardListAdapter mAdapter;

         public CardViewHolder(View itemView, CardListAdapter adapter) {
             super(itemView);
             CardItemView = itemView.findViewById(R.id.card_in_view);
             this.mAdapter = adapter;
         }
    }
}
