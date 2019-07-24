package com.tagcor.tagcor.B2BFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tagcor.tagcor.R;
import com.tagcor.tagcor.RetrofitHelpers.Api;
import com.tagcor.tagcor.RetrofitHelpers.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class B2BAddNewAddressFragment extends Fragment {

    String uId;
    private static final String TAG_PID = "user_id";

    EditText edFullname, edMobileNo, edFullAdddress, edCity, edState, edPincode;
    Button btnaddAddress;
    TextView userId;
    String userid, fullName, mobileNo, FullAddr, city, state, pincode;

    public B2BAddNewAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b2_badd_new_address, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Api.Member, Context.MODE_PRIVATE);
        uId = sharedPreferences.getString(TAG_PID, "Not Available");
        System.out.println("dsfjlsaf======================================================"+uId);
        userId = view.findViewById(R.id.userId);
        userId.setText(uId);

        Intent i = getActivity().getIntent();

        edFullname = view.findViewById(R.id.FullName);
        edMobileNo = view.findViewById(R.id.MobileNo);
        edFullAdddress = view.findViewById(R.id.FullAddress);
        edCity = view.findViewById(R.id.edCity);
        edState = view.findViewById(R.id.edState);
        edPincode = view.findViewById(R.id.edPincode);

        btnaddAddress = view.findViewById(R.id.btnUpdate);

        btnaddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userid = userId.getText().toString().trim();
                fullName = edFullname.getText().toString().trim();
                mobileNo = edMobileNo.getText().toString().trim();
                FullAddr = edFullAdddress.getText().toString().trim();
                city = edCity.getText().toString().trim();
                state = edState.getText().toString().trim();
                pincode = edPincode.getText().toString().trim();

                AddUserAddressInfo();

                Fragment fragment ;
                fragment = new B2BAddressListFragment();
                FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.attach(fragment);
                transaction.commit();
            }
        });

        return view;
    }

    private void AddUserAddressInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        if(!progressDialog.isShowing())
            progressDialog.show(); // show progress dialog

        ApiInterface apiService = Api.getClientB2B().create(ApiInterface.class);
        Call<Object> call = apiService.B2BUserAddressInfoAdd(userid, fullName, mobileNo, FullAddr, city, state, pincode);
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
                        JSONObject json2 = json.getJSONObject("Addb2buseraddres");

                        String statusCode = json2.getString("StatusCode");
                        String statusText = json2.getString("StatusText");
                        if (!response.body().toString().contains("Data")) {
                            statusText = "Please, Try Again !";
                        }
                        System.out.println("Status code :" + statusCode);

                        if (statusCode.contains("204")) {
                            Toast.makeText(getContext(), statusText, Toast.LENGTH_LONG).show();
                        } else {
                            if (statusCode.contains("200")) {
                                JSONObject obj = json2.getJSONObject("Data");
                                //String data = String.valueOf(obj);

                            } else
                                Toast.makeText(getContext(), statusText, Toast.LENGTH_LONG).show();
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

}
