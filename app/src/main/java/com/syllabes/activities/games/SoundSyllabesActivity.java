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

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syllabes.R;
import com.syllabes.activities.info.SoundSyllabesInfoActivity;
import com.syllabes.activities.menu.AbstractGameActivity;
import com.syllabes.activities.menu.VictoryActivity;
import com.syllabes.utils.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SoundSyllabesActivity extends AbstractGameActivity implements OnClickListener {
    private final ArrayList<Button> buttons = new ArrayList<>();

    private TextView userInput;

    private int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_syllabes_layout);

        initView();
        prepareWord();
    }

    @Override
    protected void prepareWord() {
        super.prepareWord();
        Random random = new Random();

        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < randomWord.getSyllabes().length) {
            indexes.add(random.nextInt(buttons.size()));
        }

        int i = 0;
        for (int j = 0; j < buttons.size(); j++) {
            if (indexes.contains(j)) {
                buttons.get(j).setText(randomWord.getSyllabes()[i].toUpperCase());
                i++;
            } else {
                buttons.get(j).setText(Syllabes.get(random.nextInt(Syllabes.size()) + 1).toUpperCase());
            }
        }

        Player.playSound("mot_a_trouver", this).setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Player.playSound(randomWord.getLabel(), SoundSyllabesActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cleanInput:
                clickCounter = 0;
                userInput.setText("");
                break;
            case R.id.repeat:
                Player.playSound(randomWord.getLabel(), SoundSyllabesActivity.this);
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.answer:
                final CharSequence backup = userInput.getText();
                userInput.setText(randomWord.getLabel().toUpperCase());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        userInput.setText(backup);
                    }
                }, SHOW_RESPONSE_TIME);
                break;

            case R.id.info:
                startActivity(new Intent(SoundSyllabesActivity.this,
                        SoundSyllabesInfoActivity.class));
                break;
        }
    }


    @Override
    protected void initView() {
        userInput = (TextView) findViewById(R.id.userInput);
        userInput.setTypeface(customFont);

        prepareButtons((LinearLayout) findViewById(R.id.buttonGroup1));
        prepareButtons((LinearLayout) findViewById(R.id.buttonGroup2));

        findViewById(R.id.repeat).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.answer).setOnClickListener(this);
        findViewById(R.id.cleanInput).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);
    }

    @Override
    protected void checkWin() {
        if (clickCounter == randomWord.getSyllabes().length) {
            clickCounter = 0;
            if (userInput.getText().toString().toLowerCase().equals(randomWord.getLabel())) {
                userInput.setTextColor(ContextCompat.getColor(this, R.color.valid));
                startActivity(new Intent(SoundSyllabesActivity.this, VictoryActivity.class)
                        .putExtra(WORD_WIN_KEY, randomWord.getLabel())
                        .putExtra(GAME_ID_KEY, SoundSyllabesActivity.class.getSimpleName()));
                finish();
            } else {
                userInput.setTextColor(android.graphics.Color.RED);
                Player.playSound("sound_fail", SoundSyllabesActivity.this).setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        resetScreen();
                    }
                });
            }
        } else {
            toggleClick(true);
        }
    }

    private void prepareButtons(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            if (child instanceof ViewGroup)
                prepareButtons((ViewGroup) child);
            else if (child instanceof Button) {
                final Button b = (Button) child;
                b.setTypeface(customFont);
                buttons.add(b);
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleClick(false);
                        clickCounter++;
                        userInput.setText(TextUtils.concat(userInput.getText(), b.getText()));
                        Player.playSound(b.getText().toString().toLowerCase(), SoundSyllabesActivity.this).setOnCompletionListener(new OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                checkWin();
                            }
                        });
                    }
                });
            }
        }
    }

    private void toggleClick(boolean enabled) {
        for (Button b : buttons) {
            b.setClickable(enabled);
        }
    }

    private void resetScreen() {
        userInput.setText("");
        userInput.setTextColor(ContextCompat.getColor(SoundSyllabesActivity.this, android.R.color.black));
        toggleClick(true);
    }
}
