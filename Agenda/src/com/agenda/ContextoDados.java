package com.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class ContextoDados extends SQLiteOpenHelper {

	/** O nome do arquivo de base de dados no sistema de arquivos */
	private static final String NOME_BD = "Agenda";
	/** A versão da base de dados que esta classe compreende. */
	private static final int VERSAO_BD = 2;
	private static final String LOG_TAG = "Agenda";
	/** Mantém rastreamento do contexto que nós podemos carregar SQL */
	private final Context contexto;
	
	public ContextoDados(Context context) {
		super(context, NOME_BD, null, VERSAO_BD);
		this.contexto = context;
		}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String[] sql = contexto.getString(R.string.ContextoDados_onCreate).split("\n");
		db.beginTransaction();
		
		try 
		{
			// Cria a tabela e testa os dados
			ExecutarComandosSQL(db, sql);
			db.setTransactionSuccessful();
		} 
		catch (SQLException e) 
		{
			Log.e("Erro ao criar as tabelas e testar os dados", e.toString());
		} 
		finally 
		{
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.w(LOG_TAG, "Atualizando a base de dados da versão " + oldVersion + " para " + newVersion + ", que destruirá todos os dados antigos");
		String[] sql = contexto.getString(R.string.ContextoDados_onUpgrade).split("\n");
		db.beginTransaction();
		
		try 
		{
			ExecutarComandosSQL(db, sql);
			db.setTransactionSuccessful();
		}
		catch (SQLException e) 
		{
			Log.e("Erro ao atualizar as tabelas e testar os dados", e.toString());
			throw e;
		} 
		finally 
		{
			db.endTransaction();
		}
		
		// Isto é apenas didático. Na vida real, você terá de adicionar novas colunas e não apenas recriar o mesmo banco
		onCreate(db);
	}
	
	/**
	* Executa todos os comandos SQL passados no vetor String[]
	* @param db A base de dados onde os comandos serão executados
	* @param sql Um vetor de comandos SQL a serem executados
	*/
	private void ExecutarComandosSQL(SQLiteDatabase db, String[] sql)
	{
		for( String s : sql )
			if (s.trim().length()>0)	
				db.execSQL(s);
	}
	
	/** Retorna um ContatosCursor ordenado
	* @param critério de ordenação
	*/
	public ContatosCursor RetornarContatos(ContatosCursor.OrdenarPor ordenarPor) 
	{
		String sql = ContatosCursor.CONSULTA + (ordenarPor == ContatosCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
		SQLiteDatabase bd = getReadableDatabase();
		ContatosCursor cc = (ContatosCursor) bd.rawQueryWithFactory(new ContatosCursor.Factory(), sql, null, null);
		cc.moveToFirst();
		return cc;
	}
	
	public long InserirContato(String nome, String telefone, String endereco)
	{
		SQLiteDatabase db = getReadableDatabase();
		
		try
		{
			ContentValues initialValues = new ContentValues();
			initialValues.put("Nome", nome);
			initialValues.put("Telefone", telefone);
			initialValues.put("Endereco", endereco);
			return db.insert("Contatos", null, initialValues);
		}
		finally
		{
			db.close();
		}
	}
	
	public static class ContatosCursor extends SQLiteCursor
	{
		public static enum OrdenarPor{
			NomeCrescente,
			NomeDecrescente
		}
		
		private static final String CONSULTA = "SELECT * FROM Contatos ORDER BY Nome ";
		
		private ContatosCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) 
		{
			super(db, driver, editTable, query);
		}
		
		private static class Factory implements SQLiteDatabase.CursorFactory
		{
			@Override
			public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) 
			{
				return new ContatosCursor(db, driver, editTable, query);
			}
		}
		
		public long getID()
		{
			return getLong(getColumnIndexOrThrow("ID"));
		}
		
		public String getNome()
		{
			return getString(getColumnIndexOrThrow("Nome"));
		}
		
		public String getEndereco() 
		{
			return getString(getColumnIndexOrThrow("Endereco"));
		}
		
		public String getTelefone() 
		{
			return getString(getColumnIndexOrThrow("Telefone"));
		}
	}
}
