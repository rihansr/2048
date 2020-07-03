package com.rihansr.a2048;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

@SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener{

    private LinearLayout Normal_Mode_Layout, Challenge_Mode_Layout;
    private RelativeLayout Content_Layout;
    private LinearLayout Board_Layout;

    private TextView tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16;
    private TextView app_Name, mode_Tv, ch_App_Name, moves_Count_Tv, current_Score_Tv, best_Score_Tv, menu_Tv, share_Tv;
    private TextView Current_Score, Best_Score, Moves_Count;

    private LinearLayout challenge_One_Layout, challenge_Two_Layout, challenge_Three_Layout;
    private TextView challenge_One_Tv, challenge_Two_Tv, challenge_Three_Tv;
    private TextView challenge_One_Count, challenge_Two_Count, challenge_Three_Count;
    private TextView challenge_One_Target, challenge_Two_Target, challenge_Three_Target;

    private boolean screenOff = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Set_Id();

        View[] views = {app_Name, mode_Tv, ch_App_Name, challenge_One_Tv, challenge_One_Count, challenge_One_Target, challenge_Two_Tv,
                challenge_Two_Count, challenge_Two_Target, challenge_Three_Tv, challenge_Three_Count, challenge_Three_Target,
                moves_Count_Tv, Moves_Count, Current_Score, current_Score_Tv, Best_Score, best_Score_Tv, menu_Tv, share_Tv};

        Common.Set_Font(getApplicationContext(), views);

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        Common.Set_Font(getApplicationContext(), tiles);

        Set_Layout_Size();

        Set_Game_Mode();

        Set_Listener();

        Load_Tiles_Background();

        Load_Game();
    }

    private void Set_Id() {
        Normal_Mode_Layout = findViewById(R.id.normal_mode_layout);
        Challenge_Mode_Layout = findViewById(R.id.challenge_mode_layout);
        Board_Layout = findViewById(R.id.board_layout);
        Content_Layout = findViewById(R.id.content_layout);

        app_Name = findViewById(R.id.appName_tv);
        mode_Tv = findViewById(R.id.mode_tv);
        ch_App_Name = findViewById(R.id.ch_appName_tv);

        challenge_One_Layout = findViewById(R.id.challenge_one_layout);
        challenge_One_Tv = findViewById(R.id.challenge_one_tv);
        challenge_One_Count = findViewById(R.id.challenge_one_count);
        challenge_One_Target = findViewById(R.id.challenge_one_target);

        challenge_Two_Layout = findViewById(R.id.challenge_two_layout);
        challenge_Two_Tv = findViewById(R.id.challenge_two_tv);
        challenge_Two_Count = findViewById(R.id.challenge_two_count);
        challenge_Two_Target = findViewById(R.id.challenge_two_target);

        challenge_Three_Layout = findViewById(R.id.challenge_three_layout);
        challenge_Three_Tv = findViewById(R.id.challenge_three_tv);
        challenge_Three_Count = findViewById(R.id.challenge_three_count);
        challenge_Three_Target = findViewById(R.id.challenge_three_target);

        moves_Count_Tv = findViewById(R.id.moves_count_tv);
        Moves_Count = findViewById(R.id.moves_count);
        Current_Score = findViewById(R.id.current_score_count);
        current_Score_Tv = findViewById(R.id.current_score_tv);
        Best_Score = findViewById(R.id.best_score_count);
        best_Score_Tv = findViewById(R.id.best_score_tv);

        menu_Tv = findViewById(R.id.menu_tv);
        share_Tv = findViewById(R.id.share_tv);

        Common.Jelly_Animation(getApplicationContext(), findViewById(R.id.menu_icon), 1000);
        Common.Jelly_Animation(getApplicationContext(), findViewById(R.id.share_icon), 1000);

        tile_1 = findViewById(R.id.tile_1);
        tile_2 = findViewById(R.id.tile_2);
        tile_3 = findViewById(R.id.tile_3);
        tile_4 = findViewById(R.id.tile_4);
        tile_5 = findViewById(R.id.tile_5);
        tile_6 = findViewById(R.id.tile_6);
        tile_7 = findViewById(R.id.tile_7);
        tile_8 = findViewById(R.id.tile_8);
        tile_9 = findViewById(R.id.tile_9);
        tile_10 = findViewById(R.id.tile_10);
        tile_11 = findViewById(R.id.tile_11);
        tile_12 = findViewById(R.id.tile_12);
        tile_13 = findViewById(R.id.tile_13);
        tile_14 = findViewById(R.id.tile_14);
        tile_15 = findViewById(R.id.tile_15);
        tile_16 = findViewById(R.id.tile_16);

        /* TAG */
        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (int i=0; i < tiles.length; i++){
            tiles[i].setTag(String.valueOf(i+1));
        }
    }

    private void Set_Layout_Size() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Board_Layout.getLayoutParams();
            int size = displayMetrics.widthPixels;

            if(size <= 500){
                params.height = size - (size/12);
                params.width = size - (size/12);
            }
            else{
                params.height = size - (size/10);
                params.width = size - (size/10);
            }

            Board_Layout.setLayoutParams(params);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void Set_Game_Mode(){
        if(Common.Get_Challenge_Mode(getApplicationContext())){
            Normal_Mode_Layout.setVisibility(View.GONE);
            Challenge_Mode_Layout.setVisibility(View.VISIBLE);
        }
        else {
            Normal_Mode_Layout.setVisibility(View.VISIBLE);
            Challenge_Mode_Layout.setVisibility(View.GONE);
            Best_Score.setText(Common.Get_Game_Score(getApplicationContext(), Common.Best_Score_Key) + "");
        }
    }

    private void Load_Tiles_Background(){

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                    tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (TextView tile: tiles){
            Set_Tiles_Style(tile);
        }

        for (TextView view: new TextView[]{challenge_One_Tv, challenge_Two_Tv, challenge_Three_Tv}){
            Set_Challenge_Style(view);
        }
    }

    private void Load_Game() {
        if (Common.Get_Is_Game_Resumed(getApplicationContext())) {
            Reload_Game();
        }
        else {
            Random_Tile_Position_Style();
        }

        if(Is_Game_Over()){
            Disable_Game();
            Game_Over_Dialog();
        }

        if(Common.Get_Challenge_Mode(getApplicationContext()) && Common.Get_Challenge_Id(getApplicationContext()) != 0){
            if(Is_Challenge_Win()){
                Disable_Game();
                Challenge_Win_Dialog();
            }

            if(Is_Challenge_Lose()){
                Disable_Game();
                Game_Over_Dialog();
            }
        }
    }

    private void Random_Tile_Position_Style(){

        switch (new Random().nextInt(16)+1){

            case 1:
                for(TextView tile: new TextView[]{tile_2, tile_4, tile_5, tile_7, tile_10, tile_12, tile_13, tile_15}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_3, tile_6, tile_8, tile_9, tile_11, tile_14, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 2:
                for(TextView tile: new TextView[]{tile_1, tile_3, tile_6, tile_8, tile_9, tile_11, tile_14, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_2, tile_4, tile_5, tile_7, tile_10, tile_12, tile_13, tile_15}){
                    tile.setAlpha(0);
                }
                break;

            case 3:
                for(TextView tile: new TextView[]{tile_1, tile_3, tile_4, tile_5, tile_12, tile_13, tile_14, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_2, tile_6, tile_7, tile_8, tile_9, tile_10, tile_11, tile_15}){
                    tile.setAlpha(0);
                }
                break;

            case 4:
                for(TextView tile: new TextView[]{tile_2, tile_6, tile_7, tile_8, tile_9, tile_10, tile_11, tile_15}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_3, tile_4, tile_5, tile_12, tile_13, tile_14, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 5:
                for(TextView tile: new TextView[]{tile_1, tile_4, tile_6, tile_7, tile_10, tile_11, tile_13, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_2, tile_3, tile_5, tile_8, tile_9, tile_12, tile_14, tile_15}){
                    tile.setAlpha(0);
                }
                break;

            case 6:
                for(TextView tile: new TextView[]{tile_2, tile_3, tile_5, tile_8, tile_9, tile_12, tile_14, tile_15}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_4, tile_6, tile_7, tile_10, tile_11, tile_13, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 7:
                for(TextView tile: new TextView[]{tile_1, tile_5, tile_6, tile_7, tile_10, tile_11, tile_12, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_2, tile_3, tile_4, tile_8, tile_9, tile_13, tile_14, tile_15}){
                    tile.setAlpha(0);
                }
                break;

            case 8:
                for(TextView tile: new TextView[]{tile_2, tile_3, tile_4, tile_8, tile_9, tile_13, tile_14, tile_15}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_5, tile_6, tile_7, tile_10, tile_11, tile_12, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 9:
                for(TextView tile: new TextView[]{tile_1, tile_4, tile_5, tile_6, tile_11, tile_12, tile_13, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_2, tile_3, tile_7, tile_8, tile_9, tile_10, tile_14, tile_15}){
                    tile.setAlpha(0);
                }
                break;

            case 10:
                for(TextView tile: new TextView[]{tile_2, tile_3, tile_7, tile_8, tile_9, tile_10, tile_14, tile_15}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_4, tile_5, tile_6, tile_11, tile_12, tile_13, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 11:
                for(TextView tile: new TextView[]{tile_1, tile_2, tile_5, tile_6, tile_11, tile_12, tile_15, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_3, tile_4, tile_7, tile_8, tile_9, tile_10, tile_13, tile_14}){
                    tile.setAlpha(0);
                }
                break;

            case 12:
                for(TextView tile: new TextView[]{tile_3, tile_4, tile_7, tile_8, tile_9, tile_10, tile_13, tile_14}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_2, tile_5, tile_6, tile_11, tile_12, tile_15, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 13:
                for(TextView tile: new TextView[]{tile_1, tile_2, tile_4, tile_5, tile_12, tile_13, tile_15, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_3, tile_6, tile_7, tile_8, tile_9, tile_10, tile_11, tile_14}){
                    tile.setAlpha(0);
                }
                break;

            case 14:
                for(TextView tile: new TextView[]{tile_3, tile_6, tile_7, tile_8, tile_9, tile_10, tile_11, tile_14}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_2, tile_4, tile_5, tile_12, tile_13, tile_15, tile_16}){
                    tile.setAlpha(0);
                }
                break;

            case 15:
                for(TextView tile: new TextView[]{tile_1, tile_2, tile_5, tile_7, tile_10, tile_12, tile_15, tile_16}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_3, tile_4, tile_6, tile_8, tile_9, tile_11, tile_13, tile_14}){
                    tile.setAlpha(0);
                }
                break;

            case 16:
                for(TextView tile: new TextView[]{tile_3, tile_4, tile_6, tile_8, tile_9, tile_11, tile_13, tile_14}){
                    tile.setAlpha(1);
                    Common.Bounce_Animation(getApplicationContext(), tile, 0.25, 5);
                }

                for (TextView tile: new TextView[]{tile_1, tile_2, tile_5, tile_7, tile_10, tile_12, tile_15, tile_16}){
                    tile.setAlpha(0);
                }
                break;
        }
    }

    private void Set_Listener() {
        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (TextView tile: tiles){
            tile.setOnTouchListener(this);
            tile.setOnDragListener(this);
        }
    }

    @Override public boolean onTouch(View v, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("value", ((TextView) v).getText());
                View.DragShadowBuilder shadowBuilder = new MyDragShadowBuilder(v);

                if(v.getAlpha()==1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.startDragAndDrop(data, shadowBuilder, v, 0);
                    }
                    else {
                        v.startDrag(data, shadowBuilder, v, 0);
                    }
                }
                Common.Set_Tile_Tag(getApplicationContext(), Common.Digit_Key,((TextView) v).getText()+"");
                Common.Set_Tile_Tag(getApplicationContext(), Common.Tag_Key,v.getTag()+"");
                return true;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
        }
        return false;
    }

    @Override public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_LOCATION:
            case DragEvent.ACTION_DRAG_EXITED:
                return true;

            case DragEvent.ACTION_DROP:
                String old_Tag = Common.Get_Tile_Tag(getApplicationContext());
                String new_Tag = v.getTag().toString();

                if(!new_Tag.equals(old_Tag)){
                    Generate_Rules(v);
                    if(Is_Game_Over()){
                        Disable_Game();
                        Game_Over_Dialog();
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:

        }
        return false;
    }

    private void Generate_Rules(View v) {
        String _1 = tile_1.getText().toString();
        String _2 = tile_2.getText().toString();
        String _3 = tile_3.getText().toString();
        String _4 = tile_4.getText().toString();
        String _5 = tile_5.getText().toString();
        String _6 = tile_6.getText().toString();
        String _7 = tile_7.getText().toString();
        String _8 = tile_8.getText().toString();
        String _9 = tile_9.getText().toString();
        String _10 = tile_10.getText().toString();
        String _11 = tile_11.getText().toString();
        String _12 = tile_12.getText().toString();
        String _13 = tile_13.getText().toString();
        String _14 = tile_14.getText().toString();
        String _15 = tile_15.getText().toString();
        String _16 = tile_16.getText().toString();

        switch (Integer.parseInt(Common.Get_Tile_Tag(getApplicationContext()))){
            case 1:
                switch (v.getId()){
                    case R.id.tile_2:
                    case R.id.tile_3:
                    case R.id.tile_4:
                        Check_Tiles_Move(tile_1, tile_2, _1, _2);
                        break;

                    case R.id.tile_5:
                    case R.id.tile_9:
                    case R.id.tile_13:
                        Check_Tiles_Move(tile_1, tile_5, _1, _5);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 2:
                switch (v.getId()){
                    case R.id.tile_1:
                        Check_Tiles_Move(tile_2, tile_1, _2, _1);
                        break;

                    case R.id.tile_3:
                    case R.id.tile_4:
                        Check_Tiles_Move(tile_2, tile_3, _2, _3);
                        break;

                    case R.id.tile_6:
                    case R.id.tile_14:
                    case R.id.tile_10:
                        Check_Tiles_Move(tile_2, tile_6, _2, _6);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 3:
                switch (v.getId()){
                    case R.id.tile_1:

                    case R.id.tile_2:
                        Check_Tiles_Move(tile_3, tile_2, _3, _2);
                        break;

                    case R.id.tile_4:
                        Check_Tiles_Move(tile_3, tile_4, _3, _4);
                        break;

                    case R.id.tile_7:
                    case R.id.tile_11:
                    case R.id.tile_15:
                        Check_Tiles_Move(tile_3, tile_7, _3, _7);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 4:
                switch (v.getId()){
                    case R.id.tile_1:
                    case R.id.tile_2:
                    case R.id.tile_3:
                        Check_Tiles_Move(tile_4, tile_3, _4, _3);
                        break;

                    case R.id.tile_8:
                    case R.id.tile_12:
                    case R.id.tile_16:
                        Check_Tiles_Move(tile_4, tile_8, _4, _8);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 5:
                switch (v.getId()){
                    case R.id.tile_1:
                        Check_Tiles_Move(tile_5, tile_1, _5, _1);
                        break;

                    case R.id.tile_6:
                    case R.id.tile_7:
                    case R.id.tile_8:
                        Check_Tiles_Move(tile_5, tile_6, _5, _6);
                        break;

                    case R.id.tile_9:
                    case R.id.tile_13:
                        Check_Tiles_Move(tile_5, tile_9, _5, _9);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 6:
                switch (v.getId()){
                    case R.id.tile_2:
                        Check_Tiles_Move(tile_6, tile_2, _6, _2);
                        break;

                    case R.id.tile_5:
                        Check_Tiles_Move(tile_6, tile_5, _6, _5);
                        break;

                    case R.id.tile_7:
                    case R.id.tile_8:
                        Check_Tiles_Move(tile_6, tile_7, _6, _7);
                        break;

                    case R.id.tile_10:
                    case R.id.tile_14:
                        Check_Tiles_Move(tile_6, tile_10, _6, _10);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 7:
                switch (v.getId()){
                    case R.id.tile_3:
                        Check_Tiles_Move(tile_7, tile_3, _7, _3);
                        break;

                    case R.id.tile_5:
                    case R.id.tile_6:
                        Check_Tiles_Move(tile_7, tile_6, _7, _6);
                        break;

                    case R.id.tile_8:
                        Check_Tiles_Move(tile_7, tile_8, _7, _8);
                        break;

                    case R.id.tile_11:
                    case R.id.tile_15:
                        Check_Tiles_Move(tile_7, tile_11, _7, _11);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 8:
                switch (v.getId()){
                    case R.id.tile_4:
                        Check_Tiles_Move(tile_8, tile_4, _8, _4);
                        break;

                    case R.id.tile_5:
                    case R.id.tile_7:
                    case R.id.tile_6:
                        Check_Tiles_Move(tile_8, tile_7, _8, _7);
                        break;

                    case R.id.tile_12:
                    case R.id.tile_16:
                        Check_Tiles_Move(tile_8, tile_12, _8, _12);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 9:
                switch (v.getId()){
                    case R.id.tile_1:
                    case R.id.tile_5:
                        Check_Tiles_Move(tile_9, tile_5, _9, _5);
                        break;

                    case R.id.tile_10:
                    case R.id.tile_12:
                    case R.id.tile_11:
                        Check_Tiles_Move(tile_9, tile_10, _9, _10);
                        break;

                    case R.id.tile_13:
                        Check_Tiles_Move(tile_9, tile_13, _9, _13);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 10:
                switch (v.getId()){
                    case R.id.tile_2:
                    case R.id.tile_6:
                        Check_Tiles_Move(tile_10, tile_6, _10, _6);
                        break;

                    case R.id.tile_9:
                        Check_Tiles_Move(tile_10, tile_9, _10, _9);
                        break;

                    case R.id.tile_11:
                    case R.id.tile_12:
                        Check_Tiles_Move(tile_10, tile_11, _10, _11);
                        break;

                    case R.id.tile_14:
                        Check_Tiles_Move(tile_10, tile_14, _10, _14);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 11:
                switch (v.getId()){
                    case R.id.tile_3:
                    case R.id.tile_7:
                        Check_Tiles_Move(tile_11, tile_7, _11, _7);
                        break;

                    case R.id.tile_9:
                    case R.id.tile_10:
                        Check_Tiles_Move(tile_11, tile_10, _11, _10);
                        break;

                    case R.id.tile_12:
                        Check_Tiles_Move(tile_11, tile_12, _11, _12);
                        break;

                    case R.id.tile_15:
                        Check_Tiles_Move(tile_11, tile_15, _11, _15);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 12:
                switch (v.getId()){
                    case R.id.tile_4:
                    case R.id.tile_8:
                        Check_Tiles_Move(tile_12, tile_8, _12, _8);
                        break;

                    case R.id.tile_9:
                    case R.id.tile_11:
                    case R.id.tile_10:
                        Check_Tiles_Move(tile_12, tile_11, _12, _11);
                        break;

                    case R.id.tile_16:
                        Check_Tiles_Move(tile_12, tile_16, _12, _16);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 13:
                switch (v.getId()){
                    case R.id.tile_1:
                    case R.id.tile_9:
                    case R.id.tile_5:
                        Check_Tiles_Move(tile_13, tile_9, _13, _9);
                        break;

                    case R.id.tile_14:
                    case R.id.tile_16:
                    case R.id.tile_15:
                        Check_Tiles_Move(tile_13, tile_14, _13, _14);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 14:
                switch (v.getId()){
                    case R.id.tile_2:
                    case R.id.tile_10:
                    case R.id.tile_6:
                        Check_Tiles_Move(tile_14, tile_10, _14, _10);
                        break;

                    case R.id.tile_13:
                        Check_Tiles_Move(tile_14, tile_13, _14, _13);
                        break;

                    case R.id.tile_15:
                    case R.id.tile_16:
                        Check_Tiles_Move(tile_14, tile_15, _14, _15);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 15:
                switch (v.getId()){
                    case R.id.tile_7:
                    case R.id.tile_11:
                    case R.id.tile_3:
                        Check_Tiles_Move(tile_15, tile_11, _15, _11);
                        break;

                    case R.id.tile_13:
                    case R.id.tile_14:
                        Check_Tiles_Move(tile_15, tile_14, _15, _14);
                        break;

                    case R.id.tile_16:
                        Check_Tiles_Move(tile_15, tile_16, _15, _16);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;

            case 16:
                switch (v.getId()){
                    case R.id.tile_4:
                    case R.id.tile_12:
                    case R.id.tile_8:
                        Check_Tiles_Move(tile_16, tile_12, _16, _12);
                        break;

                    case R.id.tile_13:
                    case R.id.tile_15:
                    case R.id.tile_14:
                        Check_Tiles_Move(tile_16, tile_15, _16, _15);
                        break;

                    default:
                        Common.Custom_Toast(getApplicationContext(), "Wrong Move");
                }
                break;
        }
    }

    private void Check_Tiles_Move(TextView first_Tile, TextView second_Tile, String first_Value, String second_Value){
        if(first_Tile.getAlpha()==1 && second_Tile.getAlpha()==0){
            Set_Visibility(second_Tile.getTag().toString());
            second_Tile.setText(first_Value);
            Set_Tiles_Style(second_Tile);
            Common.Sound(getApplicationContext(), "Move");
            if(Common.Get_Challenge_Mode(getApplicationContext())){
                Challenge_Move_Count(second_Value);
            }
            Common.Vibration(getApplicationContext());
            Set_Invisibility(first_Tile.getTag().toString());
            Generate_Random_Tile_Value();
            Common.Set_Is_Game_Resumed(getApplicationContext(),true);
            Resume_Game();
        }
        else if(first_Tile.getAlpha()==1 && second_Tile.getAlpha()==1){
            if(first_Value.equals(second_Value)){
                String new_Value = String.valueOf(Integer.parseInt(first_Value) + Integer.parseInt(second_Value));
                second_Tile.setText(new_Value);
                Set_Invisibility(Common.Get_Tile_Tag(getApplicationContext()));
                Set_Tiles_Style(second_Tile);
                Common.Sound(getApplicationContext(), "Move");
                Common.Vibration(getApplicationContext());
                if(!Common.Get_Challenge_Mode(getApplicationContext())){
                    Count_Score(Long.parseLong(new_Value));
                }
                else {
                    Challenge_Move_Count(new_Value);
                }
                Generate_Random_Tile_Value();
                Common.Set_Is_Game_Resumed(getApplicationContext(),true);
                Resume_Game();
            }
            else {
                Common.Custom_Toast(getApplicationContext(), "Wrong Move");
            }
        }
    }

    private void Set_Visibility(String Tag){

        switch (Tag){
            case "1":
                tile_1.setAlpha(1);
                break;

            case "2":
                tile_2.setAlpha(1);
                break;

            case "3":
                tile_3.setAlpha(1);
                break;

            case "4":
                tile_4.setAlpha(1);
                break;

            case "5":
                tile_5.setAlpha(1);
                break;

            case "6":
                tile_6.setAlpha(1);
                break;

            case "7":
                tile_7.setAlpha(1);
                break;

            case "8":
                tile_8.setAlpha(1);
                break;

            case "9":
                tile_9.setAlpha(1);
                break;

            case "10":
                tile_10.setAlpha(1);
                break;

            case "11":
                tile_11.setAlpha(1);
                break;

            case "12":
                tile_12.setAlpha(1);
                break;

            case "13":
                tile_13.setAlpha(1);
                break;

            case "14":
                tile_14.setAlpha(1);
                break;

            case "15":
                tile_15.setAlpha(1);
                break;

            case "16":
                tile_16.setAlpha(1);
                break;
        }
    }

    private void Set_Invisibility(String Tag){

        switch (Tag){
            case "1":
                tile_1.setAlpha(0);
                break;

            case "2":
                tile_2.setAlpha(0);
                break;

            case "3":
                tile_3.setAlpha(0);
                break;

            case "4":
                tile_4.setAlpha(0);
                break;

            case "5":
                tile_5.setAlpha(0);
                break;

            case "6":
                tile_6.setAlpha(0);
                break;

            case "7":
                tile_7.setAlpha(0);
                break;

            case "8":
                tile_8.setAlpha(0);
                break;

            case "9":
                tile_9.setAlpha(0);
                break;

            case "10":
                tile_10.setAlpha(0);
                break;

            case "11":
                tile_11.setAlpha(0);
                break;

            case "12":
                tile_12.setAlpha(0);
                break;

            case "13":
                tile_13.setAlpha(0);
                break;

            case "14":
                tile_14.setAlpha(0);
                break;

            case "15":
                tile_15.setAlpha(0);
                break;

            case "16":
                tile_16.setAlpha(0);
                break;
        }
    }

    private void Generate_Random_Tile_Value(){

        if(!(tile_1.getAlpha()==1 && tile_2.getAlpha()==1 && tile_3.getAlpha()==1 && tile_4.getAlpha()==1 &&
                tile_5.getAlpha()==1 && tile_6.getAlpha()==1 && tile_7.getAlpha()==1 && tile_8.getAlpha()==1 &&
                tile_9.getAlpha()==1 && tile_10.getAlpha()==1 && tile_11.getAlpha()==1 && tile_12.getAlpha()==1 &&
                tile_13.getAlpha()==1 && tile_14.getAlpha()==1 && tile_15.getAlpha()==1 && tile_16.getAlpha()==1)){

            switch (new Random().nextInt(16)+1){
                case 1:
                    Set_Random_Position_Value(tile_1);
                    break;

                case 2:
                    Set_Random_Position_Value(tile_2);
                    break;

                case 3:
                    Set_Random_Position_Value(tile_3);
                    break;

                case 4:
                    Set_Random_Position_Value(tile_4);
                    break;

                case 5:
                    Set_Random_Position_Value(tile_5);
                    break;

                case 6:
                    Set_Random_Position_Value(tile_6);
                    break;

                case 7:
                    Set_Random_Position_Value(tile_7);
                    break;

                case 8:
                    Set_Random_Position_Value(tile_8);
                    break;

                case 9:
                    Set_Random_Position_Value(tile_9);
                    break;

                case 10:
                    Set_Random_Position_Value(tile_10);
                    break;

                case 11:
                    Set_Random_Position_Value(tile_11);
                    break;

                case 12:
                    Set_Random_Position_Value(tile_12);
                    break;

                case 13:
                    Set_Random_Position_Value(tile_13);
                    break;

                case 14:
                    Set_Random_Position_Value(tile_14);
                    break;

                case 15:
                    Set_Random_Position_Value(tile_15);
                    break;

                case 16:
                    Set_Random_Position_Value(tile_16);
                    break;

                default:
                    Generate_Random_Tile_Value();
            }
        }
    }

    private void Set_Random_Position_Value(TextView tile) {
        if(tile.getAlpha()==0){
            tile.setEnabled(true);
            Set_Tiles_Style(tile);
            tile.setAlpha(1);
            tile.setText("2");
        }
        else {
            Generate_Random_Tile_Value();
        }
    }

    private void Set_Tiles_Style(final View view) {
        ((TextView) view).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                switch (s.toString()) {
                    case "2":
                        ((TextView) view).setTextSize(30);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_2));
                        break;

                    case "4":
                        ((TextView) view).setTextSize(30);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_4));
                        break;

                    case "8":
                        ((TextView) view).setTextSize(30);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_8));
                        break;

                    case "16":
                        ((TextView) view).setTextSize(30);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_16));
                        break;

                    case "32":
                        ((TextView) view).setTextSize(30);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_32));
                        break;

                    case "64":
                        ((TextView) view).setTextSize(30);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_64));
                        break;

                    case "128":
                        ((TextView) view).setTextSize(27);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_128));
                        break;

                    case "256":
                        ((TextView) view).setTextSize(27);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_256));
                        break;

                    case "512":
                        ((TextView) view).setTextSize(27);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_512));
                        break;

                    case "1024":
                        ((TextView) view).setTextSize(22);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_1024));
                        break;

                    case "2048":
                        ((TextView) view).setTextSize(22);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_2048));
                        break;

                    case "4096":
                    case "8192":
                        ((TextView) view).setTextSize(22);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_infinite));
                        break;

                    default:
                        ((TextView) view).setTextSize(17);
                        view.setBackground(getResources().getDrawable(R.drawable.shape_infinite));
                        break;
                }

            }
        });
    }

    private void Count_Score(long Value) {
        long Score = Long.parseLong(Current_Score.getText().toString());
        long Total_Score = (Score + Value);
        long Highest_Score = Long.parseLong(Best_Score.getText().toString());

        Current_Score.setText(Total_Score + "");

        if(Total_Score > Highest_Score ){
            Best_Score.setText(Total_Score + "");
            Common.Set_Game_Score(getApplicationContext(), Common.Best_Score_Key, Total_Score);
        }
    }

    private boolean Is_Game_Over() {
        String _1 = tile_1.getText().toString();
        String _2 = tile_2.getText().toString();
        String _3 = tile_3.getText().toString();
        String _4 = tile_4.getText().toString();
        String _5 = tile_5.getText().toString();
        String _6 = tile_6.getText().toString();
        String _7 = tile_7.getText().toString();
        String _8 = tile_8.getText().toString();
        String _9 = tile_9.getText().toString();
        String _10 = tile_10.getText().toString();
        String _11 = tile_11.getText().toString();
        String _12 = tile_12.getText().toString();
        String _13 = tile_13.getText().toString();
        String _14 = tile_14.getText().toString();
        String _15 = tile_15.getText().toString();
        String _16 = tile_16.getText().toString();

        return !_1.equals(_2) && !_1.equals(_5) && !_2.equals(_1) && !_2.equals(_3) && !_2.equals(_6) && !_3.equals(_2) &&
                !_3.equals(_4) && !_3.equals(_7) && !_4.equals(_3) && !_4.equals(_8) && !_5.equals(_1) && !_5.equals(_6) &&
                !_5.equals(_9) && !_6.equals(_2) && !_6.equals(_5) && !_6.equals(_7) && !_6.equals(_10) && !_7.equals(_3) &&
                !_7.equals(_6) && !_7.equals(_8) && !_7.equals(_11) && !_8.equals(_4) && !_8.equals(_7) && !_8.equals(_12) &&
                !_9.equals(_5) && !_9.equals(_10) && !_9.equals(_13) && !_10.equals(_6) && !_10.equals(_9) && !_10.equals(_11) &&
                !_10.equals(_14) && !_11.equals(_7) && !_11.equals(_10) && !_11.equals(_12) && !_11.equals(_15) && !_12.equals(_8) &&
                !_12.equals(_11) && !_12.equals(_16) && !_13.equals(_9) && !_13.equals(_14) && !_14.equals(_10) && !_14.equals(_13) &&
                !_14.equals(_15) && !_15.equals(_11) && !_15.equals(_14) && !_15.equals(_16) && !_16.equals(_12) && !_16.equals(_15);
    }

    private void Enable_Game() {

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (TextView tile: tiles){
            tile.setEnabled(true);
        }
    }

    private void Disable_Game() {

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (TextView tile: tiles){
            tile.setEnabled(false);
        }
    }

    public void Open_Menu(View view) {
        Common.Bounce_Animation(getApplicationContext(), findViewById(R.id.menu_icon), 0.15, 5);
        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
        Menu_Dialog();
    }

    private void Menu_Dialog(){
        View view = View.inflate(getApplicationContext(),R.layout.menu_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        final AlertDialog menu_Dialog = alertDialogBuilder.create();
        menu_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menu_Dialog.show();

        final TextView menu_Title = view.findViewById(R.id.menu_title);
        final ImageButton Back = view.findViewById(R.id.back_btn);
        final Button keep_Going = view.findViewById(R.id.keep_going);
        final Button new_Game = view.findViewById(R.id.new_game);
        final Button try_Again = view.findViewById(R.id.try_again);
        final Button challenges = view.findViewById(R.id.challenges);
        final Button exit_Challenges = view.findViewById(R.id.exit_challenges);
        final Button how_To_Play = view.findViewById(R.id.how_to_play);
        final Button quit_Game = view.findViewById(R.id.quit_game);
        final LinearLayout sound_btn = view.findViewById(R.id.sound_btn);
        final ImageButton sound_icon = view.findViewById(R.id.sound_icon);
        final TextView sound_tv = view.findViewById(R.id.sound_tv);
        final LinearLayout vibration_btn = view.findViewById(R.id.vibrate_btn);
        final ImageButton vibration_icon = view.findViewById(R.id.vibrate_icon);
        final TextView vibration_tv = view.findViewById(R.id.vibrate_tv);
        final LinearLayout shareApp_btn = view.findViewById(R.id.share_btn);
        final TextView shareApp_tv = view.findViewById(R.id.share_tv);
        final ImageButton shareApp_icon = view.findViewById(R.id.share_icon);

        Common.Set_Font(getApplicationContext(), new View[]{menu_Title, keep_Going, new_Game, try_Again, challenges,
                                    exit_Challenges, how_To_Play, quit_Game, sound_tv, vibration_tv, shareApp_tv});


        if(Common.Get_Sound_Mode(getApplicationContext())){
            sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_on));
            sound_tv.setText("Sound On");
        }
        else {
            sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_off));
            sound_tv.setText("Sound Off");
        }

        if(Common.Get_Vibration_Mode(getApplicationContext())){
            vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_on));
            vibration_tv.setText("Vibration On");
        }
        else {
            vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_off));
            vibration_tv.setText("Vibration Off");
        }

        Common.Jelly_Animation(getApplicationContext(), keep_Going, 1000);
        keep_Going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                if (Common.Get_Is_Game_Resumed(getApplicationContext())) {
                    Reload_Game();
                }
                menu_Dialog.dismiss();
            }
        });

        Common.Jelly_Animation(getApplicationContext(), challenges, 1025);
        challenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                Challenges_Dialog(menu_Dialog);
            }
        });

        if(!Common.Get_Challenge_Mode(getApplicationContext())){
            new_Game.setVisibility(View.VISIBLE);
            Common.Jelly_Animation(getApplicationContext(), new_Game,1050);
            new_Game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                    Common.Sound(getApplicationContext(), "Click");
                    Common.Vibration(getApplicationContext());
                    Restart_Game();
                    menu_Dialog.dismiss();
                }
            });
        }
        else {
            try_Again.setVisibility(View.VISIBLE);
            Common.Jelly_Animation(getApplicationContext(), try_Again, 1050);
            try_Again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                    Common.Sound(getApplicationContext(), "Click");
                    Common.Vibration(getApplicationContext());
                    Challenges(Common.Get_Challenge_Id(getApplicationContext()));
                    Restart_Game();
                    menu_Dialog.dismiss();
                }
            });

            exit_Challenges.setVisibility(View.VISIBLE);
            Common.Jelly_Animation(getApplicationContext(),exit_Challenges, 1075);
            exit_Challenges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                    Common.Sound(getApplicationContext(), "Click");
                    Common.Vibration(getApplicationContext());
                    Common.Set_Challenge_Mode(getApplicationContext(),false);
                    Restart_Game();
                    menu_Dialog.dismiss();
                }
            });
        }

        Common.Jelly_Animation(getApplicationContext(), how_To_Play, 1100);
        how_To_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                How_To_Play_Dialog();
            }
        });

        Common.Jelly_Animation(getApplicationContext(), quit_Game, 1125);
        quit_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                Quit_Dialog();
            }
        });

        Common.Jelly_Animation(getApplicationContext(), sound_icon, 975);
        sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), sound_icon, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                if(sound_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_sound_on).getConstantState()){
                    sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_off));
                    sound_tv.setText("Sound Off");
                    Common.Set_Sound_Mode(getApplicationContext(),false);
                }
                else {
                    sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_on));
                    sound_tv.setText("Sound On");
                    Common.Set_Sound_Mode(getApplicationContext(),true);
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), vibration_icon, 1000);
        vibration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), vibration_icon, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                if(vibration_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_vibration_on).getConstantState()){
                    vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_off));
                    vibration_tv.setText("Vibration Off");
                    Common.Set_Vibration_Mode(getApplicationContext(),false);
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibe != null) vibe.cancel();
                }
                else {
                    vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_on));
                    vibration_tv.setText("Vibration On");
                    Common.Set_Vibration_Mode(getApplicationContext(),true);
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibe != null) vibe.vibrate(35);
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), shareApp_icon, 1025);
        shareApp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), shareApp_icon, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                if(isPermissionGranted(333)){
                    Share_Apk();
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), Back, 1000);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                if (Common.Get_Is_Game_Resumed(getApplicationContext())) {
                    Reload_Game();
                }
                menu_Dialog.dismiss();
            }
        });
    }

    private void Challenges_Dialog(AlertDialog menu_Dialog){
        View view = View.inflate(getApplicationContext(),R.layout.challenges_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog challenge_Dialog = alertDialogBuilder.create();
        challenge_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        challenge_Dialog.show();

        TextView challenges_title = view.findViewById(R.id.challenge_title);
        ImageButton Back = view.findViewById(R.id.back_btn);

        int[] Board_Ids = {R.id.board_layout_one, R.id.board_layout_two, R.id.board_layout_three, R.id.board_layout_four};

        for(int id : Board_Ids){
            LinearLayout board = view.findViewById(id);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) board.getLayoutParams();

            int size = displayMetrics.widthPixels;

            if(size <= 500){
                params.height = size - (size/12);
                params.width = size - (size/12);
            }
            else{
                params.height = size - (size/10);
                params.width = size - (size/10);
            }

            board.setLayoutParams(params);
        }

        int[] Lock_Id = new int[]{
                R.id.lock_1, R.id.lock_2, R.id.lock_3, R.id.lock_4, R.id.lock_5, R.id.lock_6, R.id.lock_7, R.id.lock_8,
                R.id.lock_9, R.id.lock_10, R.id.lock_11, R.id.lock_12, R.id.lock_13, R.id.lock_14, R.id.lock_15, R.id.lock_16,
                R.id.lock_17, R.id.lock_18, R.id.lock_19, R.id.lock_20, R.id.lock_21, R.id.lock_22, R.id.lock_23, R.id.lock_24,
                R.id.lock_25, R.id.lock_26, R.id.lock_27, R.id.lock_28, R.id.lock_29, R.id.lock_30, R.id.lock_31, R.id.lock_32,
                R.id.lock_33, R.id.lock_34, R.id.lock_35, R.id.lock_36, R.id.lock_37, R.id.lock_38, R.id.lock_39, R.id.lock_40,
                R.id.lock_41, R.id.lock_42, R.id.lock_43, R.id.lock_44, R.id.lock_45, R.id.lock_46, R.id.lock_47, R.id.lock_48,
                R.id.lock_49, R.id.lock_50, R.id.lock_51, R.id.lock_52, R.id.lock_53, R.id.lock_54, R.id.lock_55, R.id.lock_56,
                R.id.lock_57, R.id.lock_58, R.id.lock_59, R.id.lock_60, R.id.lock_61, R.id.lock_62, R.id.lock_63, R.id.lock_64
        };

        for(int id = 0; id<Common.Get_Unlock_Challenge(getApplicationContext()); id++){
            ImageButton lock = view.findViewById(Lock_Id[id]);
            lock.setVisibility(View.GONE);
        }

        int Tag = 0;
        int[] Challenges_id = new int[]{
                R.id.challenge_1, R.id.challenge_2, R.id.challenge_3, R.id.challenge_4, R.id.challenge_5, R.id.challenge_6, R.id.challenge_7, R.id.challenge_8,
                R.id.challenge_9, R.id.challenge_10, R.id.challenge_11, R.id.challenge_12, R.id.challenge_13, R.id.challenge_14, R.id.challenge_15, R.id.challenge_16,
                R.id.challenge_17, R.id.challenge_18, R.id.challenge_19, R.id.challenge_20, R.id.challenge_21, R.id.challenge_22, R.id.challenge_23, R.id.challenge_24,
                R.id.challenge_25, R.id.challenge_26, R.id.challenge_27, R.id.challenge_28, R.id.challenge_29, R.id.challenge_30, R.id.challenge_31, R.id.challenge_32,
                R.id.challenge_33, R.id.challenge_34, R.id.challenge_35, R.id.challenge_36, R.id.challenge_37, R.id.challenge_38, R.id.challenge_39, R.id.challenge_40,
                R.id.challenge_41, R.id.challenge_42, R.id.challenge_43, R.id.challenge_44, R.id.challenge_45, R.id.challenge_46, R.id.challenge_47, R.id.challenge_48,
                R.id.challenge_49, R.id.challenge_50, R.id.challenge_51, R.id.challenge_52, R.id.challenge_53, R.id.challenge_54, R.id.challenge_55, R.id.challenge_56,
                R.id.challenge_57, R.id.challenge_58, R.id.challenge_59, R.id.challenge_60, R.id.challenge_61, R.id.challenge_62, R.id.challenge_63, R.id.challenge_64
        };

        for(int id : Challenges_id){
            TextView tv = view.findViewById(id);
            tv.setTypeface(Typeface.createFromAsset(getAssets(), "vinque.ttf"));
            tv.setOnClickListener(new GoForChallenge(menu_Dialog, challenge_Dialog, Tag+1));
            Tag++;
        }

        challenges_title.setTypeface(Typeface.createFromAsset(getAssets(), "vinque.ttf"));

        Common.Jelly_Animation(getApplicationContext(), Back, 1000);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                challenge_Dialog.dismiss();
            }
        });
    }

    private class GoForChallenge implements View.OnClickListener {
        AlertDialog menu_Dialog, challenge_Dialog;
        int id;

        private GoForChallenge(AlertDialog menu_Dialog, AlertDialog challenge_Dialog, int id) {
            this.menu_Dialog = menu_Dialog;
            this.challenge_Dialog = challenge_Dialog;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Common.Set_Challenge_Mode(getApplicationContext(),true);
            Common.Sound(getApplicationContext(), "Click");
            Common.Vibration(getApplicationContext());
            Restart_Game();
            Common.Set_Challenge_Id(getApplicationContext(), id);
            Challenges(id);
            for (TextView view: new TextView[]{challenge_One_Tv, challenge_Two_Tv, challenge_Three_Tv}){
                Set_Challenge_Style(view);
            }
            Common.Set_Is_Game_Resumed(getApplicationContext(),true);
            Resume_Game();
            menu_Dialog.dismiss();
            challenge_Dialog.dismiss();
        }
    }

    private void Set_Challenge_Style(final View view) {
        ((TextView) view).addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {

                switch (s.toString()) {
                    case "4":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_4));
                        break;

                    case "8":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_8));
                        break;

                    case "16":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_16));
                        break;

                    case "32":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_32));
                        break;

                    case "64":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_64));
                        break;

                    case "128":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_128));
                        break;

                    case "256":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_256));
                        break;

                    case "512":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_512));
                        break;

                    case "1024":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_1024));
                        break;

                    case "2048":
                        view.setBackground(getResources().getDrawable(R.drawable.shape_2048));
                        break;

                    default:
                        view.setBackground(getResources().getDrawable(R.drawable.shape_infinite));
                        break;
                }

            }
        });
    }

    private void Challenges(int id){
        switch(id){
            case 1:
                Challenge_Setup(10,4,4,8,1,16,1);
                break;

            case 2:
                Challenge_Setup(12,4,5,8,3,16,1);
                break;

            case 3:
                Challenge_Setup(15,4,7,8,3,16,2);
                break;

            case 4:
                Challenge_Setup(36,16,2,32,2,64,1);
                break;

            case 5:
                Challenge_Setup(60,16,8,32,3,64,1);
                break;

            case 6:
                Challenge_Setup(85,16,10,32,5,64,2);
                break;

            case 7:
                Challenge_Setup(105,32,7,64,3,128,1);
                break;

            case 8:
                Challenge_Setup(125,32,8,64,4,128,2);
                break;

            case 9:
                Challenge_Setup(145,64,5,128,2,256,1);
                break;

            case 10:
                Challenge_Setup(160,64,7,128,3,256,1);
                break;

            case 11:
                Challenge_Setup(185,64,9,128,4,256,2);
                break;

            case 12:
                Challenge_Setup(205,64,11,128,5,256,2);
                break;

            case 13:
                Challenge_Setup(225,128,5,256,2,512,1);
                break;

            case 14:
                Challenge_Setup(240,128,7,256,3,512,1);
                break;

            case 15:
                Challenge_Setup(255,128,9,256,4,512,2);
                break;

            case 16:
                Challenge_Setup(280,256,5,512,2,1024,1);
                break;

            case 17:
                Challenge_Setup(10,4,6,8,3,16,1);
                break;

            case 18:
                Challenge_Setup(12,4,4,8,4,16,1);
                break;

            case 19:
                Challenge_Setup(18,4,7,8,5,16,2);
                break;

            case 20:
                Challenge_Setup(36,16,5,32,2,64,1);
                break;

            case 21:
                Challenge_Setup(60,16,9,32,4,64,1);
                break;

            case 22:
                Challenge_Setup(92,16,11,32,5,64,2);
                break;

            case 23:
                Challenge_Setup(102,32,8,64,3,128,1);
                break;

            case 24:
                Challenge_Setup(125,32,8,64,4,128,2);
                break;

            case 25:
                Challenge_Setup(132,64,4,128,2,256,1);
                break;

            case 26:
                Challenge_Setup(150,64,6,128,2,256,1);
                break;

            case 27:
                Challenge_Setup(175,64,8,128,4,256,2);
                break;

            case 28:
                Challenge_Setup(202,64,14,128,5,256,2);
                break;

            case 29:
                Challenge_Setup(227,128,6,256,2,512,1);
                break;

            case 30:
                Challenge_Setup(240,128,10,256,2,512,1);
                break;

            case 31:
                Challenge_Setup(255,128,8,256,5,512,2);
                break;

            case 32:
                Challenge_Setup(272,256,4,512,3,1024,1);
                break;

            case 33:
                Challenge_Setup(8,4,3,8,1,16,1);
                break;

            case 34:
                Challenge_Setup(14,4,6,8,3,16,2);
                break;

            case 35:
                Challenge_Setup(18,4,8,8,5,16,2);
                break;

            case 36:
                Challenge_Setup(27,16,3,32,1,64,1);
                break;

            case 37:
                Challenge_Setup(46,16,5,32,2,64,1);
                break;

            case 38:
                Challenge_Setup(72,16,9,32,4,64,2);
                break;

            case 39:
                Challenge_Setup(85,32,4,64,3,128,1);
                break;

            case 40:
                Challenge_Setup(95,32,7,64,4,128,2);
                break;

            case 41:
                Challenge_Setup(115,64,5,128,3,256,1);
                break;

            case 42:
                Challenge_Setup(127,64,7,128,3,256,2);
                break;

            case 43:
                Challenge_Setup(135,64,8,128,4,256,2);
                break;

            case 44:
                Challenge_Setup(158,64,12,128,4,256,2);
                break;

            case 45:
                Challenge_Setup(175,128,4,256,2,512,1);
                break;

            case 46:
                Challenge_Setup(187,128,7,256,2,512,1);
                break;

            case 47:
                Challenge_Setup(205,128,8,256,3,512,2);
                break;

            case 48:
                Challenge_Setup(227,256,4,512,2,1024,1);
                break;

            case 49:
                Challenge_Setup(20,4,8,8,6,16,2);
                break;

            case 50:
                Challenge_Setup(30,8,9,16,4,32,2);
                break;

            case 51:
                Challenge_Setup(45,8,12,16,7,32,3);
                break;

            case 52:
                Challenge_Setup(38,16,4,32,3,64,1);
                break;

            case 53:
                Challenge_Setup(16,8,2,16,1,32,1);
                break;

            case 54:
                Challenge_Setup(60,16,8,32,5,64,2);
                break;

            case 55:
                Challenge_Setup(120,32,12,64,4,128,3);
                break;

            case 56:
                Challenge_Setup(90,64,4,128,2,256,1);
                break;

            case 57:
                Challenge_Setup(128,64,5,128,4,256,1);
                break;

            case 58:
                Challenge_Setup(150,64,8,128,2,256,2);
                break;

            case 59:
                Challenge_Setup(172,128,4,256,2,512,1);
                break;

            case 60:
                Challenge_Setup(205,128,7,256,3,512,2);
                break;

            case 61:
                Challenge_Setup(227,256,3,512,2,1024,1);
                break;

            case 62:
                Challenge_Setup(245,128,7,512,3,1024,1);
                break;

            case 63:
                Challenge_Setup(270,512,3,1024,1,2048,1);
                break;

            case 64:
                Challenge_Setup(285,512,5,1024,2,2048,1);
                break;

            default:
                Common.Set_Challenge_Id(getApplicationContext(), 1);
                Challenge_Setup(10,4,4,8,1,16,1);
        }
    }

    private void Challenge_Setup(int moves, int ch_1, int ch_1_target, int ch_2, int ch_2_target, int ch_3, int ch_3_target) {
        Moves_Count.setText(String.valueOf(moves));
        challenge_One_Tv.setText(String.valueOf(ch_1));
        challenge_One_Count.setText(String.valueOf(0));
        challenge_One_Target.setText(String.valueOf(ch_1_target));
        challenge_Two_Tv.setText(String.valueOf(ch_2));
        challenge_Two_Count.setText(String.valueOf(0));
        challenge_Two_Target.setText(String.valueOf(ch_2_target));
        challenge_Three_Tv.setText(String.valueOf(ch_3));
        challenge_Three_Count.setText(String.valueOf(0));
        challenge_Three_Target.setText(String.valueOf(ch_3_target));
    }

    private void Challenge_Move_Count(String value) {
        int moves = Integer.parseInt(Moves_Count.getText().toString());

        String ch_1 = challenge_One_Tv.getText().toString();
        int ch_1_count = Integer.parseInt(challenge_One_Count.getText().toString());
        int ch_1_target = Integer.parseInt(challenge_One_Target.getText().toString());

        String ch_2 = challenge_Two_Tv.getText().toString();
        int ch_2_count = Integer.parseInt(challenge_Two_Count.getText().toString());
        int ch_2_target = Integer.parseInt(challenge_Two_Target.getText().toString());

        String ch_3 = challenge_Three_Tv.getText().toString();
        int ch_3_count = Integer.parseInt(challenge_Three_Count.getText().toString());
        int ch_3_target = Integer.parseInt(challenge_Three_Target.getText().toString());

        Drawable background = getResources().getDrawable(R.drawable.shape_unlock);
        Moves_Count.setText(String.valueOf(moves-1));

        if(ch_1.equals(value) && (ch_1_count < ch_1_target)){
            challenge_One_Count.setText(String.valueOf(ch_1_count+1));
            if((Integer.parseInt(challenge_One_Count.getText().toString()) == Integer.parseInt(challenge_One_Target.getText().toString()))){
                challenge_One_Layout.setBackground(background);
            }
        }
        else if(ch_2.equals(value) && (ch_2_count < ch_2_target)){
            challenge_Two_Count.setText(String.valueOf(ch_2_count+1));
            if((Integer.parseInt(challenge_Two_Count.getText().toString()) == Integer.parseInt(challenge_Two_Target.getText().toString()))){
                challenge_Two_Layout.setBackground(background);
            }
        }
        else if(ch_3.equals(value) && (ch_3_count < ch_3_target)){
            challenge_Three_Count.setText(String.valueOf(ch_3_count+1));
            if((Integer.parseInt(challenge_Three_Count.getText().toString()) == Integer.parseInt(challenge_Three_Target.getText().toString()))){
                challenge_Three_Layout.setBackground(background);
            }
        }

        if(Is_Challenge_Win()){
            Disable_Game();
            int Challenge = Common.Get_Challenge_Id(getApplicationContext());
            if((Common.Get_Unlock_Challenge(getApplicationContext()) <= Challenge) && (Challenge < 64)){
                Common.Set_Unlock_Challenge(getApplicationContext(),(Challenge+1));
            }
            Challenge_Win_Dialog();
        }

        if(Is_Challenge_Lose()){
            Disable_Game();
            Game_Over_Dialog();
        }
    }

    private boolean Is_Challenge_Win() {
        int moves = Integer.parseInt(Moves_Count.getText().toString());
        int ch_1_count = Integer.parseInt(challenge_One_Count.getText().toString());
        int ch_1_target = Integer.parseInt(challenge_One_Target.getText().toString());

        int ch_2_count = Integer.parseInt(challenge_Two_Count.getText().toString());
        int ch_2_target = Integer.parseInt(challenge_Two_Target.getText().toString());

        int ch_3_count = Integer.parseInt(challenge_Three_Count.getText().toString());
        int ch_3_target = Integer.parseInt(challenge_Three_Target.getText().toString());

        return moves >= 0 && (ch_1_count == ch_1_target) && (ch_2_count == ch_2_target) && (ch_3_count == ch_3_target);
    }

    private void Challenge_Win_Dialog(){

        final View view = View.inflate(getApplicationContext(),R.layout.win_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        Common.Sound(getApplicationContext(), "Win");

        final TextView you_Win = view.findViewById(R.id.you_win);
        final LinearLayout challenge_Layout = view.findViewById(R.id.challenge_no_layout);
        final TextView mode = view.findViewById(R.id.mode_tv);
        final TextView challenge = view.findViewById(R.id.challenge_no);
        final Button Share = view.findViewById(R.id.share);
        final Button play_Again = view.findViewById(R.id.play_again);
        final Button next_Challenge = view.findViewById(R.id.next_challenge);
        final Button exit_Challenge = view.findViewById(R.id.exit_challenge);

        Common.Jelly_Animation(getApplicationContext(), challenge_Layout, 975);

        /* Set Font */
        Common.Set_Font(getApplicationContext(), new View[]{you_Win, mode, challenge, Share, play_Again, next_Challenge, exit_Challenge});

        final int challenge_Id = Common.Get_Challenge_Id(getApplicationContext());
        challenge.setText(String.valueOf(challenge_Id));
        challenge.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {

                if(Integer.parseInt(s.toString())<=16){
                    challenge_Layout.setBackground(getResources().getDrawable(R.drawable.shape_256));
                }
                else if(Integer.parseInt(s.toString())>16 && Integer.parseInt(s.toString())<=32){
                    challenge_Layout.setBackground(getResources().getDrawable(R.drawable.shape_16));
                }
                else if(Integer.parseInt(s.toString())>32 && Integer.parseInt(s.toString())<=48){
                    challenge_Layout.setBackground(getResources().getDrawable(R.drawable.shape_512));
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), Share, 1000);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                if(Common.Is_Internet_Connected(getApplicationContext())){
                    if(isPermissionGranted(222)){
                        Share();
                    }
                }
                else {
                    Common.Custom_SnackBar(getApplicationContext(), view.findViewById(R.id.root_layout), getResources().getString(R.string.network_Error));
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), play_Again, 1025);
        play_Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                Common.Set_Challenge_Mode(getApplicationContext(),true);
                Restart_Game();
                Challenges(challenge_Id);
                Common.Set_Is_Game_Resumed(getApplicationContext(),true);
                Resume_Game();
                alertDialog.dismiss();
            }
        });

        if(challenge_Id==64){
            next_Challenge.setVisibility(View.GONE);
        }
        else {
            Common.Jelly_Animation(getApplicationContext(), next_Challenge, 1050);
            next_Challenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                    Common.Sound(getApplicationContext(), "Click");
                    Common.Vibration(getApplicationContext());
                    Common.Set_Challenge_Mode(getApplicationContext(),true);
                    Restart_Game();
                    Common.Set_Challenge_Id(getApplicationContext(),(challenge_Id+1));
                    Challenges(Common.Get_Challenge_Id(getApplicationContext()));
                    for (TextView view: new TextView[]{challenge_One_Tv, challenge_Two_Tv, challenge_Three_Tv}){
                        Set_Challenge_Style(view);
                    }
                    Common.Set_Is_Game_Resumed(getApplicationContext(),true);
                    Resume_Game();
                    alertDialog.dismiss();
                }
            });
        }

        Common.Jelly_Animation(getApplicationContext(), exit_Challenge, 1075);
        exit_Challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                Common.Set_Challenge_Mode(getApplicationContext(),false);
                Restart_Game();
                alertDialog.dismiss();
            }
        });
    }

    private boolean Is_Challenge_Lose() {
        int moves = Integer.parseInt(Moves_Count.getText().toString());
        int ch_1_count = Integer.parseInt(challenge_One_Count.getText().toString());
        int ch_1_target = Integer.parseInt(challenge_One_Target.getText().toString());

        int ch_2_count = Integer.parseInt(challenge_Two_Count.getText().toString());
        int ch_2_target = Integer.parseInt(challenge_Two_Target.getText().toString());

        int ch_3_count = Integer.parseInt(challenge_Three_Count.getText().toString());
        int ch_3_target = Integer.parseInt(challenge_Three_Target.getText().toString());

        return moves == 0 && !((ch_1_count == ch_1_target) && (ch_2_count == ch_2_target) && (ch_3_count == ch_3_target));
    }

    private void Game_Over_Dialog(){

        final View view = View.inflate(getApplicationContext(),R.layout.game_over_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        Common.Sound(getApplicationContext(), "Lose");

        final TextView game_Over = view.findViewById(R.id.game_over);
        final Button Score = view.findViewById(R.id.score);
        final Button Share = view.findViewById(R.id.share);
        final Button try_Again = view.findViewById(R.id.try_again);
        final Button exit_Challenge = view.findViewById(R.id.exit_challenge);
        final Button continue_Game = view.findViewById(R.id.continue_game);

        /* Set Font */
        Common.Set_Font(getApplicationContext(), new View[]{game_Over, Score, Share, try_Again, exit_Challenge, continue_Game});

        if(Common.Get_Challenge_Mode(getApplicationContext())){
            exit_Challenge.setVisibility(View.VISIBLE);
            Common.Jelly_Animation(getApplicationContext(), exit_Challenge, 1000);
            exit_Challenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                    Common.Sound(getApplicationContext(), "Click");
                    Common.Vibration(getApplicationContext());
                    Common.Set_Challenge_Mode(getApplicationContext(),false);
                    Restart_Game();
                    alertDialog.dismiss();
                }
            });
        }
        else {
            Score.setVisibility(View.VISIBLE);
            Common.Jelly_Animation(getApplicationContext(), Score, 1000);
            int New_Score = Integer.parseInt(Current_Score.getText().toString());
            int Highest_Score = Integer.parseInt(Best_Score.getText().toString());

            if(New_Score < Highest_Score){
                Score.setText("Score\n"+New_Score);
            }
            else {
                Score.setText(getResources().getString(R.string.best_score)+New_Score);
            }

            Share.setVisibility(View.VISIBLE);
            Common.Jelly_Animation(getApplicationContext(), Share, 1025);
            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                    Common.Sound(getApplicationContext(), "Click");
                    Common.Vibration(getApplicationContext());
                    if(Common.Is_Internet_Connected(getApplicationContext())){
                        if(isPermissionGranted(222)){
                            Share();
                        }
                    }
                    else {
                        Common.Custom_SnackBar(getApplicationContext(), view.findViewById(R.id.root_layout), getResources().getString(R.string.network_Error));
                    }
                }
            });
        }

        Common.Jelly_Animation(getApplicationContext(), continue_Game, 1050);
        continue_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                if(Common.Get_Lives_Remaining(getApplicationContext()) > 0){
                    int lives_remaining = Common.Get_Lives_Remaining(getApplicationContext()) - 1;
                    Common.Set_Lives_Remaining(getApplicationContext(),lives_remaining);

                    if(Common.Get_Challenge_Mode(getApplicationContext())){
                        Enable_Game();
                        Moves_Count.setText("5");
                    }
                    else {
                        Shuffle_Values();
                    }
                    Common.Set_Is_Game_Resumed(getApplicationContext(),true);
                    Resume_Game();
                    alertDialog.dismiss();
                    if(Is_Game_Over()){
                        Disable_Game();
                        Game_Over_Dialog();
                    }
                }
                else {
                    Common.Custom_SnackBar(getApplicationContext(), view.findViewById(R.id.root_layout), "Sorry! no more lives");
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), try_Again, 1050);
        try_Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                if(Common.Get_Challenge_Mode(getApplicationContext())){
                    Challenges(Common.Get_Challenge_Id(getApplicationContext()));
                    Restart_Game();
                }
                else {
                    Restart_Game();
                }
                alertDialog.dismiss();
            }
        });
    }

    private void Shuffle_Values(){

        ArrayList<Long> values = new ArrayList<>();
        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for(TextView tile : tiles){
            tile.setEnabled(true);
            values.add(Long.parseLong(tile.getText().toString()));
        }

        Collections.shuffle(values);

        for (int i=0; i < tiles.length; i++){
            Common.Bounce_Animation(getApplicationContext(), tiles[i], 0.25, 5);
            tiles[i].setText(values.get(i).toString());
        }
    }

    private void Restart_Game() {

        Set_Game_Mode();

        Common.Set_Lives_Remaining(getApplicationContext(),3);

        Current_Score.setText("0");

        Random_Tile_Position_Style();

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                    tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (TextView tile: tiles){
            tile.setText("2");
            tile.setEnabled(true);
        }

        if(Common.Get_Challenge_Mode(getApplicationContext())){
            for (LinearLayout Layout: new LinearLayout[]{challenge_One_Layout, challenge_Two_Layout, challenge_Three_Layout}){
                Layout.setBackground(getResources().getDrawable(R.drawable.shape_lock));
            }
        }

        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
    }

    private void How_To_Play_Dialog(){
        View view = View.inflate(getApplicationContext(),R.layout.how_to_play_layout, null);

        TextView howToPlay_Title = view.findViewById(R.id.howToPlay_Title);
        TextView howToPlay_Tv = view.findViewById(R.id.howToPlay_Tv);
        ImageButton Cancel = view.findViewById(R.id.back_btn);

        /* Set Font*/
        Common.Set_Font(getApplicationContext(), new View[]{howToPlay_Title, howToPlay_Tv});

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        Common.Jelly_Animation(getApplicationContext(), Cancel, 1000);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                alertDialog.dismiss();
            }
        });
    }

    private void Share_Apk() {
        try {
            File initialApkFile = new File(getPackageManager().getApplicationInfo(getPackageName(), 0).sourceDir);

            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");

            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;

            tempFile = new File(tempFile.getPath() + "/" + getResources().getString(R.string.app_name) + ".apk");

            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }

            InputStream in = new FileInputStream(initialApkFile);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("*/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(share, "Share " + getResources().getString(R.string.app_name) + " Via"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Resume_Game(){

        if(!Common.Get_Challenge_Mode(getApplicationContext())){
            Common.Set_Game_Score(getApplicationContext(), Common.Current_Score_Key, Integer.parseInt(Current_Score.getText().toString()));
        }
        else {
            Common.Set_Resume_Data(getApplicationContext(),Common.Moves_Key, Moves_Count.getText().toString());
            Common.Set_Resume_Data(getApplicationContext(),Common.Challenge_One_Key, challenge_One_Count.getText().toString());
            Common.Set_Resume_Data(getApplicationContext(),Common.Challenge_Two_Key, challenge_Two_Count.getText().toString());
            Common.Set_Resume_Data(getApplicationContext(),Common.Challenge_Three_Key, challenge_Three_Count.getText().toString());
        }

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (int i=0; i<tiles.length; i++){
            if(tiles[i].getAlpha()==1){
                Common.Set_Resume_Data(getApplicationContext(),String.valueOf(i+1), tiles[i].getText().toString());
            }
            else {
                Common.Set_Resume_Data(getApplicationContext(),String.valueOf(i+1), "invisible");
            }
        }
    }

    private void Reload_Game(){

        Set_Game_Mode();

        if(!Common.Get_Challenge_Mode(getApplicationContext())){
            Current_Score.setText(String.valueOf(Common.Get_Game_Score(getApplicationContext(), Common.Current_Score_Key)));
        }
        else {
            Challenges(Common.Get_Challenge_Id(getApplicationContext()));
            Moves_Count.setText(Common.Get_Resumed_Data(getApplicationContext()).get(Common.Moves_Key));
            challenge_One_Count.setText(Common.Get_Resumed_Data(getApplicationContext()).get(Common.Challenge_One_Key));
            challenge_Two_Count.setText(Common.Get_Resumed_Data(getApplicationContext()).get(Common.Challenge_Two_Key));
            challenge_Three_Count.setText(Common.Get_Resumed_Data(getApplicationContext()).get(Common.Challenge_Three_Key));

            int ch_1_count = Integer.parseInt(challenge_One_Count.getText().toString());
            int ch_1_target = Integer.parseInt(challenge_One_Target.getText().toString());

            int ch_2_count = Integer.parseInt(challenge_Two_Count.getText().toString());
            int ch_2_target = Integer.parseInt(challenge_Two_Target.getText().toString());

            int ch_3_count = Integer.parseInt(challenge_Three_Count.getText().toString());
            int ch_3_target = Integer.parseInt(challenge_Three_Target.getText().toString());

            if(ch_1_count == ch_1_target){
                challenge_One_Layout.setBackground(getResources().getDrawable(R.drawable.shape_unlock));
            }
            else {
                challenge_One_Layout.setBackground(getResources().getDrawable(R.drawable.shape_lock));
            }

            if(ch_2_count == ch_2_target){
                challenge_Two_Layout.setBackground(getResources().getDrawable(R.drawable.shape_unlock));
            }
            else {
                challenge_Two_Layout.setBackground(getResources().getDrawable(R.drawable.shape_lock));
            }

            if(ch_3_count == ch_3_target){
                challenge_Three_Layout.setBackground(getResources().getDrawable(R.drawable.shape_unlock));
            }
            else {
                challenge_Three_Layout.setBackground(getResources().getDrawable(R.drawable.shape_lock));
            }
        }

        TextView[] tiles = {tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8,
                tile_9, tile_10, tile_11, tile_12, tile_13, tile_14, tile_15, tile_16};

        for (int i=0; i<tiles.length; i++){
            if(Common.Get_Resumed_Data(getApplicationContext()).get(String.valueOf(i+1)).equals("invisible")){
                Set_Invisibility(tiles[i].getTag().toString());
            }
            else {
                tiles[i].setText(Common.Get_Resumed_Data(getApplicationContext()).get(String.valueOf(i+1)));
                Set_Tiles_Style(tiles[i]);
                Set_Visibility(tiles[i].getTag().toString());
            }
        }
    }

    @Override protected void onPause() {
        super.onPause();

        screenOff = true;
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
    }

    @Override protected void onResume() {
        super.onResume();

        if(screenOff) {
            if (Common.Get_Is_Game_Resumed(getApplicationContext())) {
                Reload_Game();
                screenOff = false;
            }
        }
    }

    @Override public void onBackPressed() {
        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
        Quit_Dialog();
    }

    private void Quit_Dialog(){
        View view = View.inflate(getApplicationContext(),R.layout.dialog_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        alertDialog.show();

        LinearLayout dialog_Layout = view.findViewById(R.id.dialog_layout);
        TextView Message = view.findViewById(R.id.message);
        Button Yes = view.findViewById(R.id.yes);
        Button No = view.findViewById(R.id.no);

        /* Set Font */
        Common.Set_Font(getApplicationContext(), new View[]{Message, Yes, No});

        /* Set Display Size */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog_Layout.getLayoutParams();

        int size = displayMetrics.widthPixels;

        if(size <= 500){
            params.height = size/2;
            params.width = size - (size/12);
        }
        else{
            params.height = size/2;
            params.width = size - (size/10);
        }
        dialog_Layout.setLayoutParams(params);

        Common.Jelly_Animation(getApplicationContext(), Yes, 1000);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                alertDialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

        Common.Jelly_Animation(getApplicationContext(), No, 1000);
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                alertDialog.dismiss();
            }
        });
    }

    public void Share_Snapshot(View view) {
        Common.Bounce_Animation(getApplicationContext(), findViewById(R.id.share_icon), 0.15, 5);
        Common.Vibration(getApplicationContext());
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
        Snapshot_Dialog();
    }

    private void Snapshot_Dialog(){

        final View view = View.inflate(getApplicationContext(),R.layout.snapshot_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        alertDialog.show();

        Common.Sound(getApplicationContext(), "Capture");
        Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);

        RelativeLayout snapshot_layout = view.findViewById(R.id.snapshot_layout);
        LinearLayout Save = view.findViewById(R.id.saveTo_gallery_btn);
        LinearLayout Share = view.findViewById(R.id.share_btn);
        ImageButton Cancel = view.findViewById(R.id.cancel_btn);
        TextView Save_tv = view.findViewById(R.id.save_tv);
        TextView Share_tv = view.findViewById(R.id.share_tv);


        /* Set Font */
        Common.Set_Font(getApplicationContext(), new View[]{Save_tv, Share_tv});

        snapshot_layout.setBackground(new BitmapDrawable(getResources(), Take_Screenshot(Content_Layout)));

        Common.Jelly_Animation(getApplicationContext(), Save, 1000);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                if(isPermissionGranted(111)){
                    Save_To_Gallery();
                }
                alertDialog.dismiss();
            }
        });

        Common.Jelly_Animation(getApplicationContext(), Share, 1000);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                alertDialog.dismiss();
                if(Common.Is_Internet_Connected(getApplicationContext())){
                    if(isPermissionGranted(222)){
                        Share();
                    }
                }
                else {
                    Common.Custom_SnackBar(getApplicationContext(), findViewById(R.id.root_layout), getResources().getString(R.string.network_Error));
                }
            }
        });

        Common.Jelly_Animation(getApplicationContext(), Cancel, 1000);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.15, 5);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                alertDialog.dismiss();
            }
        });
    }

    private void Share(){

        @SuppressLint("DefaultLocale") String path = Environment.getExternalStorageDirectory().toString() + "/" + String.format("%d.png", System.currentTimeMillis());

        OutputStream outputStream = null;
        File imageFile = new File(path);

        try {
            outputStream = new FileOutputStream(imageFile);
            Take_Screenshot(Content_Layout).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        /*intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");*/
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        intent.setType("image/*");
        startActivity(intent);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 111:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Save_To_Gallery();
                }
                break;

            case 222:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(Common.Is_Internet_Connected(getApplicationContext())){
                        Share();
                    }
                    else {
                        Common.Custom_SnackBar(getApplicationContext(), findViewById(R.id.root_layout), getResources().getString(R.string.network_Error));
                    }
                }
                break;

            case 333:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Share_Apk();
                }
                break;
        }
    }

    private boolean isPermissionGranted(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private Bitmap Take_Screenshot(View view) {

        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY);

        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY);

        view.measure(widthSpec, heightSpec);

        /* set layout sizes */
        view.layout(view.getLeft(), view.getTop(), view.getMeasuredWidth() + view.getLeft(), view.getMeasuredHeight() + view.getTop());

        /* create the bitmap */
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        /* create a canvas used to get the view's image and draw it on the bitmap */
        Canvas c = new Canvas(bitmap);

        /* position the image inside the canvas */
        c.translate(-view.getScrollX(), -view.getScrollY());

        view.draw(c);

        return bitmap;
    }

    private void Save_To_Gallery() {
        try {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File sdCard = Environment.getExternalStorageDirectory();
                File path = new File(sdCard.getAbsolutePath() + "/" + getResources().getString(R.string.app_name));

                if (!path.exists()) {
                    path.mkdir();
                }

                @SuppressLint("DefaultLocale") String imageName = String.format("%d.png", System.currentTimeMillis());
                File outputFile = new File(path, imageName);

                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                FileOutputStream outputStream = new FileOutputStream(outputFile);
                Take_Screenshot(Content_Layout).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                Show_In_Gallery(outputFile.getAbsolutePath());

                outputStream.flush();
                outputStream.close();

                Common.Custom_SnackBar(getApplicationContext(), findViewById(R.id.root_layout), getResources().getString(R.string.save_snapshot));
            }
            else {
                Common.Custom_Toast(getApplicationContext(), "Sorry! No SDCard Found");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void Show_In_Gallery(String PicturePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(PicturePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}