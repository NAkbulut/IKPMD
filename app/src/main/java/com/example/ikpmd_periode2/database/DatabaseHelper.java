package com.example.ikpmd_periode2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase mSQLDB;
    private static DatabaseHelper mInstance;
    public static final String dbName = "pokedex.db";
    public static final int dbVersion = 1;		// Versie nr van je db.

    public DatabaseHelper(Context ctx) {
        super(ctx, dbName, null, dbVersion);	// gebruik de super constructor.
    }

    // synchronized … dit zorgt voor . . . . (?)
    // welk design pattern is dit ??
    public static synchronized DatabaseHelper getHelper (Context ctx){
        if (mInstance == null){
            mInstance = new DatabaseHelper(ctx);
            mSQLDB = mInstance.getWritableDatabase();
        }
        return mInstance;
    }


    @Override										// Maak je tabel met deze kolommen
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseInfo.PokemonTable.POKEMONTABLE
                + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.PokemonTable_Columns.Name + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.type1 + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.type2 + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.HP + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.ATK + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.SP_ATK + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.DEF + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.SP_DEF + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.SPD + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.Weight + " TEXT," +
                DatabaseInfo.PokemonTable_Columns.Height + " TEXT);"
        );
    }
    // CREATE TABLE course (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ects TEXT, grade TEXT);

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DatabaseInfo.PokemonTable.POKEMONTABLE);
        onCreate(db);
    }


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ){
        super(context,name,factory, version);
    }


    public void insert(String table, String nullColumnHack, ContentValues values){
        mSQLDB.insert(table, nullColumnHack, values);
    }


    public Cursor query(String table, String[] columns, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
        return mSQLDB.query(table, columns, selection, selectArgs, groupBy, having, orderBy);
    }

    public long getProfilesCount(String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        //db.close();
        System.out.println(count);
        return count;
    }



}
