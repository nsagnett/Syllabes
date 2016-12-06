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
import android.widget.ImageView;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.activities.games.SoundSyllabesActivity;
import com.syllabes.activities.games.SpeakSyllabesActivity;
import com.syllabes.activities.games.SyllaBubbleActivity;
import com.syllabes.res.Player;

public class VictoryActivity extends AbstractActivity implements
        OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        String response = getIntent().getStringExtra(WORD_WIN_KEY);
        if (response != null) {
            ((ImageView) findViewById(R.id.image_id_victory)).setImageResource(getResources().getIdentifier(response, "drawable", getPackageName()));
        }

        findViewById(R.id.victory_play_again).setOnClickListener(this);
        findViewById(R.id.victory_menu).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Player.playSound("clap_clap_victory", VictoryActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.victory_play_again:
                String name = getIntent().getStringExtra(GAME_ID_KEY);
                if (name != null) {
                    if (name.equals(SoundSyllabesActivity.class.getSimpleName())) {
                        startActivity(new Intent(VictoryActivity.this, SoundSyllabesActivity.class));
                    } else if (name.equals(SyllaBubbleActivity.class.getSimpleName())) {
                        startActivity(new Intent(VictoryActivity.this, SyllaBubbleActivity.class));
                    } else if (name.equals(SpeakSyllabesActivity.class.getSimpleName())) {
                        startActivity(new Intent(VictoryActivity.this, SpeakSyllabesActivity.class));
                    }
                    finish();
                }
                break;
            case R.id.victory_menu:
                onBackPressed();
                break;
        }
    }
}
