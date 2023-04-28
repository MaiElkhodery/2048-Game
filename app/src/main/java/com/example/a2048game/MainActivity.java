package com.example.a2048game;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ResultFragment.SetonClickAgain,GestureDetector.OnGestureListener,View.OnKeyListener{

    TextView score ;
    TextView bestScore;
    int scoreValue ;
    long bestScoreValue;
    ArrayList<int[]> emptySpaces;
    int[][] cellValuesOfBoard;
    AppCompatButton[][] cellTextViewOfBoard;
    SharedPreferences sharedPreferences;
    String FILE_NAME = "highScoreFile";
    String high_score = "highScore";
    private GestureDetectorCompat gestureDetector;
    FrameLayout frameLayout;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int RIGHT = 3;
    private final int LEFT = 4;
    private final int BOARD_SIZE=4;
    ResultFragment resultFragment;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = findViewById(R.id.scoreValue_textView);
        bestScore = findViewById(R.id.bestScoreValue_textview);
        AppCompatImageButton restartButton = findViewById(R.id.newGameButton);
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
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        boolean changed;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    Log.d("Move", "UP");
                    changed=moveUp();
                    if (changed)
                        update();
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    Log.d("Move", "UP");
                    changed = moveDown();
                    if (changed)
                        update();
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    Log.d("Move", "UP");
                    changed=moveLeft();
                    if (changed)
                        update();
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    Log.d("Move", "UP");
                    changed=moveRight();
                    if (changed)
                        update();
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
        int dir = direction(e1.getX(),e1.getY(),e2.getX(),e2.getY());
        boolean changed;
        if(dir == UP) {
            Log.d("Move", "UP");
            changed = moveUp();
            if (changed)
                update();
        }
        else if(dir == DOWN){
            Log.d("Move", "DOWN");
            changed=moveDown();
            if (changed)
                update();
        }

        else if(dir == LEFT){
            Log.d("Move", "LEFT");
            changed=moveLeft();
            if (changed)
                update();
        }
        else{
            Log.d("Move", "RIGHT");
            changed=moveRight();
            if (changed)
                update();
        }
        return true;
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
               button.setTextColor(getResources().getColor(R.color.black));
               break;
           default:
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.Color2048));
               button.setTextColor(getResources().getColor(R.color.black));
       }
    }
    public void addRandomNum(){
        if(emptySpaces.isEmpty())
            isGameOver();
        Random random = new Random();
        int randomNo = random.nextInt(emptySpaces.size());
        int[] randomPosition = emptySpaces.get(randomNo);
        int row = randomPosition[0];
        int column = randomPosition[1];
        emptySpaces.remove(randomNo);
        int addedRandomNum = random.nextInt(2);
        if(addedRandomNum == 0){
            cellValuesOfBoard[row][column] = 2;
        }else{
            cellValuesOfBoard[row][column] = 4;
        }
        makeCellStyle(cellTextViewOfBoard[row][column],cellValuesOfBoard[row][column]);
    }

    @SuppressLint("SetTextI18n")
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
        bestScore.setText(String.valueOf(bestScoreValue));
        addRandomNum();
        addRandomNum();
    }
    void isGameOver(){

        // Check if any tile has a value of 2048.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cellValuesOfBoard[i][j] == 2048) {
                    openResultLayout("YOU WIN!");
                    Log.d("Result", "YOU WIN!");
                }
            }
        }
        // Check if there are any empty tiles on the board.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cellValuesOfBoard[i][j] == 0) {
                    return ;
                }
            }
        }
        // Check if there are any adjacent tiles with the same value.
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int currentValue = cellValuesOfBoard[i][j];

                // Check the tile to the right.
                if (j < BOARD_SIZE - 1 && cellValuesOfBoard[i][j + 1] == currentValue) {
                    return ;
                }

                // Check the tile below.
                if (i < BOARD_SIZE - 1 && cellValuesOfBoard[i + 1][j] == currentValue) {
                    return ;
                }
            }
        }

        if(scoreValue> sharedPreferences.getLong(high_score,0)){
            sharedPreferences.edit().putLong(high_score,scoreValue).apply();
        }
        openResultLayout("GAME OVER");
        Log.d("Result", "GAME OVER");
    }

    private void openResultLayout(String msg){
        resultFragment=ResultFragment.newInstance(msg);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,resultFragment).commit();
    }
    public int direction(float x1,float x2,float y1,float y2){
        if (Math.abs(x1-x2) > Math.abs(y1-y2)){
            return x1>x2 ? LEFT : RIGHT;
        }else{
            return y1>y2 ? DOWN : UP;
        }
    }
    private void updateEmptyCells(){
        emptySpaces.clear();
        for(int r =0 ; r<BOARD_SIZE;r++){
            for(int c =0 ; c<BOARD_SIZE;c++){
                if(cellValuesOfBoard[r][c] == 0){
                    emptySpaces.add(new int[]{r,c});
                }
            }
        }
    }
    private void updateCells(){
        for(int r =0 ; r<BOARD_SIZE;r++){
            for(int c =0 ; c<BOARD_SIZE;c++){
                makeCellStyle(cellTextViewOfBoard[r][c],cellValuesOfBoard[r][c]);
            }
        }
    }
    public void update(){
        updateEmptyCells();
        addRandomNum();
        updateCells();
        isGameOver();
    }

    public boolean moveUp(){
        Log.d("Move", "UP");
        transpose();
        boolean changed = moveLeft();
        transpose();
        return changed;
    }
    public boolean moveDown(){
        Log.d("Move", "DOWN");
        transpose();
        boolean changed = moveRight();
        transpose();
        return changed;
    }
    public boolean moveRight(){
        Log.d("Move", "RIGHT");
       reverse();
       boolean changed = moveLeft();
       reverse();
       return changed;
    }
    public boolean moveLeft(){
        Log.d("Move", "LEFT");
       boolean firstChange = compress();
       boolean secChange = merge();
       boolean thirdChange = compress();
       return firstChange || secChange || thirdChange;
    }


    private void reverse() {
        int[][] newBoardValues = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newBoardValues[i][j] = cellValuesOfBoard[i][3 - j];
            }
        }
        cellValuesOfBoard=newBoardValues;
    }
    private void transpose() {
        int[][] newBoardValues = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newBoardValues[i][j] = cellValuesOfBoard[j][i];
            }
        }
        cellValuesOfBoard=newBoardValues;
    }

    public boolean  merge() {
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (cellValuesOfBoard[i][j] == cellValuesOfBoard[i][j + 1] && cellValuesOfBoard[i][j] != 0) {
                    cellValuesOfBoard[i][j] = cellValuesOfBoard[i][j] * 2;
                    scoreValue+=cellValuesOfBoard[i][j];
                    score.setText(String.valueOf(scoreValue));
                    cellValuesOfBoard[i][j + 1] = 0;
                    changed = true;
                }
            }
        }
        return changed;
    }
    public boolean compress() {
        boolean changed = false;
        int[][] newBoardValues = new int[4][4];
        for (int i = 0; i < 4; i++) {
            int pos = 0;
            for (int j = 0; j < 4; j++) {
                if (cellValuesOfBoard[i][j] != 0) {
                    newBoardValues[i][pos] = cellValuesOfBoard[i][j];
                    if (j != pos) {
                        changed = true;
                    }
                    pos++;
                }
            }
        }
        cellValuesOfBoard=newBoardValues;
        return changed;
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
            sharedPreferences.edit().putLong(high_score,scoreValue).apply();
        }
    }


}