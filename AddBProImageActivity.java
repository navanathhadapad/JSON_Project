package com.tagcor.tagcorloc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tagcor.tagcorloc.Utils.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class AddBProImageActivity extends Activity {


    private Button buttonChoose;
    //Image request code
    //private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST = 1;

    String uLcid;
    private static final String TAG_PID = "lcid";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    Button add_business;
    ImageView imageView;
    EditText tv_lcid, tv_lcbid;
    Spinner discount_type;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    String b_lcid, b_lcbid, b_img;

    private Bitmap bitmap;

    private Uri uri;
    Button btn_cancel;

    public static final String UPLOAD_KEY = "bimg";
    private String selectedImagePath;
    TextView err_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bpro_image);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        uLcid = sharedPreferences.getString(TAG_PID,"Not Available");
        //mob_no.setText(email);
        System.out.println("dsfjlsaf======================================================"+uLcid);

        Intent i = getIntent();
        uLcid = i.getStringExtra(TAG_PID);

        new GetAllInformation().execute(uLcid);
        StrictMode.setThreadPolicy(policy);

        buttonChoose = findViewById(R.id.add_bproduct);

        add_business = findViewById(R.id.add_business);
        err_msg = findViewById(R.id.error_msg);

        tv_lcid = findViewById(R.id.lcid);
        tv_lcbid = findViewById(R.id.lcbid);
        imageView = findViewById(R.id.pathImg);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBProImageActivity.this, WelcomeActivity.class);
                startActivity(intent);

            }
        });

        //add_business.setOnClickListener(this);

        add_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageView.getDrawable() != null) {


                    b_lcid = tv_lcid.getText().toString().trim();
                    b_lcbid = tv_lcbid.getText().toString().trim();
                    b_img = getStringImage(bitmap);

                    insert_business_data();

                } else {
                    err_msg.setText("Please select image...");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            //imageView.setImageURI(Uri.parse(new File(String.valueOf(uri)).toString()));

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Log.d(TAG, String.valueOf(bitmap));
                //String PathHolder = uri.getPath();
                String path = getApplication().getFilesDir().getAbsolutePath();
                File file = new File(path);

                InputStream input = getApplication().getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(input , null, null);
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        /*ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 10, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        byte[] imageBytes = out.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/
        return encodedImage;
    }

   /* @Override
    public void onClick(View v) {

        // uploadImage();
        String result = null;
        InputStream is = null;

        b_lcid = tv_lcid.getText().toString().trim();
        b_lcbid = tv_lcbid.getText().toString().trim();
        b_img = getStringImage(bitmap);
        //b_img = buttonChoose.getText().toString().trim();

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("lcid", b_lcid));
        nameValuePairs.add(new BasicNameValuePair("lcbid", b_lcbid));

        nameValuePairs.add(new BasicNameValuePair("bimg", b_img));

        Log.i("Mynewsam", "============================" + b_img);

        //Toast.makeText(RegisterActivity.this, "Value "+ed_name.getText(), Toast.LENGTH_SHORT).show();
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.UrlInsertBImage);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success");
            Toast.makeText(getApplicationContext(), "Data Send Successfully", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

    }*/

    public void insert_business_data(){
        InputStream is = null;
        String result = null;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("lcid", b_lcid));
        nameValuePairs.add(new BasicNameValuePair("lcbid", b_lcbid));
        nameValuePairs.add(new BasicNameValuePair("bimg", b_img));

        Log.i("Mynewsam", "============================" + b_img);

        //Toast.makeText(RegisterActivity.this, "Value "+ed_name.getText(), Toast.LENGTH_SHORT).show();
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.UrlInsertBImage);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success");
            Toast.makeText(getApplicationContext(), "Data Send Successfully", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
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
                Intent i = new Intent(AddBProImageActivity.this, WelcomeActivity.class);
                i.putExtra(TAG_PID, uLcid);
                startActivity(i);
                finish();
            }
            is.close();

            result=sb.toString();
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error converting result "+e.toString());
        }
    }

    public class GetAllInformation extends AsyncTask {
        String response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                URL reurl = new URL(Config.UrlGetBusic+"?lcbid="+params[0]);
                URLConnection connection  = reurl.openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String temp = null;
                StringBuilder sb = new StringBuilder();
                while ((temp = br.readLine())!=null){
                    sb.append(temp);
                    sb.append("\n");
                }
                response = sb.toString();
            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(response!=null){

                try {
                    JSONObject element = new JSONObject(response);

                    String m_id1 = element.getString("lcid");
                    String bsid = element.getString("lcbid");

                    System.out.println(m_id1);
                    System.out.println(bsid);

                    tv_lcid.setText(tv_lcid.getText()+m_id1);
                    tv_lcbid.setText(tv_lcbid.getText()+bsid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
