package com.ikhokha.techcheck.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.internal.LinkedTreeMap;
import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.CartItem;
import com.ikhokha.techcheck.model.Item;
import com.ikhokha.techcheck.model.util.Constants;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Item>> dataMapArrayList;
    private HashMap<String, Item> resultMap = new HashMap<>();
    private List<String> resultMapKeys = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public static  class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView descriptionText;
        public TextView priceText;
        public final ImageView imageView;

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

        FirebaseStorage storage = FirebaseStorage.getInstance();


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

                StorageReference storageReference = storage.getReferenceFromUrl("gs://the-busy-shop.appspot.com/"+image);
                Glide.with(context)
                    .load(storageReference)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return dataMapArrayList.size();
    }

    public void callBack(int position){
        HashMap<String, Item> localResultMap = new HashMap<>();
        localResultMap = dataMapArrayList.get(position);

        Object getRowOnItem = localResultMap.get(resultMapKeys.get(position));

        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) getRowOnItem;
        String description = t.get("description").toString();
        String price = t.get("price").toString();

        CartItem cartItem = new CartItem();
        cartItem.setDescription(description);
        cartItem.setPrice(Double.parseDouble(price));

        Constants.constantList.add(cartItem);

    }
   

}
