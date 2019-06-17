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


public class SignupActivity extends AppCompatActivity {

    public static final String url = "https://d1668099.ngrok.io/register";
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOGIN = 1;

    @BindView(R.id.input_first_name) EditText _first_nameText;
    @BindView(R.id.input_last_name) EditText _last_nameText;
    @BindView(R.id.input_user_name) EditText _user_name;
    @BindView(R.id.input_phone) EditText _phone_text;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Bind the activity
        ButterKnife.bind(this);


        _first_nameText=findViewById(R.id.input_first_name);
        _last_nameText=findViewById(R.id.input_last_name);
        _user_name=findViewById(R.id.input_user_name);
        _phone_text=findViewById(R.id.input_phone);
        _emailText=findViewById(R.id.input_email);
        _passwordText=findViewById(R.id.input_password);

        _loginLink=findViewById(R.id.link_login);
        _signupButton=findViewById(R.id.btn_signup);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }



        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String fname = _first_nameText.getText().toString();
        final String lname = _last_nameText.getText().toString();
        final String username = _user_name.getText().toString();
        final String phone = _phone_text.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

    try{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName",fname);
        jsonObject.put("lastName",lname);
        jsonObject.put("userName",username);
        jsonObject.put("phone",phone);
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
                                Toast.makeText(getBaseContext(), "Registration success", Toast.LENGTH_LONG).show();
                                onSignupSuccess();

                            }else {
                               onSignupFailed();
                            }
                        } catch (JSONException e) {

                        }
                        Log.d("Response", String.valueOf(response));
                    }
                },
                        new Response.ErrorListener(){

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


    }catch (JSONException e){
        e.printStackTrace();

    }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onSignupSuccess() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up failed", Toast.LENGTH_LONG).show();
    }


    public boolean validate() {
        boolean valid = true;

        String fname = _first_nameText.getText().toString();
        String lname = _last_nameText.getText().toString();
        String username = _user_name.getText().toString();
        String phone = _phone_text.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (fname.isEmpty() || fname.length() < 3) {
            _first_nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _first_nameText.setError(null);
        }
        if (lname.isEmpty() || lname.length() < 3) {
            _last_nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _last_nameText.setError(null);
        }
        if (username.isEmpty() || username.length() < 3) {
            _user_name.setError("at least 3 characters");
            valid = false;
        } else {
            _user_name.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            _phone_text.setError("at least 10 numbers");
            valid = false;
        } else {
            _phone_text.setError(null);
        }

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
