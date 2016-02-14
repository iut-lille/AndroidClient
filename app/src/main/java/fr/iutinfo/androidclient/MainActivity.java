package fr.iutinfo.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.iutinfo.androidclient.bean.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static fr.iutinfo.androidclient.Configuration.SERVER;


public class MainActivity extends ActionBarActivity {

    private final String URL = SERVER + "/v1/user";

    private TextView textError;
    private ListView listOfUsersView;
    private ProgressBar progressBar;

    private List<User> users;
    private ArrayAdapter<String> mAdapter;
    private List<String> values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activity_main_title);

        textError = (TextView) findViewById(R.id.text);
        listOfUsersView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        users = new ArrayList<>();
        values = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, values);
        listOfUsersView.setAdapter(mAdapter);

        listOfUsersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("id", users.get(i).getId());
                intent.putExtra("name", users.get(i).getName());
                intent.putExtra("alias", users.get(i).getAlias());
                startActivity(intent);
            }
        });

        load();
    }

    private void load() {

        progressBar.setVisibility(View.VISIBLE);
        textError.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        progressBar.setVisibility(View.GONE);
                        buildUsersFromJson(json);
                        buildValues();
                        showList();
                        listOfUsersView.invalidateViews();
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
            }
        });

        queue.add(stringRequest);
    }

    private void showError(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        textError.setText(getString(R.string.error, error.getMessage()));
        textError.setVisibility(View.VISIBLE);
    }

    private void buildValues() {
        values.clear();
        for (User user : users) {
            values.add(user.toString());
        }
    }

    private void buildUsersFromJson(String json) {
        final Gson gson = new GsonBuilder().create();

        Type listType = new TypeToken<List<User>>() {
        }.getType();
        users = gson.fromJson(json, listType);
    }

    private void showList() {
        if (users.isEmpty()) {
            textError.setText(getString(R.string.empty_list));
            textError.setVisibility(View.VISIBLE);
            listOfUsersView.setVisibility(View.GONE);
        } else {
            textError.setVisibility(View.GONE);
            listOfUsersView.setVisibility(View.VISIBLE);
        }
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
