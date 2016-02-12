package fr.iutinfo.androidclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.iutinfo.androidclient.bean.User;


public class MainActivity extends ActionBarActivity {

    private final String URL = "http://192.168.1.18:8080/v1/user";

    private TextView mTextView;
    private ListView mListView;
    private ProgressBar mProgressBar;

    private List<String> users;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activity_main_title);

        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        users = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, users);
        mListView.setAdapter(mAdapter);

        load();
    }

    private void load() {

        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        mProgressBar.setVisibility(View.GONE);

                        final Gson gson = new GsonBuilder().create();

                        Type listType = new TypeToken<List<User>>() {
                        }.getType();
                        List<User> userList = gson.fromJson(json, listType);

                        users.clear();
                        for (User user : userList) {
                            users.add(user.getName() + " (" + user.getAlias() + ")");
                        }

                        if (users.isEmpty()) {
                            mTextView.setText(getString(R.string.empty_list));
                            mTextView.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {
                            mTextView.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }

                        mListView.invalidateViews();
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressBar.setVisibility(View.GONE);
                mTextView.setText(getString(R.string.error, error.getMessage()));
                mTextView.setVisibility(View.VISIBLE);
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            load();
            return true;
        } else if (id == R.id.action_add) {

            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
