package com.ikhokha.techcheck.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.CartItem;
import com.ikhokha.techcheck.model.util.Constants;
import com.ikhokha.techcheck.model.util.ScreenShot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView dateTimeTextView;
    private TextView totalTextView;
    private ImageButton whatsappShare;
    private ImageButton googleShare;
    private Double total = 0.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initWidgets();

        initDynamicRows();

        total = calculateToatl(Constants.constantList);

        totalTextView.setText(String.valueOf(total));
    }

    private void initWidgets(){
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        totalTextView = findViewById(R.id.totalTextView);
        whatsappShare = findViewById(R.id.whatsappImgBtn);
        googleShare = findViewById(R.id.googleImgBtn);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String dateTime = df.format(Calendar.getInstance().getTime());

        dateTimeTextView.setText(dateTime);

        whatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Figure out saving screenshot directly
                storeImage(ScreenShot.takeScreenShotOfRootView(v));

                /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse();

                sharingIntent.setType("image/png");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));*/

            }
        });

        googleShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initDynamicRows(){

        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout);


        int i = 0;
        for (CartItem item: Constants.constantList) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,20);
            row.setLayoutParams(lp);

            TextView descriptionTextView = new TextView(this);
            TextView priceTextView = new TextView(this);

            descriptionTextView.setText(item.getDescription());
            descriptionTextView.setTextSize(25);
            descriptionTextView.setTypeface(descriptionTextView.getTypeface(), Typeface.BOLD);
            descriptionTextView.setLayoutParams(lp);

            priceTextView.setText(String.valueOf(item.getPrice()));
            priceTextView.setTextSize(20);
            priceTextView.setTypeface(priceTextView.getTypeface(), Typeface.BOLD);
            priceTextView.setLayoutParams(lp);

            View view = new View(this);

            row.addView(descriptionTextView);
            row.addView(view);
            row.addView(priceTextView);
            ll.addView(row,i);

            i++;
        }
    }

    private double calculateToatl(List<CartItem> items){
        double total = 0.0;
        for (CartItem cartItem: items ){
            total = total + cartItem.getPrice();
        }
        return total;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(this.getClass().getName(),
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getName(), "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(this.getClass().getName(), "Error accessing file: " + e.getMessage());
        }
    }


    private  File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

}
