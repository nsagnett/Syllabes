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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.syllabes.R;
import com.syllabes.activities.AbstractActivity;
import com.syllabes.model.Word;
import com.syllabes.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SplashScreenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Utils.SonSyllabes = new HashMap<String, Integer>();
                Utils.SonMots = new HashMap<String, Integer>();
                Utils.TexteSyllabes = new ArrayList<String>();
                Utils.TexteMots = new ArrayList<String>();
                Utils.ImagesMots = new HashMap<String, Integer>();
                Utils.Questions = new HashMap<String, Integer>();
                customFont = Typeface.createFromAsset(getAssets(), "fonts/a_for_a.ttf");

                try {
                    InputStream is = getAssets().open("data.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    if (is.read(buffer) == -1) {
                        is.close();
                        return null;
                    }
                    is.close();
                    JSONObject result = new JSONObject(new String(buffer, "UTF-8"));
                    JSONObject words = result.getJSONObject("words");
                    Iterator<String> iter = words.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        Word w = new Word(key);

                        JSONArray jsa = words.getJSONArray(key);
                        int length = jsa.length();
                        String[] sa = new String[length];
                        for (int i = 0; i < length; i++) {
                            sa[i] = jsa.getString(i);
                        }
                        w.setSyllabes(sa);
                        Words.add(w);
                    }
                    JSONArray array = result.getJSONArray("syllabes");
                    int length = array.length();
                    for (int i = 0; i < length; i++) {
                        Syllabes.append(i + 1, array.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreenActivity.this,
                                SyllabesActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 1400);
            }
        }.execute();
    }
}
