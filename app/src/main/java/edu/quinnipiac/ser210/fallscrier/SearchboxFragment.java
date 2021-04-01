package edu.quinnipiac.ser210.fallscrier;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SearchboxFragment extends Fragment {

    private OnSearchButtonPressedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_box, container, false);
        Button button = (Button) v.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.executeSearch(v);
            }
        });
        return v;
    }

    public interface OnSearchButtonPressedListener {
        void executeSearch(View view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchButtonPressedListener) {
            listener = (OnSearchButtonPressedListener) context;
        } else throw new ClassCastException(context.toString() + "can't handle the search button press.");
    }
}
