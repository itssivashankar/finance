package com.example.sample.ui.collection;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sample.MainActivity;
import com.example.sample.R;
import com.example.sample.client.HttpClient;
import com.example.sample.model.CollectionsResponse;
import com.example.sample.model.LoanResponse;
import com.example.sample.util.Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFragment extends Fragment {

    private CreateViewModel createViewModel;

    private MaterialButton employee, status, collectionDate, paidDate, createCollection,colletion_status;
    private MainActivity activity;
    private int employeeAlertCheckedItem = 0, statusAlertCheckedItem = 0;
    private MaterialButton borrower;
    private int borrowerAlertCheckedItem = 0;
    private ProgressBar loader;
    private TextInputEditText PrincipalAmount,RateOfInterest,LoanCollectionType,dweamount;
    private TextInputLayout PrincipalAmount_view,RateOfInterest_view,LoanCollectionType_view,dweamount_view;
    private String loan_id_create,loan_id_code;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createViewModel =
                new ViewModelProvider(this).get(CreateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_collection, container, false);
        activity = (MainActivity) getActivity();
        employee = root.findViewById(R.id.employee);
        loader = root.findViewById(R.id.progress);
        colletion_status = root.findViewById(R.id.colletion_status);
        PrincipalAmount_view = root.findViewById(R.id.PrincipalAmount_view);
        PrincipalAmount = root.findViewById(R.id.PrincipalAmount);
        dweamount_view = root.findViewById(R.id.dweamount_view);
        dweamount = root.findViewById(R.id.dweamount);
        RateOfInterest_view = root.findViewById(R.id.RateOfInterest_view);
        RateOfInterest = root.findViewById(R.id.RateOfInterest);
        LoanCollectionType_view = root.findViewById(R.id.LoanCollectionType_view);
        LoanCollectionType = root.findViewById(R.id.LoanCollectionType);

        colletion_status.setOnClickListener(v->{
            setcollectiostatus();
        });
        employee.setOnClickListener(v -> {
            employee();
        });
        borrower = root.findViewById(R.id.barrower_name);
        borrower.setOnClickListener(v -> {
            borrower();
        });
        status = root.findViewById(R.id.loan_id);
        status.setOnClickListener(v -> {
            loans();
        });
        collectionDate = root.findViewById(R.id.collection_date);
        collectionDate.setOnClickListener(v -> {
            collectionDate();
        });
        paidDate = root.findViewById(R.id.paid_date);
        paidDate.setOnClickListener(v -> {
            paidDate();
        });
        createCollection = root.findViewById(R.id.createCollection);
        createCollection.setOnClickListener(v -> {
            createCollection();
        });
        return root;
    }

    private void setcollectiostatus() {
        ArrayList<String> loan_status_l = new ArrayList<>();
        loan_status_l.add("Paid");
        AlertDialog.Builder loanstatusAlert = new AlertDialog.Builder(activity);
        loanstatusAlert.setCancelable(true);
        String[] branchArray = loan_status_l.toArray(new String[0]);
        loanstatusAlert.setSingleChoiceItems(branchArray, employeeAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                employeeAlertCheckedItem = which;
                colletion_status.setText(branchArray[which]);

                dialog.cancel();
            }
        });
        loanstatusAlert.show();
    }

    private void borrower() {
        AlertDialog.Builder employeeAlert = new AlertDialog.Builder(activity);
        employeeAlert.setCancelable(true);
        String[] branchArray = activity.getBorrowers();
        employeeAlert.setSingleChoiceItems(branchArray, borrowerAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrowerAlertCheckedItem = which;
                borrower.setText(branchArray[which]);
                dialog.cancel();
               // latestfun();
            }
        });
        employeeAlert.show();
    }

    private void employee() {
        AlertDialog.Builder employeeAlert = new AlertDialog.Builder(activity);
        employeeAlert.setCancelable(true);
        String[] branchArray = activity.getEmployee();
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
    private void latestfun(){
        ArrayList<String> loan_id_list = new ArrayList<>();
        ArrayList<LoanResponse.Loan> loanList = activity.getLoans();
        for (LoanResponse.Loan type : loanList) {
            if (type.getFirstname().equalsIgnoreCase(borrower.getText().toString())) {
                System.out.println("lllllllllllllllllllllllll 1"+type.getCollectiontyp());
                System.out.println("lllllllllllllllllllllllll 2"+Preference.getBranchName());
                System.out.println("lllllllllllllllllllllllll 3"+type.getLoanid());
                if (type.getLoanid().length() < 2)
                    type.setLoanid("0"+type.getLoanid());

                //String data = loan__type_info+Preference.getBranchName().substring(0,3)+type.getLoanid().toUpperCase();
               // System.out.println("lllllllllllllllllllllllll "+data);
             //   loan_id_list.add(data.toUpperCase());
                //loan_id_list.add(type.getLoancode());
//                System.out.println("type data"+type);
//                loan_id_create = type.getLoanid();
//                PrincipalAmount_view.setVisibility(View.VISIBLE);
//                //RateOfInterest_view.setVisibility(View.VISIBLE);
//                LoanCollectionType_view.setVisibility(View.VISIBLE);
//                PrincipalAmount.setText(type.getPrincipalamount());
//                //RateOfInterest.setText(type.ge);
//                LoanCollectionType.setText(type.getCollectiontyp());
            }
        }

    }

    private void loans() {
        AlertDialog.Builder statusAlert = new AlertDialog.Builder(activity);
        statusAlert.setCancelable(true);
        ArrayList<String> types = new ArrayList<>();
        ArrayList<LoanResponse.Loan> loanList = activity.getLoans();
        HashMap<String,LoanResponse.Loan> loanHashMap = new HashMap<>();
        HashMap<String,String> titleMap = new HashMap<>();
        for (LoanResponse.Loan type : loanList) {
            if (type.getFirstname().equalsIgnoreCase(borrower.getText().toString())) {
                String loan__type_info = null;
                if (type.getLoanid().length() < 2)
                    type.setLoanid("0"+type.getLoanid());
                if(type.getCollectiontyp().equals("Daily")){
                    loan__type_info = "DC";
                }
                if(type.getCollectiontyp().equals("Weekly")){
                    loan__type_info = "WK";
                }
                if(type.getCollectiontyp().equals("Interest")){
                    loan__type_info = "MO";
                }
                String data = loan__type_info+Preference.getBranchName().substring(0,3)+type.getLoanid();
                String showText = type.getLoancode()+"/"+data.toUpperCase();
                types.add(showText);
                titleMap.put(showText, type.getLoancode());
                loanHashMap.put(type.getLoancode(),type);
                System.out.println("type data"+type);
            }
        }

        types.toArray(new String[0]);
        String[] branchArray = types.toArray(new String[0]);
        statusAlert.setSingleChoiceItems(branchArray, statusAlertCheckedItem, (dialog, which) -> {
            statusAlertCheckedItem = which;
            status.setText(branchArray[which]);
            LoanResponse.Loan type = loanHashMap.get(titleMap.get(branchArray[which]));
            //getdueamount(type.getLoancode());
            if (type != null) {
                System.out.println("branchArray[which] "+branchArray[which].split("/")[1]);
                loan_id_create = type.getLoanid();
                loan_id_code = branchArray[which].split("/")[1];
                PrincipalAmount_view.setVisibility(View.VISIBLE);
                //RateOfInterest_view.setVisibility(View.VISIBLE);
                LoanCollectionType_view.setVisibility(View.VISIBLE);
                dweamount_view.setVisibility(View.VISIBLE);
                PrincipalAmount.setText(type.getPrincipalamount());
                dweamount.setText(type.getDueamount());
                //RateOfInterest.setText(type.ge);
                LoanCollectionType.setText(type.getCollectiontyp());
            }
            dialog.cancel();
        });
        statusAlert.show();
    }
//    private void getdueamount(String duw_id){
//        System.out.println("collection info 1 "+duw_id);
//        ArrayList<CollectionsResponse.Collection> collectionLists = activity.getCollectionLists();
//        for (CollectionsResponse.Collection collection : collectionLists){
//            if(collection.getLoancode().equals(duw_id)){
//                System.out.println("collection info 2 "+duw_id);
//                dweamount.setText(collection.getAmount());
//            }
//        }
//    }

    private void collectionDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            collectionDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, month+1, year));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void paidDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            paidDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, month+1, year));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void createCollection() {
        if (employee.getText().toString().equalsIgnoreCase("Select Employee")) {
            Toast.makeText(getActivity(),"Please Select employee", Toast.LENGTH_SHORT).show();
            return;
        }
        if (borrower.getText().toString().equalsIgnoreCase("Select Barrower")) {
            Toast.makeText(getActivity(),"Please Select borrower", Toast.LENGTH_SHORT).show();
            return;
        }
        if (status.getText().toString().equalsIgnoreCase("Select Loan NO")) {
            Toast.makeText(getActivity(),"Please Select Loan NO", Toast.LENGTH_SHORT).show();
            return;
        }
        if (collectionDate.getText().toString().equalsIgnoreCase("Collection Date")) {
            borrower.requestFocus();
            Toast.makeText(getActivity(),"Please Select collectionDate", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paidDate.getText().toString().equalsIgnoreCase("Paid Date")) {
            borrower.requestFocus();
            Toast.makeText(getActivity(),"Please Select paidDate", Toast.LENGTH_SHORT).show();
            return;
        }
        if (colletion_status.getText().toString().equalsIgnoreCase("Select Status")) {
            borrower.requestFocus();
            Toast.makeText(getActivity(),"Please Select Status", Toast.LENGTH_SHORT).show();
            return;
        }



        loader.setVisibility(View.VISIBLE);
        createCollection.setVisibility(View.GONE);
        JsonObject jsonObject = new JsonObject();
        System.out.println("status.getText().toString() "+status.getText().toString().split("/")[0]);
        jsonObject.addProperty("Loanid", loan_id_create);
        jsonObject.addProperty("Loancode", loan_id_code);
        jsonObject.addProperty("Branchid", Preference.getBranchID());
        jsonObject.addProperty("Employeeid", Preference.getEmployeeID());
        jsonObject.addProperty("Employeeidname", Preference.getEmployeeName());
        System.out.println("RESSSSSSL " + status.getText().toString() + Preference.getBranchID());
        HttpClient.createCollectionList(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("RESSSSSSL " + response.body());
                System.out.println("RESSSSSSL 1" + response.code());
                loader.setVisibility(View.GONE);
                createCollection.setVisibility(View.VISIBLE);
                redirect();
                Toast.makeText(getActivity(), "Collection Created!",
                        Toast.LENGTH_LONG).show();
                resetFields();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loader.setVisibility(View.GONE);
                createCollection.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Problem in Creating Collection!",
                        Toast.LENGTH_LONG).show();
                resetFields();
            }
        });
    }

    private void redirect() {
        activity.showCollectionList();
    }

    private void resetFields() {
        status.setText("Select Loan ID");
        statusAlertCheckedItem = 0;
        employee.setText("Select Employee");
        employeeAlertCheckedItem = 0;
    }
}