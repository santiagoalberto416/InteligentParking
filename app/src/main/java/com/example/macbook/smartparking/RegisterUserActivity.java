package com.example.macbook.smartparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.macbook.smartparking.data.login.RegisterPostData;
import com.example.macbook.smartparking.data.login.RegisterRequestInterface;
import com.example.macbook.smartparking.data.login.RegisterResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText mUserNameEdit;
    private EditText mPasswordEdit;
    private EditText mEmailEdit;
    private RelativeLayout mProgress;
    private Button mButton;
    private static final String urlRequest = "http://sparkingsystem.info/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mUserNameEdit = (EditText)findViewById(R.id.edit_username);
        mPasswordEdit = (EditText)findViewById(R.id.edit_password);
        mEmailEdit = (EditText)findViewById(R.id.edit_email);
        mButton = (Button)findViewById(R.id.request_button);
        mProgress = (RelativeLayout)findViewById(R.id.progresView);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        setTextWatcher();
    }

    @Override
    protected void  onResume(){
        super.onResume();

    }

    private void submit(){
        boolean valid = true;
        if(isEmpty(mUserNameEdit)){
            valid = false;
            setError(mUserNameEdit, "Username no puede estar vacio");
        }
        if(isEmpty(mPasswordEdit)){
            valid = false;
            setError(mPasswordEdit, "Password no puede estar vacio");
        }
        if(isEmpty(mEmailEdit)){
            valid = false;
            setError(mEmailEdit, "Email no puede estar vacio");
        }
        if(valid){
            // make request
            registerUser(mEmailEdit.getText().toString(), mUserNameEdit.getText().toString(), mPasswordEdit.getText().toString());
        }
    }

    public void setTextWatcher(){
        mUserNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    clearError(mUserNameEdit);
                }
            }
        });
    }

    public static boolean isEmpty(EditText editText) {
        String input = editText.getText().toString().trim();
        return input.length() == 0;
    }

    public static void setError(EditText editText, String errorString) {
        editText.setError(errorString);
    }

    public static void clearError(EditText editText) {
        editText.setError(null);
    }


    public void registerUser(final String email, String username, String password){
        Gson gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Retrofit retrofitBuilder = new Retrofit.Builder()
                .baseUrl(urlRequest)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();
        RegisterRequestInterface restClient = retrofitBuilder.create(RegisterRequestInterface.class);
        showLoading();
        RegisterPostData graph = new RegisterPostData(username, password,email);
        Call<RegisterResponse> call = restClient.postJson(graph);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.code()==200){
                    RegisterResponse data = response.body();
                    switch (data.getStatus()){
                        case 0:
                            //correct
                            RegisterUserActivity.this.finish();
                            break;
                        case 200:
                            //email repeated
                            setError(mEmailEdit, "Este correo ya esta registrado");
                            break;
                    }


                }
                hideLoading();
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("data", "error");
                hideLoading();
            }
        });
    }

    public void showLoading(){
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        mProgress.setVisibility(View.INVISIBLE);
    }
}
