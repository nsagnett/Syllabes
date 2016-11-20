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

package com.syllabes.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.utils.Utils;

public class SyllabesActivity extends AbstractActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabes);

        ((TextView) findViewById(R.id.touch_screen_id)).setTypeface(customFont);
        findViewById(R.id.first_layout_id).setOnClickListener(this);
        findViewById(R.id.quit_button_id).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.playSound("home", this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_layout_id:
                startActivity(new Intent(SyllabesActivity.this, GamesChoiceActivity.class));
                break;
            case R.id.quit_button_id:
                onBackPressed();
                break;
        }
    }
}
