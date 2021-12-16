package com.example.mrnapiwek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private EditText etBillamount;
    private TextView tvTip;
    private TextView tvSum;
    private TextView tvBillpercent;
    private TextView tvQuality;
    private TextView tvBillperperson;
    private EditText etPeopleamount;
    private ImageView ivVerifyTip;
    private final String[] TipLevel={"Znikomy","Słaby","Akceptowalny","Świetny","Niesamowity"};
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.SBPercent);
        etBillamount = findViewById(R.id.ETBillamount);
        tvTip = findViewById(R.id.TVTip);
        tvSum = findViewById(R.id.TVSum);
        tvBillpercent = findViewById(R.id.TVBillpercent);
        tvQuality = findViewById(R.id.TVQuality);
        tvBillperperson = findViewById(R.id.TVBillperperson);
        etPeopleamount = findViewById(R.id.ETPeopleamount);
        ivVerifyTip=findViewById(R.id.ivVerifyTip);




        updateQuality(-1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateProgress(progress);
                updateSumProgress();
                updateBillperperson();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateQuality(seekBar.getProgress());
            }
        });
        etBillamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSumProgress();
                updateBillperperson();
            }
        });
        etPeopleamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSumProgress();
                updateBillperperson();
            }
        });
    }

    public void updateProgress(int progress) {
        String percent = progress + "%";
        tvBillpercent.setText(percent);
    }

    public void updateSumProgress() {
        if (etBillamount.getText().toString().matches("")) {
            tvTip.setText("");
            tvSum.setText("");
            tvBillperperson.setText("");
        } else {
            try {
                double amount = Double.parseDouble(String.valueOf(etBillamount.getText()));
                double Tip = amount * seekBar.getProgress() / 100;
                double Sum = amount + Tip;
                tvTip.setText(String.valueOf(BigDecimal.valueOf(Tip).setScale(2, RoundingMode.HALF_UP)));
                tvSum.setText(String.valueOf((BigDecimal.valueOf(Sum).setScale(2, RoundingMode.HALF_UP))));
            } catch (NumberFormatException e1) {
                Log.i("NumberFormatException", "catched1");
                etBillamount.setText("");
            }
        }
    }

    public void updateQuality(int progress) {
        if (progress < 0) {
            seekBar.setMax(30);
            seekBar.setProgress(10);
            tvBillpercent.setText("10%");
            tvQuality.setText(TipLevel[2]);
            tvQuality.setTextColor(getResources().getColor(R.color.blue));
        } else {
            tvQuality.setAlpha(0);
            tvQuality.setTranslationX(-1500);
            if (progress <= 4) {
                tvQuality.setText(TipLevel[0]);
                tvQuality.setTextColor(getResources().getColor(R.color.red));
            } else if (progress <= 9) {
                tvQuality.setText(TipLevel[1]);
                tvQuality.setTextColor(getResources().getColor(R.color.orange));
            } else if (progress <= 15) {
                tvQuality.setText(TipLevel[2]);
                tvQuality.setTextColor(getResources().getColor(R.color.blue));
            } else if (progress <= 20) {
                tvQuality.setText(TipLevel[3]);
                tvQuality.setTextColor(getResources().getColor(R.color.pink));
            } else {
                tvQuality.setText(TipLevel[4]);
                tvQuality.setTextColor(getResources().getColor(R.color.green));
            }
            tvQuality.animate().translationXBy(1500);
            tvQuality.animate().alpha(1).setDuration(600);
        }
    }

    public void updateBillperperson() {
        String ViewSum = String.valueOf(tvSum.getText());
        if (etPeopleamount.getText().toString().matches("")) {
            tvBillperperson.setText("");
        } else {
            try {
                if(!ViewSum.isEmpty()){
                    String amount = String.valueOf(etPeopleamount.getText());
                    double Sum = Double.parseDouble(ViewSum);
                    double Billperperson = (Sum / Double.parseDouble(amount));
                    tvBillperperson.setText(String.valueOf(BigDecimal.valueOf(Billperperson).setScale(2, RoundingMode.HALF_UP)));
                }
            } catch (NumberFormatException e1) {
                Log.i("NumberFormatException", "catched2");
                etPeopleamount.setText("");
            }
        }
    }
    public void switchActivity(View view){
        Intent switchActivityIntent=new Intent(this,gameActivity.class);
        String message=tvSum.getText().toString();
        String message1=tvBillperperson.getText().toString();
        switchActivityIntent.putExtra(Intent.EXTRA_TEXT,message);
        switchActivityIntent.putExtra(Intent.EXTRA_TITLE,message1);
        startActivity(switchActivityIntent);
    }
    public void verifyTip(View view){
        if(!String.valueOf(etBillamount.getText()).isEmpty()) {
            String holder=tvQuality.getText().toString();
            if (holder.equals(TipLevel[0])) {
                ivVerifyTip.setImageResource(R.drawable.tiplevel1);
            } else if (holder.equals(TipLevel[1])) {
                ivVerifyTip.setImageResource(R.drawable.tiplevel2);
            } else if (holder.equals(TipLevel[2])) {
                ivVerifyTip.setImageResource(R.drawable.tiplevel3);
            } else if (holder.equals(TipLevel[3])) {
                ivVerifyTip.setImageResource(R.drawable.tiplevel4);
            } else if (holder.equals(TipLevel[4])) {
                mediaPlayer = MediaPlayer.create(this, R.raw.applause);
                mediaPlayer.start();
                ivVerifyTip.setImageResource(R.drawable.tiplevel5);
            }
            Toast.makeText(this, holder + " napiwek", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Wprowadź kwotę rachunku", Toast.LENGTH_LONG).show();
        }
        ivVerifyTip.setAlpha(1.0F);
        new CountDownTimer(3700,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                ivVerifyTip.animate().alpha(0).setDuration(250);
            }
        }.start();
    }
}
