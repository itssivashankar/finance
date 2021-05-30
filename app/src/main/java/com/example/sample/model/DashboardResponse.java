package com.example.sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Locale;

public class DashboardResponse {

    @SerializedName("success")
    private Integer success;

    @SerializedName("dashborad")
    private ArrayList<DashboardResponse.Dash> dashboardList;

    public boolean getSuccess() {
        return success == 1;
    }

    public ArrayList<DashboardResponse.Dash> getEmployeesList() {
        return dashboardList;
    }

    public class Dash {

        @SerializedName("Loanno")
        private String loanno;
        @SerializedName("Branchname")
        private String branchname;
        @SerializedName("Collectiontype")
        private String collectiontype;
        @SerializedName("Firstname")
        private String firstname;
        @SerializedName("Mobile")
        private String mobile;
        @SerializedName("Amount")
        private String amount;
        @SerializedName("Dueamount")
        private Integer dueamount;
        @SerializedName("Startdate")
        private String startdate;
        @SerializedName("Enddate")
        private String enddate;

        public String getLoanno() {
            return loanno;
        }

        public void setLoanno(String loanno) {
            this.loanno = loanno;
        }

        public Dash withLoanno(String loanno) {
            this.loanno = loanno;
            return this;
        }

        public String getBranchname() {
            return branchname;
        }

        public void setBranchname(String branchname) {
            this.branchname = branchname;
        }

        public Dash withBranchname(String branchname) {
            this.branchname = branchname;
            return this;
        }

        public String getCollectiontype() {
            return collectiontype;
        }

        public void setCollectiontype(String collectiontype) {
            this.collectiontype = collectiontype;
        }

        public Dash withCollectiontype(String collectiontype) {
            this.collectiontype = collectiontype;
            return this;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public Dash withFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Dash withMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Dash withAmount(String amount) {
            this.amount = amount;
            return this;
        }

        public Integer getDueamount() {
            return dueamount;
        }

        public void setDueamount(Integer dueamount) {
            this.dueamount = dueamount;
        }

        public Dash withDueamount(Integer dueamount) {
            this.dueamount = dueamount;
            return this;
        }

        public String getStartdate() {
            String [] format = startdate.split("-");
            return String.format(Locale.getDefault(),"%s-%s-%s",format[2],format[1],format[0]);
            //return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public Dash withStartdate(String startdate) {
            this.startdate = startdate;
            return this;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public Dash withEnddate(String enddate) {
            this.enddate = enddate;
            return this;
        }
    }
}
