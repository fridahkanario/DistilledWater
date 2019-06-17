package com.example.distilled_water_distribution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public static final String url = "https://d1668099.ngrok.io/login";
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    //Inject the views using butterknife bindview

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Bind the activity
        ButterKnife.bind(this);

        _loginButton=findViewById(R.id.btn_login);
        _signupLink=findViewById(R.id.link_signup);
        _emailText=findViewById(R.id.input_email);
        _passwordText=findViewById(R.id.input_password);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Sign up activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    //login method

    public void login() {

        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating....");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email",email);
            jsonObject.put("password",password);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            try {
                                int status = response.getInt("Status");
                                Log.d("Status", Integer.toString(status));

                                if (status == 200) {
                                    Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
                                    onLoginSuccess();
                                }

                                if (status==404){

                                    Toast.makeText(getBaseContext(), "User does not exist", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {

                            }

                            Log.d("Response", String.valueOf(response));
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Log.d("Error.Response", "error");
                        }
                    }
            ) {
            };

             MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//            Volley.newRequestQueue(this).add(jsonObjectRequest);
            // TODO: Implement your own authentication logic here.
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        Intent dashboard = new Intent(this, Dashboard.class);
        startActivity(dashboard);

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
