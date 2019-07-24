package com.tagcor.tagcor.ProfessionalFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tagcor.tagcor.JobsAdapters.RecentHomePageAdapter;
import com.tagcor.tagcor.JobsBeans.HomePageRecentBean;
import com.tagcor.tagcor.ProfessionalAdapters.ProfessionalHomePostAdapter;
import com.tagcor.tagcor.ProfessionalBeans.ProfessHomePostBean;
import com.tagcor.tagcor.R;
import com.tagcor.tagcor.RetrofitHelpers.Api;
import com.tagcor.tagcor.RetrofitHelpers.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeProfeFragment extends Fragment {

    String uId;
    private static final String TAG_PID = "user_id";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    private boolean loggedIn = false;

    ProfessionalHomePostAdapter professionalHomePostAdapter;
    RecyclerView RcProfPostlist;
    List<ProfessHomePostBean> ProfessPostList;

    public HomeProfeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_professional, container, false);

        Intent i = getActivity().getIntent();
        //mid = i.getStringExtra(TAG_PID);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Api.Member, Context.MODE_PRIVATE);
        uId = sharedPreferences.getString(TAG_PID, "Not Available");
        System.out.println("dsfjlsaf======================================================"+uId);

        ProfessPostList =  new ArrayList<>();

        RcProfPostlist = (RecyclerView) view.findViewById(R.id.RcProfPostList);
        RcProfPostlist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getProfessHomePagePostList();

        return view;
    }

    private void getProfessHomePagePostList() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        if(!progressDialog.isShowing())
            progressDialog.show(); // show progress dialog

        ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);

        Call<Object> call = apiService.getProfessionalPostHome(uId);
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
                        JSONObject json2 = json.getJSONObject("Professionalposts");

                        String statusCode = json2.getString("StatusCode");

                        if (statusCode.contains("200")) {
                            JSONArray obj = json2.getJSONArray("Data");
                            String data = String.valueOf(obj);
                            System.out.println("final data :" + data);

                            for (int i = 0; i<obj.length(); i++) {
                                JSONObject element = obj.getJSONObject(i);

                                String post_id = element.getString("post_id");
                                String user_id = element.getString("user_id");
                                //String sessionId = element.getString(uId);
                                String request_accept = element.getString("login_id");
                                String profileImage = element.optString("profile_image");
                                String fname = element.getString("fname");
                                String lname = element.getString("lname");
                                String postText = element.getString("description");
                                String profpostImage = element.optString("profpost_image");
                                String postLike = element.getString("post_like");
                                String postUnlike = element.getString("post_unlike");

                                ProfessHomePostBean mb = new ProfessHomePostBean();
                                mb.setPostId(post_id);
                                mb.setUserId(user_id);
                                mb.setRequestAccept(request_accept);
                                mb.setProfileImage(profileImage);
                                mb.setFname(fname);
                                mb.setLname(lname);
                                mb.setDescription(postText);
                                mb.setProfpostImage(profpostImage);
                                mb.setPostLike(postLike);
                                mb.setPostUnlike(postUnlike);
                                mb.setSessionId(uId);

                                ProfessPostList.add(mb);

                            }
                            professionalHomePostAdapter = new ProfessionalHomePostAdapter(getContext(), ProfessPostList);
                            RcProfPostlist.setAdapter(professionalHomePostAdapter);
                            /*professionalHomePostAdapter.notifyDataSetChanged();*/
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
                Toast.makeText(getContext(), "Network error, Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    //Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

}
