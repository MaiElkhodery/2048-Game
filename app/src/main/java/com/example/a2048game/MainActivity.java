package com.example.a2048game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.color.DynamicColors;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = findViewById(R.id.scoreValue_textView);
        bestScore = findViewById(R.id.bestScoreValue_textview);
        AppCompatImageButton startButton = findViewById(R.id.startButton);
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
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRandomNum();
                addRandomNum();
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
    }

    public void makeCellStyle(AppCompatButton button,int num){
       if(num == 0){
           button.setText(" ");
       }else{
           button.setText(num+"");
       }
       switch (num){
           case 0:
               button.setTextColor(Color.BLACK);
               button.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.score_best));
               break;
           case 2: button.setBackgroundColor(Color.rgb(240,240,240));
               button.setTextColor(Color.BLACK);
               break;
           case 4: button.setBackgroundColor(Color.rgb(255,255,224));
               button.setTextColor(Color.BLACK);
               break;
           case 8: button.setBackgroundColor(Color.rgb(255,200,100));
               button.setTextColor(Color.WHITE);
               break;
           case 16: button.setBackgroundColor(Color.rgb(255,140,30));
               button.setTextColor(Color.WHITE);
               break;
           case 32: button.setBackgroundColor(Color.rgb(255,100,65));
               button.setTextColor(Color.WHITE);
               break;
           case 64: button.setBackgroundColor(Color.rgb(250,80,100));
               button.setTextColor(Color.WHITE);
               break;
           case 128: button.setBackgroundColor(Color.rgb(255,220,0));
               button.setTextColor(Color.WHITE);
               break;
           case 256: button.setBackgroundColor(Color.rgb(240,240,0));
               button.setTextColor(Color.BLACK);
               break;
           case 512: button.setBackgroundColor(Color.rgb(245,245,0));
               button.setTextColor(Color.BLACK);

               break;
           case 1024: button.setBackgroundColor(Color.rgb(250,250,0));
               button.setTextColor(Color.BLACK);
               break;
           default: button.setBackgroundColor(Color.rgb(255,255,0));
               button.setTextColor(Color.BLACK);
       }
    }
    public void addRandomNum(){
        if(emptySpaces.isEmpty())
            return;
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

    public void restart(){
        for(int row = 0; row<4; row++){
            for(int column = 0; column<4; column++){
                int[] emptyPositions={row,column};
                emptySpaces.add(emptyPositions);
                cellValuesOfBoard[row][column]=0;
                makeCellStyle(cellTextViewOfBoard[row][column],cellValuesOfBoard[row][column]);
            }
        }
        scoreValue =0;
        score.setText(""+scoreValue);
        bestScoreValue = sharedPreferences.getLong(high_score,0);
        bestScore.setText(""+bestScoreValue);
        addRandomNum();
        addRandomNum();
    }
    boolean isGameOver(){
        if(cellValuesOfBoard[0][0]==0)
            return false;
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
        return true;
    }

}