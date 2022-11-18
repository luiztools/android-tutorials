package com.example.fernando.exemplolistview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView lstResultados;
    EditText txtPesquisa;
    Button btnBuscar;

    List<Cidade> cidades = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cidades = new ArrayList<>();
        cidades.add(new Cidade("Florianópolis", "Capital de Santa Catarina", "SC", R.drawable.florianopolis));
        cidades.add(new Cidade("Curitiba", "Capital do Paraná", "PR", R.drawable.curitiba));
        cidades.add(new Cidade("São Paulo", "Capital de São Paulo", "SP",R.drawable.sao_paulo));
        cidades.add(new Cidade("Porto Alegre", "Capital do Rio Grande do Sul", "RS", R.drawable.porto_alegre));

        txtPesquisa = (EditText)findViewById(R.id.txtPesquisa);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        lstResultados = (ListView)findViewById(R.id.lstResultados);
        lstResultados.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cidade cidade = (Cidade)lstResultados.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), "Item " + cidade.getNome(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void btnBuscar_OnClick(View v){
        String busca = txtPesquisa.getText().toString();
        List<Cidade> encontradas = new ArrayList<>();
        for(Cidade cidade : cidades){
            if(cidade.getNome().contains(busca)) encontradas.add(cidade);
        }
        CidadeAdapter adapter =
                new CidadeAdapter(getBaseContext(), R.layout.item_modelo, encontradas);
        lstResultados.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
