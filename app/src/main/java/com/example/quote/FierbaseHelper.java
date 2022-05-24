package com.example.quote;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.quote.Models.QuoteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FierbaseHelper{

    DBHelper helper;
    Context context;
    boolean isConnected;
    FirebaseDatabase database;
    static FierbaseHelper Fhelper;

    public boolean isConnected() {
        //isConnected = false;
        DatabaseReference connectedRef = database.getReference("connected");
        //connectedRef.setValue("yes");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(value!=null&&value.equals("yes")){
                    isConnected = true;
                    Log.d(TAG,"get data from server connected.");
                }else{
                    isConnected = false;
                    Log.d(TAG,"get data from server not connected.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                isConnected = false;
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
        return isConnected;
    }

    private FierbaseHelper(Context context){
        database = FirebaseDatabase.getInstance();
        this.context = context;
        helper = new DBHelper(context);
        isConnected = false;
    }
    public static FierbaseHelper getHelper(Context context){
        if(Fhelper==null){
            Fhelper = new FierbaseHelper(context);
        }
        return Fhelper;
    }

    public void writeData(){

        Log.d(TAG,"In write method.");
        if(isConnected()){
            Log.d(TAG,"Try to write data");
            Cursor cursor = helper.getLoadCursor();
            if(cursor.moveToFirst()){
                while(cursor.moveToNext()){
                    QuoteModel model = new QuoteModel(cursor.getInt(0),cursor.getString(1),cursor.
                            getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5));
                    DatabaseReference writeRef = database.getReference().child("Quotes");
                    writeRef.child(model.getUid()).setValue(model);

                    Log.d(TAG,"data push success quote is : "+model.getQuote());
                    //Toast.makeText(context,"data push succes name : "+cursor.getString(2),Toast.LENGTH_SHORT).show();

                    DatabaseReference checkerRef = database.getReference().child("Quotes").child(model.getUid());
                    checkerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            QuoteModel model1 = dataSnapshot.getValue(QuoteModel.class);
                            if(model1!=null) {
                                Log.d(TAG, "Delete from load name is: " + model1.getQuoterName());
                                helper.deleteQuore("Load", model1.getId());
                            }else{
                                Log.d(TAG, "data not available to delete from database." + model.getQuoterName());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to Delete value.", error.toException());
                        }
                    });
                }
            }else {
                Log.d(TAG, "table empty.");
            }
            Log.d(TAG, "All data write finished.");
        }else {
            Log.d(TAG, "Not connected wet.");
        }
    }

    public void readData(){
        Log.d(TAG,"In read method.");
        DatabaseReference readRef = database.getReference().child("Quotes");
        readRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG,"geting childern. size is : "+dataSnapshot.getChildrenCount());
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    QuoteModel model1 = ds.getValue(QuoteModel.class);
                    if(!helper.isExists(model1.getUid())) {
                        helper.insertQuote(model1.getUid(), model1.getQuoterName(), model1.getQuote(), model1.getImage(), false);
                        Log.d(TAG, "Inserted from firebase named : " + model1.getQuoterName());
                    }else{
                        Log.d(TAG, "Already exists from firebase named : " + model1.getQuoterName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Log.d(TAG,"Read finished.");
    }

    public boolean deleteData(String uid){
        if(isConnected()){
            DatabaseReference deleteRef = database.getReference().child("Quotes").child(uid);
            deleteRef.removeValue();
            return true;
        }else{
            return false;
        }
    }
}
