// Alexandria Banta
// Amanda McNair
// CSCI 4010

package com.alexandriabanta.orderandchaosv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.RadioGroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameplayActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    // spaces can be only X, O, or BLANK
    enum space {X, O, BLANK}

    // piece types are X and O
    enum pieceTypes {Xes, Oes}

    private int roundCount = 0; //keeps track of turns in case of draw
    private pieceTypes pieceType = pieceTypes.Xes;

    public static int ROWS = 3, COLS = 3;
    private space[][] board = new space[ROWS][COLS];

    public int playerNum = 1;
    Boolean someoneWon = false;

    // 3x3 array of IDs corresponding to board array
    public static int[][] IDArr = {
            {R.id.row0col0, R.id.row0col1, R.id.row0col2},
            {R.id.row1col0, R.id.row1col1, R.id.row1col2},
            {R.id.row2col0, R.id.row2col1, R.id.row2col2}
    };

    // the following for converting the board to a 1d array for
    // onSavedInstance purposes
    public int[] oneDBoard = {0,0,0,0,0,0,0,0,0};
    int pieceTypeNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);


        File save = new File(getFilesDir(), "wildTicTacToe.txt");

        if (save.exists() && (save.length() != 0)) {
            RadioGroup rg = findViewById(R.id.radioGroup);
            rg.setOnCheckedChangeListener(this);

            ImageButton r0c0 = findViewById(R.id.row0col0);
            ImageButton r0c1 = findViewById(R.id.row0col1);
            ImageButton r0c2 = findViewById(R.id.row0col2);

            ImageButton r1c0 = findViewById(R.id.row1col0);
            ImageButton r1c1 = findViewById(R.id.row1col1);
            ImageButton r1c2 = findViewById(R.id.row1col2);

            ImageButton r2c0 = findViewById(R.id.row2col0);
            ImageButton r2c1 = findViewById(R.id.row2col1);
            ImageButton r2c2 = findViewById(R.id.row2col2);

            r0c0.setOnClickListener(this);
            r0c1.setOnClickListener(this);
            r0c2.setOnClickListener(this);

            r1c0.setOnClickListener(this);
            r1c1.setOnClickListener(this);
            r1c2.setOnClickListener(this);

            r2c0.setOnClickListener(this);
            r2c1.setOnClickListener(this);
            r2c2.setOnClickListener(this);

            try {
                FileInputStream fis = openFileInput("wildTicTacToe.txt");
                Scanner scanner = new Scanner(fis);

                // 1) oneDBoard
                // 2) playerNum
                // 3) pieceNum

                for (int i = 0; i < 9; i++) {
                    //save the 9 values into 1d board array
                    oneDBoard[i] = Integer.parseInt(scanner.next());
                    Log.i("1DBOARD",""+ oneDBoard[i]);
                }

                playerNum = Integer.parseInt(scanner.next());
                pieceTypeNum = Integer.parseInt(scanner.next());

                if (playerNum == 2) {
                    TextView playerText = findViewById(R.id.turn_text_view);
                    playerText.setText("Player " + playerNum + "'s turn");
                }

                //use the pieceTypeNum variable to restore the piece type
                if (pieceTypeNum == 1) {
                    pieceType = pieceTypes.Xes;
                } else {
                    RadioButton b = (RadioButton) findViewById(R.id.o_radio_button);
                    b.setChecked(true); pieceType = pieceTypes.Oes;
                }

                // now that we have the pieces placed in the spaces, and the 1D array
                // repopulate the board and ID array
                int boardIndex = 0;
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {

                        //0 means blank, 1 means O, 2 means X
                        if (oneDBoard[boardIndex] == 0) {
                            //saveVal = space.BLANK;
                            board[row][col] = space.BLANK;
                            //restore the blank imageViews
                            ((ImageButton) findViewById(IDArr[row][col])).setImageResource(R.drawable.blank_board_piece);

                        } else if (oneDBoard[boardIndex] == 1) {
                            //saveVal = space.O;
                            board[row][col] = space.O;
                            //restore the O imageViews
                            ((ImageButton) findViewById(IDArr[row][col])).setImageResource(R.drawable.o_board_piece);

                        } else if (oneDBoard[boardIndex] == 2) {
                            //saveVal = space.X;
                            board[row][col] = space.X;
                            //restore the X blank imageViews
                            ((ImageButton) findViewById(IDArr[row][col])).setImageResource(R.drawable.x_board_piece);

                        }
                        boardIndex++;
                    }

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else {
            // initialize board array as fully blank
            // set up each space as blank
            //playerNum = 1; pieceType = pieceTypes.Xes;

            if (savedInstanceState == null) {
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        board[i][j] = space.BLANK;
                        ((ImageButton) findViewById(IDArr[i][j])).setImageResource(R.drawable.blank_board_piece);
                    }
                }

                RadioGroup rg = findViewById(R.id.radioGroup);
                rg.setOnCheckedChangeListener(this);

                ImageButton r0c0 = findViewById(R.id.row0col0);
                ImageButton r0c1 = findViewById(R.id.row0col1);
                ImageButton r0c2 = findViewById(R.id.row0col2);

                ImageButton r1c0 = findViewById(R.id.row1col0);
                ImageButton r1c1 = findViewById(R.id.row1col1);
                ImageButton r1c2 = findViewById(R.id.row1col2);

                ImageButton r2c0 = findViewById(R.id.row2col0);
                ImageButton r2c1 = findViewById(R.id.row2col1);
                ImageButton r2c2 = findViewById(R.id.row2col2);

                r0c0.setOnClickListener(this);
                r0c1.setOnClickListener(this);
                r0c2.setOnClickListener(this);

                r1c0.setOnClickListener(this);
                r1c1.setOnClickListener(this);
                r1c2.setOnClickListener(this);

                r2c0.setOnClickListener(this);
                r2c1.setOnClickListener(this);
                r2c2.setOnClickListener(this);

                //File file = new File(getFilesDir(),"wildTicTacToe.txt");
                //file.delete();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Player 1 plays first.",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int clickedId) {
        Log.i("clickedId", "" + clickedId);

        if (clickedId == R.id.x_radio_button) {
            //then we are playing as X
            pieceType = pieceTypes.Xes;
        } else if (clickedId == R.id.o_radio_button) {
            //then we are playing as O
            pieceType = pieceTypes.Oes;
        } else {
            throw new RuntimeException("Illegal button selection");
        }
    }

    public void onClick(View v) {
        //Log.i("PIECETYPES",""+pieceType);
            playerTurn(v);
    }

    public void playerTurn(View spaceChosen) {
        //assuming no one has won yet
        Log.i("ROUND COUNT: ","" + roundCount);

        if (!checkForWinner()) {

            // get the corresponding index in the board arr
            int rowOfSpaceChosen = 0;
            int colOfSpaceChosen = 0;

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (IDArr[row][col] == spaceChosen.getId()) {
                        rowOfSpaceChosen = row;
                        colOfSpaceChosen = col;
                        Log.i("v,", "IDArr" + "[" + rowOfSpaceChosen + "][" + colOfSpaceChosen + "]");
                    }
                }
            }

            //if the space they chose is blank, then they can place a piece.
            if (board[rowOfSpaceChosen][colOfSpaceChosen] == space.BLANK) {

                if (pieceType == pieceTypes.Xes) {
                    // set that index to X
                    board[rowOfSpaceChosen][colOfSpaceChosen] = space.X;
                    //update the interface
                    Log.i("imageButton,", "" + findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen]));
                    ((ImageButton) findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen])).setImageResource(R.drawable.x_board_piece);
                } else if (pieceType == pieceTypes.Oes) {
                    // set that index to O
                    board[rowOfSpaceChosen][colOfSpaceChosen] = space.O;
                    //update the interface
                    Log.i("imageButton,", "" + findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen]));
                    ((ImageButton) findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen])).setImageResource(R.drawable.o_board_piece);
                }

                if (!checkForWinner()) {
                    roundCount++;
                    switchPlayers();
                }


            } else if (board[rowOfSpaceChosen][colOfSpaceChosen] != space.BLANK) {
                //make a toast message that says "error, space taken. Try again with a different space.
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Choose an empty space!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

        }

        if (roundCount >= 9) {
            showDrawAlertDialog();
        }
    }


    public Boolean checkForWinner() {
        //Assume there is no winner in current turn
        //Boolean playerWon = false;

        // left to right
        // up to down
        // diagonal from top left to bottom right
        // diagonal from bottom left to top right

        // check for a COLUMN streak
        for (int col = 0; col < COLS; col++) {
            //check for streak of Os
            if ((      board[col][0] == space.O
                    && board[col][1] == space.O
                    && board[col][2] == space.O)) {

                //then there is a 3-spot streak, and current player won.
                someoneWon = true;
                Log.i("v", "through COL streak in col " + col);
            }
            //check for streak of Xs
            else if ((board[col][0] == space.X
                    && board[col][1] == space.X
                    && board[col][2] == space.X)) {
                someoneWon = true;
                Log.i("v", "through COL streak in col " + col);
            }
        }

        //check for a ROW streak.
        //if exists, then there is a 3-spot streak, and current player won.
        for (int row = 0; row < ROWS; row++) {
            if ((board[0][row] == space.O
                    && board[1][row] == space.O
                    && board[2][row] == space.O)) {
                someoneWon = true;
                Log.i("v", "through COL streak in col " + row);

            } else if ((board[0][row] == space.X
                    && board[1][row] == space.X
                    && board[2][row] == space.X)) {
                someoneWon = true;
                Log.i("v", "through COL streak in col " + row);
            }
        }

        // * o o        o o *
        // o * o        o * o
        // o o *        * o o
        // [row][row]   [row][(ROWS-1)-row]

        //for the first diagonal-- row and column will be same number

        Boolean downwardDiagonalOStreak = true, upwardDiagonalOStreak = true;
        Boolean downwardDiagonalXStreak = true, upwardDiagonalXStreak = true;


        for (int row = 0; row < ROWS; row++) {
            if (board[row][row] != space.O) {
                downwardDiagonalOStreak = false;
            } if (board[row][row] != space.X) {
                downwardDiagonalXStreak = false;
            }
        }

        //second diagonal: col is ROWS - row
        for (int row = 0; row < ROWS; row++) {
            if (board[row][(ROWS - 1) - row] != space.O) {
                upwardDiagonalOStreak = false;
            } if (board[row][(ROWS - 1) - row] != space.X) {
                upwardDiagonalXStreak = false;
            }
        }

        if (downwardDiagonalOStreak || upwardDiagonalOStreak ||
                downwardDiagonalXStreak || upwardDiagonalXStreak) someoneWon = true;

        //if someone won, reset defaults after displaying alert dialog.
        if (someoneWon) {
            showWinnerAlertDialog();

            RadioButton b = (RadioButton) findViewById(R.id.x_radio_button);
            b.setChecked(true); pieceType = pieceTypes.Xes; playerNum = 1;
        }

        return (someoneWon);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        oneDBoard = savedInstanceState.getIntArray("oneDBoard");

        //now to convert the 1d array back into 2d enum
        //spaceVal is a temporary var used to restore each entry in the 2d enum board
        space saveVal = space.BLANK;
        int boardIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                //saveVal 0 means blank, 1 means O, 2 means X
                if (oneDBoard[boardIndex] == 0) {
                    saveVal = space.BLANK;

                    //restore the blank imageViews
                    ((ImageButton) findViewById(IDArr[row][col])).setImageResource(R.drawable.blank_board_piece);

                } else if (oneDBoard[boardIndex] == 1) {
                    saveVal = space.O;

                    //restore the O imageViews
                    ((ImageButton) findViewById(IDArr[row][col])).setImageResource(R.drawable.o_board_piece);

                } else if (oneDBoard[boardIndex] == 2) {
                    saveVal = space.X;

                    //restore the X blank imageViews
                    ((ImageButton) findViewById(IDArr[row][col])).setImageResource(R.drawable.x_board_piece);

                }

                // use the save val to restore the 2d board
                board[row][col] = saveVal;
                boardIndex++;
            }
        }

        //remember whose turn it is and what piece they chose
        playerNum = savedInstanceState.getInt("playerNum");
        pieceTypeNum = savedInstanceState.getInt("pieceTypeNum");

        //using the pieceTypeNum, restore the current pieceType
        if (pieceTypeNum == 1) {
            pieceType = pieceTypes.Xes;
        } else {pieceType = pieceTypes.Oes; }

    }

    //when i go back, I want to to do the same thing savedInstanceState does
    public void saveBundle(Bundle outState) {
        //convert the board to a 1d array and put it

        int boardIndex = 0, saveVal = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                //saveVal 0 means blank, 1 means O, 2 means X
                if (board[row][col] == space.BLANK) {
                    saveVal = 0;
                } else if (board[row][col] == space.O) {
                    saveVal = 1;
                } else if (board[row][col] == space.X) {
                    saveVal = 2;
                }

                oneDBoard[boardIndex] = saveVal;
                boardIndex++;
            }
        }

        //now the 2d enum array has been saved as a 1d int arr
        outState.putIntArray("oneDBoard",oneDBoard);

        //remember whose turn it is and what piece they chose
        outState.putInt("playerNum",playerNum);

        if (pieceType == pieceTypes.Xes) {
            pieceTypeNum = 1;
        } else {pieceTypeNum = 2;}

        outState.putInt("pieceTypeNum",pieceTypeNum);
    }

    public void showWinnerAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations!");
        builder.setMessage("Player "+ playerNum + " won! Click 'OK' to play another game.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int choice) {
                // Dismiss Dialog
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                getApplicationContext().startActivity(in);

                File file = new File(getFilesDir(),"wildTicTacToe.txt");
                file.delete();

                //clear board
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        board[i][j] = space.BLANK;
                        ((ImageButton) findViewById(IDArr[i][j])).setImageResource(R.drawable.blank_board_piece);
                    }
                }

                roundCount = 0; //reset the round count
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1100, 600);
    }

    public void showDrawAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oh no!");
        builder.setMessage("Looks like this one was a draw! Click 'OK' to play another game.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int choice) {
                // Dismiss Dialog
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                getApplicationContext().startActivity(in);

                File file = new File(getFilesDir(),"wildTicTacToe.txt");
                file.delete();

                //clear board
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        board[i][j] = space.BLANK;
                        ((ImageButton) findViewById(IDArr[i][j])).setImageResource(R.drawable.blank_board_piece);
                    }
                }

                roundCount = 0; //reset the round count
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1100, 600);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveBundle(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        saveBundle(bundle);
        onSaveInstanceState(bundle);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        try {
            FileOutputStream FOS = openFileOutput("wildTicTacToe.txt", Context.MODE_PRIVATE);
            OutputStreamWriter OSW = new OutputStreamWriter(FOS);
            BufferedWriter BW = new BufferedWriter(OSW);
            PrintWriter PW = new PrintWriter(BW);

            // 1) oneDBoard
            // 2) playerNum
            // 3) pieceNUm

            int boardIndex = 0, saveVal = 0;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    //saveVal 0 means blank, 1 means O, 2 means X
                    if (board[row][col] == space.BLANK) {
                        saveVal = 0;
                    } else if (board[row][col] == space.O) {
                        saveVal = 1;
                    } else if (board[row][col] == space.X) {
                        saveVal = 2;
                    }

                    oneDBoard[boardIndex] = saveVal;
                    boardIndex++;
                    PW.println(saveVal);
                }
            }

            if (pieceType == pieceTypes.Xes) {
                pieceTypeNum = 1;
            } else if (pieceType == pieceTypes.Oes) {
                pieceTypeNum = 2;
            }

            PW.println(playerNum);
            PW.println(pieceTypeNum);
            PW.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void switchPlayers() {
        if (playerNum == 1) {
            playerNum = 2;
        } else if (playerNum == 2) {
            playerNum = 1;
        }

        TextView playerText = findViewById(R.id.turn_text_view);
        playerText.setText("Player " + playerNum + "'s turn");
    }


}