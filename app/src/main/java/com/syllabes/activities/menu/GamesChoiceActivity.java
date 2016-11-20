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

package com.syllabes.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.activities.games.SoundSyllabesActivity;
import com.syllabes.activities.games.SpeakSyllabesActivity;
import com.syllabes.activities.games.SyllaBubbleActivity;
import com.syllabes.utils.Utils;

public class GamesChoiceActivity extends AbstractActivity implements
        OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_choice);

        findViewById(R.id.back_id).setOnClickListener(this);
        findViewById(R.id.sound_syllabes_button).setOnClickListener(this);
        findViewById(R.id.syllabubble_button).setOnClickListener(this);
        findViewById(R.id.speak_button).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.playSound("choix_jeu", GamesChoiceActivity.this);
    }

    @Override
    public void onClick(View v) {
        Utils.mPlayer.stop();
        switch (v.getId()) {
            case R.id.back_id:
                onBackPressed();
                break;
            case R.id.sound_syllabes_button:
                startActivity(new Intent(GamesChoiceActivity.this, SoundSyllabesActivity.class));
                break;
            case R.id.syllabubble_button:
                startActivity(new Intent(GamesChoiceActivity.this, SyllaBubbleActivity.class));
                break;
            case R.id.speak_button:
                startActivity(new Intent(GamesChoiceActivity.this, SpeakSyllabesActivity.class));
                break;
        }
    }
}
