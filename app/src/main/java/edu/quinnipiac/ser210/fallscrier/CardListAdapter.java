package edu.quinnipiac.ser210.fallscrier;

// Absolutely lifted this code directly from LS07 slide.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {
    private final LinkedList<String> mCardList;
    private LayoutInflater mInflater;

    public CardListAdapter(Context context, LinkedList<String> CardList) {
        mInflater = LayoutInflater.from(context);
        this.mCardList = CardList;
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
        String mCurrent = mCardList.get(position);
        // Add the data to the view holder.
        holder.CardItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return  mCardList.size();
    }

     class CardViewHolder extends RecyclerView.ViewHolder {
         public TextView CardItemView;
         CardListAdapter mAdapter;

         public CardViewHolder(View itemView, CardListAdapter adapter) {
             super(itemView);
             CardItemView = itemView.findViewById(R.id.formatted_card);
             this.mAdapter = adapter;
         }
    }
}
