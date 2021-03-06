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

package com.syllabes.v2.activities.games;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syllabes.v2.R;
import com.syllabes.v2.activities.menu.AbstractGameActivity;
import com.syllabes.v2.activities.menu.VictoryActivity;
import com.syllabes.v2.res.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SyllaBubbleActivity extends AbstractGameActivity implements OnClickListener {

    private static final int BUBBLE_COUNT_PER_QUEUE = 6;
    private static final int QUEUE_COUNT = 2;

    private final ArrayList<Button> bubbles = new ArrayList<>();
    private final ArrayList<TextView> answerTextViews = new ArrayList<>();
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
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.answer:
                final SparseArray<String> backup = new SparseArray<>();
                for (int i = 0; i < answerTextViews.size(); i++) {
                    backup.put(i, answerTextViews.get(i).getText().toString());
                    answerTextViews.get(i).setText(randomWord.getSyllabes()[i].toUpperCase());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < backup.size(); i++) {
                            answerTextViews.get(i).setText(backup.get(i));
                        }
                    }
                }, SHOW_RESPONSE_TIME);
                break;
            case R.id.imageTip:
                AlphaAnimation a = new AlphaAnimation(1.f, 0.f);
                a.setFillAfter(true);
                a.setDuration(400);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ((ImageView) v).setImageResource(getResources().getIdentifier("img_" + randomWord.getLabel(), "drawable", getPackageName()));
                        AlphaAnimation a = new AlphaAnimation(0.f, 1.f);
                        a.setFillAfter(true);
                        a.setDuration(400);
                        v.startAnimation(a);
                        v.setOnClickListener(null);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(a);

                break;
        }
    }

    @Override
    protected void prepareWord() {
        super.prepareWord();
        int randomWordSyllabesLength = randomWord.getSyllabes().length;
        validSyllabes = Arrays.copyOf(randomWord.getSyllabes(), randomWordSyllabesLength);
        Random random = new Random();

        int length = randomWordSyllabesLength / 2;
        for (int i = 0; i < length; i++) {
            validSyllabes[random.nextInt(randomWordSyllabesLength)] = "";
        }
        missingSyllabeCount = getMissingSyllabeCount();

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
        Player.playSound("syllabe_a_trouver", this);
    }

    @Override
    protected void initView() {
        findViewById(R.id.answer).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int bubbleSize = (int) getResources().getDimension(R.dimen.bubble_image_size);
        float bubbleTextSize = getResources().getDimension(R.dimen.bubble_text_size);
        int availableWidth = metrics.widthPixels / BUBBLE_COUNT_PER_QUEUE;

        final RelativeLayout bubbleGroup = ((RelativeLayout) findViewById(R.id.bubbleGroup));
        for (int i = 0; i < QUEUE_COUNT; i++) {
            for (int j = 0; j < BUBBLE_COUNT_PER_QUEUE; j++) {
                RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(bubbleSize, bubbleSize);
                final Button button = new Button(this);
                btnParams.setMargins(j * availableWidth + bubbleSize / 4, i * (bubbleSize) + i * bubbleSize / 4, 0, 0);
                button.setLayoutParams(btnParams);
                button.setTextColor(Color.BLACK);
                button.setTextSize(bubbleTextSize);
                button.setBackgroundResource(R.drawable.custom_button_bubble);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        for (int k = 0; k < validSyllabes.length; k++) {
                            TextView current = answerTextViews.get(k);
                            if (TextUtils.isEmpty(current.getText())) {
                                current.setText(((TextView) view).getText());
                                missingSyllabeCount--;
                                break;
                            }
                        }
                        Player.playSound("bubble_pop", SyllaBubbleActivity.this).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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

    @Override
    protected void checkWin() {
        if (missingSyllabeCount == 0) {
            String response = "";
            for (TextView t : answerTextViews) {
                response += t.getText();
            }
            if (response.toLowerCase().equals(randomWord.getLabel())) {
                for (TextView t : answerTextViews) {
                    t.setTextColor(getResources().getColor(R.color.valid));
                }
                startActivity(new Intent(SyllaBubbleActivity.this, VictoryActivity.class)
                        .putExtra(WORD_WIN_KEY, randomWord.getLabel())
                        .putExtra(GAME_ID_KEY, SyllaBubbleActivity.class.getSimpleName()));
                finish();
            } else {
                checkSyllabesCompleted(true);
                missingSyllabeCount = getMissingSyllabeCount();
                Player.playSound("sound_fail", SyllaBubbleActivity.this).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        for (int i = 0; i < validSyllabes.length; i++) {
                            TextView current = answerTextViews.get(i);
                            if (TextUtils.isEmpty(validSyllabes[i])) {
                                current.setTextColor(Color.BLACK);
                                current.setText("");
                            }
                        }
                    }
                });
            }
        } else {
            checkSyllabesCompleted(false);
        }
    }

    private void prepareAnswerView(int length) {
        final LinearLayout answerViewGroup = ((LinearLayout) findViewById(R.id.bottomLayout));
        float bubbleTextSize = getResources().getDimension(R.dimen.bubble_answer_text_size);

        int bubbleAnswerImageSize = (int) getResources().getDimension(R.dimen.bubble_image_answer_size);
        int bubbleAnswerImageMargin = (int) getResources().getDimension(R.dimen.bubble_image_answer_margin);
        for (int i = 0; i < length; i++) {
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(bubbleAnswerImageSize, bubbleAnswerImageSize);
            TextView t = new TextView(this);
            tvParams.setMargins(bubbleAnswerImageMargin, 0, 0, 0);
            t.setLayoutParams(tvParams);
            t.setTextSize(bubbleTextSize);
            t.setGravity(Gravity.CENTER);
            t.setBackgroundResource(R.drawable.img_circle_bubble);
            if (TextUtils.isEmpty(validSyllabes[i])) {
                t.setTextColor(Color.BLACK);
            } else {
                t.setTextColor(getResources().getColor(R.color.valid));
            }
            t.setText(validSyllabes[i].toUpperCase());

            answerTextViews.add(t);
            answerViewGroup.addView(t);
            Animation a = AnimationUtils.loadAnimation(this, R.anim.fadein);
            a.setStartOffset((i * length) * 200);
            t.startAnimation(a);
        }
        ImageView imageTip = ((ImageView) findViewById(R.id.imageTip));
        imageTip.setOnClickListener(this);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadein);
        a.setStartOffset(length * 500);
        imageTip.startAnimation(a);
    }

    private int getMissingSyllabeCount() {
        int count = 0;
        for (String validSyllabe : validSyllabes) {
            if (TextUtils.isEmpty(validSyllabe)) {
                count++;
            }
        }
        return count;
    }

    private void checkSyllabesCompleted(boolean redColorRequired) {
        for (int i = 0; i < validSyllabes.length; i++) {
            TextView current = answerTextViews.get(i);
            String text = current.getText().toString().toLowerCase();
            if (TextUtils.isEmpty(validSyllabes[i]) && !text.equals(randomWord.getSyllabes()[i]) && redColorRequired) {
                current.setTextColor(Color.RED);
            } else {
                validSyllabes[i] = text.toLowerCase();
                current.setTextColor(getResources().getColor(R.color.valid));
            }
        }
    }
}