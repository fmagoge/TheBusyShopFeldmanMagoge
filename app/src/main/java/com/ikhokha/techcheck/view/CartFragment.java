package com.ikhokha.techcheck.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.util.Constants;


public class CartFragment extends Fragment {

    public CartFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cart_frame_layout, container, false);
        TextView cartCoutTextView = view.findViewById(R.id.shopping_cart_items);
        Button checkoutButton = view.findViewById(R.id.shopping_cart_button);

        int numberOfListItems = Constants.constantList.size();
        if (Constants.constantList.size()<1 ){
            numberOfListItems = 0;
        }
        cartCoutTextView.setText(String.valueOf(numberOfListItems));

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "CHECKOUT ITEMS IN CART: "+Constants.constantList.size(),Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
