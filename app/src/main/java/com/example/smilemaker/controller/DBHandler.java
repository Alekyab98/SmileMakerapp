package com.example.smilemaker.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smilemaker.FacebookFeedModal;
import com.example.smilemaker.modal.Jokes;
import com.example.smilemaker.modal.User;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "smile";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "user";

    // below variable is for our id column.
    private static final String ID_COL = "id";


    private static final String FNAME_COL = "fname";
    private static final String UNAME_COL = "uname";

    private static final String pass1 = "pass1";
    private static final String pass2 = "pass2";

    Context context;

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + UNAME_COL + " TEXT PRIMARY KEY, "
                + FNAME_COL + " TEXT, "
                + pass1 + " TEXT, "
                + pass2 + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
        System.out.println(TABLE_NAME + " created");

        String CREATE_TABLE_WALLPOST =
                "CREATE TABLE " + PostHandler.WALLPOST_TBL + "("
                        + PostHandler.WID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + PostHandler.UNAME_COL + " TEXT, "
                        + PostHandler.FNAME_COL + " TEXT, "
                        + PostHandler.CONTENT_COL + " TEXT, "
                        + PostHandler.LIKE_COL + " TEXT"
                        + ")";

        db.execSQL(CREATE_TABLE_WALLPOST);
        System.out.println(PostHandler.WALLPOST_TBL + " created");

        String query1 = "CREATE TABLE " + CommentHandler.COMMENTS_TBL + " ("
                + CommentHandler.CID_COL + " TEXT PRIMARY KEY, "
                + PostHandler.WID_COL + " TEXT, "
                + CommentHandler.CONTENT_COL + " TEXT, "
                + CommentHandler.DATE_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query1);
        System.out.println(CommentHandler.COMMENTS_TBL + " created");

        //insertJokes();


    }

    public void insertJokes() {
        ArrayList<FacebookFeedModal> jokes1 = Jokes.jokes();
        PostHandler db = new PostHandler(context);
        for (int i = 0; i < jokes1.size(); i++) {
            FacebookFeedModal jo = jokes1.get(i);
            db.addNewPost(jo.getuName(), jo.getPostDescription(), context, jo.getAuthorName());
        }

    }

    public User getUserFromUid(String uid) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where uname='" + uid + "'", null);

        User u = null;

        if (cursorCourses.moveToFirst()) {
            // on below line we are adding the data from cursor to our array list.
            System.out.println("test: " + cursorCourses.getString(2));
            u = new User(cursorCourses.getString(0),
                    cursorCourses.getString(1),
                    cursorCourses.getString(2));
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return u;
    }


    // this metzhod is use to add new course to our sqlite database.
    public void addNewCourse(String uname, String fname, String pass1, String pass2) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(UNAME_COL, uname);
        values.put(FNAME_COL, fname);
        values.put("pass1", pass1);
        values.put("pass2", pass2);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        insertJokes();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

