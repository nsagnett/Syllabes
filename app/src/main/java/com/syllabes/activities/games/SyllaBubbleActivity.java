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

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.activities.info.SyllaBubbleInfoActivity;
import com.syllabes.activities.menu.VictoryActivity;
import com.syllabes.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SyllaBubbleActivity extends AbstractActivity implements OnClickListener {

    private static final int BUBBLE_COUNT_PER_QUEUE = 6;
    private static final int QUEUE_COUNT = 2;
    private static final int ANSWER_VIEW_PIXELS_SIZE = 80;
    private static final int BUBBLES_PIXEL_SIZE = 60;

    private DisplayMetrics metrics;
    private ArrayList<Button> bubbles = new ArrayList<>();
    private ArrayList<Button> answerBubbles = new ArrayList<>();
    private String[] validSyllabes;

    private int missingSyllabeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sylla_bubble);

        initView();
        prepareWord();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.answer:
                break;
            case R.id.info:
                startActivity(new Intent(SyllaBubbleActivity.this, SyllaBubbleInfoActivity.class));
                break;
        }
    }

    @Override
    protected void prepareWord() {
        super.prepareWord();
        int randomWordSyllabesLength = randomWord.getSyllabes().length;
        validSyllabes = Arrays.copyOf(randomWord.getSyllabes(), randomWordSyllabesLength);
        Random random = new Random();
        missingSyllabeCount = randomWordSyllabesLength / 2;

        for (int i = 0; i < missingSyllabeCount; i++) {
            validSyllabes[random.nextInt(randomWordSyllabesLength)] = "";
        }

        prepareAnswerView(randomWordSyllabesLength);

        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < missingSyllabeCount) {
            indexes.add(random.nextInt(bubbles.size()));
        }

        int i = 0;
        for (int j = 0; j < bubbles.size(); j++) {
            if (indexes.contains(j)) {
                for (; i < randomWord.getSyllabes().length; i++) {
                    if (TextUtils.isEmpty(validSyllabes[i])) {
                        bubbles.get(j).setText(randomWord.getSyllabes()[i].toUpperCase());
                        break;
                    }
                }
            } else {
                bubbles.get(j).setText(Syllabes.get(random.nextInt(Syllabes.size()) + 1).toUpperCase());
            }
        }
        Utils.playSound("syllabe_a_trouver", this);
    }

    private void initView() {
        findViewById(R.id.answer).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);

        metrics = getResources().getDisplayMetrics();
        int bubbleSize = (int) (BUBBLES_PIXEL_SIZE * metrics.density);
        int availableWidth = metrics.widthPixels / BUBBLE_COUNT_PER_QUEUE;

        final RelativeLayout bubbleGroup = ((RelativeLayout) findViewById(R.id.bubbleGroup));
        for (int i = 0; i < QUEUE_COUNT; i++) {
            for (int j = 0; j < BUBBLE_COUNT_PER_QUEUE; j++) {
                RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(bubbleSize, bubbleSize);
                final Button button = new Button(this);
                btnParams.setMargins(j * availableWidth + bubbleSize / 3, i * (bubbleSize) + i * bubbleSize / 3, 0, 0);
                button.setLayoutParams(btnParams);
                button.setTextColor(Color.BLACK);
                button.setBackgroundResource(R.drawable.custom_button_bubble);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        for (int k = 0; k < validSyllabes.length; k++) {
                            Button current = answerBubbles.get(k);
                            if (TextUtils.isEmpty(validSyllabes[k])) {
                                current.setText(((Button) view).getText());
                                missingSyllabeCount--;
                                break;
                            }
                        }
                        Utils.playSound("bubble_pop", SyllaBubbleActivity.this).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                checkWin();
                            }
                        });
                        bubbleGroup.removeView(view);
                        bubbleGroup.addView(button);
                        Animation a = AnimationUtils.loadAnimation(SyllaBubbleActivity.this, R.anim.upscale);
                        a.setStartOffset(200);
                        button.startAnimation(a);
                    }
                });
                bubbles.add(button);
                bubbleGroup.addView(button);

                Animation a = AnimationUtils.loadAnimation(this, R.anim.upscale);
                a.setStartOffset(((i + j) + i * BUBBLE_COUNT_PER_QUEUE) * 200);
                button.startAnimation(a);
            }
        }
    }

    private void prepareAnswerView(int length) {
        final LinearLayout answerViewGroup = ((LinearLayout) findViewById(R.id.bottomLayout));
        int bubbleSize = (int) (ANSWER_VIEW_PIXELS_SIZE * metrics.density);

        for (int i = 0; i < length; i++) {
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(bubbleSize, bubbleSize);
            Button b = new Button(this);
            btnParams.setMargins(30, 0, 0, 0);
            b.setLayoutParams(btnParams);
            if (TextUtils.isEmpty(validSyllabes[i])) {
                b.setTextColor(Color.BLACK);
            } else {
                b.setTextColor(getResources().getColor(R.color.valid));
            }
            b.setBackgroundResource(R.drawable.custom_button_bubble);
            b.setText(validSyllabes[i].toUpperCase());

            answerBubbles.add(b);
            answerViewGroup.addView(b);
            Animation a = AnimationUtils.loadAnimation(this, R.anim.upscale);
            a.setStartOffset((i * length) * 200);
            b.startAnimation(a);
        }
        ImageView imageTip = new ImageView(this);
        LinearLayout.LayoutParams imgLP = new LinearLayout.LayoutParams(bubbleSize, bubbleSize);
        imgLP.setMargins(30, 0, 0, 0);
        imageTip.setLayoutParams(imgLP);
        imageTip.setImageResource(getResources().getIdentifier(randomWord.getLabel(), "drawable", getPackageName()));

        answerViewGroup.addView(imageTip);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadein);
        a.setStartOffset(length * 200);
        imageTip.startAnimation(a);
    }

    private void checkWin() {
        if (missingSyllabeCount == 0) {
            missingSyllabeCount = randomWord.getSyllabes().length / 2;
            String response = "";
            for (Button b : answerBubbles) {
                response += b.getText();
            }
            if (response.toLowerCase().equals(randomWord.getLabel())) {
                for (Button b : answerBubbles) {
                    b.setTextColor(getResources().getColor(R.color.valid));
                }
                startActivity(new Intent(SyllaBubbleActivity.this, VictoryActivity.class)
                        .putExtra(WORD_WIN_KEY, randomWord.getLabel())
                        .putExtra(GAME_ID_KEY, SyllaBubbleActivity.class.getSimpleName()));
                finish();
            } else {
                for (int i = 0; i < validSyllabes.length; i++) {
                    Button current = answerBubbles.get(i);
                    String btnText = current.getText().toString().toLowerCase();
                    if (TextUtils.isEmpty(validSyllabes[i]) && !btnText.equals(randomWord.getSyllabes()[i])) {
                        current.setTextColor(Color.RED);
                    } else {
                        validSyllabes[i] = btnText.toLowerCase();
                        current.setTextColor(getResources().getColor(R.color.valid));
                    }
                }
                Utils.playSound("sound_fail", SyllaBubbleActivity.this).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        for (int i = 0; i < validSyllabes.length; i++) {
                            Button current = answerBubbles.get(i);
                            if (TextUtils.isEmpty(validSyllabes[i])) {
                                current.setTextColor(Color.BLACK);
                                current.setText("");
                            }
                        }
                    }
                });
            }
        }
    }
}
