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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.activities.info.SyllaBubbleInfoActivity;
import com.syllabes.utils.Utils;

public class SyllaBubbleActivity extends AbstractActivity implements OnClickListener {

    private int topLimit;
    private int bottomLimit;

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
        ((ImageView) findViewById(R.id.imageTip)).setImageResource(getResources().getIdentifier(randomWord.getLabel(), "drawable", getPackageName()));
        Utils.playSound("syllabe_a_trouver", this);
    }

    private void initView() {
        findViewById(R.id.answer).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        topLimit = findViewById(R.id.answer).getLayoutParams().height;
        bottomLimit = metrics.heightPixels - findViewById(R.id.imageTip).getLayoutParams().height;
        int bubbleSize = (int) (60 * metrics.density);
        int maxWidth = metrics.widthPixels;

        RelativeLayout bubbleGroup = ((RelativeLayout) findViewById(R.id.bubbleGroup));
        for (int i = 0; i < 10; i++) {
            RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(bubbleSize, bubbleSize);
            Button b = new Button(this);
            btnParams.setMargins(i * 15, i * 15, 0, 0);
            b.setLayoutParams(btnParams);
            b.setBackgroundResource(R.drawable.custom_button_bubble);
            b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            bubbleGroup.addView(b);
        }
    }
}
