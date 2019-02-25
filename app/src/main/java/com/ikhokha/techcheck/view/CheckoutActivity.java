package com.ikhokha.techcheck.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ikhokha.techcheck.R;
import com.ikhokha.techcheck.model.CartItem;
import com.ikhokha.techcheck.model.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView dateTimeTextView;
    private TextView totalTextView;
    private Button shareButtn;
    private Double total = 0.00;
    String imageFilePath;
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
        shareButtn = findViewById(R.id.shareBtn);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String dateTime = df.format(Calendar.getInstance().getTime());

        dateTimeTextView.setText(dateTime);

        shareButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeScreenshot();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpg");
                final File photoFile = new File(imageFilePath);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
                startActivity(Intent.createChooser(shareIntent, "Share image using"));

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

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            imageFilePath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(imageFilePath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}
