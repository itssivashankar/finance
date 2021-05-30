package com.example.sample.ui.loan;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sample.MainActivity;
import com.example.sample.R;
import com.example.sample.client.HttpClient;
import com.example.sample.model.LoanDetail;
import com.example.sample.model.LoanResponse;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanListFragment extends Fragment implements LifecycleOwner, MainActivity.Listener {

    private ListViewModel listViewModel;
    private com.example.sample.ui.loan.LoanListFragment.RecyclerListAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private MainActivity activity;
    private SwipeRefreshLayout refresh;
    private LoanListFragment fragment;
    private ProgressBar progressBar;
    private View loanDetail;
    private MaterialTextView principalAmount,Initiatedamount,Collectiontyp,Pendinginstallments,Amount,Balance,EndDate,Startdate;
    Observer<ArrayList<LoanResponse.Loan>> userListUpdateObserver = new Observer<ArrayList<LoanResponse.Loan>>() {
        @Override
        public void onChanged(ArrayList<LoanResponse.Loan> userArrayList) {
            Collections.reverse(userArrayList);
            recyclerViewAdapter = new RecyclerListAdapter(getActivity(), userArrayList, fragment);
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
        if (activity != null) {
            activity.addListener(this);
        }
        fragment = this;
        View root = inflater.inflate(R.layout.fragment_loan_list, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progress_bar);
        loanDetail = root.findViewById(R.id.loan_detail);
        principalAmount = root.findViewById(R.id.principal_amount);
        Initiatedamount = root.findViewById(R.id.Initiatedamount);
        Collectiontyp = root.findViewById(R.id.Collectiontyp);
        Balance = root.findViewById(R.id.Balance);
        Amount = root.findViewById(R.id.Amount);
        Startdate = root.findViewById(R.id.StartDate);
        EndDate = root.findViewById(R.id.EndDate);
        Pendinginstallments = root.findViewById(R.id.Pendinginstallments);
        activity.setLoanModel(listViewModel);
        listViewModel.getStringLiveData().observe(getViewLifecycleOwner(), userListUpdateObserver);
        activity.getLoanList();
        refresh = root.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(() -> activity.getLoanList());

        return root;
    }

    @Override
    public void onPause() {
        if (activity != null) activity.setLoanModel(null);
        super.onPause();
    }

    private static class RecyclerListAdapter extends RecyclerView.Adapter<com.example.sample.ui.loan.LoanListFragment.RecyclerListAdapter.ListViewHolder> {

        final Activity context;
        final ArrayList<LoanResponse.Loan> userArrayList;
        private final LoanListFragment fragment;

        public RecyclerListAdapter(Activity context, ArrayList<LoanResponse.Loan> userArrayList, LoanListFragment fragment) {
            this.userArrayList = userArrayList;
            this.context = context;
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public com.example.sample.ui.loan.LoanListFragment.RecyclerListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new com.example.sample.ui.loan.LoanListFragment.RecyclerListAdapter.ListViewHolder(
                    LayoutInflater.from(
                            parent.getContext()
                    ).inflate(
                            R.layout.loanlistlayout,
                            parent,
                            false
                    )
            );
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.sample.ui.loan.LoanListFragment.RecyclerListAdapter.ListViewHolder holder, int position) {
            LoanResponse.Loan listValue = userArrayList.get(position);
            System.out.println("listValue data " + listValue);
            holder.userName.setText(listValue.getFirstname()+":"+listValue.getLoancode());
            holder.circleName.setText(String.valueOf(listValue.getFirstname().charAt(0)).toUpperCase());
            holder.pendingAmount.setText(listValue.getPrincipalamount());
            holder.viewinfo.setOnClickListener(v->{
                System.out.println("listValue.getCollectiontyp() "+listValue.getCollectiontyp());
                String id = null;
                if(listValue.getCollectiontyp().equals("Daily")){
                    id="1";
                }
                if(listValue.getCollectiontyp().equals("Weekly")){
                    id="2";
                }
                if(listValue.getCollectiontyp().equals("Interest")){
                    id="4";
                }
                JsonObject info = new JsonObject();
                info.addProperty("Collectiontypeid",id);
                info.addProperty("Loanid",listValue.getLoanid());
                System.out.println("response lll "+info.toString());
                fragment.loanDetailRequest(info);
            });
        }

        @Override
        public int getItemCount() {
            if (userArrayList == null)
                return 0;
            return userArrayList.size();
        }

        public static class ListViewHolder extends RecyclerView.ViewHolder {

            private final TextView userName, circleName, pendingAmount;
            private final CardView itemCard;
            private final ImageView viewinfo;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                circleName = itemView.findViewById(R.id.circleName);
                pendingAmount = itemView.findViewById(R.id.userMobile);
                itemCard = itemView.findViewById(R.id.itemCard);
                viewinfo = itemView.findViewById(R.id.viewinfo);
            }
        }
    }

    private void loanDetailRequest(JsonObject info) {

        progressBar.setVisibility(View.VISIBLE);
        HttpClient.getloaninfo(info).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    System.out.println("response.body() 2 "+response.body().toString());
                    if (response.body() != null && response.body().getAsJsonArray("loanlist").size() > 0)
                        loanDetailResponse(new Gson().fromJson(response.body().getAsJsonArray("loanlist").get(0).getAsJsonObject(),LoanDetail.class));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void loanDetailResponse(LoanDetail loan) {
        progressBar.setVisibility(View.GONE);
        loanDetail.setVisibility(View.VISIBLE);
        principalAmount.setText(loan.getPrincipalamount());
        Initiatedamount.setText(loan.getInitiatedamount());
        Collectiontyp.setText(loan.getCollectiontyp());
        Pendinginstallments.setText(loan.getPendinginstallments());
        Amount.setText(loan.getAmount());
        Balance.setText(loan.balance);
        if(loan.stratdate!=null) {
            String[] date_one = loan.stratdate.split("-");
            String[] date_two = loan.enddate.split("-");
            Startdate.setText(date_one[2] + "-" + date_one[1] + "-" + date_one[0]);
            EndDate.setText(date_two[2] + "-" + date_two[1] + "-" + date_two[0]);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (loanDetail.getVisibility() == View.VISIBLE) {
            loanDetail.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }
}