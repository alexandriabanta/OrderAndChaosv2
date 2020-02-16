// Alexandria Banta
// Amanda McNair
// CSCI 4010

package com.alexandriabanta.orderandchaosv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playButton);
        Button moreInfoButton = findViewById(R.id.random_tic_tac_toe_imageButton);

        //(1) Anonymous inner class listener pattern
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First listener method (anonymous inner class)
                final String URL = "https://en.wikipedia.org/wiki/Order_and_Chaos";
                Uri uri = Uri.parse(URL);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });

        Button aboutTheAppButton = findViewById(R.id.aboutTheAppButton);

        Intent i = new Intent(getApplicationContext(),AboutAppActivity.class);
        AboutAppListener listener = new AboutAppListener(i, getApplicationContext());

        aboutTheAppButton.setOnClickListener(listener);
        playButton.setOnClickListener(this);

        findViewById(R.id.reverse_tic_tac_toe_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playReverseTicTacToe();
            }
        });

        findViewById(R.id.random_tic_tac_toe_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playRandomTicTacToe();
            }
        });
    }

    // (2) Listener is implemented directly by the class that needs it
    // (3rd pattern in separate file)
    public void onClick(View v) {
        if (v.getId() == R.id.playButton) {
            Intent i = new Intent(getApplicationContext(),GameplayActivity.class);
            startActivity(i);
        }
    }

    private void playReverseTicTacToe() {
        Intent intent = new Intent(getApplicationContext(), ReverseTicTacToe.class);
        startActivity(intent);
    }


    private void playRandomTicTacToe() {
        Intent intent = new Intent(getApplicationContext(), RandomTicTacToe.class);
        startActivity(intent);
    }


}

