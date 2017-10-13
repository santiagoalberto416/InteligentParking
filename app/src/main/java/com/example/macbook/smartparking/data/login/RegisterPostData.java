package com.example.macbook.smartparking.data.login;

/**
 * Created by santiagoalbertokirk on 12/10/17.
 */

public class RegisterPostData {

    final String name;
    final String password;
    final String email;

    public RegisterPostData(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
