package com.tagcor.tagcor.ProfessionalAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tagcor.tagcor.JobsBeans.HomePageRecentBean;
import com.tagcor.tagcor.ProfessionalBeans.ProfessHomePostBean;
import com.tagcor.tagcor.ProfessionalFragments.SearchResultFriendFragment;
import com.tagcor.tagcor.R;
import com.tagcor.tagcor.RetrofitHelpers.Api;
import com.tagcor.tagcor.RetrofitHelpers.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfessionalHomePostAdapter extends RecyclerView.Adapter<ProfessionalHomePostAdapter.ViewHolder>{

    List<ProfessHomePostBean> ProfessPostList;
    LayoutInflater inflater;
    Context context;
    String sessionId;

    //private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog dialog;
    TextView tv_hideme, tv_delete, tv_report;
    View modalbottomsheet;


    String uId;
    private static final String TAG_PID = "user_id";
    private boolean loggedIn = false;
    //ProfessHomePostBean item;
    public ProfessionalHomePostAdapter(Context context, List<ProfessHomePostBean> ProfessPostList){
        this.ProfessPostList = ProfessPostList;
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        //this.productList = new ArrayList<MainBean>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_profhomepost, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return ProfessPostList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProfessHomePostBean item = ProfessPostList.get(position);

        Glide.with(context).load(item.getProfileImage()).into(holder.userImg);
        holder.tv_fname.setText(item.getFname());
        holder.tv_lname.setText(item.getLname());
        holder.post_text.setText(item.getDescription());
        Glide.with(context).load(item.getProfpostImage()).into(holder.mimageView);
        holder.post_like.setText(item.getPostLike());
        holder.post_unlike.setText(item.getPostUnlike());

        modalbottomsheet = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.settings, null);
        tv_hideme = (TextView) modalbottomsheet.findViewById(R.id.btn_hideme);
        tv_delete = (TextView) modalbottomsheet.findViewById(R.id.btn_delete);
        tv_report = (TextView) modalbottomsheet.findViewById(R.id.btn_Report);

        dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        sessionId = item.getSessionId().toString();
  //   String   UserId = ProfessPostList.get(position).getUserId();
        holder.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = ProfessPostList.get(position).getUserId();
                Bundle args = new Bundle();
                args.putString(TAG_PID, user_id);
                Fragment fragment;
                fragment = new SearchResultFriendFragment();
                fragment.setArguments(args);
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if (item.getDescription().equals("")){
            holder.post_text.setVisibility(View.GONE);
        }else {
            holder.post_text.setVisibility(View.VISIBLE);
        }if (item.getProfpostImage().equals("")){
            holder.mimageView.setVisibility(View.GONE);
        }else {
            holder.mimageView.setVisibility(View.VISIBLE);
        }

        /*Bitmap mbitmap = ((BitmapDrawable) ((AppCompatActivity) context).getResources().getDrawable(R.drawable.bg)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 50, 50, mpaint);// Round Image Corner 100 100 100 100
        holder.mimageView.setImageBitmap(imageRounded);*/

        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                String UserId = ProfessPostList.get(position).getUserId();

                if (sessionId.equalsIgnoreCase(UserId)){

                    tv_report.setVisibility(View.GONE);
                    tv_hideme.setVisibility(View.VISIBLE);
                    tv_delete.setVisibility(View.VISIBLE);
                }
                else  {
                    tv_report.setVisibility(View.VISIBLE);
                    tv_hideme.setVisibility(View.VISIBLE);
                    tv_delete.setVisibility(View.GONE);
                }
              //  init_modal_bottomsheet_setting();

                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String postId = ProfessPostList.get(position).getPostId();

                        ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);
                        Call<Object> call = apiService.getProfessionalPostDelete(postId);
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
                                        JSONObject json2 = json.getJSONObject("Deleteprofessionalpost");

                                        String statusCode = json2.getString("StatusCode");

                                        if (statusCode.contains("200")) {
                                            //JSONArray obj = json2.getJSONArray("Data");
                                            //JSONObject element = json2.getJSONObject("Data");

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
                                Toast.makeText(context, "Network error, Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });

                        Fragment frg = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                        dialog.dismiss();
                    }
                });


                tv_hideme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String post_id = ProfessPostList.get(position).getPostId();
                        //String user_id = ProfessPostList.get(position).getSessionId();
                        ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);
                        Call<Object> call = apiService.getProfessionalPostHideMe(sessionId, post_id);
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
                                        JSONObject json2 = json.getJSONObject("Homepagehideprofessionalpost");

                                        String statusCode = json2.getString("StatusCode");

                                        if (statusCode.contains("200")) {
                                            //JSONArray obj = json2.getJSONArray("Data");
                                            //JSONObject element = json2.getJSONObject("Data");

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
                                Toast.makeText(context, "Network error, Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });

                        Fragment frg = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                        dialog.dismiss();
                    }
                });

                tv_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String post_id = ProfessPostList.get(position).getPostId();
                        //String user_id = ProfessPostList.get(position).getSessionId();
                        ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);
                        Call<Object> call = apiService.getProfessionalPostReport(sessionId, post_id);
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
                                        JSONObject json2 = json.getJSONObject("Reportprofessionalpost");

                                        String statusCode = json2.getString("StatusCode");

                                        if (statusCode.contains("200")) {
                                            //JSONArray obj = json2.getJSONArray("Data");
                                            //JSONObject element = json2.getJSONObject("Data");

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
                                Toast.makeText(context, "Network error, Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });

                        Fragment frg = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                        dialog.dismiss();
                    }
                });

            }
        });


        holder.likedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String post_id = ProfessPostList.get(position).getPostId();
                String usere_id = ProfessPostList.get(position).getSessionId();
                String posted_pers_id = ProfessPostList.get(position).getUserId();
                ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);
                Call<Object> call = apiService.getProfessionalPostlike(post_id,usere_id,posted_pers_id);
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
                                JSONObject json2 = json.getJSONObject("Professionalpostlike");

                                String statusCode = json2.getString("StatusCode");

                                if (statusCode.contains("200")) {
                                    //JSONArray obj = json2.getJSONArray("Data");
                                    //JSONObject element = json2.getJSONObject("Data");

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
                        Toast.makeText(context, "Network error, Please try again.", Toast.LENGTH_LONG).show();
                    }
                });

                Fragment frg = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });

        holder.unlikedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String post_id = ProfessPostList.get(position).getPostId();
                String usere_id = ProfessPostList.get(position).getSessionId();
                String posted_pers_id = ProfessPostList.get(position).getUserId();
                ApiInterface apiService = Api.getClientProfessional().create(ApiInterface.class);
                Call<Object> call = apiService.getProfessionalPostUnlike(post_id,usere_id,posted_pers_id);
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
                                JSONObject json2 = json.getJSONObject("ProfessionalpostUnlike");

                                String statusCode = json2.getString("StatusCode");

                                if (statusCode.contains("200")) {
                                    //JSONArray obj = json2.getJSONArray("Data");
                                    //JSONObject element = json2.getJSONObject("Data");

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
                        Toast.makeText(context, "Network error, Please try again.", Toast.LENGTH_LONG).show();
                    }
                });

                Fragment frg = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frame_container);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mimageView, settings, likedImg, unlikedImg;
        public TextView tv_fname, tv_lname, post_text, post_like, post_unlike;
        public CircleImageView userImg;

        public ViewHolder(View itemView) {
            super(itemView);
            //CompLogo = (ImageView) itemView.findViewById(R.id.image);
            //tv_jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);

            userImg = (CircleImageView) itemView.findViewById(R.id.user_img);
            tv_fname = (TextView) itemView.findViewById(R.id.tv_fname);
            tv_lname = (TextView) itemView.findViewById(R.id.tv_lname);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            post_like = (TextView) itemView.findViewById(R.id.post_liked);
            post_unlike = (TextView) itemView.findViewById(R.id.post_unliked);

            mimageView = (ImageView) itemView.findViewById(R.id.post_img);
            settings = (ImageView) itemView.findViewById(R.id.more_op);

            likedImg = (ImageView) itemView.findViewById(R.id.liked);
            unlikedImg = (ImageView) itemView.findViewById(R.id.unliked);
        }
    }

    private void init_modal_bottomsheet_setting() {

    }
}

