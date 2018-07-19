package com.decimalcorp.aditya.creditmanagementapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.AppDatabase;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.Transfer;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.TransferTableInterface;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.history_list);

        GetList getList = new GetList(this);
        getList.execute();
    }

    class GetList extends AsyncTask<Void,Void,List<Transfer>>{
        Context context;
        GetList(Context context){
            this.context = context;
        }

        @Override
        protected List<Transfer> doInBackground(Void... voids) {

            AppDatabase appDatabase =AppDatabase.getAppDatabase(context);
            TransferTableInterface transferTableInterface = appDatabase.transferTableInterface();
            List<Transfer> mList = transferTableInterface.getAll();
            return mList;
        }

        @Override
        protected void onPostExecute(List<Transfer> transfers) {
            super.onPostExecute(transfers);

            ListAdapter adapter = new ListAdapter(context,transfers);
            listView.setAdapter(adapter);
        }
    }

    class ListAdapter extends BaseAdapter{

        Context context;
        List<Transfer> mList;

        ListAdapter(Context context, List<Transfer> list){
            this.context = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.history_item, null);
            }

            //TextView tv = (TextView) convertView.findViewById(R.id.time_transfer);
            TextView tv1 = convertView.findViewById(R.id.from);
            TextView tv2 = convertView.findViewById(R.id.to);
            TextView tv3 = convertView.findViewById(R.id.score_transfer);

            String time = mList.get(position).getTime();
            String from = mList.get(position).getName1();
            String to = mList.get(position).getName2();
            int sc = mList.get(position).getAmount();

            //tv.setText(time);
            tv1.setText(from);
            tv2.setText(to);
            tv3.setText(sc +" ");

            return convertView;
        }
    }
}
