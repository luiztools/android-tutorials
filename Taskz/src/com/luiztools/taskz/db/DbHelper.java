package com.luiztools.taskz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	final String CREATE_DATABASE = "CREATE TABLE tbTarefas (id integer primary key autoincrement, tarefa text not null);";
	private static final String DATABASE_NAME = "TaskzDb.db";
	private static final int DATABASE_VERSION = 2;
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS tbTarefas");
		onCreate(db);
	}

}
