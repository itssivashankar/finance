package com.example.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sample.client.HttpClient;
import com.example.sample.util.Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private Button username, password;

    private LinearLayout f_username_update, f_password_update, update_user;
    private TextInputEditText old_username, new_username, conform_password, old_password, new_password;
    private MaterialButton User_update,Password_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Preference(this).init();

        setContentView(R.layout.activity_update_user);

        username = findViewById(R.id.update_user_name);
        password = findViewById(R.id.update_user_password);
        f_password_update = findViewById(R.id.f_password_update);
        f_username_update = findViewById(R.id.f_username_update);
        User_update = findViewById(R.id.update_user_name_button);
        Password_update = findViewById(R.id.update_password_button);
        update_user = findViewById(R.id.update_user);
        old_username = findViewById(R.id.old_username);
        new_username = findViewById(R.id.new_username);
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        conform_password = findViewById(R.id.conform_password);
        username.setText("Update UserName");
        password.setText("Update Password");
        username.setOnClickListener(n -> {
            update_user.setVisibility(View.GONE);
            f_username_update.setVisibility(View.VISIBLE);
        });
        password.setOnClickListener(n -> {
            update_user.setVisibility(View.GONE);
            f_password_update.setVisibility(View.VISIBLE);
        });
        Password_update.setOnClickListener(n -> {
            String oldpassword = old_password.getText().toString();
            String newpassword = new_password.getText().toString();
            String conformpassword = conform_password.getText().toString();
            JsonObject data = new JsonObject();

            if (newpassword.equals(conformpassword)) {
                data.addProperty("Employeeid", Preference.getEmployeeID());
                data.addProperty("OldPassword", oldpassword);
                data.addProperty("NewPassword", newpassword);
                data.addProperty("ConfirmPassword", conformpassword);
                System.out.println("update password 1 "+data);
                HttpClient.updatePassword(data).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        System.out.println("update password 2 "+response.code());
                        System.out.println("update password 3 "+response.body().toString());
                        old_password.setText(null);
                        conform_password.setText(null);
                        new_password.setText(null);
                        Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        System.out.println("update password 4 "+t);
                        Toast.makeText(ProfileActivity.this, "Problem in  Update Password", Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Toast.makeText(this, "Passwords are not equal", Toast.LENGTH_LONG).show();
        });
        User_update.setOnClickListener(n -> {
            JsonObject data = new JsonObject();
            System.out.println("Update user call");
            if (!old_username.getText().toString().equals(new_username.getText().toString())) {
                data.addProperty("Username", new_username.getText().toString());
                data.addProperty("Employeeid", Preference.getEmployeeID());
                System.out.println("Update user call 1"+data);
                HttpClient.updateUsername(data).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println("Update user call 2 "+response.code());
                        if(response.code()==200){
                            old_username.setText(null);
                            new_username.setText(null);
                            Toast.makeText(ProfileActivity.this, "User name Updated", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println("Update user call 3 "+ t);
                        Toast.makeText(ProfileActivity.this, "Problem in Update", Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Toast.makeText(this, "Username are same", Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public void onBackPressed() {
        if (update_user.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else {
            f_username_update.setVisibility(View.GONE);
            f_password_update.setVisibility(View.GONE);
            update_user.setVisibility(View.VISIBLE);
        }
    }

    public void logoutUser(View view) {
        Preference.logout();
        finish();
    }
}