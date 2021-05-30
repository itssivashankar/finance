package com.example.sample.ui.borrower;

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
import com.example.sample.model.BorrowerResponse;
import com.example.sample.util.Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFragment extends Fragment {

    private CreateViewModel createViewModel;
    private ProgressBar loader;
    private MainActivity activity;
    private int employeeAlertCheckedItem = 0;
    private MaterialButton employee_status;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createViewModel = new ViewModelProvider(this).get(CreateViewModel.class);

        View root = inflater.inflate(R.layout.fragment_new_borrowers, container, false);
        activity = (MainActivity) getActivity();

        TextInputEditText firstName = root.findViewById(R.id.first_name);
        TextInputEditText fatherName = root.findViewById(R.id.father_name);
        TextInputEditText eMail = root.findViewById(R.id.e_mail);
        TextInputEditText mobileNumber = root.findViewById(R.id.mobile_number);
        TextInputEditText address = root.findViewById(R.id.address);
        TextInputEditText occupation = root.findViewById(R.id.occupation);
        TextInputEditText monthlyIncome = root.findViewById(R.id.monthly_income);
        TextInputEditText kycProof = root.findViewById(R.id.proof_no);
        TextInputEditText referenceName = root.findViewById(R.id.reference_name);
        TextInputEditText referenceMobile = root.findViewById(R.id.reference_mobile);
        loader = root.findViewById(R.id.progress);
        employee_status = root.findViewById(R.id.employee_status);

        final MaterialButton create = root.findViewById(R.id.createBorrower);

        employee_status.setOnClickListener(n->{
            employeesataus();
        });

        create.setOnClickListener(v -> {

            if (firstName.getText() == null || firstName.getText().toString().isEmpty()) {
                firstName.requestFocus();
                Toast.makeText(getActivity(),"Please enter firstName", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fatherName.getText() == null || fatherName.getText().toString().isEmpty()) {
                fatherName.requestFocus();
                Toast.makeText(getActivity(),"Please enter fatherName", Toast.LENGTH_SHORT).show();
                return;

            }
//            if (eMail.getText() == null || eMail.getText().toString().isEmpty()) {
//                eMail.requestFocus();
//                Toast.makeText(getActivity(),"Please enter eMail", Toast.LENGTH_SHORT).show();
//                return;
//
//            }
            if (mobileNumber.getText() == null || mobileNumber.getText().toString().isEmpty()) {
                mobileNumber.requestFocus();
                Toast.makeText(getActivity(),"Please enter mobileNumber", Toast.LENGTH_SHORT).show();
                return;

            }
            if (address.getText() == null || address.getText().toString().isEmpty()) {
                address.requestFocus();
                Toast.makeText(getActivity(),"Please enter address", Toast.LENGTH_SHORT).show();
                return;

            }
//            if (occupation.getText() == null || occupation.getText().toString().isEmpty()) {
//                occupation.requestFocus();
//                Toast.makeText(getActivity(),"Please enter occupation", Toast.LENGTH_SHORT).show();
//                return;
//
//            }
//            if (monthlyIncome.getText() == null || monthlyIncome.getText().toString().isEmpty()) {
//                monthlyIncome.requestFocus();
//                Toast.makeText(getActivity(),"Please enter monthlyIncome", Toast.LENGTH_SHORT).show();
//                return;
//
//            }
//            if (kycProof.getText() == null || kycProof.getText().toString().isEmpty()) {
//                kycProof.requestFocus();
//                Toast.makeText(getActivity(),"Please enter kycProof", Toast.LENGTH_SHORT).show();
//                return;
//
//            }
            if (referenceName.getText() == null || referenceName.getText().toString().isEmpty()) {
                referenceName.requestFocus();
                Toast.makeText(getActivity(),"Please enter referenceName", Toast.LENGTH_SHORT).show();
                return;

            }
            if (referenceMobile.getText() == null || referenceMobile.getText().toString().isEmpty()) {
                referenceMobile.requestFocus();
                Toast.makeText(getActivity(),"Please enter referenceMobile", Toast.LENGTH_SHORT).show();
                return;
            }

            create.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            BorrowerResponse.Borrower borrower = new BorrowerResponse.Borrower();
            borrower.setFirstName(firstName.getText().toString());
            borrower.setFatherName(fatherName.getText().toString());
            if(!eMail.getText().equals(null)) {
                borrower.setEmail(eMail.getText().toString());
            }else {
                borrower.setEmail("");
            }
            borrower.setMobile(mobileNumber.getText().toString());
            borrower.setAddress(address.getText().toString());
            if(!occupation.getText().equals(null)) {
                borrower.setOccupation(occupation.getText().toString());
            }else{
                borrower.setOccupation("");
            }
            borrower.setMonthlyIncome(monthlyIncome.getText().toString());
            if(!kycProof.getText().equals(null)) {
                borrower.setProof(kycProof.getText().toString());
            }else{
                borrower.setProof("");
            }
            borrower.setBranchid(Preference.getBranchID());

            borrower.setReferenceName(referenceName.getText().toString());
            borrower.setReferencMobile(referenceMobile.getText().toString());
            System.out.println("borrower borrower "+new Gson().toJson(borrower,BorrowerResponse.Borrower.class));
            HttpClient.createBorrower(borrower).enqueue(new Callback<BorrowerResponse.Borrower>() {
                @Override
                public void onResponse(Call<BorrowerResponse.Borrower> call, Response<BorrowerResponse.Borrower> response) {
                    create.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    emptyFields();
                    Toast.makeText(getActivity(), "Borrower Created ..!",
                            Toast.LENGTH_LONG).show();
                    System.out.println("response.body() " + response.body().toString());
                    System.out.println("response.body() " + response.code());
                    redrict();
                }

                @Override
                public void onFailure(Call<BorrowerResponse.Borrower> call, Throwable t) {
                    System.out.println("response.body() 1 " +t);
                    create.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    t.printStackTrace();
                    Toast.makeText(getActivity(), "Problem in Creating Borrower..!",
                            Toast.LENGTH_LONG).show();
                    emptyFields();

                }

                private void emptyFields() {
                    firstName.setText(null);
                    fatherName.setText(null);
                    eMail.setText(null);
                    mobileNumber.setText(null);
                    address.setText(null);
                    occupation.setText(null);
                    monthlyIncome.setText(null);
                    kycProof.setText(null);
                    referenceName.setText(null);
                    referenceMobile.setText(null);
                }
            });
            System.out.println("Borrower Model : " + borrower.toString());
        });

        return root;
    }

    private void redrict() {
        activity.showBarrowerList();
    }

    private void employeesataus() {
        ArrayList<String> loan_status = new ArrayList<>();
        loan_status.add("Active");
        loan_status.add("InActive");
        AlertDialog.Builder loanstatusAlert = new AlertDialog.Builder(activity);
        loanstatusAlert.setCancelable(true);
        String[] branchArray = loan_status.toArray(new String[0]);
        loanstatusAlert.setSingleChoiceItems(branchArray, employeeAlertCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                employeeAlertCheckedItem = which;
                employee_status.setText(branchArray[which]);
                dialog.cancel();
            }
        });
        loanstatusAlert.show();
    }
}