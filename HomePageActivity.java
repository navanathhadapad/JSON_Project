package com.tagcor.tagcor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tagcor.tagcor.JobsAdapters.AppliedResumelistJobAdapter;
import com.tagcor.tagcor.JobsBeans.ResumeListBean;
import com.tagcor.tagcor.JobsFragments.EditExpFragment;
import com.tagcor.tagcor.ProfessionalAdapters.NotificationListProfAdapter;
import com.tagcor.tagcor.ProfessionalBeans.ProfNotificationListBean;
import com.tagcor.tagcor.ProfessionalFragments.FriendRequestFragment;
import com.tagcor.tagcor.ProfessionalFragments.HomeProfeFragment;
import com.tagcor.tagcor.ProfessionalFragments.ProfessionalProfileFragment;
import com.tagcor.tagcor.ProfessionalFragments.SearchFriendsFragment;
import com.tagcor.tagcor.RetrofitHelpers.Api;
import com.tagcor.tagcor.RetrofitHelpers.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePageActivity extends AppCompatActivity {

    String uId;
    private static final String TAG_PID = "user_id";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    private boolean loggedIn = false;

    private ImageView more_opt, settings;
    //private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog dialog, dialog1;

    //Button BtnSearch;
    LinearLayout BtnSearch;
    TextView LikeNotifCount, errorMsg;
    CircleImageView BGLikeNot;

    List<ProfNotificationListBean> NotificationList;
    NotificationListProfAdapter notificationListProfAdapter;
    RecyclerView RclikeNotificationList;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        //back_Flag = i.getStringExtra(session_id);

        SharedPreferences sharedPreferences = getSharedPreferences(Api.Member, Context.MODE_PRIVATE);
        uId = sharedPreferences.getString(TAG_PID, "Not Available");
        System.out.println("dsfjlsaf======================================================" + uId);

        LikeNotifCount = findViewById(R.id.notiCount);
        BGLikeNot = findViewById(R.id.user_img);
        getLikeNotificationCount();

        NotificationList =  new ArrayList<>();

        //this.mHandler = new Handler();
        //m_Runnable.run();

        final ImageView notificatin = (ImageView) findViewById(R.id.notification);
        notificatin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.professional_notification, null);
                final PopupWindow popupWindow = new PopupWindow(getApplication());
                popupWindow.setContentView(popupView);
                popupWindow.setWindowLayoutMode(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(1);
                popupWindow.setWidth(1);
                popupWindow.setFocusable(true);
                //hiding the stroke
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(notificatin);

                RclikeNotificationList = popupView.findViewById(R.id.RVNotificationList);
                RclikeNotificationList.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.VERTICAL, false));
                errorMsg = popupView.findViewById(R.id.errorMsg);
                getLikeUnlieNotiList();

                NotificationList.clear();

            }
        });

        BtnSearch = findViewById(R.id.search);
        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment ;
                fragment = new SearchFriendsFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        more_opt = findViewById(R.id.setting);
        more_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        init_modal_bottomsheet();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeProfeFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {

                case R.id.navigation_Dashboard:
                    //mTextMessage.setText(R.string.title_home);
                    fragment = new HomeProfeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_category:
                    //mTextMessage.setText(R.string.title_dashboard);
                    fragment = new HomeProfeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_history:
                    // mTextMessage.setText(R.string.title_notifications);
                    Intent i = new Intent(getApplication(), PostStatusActivity.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    // mTextMessage.setText(R.string.title_notifications);
                    fragment = new FriendRequestFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    // mTextMessage.setText(R.string.title_notifications);
                    fragment = new ProfessionalProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.sign_out_menu, null);

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        /*btn_ed_profile = (TextView)modalbottomsheet.findViewById(R.id.btn_ed_profile);
        btn_ed_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                Fragment fragment ;
                fragment = new ProfileFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fl_Content, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });*/

        TextView LogoutBtn = (TextView) modalbottomsheet.findViewById(R.id.btn_logout);
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                SharedPreferences preferences = getSharedPreferences(Api.Member, Context.MODE_PRIVATE);
                //Getting editor
                SharedPreferences.Editor editor = preferences.edit();

                //Puting the value false for loggedin
                editor.putBoolean(LOGGEDIN_SHARED_PREF, false);
                //Putting blank value to email
                editor.putString(TAG_PID, "");
                editor.clear();
                //Saving the sharedpreferences
                editor.commit();
                finish();
                startActivity(intent);

            }
        });
    }

    private void getLikeNotificationCount() {

        ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);

        Call<Object> call = apiService.getProfessionalLikeNotifCount(uId);
        System.out.println("categories url is :"+call.request().url());
        call.enqueue(new Callback<Object>() {

            String response;
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.body() != null && response.body().toString().length() > 0) {

                    String finalresponse = new Gson().toJson(response.body());
                    JSONObject json = null;
                    try {
                        json = new JSONObject(finalresponse);
                        JSONObject json2 = json.getJSONObject("Likenotificationcount");

                        String statusCode = json2.getString("StatusCode");

                        if (statusCode.contains("200")) {

                            int LikeNotCount = json2.getInt("Data");
                            LikeNotifCount.setText(String.valueOf(LikeNotifCount.getText())+LikeNotCount);

                            if (LikeNotifCount.equals(LikeNotCount)){
                                LikeNotifCount.setVisibility(View.GONE);
                                BGLikeNot.setVisibility(View.GONE);
                            }else{
                                LikeNotifCount.setVisibility(View.VISIBLE);
                                BGLikeNot.setVisibility(View.VISIBLE);
                                //String.valueOf(LikeNotifCount.equals("")).equals(LikeNotCount)
                            }
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
                Toast.makeText(HomePageActivity.this, "Network error, Please try again.", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getLikeUnlieNotiList() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        if(!progressDialog.isShowing())
            progressDialog.show(); // show progress dialog

        ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);

        Call<Object> call = apiService.getProfessitionlNotiListInfo(uId);
        System.out.println("categories url is :"+call.request().url());
        call.enqueue(new Callback<Object>() {

            String response;
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.body() != null && response.body().toString().length() > 0) {

                    String finalresponse = new Gson().toJson(response.body());
                    JSONObject json = null;
                    try {
                        json = new JSONObject(finalresponse);
                        JSONObject json2 = json.getJSONObject("Likeunlikenotificationinfo");

                        String statusCode = json2.getString("StatusCode");

                        if (statusCode.contains("200")) {
                            JSONArray obj = json2.getJSONArray("Data");

                            for (int i = 0; i<obj.length(); i++) {
                                JSONObject element = obj.getJSONObject(i);

                                String id = element.getString("id");
                                String postid = element.getString("post_id");
                                String userId = element.getString("user_id");
                                String fname = element.getString("fname");
                                String lname = element.getString("lname");
                                String profileImage = element.optString("profile_image");

                                ProfNotificationListBean mb = new ProfNotificationListBean();
                                mb.setId(id);
                                mb.setPostId(postid);
                                mb.setUserId(userId);
                                mb.setFname(fname);
                                mb.setLname(lname);
                                mb.setProfileImage(profileImage);
                                NotificationList.add(mb);
                            }
                            notificationListProfAdapter = new NotificationListProfAdapter(HomePageActivity.this, NotificationList);
                            RclikeNotificationList.setAdapter(notificationListProfAdapter);
                        }else if (statusCode.contains("204")){
                            String statusCode1 = json2.getString("StatusText");
                            errorMsg.setText(errorMsg.getText()+statusCode1);
                            errorMsg.setVisibility(View.VISIBLE);
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
                Toast.makeText(HomePageActivity.this, "Network error, Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

   /* private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            Toast.makeText(HomePageActivity.this,"in runnable",Toast.LENGTH_SHORT).show();

            HomePageActivity.this.mHandler.postDelayed(m_Runnable, 5000);
        }
    };*/

}
