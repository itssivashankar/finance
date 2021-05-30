package com.example.sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoanDetail {
    @SerializedName("Collectiondate")
    public String collectiondate;
    @SerializedName("Loancode")
    public String loancode;
    @SerializedName("Principalamount")
    public String principalamount;
    @SerializedName("Initiatedamount")
    public String initiatedamount;
    @SerializedName("Intrest")
    public String intrest;
    @SerializedName("Employeeid")
    public String employeeid;
    @SerializedName("Employeecod")
    public Object employeecod;
    @SerializedName("Employeename")
    public Object employeename;
    @SerializedName("Branchname")
    public Object branchname;
    @SerializedName("Borrowerscode")
    public Object borrowerscode;
    @SerializedName("Firstname")
    public Object firstname;
    @SerializedName("Collectiontyp")
    public String collectiontyp;
    @SerializedName("Noofdays")
    public String noofdays;
    @SerializedName("Pendinginstallments")
    public String pendinginstallments;
    @SerializedName("Amount")
    public String amount;
    @SerializedName("Balance")
    public String balance;
    @SerializedName("Collection")
    public String collection;
    @SerializedName("Stratdate")
    public String stratdate;
    @SerializedName("Enddate")
    public String enddate;

    public String getCollectiondate() {
        return this.collectiondate;
    }

    public void setCollectiondate(String collectiondate) {
        this.collectiondate = collectiondate;
    }

    public String getLoancode() {
        return this.loancode;
    }

    public void setLoancode(String loancode) {
        this.loancode = loancode;
    }

    public String getPrincipalamount() {
        return this.principalamount;
    }

    public void setPrincipalamount(String principalamount) {
        this.principalamount = principalamount;
    }

    public String getInitiatedamount() {
        return this.initiatedamount;
    }

    public void setInitiatedamount(String initiatedamount) {
        this.initiatedamount = initiatedamount;
    }
    public String getStratdate() {
        return this.stratdate;
    }

    public void setStratdate(String stratdate) {
        this.stratdate = stratdate;
    }

    public String getEnddate() {
        return this.enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getIntrest() {
        return this.intrest;
    }

    public void setIntrest(String intrest) {
        this.intrest = intrest;
    }

    public String getEmployeeid() {
        return this.employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public Object getEmployeecod() {
        return this.employeecod;
    }

    public void setEmployeecod(Object employeecod) {
        this.employeecod = employeecod;
    }

    public Object getEmployeename() {
        return this.employeename;
    }

    public void setEmployeename(Object employeename) {
        this.employeename = employeename;
    }

    public Object getBranchname() {
        return this.branchname;
    }

    public void setBranchname(Object branchname) {
        this.branchname = branchname;
    }

    public Object getBorrowerscode() {
        return this.borrowerscode;
    }

    public void setBorrowerscode(Object borrowerscode) {
        this.borrowerscode = borrowerscode;
    }

    public Object getFirstname() {
        return this.firstname;
    }

    public void setFirstname(Object firstname) {
        this.firstname = firstname;
    }

    public String getCollectiontyp() {
        return this.collectiontyp;
    }

    public void setCollectiontyp(String collectiontyp) {
        this.collectiontyp = collectiontyp;
    }

    public String getNoofdays() {
        return this.noofdays;
    }

    public void setNoofdays(String noofdays) {
        this.noofdays = noofdays;
    }

    public String getPendinginstallments() {
        return this.pendinginstallments;
    }

    public void setPendinginstallments(String pendinginstallments) {
        this.pendinginstallments = pendinginstallments;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCollection() {
        return this.collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "collectiondate='" + collectiondate + '\'' +
                ", loancode='" + loancode + '\'' +
                ", principalamount='" + principalamount + '\'' +
                ", initiatedamount='" + initiatedamount + '\'' +
                ", intrest='" + intrest + '\'' +
                ", employeeid='" + employeeid + '\'' +
                ", employeecod=" + employeecod +
                ", employeename=" + employeename +
                ", branchname=" + branchname +
                ", borrowerscode=" + borrowerscode +
                ", firstname=" + firstname +
                ", collectiontyp='" + collectiontyp + '\'' +
                ", noofdays='" + noofdays + '\'' +
                ", pendinginstallments='" + pendinginstallments + '\'' +
                ", amount='" + amount + '\'' +
                ", balance='" + balance + '\'' +
                ", collection='" + collection + '\'' +
                '}';
    }
}
