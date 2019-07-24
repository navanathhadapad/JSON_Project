package com.tagcor.tagcor.B2BAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tagcor.tagcor.B2BBeans.B2BProductListBean;
import com.tagcor.tagcor.B2BFragments.BuyNowFragment;
import com.tagcor.tagcor.R;


import java.util.List;


public class B2BProductListAdapter extends RecyclerView.Adapter<B2BProductListAdapter.ViewHolder>{

    List<B2BProductListBean> Productlist;
    LayoutInflater inflater;
    private Context context;
    String uId;
    private static final String TAG_PID = "user_id";

    public B2BProductListAdapter(Context context, List<B2BProductListBean> productList){
        this.Productlist = productList;
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        //this.productList = new ArrayList<MainBean>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_productlistbtob, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return Productlist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        B2BProductListBean item = Productlist.get(position);

        //Picasso.with(context).load(item.getImgurl()).placeholder(R.drawable.process_gif).into(holder.productImg);
        Glide.with(context).load(item.getB2bproductImage()).into(holder.pro_img);

        holder.pro_name.setText(item.getProductTitle());
        holder.pro_units.setText(item.getMinOrder());
        holder.price.setText(item.getPrice());

        holder.pro_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_id = Productlist.get(position).getProductId();
                Bundle args = new Bundle();
                args.putString(TAG_PID, post_id);
                Fragment fragment;
                fragment = new BuyNowFragment();
                fragment.setArguments(args);
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView pro_img;
        public TextView pro_name, pro_units, price;

        public ViewHolder(View itemView) {
            super(itemView);
            pro_img = (ImageView) itemView.findViewById(R.id.pro_img);
            pro_name = (TextView) itemView.findViewById(R.id.pro_name);
            pro_units = (TextView) itemView.findViewById(R.id.minUnits);
            price = (TextView) itemView.findViewById(R.id.pro_price);

        }

    }

}
