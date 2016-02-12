package fr.iutinfo.androidclient;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import fr.iutinfo.androidclient.bean.User;


public class EditActivity extends ActionBarActivity {

    private final String URL = "http://192.168.1.18:8080/v1/user";

    private TextView mNameTitle;
    private EditText mNameEdit;
    private EditText mAliasEdit;
    private ProgressBar mProgressBar;
    private Button mSubmit;
    private Button mDelete;
    private Button mCancel;
    private TextView mTextView;

    private ColorStateList defaultColor;
    private int id;
    private String name;
    private String alias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        alias = getIntent().getStringExtra("alias");

        setTitle(name);

        mNameTitle = (TextView) findViewById(R.id.name_title);
        mNameEdit = (EditText) findViewById(R.id.edit_name);
        mAliasEdit = (EditText) findViewById(R.id.edit_alias);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mSubmit = (Button) findViewById(R.id.submit);
        mDelete = (Button) findViewById(R.id.delete);
        mCancel = (Button) findViewById(R.id.cancel);
        mTextView = (TextView) findViewById(R.id.textView);

        defaultColor = mNameTitle.getTextColors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
        mSubmit.setEnabled(true);
        mDelete.setEnabled(true);
        mCancel.setEnabled(true);
        mNameTitle.setTextColor(defaultColor);
        mNameEdit.setText(name);
        mAliasEdit.setText(alias);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       return true;
    }

    public void submit(View v) {

        User user = new User();
        user.setName(mNameEdit.getText().toString());
        user.setAlias(mAliasEdit.getText().toString());

        mNameTitle.setTextColor(defaultColor);

        if (user.getName().isEmpty()) {

            mNameTitle.setTextColor(Color.RED);
            return;
        }

        final Gson gson = new GsonBuilder().create();
        final String json = gson.toJson(user);

        mProgressBar.setVisibility(View.VISIBLE);
        mSubmit.setEnabled(false);
        mDelete.setEnabled(false);
        mCancel.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL + "/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        goToList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressBar.setVisibility(View.GONE);
                mTextView.setText(getString(R.string.error, error.getMessage()));
                mTextView.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                return json.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    public void delete(View v) {

        mProgressBar.setVisibility(View.VISIBLE);
        mSubmit.setEnabled(false);
        mDelete.setEnabled(false);
        mCancel.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL + "/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        goToList();
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

    public void cancel(View v) {

        goToList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void goToList() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
