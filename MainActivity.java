package com.example.dragdrop;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView iconDraggable,iconXPositive,iconYPositive,iconXNegative,iconYNegative;
    private float startX, startY;
    private String currentlyMovingX = "";
    private float[] initX={0};
    private float[] initY={0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find views by their IDs
         iconXPositive = findViewById(R.id.iconXPositive);
         iconXNegative = findViewById(R.id.iconXNegative);
         iconYPositive = findViewById(R.id.iconYPositive);
         iconYNegative = findViewById(R.id.iconYNegative);
        iconDraggable = findViewById(R.id.iconDraggable);

        setDraggable();
        // Set touch listener for draggable icon


    }
    private void setDraggable(){
        iconDraggable.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        if(initX[0] == 0){
                            initX[0] = iconDraggable.getX();
                        }
                        if(initY[0]==0){
                            initY[0] = iconDraggable.getY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - startX;
                        float dy = event.getY() - startY;
                        if (currentlyMovingX.equals("")) {
                            // Move only along the x-axis
                            if (Math.abs(dx) > Math.abs(dy)) {
                                if(event.getX()>initX[0]){
                                    currentlyMovingX = "X";
                                }else {
                                    currentlyMovingX = "-X";
                                }
                            } else {
                                Log.d("Y event",event.getY()+"");
                                Log.d("Y ",initY[0]+"");
                                if(event.getY()>initY[0]){
                                    currentlyMovingX = "Y";
                                }else {
                                    currentlyMovingX = "-Y";
                                }
                            }
                        }

                        // Restrict movement to only x-axis or y-axis
                        if (currentlyMovingX.contains("X")) {
                            // Move only along the x-axis
                            iconDraggable.setX(iconDraggable.getX() + dx);
                        } else {
                            // Move only along the y-axis
                            iconDraggable.setY(iconDraggable.getY() + dy);
                        }

                        Rect rect1 = new Rect();
                        iconDraggable.getHitRect(rect1);
                        Rect rect2 = new Rect();
                        switch (currentlyMovingX){
                            case "+X":
                                if(checkIntersection(iconDraggable,iconXPositive)){
                                    Log.d("XPositive","X+ve");
                                }
                                break;
                            case "+Y":
                                if(checkIntersection(iconDraggable,iconYPositive)){
                                    Log.d("YPositive","Y+ve");
                                }
                                break;
                            case "-X":
                                if(checkIntersection(iconDraggable,iconXNegative)){
                                    Log.d("XNositive","X-ve");
                                }
                                break;
                            case "-Y":
                                if(checkIntersection(iconDraggable,iconYNegative)){
                                    Log.d("YNositive","Y-ve");
                                }
                                break;
                        }

                        break;


                    case MotionEvent.ACTION_UP:
                        animateToPosition(initX[0],initY[0]);
                        // Perform any desired action after the drag ends
                        break;
                }
                return true;
            }
        });
    }
    private boolean checkIntersection(View view1, View view2) {
        Rect rect1 = new Rect();
        view1.getHitRect(rect1);

        Rect rect2 = new Rect();
        view2.getHitRect(rect2);

        return rect1.intersect(rect2);
    }
    private void animateToPosition(float targetX, float targetY) {
        iconDraggable.setOnTouchListener(null);
        float currentX = iconDraggable.getX();
        float currentY = iconDraggable.getY();

        // Calculate the distance to animate
        float deltaX = targetX - currentX;
        float deltaY = targetY - currentY;

        // Create the animation
        TranslateAnimation animation = new TranslateAnimation(0, deltaX, 0, deltaY);
        animation.setDuration(50); // Animation duration in milliseconds
        animation.setFillAfter(true); // Maintain the final position after the animation ends
        animation.setInterpolator(new AccelerateDecelerateInterpolator()); // Optional interpolator for smooth animation

        // Set animation listener if needed
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended
                iconDraggable.setX(targetX);
                iconDraggable.setY(targetY);
                iconDraggable.clearAnimation();
                currentlyMovingX="";
                //setDraggable();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setDraggable();
                    }
                },1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated
            }
        });

        // Start the animation
        iconDraggable.startAnimation(animation);
    }
}
