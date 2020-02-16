// Alexandria Banta
// Amanda McNair
// CSCI 4010

package com.alexandriabanta.orderandchaosv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //wild
        Button wildButton = findViewById(R.id.playButton);
        //reverse
        Button reverseButton = findViewById(R.id.reverse_tic_tac_toe_imageButton);
        //random
        Button randomButton = findViewById(R.id.randomButton);
        //about the app
        Button instructionsButton = findViewById(R.id.aboutTheAppButton);

        //(1) Anonymous inner class listener pattern
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playReverseTicTacToe();
            }
        });

        Intent i = new Intent(getApplicationContext(),AboutAppActivity.class);
        InstructionsListener listener = new InstructionsListener(i, getApplicationContext());

        instructionsButton.setOnClickListener(listener);
        wildButton.setOnClickListener(this);
        //reverseButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        findViewById(R.id.reverse_tic_tac_toe_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playReverseTicTacToe();
            }
        });
    }

    // (2) Listener is implemented directly by the class that needs it
    // (3rd pattern in separate file)
    public void onClick(View v) {
        if (v.getId() == R.id.playButton) {
            Intent i = new Intent(getApplicationContext(),GameplayActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.randomButton) {
            playRandom();
        }
    }

    private void playReverseTicTacToe() {
        Intent intent = new Intent(getApplicationContext(), ReverseTicTacToe.class);
        startActivity(intent);
    }

    private void playRandom() {
        Intent intent = new Intent(getApplicationContext(), RandomTicTacToe.class);
        startActivity(intent);
    }


}

