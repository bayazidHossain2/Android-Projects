package com.example.quote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class CreateQuoteActivity extends AppCompatActivity {

    ImageView setImage;
    FloatingActionButton addImage;
    EditText name,Quote;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quote);

        setImage = findViewById(R.id.iv_setImage);
        addImage = findViewById(R.id.btn_addImage);
        name = findViewById(R.id.et_name);
        Quote = findViewById(R.id.et_quote);
        submit = findViewById(R.id.btn_submit);
        DBHelper helper = new DBHelper(CreateQuoteActivity.this);

        int id = getIntent().getIntExtra("id",-1);
        if(id==-1) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sname = name.getText().toString();
                    String squote = Quote.getText().toString();

                    if (sname.isEmpty() || squote.isEmpty()) {
                        Toast.makeText(CreateQuoteActivity.this, "Name or quote are not filled.", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isInserted = helper.insertQuote(new Date().getTime()+"",sname, squote, R.drawable.me,true);
                        if (isInserted) {
                            Toast.makeText(CreateQuoteActivity.this, "Data insert success.", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            Quote.setText("");
                        } else {
                            Toast.makeText(CreateQuoteActivity.this, "Data insert Fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }else{
            Cursor cursor = helper.getCursorById(id);
            name.setText(cursor.getString(2));
            Quote.setText(cursor.getString(3));
            submit.setText("Update Now");

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sname = name.getText().toString();
                    String squote = Quote.getText().toString();

                    if (sname.isEmpty() || squote.isEmpty()) {
                        Toast.makeText(CreateQuoteActivity.this, "Name or quote are not filled.", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isUpdated = helper.UpdateQuote(id,cursor.getString(1),sname, squote, R.drawable.me,0);
                        if (isUpdated) {
                            Toast.makeText(CreateQuoteActivity.this, "Data Update success.", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(CreateQuoteActivity.this, "Data Update Fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(CreateQuoteActivity.this,QuotesActivity.class));
    }
}