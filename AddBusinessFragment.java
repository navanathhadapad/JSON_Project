package com.tagcor.tagcorloc.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tagcor.tagcorloc.Adapters.McatAdapter;
import com.tagcor.tagcorloc.Adapters.ScatAdapter;
import com.tagcor.tagcorloc.AddBusinessProductActivity;
import com.tagcor.tagcorloc.AppLocationService;
import com.tagcor.tagcorloc.Beans.McatBean;
import com.tagcor.tagcorloc.Beans.ScatBean;
import com.tagcor.tagcorloc.LocationAddress;
import com.tagcor.tagcorloc.R;
import com.tagcor.tagcorloc.Utils.Config;
import com.tagcor.tagcorloc.WelcomeActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBusinessFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    String uLcid;
    private static final String TAG_PID = "lcid";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    Button getLocationBtn, btn_cancel;
    LocationManager locationManager;
    AppLocationService appLocationService;

    private Spinner mcat_sp, scat_sp;
    ArrayList<McatBean> productlist;
    ArrayList<ScatBean> productlist1;
    private McatAdapter main2Adapter;
    private ScatAdapter scatAdapter;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    EditText bus_title, owner_name, website, mob_no, opt_mobNo, adhar_no, full_address;
    Button add_business;
    TextView tv_mname, tv_sname, locationText, lang_txt, b_lcid;
    String b_title, name_own, b_website, b_mob, b_opt_mob, b_adhar, bfull_addr,
            sp,sp1, blcid, blat, blang,sp_time_to, sp_time_end, spweekoff;

    Location location;
    boolean isNetworkEnabled = false;
    public static final int REQUEST_LOCATION_CODE = 99;
    public AddBusinessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_business, container, false);
        appLocationService = new AppLocationService(getActivity());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkLocationPermission();

            //M

        }

        StrictMode.setThreadPolicy(policy);

        btn_cancel = view.findViewById(R.id.btn_cancel);

        getLocationBtn = view.findViewById(R.id.getlocation);
        locationText = view.findViewById(R.id.lat_txt);
        lang_txt = view.findViewById(R.id.lag_txt);
        full_address = view.findViewById(R.id.fulladdr);

        Intent i = getActivity().getIntent();
        uLcid = i.getStringExtra(TAG_PID);
        productlist = new ArrayList<>();
        productlist1 = new ArrayList<>();

        bus_title = view.findViewById(R.id.btitle);
        owner_name = view.findViewById(R.id.own_name);
        website = view.findViewById(R.id.website);
        mob_no = view.findViewById(R.id.mob_no);
        opt_mobNo = view.findViewById(R.id.opt_mobno);
        adhar_no = view.findViewById(R.id.adhar_no);

        tv_mname = view.findViewById(R.id.m_name);
        tv_sname = view.findViewById(R.id.s_name);
        b_lcid = view.findViewById(R.id.lcid);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uLcid = sharedPreferences.getString(TAG_PID,"Not Available");
        b_lcid.setText(uLcid);

        add_business = view.findViewById(R.id.add_business);

        mcat_sp = (Spinner) view.findViewById(R.id.mcat);
        scat_sp = (Spinner) view.findViewById(R.id.scat);

        new GetMainCatProduct().execute(uLcid);
        new GetSubCatProduct().execute(uLcid);

        /*ArrayAdapter<McatBean> dataAdapter1 = new ArrayAdapter<McatBean>(getContext(), android.R.layout.simple_spinner_item, productlist);
        dataAdapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mcat_sp.setAdapter(dataAdapter1);*/

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                startActivity(intent);

            }
        });

        mcat_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m_id = productlist.get(position).getMid().toString();
                /*Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.putExtra(TAG_PID, m_id);
                //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);*/

                new GetSubCatProduct().execute(m_id);
                productlist1.clear();
                scat_sp.setAdapter(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Nothing","Selected");
            }
        });

        scat_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Spinner weekof = (Spinner) view.findViewById(R.id.add_pro);
        weekof.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Open All Day");
        categories.add("Sunday");
        categories.add("Monday");
        categories.add("Tuesday");
        categories.add("Wednesday");
        categories.add("Thursday");
        categories.add("Friday");
        categories.add("Saturday");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        // attaching data adapter to spinner
        weekof.setAdapter(dataAdapter);

        final Spinner stime_start = (Spinner) view.findViewById(R.id.stime_start);
        stime_start.setOnItemSelectedListener(this);

        final Spinner stime_end = (Spinner) view.findViewById(R.id.stime_end);
        stime_end.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        final List<String> startTime = new ArrayList<String>();
        startTime.add("1 AM");
        startTime.add("2 AM");
        startTime.add("3 AM");
        startTime.add("4 AM");
        startTime.add("5 AM");
        startTime.add("6 AM");
        startTime.add("7 AM");
        startTime.add("8 AM");
        startTime.add("9 AM");
        startTime.add("10 AM");
        startTime.add("11 AM");
        startTime.add("12 AM");
        startTime.add("1 PM");
        startTime.add("2 PM");
        startTime.add("3 PM");
        startTime.add("4 PM");
        startTime.add("5 PM");
        startTime.add("6 PM");
        startTime.add("7 PM");
        startTime.add("8 PM");
        startTime.add("9 PM");
        startTime.add("10 PM");
        startTime.add("11 PM");
        startTime.add("12 PM");

        // Creating adapter for spinner
        ArrayAdapter<String> stimestart = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, startTime);
        // Drop down layout style - list view with radio button
        stimestart.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        // attaching data adapter to spinner
        stime_start.setAdapter(stimestart);
        stime_end.setAdapter(stimestart);


        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                location = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

                    locationText.setText(""+location.getLatitude());
                    lang_txt.setText(""+location.getLongitude());

                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude, getContext(), new GeocoderHandler());
                } else {
                    // showSettingsAlert();
                }
            }
        });

        add_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bus_title.getText().toString().trim().length() != 0){

                    if(mob_no.getText().toString().trim().length() != 0){

                        blcid = b_lcid.getText().toString().trim();
                        b_title = bus_title.getText().toString().trim();
                        b_mob = mob_no.getText().toString().trim();
                        name_own = owner_name.getText().toString().trim();
                        b_website = website.getText().toString().trim();
                        b_opt_mob = opt_mobNo.getText().toString().trim();
                        b_adhar = adhar_no.getText().toString().trim();
                        blat = locationText.getText().toString().trim();
                        blang = lang_txt.getText().toString().trim();
                        sp_time_to = stime_start.getSelectedItem().toString().trim();
                        sp_time_end = stime_end.getSelectedItem().toString().trim();
                        spweekoff = weekof.getSelectedItem().toString().trim();
                        bfull_addr = full_address.getText().toString().trim();

                        TextView tv_mcat = (TextView)mcat_sp.getSelectedView().findViewById(R.id.m_id);
                        sp = tv_mcat.getText().toString();
                        TextView tv_scat = (TextView)scat_sp.getSelectedView().findViewById(R.id.s_id);
                        sp1 = tv_scat.getText().toString();

                        insert_business_data();


                    }else {
                        mob_no.setError("Mobile No is Mandatory");
                    }
                }else {
                    bus_title.setError("Business Title is Mandatory");
                }
                /*if(b_lcid.getText().toString().length() != 0||
                        bus_title.getText().toString().trim().length() != 0||
                        owner_name.getText().toString().length() != 0||
                        website.getText().toString().length() != 0||
                        mob_no.getText().toString().trim().length() != 0||
                        opt_mobNo.getText().toString().length() != 0||
                        adhar_no.getText().toString().length() != 0||
                        locationText.getText().toString().trim().length() != 0||
                        lang_txt.getText().toString().trim().length() != 0||
                        stime_start.getSelectedItem().toString().length() != 0||
                        stime_end.getSelectedItem().toString().length() != 0||
                        weekof.getSelectedItem().toString().length() != 0||
                        full_address.getText().toString().trim().length() != 0){

                    blcid = b_lcid.getText().toString().trim();
                    b_title = bus_title.getText().toString().trim();
                    b_mob = mob_no.getText().toString().trim();
                    name_own = owner_name.getText().toString().trim();
                    b_website = website.getText().toString().trim();
                    b_opt_mob = opt_mobNo.getText().toString().trim();
                    b_adhar = adhar_no.getText().toString().trim();
                    blat = locationText.getText().toString().trim();
                    blang = lang_txt.getText().toString().trim();
                    sp_time_to = stime_start.getSelectedItem().toString().trim();
                    sp_time_end = stime_end.getSelectedItem().toString().trim();
                    spweekoff = weekof.getSelectedItem().toString().trim();
                    bfull_addr = full_address.getText().toString().trim();

                    TextView tv_mcat = (TextView)mcat_sp.getSelectedView().findViewById(R.id.m_id);
                    sp = tv_mcat.getText().toString();
                    TextView tv_scat = (TextView)scat_sp.getSelectedView().findViewById(R.id.s_id);
                    sp1 = tv_scat.getText().toString();

                    insert_business_data();

                }else {
                    //bus_title.setError("First name is required!");
                    bus_title.setError("Title name is required!");
                    mob_no.setError("Email is required!");
                    locationText.setError("Password is required!");
                    lang_txt.setError("Mobile no is required!");
                    full_address.setError("City name is required!");
                }

                //clear entered data edit text feild
                *//*ed_name.setText("");
                ed_tite.setText("");
                ed_email.setText("");
                ed_pass.setText("");
                ed_mobile.setText("");
                ed_city.setText("");*/
            }
        });

        return view;
    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    /*public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }*/

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            full_address.setText(locationAddress);
        }
    }

    public class GetMainCatProduct extends AsyncTask {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL reurl = new URL(Config.urlMcat+ "?mid=" + params[0]);
                URLConnection urlConnection = reurl.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String temp = null;
                StringBuilder sb = new StringBuilder();

                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                    sb.append("\n");
                }
                response = sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //dialog.cancel();

            if (response != null) {
                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject element = jsonArray.getJSONObject(i);
                        String m_id1 = element.getString("bmcatId");
                        String mcat_name = element.getString("bmcatName");

                        System.out.println(m_id1);
                        System.out.println(mcat_name);

                        McatBean mb = new McatBean();
                        mb.setMid(m_id1);
                        mb.setMcatname(mcat_name);

                        productlist.add(mb);
                    }
                    main2Adapter = new McatAdapter(getContext(), productlist);
                    // main2Adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    mcat_sp.setAdapter(main2Adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class GetSubCatProduct extends AsyncTask {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL reurl = new URL(Config.UrlScat + "?mid=" + params[0]);
                URLConnection urlConnection = reurl.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String temp = null;
                StringBuilder sb = new StringBuilder();

                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                    sb.append("\n");
                }
                response = sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //dialog.cancel();

            if (response != null) {
                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject element = jsonArray.getJSONObject(i);
                        //String m_id1 = element.getString("lcid");
                        String s_id = element.getString("bscatId");
                        String mcatid = element.getString("bmcatId");

                        String scat_name = element.getString("bscatName");

                        //System.out.println(m_id1);
                        System.out.println(s_id);
                        System.out.println(mcatid);
                        System.out.println(scat_name);

                        ScatBean mb = new ScatBean();
                        mb.setSid(s_id);
                        mb.setMcatid(mcatid);
                        mb.setScatname(scat_name);

                        productlist1.add(mb);
                    }
                    //productlist1.clear();
                    scatAdapter = new ScatAdapter(getContext(), productlist1);
                    scat_sp.setAdapter(scatAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insert_business_data(){
        InputStream is = null;
        String result = null;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("lcid", blcid));
        nameValuePairs.add(new BasicNameValuePair("btitle", b_title));
        nameValuePairs.add(new BasicNameValuePair("owner",name_own));
        nameValuePairs.add(new BasicNameValuePair("website",b_website));
        nameValuePairs.add(new BasicNameValuePair("phone",b_mob));
        nameValuePairs.add(new BasicNameValuePair("optno",b_opt_mob));
        nameValuePairs.add(new BasicNameValuePair("adhar",b_adhar));
        nameValuePairs.add(new BasicNameValuePair("baddress",bfull_addr));
        nameValuePairs.add(new BasicNameValuePair("bmcat",sp));
        nameValuePairs.add(new BasicNameValuePair("bscat",sp1));
        nameValuePairs.add(new BasicNameValuePair("lat",blat));
        nameValuePairs.add(new BasicNameValuePair("long",blang));

        nameValuePairs.add(new BasicNameValuePair("bstime",sp_time_to));
        nameValuePairs.add(new BasicNameValuePair("bctime",sp_time_end));
        nameValuePairs.add(new BasicNameValuePair("weekoff",spweekoff));

        //Toast.makeText(RegisterActivity.this, "Value "+ed_name.getText(), Toast.LENGTH_SHORT).show();
        try
        {
            /*HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String string,SSLSession ssls) {
                    return true;
                }
            });*/

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.UrlBusinessDetails);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
            Toast.makeText(getContext(), "Data Send Successfully", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
                Intent i = new Intent(getContext(), WelcomeActivity.class);
                i.putExtra(TAG_PID, uLcid);
                startActivity(i);
                getActivity().finish();
            }
            is.close();

            result=sb.toString();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error converting result "+e.toString());
        }


        /*//convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
                Intent i = new Intent(getContext(),WelcomeActivity.class);
                startActivity(i);
            }
            is.close();

            result=sb.toString();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error converting result "+e.toString());
        }*/

        /*try{

            JSONObject json_data = new JSONObject(result);
            String status = json_data.getString("status");
            if(status=="true")
            {
                Toast.makeText(getContext(), "Inserted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }


        }
        catch(JSONException e) {
            Log.e("log_tag", "Error parsing data "+e.toString());
            Toast.makeText(getContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
        }*/

    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    DashboardFragment businessListFragment = new DashboardFragment();
                    final FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    //FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, businessListFragment);
                    //fragmentTransaction.add(R.id.frame_container, businessListFragment);
                    fragmentTransaction.addToBackStack(null);
                    //fragmentTransaction.remove(businessListFragment);
                    fragmentTransaction.commit();

                    //Intent intent = new Intent(getContext(), WelcomeActivity.class);
                    //intent.putExtra(TAG_PID, uLcid);
                    //startActivity(intent);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onStart();
    }
}
