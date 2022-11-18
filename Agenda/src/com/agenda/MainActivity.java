package com.agenda;

import com.agenda.ContextoDados.ContatosCursor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    
	Button btnSalvar, btnCancelar, btnNovo;
	EditText txtNome, txtEndereco, txtTelefone;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarregarInterfaceListagem();
    }
    
    public void CarregarInterfaceListagem()
    {
    	setContentView(R.layout.main);
        
        //configurando o botão de criar novo cadastro
        btnNovo = (Button)findViewById(R.id.btnNovo);
        btnNovo.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				CarregarInterfaceCadastro();
			}});
        
        CarregarLista(this);
    }
    
    public void CarregarInterfaceCadastro()
    {
    	setContentView(R.layout.cadastro);
    	
    	//configurando o botão de cancelar cadastro
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				CarregarInterfaceListagem();
			}});
        
        //configurando o formulário de cadastro
        txtNome = (EditText)findViewById(R.id.txtNome);
        txtEndereco = (EditText)findViewById(R.id.txtEndereco);
        txtTelefone = (EditText)findViewById(R.id.txtTelefone);
        
        //configurando o botão de salvar
        btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				SalvarCadastro();
			}});
    }
    
    public void SalvarCadastro()
    {
    	ContextoDados db = new ContextoDados(this);
		db.InserirContato(txtNome.getText().toString(), txtTelefone.getText().toString(), txtEndereco.getText().toString());
		setContentView(R.layout.main);
		CarregarLista(this);
    }
    
    public void CarregarLista(Context c)
    {
    	ContextoDados db = new ContextoDados(c);
        ContatosCursor cursor = db.RetornarContatos(ContatosCursor.OrdenarPor.NomeCrescente);
        
        for( int i=0; i <cursor.getCount(); i++)
        {
        	cursor.moveToPosition(i);
        	ImprimirLinha(cursor.getNome(), cursor.getTelefone());
        }
    }
    
    public void ImprimirLinha(String nome, String telefone)
    {
    	TextView tv = (TextView)findViewById(R.id.listaContatos);
    	
    	if(tv.getText().toString().equalsIgnoreCase("Nenhum contato cadastrado."))
    		tv.setText("");
    	
    	tv.setText(tv.getText() + "\r\n" + nome + " - " + telefone);
    }
}