package com.example.quote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.quote.Adapters.LoadAdapter;
import com.example.quote.Adapters.QuoteAdapter;
import com.example.quote.Models.QuoteModel;

import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    RecyclerView LoadQuots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        LoadQuots = findViewById(R.id.rv_loadQuots);

        ArrayList<QuoteModel> list = new ArrayList<>();

        DBHelper helper = new DBHelper(LoadActivity.this);
        Cursor cursor = helper.getLoadCursor();
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                QuoteModel model = new QuoteModel(cursor.getInt(0),cursor.getString(1),cursor.
                        getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5));
                //Toast.makeText(context, model.getQuoterName(), Toast.LENGTH_SHORT).show();
                list.add(model);
            }
        }
        cursor.close();

        QuoteAdapter adapter = new QuoteAdapter(list,LoadActivity.this);
        LoadQuots.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(LoadActivity.this);
        LoadQuots.setLayoutManager(llm);
        FierbaseHelper fierbaseHelper = FierbaseHelper.getHelper(LoadActivity.this);
        fierbaseHelper.writeData();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(LoadActivity.this,QuotesActivity.class));
    }
}