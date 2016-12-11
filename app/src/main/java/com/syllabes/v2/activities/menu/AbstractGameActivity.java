package com.syllabes.v2.activities.menu;


import com.syllabes.v2.activities.AbstractActivity;

public abstract class AbstractGameActivity extends AbstractActivity {

    protected abstract void initView();

    protected abstract void checkWin();
}
