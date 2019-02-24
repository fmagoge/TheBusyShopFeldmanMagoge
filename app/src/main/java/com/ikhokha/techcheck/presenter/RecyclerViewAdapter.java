package com.ikhokha.techcheck.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.Item;
import com.ikhokha.techcheck.view.MainActivity;

import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private ArrayList<HashMap<String, Item>> dataMapArrayList;
    private HashMap<String, Item> resultMap = new HashMap<>();
    private List<String> resultMapKeys = new ArrayList<>();
    public OnItemClickListener onItemClickListener;
    ImageView androidIMGGlobal;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public static  class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView descriptionText;
        public TextView priceText;
        public final NetworkImageView imageView;

        public RecyclerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            priceText = itemView.findViewById(R.id.priceText);
            imageView =  itemView.findViewById(R.id.imgView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerViewAdapter( ArrayList<HashMap<String, Item>> arraylist, Context context) {
        this.dataMapArrayList = arraylist;
        this.context = context;

        imageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, onItemClickListener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        resultMap = dataMapArrayList.get(position);

        //Get keys from HashMap
        for (String str: resultMap.keySet()){
            resultMapKeys.add(str);
        }

        Object getRowOnItem = resultMap.get(resultMapKeys.get(position));

        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) getRowOnItem;
        String description = t.get("description").toString();
        String price = t.get("price").toString();
        String image = t.get("image").toString();

        resultMap.get(resultMapKeys.get(position));

        if (resultMap != null) {
                holder.descriptionText.setText(description);

                DecimalFormat df2 = new DecimalFormat(".##");
                Double priceD = Double.parseDouble(price);
                df2.setRoundingMode(RoundingMode.UP);
                holder.priceText.setText(df2.format(priceD));

                imageLoader.get("gs://the-busy-shop.appspot.com/" + image, ImageLoader.getImageListener(holder.imageView, R.mipmap.loading, R.mipmap.ic_launcher));
                holder.imageView.setImageUrl("gs://the-busy-shop.appspot.com/" + image, imageLoader);

                androidIMGGlobal = holder.imageView;
        }
    }

    @Override
    public int getItemCount() {
        return dataMapArrayList.size();
    }

    public void callBack(int position){
        resultMap = dataMapArrayList.get(position);


    }
   

}
