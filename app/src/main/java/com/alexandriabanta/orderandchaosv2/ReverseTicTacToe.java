// Amanda and Alex
package com.alexandriabanta.orderandchaosv2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class ReverseTicTacToe extends AppCompatActivity implements  View.OnClickListener {

    public Button[][] words = new Button[3][3];
    //private int[][] numbers = new int[3][3];

    // max of 9 rounds
    private int roundCount = 0;
    private int count = 0;

    private int player1Points;
    private int player2Points;

    private TextView player1Text;
    private TextView player2Text;

    private TextView roundNumberText;
    private TextView turnText;

    private boolean player1Turn = true;

    /*
    private boolean button1_1Clicked = false;
    private boolean button1_2Clicked = false;
    private boolean button1_3Clicked = false;
    private boolean button2_1Clicked = false;
    private boolean button2_2Clicked = false;
    private boolean button2_3Clicked = false;
    private boolean button3_1Clicked = false;
    private boolean button3_2Clicked = false;
    private boolean button3_3Clicked = false;
    */

    private MediaPlayer mediaPlayer;

    @Override
    protected void onStop() {
        try {
            FileOutputStream FOS = openFileOutput("rvTicTacToe.txt", Context.MODE_PRIVATE);
            OutputStreamWriter OSW = new OutputStreamWriter(FOS);
            BufferedWriter BW = new BufferedWriter(OSW);
            PrintWriter PW = new PrintWriter(BW);

            Log.i("FILE", "Found rvTicTacToe.txt");
            //PW.println("Hello");

            //want to save:
            // state of private button words
            //convert the board to a 1d array and put it
            //int oneDBoard[] = new int[9];

            int saveVal = 0;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    //saveVal 0 means blank, 1 means O, 2 means X
                    if (words[row][col].getText() == "O") {
                        saveVal = 1;
                    } else if (words[row][col].getText() == "X") {
                        saveVal = 2;
                    } else {
                        saveVal = 0;
                    }

                    Log.i("FILE", "Writing saveVal: " + saveVal);
                    PW.println(saveVal);
                }
            }

            // int roundCount
            PW.println(roundCount);
            // int count
            PW.println(count);
            // int player1points, int player2points
            PW.println(player1Points);
            PW.println(player2Points);
            // boolean player1turn
            PW.println(player1Turn);
            PW.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reversetictactoe);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // music
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bensound_jazzcomedy_reverse);
        mediaPlayer.start();

        player1Text = findViewById(R.id.player1_score_textview);
        player2Text = findViewById(R.id.player2_score_textview);
        roundNumberText = findViewById(R.id.roundCount_number);
        turnText = findViewById(R.id.turn_textview);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + (i + 1) + "_" + (j + 1);
                Log.i("Button Id", buttonID);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                words[i][j] = findViewById(resID);
                words[i][j].setOnClickListener(this);
            }
        }

        File save = new File(getFilesDir(), "rvTicTacToe.txt");
        if (save.exists() && save.length() != 0) {
            tryRestoreGame();
        } else {
            findViewById(R.id.replay_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            words[i][j].setText("");
                            replay();
                        }
                    }
                }
            });
        }
    }

    // stops the music when I leave game
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            if (isFinishing()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }


    @Override
    public void onClick(View view) {
        // prevents players from selecting same button
        if (!((Button) view).getText().toString().equals("")) {
            return;
        }


        if (player1Turn) {
            ((Button) view).setText("X");
            ((Button) view).getBackground();
            Toast.makeText(getApplicationContext(), "Player 2 Turn", Toast.LENGTH_SHORT).show();
            turnText.setText("Turn: Player 2");
            player1Turn = false;
        } else if (!player1Turn) {
            ((Button) view).setText("O");
            Toast.makeText(getApplicationContext(), "Player 1 Turn", Toast.LENGTH_SHORT).show();
            turnText.setText("Turn: Player 1");
            player1Turn = true;
        }

        // player 2 wins
        if (Loser() && !player1Turn) {
            roundCount++;
            player1Loses();
            roundNumberText.setText("" + roundCount);
        }

        // player 1 wins
        else if (Loser() && player1Turn) {
            roundCount++;
            player2Loses();
            roundNumberText.setText("" + roundCount);
        } else if (count >= 8) {
            draw();

        }
        count++;
    }

    private void replay() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                words[i][j].setOnClickListener(this);
            }
        }
        count = 0;
    }

    private void player1Loses() {
        player2Points++;
        Toast.makeText(this, "3 in a row Player 1 :(    Player 2 wins!", Toast.LENGTH_SHORT).show();
        winAlertDialog(2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                words[i][j].setOnClickListener(null);
            }
        }
        updatePointsText();
    }

    private void player2Loses() {
        player1Points++;
        Toast.makeText(this, "3 in a row Player 2 :(   Player 1 wins!", Toast.LENGTH_SHORT).show();
        winAlertDialog(1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                words[i][j].setOnClickListener(null);
            }
        }
        updatePointsText();
    }

    private void draw() {
        roundCount++;
        roundNumberText.setText("" + roundCount);
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        count = 0;
        //resetBoard();
    }

    private void updatePointsText() {
        player1Text.setText("" + player1Points);
        player2Text.setText("" + player2Points);
    }

    private boolean Loser() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = words[i][j].getText().toString();

            }
        }

        for (int i = 0; i < 3; i++) {
            // compare fields next to each other
            // checks each row
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            // checking the columns
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        // checks diagonals
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[2][0].equals(field[1][1]) && field[2][0].equals(field[0][2]) && !field[2][0].equals("")) {
            return true;
        }

        return false;
    }

    public void winAlertDialog(int playerNum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations!");
        builder.setMessage("Player " + playerNum + " won! Click 'OK' to play another game.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int choice) {
                // Dismiss Dialog
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                getApplicationContext().startActivity(i);

                File file = new File(getFilesDir(), "rvTicTacToe.txt");
                file.delete();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1100, 600);
    }

    public void tryRestoreGame() {
        //then draw from the existing game
        try {
            FileInputStream IS = openFileInput("rvTicTacToe.txt");
            Log.i("FILE", "File input stream created");
            Scanner scanner = new Scanner(IS);

            int oneDBoard[] = new int[9];
            for (int i = 0; i < 9; i++) {
                oneDBoard[i] = Integer.parseInt(scanner.next());
            }

            int boardIndex = 0;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    //saveVal 0 means blank, 1 means O, 2 means X
                    if (oneDBoard[boardIndex] == 1) {
                        words[row][col].setText("O");
                    } else if (oneDBoard[boardIndex] == 2) {
                        words[row][col].setText("X");
                    } else {
                        words[row][col].setText("");
                    }
                    Log.i("FILE", "Reading words[" + row + "][" + col + "]: " + words[row][col]);
                    boardIndex++;
                }
            }

            // int roundCount
            roundCount = Integer.parseInt(scanner.next());
            // int count
            count = Integer.parseInt(scanner.next());
            // int player1points, int player2points
            player1Points = Integer.parseInt(scanner.next());
            player2Points = Integer.parseInt(scanner.next());
            // TV player1text, player2text

            player1Turn = Boolean.parseBoolean(scanner.next());


            player1Text.setText("" + player1Points);
            player2Text.setText("" + player2Points);
            // tv roundnumbertext
            roundNumberText.setText("" + roundCount);

            // tv turntext
            if (player1Turn) {
                turnText.setText("Player 1's Turn");
            } else {
                turnText.setText("Player 2's Turn");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("FILE","");
        }
    }
}