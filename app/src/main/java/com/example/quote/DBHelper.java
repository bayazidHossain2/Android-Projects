package com.example.quote;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.method.QwertyKeyListener;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quote.Models.QuoteModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    final static String NAME = "QuoteDatabase";
    final static int Version = 8;
    Context context;

    public DBHelper(@Nullable Context context) {
        super(context, NAME, null, Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "Create table Quotes"+
                        "(id integer primary key autoincrement,"+
                        "uniqueId text,"+
                        "Name text,"+
                        "Quote text,"+
                        "Image int,"+
                        "IsSaved int)"
        );
        sqLiteDatabase.execSQL(
                "Create table Load"+
                        "(id integer primary key autoincrement,"+
                        "uniqueId text,"+
                        "Name text,"+
                        "Quote text,"+
                        "Image int,"+
                        "IsSaved int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP table if Exists Quotes");
        sqLiteDatabase.execSQL("DROP table if Exists Load");
        onCreate(sqLiteDatabase);
    }

    public boolean insertQuote(String id,String name,String quote,int image,boolean addToLoad){
        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("uniqueId",id);
        values.put("Name",name);
        values.put("Quote",quote);
        values.put("Image",image);
        values.put("IsSaved",0);

        long pushed = database.insert("Quotes",null,values);
        if(pushed<=0){
            return false;
        }else{
            if(addToLoad) {
                AddToLoad(values);
            }
            return true;
        }
    }

    public boolean UpdateQuote(int id,String uid,String name,String quote,int image,int isFav){
        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("uniqueId",uid);
        values.put("Name",name);
        values.put("Quote",quote);
        values.put("Image",image);
        values.put("IsSaved",isFav);
        long pushed = database.update("Quotes",values,"id="+id,null);
        if(pushed<=0){
            return false;
        }else{
            AddToLoad(values);
            return true;
        }
    }

    public void AddToLoad(ContentValues values){
        SQLiteDatabase database = getReadableDatabase();
        long pushed = database.insert("Load",null,values);
        if(pushed<=0){
            Log.d(TAG,"Insert Fail to Load table.");
        }else {
            FierbaseHelper fhelper = FierbaseHelper.getHelper(context);
            fhelper.writeData();
            Log.d(TAG,"Success to insert in Load table.");
        }
    }

    public Cursor getLoadCursor(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Load",null);
        return cursor;
    }
    public long deleteQuore(String table,int id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(table,"id="+id,null);
    }

    public ArrayList<QuoteModel> getQuotes(){
        ArrayList<QuoteModel> quotes = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Quotes",null);
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                QuoteModel model = new QuoteModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.
                        getString(3),cursor.getInt(4),cursor.getInt(5));
                //Toast.makeText(context, model.getQuoterName(), Toast.LENGTH_SHORT).show();
                quotes.add(model);
            }
        }
        quotes = reverse(quotes,0,new ArrayList<>());
        cursor.close();
        database.close();
        return quotes;
    }

    ArrayList<QuoteModel> reverse(ArrayList<QuoteModel> old, int ind, ArrayList<QuoteModel> n){
        if(old.size()==ind){
            return n;
        }
        reverse(old,ind+1,n);
        n.add(old.get(ind));
        return n;
    }

    Cursor getCursorById(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Quotes where id="+id,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean isExists(String uid){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Quotes where uniqueId="+uid,null);
        Log.d(TAG,"Cursor is : "+cursor);
        try{
            cursor.moveToFirst();
            Log.d(TAG,"Exists uid : "+uid+" Name is : "+cursor.getString(2));
            return true;
        }catch (Exception ex){
            Log.d(TAG,"Not exists : "+uid);
            return false;
        }
    }

}
