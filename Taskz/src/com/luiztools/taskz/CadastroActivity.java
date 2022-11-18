package com.luiztools.taskz;

import com.luiztools.taskz.R;
import com.luiztools.taskz.db.DbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends Activity 
{
	Button btnSalvar;
	Button btnCancelar;
	EditText txtTarefa;
	boolean novatarefa;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);
        
        btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        txtTarefa = (EditText)findViewById(R.id.txtTarefa);
        
        novatarefa = TaskzApp.Tarefa.equals("") || TaskzApp.Tarefa.equals(null);
        
        if(!novatarefa)
        	txtTarefa.setText(TaskzApp.Tarefa);
        
        btnCancelar.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
        	
        });
        
        btnSalvar.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				String tarefa = txtTarefa.getText().toString();
				
				if(tarefa.equals("") || tarefa.equals(null)){
					DialogHelper.alert(v.getContext(), "Digite uma tarefa!");
					return;
				}
				
				if(novatarefa)
				DbAdapter.salvarTarefa(v.getContext(), tarefa);
				else DbAdapter.editarTarefa(v.getContext(), TaskzApp.Tarefa, tarefa);
				finish();
			}
        	
        });
	}
}
