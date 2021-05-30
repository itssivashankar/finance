package com.example.sample.ui.collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sample.MainActivity;
import com.example.sample.R;
import com.example.sample.model.CollectionsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ListFragment extends Fragment implements LifecycleOwner {

    private ListViewModel listViewModel;
    private RecyclerListAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private MainActivity activity;
    private SwipeRefreshLayout refresh;
    Observer<ArrayList<CollectionsResponse.Collection>> userListUpdateObserver = new Observer<ArrayList<CollectionsResponse.Collection>>() {
        @Override
        public void onChanged(ArrayList<CollectionsResponse.Collection> userArrayList) {
            Collections.reverse(userArrayList);
            recyclerViewAdapter = new RecyclerListAdapter(getActivity(), userArrayList);
            refresh.setRefreshing(false);
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(verticalLayoutManager);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);
        activity = ((MainActivity) getActivity());
        View root = inflater.inflate(R.layout.fragment_list_collections, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        activity.setCollectionModel(listViewModel);
        listViewModel.getStringLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        activity.getCollection();
        refresh = root.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(() -> activity.getCollection());

        return root;
    }

    @Override
    public void onPause() {
        if (activity != null) activity.setCollectionModel(null);
        super.onPause();
    }

    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ListViewHolder> {

        final Activity context;
        final ArrayList<CollectionsResponse.Collection> userArrayList;

        public RecyclerListAdapter(Activity context, ArrayList<CollectionsResponse.Collection> userArrayList) {
            this.userArrayList = userArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListViewHolder(
                    LayoutInflater.from(
                            parent.getContext()
                    ).inflate(
                            R.layout.collection_list_details,
                            parent,
                            false
                    )
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerListAdapter.ListViewHolder holder, int position) {
            CollectionsResponse.Collection listValue = userArrayList.get(position);
            System.out.println("listValue " + listValue.getFirstname());
            holder.userName.setText(listValue.getFirstname());
            holder.circleName.setText(String.valueOf(listValue.getFirstname().charAt(0)).toUpperCase());
            holder.pendingAmount.setText(String.format(Locale.getDefault(), "Amount - %s", listValue.getAmount()));
            holder.date_value.setText(listValue.getCollectiondate());
        }

        @Override
        public int getItemCount() {
            if (userArrayList == null)
                return 0;
            return userArrayList.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            private final TextView userName, circleName, pendingAmount,date_value;
            private final CardView itemCard;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                circleName = itemView.findViewById(R.id.circleName);
                pendingAmount = itemView.findViewById(R.id.userMobile);
                date_value = itemView.findViewById(R.id.date_value);
                itemCard = itemView.findViewById(R.id.itemCard);
            }
        }
    }
}