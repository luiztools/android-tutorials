package com.luiztools.taskz;

import java.util.Vector;

import com.luiztools.taskz.R;
import com.luiztools.taskz.db.DbAdapter;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	ListView listView1;
	Vector<String> items;
	
	private void AtualizarListagem(){
		int layoutID = android.R.layout.simple_list_item_1;
        items = DbAdapter.retornarTarefas(this);
        
        if(items.isEmpty())
        	items.add("Não existem tarefas");
        	
    	listView1 = (ListView)findViewById(android.R.id.list);
    	ArrayAdapter<String> aa = new ArrayAdapter<String>(this, layoutID , items);
    	listView1.setAdapter(aa);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        AtualizarListagem();
        registerForContextMenu(listView1);
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	AtualizarListagem();
    };
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
    	super.onListItemClick(l, v, position, id);
    	TaskzApp.Tarefa = l.getItemAtPosition(position).toString();
    	l.performLongClick(); 
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        menu.add(1, 1, 1, " Nova Tarefa");
        menu.add(1, 2, 2, " Editar Tarefa");
	    menu.add(1, 3, 3, " Excluir Tarefa");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:{
            	TaskzApp.Tarefa = "";
            	startActivity(new Intent(MainActivity.this, CadastroActivity.class));
                return true;
            }
            case 2:{
            	startActivity(new Intent(MainActivity.this, CadastroActivity.class));
                return true;
            }
            case 3:{
                DialogHelper.confirm(this, "Tem certeza que deseja excluir esta tarefa?", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DbAdapter.excluirTarefa(getApplicationContext(), TaskzApp.Tarefa);
						startActivity(new Intent(MainActivity.this, MainActivity.class));
						finish();
					}

                });
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    super.onCreateOptionsMenu(menu);
	    
	    menu.add(1, 1, 1, "Nova Tarefa");
	    menu.add(1, 2, 2, "Sair");
	
	    return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	    final int itemId = item.getItemId();
	    
	    switch(itemId) {
		    case 1: {
		    	TaskzApp.Tarefa = "";
		    	startActivity(new Intent(MainActivity.this, CadastroActivity.class));
		    	return true;
		    }
		    case 2:{ 
		    	TaskzApp.Tarefa = "";
		    	DialogHelper.confirm(this, "Tem certeza que deseja sair da aplicação?", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}

			    });
			    return true;
		    }
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
    }
}