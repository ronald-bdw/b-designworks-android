package com.b_designworks.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class InitialScreen extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserManager userManager = DI.getInstance().getUserManager();
        if (userManager.userHasToken()) {
            Navigator.chat(this);
        } else {
            userManager.clearAll();
            Navigator.welcome(this);
        }
        finish();
    }
}
