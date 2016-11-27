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

package com.syllabes.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.SparseArray;

import com.syllabes.R;
import com.syllabes.activities.menu.GamesChoiceActivity;
import com.syllabes.model.Word;
import com.syllabes.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractActivity extends Activity {
    protected static final String WORD_WIN_KEY = "WordWinKey";
    protected static final String GAME_ID_KEY = "GameIdKey";

    protected static ArrayList<Word> Words = new ArrayList<>();
    protected static SparseArray<String> Syllabes = new SparseArray<>();

    protected Typeface customFont;
    protected Word randomWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initWords();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Utils.mPlayer.stop();
        if (this instanceof GamesChoiceActivity) {
            Utils.playSound("fin", this);
        }
    }

    protected void prepareWord() {
        Random random = new Random();
        do {
            randomWord = Words.get(random.nextInt(Words.size()));
        }
        while (randomWord.isAlreadyUsed());
        randomWord.setAlreadyUsed(true);
    }

    private void initWords() {
        if (Words != null && Words.size() > 0) {
            boolean find = false;
            int i = 0;
            while (!find && i < Words.size()) {
                if (!Words.get(i).isAlreadyUsed()) {
                    find = true;
                } else {
                    i++;
                }
            }

            if (!find) {
                for (Word w : Words) {
                    w.setAlreadyUsed(false);
                }
            }
        }
    }
}
