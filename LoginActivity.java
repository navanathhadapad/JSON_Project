package com.tagcor.tagcor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tagcor.tagcor.C2CBeans.UserLoginBean;
import com.tagcor.tagcor.RetrofitHelpers.Api;
import com.tagcor.tagcor.RetrofitHelpers.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tagcor.tagcor.InternetConnection.checkInternetConenction;

public class LoginActivity extends AppCompatActivity {

    private Button btn_sign_in;
    private EditText ed_email, ed_pw;
    //private String back_Flag = "0";

    String Uname, Password;
    //public static String user_Flag = "0";

    String uId;
    private static final String TAG_PID = "user_id";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title bar code here---
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        ed_email = findViewById(R.id.email);
        ed_pw = findViewById(R.id.pass);

        btn_sign_in = findViewById(R.id.login);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (checkValidation()) {
                    if (checkInternetConenction(LoginActivity.this)) {
                        log_in(ed_email.getText().toString().trim(), ed_pw.getText().toString().trim());
                    }else Toast.makeText(LoginActivity.this,"No internet connection.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void log_in(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        if(!progressDialog.isShowing())
            progressDialog.show(); // show progress dialog

        email = ed_email.getText().toString();
        password = ed_pw.getText().toString();

        ApiInterface apiService = Api.getClient().create(ApiInterface.class);

        Call<Object> call = apiService.login(email, password);
        System.out.println("signup url is :"+call.request().url()+"\n"+call.request().body().toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                // String finalrespons = new Gson().toJson(response);
                //System.out.println("final response :"+new Gson().toJson(response));
                System.out.println("response :" + response.body());
                if (response.body() != null && response.body().toString().length() > 0) {


                    String finalresponse = new Gson().toJson(response.body());
                    JSONObject json = null;
                    try {
                        json = new JSONObject(finalresponse);
                        JSONObject json2 = json.getJSONObject("Login");

                        String statusCode = json2.getString("StatusCode");
                        String statusText = json2.getString("StatusText");
                        if (!response.body().toString().contains("Data")) {
                            statusText = "Please, Try Again !";
                        }
                        System.out.println("Status code :" + statusCode);

                        if (statusCode.contains("204")) {
                            Toast.makeText(LoginActivity.this, statusText, Toast.LENGTH_LONG).show();
                        } else {
                            if (statusCode.contains("200")) {

                                JSONObject obj = json2.getJSONObject("Data");
                                //String data = String.valueOf(obj);
                                String data = obj.getString("user_id");

                                System.out.println("final data :" + data);
                                Toast.makeText(LoginActivity.this,"response :"+data,Toast.LENGTH_LONG).show();
                                Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();

                                SharedPreferences preferences = getSharedPreferences(Api.Member, MODE_PRIVATE);

                                UserLoginBean members = GsonConversions.memberConversion(LoginActivity.this);
                                System.out.println("User id is :"+members.getUserId());
                                Intent in = new Intent(LoginActivity.this,AllLinksActivity.class);

                                SharedPreferences.Editor edit = preferences.edit();
                                edit.putString(TAG_PID, data);
                                edit.putBoolean(LOGGEDIN_SHARED_PREF, true);
                                edit.commit();
                                startActivity(in);
                                finish();

                            } else
                                Toast.makeText(LoginActivity.this, statusText, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // Log error here since request failed
                System.out.println("response on failure is :" + t.toString());
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Network error, Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();// optional depending on your needs
        //finish();
        finishAffinity();

    }


    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(ed_email, "Enter User Email Id", LoginActivity.this)) ret = false;
        if (!Validation.hasText(ed_pw, "Enter Password", LoginActivity.this)) ret = false;
        return ret;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        imm.hideSoftInputFromWindow(ed_email.getWindowToken(), 0);

    }


    @Override
    protected void onPause() {
       /* if (back_Flag.equals("1"))
            LoginActivity.this.finish();*/
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(Api.Member, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, AllLinksActivity.class);
            intent.putExtra(TAG_PID, uId);
            startActivity(intent);
        }
    }
}
