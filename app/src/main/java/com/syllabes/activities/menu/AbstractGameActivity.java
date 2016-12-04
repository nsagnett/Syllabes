package com.syllabes.activities.menu;


import com.syllabes.activities.AbstractActivity;

public abstract class AbstractGameActivity extends AbstractActivity {

    protected abstract void initView();

    protected abstract void checkWin();
}
