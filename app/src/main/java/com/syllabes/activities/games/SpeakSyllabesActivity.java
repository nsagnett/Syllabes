//Authors : Jordan Chabrier, Arthur D�camp, Ludwig Raepsaet, Nicolas Sagnette, Cl�ment Saubagnac
//2014

// LICENCE
//-----------------------------------------
//This file is part of Syllabes.
//
//    Syllabes is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Syllabes is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Syllabes.  If not, see <http://www.gnu.org/licenses/>.
//-----------------------------------------

package com.syllabes.activities.games;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.syllabes.R;
import com.syllabes.activities.info.SpeakSyllabesInfoActivity;
import com.syllabes.activities.menu.AbstractGameActivity;
import com.syllabes.activities.menu.VictoryActivity;
import com.syllabes.res.OnWaitingCompletionListener;
import com.syllabes.res.Player;
import com.syllabes.res.Word;

import java.util.ArrayList;
import java.util.Random;

public class SpeakSyllabesActivity extends AbstractGameActivity implements OnClickListener {

    private final int RESULT_CODE = 1;
    private final Word[] wordsList = new Word[4];

    private TextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        initView();
        prepareWord();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK) {
            ArrayList<String> suggestions = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String best = suggestions.get(0);
            if (best.split(" ").length <= 2) {
                userText.setText(best.toUpperCase());
            } else {
                userText.setText("TROP LONG !");
            }

            userText.setTextColor(getResources().getColor(android.R.color.black));
            checkWin();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.micro:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, RESULT_CODE);
                break;
            case R.id.repeat:
                Player.playSound(randomWord.getLabel() + "_question", this);
                break;
            case R.id.answer:
                userText.setTextColor(getResources().getColor(android.R.color.black));
                userText.setText(randomWord.getLabel().toUpperCase());
                new Handler().postDelayed(() -> userText.setText(""), SHOW_RESPONSE_TIME);
                break;
            case R.id.info:
                startActivity(new Intent(SpeakSyllabesActivity.this,
                        SpeakSyllabesInfoActivity.class));
                break;
            case R.id.firstImage:
            case R.id.secondImage:
            case R.id.thirdImage:
            case R.id.fourthImage:
                userText.setText(v.getContentDescription().toString().toUpperCase());
                checkWin();
                break;
        }
    }

    @Override
    protected void prepareWord() {
        super.prepareWord();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            Word other;
            do {
                other = Words.get(random.nextInt(Words.size()));
            }
            while (other.getLabel().equals(randomWord.getLabel()));
            wordsList[i] = other;
        }
        wordsList[random.nextInt(wordsList.length)] = randomWord;

        int i = 0;
        ImageView firstImage = ((ImageView) findViewById(R.id.firstImage));
        firstImage.setImageResource(getResources().getIdentifier(wordsList[i].getLabel(), "drawable", getPackageName()));
        firstImage.setContentDescription(wordsList[i++].getLabel());
        firstImage.setOnClickListener(this);

        ImageView secondImage = ((ImageView) findViewById(R.id.secondImage));
        secondImage.setImageResource(getResources().getIdentifier(wordsList[i].getLabel(), "drawable", getPackageName()));
        secondImage.setContentDescription(wordsList[i++].getLabel());
        secondImage.setOnClickListener(this);

        ImageView thirdImage = ((ImageView) findViewById(R.id.thirdImage));
        thirdImage.setImageResource(getResources().getIdentifier(wordsList[i].getLabel(), "drawable", getPackageName()));
        thirdImage.setContentDescription(wordsList[i++].getLabel());
        thirdImage.setOnClickListener(this);

        ImageView fourthImage = ((ImageView) findViewById(R.id.fourthImage));
        fourthImage.setImageResource(getResources().getIdentifier(wordsList[i].getLabel(), "drawable", getPackageName()));
        fourthImage.setContentDescription(wordsList[i].getLabel());
        fourthImage.setOnClickListener(this);

        Player.playSound("intro", this).setOnCompletionListener(mp -> Player.playSound(randomWord.getLabel() + "_question", SpeakSyllabesActivity.this));
    }

    @Override
    protected void initView() {
        findViewById(R.id.micro).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.answer).setOnClickListener(this);
        findViewById(R.id.repeat).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);
        userText = (TextView) findViewById(R.id.userText);
    }

    @Override
    protected void checkWin() {
        if (userText.getText().toString().toLowerCase().contains(randomWord.getLabel())) {
            userText.setTextColor(getResources().getColor(R.color.valid));
            startActivity(new Intent(SpeakSyllabesActivity.this, VictoryActivity.class)
                    .putExtra(GAME_ID_KEY, SpeakSyllabesActivity.class.getSimpleName())
                    .putExtra(WORD_WIN_KEY, randomWord.getLabel()));
            finish();
        } else {
            userText.setTextColor(android.graphics.Color.RED);

            Player.playSound("sound_fail", this).setOnCompletionListener(new OnWaitingCompletionListener(() -> userText.setText("")));
        }
    }
}
