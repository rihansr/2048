package com.rihansr.a2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Vibrator;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Common {

    private static boolean isSoundPlaying = false;
    private static MediaPlayer mediaPlayer;

    public static String Best_Score_Key = "Best_Score_Key";
    public static String Current_Score_Key = "Current_Score_Key";
    public static String Digit_Key = "Digit_Key";
    public static String Tag_Key = "Tag_Key";
    public static String Moves_Key = "Moves_Key";
    public static String Challenge_One_Key = "Challenge_One_Key";
    public static String Challenge_Two_Key = "Challenge_Two_Key";
    public static String Challenge_Three_Key = "Challenge_Three_Key";


    /* Vibration*/
    public static void Set_Vibration_Mode(Context context, boolean state){
        context.getSharedPreferences("VibrationMode", Context.MODE_PRIVATE).edit().putBoolean("Vibration_Key",state).apply();
    }

    public static Boolean Get_Vibration_Mode(Context context){
        return context.getSharedPreferences("VibrationMode", Context.MODE_PRIVATE).getBoolean("Vibration_Key",true);
    }

    public static void Vibration(Context context){

        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibe != null) {
            if(Get_Vibration_Mode(context)){
                vibe.vibrate(25);
            }
            else {
                vibe.cancel();
            }
        }
    }


    /* Sound */
    public static void Set_Sound_Mode(Context context, boolean state){
        context.getSharedPreferences("SoundMode", Context.MODE_PRIVATE).edit().putBoolean("Sound_Key",state).apply();
    }

    public static Boolean Get_Sound_Mode(Context context){
        return context.getSharedPreferences("SoundMode", Context.MODE_PRIVATE).getBoolean("Sound_Key",true);
    }

    public static void Sound(Context context, String sound){

        if(Get_Sound_Mode(context)){

            if(isSoundPlaying){
                mediaPlayer.release();
                isSoundPlaying = false;
            }
            switch (sound){
                case "Click":
                    mediaPlayer = MediaPlayer.create(context, R.raw.click_sound);
                    break;

                case "Move":
                    mediaPlayer = MediaPlayer.create(context, R.raw.move_sound);
                    break;

                case "Win":
                    mediaPlayer = MediaPlayer.create(context, R.raw.win_sound);
                    break;

                case "Lose":
                    mediaPlayer = MediaPlayer.create(context, R.raw.lose_sound);
                    break;

                case "Capture":
                    mediaPlayer = MediaPlayer.create(context, R.raw.capture_sound);
                    break;

                default:
                    mediaPlayer = MediaPlayer.create(context, R.raw.click_sound);
            }
            isSoundPlaying = true;
            mediaPlayer.start();
        }
    }


    /* Animation */
    public static void Bounce_Animation(Context context, View view, double amplitude, double frequency){
        Animation Bounce_Animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(amplitude,frequency);
        Bounce_Animation.setInterpolator(interpolator);
        view.startAnimation(Bounce_Animation);
    }


    /* Jelly Animation */
    public static void Jelly_Animation(final Context context, final View view, final int time){

       try {
           Animation Bounce_Animation = AnimationUtils.loadAnimation(context, R.anim.jelly);
           MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1,30);
           Bounce_Animation.setInterpolator(interpolator);
           view.startAnimation(Bounce_Animation);

           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   Jelly_Animation(context,view, time);
               }
           },time);
       }
       catch (Exception ex){
           ex.printStackTrace();
       }
    }


    /* Font */
    public static void Set_Font(Context context, View[] view){
        for(View font_View: view){
            if(font_View instanceof TextView){
                ((TextView) font_View).setTypeface(Typeface.createFromAsset(context.getAssets(), "vinque.ttf"));
            }
            if(font_View instanceof Button){
                ((Button) font_View).setTypeface(Typeface.createFromAsset(context.getAssets(), "vinque.ttf"));
            }
        }
    }


    /* Score */
    public static void Set_Game_Score(Context context, String key, long score){
        context.getSharedPreferences("GameScore", Context.MODE_PRIVATE).edit().putLong(key, score).apply();
    }

    public static long Get_Game_Score(Context context, String key){
        return context.getSharedPreferences("GameScore", Context.MODE_PRIVATE).getLong(key, 0);
    }


    /* Lives */
    public static void Set_Lives_Remaining(Context context, int remaining){
        context.getSharedPreferences("LivesRemaining", Context.MODE_PRIVATE).edit().putInt("Lives_Key", remaining).apply();
    }

    public static int Get_Lives_Remaining(Context context){
        return context.getSharedPreferences("LivesRemaining", Context.MODE_PRIVATE).getInt("Lives_Key", 3);
    }


    /* Tile Tag */
    public static void Set_Tile_Tag(Context context, String key, String value){
        context.getSharedPreferences("Tile_Tag", Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String Get_Tile_Tag(Context context){
        return context.getSharedPreferences("Tile_Tag", Context.MODE_PRIVATE).getString(Tag_Key, null);
    }


    /* Challenge */
    public static void Set_Challenge_Mode(Context context, boolean isChallenges){
        context.getSharedPreferences("ChallengeMode", Context.MODE_PRIVATE).edit().putBoolean("ChallengeMode_Key", isChallenges).apply();
    }

    public static Boolean Get_Challenge_Mode(Context context){
        return context.getSharedPreferences("ChallengeMode", Context.MODE_PRIVATE).getBoolean("ChallengeMode_Key",false);
    }

    public static void Set_Challenge_Id(Context context, int id){
        context.getSharedPreferences("Challenges", Context.MODE_PRIVATE).edit().putInt("Challenge_Key", id).apply();
    }

    public static int Get_Challenge_Id(Context context){
        return context.getSharedPreferences("Challenges", Context.MODE_PRIVATE).getInt("Challenge_Key", 0);
    }

    public static void Set_Unlock_Challenge(Context context, int id){
        context.getSharedPreferences("ChallengeUnlock", Context.MODE_PRIVATE).edit().putInt("Unlock_Key", id).apply();
    }

    public static int Get_Unlock_Challenge(Context context){
        return context.getSharedPreferences("ChallengeUnlock", Context.MODE_PRIVATE).getInt("Unlock_Key", 1);
    }


    /* Game Resume */
    public static void Set_Is_Game_Resumed(Context context, boolean isResumed){
        context.getSharedPreferences("ResumeGame", Context.MODE_PRIVATE).edit().putBoolean("Resume",isResumed).apply();
    }

    public static Boolean Get_Is_Game_Resumed(Context context){
        return context.getSharedPreferences("ResumeGame", Context.MODE_PRIVATE).getBoolean("Resume",false);
    }

    public static void Set_Resume_Data(Context context, String key, String value){
        context.getSharedPreferences("ResumeData", Context.MODE_PRIVATE).edit().putString(key,value).apply();
    }

    public static Map<String, String> Get_Resumed_Data(Context context){
        SharedPreferences sp = context.getSharedPreferences("ResumeData", Context.MODE_PRIVATE);
        Map<String, String> Data = new HashMap<>();
        Data.put(Moves_Key, sp.getString(Moves_Key, "0"));
        Data.put(Challenge_One_Key, sp.getString(Challenge_One_Key, "0"));
        Data.put(Challenge_Two_Key, sp.getString(Challenge_Two_Key, "0"));
        Data.put(Challenge_Three_Key, sp.getString(Challenge_Three_Key, "0"));

        for(int i=1; i<=16; i++){
            Data.put(String.valueOf(i), sp.getString(String.valueOf(i), "2"));
        }

        return Data;
    }


    /* Toast */
    public static void Custom_Toast(Context context, String message){
        Typeface toastTf = Typeface.createFromAsset(context.getAssets(), "vinque.ttf");
        final Toast toast = Toast.makeText(context,message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        TextView textView = toastView.findViewById(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTypeface(toastTf);
        toastView.setPadding(20,10,20,15);
        toastView.setBackgroundResource(R.drawable.shape_toast);
        toast.show();
        Vibration(context);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }


    /* Custom SnackBar: */
    public static void Custom_SnackBar(final Context context, final View layout, final String message){

        Typeface snackBarTf = Typeface.createFromAsset(context.getAssets(), "vinque.ttf");

        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout s_layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = s_layout.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setTypeface(snackBarTf);
        textView.setMaxLines(3);
        s_layout.setBackground(context.getResources().getDrawable(R.drawable.shape_snackbar));
        snackbar.show();
    }


    /* Check Internet Connection: */
    public static boolean Is_Internet_Connected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null){

                for (NetworkInfo anInfo : info){

                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {

                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                            int     exitValue = ipProcess.waitFor();
                            return (exitValue == 0);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            }
        }
        return false;
    }
}
