package com.example.variablegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.variablegame.QuizContrat.*;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {


    private static  final  String DATABASE_NAME = "VariableGame.db";
    private static  final  int DATABASE_VERSION = 1;

    private  SQLiteDatabase db;
    
    public QuizDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        final  String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANS_NUMBER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private  void  fillQuestionTable(){

        Question q1 = new Question("Summer = True ", "Number", "String", "Boolean", "Incorrect", 3);
        addQuestion(q1);
        Question q2 = new Question("name = \"Bruno Mars\"", "Number", "String", "Boolean", "Incorrect", 2);
        addQuestion(q2);
        Question q3 = new Question("Exciting = false", "Number", "String", "Boolean", "Incorrect", 4);
        addQuestion(q3);
        Question q4 = new Question("Price = 14", "Number", "String", "Boolean", "Incorrect", 1);
        addQuestion(q4);
        Question q5 = new Question("Friend = Lady Gaga", "Number", "String", "Boolean", "Incorrect", 2);
        addQuestion(q5);
        Question q6 = new Question("Weight = 21", "Number", "String", "Boolean", "Incorrect", 1);
        addQuestion(q6);
}

    private  void  addQuestion(Question question) {

        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANS_NUMBER, question.getAnsNumber());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestion(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do{
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION )));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnsNumber(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANS_NUMBER)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}