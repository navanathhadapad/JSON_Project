package com.tagcor.tagcorloc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tagcor.tagcorloc.Beans.UserBean;
import com.tagcor.tagcorloc.Utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    Button login, signup;
    EditText email, password;

    TextView error_msg;

    String uLcid;
    private static final String TAG_PID = "lcid";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    private boolean loggedIn = false;
    String str_email, str_password;

    private String valid_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i = getIntent();
        uLcid = i.getStringExtra(TAG_PID);

        //shopid = Integer.parseInt(i.getStringExtra(String.valueOf(TAG_PID)));

        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.pass);

        //error_msg = (TextView) findViewById(R.id.error_msg);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().length()!=0){
                    //email.setError("Invalid details");
                    //Toast.makeText(LoginActivity.this, "Invalid details", Toast.LENGTH_SHORT).show();
                    if(password.getText().toString().trim().length()!=0){

                        UserBean ub = new UserBean();
                        ub.setEmail(email.getText().toString().trim());
                        ub.setPassword(password.getText().toString().trim());
                        new SendLoginDetails().execute(ub);


                        /*if (mSessionManager.isUserExists() != null)
                            naviageProfilePage(mSessionManager.isUserExists());

                        if (email.getText().toString() == null) {
                            email.setError("Invalid Email Address");
                            valid_email = null;
                        } else if (isEmailValid(email.getText().toString()) == false) {
                            email.setError("Invalid Email Address");
                            valid_email = null;
                        } else {
                            valid_email = email.getText().toString();

                        }*/

                        /*SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("username", lcid);*/
                        //mSessionManager.createUserSession(lcid);

                    }else {
                        password.setError("Password is Mandatory");
                    }
                }else {
                    email.setError("Email is Mandatory");
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            intent.putExtra(TAG_PID, uLcid);
            startActivity(intent);
        }
    }



    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();// optional depending on your needs
        finish();
    }

    public class SendLoginDetails extends AsyncTask<UserBean, Void, String> {
        Dialog dialog;
        Context context;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.custom1);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            //dialog.setTitle("Loading Please Wait...");
            //dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(UserBean... params) {

            UserBean ub = params[0];
            ServiceHandler sh = new ServiceHandler();

            String response = sh.makeServiceCalls(Config.loginUrl + "?lcemail=" + ub.getEmail() + "&&lcpassword=" + ub.getPassword());

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s != null) {
                //TextView nameDisp = (TextView) findViewById(R.id.user_id);
                System.out.print(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    String user_id = jsonObject.getString("lcid");

                    if (status.equals("successfully")) {

                        //naviageProfilePage(lcid);
                        //mSessionManager.createUserSession(lcid);
                        //SharedPrefManager.getInstance(getApplicationContext()).userLogin(lcid);

                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Creating editor to store values to shared preferences
                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                        //editor.putString(TAG_PID, user_id);
                        //Saving values to editor
                        //editor.commit();

                        Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                        //final String email;
                        //i.putExtra(UserEmail, email);
                        //mSessionManager.createUserSession(lcid);
                        i.putExtra(TAG_PID, user_id);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                        editor.putString(TAG_PID, user_id);
                        //Saving values to editor
                        editor.commit();
                        startActivity(i);
                        finish();

                    } else if (status.equals("Please try again")) {

                        Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
