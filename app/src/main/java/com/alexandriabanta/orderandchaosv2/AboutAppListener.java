//Alexandria Banta
//Amanda McNair
//CSCI 4010

package com.alexandriabanta.orderandchaosv2;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// (3) Listener in separate .java file
public class AboutAppListener extends AppCompatActivity implements View.OnClickListener {

    private Intent intent; private Context context;

    public AboutAppListener(Intent intentParam, Context contextParam) {
        this.intent = intentParam; this.context = contextParam;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aboutTheAppButton) {
                context.startActivity(intent);
        }
    }
}
