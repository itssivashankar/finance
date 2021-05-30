package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sample.client.HttpClient;
import com.example.sample.model.BorrowerResponse;
import com.example.sample.model.CollectionsResponse;
import com.example.sample.model.DashboardResponse;
import com.example.sample.model.EmployeeResponse;
import com.example.sample.model.LoanResponse;
import com.example.sample.ui.collection.ListViewModel;
import com.example.sample.ui.dashboard.DashboardViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener, NavigationView.OnNavigationItemSelectedListener {

    Listener listener;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private View fab;
    private ArrayList<BorrowerResponse.Borrower> borrowersList  = new ArrayList<>();
    private ArrayList<LoanResponse.Loan> loanList  = new ArrayList<>();
    private ArrayList<EmployeeResponse.Employee> employeeList  = new ArrayList<>();
    private ArrayList<CollectionsResponse.Collection> collectionLists = new ArrayList<>();
    private ArrayList<DashboardResponse.Dash> todayCollectionLists = new ArrayList<>();
    private ListViewModel collectionListModel;
    private com.example.sample.ui.borrower.ListViewModel borrowerListModel;
    private com.example.sample.ui.loan.ListViewModel loanListModel;
    private DashboardViewModel dashboardViewModel;
    private NavigationView navigationView;
    private SearchView searchview;
    public String TodaycollectionAmount;
    private MaterialButton totalamount;

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() == R.id.nav_dashboard) {
            fab.setVisibility(View.VISIBLE);
            totalamount.setVisibility(View.GONE);
        } else if(destination.getId() == R.id.nav_col_list){
            totalamount.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.GONE);
            totalamount.setVisibility(View.GONE);
        }
    }

    public void addListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setEnterTransition(new Explode());

        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_main);

        getEmployees();

        getBorrower();

        getLoanList();

        startActivityContents();
        gettotalamount();
        getCollection();
    }



    private void startActivityContents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        totalamount = findViewById(R.id.totalamount);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        searchview = findViewById(R.id.search_view);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                searchview.onActionViewCollapsed();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (dashboardViewModel != null) {
                    if (newText.equals("")) {
                        dashboardViewModel.updateData(todayCollectionLists);
                        return false;
                    }
                    ArrayList<DashboardResponse.Dash> filteredList = new ArrayList<>();
                    for (DashboardResponse.Dash collection: todayCollectionLists) {
                        if (collection.getFirstname().contains(newText)) {
                            filteredList.add(collection);
                        }
                    }
                    dashboardViewModel.updateData(filteredList);
                } else if (borrowerListModel != null) {
                    if (newText.equals("")) {
                        borrowerListModel.updateData(borrowersList);
                        return false;
                    }
                    ArrayList<BorrowerResponse.Borrower> filteredList = new ArrayList<>();
                    for (BorrowerResponse.Borrower borrower: borrowersList) {
                        if (borrower.getFirstName().contains(newText)) {
                            filteredList.add(borrower);
                        }
                    }
                    borrowerListModel.updateData(filteredList);
                } else if (collectionListModel != null) {
                    if (newText.equals("")) {
                        collectionListModel.updateData(collectionLists);
                        return false;
                    }
                    ArrayList<CollectionsResponse.Collection> filteredList = new ArrayList<>();
                    for (CollectionsResponse.Collection collection: collectionLists) {
                        if (collection.getFirstname().contains(newText)) {
                            filteredList.add(collection);
                        }
                    }
                    collectionListModel.updateData(filteredList);
                } else if (loanListModel != null) {
                    if (newText.equals("")) {
                        loanListModel.updateData(loanList);
                        return false;
                    }
                    ArrayList<LoanResponse.Loan> filteredList = new ArrayList<>();
                    for (LoanResponse.Loan loan: loanList) {
                        if (loan.getFirstname().contains(newText)) {
                            filteredList.add(loan);
                        }
                    }
                    loanListModel.updateData(filteredList);
                }
                return false;
            }
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_bor_create, R.id.nav_bor_list, R.id.nav_col_create, R.id.nav_col_list,R.id.nav_loan_create,R.id.nav_loan_list,R.id.nav_expenses_create,R.id.nav_expenses_list,R.id.nav_report_list)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void getEmployees() {
        HttpClient.getEmployees().enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                System.out.println("Responce :  " + response.body().toString());
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() ) {
                    setEmployeeList(response.body().getEmployeesList());
                } else {
                    getEmployees();
                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                getEmployees();
            }
        });
    }

    private void gettotalamount() {
        HttpClient.getTotalamount().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Responce : oo " + response.code());
                System.out.println("Responce : oo 1" + response.body());
                //System.out.println("Responce : oo1 " + response.body().getAsJsonArray("collectionlist").get(0).getAsJsonObject().get("totalcollection").getAsString());
                if(!response.body().getAsJsonArray("collectionlist").get(0).getAsJsonObject().get("totalcollection").isJsonNull()){
                    String amount = response.body().getAsJsonArray("collectionlist").get(0).getAsJsonObject().get("totalcollection").getAsString();
                    TodaycollectionAmount = amount;
                }else{
                    TodaycollectionAmount = "00.0";
                }

//                if(!amount.equals(null)){
//                    TodaycollectionAmount = "00.0";
//                }else {
//                    TodaycollectionAmount = amount;
//                }
                totalamount.setText("Total : "+TodaycollectionAmount);
                //totalamount.setVisibility(View.VISIBLE);
                //totalcollection
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void getLoanList() {
        HttpClient.getLoanList().enqueue(new Callback<LoanResponse>() {
            @Override
            public void onResponse(Call<LoanResponse> call, Response<LoanResponse> response) {
                System.out.println("Responce loan:  " + response.body().toString());
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() ) {
                    setLoanList(response.body().getLoantlist());
                } else {
                    getLoanList();
                }
            }

            @Override
            public void onFailure(Call<LoanResponse> call, Throwable t) {
                getLoanList();
            }
        });
    }

    private void setLoanList(ArrayList<LoanResponse.Loan> collectionType) {
        if (loanListModel != null)
            loanListModel.updateData(collectionType);
        this.loanList = collectionType;
    }

    private void setEmployeeList(ArrayList<EmployeeResponse.Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public ArrayList<LoanResponse.Loan> getLoans() {
        return loanList;
    }

    public String[] getEmployee() {
        ArrayList<String> employees = new ArrayList<>();
        for (EmployeeResponse.Employee type : employeeList) {
            employees.add(type.getUsername());
        }
        System.out.println("employeeList"+employeeList.toString());
        return employees.toArray(new String[0]);
    }

    public ArrayList<CollectionsResponse.Collection> getCollectionLists() {
        return collectionLists;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (navController != null) navController.addOnDestinationChangedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getBorrower() {
        HttpClient.getBorrowers().enqueue(new Callback<BorrowerResponse>() {
            @Override
            public void onResponse(Call<BorrowerResponse> call, Response<BorrowerResponse> response) {
                System.out.println("Responce :  " + response.code());
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() ) {
                    setBorrowersList(response.body().getBorrowersList());
                } else {
                    getBorrower();
                }
            }

            @Override
            public void onFailure(Call<BorrowerResponse> call, Throwable t) {
                System.out.println("Responce :  ");
                t.printStackTrace();
                getBorrower();
            }
        });
    }

    public void getCollection() {
        HttpClient.getCollectionList().enqueue(new Callback<CollectionsResponse>() {
            @Override
            public void onResponse(Call<CollectionsResponse> call, Response<CollectionsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() ) {
                    setCollectionList(response.body().getCollectionList());
                } else {
                    getCollection();
                }
            }

            @Override
            public void onFailure(Call<CollectionsResponse> call, Throwable t) {
                getCollection();
            }
        });
    }

    public void getTodayCollection() {
        HttpClient.getTodayCollectionList().enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() ) {
                    setTodayCollectionList(response.body().getEmployeesList());
                } else {
                    getTodayCollection();
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                getTodayCollection();
            }
        });
    }

    public ArrayList<BorrowerResponse.Borrower> getBorrowersList() {
        return borrowersList;
    }

    private void setBorrowersList(ArrayList<BorrowerResponse.Borrower> borrowersList) {
        if (borrowerListModel != null)
            borrowerListModel.updateData(borrowersList);
        this.borrowersList = borrowersList;
    }

    private void setTodayCollectionList(ArrayList<DashboardResponse.Dash> collectionLists) {
        if (dashboardViewModel != null)
            dashboardViewModel.updateData(collectionLists);
        this.todayCollectionLists = collectionLists;
    }

    private void setCollectionList(ArrayList<CollectionsResponse.Collection> collectionLists) {
        if (collectionListModel != null)
            collectionListModel.updateData(collectionLists);
        this.collectionLists = collectionLists;
    }

    @Override
    public void onBackPressed() {
        if (listener != null && listener.onBackPressed())
            return;
        super.onBackPressed();
    }

    public void setCollectionModel(ListViewModel listViewModel) {
        this.collectionListModel = listViewModel;
    }

    public void setBorrowerModel(com.example.sample.ui.borrower.ListViewModel listViewModel) {
        System.out.println("borrowerListModel "+borrowerListModel);
        this.borrowerListModel = listViewModel;
    }

    public void setLoanModel(com.example.sample.ui.loan.ListViewModel listViewModel) {
        this.loanListModel = listViewModel;
    }

    public void setDashboardModel(DashboardViewModel dashboardViewModel) {
        System.out.println("dashboardViewModel "+dashboardViewModel);
        this.dashboardViewModel = dashboardViewModel;
    }

    public String[] getBorrowers() {
        ArrayList<String> types = new ArrayList<>();
        for (BorrowerResponse.Borrower type : borrowersList) {
            types.add(type.getFirstName());
        }

        return types.toArray(new String[0]);
    }

    public String[] getLoanBorrowers() {
        ArrayList<String> types = new ArrayList<>();
        for (BorrowerResponse.Borrower type : borrowersList) {
            //types.add(type.getFirstName());
            types.add(type.getFirstName()+":"+type.getBorrowersId());
        }

        return types.toArray(new String[0]);
    }

    public void showLoanList() {
        System.out.println("navigationView.getMenu() " + navigationView.getMenu().size());
        navController.navigate(R.id.nav_loan_list);
//        navigationView.getMenu().findItem(R.id.nav_loan_list).setChecked(true);
    }
    public void showCollectionList() {
        System.out.println("navigationView.getMenu() 1 " + navigationView.getMenu().size());
        navController.navigate(R.id.nav_col_list);
//        navigationView.getMenu().findItem(R.id.nav_loan_list).setChecked(true);
    }
    public void showBarrowerList() {
        navController.navigate(R.id.nav_bor_list);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void showCreateCollection() {
        System.out.println("showCreateCollection");
        navController.navigate(R.id.nav_col_create);
    }

    public interface Listener {
        default boolean onBackPressed() {
            return false;
        }
    }
}