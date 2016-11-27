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


package com.syllabes.utils;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
    // Stockage des donnees
    public static HashMap<String, Integer> SonSyllabes = null;
    public static HashMap<String, Integer> SonMots = null;
    public static HashMap<String, Integer> ImagesMots = null;
    public static ArrayList<String> TexteSyllabes = null;
    public static ArrayList<String> TexteMots = null;
    public static HashMap<String, Integer> Questions = null;

    // Lecteur audio
    public static MediaPlayer mPlayer = null;

    public static String reponse = null;

    // Lecture d'un son
    public static MediaPlayer playSound(String resName, Context ctx) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(ctx, ctx.getResources().getIdentifier(resName, "raw", ctx.getPackageName()));
        mPlayer.start();

        return mPlayer;
    }
}
