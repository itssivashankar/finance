package com.example.sample.ui.loan;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sample.MainActivity;
import com.example.sample.R;
import com.example.sample.client.HttpClient;
import com.example.sample.model.BorrowerResponse;
import com.example.sample.ui.borrower.CreateViewModel;
import com.example.sample.util.Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoanFragment extends Fragment {


    private MainActivity activity;
    private CreateViewModel createViewModel;
    private MaterialButton employee, loan_type, barrower_name, loan_status, createloan;
    private TextInputEditText principal_amount,service_charge,initiated_amount,estimation_month,interest_percentage;
    private int employeeAlertCheckedItem = 0;
    private String[] toArray = new String[]{};
    private int borrowerAlertCheckedItem = 0;
    private ProgressBar loader;
    private String barroower_id = null;
    private View root;
    private final String DAILY = "Daily";
    private final String WEEKLY = "Weekly";
    private final String INTEREST = "Interest";
    private int send_id=10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createViewModel = new ViewModelProvider(this).get(CreateViewModel.class);

        root = inflater.inflate(R.layout.fragment_loan, container, false);
        activity = (MainActivity) getActivity();
        employee = root.findViewById(R.id.employee);
        loan_type = root.findViewById(R.id.loan_type);
        barrower_name = root.findViewById(R.id.barrower_name);
        principal_amount = root.findViewById(R.id.principal_amount);
        service_charge = root.findViewById(R.id.service_charge);
        initiated_amount = root.findViewById(R.id.initiated_amount);
        interest_percentage  = root.findViewById(R.id.interest_percentage);
        estimation_month = root.findViewById(R.id.estimation_month);
        loan_status = root.findViewById(R.id.loan_status);
        createloan = root.findViewById(R.id.createloan);
        loader = root.findViewById(R.id.progress);

        principal_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) {
                    initiated_amount.setText(null);
                    service_charge.setText(null);
                    initiated_amount.setText(null);
                    return;
                }

                if (loan_type.getText().toString().equalsIgnoreCase(INTEREST)) {
                    initiated_amount.setText(s.toString());
                    return;
                }

                if (loan_type.getText().toString().equalsIgnoreCase(DAILY)) {
                    int pAmount = Integer.parseInt(s.toString());
                    service_charge.setText(String.valueOf((pAmount*12)/100));
                    initiated_amount.setText(String.valueOf(pAmount - ((pAmount*12)/100)));
                    return;
                }

                if (loan_type.getText().toString().equalsIgnoreCase(WEEKLY)) {
                    int pAmount = Integer.parseInt(s.toString());
                    service_charge.setText(String.valueOf((pAmount*5)/100));
                    initiated_amount.setText(String.valueOf(pAmount - ((pAmount*5)/100)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getBorrower();

        createloan.setOnClickListener(n->{
            createloan();
        });
        employee.setOnClickListener(v -> {
            employee();
        });

        barrower_name.setOnClickListener(n->{
            showBorrower();
        });
        loan_type.setOnClickListener(v->{
            loanType();
        });
        loan_status.setOnClickListener(n->{
            loanstaus();
        });
        return root;
    }

    private void showBorrower() {
        AlertDialog.Builder employeeAlert = new AlertDialog.Builder(activity);
        employeeAlert.setCancelable(true);
        String[] branchArray = activity.getLoanBorrowers();
        employeeAlert.setSingleChoiceItems(branchArray, borrowerAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrowerAlertCheckedItem = which;
                String[] item = branchArray[which].split(":");
                barrower_name.setText(item[0]);
                barroower_id = item[1];
                dialog.cancel();
            }
        });
        employeeAlert.show();
    }

    private void getBorrower() {
        HttpClient.getBorrowers().enqueue(new Callback<BorrowerResponse>() {
            @Override
            public void onResponse(Call<BorrowerResponse> call, Response<BorrowerResponse> response) {
                System.out.println("Loan Responce :  " + response.code());

                if (response.body() != null) {
                    ArrayList<String> sample = new ArrayList<>();
                    for (BorrowerResponse.Borrower test : response.body().getBorrowersList()) {
                        sample.add(test.getBorrowersId());
                    }
                    setBorrowerIds(sample.toArray(new String[0]));
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

    private void setBorrowerIds(String[] toArray) {
        this.toArray = toArray;
    }


    private void loanstaus() {
        ArrayList<String> loan_status_l = new ArrayList<>();
        loan_status_l.add("Open");
        loan_status_l.add("Closed");
        AlertDialog.Builder loanstatusAlert = new AlertDialog.Builder(activity);
        loanstatusAlert.setCancelable(true);
        String[] branchArray = loan_status_l.toArray(new String[0]);
        loanstatusAlert.setSingleChoiceItems(branchArray, employeeAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                employeeAlertCheckedItem = which;
                loan_status.setText(branchArray[which]);
                System.out.println("responsetynhnjhnh 11"+loan_status.getText().toString());
//                if(loan_status.getText().toString().equals(DAILY)){
//                    System.out.println("responsetynhnjhnh DAILY"+loan_status.getText().toString());
//                    send_id = 1;
//                }
//                if(loan_status.getText().toString().equals(WEEKLY)){
//                    System.out.println("responsetynhnjhnh WEEKLY"+loan_status.getText().toString());
//                    send_id = 2;
//                }
//                if(loan_status.getText().toString().equals(INTEREST)){
//                    System.out.println("responsetynhnjhnh INTEREST"+loan_status.getText().toString());
//                    send_id = 4;
//                }

                dialog.cancel();
            }
        });
        loanstatusAlert.show();
    }

    private void loanType() {
        ArrayList<String> loan_type_l = new ArrayList<>();
        loan_type_l.add(DAILY);
        loan_type_l.add(WEEKLY);
        loan_type_l.add(INTEREST);
        AlertDialog.Builder employeeAlert = new AlertDialog.Builder(activity);
        employeeAlert.setCancelable(true);
        String[] branchArray = loan_type_l.toArray(new String[0]);
        employeeAlert.setSingleChoiceItems(branchArray, employeeAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initiated_amount.setText(null);
                service_charge.setText(null);
                initiated_amount.setText(null);
                principal_amount.setText(null);
                employeeAlertCheckedItem = which;
                loan_type.setText(branchArray[which]);
                 if(loan_type.getText().toString().equals(DAILY)){
                    System.out.println("responsetynhnjhnh DAILY"+loan_status.getText().toString());
                    send_id = 1;
                }
                if(loan_type.getText().toString().equals(WEEKLY)){
                    System.out.println("responsetynhnjhnh WEEKLY"+loan_status.getText().toString());
                    send_id = 2;
                }
                if(loan_type.getText().toString().equals(INTEREST)){
                    System.out.println("responsetynhnjhnh INTEREST"+loan_status.getText().toString());
                    send_id = 4;
                }
                if(!loan_type.getText().toString().equalsIgnoreCase(INTEREST)){
                    root.findViewById(R.id.service_charge_layout).setVisibility(View.VISIBLE);

                    root.findViewById(R.id.estimation_month_layout).setVisibility(View.GONE);
                    root.findViewById(R.id.interest_percentage_layout).setVisibility(View.GONE);
                } else {
                    root.findViewById(R.id.service_charge_layout).setVisibility(View.GONE);

                    root.findViewById(R.id.estimation_month_layout).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.interest_percentage_layout).setVisibility(View.VISIBLE);
                }
                dialog.cancel();
            }
        });
        employeeAlert.show();
    }

    private void employee() {
        AlertDialog.Builder employeeAlert = new AlertDialog.Builder(activity);
        employeeAlert.setCancelable(true);
        String[] branchArray = activity.getEmployee();
        System.out.println("branchArray "+branchArray.toString());
        employeeAlert.setSingleChoiceItems(branchArray, employeeAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                employeeAlertCheckedItem = which;
                employee.setText(branchArray[which]);
                dialog.cancel();
            }
        });
        employeeAlert.show();
    }

    private void borrower() {
        AlertDialog.Builder employeeAlert = new AlertDialog.Builder(activity);
        employeeAlert.setCancelable(true);
        String[] branchArray = activity.getEmployee();
        employeeAlert.setSingleChoiceItems(branchArray, employeeAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                employeeAlertCheckedItem = which;
                barrower_name.setText(branchArray[which]);
                dialog.cancel();
            }
        });
        employeeAlert.show();
    }
    private void resetFields(){
        principal_amount.setText(null);
        service_charge.setText(null);
        initiated_amount.setText(null);
        estimation_month.setText(null);
    }
    private void createloan() {
        if (principal_amount.getText() == null || principal_amount.getText().toString().isEmpty()) {
            principal_amount.requestFocus();
            Toast.makeText(getActivity(),"Please enter principal_amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loan_status.getText() == null || loan_status.getText().toString().isEmpty()) {
            estimation_month.requestFocus();
            Toast.makeText(getActivity(),"Please select loan Status", Toast.LENGTH_SHORT).show();
            return;
        }
        createloan.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Branchid",Preference.getBranchID());
        jsonObject.addProperty("Borrowrsid",barroower_id);
        jsonObject.addProperty("Employeeid",Preference.getEmployeeID());
        jsonObject.addProperty("Collectiontype",String.valueOf(send_id));
        //jsonObject.addProperty("Loancode","DCBAN27");
        jsonObject.addProperty("principalamount",principal_amount.getText().toString());
        //jsonObject.addProperty("Loanstatus",loan_status.getText().toString());
        if(loan_type.getText().toString().equals(INTEREST)){
            jsonObject.addProperty("Interest",interest_percentage.getText().toString());
            jsonObject.addProperty("Estimationmonth",estimation_month.getText().toString());
        }
        System.out.println("responsetynhnjhnh 1"+String.valueOf(send_id));
        System.out.println("responsetynhnjhnh 0"+jsonObject);

//        JsonObject jsonObject = new Gson().fromJson("{\"Borrowrsid\":"+barroower_id+",\"Employeeid\":"+Preference.getEmployeeID()+",\"Branchid\":"+Preference.getBranchID()+",\"Collectiontype\":\"1\",\"principalamount\":"+principal_amount.getText().toString()+"}",JsonObject.class);
//        jsonObject.addProperty("service_charge", service_charge.getText().toString());


        HttpClient.createLoan(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("responsetynhnjhnh "+response.body());
                System.out.println("responsetynhnjhnh "+response.code());
                createloan.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                redirect();
                Toast.makeText(getActivity(), "Loan Created ..!",
                        Toast.LENGTH_LONG).show();
                System.out.println("RESSSSSS " + response.body());
                resetFields();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                System.out.println("RESSSSSS " +t);
                createloan.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Problem in Loan Creation ..!",
                        Toast.LENGTH_LONG).show();
                resetFields();
            }
        });
    }

    private void redirect() {
        activity.showLoanList();
    }
}