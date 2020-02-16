package com.alexandriabanta.orderandchaosv2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RandomTicTacToe extends AppCompatActivity implements View.OnClickListener{
    private Button[][] words = new Button[3][3];
    //private int[][] numbers = new int[3][3];

    // max of 9 rounds
    private int roundCount = 0;
    private int count = 0;

    private Random random = new Random();
    private int coinFlip;

    private int player1Points;
    private int player2Points;

    private TextView player1Text;
    private TextView player2Text;

    private TextView roundNumberText;
    private TextView turnText;

    private MediaPlayer mediaPlayer;

    //private boolean player1Turn = true;

    private boolean button1_1Clicked = false;
    private boolean button1_2Clicked = false;
    private boolean button1_3Clicked = false;
    private boolean button2_1Clicked = false;
    private boolean button2_2Clicked = false;
    private boolean button2_3Clicked = false;
    private boolean button3_1Clicked = false;
    private boolean button3_2Clicked = false;
    private boolean button3_3Clicked = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.randomtictactoe);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // music
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bensound_thelounge_random);
        mediaPlayer.start();

        player1Text = findViewById(R.id.player1_score_textview);
        player2Text = findViewById(R.id.player2_score_textview);
        roundNumberText = findViewById(R.id.roundCount_number);
        turnText = findViewById(R.id.turn_textview);

        flipCoin();
        whoseTurn();

        findViewById(R.id.replay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        words[i][j].setText("");
                        replay();
                    }
                }
            }
        });


        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                String buttonID = "button" + (i + 1) + "_" + (j + 1);
                Log.i("Button Id", buttonID);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                words[i][j] = findViewById(resID);
                words[i][j].setOnClickListener(this);
            }
        }
    }

    // stops the music when I leave game
    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            if(isFinishing())
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    @Override
    public void onClick(View view) {

        // prevents players from selecting same button
        if(!((Button) view).getText().toString().equals(""))
        {
            return;
        }
        //if (player1Turn)
        // player 1 playing
        if (coinFlip == 0)
        {
            ((Button) view).setText("X");
            //turnText.setText("Turn: Player 2");
            //Toast.makeText(getApplicationContext(), "Player 2 Turn", Toast.LENGTH_SHORT).show();
            //player1Turn = false;
            //flipCoin();
        }

        //else if(!player1Turn)
        //player 2 playing
        else if(coinFlip == 1)
        {
            ((Button) view).setText("O");
            //turnText.setText("Turn: Player 1");
            //Toast.makeText(getApplicationContext(), "Player 1 Turn", Toast.LENGTH_SHORT).show();
            //player1Turn = true;
            //flipCoin();
        }

        // player 2 wins
        //if(Loser() && !player1Turn)
        if(Winner() && coinFlip == 0)
        {
            roundCount++;
            player1Wins();
            roundNumberText.setText("" + roundCount);
        }

        // player 1 wins
        //else if(Loser() && player1Turn)
        else if(Winner() && coinFlip == 1)
        {
            roundCount++;
            player2Wins();
            roundNumberText.setText("" + roundCount);
        }
        else if (count >=  8)
        {
            draw();
        }
        //flipCoin();
        //coinFlip = random.nextInt(2);
        count++;
        coinFlip = random.nextInt(2);

        // if there is not a winner, say the next turn
        if(!Winner())
        {
            whoseTurn();
        }

        Log.i("COIN: ", " " + coinFlip);
    }

    private void flipCoin()
    {
        coinFlip = random.nextInt(2);
        if(coinFlip == 0)
        {
            turnText.setText("Turn: Player 1");
            //Toast.makeText(getApplicationContext(), "Coin flip! Player 1 Go!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            turnText.setText("Turn: Player 2");
            //Toast.makeText(getApplicationContext(), "Coin flip! Player 2 Go!", Toast.LENGTH_SHORT).show();
        }
    }

    private void whoseTurn()
    {
        if(coinFlip == 0)
        {
            turnText.setText("Turn: Player 1");
            Toast.makeText(getApplicationContext(), "Coin flip! Player 1 Go!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            turnText.setText("Turn: Player 2");
            Toast.makeText(getApplicationContext(), "Coin flip! Player 2 Go!", Toast.LENGTH_SHORT).show();
        }
    }

    private void replay()
    {
        for(int i = 0;  i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                words[i][j].setOnClickListener(this);
            }
        }
        flipCoin();
        whoseTurn();
        count = 0;
    }

    private void player1Wins()
    {
        player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();

        for(int i = 0;  i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                words[i][j].setOnClickListener(null);
            }
        }
        updatePointsText();
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();

        for(int i = 0;  i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                words[i][j].setOnClickListener(null);
            }
        }
        updatePointsText();
    }
    private void draw()
    {
        roundCount++;
        roundNumberText.setText("" + roundCount);
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        count = 0;
        //resetBoard();
    }

    private void updatePointsText()
    {
        player1Text.setText("" + player1Points);
        player2Text.setText("" + player2Points);
    }

    private boolean Winner(){
        String[][] field = new String[3][3];
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                field[i][j] = words[i][j].getText().toString();

            }
        }

        for(int i = 0; i < 3; i++)
        {
            // compare fields next to each other
            // checks each row
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])  &&!field[i][0].equals(""))
            {
                return true;
            }
        }

        for(int i = 0; i < 3; i++)
        {
            // checking the columns
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])  &&!field[0][i].equals(""))
            {
                return true;
            }
        }

        // checks diagonals
        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
        {
            return true;
        }

        if(field[2][0].equals(field[1][1]) && field[2][0].equals(field[0][2]) && !field[2][0].equals(""))
        {
            return true;
        }

        return false;
    }
}
