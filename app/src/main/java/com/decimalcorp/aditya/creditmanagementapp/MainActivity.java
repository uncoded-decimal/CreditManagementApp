package com.decimalcorp.aditya.creditmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.AppDatabase;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.DataExchange;
import com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<User> mList;
    ListView listView;
    ListAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        GetList newList = new GetList(this);
        newList.execute();
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.text);
        mList = new ArrayList<User>();
        GetList getList = new GetList(this);
        getList.execute();

        listView = (ListView) findViewById(R.id.userList);
        if(mList != null) {
            adapter = new ListAdapter(this, mList);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("%%%%", "ON ITEM CLICK");

                    Intent i = new Intent(MainActivity.this,UserInfo.class);
                    i.putExtra("UID", mList.get(position).getUid());
                    i.putExtra("NAME", mList.get(position).getName());
                    i.putExtra("SCORE", mList.get(position).getScore());
                    i.putExtra("EMAIL",mList.get(position).getEmail());
                    startActivity(i);
                }
            });
        }

        Button button = (Button) findViewById(R.id.history);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
            }
        });
    }

    public  class GetList extends AsyncTask<Void, Void, List<User>> {

        Context context;
        DataExchange dataExchange;
        List<User> list;

        public GetList(Context context){
            this.context = context;
            Log.d("%%%%", "in getList");
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            AppDatabase database = AppDatabase.getAppDatabase(context);
            dataExchange = database.dataExchange();
            list = dataExchange.getAll();
            Log.d("%%%%", "in DO IN BACKGROUND");
            Log.d("%%%%", "SIZE I N BAckground =" + list.size());
            return list;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            Log.d("%%%%", "in ON POST EXECUTE");
            mList = users;
            Log.d("%%%%", "SIZE I N SYNK =" + users.size());
            ListAdapter adapter = new ListAdapter(context, users);
            listView.setAdapter(adapter);
        }
    }

    class ListAdapter extends BaseAdapter {
        Context context;
        List<User> userList;

        ListAdapter(Context context, List<User> users){
            this.context = context;
            userList = users;
        }

        @Override
        public int getCount() {
            Log.d("%%%%", "List size = "+ userList.size());
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_layout, null);
            }
            TextView tv1 = convertView.findViewById(R.id.user_name);
            TextView tv2 = convertView.findViewById(R.id.user_score);

            String name = userList.get(position).getName();
            int score = userList.get(position).getScore();
            tv1.setText(name);
            tv2.setText(" "+score);

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }
}


