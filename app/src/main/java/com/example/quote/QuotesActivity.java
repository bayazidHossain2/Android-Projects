package com.example.quote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quote.Adapters.QuoteAdapter;
import com.example.quote.Models.QuoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuotesActivity extends AppCompatActivity {

    RecyclerView rv_quots;
    FloatingActionButton add;
    TextView connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        rv_quots = findViewById(R.id.rv_quotes);
        add = findViewById(R.id.btn_add);
        connected = findViewById(R.id.tv_connected);

        FierbaseHelper fierbaseHelper = FierbaseHelper.getHelper(QuotesActivity.this);
        fierbaseHelper.readData();

        if(fierbaseHelper.isConnected()){
            connected.setText("Connected");
        }else{
            connected.setText("Not connected");
        }

        DBHelper helper = new DBHelper(this);
        ArrayList<QuoteModel> list = helper.getQuotes();
        list.add(new QuoteModel(1000,"","Mr Author","আমি পারি আমি করব, আমার জীবন আমি গড়বো।",R.drawable.me,1));
        list.add(new QuoteModel(1001,"","মালিক","আজ যদি তুমি ঘাম না ঝরাও কাল তোমাকে রক্ত ঝড়াতে হবে।",R.drawable.me,1));

        QuoteAdapter adapter = new QuoteAdapter(list,QuotesActivity.this);
        rv_quots.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(QuotesActivity.this);
        rv_quots.setLayoutManager(llm);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuotesActivity.this,CreateQuoteActivity.class);
                intent.putExtra("id",-1);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Toast.makeText(QuotesActivity.this,"Load Quors pushed",Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.load:
                finish();
                startActivity(new Intent(QuotesActivity.this,LoadActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(QuotesActivity.this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Exit")
                .setMessage("Are you sure to exit")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(QuotesActivity.this,"To Exit click yes.",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}