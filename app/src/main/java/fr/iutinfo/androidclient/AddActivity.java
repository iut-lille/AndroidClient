package fr.iutinfo.androidclient;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.iutinfo.androidclient.bean.User;

import static fr.iutinfo.androidclient.Configuration.SERVER;


public class AddActivity extends ActionBarActivity {


    private final String URL = SERVER + "/v1/user";

    private TextView nameTitle;
    private EditText nameEdit;
    private EditText aliasEdit;
    private ProgressBar progressBar;
    private Button mSubmit;
    private Button mCancel;
    private TextView textError;

    private ColorStateList defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle(R.string.activity_add_title);

        nameTitle = (TextView) findViewById(R.id.name_title);
        nameEdit = (EditText) findViewById(R.id.edit_name);
        aliasEdit = (EditText) findViewById(R.id.edit_alias);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        mSubmit = (Button) findViewById(R.id.submit);
        mCancel = (Button) findViewById(R.id.cancel);
        textError = (TextView) findViewById(R.id.textView);

        defaultColor = nameTitle.getTextColors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        mSubmit.setEnabled(true);
        mCancel.setEnabled(true);
        nameTitle.setTextColor(defaultColor);
        nameEdit.setText("");
        aliasEdit.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void submit(View v) {

        User user = new User();
        user.setName(nameEdit.getText().toString());
        user.setAlias(aliasEdit.getText().toString());

        nameTitle.setTextColor(defaultColor);

        if (user.getName().isEmpty()) {

            nameTitle.setTextColor(Color.RED);
            return;
        }

        final Gson gson = new GsonBuilder().create();
        final String userAsJson = gson.toJson(user);

        progressBar.setVisibility(View.VISIBLE);
        mSubmit.setEnabled(false);
        mCancel.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        goToList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                textError.setText(getString(R.string.error, error.getMessage()));
                textError.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                return userAsJson.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

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
