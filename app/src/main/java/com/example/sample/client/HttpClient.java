
package com.example.sample.client;

import com.example.sample.model.BorrowerResponse;
import com.example.sample.model.BranchResponse;
import com.example.sample.model.CollectionsResponse;
import com.example.sample.model.DashboardResponse;
import com.example.sample.model.EmployeeResponse;
import com.example.sample.model.LoanResponse;
import com.example.sample.model.Login;
import com.example.sample.model.User;
import com.example.sample.util.Config;
import com.example.sample.util.Preference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class HttpClient {
    public static OkHttpClient client = new OkHttpClient.Builder()
            .authenticator((route, response) -> {
                String credential = Credentials.basic(Config.AUTH_USER_NAME, Config.AUTH_PASSWORD);
                return response.request().newBuilder().header("Authorization", credential).build();
            })
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private static Gson gson = new GsonBuilder().serializeNulls().create();
    private static Retrofit retrofit;

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.SERVER_BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static FinanceAPI getServerApi() {
        return getClient().create(FinanceAPI.class);
    }

    public static Call<User> login(Login login) {
        JsonObject payload = gson.fromJson(gson.toJson(login, Login.class),JsonObject.class);
        System.out.println(login.getUserName());
        System.out.println(login.getUserPassword());
        return getServerApi().doLogin(payload);
    }

    public static Call<JsonObject> updatePassword(JsonObject data) {
        return getServerApi().updatePassword(data);
    }

    public static Call<String> createCollectionList(JsonObject data) {
        return getServerApi().addCollections(data);
    }

    public static Call<String> createLoan(JsonObject data) {
        return getServerApi().addLoan(data);
    }

    public static Call<CollectionsResponse> getCollectionList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Branchid", Preference.getBranchID());
        return getServerApi().getCollections(jsonObject);
    }

    public static Call<DashboardResponse> getTodayCollectionList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Branchid", Preference.getBranchID());
        return getServerApi().getTodayCollections(jsonObject);
    }

    public static Call<String> updateUsername(JsonObject data) {
        return getServerApi().updateUsername(data);
    }

    public static Call<JsonObject> getloaninfo(JsonObject data) {
        return getServerApi().getLoanDetails(data);
    }

    public static Call<BorrowerResponse> getBorrowers() {
        JsonObject data = new JsonObject();
        data.addProperty("Branchid", Preference.getBranchID());
        return getServerApi().getBorrowers(data);
    }

    public static Call<BorrowerResponse.Borrower> createBorrower(BorrowerResponse.Borrower borrower) {
        JsonObject payload = gson.fromJson(gson.toJson(borrower, BorrowerResponse.Borrower.class),JsonObject.class);
        return getServerApi().createBorrower(payload);
    }

    public static Call<JsonObject> editBorrower(BorrowerResponse.Borrower borrower) {
        JsonObject payload = gson.fromJson(gson.toJson(borrower, BorrowerResponse.Borrower.class),JsonObject.class);
        return getServerApi().editBorrower(payload);
    }

    public static Call<ResponseBody> downloadApk(String url) {
        return getServerApi().download(url);
    }

    public static Call<BranchResponse> getBranches() {
        return getServerApi().getBranches();
    }

    public static Call<EmployeeResponse> getEmployees() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Branchid", Preference.getBranchID());
        return getServerApi().getEmployees(jsonObject);
    }

    public static Call<LoanResponse> getLoanList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Branchid",Preference.getBranchID());
        return getServerApi().getLoanList(jsonObject);
    }
    public static Call<JsonObject> getTotalamount() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        System.out.println("sample date format "+date);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Branchid",Preference.getBranchID());
        jsonObject.addProperty("today",date);

        return getServerApi().TotalCollections(jsonObject);
    }

    public static void createCollection() {

    }

    public interface FinanceAPI {
        @GET("content")
        Call<String> getContents();

        @GET
        Call<ResponseBody> download(@Url String url);

        @POST("borrowers.php")
        Call<BorrowerResponse> getBorrowers(@Body JsonObject jsonObject);

        @POST("employeelist.php")
        Call<EmployeeResponse> getEmployees(@Body JsonObject jsonObject);

        @POST("loan.php")
        Call<LoanResponse> getLoanList(@Body JsonObject jsonObject);

        @GET("allbranch.php")
        Call<BranchResponse> getBranches();

        @POST("login.php")
        Call<User> doLogin(@Body JsonObject body);

        @POST("addborrowers.php")
        Call<BorrowerResponse.Borrower> createBorrower(@Body JsonObject borrower);

        @POST("editborrowers.php")
        Call<JsonObject> editBorrower(@Body JsonObject borrower);

        @POST("updatepassword.php")
        Call<JsonObject> updatePassword(@Body JsonObject body);

        @POST("loandetails.php")
        Call<JsonObject> getLoanDetails(@Body JsonObject body);

        @POST("updateusername.php")
        Call<String> updateUsername(@Body JsonObject body);

        @POST("collection.php")
        Call<CollectionsResponse> getCollections(@Body JsonObject body);

        @POST("dashborad.php")
        Call<DashboardResponse> getTodayCollections(@Body JsonObject body);

        @POST("addcollection.php")
        Call<String> addCollections(@Body JsonObject body);

        @POST("totalcollection.php")
        Call<JsonObject> TotalCollections(@Body JsonObject body);

        @POST("addloan.php")
        Call<String> addLoan(@Body JsonObject body);

    }

}