package com.luiztools.taskz.db;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {

	private SQLiteDatabase db;
	private DbHelper dbHelper;
	
	final static String SELECT_TAREFAS = "SELECT tarefa FROM tbTarefas";
	final static String INSERT_TAREFA = "INSERT INTO tbTarefas(tarefa) VALUES ('@tarefa')";
	final static String UPDATE_TAREFA = "UPDATE tbTarefas SET tarefa = '@tarefanova' WHERE tarefa = '@tarefavelha';";
	final static String DELETE_TAREFA = "DELETE FROM tbTarefas WHERE tarefa = '@tarefa';";
	
	public DbAdapter(Context _context) {
		dbHelper = new DbHelper(_context);
		}
	
	private DbAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
		}
	
	private void close() {
		db.close();
		}
	
	private Cursor execQuery(String sql)
	{
		return db.rawQuery(sql, null);
	}
	
	private void execCommand(String sql)
	{
		db.execSQL(sql);
	}
	
	public static Vector<String> retornarTarefas(Context contexto){
		Vector<String> lista = new Vector<String>();
		
		DbAdapter adapter = new DbAdapter(contexto);
		adapter.open();
		
		try{
			Cursor c = adapter.execQuery(SELECT_TAREFAS);
			
			if(c.moveToFirst()){
				do{
					lista.add(c.getString(0));
				}
				while(c.moveToNext());
			}
			c.close();
		}
		finally
		{
			adapter.close();
		}
		
		return lista;
	}
	
	public static void salvarTarefa(Context contexto, String tarefa){
		DbAdapter adapter = new DbAdapter(contexto);
		adapter.open();
		
		//evitando SQL Injection
		tarefa = tarefa.replace("'", "");
		
		adapter.execCommand(INSERT_TAREFA.replace("@tarefa", tarefa));
		adapter.close();
	}
	
	public static void editarTarefa(Context contexto, String tarefaVelha, String tarefaNova){
		DbAdapter adapter = new DbAdapter(contexto);
		adapter.open();
		
		//evitando SQL Injection
		tarefaNova = tarefaNova.replace("'", "");
		
		adapter.execCommand(UPDATE_TAREFA.replace("@tarefanova", tarefaNova).replace("@tarefavelha", tarefaVelha));
		adapter.close();
	}
	
	public static void excluirTarefa(Context contexto, String tarefa){
		DbAdapter adapter = new DbAdapter(contexto);
		adapter.open();
		
		//evitando SQL Injection
		tarefa = tarefa.replace("'", "");
		
		adapter.execCommand(DELETE_TAREFA.replace("@tarefa", tarefa));
		adapter.close();
	}
}
