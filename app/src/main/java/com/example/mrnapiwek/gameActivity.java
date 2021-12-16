package com.example.mrnapiwek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class gameActivity extends AppCompatActivity {
    int activeplayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    boolean gameactive = true;

    Button playAgainButton;
    TextView winnerTextView;
    GridLayout gridLayout;
    String winner;
    MediaPlayer mediaPlayer;
    TextView gameSum;
    TextView gameSumPerPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        playAgainButton = findViewById(R.id.playAgainButton);
        winnerTextView = findViewById(R.id.winnerTextView);
        gridLayout = findViewById(R.id.gridLayout);
        gameSum = findViewById(R.id.TVSumGame1);
        gameSumPerPerson = findViewById(R.id.TVSumGame2);

        showMessage();
    }

    public void fadeIn(View view) {
        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        if (gameState[tappedCounter] == 2 && gameactive) {
            gameState[tappedCounter] = activeplayer;
            counter.setTranslationY(-1500);
            if (activeplayer == 0) {
                counter.setImageResource(R.drawable.kolko);
                activeplayer = 1;
            } else {
                counter.setImageResource(R.drawable.krzyzyk);
                activeplayer = 0;
            }
            counter.animate().translationYBy(1500).rotation(1800).setDuration(750);
            for (int[] winningPositions : winningPositions) {
                if (gameState[winningPositions[0]] == gameState[winningPositions[1]] && gameState[winningPositions[1]] == gameState[winningPositions[2]] && gameState[winningPositions[0]] != 2) {
                    if (activeplayer == 0) {
                        winner = "Krzyżyk wygrał";
                    } else {
                        winner = "Kółko wygrało";
                    }
                    finishGame(winner);
                }
            }
        } else {
            int checkstate = 0;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i] == 2) {
                    checkstate += 1;
                }
            }
            if (checkstate == 0) {
                finishGame("remis");
            }
        }
    }

    public void finishGame(String wynik) {
        if (wynik.equals("remis")) {
            winner = "Remis";
            mediaPlayer = MediaPlayer.create(this, R.raw.remis);
        } else {
            gameactive = false;
            mediaPlayer = MediaPlayer.create(this, R.raw.win);
        }
        winnerTextView.setText(winner);
        winnerTextView.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.INVISIBLE);
        gameSum.setVisibility(View.INVISIBLE);
        gameSumPerPerson.setVisibility(View.INVISIBLE);
        mediaPlayer.start();
    }

    public void playAgain(View view) {
        Arrays.fill(gameState, 2);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView child = (ImageView) gridLayout.getChildAt(i);
            child.setImageDrawable(null);
        }
        gameactive = true;
        activeplayer = 0;
        playAgainButton.setVisibility(View.INVISIBLE);
        winnerTextView.setVisibility(View.INVISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        gameSum.setVisibility(View.VISIBLE);
        gameSumPerPerson.setVisibility(View.VISIBLE);
        mediaPlayer.pause();
    }

    public void showMessage() {
        Intent intent = getIntent();
        String holder = intent.getStringExtra(Intent.EXTRA_TEXT);
        String holder2 = intent.getStringExtra(Intent.EXTRA_TITLE);
        String message;
        if (holder2.isEmpty()) {
            message = "Wprowadź liczbę osób";
            gameSumPerPerson.setText(message);
        } else {
            message = "Kwota za osobę: ";
            message += holder2;
            gameSumPerPerson.setText(message);
        }
        if (holder.isEmpty()) {
            message = "Wprowadź kwotę do zapłaty";
            gameSum.setText(message);
        } else {
            message = "Suma do zapłaty za rachunek: ";
            message += holder;
            gameSum.setText(message);
        }
    }
}