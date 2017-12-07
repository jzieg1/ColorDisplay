package com.zieg.colordisplay;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static android.R.attr.button;
import static android.R.attr.color;
import static com.zieg.colordisplay.R.color.black;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar (does not work on API 22 and 25, fix later)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    /* changeColor creates a new button view covering the entire screen
       only one method for 16 buttons, pulls color value from pressed button
       clicking the new button destroys the button and returns to the default state
       cycling method creates button once and then rapidly changes the background color
     */

    public void onClickChangeColor(View v){
        //gets id of button clicked, uses it to identify the color of button
        int id = v.getId();
        Button clickedButton = (Button) findViewById(id);
        int buttonColor = ((ColorDrawable)clickedButton.getBackground()).getColor();

        //creates new button, sets layout parameters of button so that it will cover the screen
        //sets color of new button to color of whatever button was originally clicked
        Button newButton = new Button(this);
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.activity_main);
        newButton.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        newButton.setBackgroundColor(buttonColor);

        //sets onClick method for new button
        //button destroys self when clicked
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.activity_main);
                mainLayout.removeView(v);
            }
        };
        newButton.setOnClickListener(listener);

        //adds button to layout
        mainLayout.addView(newButton);
    }

    //cycles through colors as presented in button layout right to left and top to bottom
    //creates one button as with onClickChangeColor but changes button background color every X
    //seconds as described by user in secondSelection
    //button destroys self at end of cycle
    //clicking button also destroys it and cancels the cycle
    public void onClickCycleColor(View v){
        //gets number of seconds to delay color display from secondSelection editText in layout
        EditText secondSelection = (EditText) findViewById(R.id.secondSelection);
        if(secondSelection.getText().toString().matches("")){
            Toast.makeText(this, "Input a number of seconds to delay color change", Toast.LENGTH_SHORT).show();
            return;
        }
        final double secondsDelayed = Double.parseDouble(secondSelection.getText().toString());

        //creates new button, sets layout parameters of button so that it will cover the screen
        final Button newButton = new Button(this);
        final RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.activity_main);
        newButton.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        //sets onClick method for new button
        //button destroys self when clicked
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.activity_main);
                mainLayout.removeView(v);
            }
        };
        newButton.setOnClickListener(listener);
        newButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        //adds button to layout
        mainLayout.addView(newButton);

        //cycles through all colors in the array of colors in colors.xml
        //pauses for 2 seconds between colors
        //new colors can be added by editing the colors.xml
        final int[] rainbow = getApplicationContext().getResources().getIntArray(R.array.rainbow);
        for(int i=0; i<rainbow.length; i++){
            final int j=i;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newButton.setBackgroundColor(rainbow[j]);
                    System.out.println("Loop " + j);
                }
            },(long)(j*secondsDelayed*1000));

        }

        final Handler destroyButtonHandler = new Handler();
        destroyButtonHandler.postDelayed(new Runnable(){
            public void run(){
                mainLayout.removeView(newButton);
            }
        },(long)(1000*secondsDelayed*rainbow.length));


    }

}
