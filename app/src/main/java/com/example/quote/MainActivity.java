package com.example.quote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_share;
    ImageButton edit,delete;
    TextView quoterName,quote;
    ImageView image,heart;
    boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();

        btn_share = findViewById(R.id.btn_share);
        edit = findViewById(R.id.btn_edit);
        delete = findViewById(R.id.btn_delete);
        quoterName = findViewById(R.id.tv_name);
        quote = findViewById(R.id.tv_quote);
        image = findViewById(R.id.iv_image);
        heart = findViewById(R.id.iv_heart);

        quoterName.setText(getIntent().getStringExtra("quoterName"));
        quote.setText(getIntent().getStringExtra("quote"));
        image.setImageResource(getIntent().getIntExtra("image",R.drawable.me));
        if(getIntent().getIntExtra("isFav",0)==1) {
            heart.setImageResource(R.drawable.heartg);
            isFav = true;
        }else{
            heart.setImageResource(R.drawable.heart);
            isFav = false;
        }

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFav){
                    heart.setImageResource(R.drawable.heart);
                    isFav = false;
                }else{
                    heart.setImageResource(R.drawable.heartg);
                    isFav = true;
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreateQuoteActivity.class);
                intent.putExtra("id",getIntent().getIntExtra("id",-1));
                finish();
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setTitle("Delete")
                        .setMessage("Are you sure to delete permanently.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FierbaseHelper fhelper = FierbaseHelper.getHelper(MainActivity.this);
                                if(fhelper.deleteData(getIntent().getStringExtra("uid"))) {
                                    DBHelper helper = new DBHelper(MainActivity.this);
                                    long isDeleted = helper.deleteQuore("Quotes", getIntent().getIntExtra("id", 0));
                                    if (isDeleted <= 0) {
                                        Toast.makeText(MainActivity.this, "delete fail", Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(MainActivity.this, QuotesActivity.class));
                                        finish();
                                    }
                                }else{
                                    Toast.makeText(MainActivity.this,"You are not connected to the internet",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,"Your deleted Quote never be found.",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });


        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,quote.getText()+"\nBy: "+quoterName.getText());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DBHelper helper = new DBHelper(MainActivity.this);
        if(isFav&&getIntent().getIntExtra("isFav",0)==0){
            if(helper.UpdateQuote(getIntent().getIntExtra("id",0),getIntent().getStringExtra("uid"),
                    getIntent().getStringExtra("quoterName"), getIntent().getStringExtra("quote"),
                    getIntent().getIntExtra("image",R.drawable.me),1)){
                Toast.makeText(MainActivity.this, "Update success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
            }
        }
        if(!isFav&&getIntent().getIntExtra("isFav",0)==1){
            if(helper.UpdateQuote(getIntent().getIntExtra("id",0),getIntent().getStringExtra("uid"),
                    getIntent().getStringExtra("quoterName"), getIntent().getStringExtra("quote"),
                    getIntent().getIntExtra("image",R.drawable.me),0)){
                Toast.makeText(MainActivity.this, "Update success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Update Fail", Toast.LENGTH_SHORT).show();
            }
        }
        startActivity(new Intent(MainActivity.this,QuotesActivity.class));
    }
}