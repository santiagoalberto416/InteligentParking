package com.example.macbook.smartparking.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.macbook.smartparking.R;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText mUserNameEdit;
    private EditText mPasswordEdit;
    private EditText mEmailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mUserNameEdit = (EditText)findViewById(R.id.edit_username);
        mPasswordEdit = (EditText)findViewById(R.id.edit_password);
        mEmailEdit = (EditText)findViewById(R.id.edit_email);
    }

    @Override
    protected void  onResume(){
        super.onResume();

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
}
