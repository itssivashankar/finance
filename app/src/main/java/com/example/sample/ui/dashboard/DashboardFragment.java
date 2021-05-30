package com.example.sample.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sample.MainActivity;
import com.example.sample.R;
import com.example.sample.model.CollectionsResponse;
import com.example.sample.model.DashboardResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private RecyclerListAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private MainActivity activity;
    private SwipeRefreshLayout refresh;
    Observer<ArrayList<DashboardResponse.Dash>> userListUpdateObserver = new Observer<ArrayList<DashboardResponse.Dash>>() {
        @Override
        public void onChanged(ArrayList<DashboardResponse.Dash> userArrayList) {
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
        activity = ((MainActivity) getActivity());
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        activity.setDashboardModel(dashboardViewModel);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        dashboardViewModel.getStringLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        refresh = root.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(() -> activity.getTodayCollection());
        System.out.println("onCreateView Dashboard");
        activity.getTodayCollection();
        return root;
    }

    @Override
    public void onPause() {
        System.out.println("onPause Dashboard");
        if (activity != null) activity.setDashboardModel(null);
        super.onPause();
    }

    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ListViewHolder> {

        final Activity context;
        final ArrayList<DashboardResponse.Dash> userArrayList;

        public RecyclerListAdapter(Activity context, ArrayList<DashboardResponse.Dash> userArrayList) {
            this.userArrayList = userArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerListAdapter.ListViewHolder(
                    LayoutInflater.from(
                            parent.getContext()
                    ).inflate(
                            R.layout.list_item_collection,
                            parent,
                            false
                    )
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerListAdapter.ListViewHolder holder, int position) {
            DashboardResponse.Dash listValue = userArrayList.get(position);
            System.out.println("listValue " + listValue.getFirstname());
            holder.userName.setText(listValue.getFirstname()+'/'+listValue.getLoanno());
            holder.circleName.setText(String.valueOf(listValue.getFirstname().charAt(0)).toUpperCase());
            holder.pendingAmount.setText(String.format(Locale.getDefault(), "Amount - %s", listValue.getAmount()));
            holder.phonenumber.setText("Phone :"+listValue.getMobile());
            holder.dweamount.setText("DueAmount:"+listValue.getDueamount());
            holder.dwedate.setText(listValue.getStartdate());
            holder.itemCard.setOnClickListener(n->{
                activity.showCreateCollection();
            });
        }

        @Override
        public int getItemCount() {
            if (userArrayList == null)
                return 0;
            return userArrayList.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            private final TextView userName, circleName, pendingAmount,phonenumber,dweamount,dwedate;
            private final CardView itemCard;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                circleName = itemView.findViewById(R.id.circleName);
                pendingAmount = itemView.findViewById(R.id.userMobile);
                phonenumber = itemView.findViewById(R.id.phonenumber);
                dweamount = itemView.findViewById(R.id.dweamount);
                dwedate = itemView.findViewById(R.id.dwedate);
                itemCard = itemView.findViewById(R.id.itemCard);
            }
        }
    }

    private void redirect() {
        activity.showCreateCollection();
    }
}