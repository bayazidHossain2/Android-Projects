package com.example.quote.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quote.MainActivity;
import com.example.quote.Models.QuoteModel;
import com.example.quote.QuotesActivity;
import com.example.quote.R;

import java.util.ArrayList;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.viewHolder>{

    ArrayList<QuoteModel> list;
    Context context;

    public QuoteAdapter(ArrayList<QuoteModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_quote_sample,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        QuoteModel model = list.get(i);
        viewHolder.iv.setImageResource(model.getImage());
        if(model.getQuote().length()>30){
            viewHolder.tv_shortDesc.setText(model.getQuote().substring(0,30)+"...");
        }else{
            viewHolder.tv_shortDesc.setText(model.getQuote());
        }
        viewHolder.tv_quoterName.setText(model.getQuoterName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("quoterName",model.getQuoterName());
                intent.putExtra("image",model.getImage());
                intent.putExtra("quote",model.getQuote());
                intent.putExtra("id",model.getId());
                intent.putExtra("uid",model.getUid());
                intent.putExtra("isFav",model.getIsFav());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv_quoterName, tv_shortDesc;
        public viewHolder(@NonNull View itemView){
            super(itemView);
            tv_quoterName = itemView.findViewById(R.id.tv_name);
            tv_shortDesc = itemView.findViewById(R.id.tv_short);
            iv = itemView.findViewById(R.id.imageView);
        }
    }
}
