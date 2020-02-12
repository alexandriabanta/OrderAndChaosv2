// Alexandria Banta
// Amanda McNair
// CSCI 4010

package com.alexandriabanta.orderandchaosv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.RadioGroup;

public class GameplayActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    // spaces can be only X, O, or BLANK
    enum space {X, O, BLANK}

    // piece types are X and O
    enum pieceTypes {Xes, Oes}

    public pieceTypes pieceType = pieceTypes.Xes;
    // board has 3 x 3 entries
    public static int ROWS = 3, COLS = 3;
    public space[][] board = new space[ROWS][COLS];

    public int playerNum = 1;
    public int piecesPlaced = 0;

    Boolean someoneWon = false;

    // 3x3 array of IDs corresponding to board array
    public static int[][] IDArr = {
            {R.id.row0col0, R.id.row0col1, R.id.row0col2},
            {R.id.row1col0, R.id.row1col1, R.id.row1col2},
            {R.id.row2col0, R.id.row2col1, R.id.row2col2}
    };

    // the following for converting the board to a 1d array for
    // onSavedInstance purposes
    public int[] oneDBoard = new int[9];
    int pieceTypeNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

            // initialize board array as fully blank
            // set up each space as blank

        if (savedInstanceState == null) {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    board[i][j] = space.BLANK;
                    ((ImageButton) findViewById(IDArr[i][j])).setImageResource(R.drawable.blank_board_piece);
                }
            }

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Player 1 plays first.",
                    Toast.LENGTH_SHORT);
            toast.show();
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
        if (!someoneWon) {
            TextView playerText = findViewById(R.id.turn_text_view);
            playerText.setText("Player " + playerNum + "'s turn");
            if (playerNum == 1) {
                playerTurn(v);
                // update label text for next turn
                if (!checkForWinner()) {
                    playerText.setText("Player " + playerNum + "'s turn");
                    //playerNum = 2;
                }
                playerNum = 2;
            } else if (playerNum == 2) {
                playerTurn(v);
                // update label text for next turn
                if (!checkForWinner()) {
                    playerText.setText("Player " + playerNum + "'s turn");
                    //playerNum = 1;
                }

                playerNum = 1;
            } else {
                throw new RuntimeException("Error: only players 1 and 2 are allowed.");
            }
            if (!checkForWinner()) playerText.setText("Player " + playerNum + "'s turn");
        } else showWinnerToast();
    }

    public void playerTurn(View spaceChosen) {
        //assuming no one has won yet
        if (!someoneWon) {
            //ImageButton imageButton = (ImageButton) spaceChosen;

            //if (!checkForWinner()) {
            // get the corresponding index in the board arr
            int rowOfSpaceChosen = 0;
            int colOfSpaceChosen = 0;

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (IDArr[row][col] == spaceChosen.getId()) {
                        rowOfSpaceChosen = row;
                        colOfSpaceChosen = col;
                        Log.i("v,", "IDArr" + "[" + rowOfSpaceChosen + "][" + colOfSpaceChosen);
                    }
                }
            }

            //Log.i("v,", "IDArr" + "[" + rowOfSpaceChosen + "][" + colOfSpaceChosen);
            //if that space on the board is currently blank
            if (board[rowOfSpaceChosen][colOfSpaceChosen] == space.BLANK) {

                if (pieceType == pieceTypes.Xes) {
                    // set that index to X
                    board[rowOfSpaceChosen][colOfSpaceChosen] = space.X;
                    //update the interface
                    Log.i("imageButton,", "" + findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen]));
                    ((ImageButton) findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen])).setImageResource(R.drawable.x_board_piece);
                    piecesPlaced++;
                } else if (pieceType == pieceTypes.Oes) {
                    // set that index to O
                    board[rowOfSpaceChosen][colOfSpaceChosen] = space.O;
                    //update the interface
                    Log.i("imageButton,", "" + findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen]));
                    ((ImageButton) findViewById(IDArr[rowOfSpaceChosen][colOfSpaceChosen])).setImageResource(R.drawable.o_board_piece);
                    piecesPlaced++;
                }

            } else if (board[rowOfSpaceChosen][colOfSpaceChosen] != space.BLANK) {
                //make a toast message that says "error, space taken. Try again with a different space.
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Error: Please choose an empty space.",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

            checkForWinner();
        }
        else showWinnerToast();
    }


    public Boolean checkForWinner() {
        //Assume there is no winner in current turn
        Boolean playerWon = false;

        // left to right
        // up to down
        // diagonal from top left to bottom right
        // diagonal from bottom left to top right

        // check for a COLUMN streak
        for (int col = 0; col < COLS; col++) {
            //check for streak of Os
            if ((board[col][0] == space.O
                    && board[col][1] == space.O
                    && board[col][2] == space.O)) {
                //then there is a 3-spot streak, and current player won.
                playerWon = true;
                Log.i("v", "through COL streak in col " + col);
            }
            //check for streak of Xs
            else if ((board[col][0] == space.X
                    && board[col][1] == space.X
                    && board[col][2] == space.X)) {
                playerWon = true;
                Log.i("v", "through COL streak in col " + col);
            }
        }

        //check for a ROW streak.
        //if exists, then there is a 3-spot streak, and current player won.
        for (int row = 0; row < ROWS; row++) {
            if ((board[0][row] == space.O
                    && board[1][row] == space.O
                    && board[2][row] == space.O)) {
                playerWon = true;
                Log.i("v", "through COL streak in col " + row);

            } else if ((board[0][row] == space.X
                    && board[1][row] == space.X
                    && board[2][row] == space.X)) {
                playerWon = true;
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
                downwardDiagonalXStreak || upwardDiagonalXStreak) playerWon = true;


        if (playerWon) {
            someoneWon = true;
            showWinnerToast();
        }

        return (playerWon);
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

    //when i go back, I want to to do the same thing savedINstanceState does
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

    public void showWinnerToast() {
        int otherPlayerNum = 0;
        if (playerNum == 1) otherPlayerNum = 2;
        else otherPlayerNum = 1;
        Toast toast = Toast.makeText(getApplicationContext(),
                "Congratulations, player " + otherPlayerNum + " won!\n Press 'Back' to play again.",
                Toast.LENGTH_SHORT);
        toast.show();
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
}
