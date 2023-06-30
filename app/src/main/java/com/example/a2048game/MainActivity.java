package com.example.a2048game;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ResultFragment.SetonClickAgain,GestureDetector.OnGestureListener,View.OnKeyListener{

    TextView score ;
    TextView bestScore;
    ResultFragment resultFragment;
    AppCompatButton[][] cellTextViewOfBoard;
    FrameLayout frameLayout;
    int scoreValue ;
    long bestScoreValue;
    private final int BOARD_SIZE=4;
    ArrayList<int[]> emptySpaces;
    int[][] cellValuesOfBoard;
    SharedPreferences sharedPreferences;
    String FILE_NAME = "highScoreFile";
    String high_score = "highScore";
    private GestureDetectorCompat gestureDetector;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = findViewById(R.id.scoreValue_textView);
        bestScore = findViewById(R.id.bestScoreValue_textview);
        AppCompatButton restartButton = findViewById(R.id.newGameButton2);
        scoreValue = 0;
        score.setText(scoreValue+"");

        cellValuesOfBoard = new int[][]{
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        };
        cellTextViewOfBoard = new AppCompatButton[][]{
                {findViewById(R.id.cell1),findViewById(R.id.cell2),findViewById(R.id.cell3),findViewById(R.id.cell4)},
                {findViewById(R.id.cell5),findViewById(R.id.cell6),findViewById(R.id.cell7),findViewById(R.id.cell8)},
                {findViewById(R.id.cell9),findViewById(R.id.cell10),findViewById(R.id.cell11),findViewById(R.id.cell12)},
                {findViewById(R.id.cell13),findViewById(R.id.cell14),findViewById(R.id.cell15),findViewById(R.id.cell16)}
        };
        emptySpaces = new ArrayList<>();
        for(int row = 0; row<4; row++){
            for(int column = 0; column<4; column++){
                    int[] emptyPositions={row,column};
                    emptySpaces.add(emptyPositions);
            }
        }
        for(int row = 0; row<4; row++){
            for(int column = 0; column<4; column++){
                makeCellStyle(cellTextViewOfBoard[row][column],cellValuesOfBoard[row][column]);
            }
        }

        bestScoreValue=0;
        sharedPreferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        bestScoreValue = sharedPreferences.getLong(high_score,0);
        bestScore.setText(String.valueOf(bestScoreValue));

        restartButton.setOnClickListener(view -> restart());

        frameLayout=findViewById(R.id.frameLayout);
        gestureDetector = new GestureDetectorCompat(this, this);
        ConstraintLayout main_Layout=findViewById(R.id.mainLayout);
        main_Layout.setOnKeyListener(this);
        main_Layout.setFocusableInTouchMode(true);
        main_Layout.requestFocus();
        restart();
    }
    public void restart(){
        if(scoreValue> sharedPreferences.getLong(high_score,0)){
            sharedPreferences.edit().putLong(high_score,scoreValue).apply();
        }
        for(int row = 0; row<4; row++){
            for(int column = 0; column<4; column++){
                int[] emptyPositions={row,column};
                emptySpaces.add(emptyPositions);
                cellValuesOfBoard[row][column]=0;
                makeCellStyle(cellTextViewOfBoard[row][column],cellValuesOfBoard[row][column]);
            }
        }
        scoreValue =0;
        score.setText(String.valueOf(scoreValue));
        bestScoreValue = sharedPreferences.getLong(high_score,0);
        if (bestScoreValue > 10000){
            bestScore.setTextSize(18);
        }
        bestScore.setText(String.valueOf(bestScoreValue));
        cellValuesOfBoard=Board.addRandomNum(cellValuesOfBoard);
        cellValuesOfBoard=Board.addRandomNum(cellValuesOfBoard);
        updateCells();
    }
    public void update(int[][] boardAfterMove){
        cellValuesOfBoard=boardAfterMove.clone();
        scoreValue = Move.score;
        if (scoreValue > 10000){
            score.setTextSize(18);
        }
        score.setText(String.valueOf(scoreValue));
        cellValuesOfBoard=Board.addRandomNum(cellValuesOfBoard);
        updateCells();
        if(Board.isGameOver(cellValuesOfBoard)){
            openResultLayout("GAME OVER","Again");
        }else{
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (cellValuesOfBoard[i][j] == 2048) {
                        openResultLayout("YOU WIN!","Continue");
                        Log.d("Result", "YOU WIN!");
                    }
                }
            }
        }
    }
    public void updateCells(){
        for(int r =0 ; r<BOARD_SIZE;r++){
            for(int c =0 ; c<BOARD_SIZE;c++){
                makeCellStyle(cellTextViewOfBoard[r][c],cellValuesOfBoard[r][c]);
            }
        }
    }
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        Pair<Boolean,int[][]> afterMove;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    Log.d("Move","UP");
                    afterMove=Move.moveUp(cellValuesOfBoard);
                    if (afterMove.first)
                        update(afterMove.second);
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    Log.d("Move","DOWN");
                    afterMove=Move.moveDown(cellValuesOfBoard);
                    if (afterMove.first)
                        update(afterMove.second);
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    Log.d("Move","LEFT");
                    afterMove=Move.moveLeft(cellValuesOfBoard);
                    if (afterMove.first)
                        update(afterMove.second);
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    Log.d("Move","RIGHT");
                    afterMove=Move.moveRight(cellValuesOfBoard);
                    if (afterMove.first)
                        update(afterMove.second);
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Directions dir = direction(e1.getX(),e1.getY(),e2.getX(),e2.getY());
        Pair<Boolean,int[][]> afterMove;
        if(dir == Directions.UP) {
            afterMove=Move.moveUp(cellValuesOfBoard);
            if (afterMove.first)
                update(afterMove.second);
        }
        else if(dir == Directions.DOWN){
            afterMove=Move.moveDown(cellValuesOfBoard);
            if (afterMove.first)
                update(afterMove.second);
        }

        else if(dir == Directions.LEFT){
            afterMove=Move.moveLeft(cellValuesOfBoard);
            if (afterMove.first)
                update(afterMove.second);
        }
        else{
            afterMove=Move.moveRight(cellValuesOfBoard);
            if (afterMove.first)
                update(afterMove.second);
        }
        return true;
    }

    public Directions direction(float x1,float x2,float y1,float y2){
        if (Math.abs(x1-x2) > Math.abs(y1-y2)){
            return x1>x2 ? Directions.LEFT : Directions.RIGHT;
        }else{
            return y1>y2 ? Directions.DOWN : Directions.UP;
        }
    }
    @SuppressLint("SetTextI18n")
    public void makeCellStyle(AppCompatButton button, int num){
       if(num == 0){
           button.setText(" ");
       }else{
           button.setText(String.valueOf(num));
       }
       switch (num){
           case 0:
               button.setTextColor(Color.BLACK);
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.score_best));
               button.setTextSize(25);
               break;
           case 2:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color2));
               button.setTextColor(getResources().getColor(R.color.black));
               break;
           case 4:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color4));
               button.setTextColor(getResources().getColor(R.color.black));
               break;
           case 8:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color8));
               button.setTextColor(getResources().getColor(R.color.White));
               break;
           case 16:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color16));
               button.setTextColor(getResources().getColor(R.color.White));
               break;
           case 32:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color32));
               button.setTextColor(getResources().getColor(R.color.White));
               break;
           case 64:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color64));
               button.setTextColor(getResources().getColor(R.color.White));
               break;
           case 128:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color128));
               button.setTextColor(getResources().getColor(R.color.White));
               break;
           case 256:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color256));
               button.setTextColor(getResources().getColor(R.color.White));
               break;
           case 512:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color512));
               button.setTextColor(getResources().getColor(R.color.black));

               break;
           case 1024:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color1024));
               button.setTextColor(getResources().getColor(R.color.White));
               button.setTextSize(22);
               break;
           default:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color2048));
               button.setTextColor(getResources().getColor(R.color.White));
               button.setTextSize(22);
       }
    }

    @SuppressLint("SetTextI18n")


    public void openResultLayout(String msg1,String msg2){
        resultFragment=ResultFragment.newInstance(msg1,msg2);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,resultFragment).commit();
    }


    @Override
    public void again(ResultFragment fragment) {
        restart();
        if (frameLayout != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(scoreValue> sharedPreferences.getLong(high_score,0)){
            sharedPreferences.edit().putLong(high_score,Move.score).apply();
        }
    }


}