package com.example.a2048game;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,View.OnKeyListener{

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
    ConstraintLayout gameOverLayout;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int RIGHT = 3;
    private final int LEFT = 4;
    private final int BOARD_SIZE=4;
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
        bestScore.setText(bestScoreValue+"");

        restartButton.setOnClickListener(view -> restart());

//        frameLayout=findViewById(R.id.frameLayout);
//        gameOverLayout=findViewById(R.id.gameOverLayout);
        gestureDetector = new GestureDetectorCompat(this, this);


        ConstraintLayout main_Layout=findViewById(R.id.mainLayout);
        main_Layout.setOnKeyListener(this);
        main_Layout.setFocusableInTouchMode(true);
        main_Layout.requestFocus();
        restart();
    }
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    // Handle moving up
                    onSwipeUp();
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    // Handle moving down
                    onSwipeDown();
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    // Handle moving left
                    onSwipeLeft();
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    onSwipeRight();
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
               button.setTextColor(getResources().getColor(R.color.black));
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
//        frameLayout.removeView(gameOverLayout);
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
    boolean isGameOver(){
        if(cellValuesOfBoard[0][0]==0)
        for(int i=0;i<4;i++) {
            for(int j=1;j<4;j++){
                if(cellValuesOfBoard[i][j]==0 || cellValuesOfBoard[i][j]==cellValuesOfBoard[i][j-1])
                    return false;
            }
        }
        for(int j=0;j<4;j++) {
            for(int i=1;i<4;i++){
                if(cellValuesOfBoard[i][j]==0 || cellValuesOfBoard[i][j]==cellValuesOfBoard[i-1][j])
                    return false;
            }
        }

        //frameLayout.addView(gameOverLayout);
        Toast.makeText(this,"GAME OVER",Toast.LENGTH_SHORT);
        return true;
    }

    public int direction(float x1,float x2,float y1,float y2){
        if (Math.abs(x1-x2) > Math.abs(y1-y2)){
            return x1>x2 ? LEFT : RIGHT;
        }else{
            return y1>y2 ? DOWN : UP;
        }
    }
    private void updateEmptyCells(){
        for(int r =0 ; r<BOARD_SIZE;r++){
            for(int c =0 ; c<BOARD_SIZE;c++){
                if(cellValuesOfBoard[r][c] == 0 && !emptySpaces.contains(new int[]{r, c})){
                    emptySpaces.clear();
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
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int dir = direction(e1.getX(),e1.getY(),e2.getX(),e2.getY());
        if(dir == UP)
            onSwipeUp();
        else if(dir == DOWN)
            onSwipeDown();
        else if(dir == LEFT)
            onSwipeLeft();
        else
            onSwipeRight();
        return true;
    }

    private void onSwipeRight() {
        if(isGameOver())
            return;
        boolean isMoved=false;
        for(int r = 0;r<BOARD_SIZE;r++){//row
            int index = BOARD_SIZE-1;
            for(int c = BOARD_SIZE-2;c>=0;c--){//column
                if(cellValuesOfBoard[r][c] != 0){
                    if(cellValuesOfBoard[r][index]==0){
                        cellValuesOfBoard[r][index]=cellValuesOfBoard[r][c];
                        cellValuesOfBoard[r][c]=0;
                        isMoved=true;
                    }else if(cellValuesOfBoard[r][c]==cellValuesOfBoard[r][index]){
                        cellValuesOfBoard[r][index]*=2;
                        cellValuesOfBoard[r][c]=0;
                        scoreValue+=cellValuesOfBoard[r][index];
                        score.setText(String.valueOf(scoreValue));
                        isMoved=true;
                    }else{
                        index--;
                        if (c != index) {
                            cellValuesOfBoard[r][index] = cellValuesOfBoard[r][c];
                            cellValuesOfBoard[r][c] = 0;
                            isMoved = true;
                        }
                    }
                }
            }
        }
        if(isMoved){
            updateEmptyCells();
            addRandomNum();
            updateCells();
            isGameOver();
        }
    }

    private void onSwipeLeft() {
        if(isGameOver())
            return;
        boolean isMoved=false;
        for(int r = 0;r<BOARD_SIZE;r++){//column
            int index = 0;
            for(int c = 1;c<BOARD_SIZE;c++){//row
                if(cellValuesOfBoard[r][c] != 0){//check if below cell is not empty
                    if(cellValuesOfBoard[r][index]==0){
                        cellValuesOfBoard[r][index]=cellValuesOfBoard[r][c];
                        cellValuesOfBoard[r][c]=0;
                        isMoved=true;
                    }else if(cellValuesOfBoard[r][c]==cellValuesOfBoard[r][index]){
                        cellValuesOfBoard[r][index]*=2;
                        cellValuesOfBoard[r][c]=0;
                        scoreValue+=cellValuesOfBoard[r][index];
                        score.setText(String.valueOf(scoreValue));
                        isMoved=true;
                    }else{
                        index++;
                        if (r != index) {
                            cellValuesOfBoard[r][index] = cellValuesOfBoard[r][c];
                            cellValuesOfBoard[r][c] = 0;
                            isMoved = true;
                        }
                    }
                }
            }
        }
        if(isMoved){
            updateEmptyCells();
            addRandomNum();
            updateCells();
            isGameOver();
        }
    }

    private void onSwipeDown() {
        if(isGameOver())
            return;
        boolean isMoved=false;
        for(int c = 0;c<BOARD_SIZE;c++){//column
            int index = BOARD_SIZE-1;
            for(int r = BOARD_SIZE-2;r>=0;r--){//row
                if(cellValuesOfBoard[r][c] != 0){//check if below cell is not empty
                    if(cellValuesOfBoard[index][c]==0){
                        cellValuesOfBoard[index][c]=cellValuesOfBoard[r][c];
                        cellValuesOfBoard[r][c]=0;
                        isMoved=true;
                    }else if(cellValuesOfBoard[r][c]==cellValuesOfBoard[index][c]){
                        cellValuesOfBoard[index][c]*=2;
                        cellValuesOfBoard[r][c]=0;
                        scoreValue+=cellValuesOfBoard[index][c];
                        score.setText(String.valueOf(scoreValue));
                        isMoved=true;
                    }else{
                        index--;
                        if (r != index) {
                            cellValuesOfBoard[index][c] = cellValuesOfBoard[r][c];
                            cellValuesOfBoard[r][c] = 0;
                            isMoved = true;
                        }
                    }
                }
            }
        }
        if(isMoved){
            updateEmptyCells();
            addRandomNum();
            updateCells();
            isGameOver();
        }
    }

    private void onSwipeUp() {
        if(isGameOver())
            return;
        boolean isMoved=false;
        for(int c = 0;c<BOARD_SIZE;c++){//column
            int index = 0;
            for(int r = 1;r<BOARD_SIZE;r++){//row
                if(cellValuesOfBoard[r][c] != 0){//check if below cell is not empty
                    if(cellValuesOfBoard[index][c]==0){
                        cellValuesOfBoard[index][c]=cellValuesOfBoard[r][c];
                        cellValuesOfBoard[r][c]=0;
                        isMoved=true;
                    }else if(cellValuesOfBoard[r][c]==cellValuesOfBoard[index][c]){
                        cellValuesOfBoard[index][c]*=2;
                        cellValuesOfBoard[r][c]=0;
                        scoreValue+=cellValuesOfBoard[index][c];
                        score.setText(String.valueOf(scoreValue));
                        isMoved=true;
                    }else{
                        index++;
                        if (r != index) {
                            cellValuesOfBoard[index][c] = cellValuesOfBoard[r][c];
                            cellValuesOfBoard[r][c] = 0;
                            isMoved = true;
                        }
                    }
                }
            }
        }
        if(isMoved){
            updateEmptyCells();
            addRandomNum();
            updateCells();
            isGameOver();
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
    
}