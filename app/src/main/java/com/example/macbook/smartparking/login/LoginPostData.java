package com.example.macbook.smartparking.login;

/**
 * Created by santiagoalbertokirk on 12/10/17.
 */

public class LoginPostData {
    final String password;
    final String email;

    public LoginPostData(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
