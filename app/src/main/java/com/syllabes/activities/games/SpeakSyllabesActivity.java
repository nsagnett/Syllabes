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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.activities.info.SpeakSyllabesInfoActivity;
import com.syllabes.activities.menu.VictoryActivity;
import com.syllabes.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class SpeakSyllabesActivity extends AbstractActivity implements
        OnClickListener {

    private final int RESULT_CODE = 1;
    private Button micro = null;
    private Button retour = null;
    private ImageView image1 = null;
    private ImageView image2 = null;
    private ImageView image3 = null;
    private Button repeter = null;
    private Button solution = null;
    private Button info = null;

    private ArrayList<ImageView> images_selection = null;
    private ArrayList<Boolean> images_prises = null;
    private ArrayList<String> mots = null;

    private TextView texte = null;
    private Random random = null;
    private int selecteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

		/*initView();

		setClickListeners();

		initArray();

		setImages();

		Utils.playSound(R.raw.intro, this);

		Utils.mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Utils.playSound(Utils.Questions.get(Utils.reponse),
						SpeakSyllabesActivity.this);
			}
		});

		clearArray();*/
    }

    /*private void initView() {
        micro = (Button) findViewById(R.id.button_micro);
        image1 = (ImageView) findViewById(R.id.image_speak);
        image2 = (ImageView) findViewById(R.id.image_speak2);
        image3 = (ImageView) findViewById(R.id.image_speak3);
        texte = (TextView) findViewById(R.id.texte_utilisateur_speak);
        retour = (Button) findViewById(R.id.button_speak_return);
        repeter = (Button) findViewById(R.id.button_speak_repeat);
        solution = (Button) findViewById(R.id.button_speak_answer);
        info = (Button) findViewById(R.id.button_speak_info);
    }

    private void setImages() {
        selecteur = random.nextInt(Utils.mots_jeu3.size());
        Utils.reponse = Utils.mots_jeu3.get(selecteur);
        Utils.mots_jeu3.remove(selecteur);
        mots.remove(selecteur);

        selecteur = random.nextInt(images_selection.size());
        images_selection.get(selecteur).setImageResource(
                Utils.ImagesMots.get(Utils.reponse));
        images_prises.set(selecteur, true);
        images_selection.get(selecteur).setContentDescription(Utils.reponse);
        images_selection.get(selecteur).setOnClickListener(this);

        int taille = images_selection.size();
        for (int i = 0; i < taille; i++) {
            if (!images_prises.get(i)) {
                selecteur = random.nextInt(Utils.mots_jeu3.size());
                String s = Utils.mots_jeu3.get(selecteur);
                images_selection.get(i).setImageResource(
                        Utils.ImagesMots.get(s));
                images_selection.get(i).setContentDescription(s);
                images_selection.get(i).setOnClickListener(this);
                mots.remove(selecteur);
                images_prises.set(i, true);
            }
        }
    }

    private void clearArray() {
        images_selection.clear();
        mots.clear();
    }

    private void initArray() {
        random = new Random();
        images_selection = new ArrayList<ImageView>();
        images_prises = new ArrayList<Boolean>();
        mots = new ArrayList<String>();
        images_selection.add(image1);
        images_selection.add(image2);
        images_selection.add(image3);
        int taille = images_selection.size();
        for (int i = 0; i < taille; i++) {
            images_prises.add(false);
        }
        taille = Utils.TexteMots.size();
        for (int i = 0; i < taille; i++) {
            mots.add(Utils.TexteMots.get(i));
        }

        if (Utils.mots_jeu3.size() == 0) {
            for (int i = 0; i < Utils.TexteMots.size(); i++) {
                Utils.mots_jeu3.add(Utils.TexteMots.get(i));
            }
        }
    }

    private void setClickListeners() {
        micro.setOnClickListener(this);
        retour.setOnClickListener(this);
        repeter.setOnClickListener(this);
        solution.setOnClickListener(this);
        info.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK) {
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String s0 = results.get(0);

            if (s0.contains("�"))
                s0 = s0.replace('�', 'e');

            Utils.mPlayer_2.start();
            texte.setText(s0.toUpperCase());
            texte.setTextColor(getResources().getColor(android.R.color.black));
            victory();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
    private void victory() {
        if (texte.getText().toString().toLowerCase().contains(Utils.reponse)) {
            texte.setTextColor(getResources().getColor(R.color.valid));
            Intent i = new Intent(SpeakSyllabesActivity.this,
                    VictoryActivity.class);
            i.putExtra(GAME_ID_KEY, SpeakSyllabesActivity.class.getSimpleName());
            i.putExtra(WORD_WIN_KEY, Utils.reponse);
            startActivity(i);
            finish();
        } else {
            texte.setTextColor(android.graphics.Color.RED);
            Utils.playSound("sound_fail", this);
            Utils.mPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    texte.setText("");
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_speak_return:
                onBackPressed();
                break;
            case R.id.button_micro:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, RESULT_CODE);
                break;
            case R.id.button_speak_repeat:
                /*Utils.playSound(Utils.Questions.get(Utils.reponse),
                        SpeakSyllabesActivity.this);*/
                break;
            case R.id.button_speak_answer:
                texte.setTextColor(getResources().getColor(android.R.color.black));
                texte.setText(Utils.reponse.toUpperCase());
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        texte.setText("");
                    }
                }, Utils.DELAI_AFFICHAGE_REPONSE);
                break;

            case R.id.button_speak_info:
                startActivity(new Intent(SpeakSyllabesActivity.this,
                        SpeakSyllabesInfoActivity.class));
                break;

            case R.id.image_speak:
                texte.setText(v.getContentDescription().toString().toUpperCase());
                victory();
                break;

            case R.id.image_speak2:
                texte.setText(v.getContentDescription().toString().toUpperCase());
                victory();
                break;

            case R.id.image_speak3:
                texte.setText(v.getContentDescription().toString().toUpperCase());
                victory();
                break;
            default:
                break;
        }
    }
}
