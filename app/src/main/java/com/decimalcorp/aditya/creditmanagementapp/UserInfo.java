package com.decimalcorp.aditya.creditmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.AppDatabase;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.DataExchange;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.Transfer;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.TransferTableInterface;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.User;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class UserInfo extends AppCompatActivity {
    int uid=0;
    int score =0;
    String name = null;
    String e_mail=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        uid = b.getInt("UID");
        score = b.getInt("SCORE");
        name = b.getString("NAME");
        e_mail = b.getString("EMAIL");

        TextView tv = (TextView) findViewById(R.id.name);
        tv.setText(name);
        TextView tv1 = (TextView) findViewById(R.id.score);
        tv1.setText("Credits = "+score);
        TextView tv2 = (TextView) findViewById(R.id.email);
        tv2.setText(e_mail);
        final AutoCompleteTextView atv = (AutoCompleteTextView) findViewById(R.id.auto_text);
        final EditText et = (EditText) findViewById(R.id.credit_amount);
        final Button send = (Button) findViewById(R.id.send);

        Log.d("%%%%", "Name = " + name + "\nScore = "+score);

        Button bTransfer = (Button) findViewById(R.id.transfer);
        bTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(atv.getVisibility() == View.GONE || atv.getVisibility() == View.GONE )
                {atv.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);}
                else{
                    atv.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                send.setVisibility(View.GONE);}
            }
        });

        String[] ar = new String[]{"USER 1","USER 2","USER 3","USER 4","USER 5","USER 6","USER 7","USER 8","USER 9","USER 10",};
        ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, ar);
        atv.setThreshold(1);
        atv.setAdapter(arrayAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt(et.getText().toString());
                String u=atv.getText().toString();

                if(amount> score)
                    Toast.makeText(getBaseContext(),"Transfer amount greater than avaiable amount", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(getBaseContext(),"Transferring "+amount+" to "+u, Toast.LENGTH_SHORT).show();
                    TransferCredits transferCredits = new TransferCredits(getBaseContext(),u);
                    transferCredits.execute(amount);
                }
            }
        });
    }

    class TransferCredits extends AsyncTask<Integer, Void, Void>{

        String to;
        Context context;
        DataExchange dataExchange;
        TransferTableInterface transferTableInterface;

        TransferCredits(Context context, String to){
            this.context = context;
            this.to = to;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            AppDatabase database = AppDatabase.getAppDatabase(context);
            dataExchange = database.dataExchange();
            transferTableInterface = database.transferTableInterface();
            dataExchange.update(new User(uid,name,score-ints[0],e_mail));
            User receiver = dataExchange.findByName(to);
            dataExchange.update(
                    new User(receiver.getUid(),receiver.getName(),
                            receiver.getScore() + ints[0],receiver.getEmail()));
            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat current = new SimpleDateFormat("dd/mm/yyyy");
            String time = current.format(currentDate);
            Transfer transfer = new Transfer(time, name, to, ints[0]);
            transferTableInterface.insertAll(transfer);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context,"Transfer Successful!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
