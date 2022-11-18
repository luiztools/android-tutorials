package br.com.luiztools.apimysql;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtId)
    EditText txtId;

    @BindView(R.id.txtNome)
    EditText txtNome;

    @BindView(R.id.txtCpf)
    EditText txtCpf;

    public static ClientesService api = null;

    private void carregarAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainActivity.api = retrofit.create(ClientesService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        carregarAPI();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @OnClick(R.id.btnNovo)
    public void btnNovoOnClick(){
        txtId.setText("0");
        txtNome.setText("");
        txtCpf.setText("");
    }

    private final int ACTION_BUSCA = 1;
    @OnClick(R.id.btnBuscar)
    public void btnBuscarOnClick(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, ACTION_BUSCA);
        }
        else {
            int id = Integer.parseInt(txtId.getText().toString());
            //vai na API com esse id e traz o cliente
            Call<List<Cliente>> request = MainActivity.api.selectCliente(id);
            Response<List<Cliente>> response = null;
            try {
                response = request.execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("APIMYSQL", e.getMessage());
            }

            if (response != null && response.isSuccessful() && response.body().size() > 0) { //se encontrou um cliente com aquele id
                //preenche os campos do formulário com os dados do cliente
                Cliente cliente = response.body().get(0);
                txtNome.setText(cliente.Nome);
                txtCpf.setText(cliente.CPF);
            } else {
                Toast.makeText(this, "Nenhum cliente encontrado com o id " + id, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACTION_BUSCA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnBuscarOnClick();
                    return;
                }
                break;
            }
            case ACTION_EXCLUIR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnExcluirOnClick();
                    return;
                }
                break;
            }
            case ACTION_SALVAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnSalvarOnClick();
                    return;
                }
                break;
            }
        }

        Toast.makeText(this, "Sem essa permissão o app não irá funcionar. Tente novamente.", Toast.LENGTH_LONG).show();
    }

    private final int ACTION_EXCLUIR = 2;
    @OnClick(R.id.btnExcluir)
    public void btnExcluirOnClick(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, ACTION_EXCLUIR);
        }
        else {
            final int id = Integer.parseInt(txtId.getText().toString());

            //cria o dialog de confirmação
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Você tem certeza que deseja excluir este cliente?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //vai na API com esse id e exclui o cliente
                            Call request = MainActivity.api.deleteCliente(id);
                            Response response = null;
                            try {
                                response = request.execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("APIMYSQL", e.getMessage());
                            }

                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Cliente excluído com sucesso!", Toast.LENGTH_LONG).show();
                                txtId.setText("");
                                txtNome.setText("");
                                txtCpf.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Nenhum cliente encontrado com o id " + id, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            builder.create().show();
        }
    }

    private final int ACTION_SALVAR = 3;
    @OnClick(R.id.btnSalvar)
    public void btnSalvarOnClick(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, ACTION_SALVAR);
        }
        else {
            String idStr = txtId.getText().toString();
            final int id = idStr.equals("") ? 0 : Integer.parseInt(idStr);

            Response response = null;
            Call<Cliente> request = null;
            try {
                if (id > 0) { // edição
                    //PATCH na API enviando o id junto para editar
                    request = MainActivity.api.updateCliente(id, txtNome.getText().toString(), txtCpf.getText().toString());
                } else { // novo cadastro
                    //POST na API sem enviar o id que será gerado no banco
                    request = MainActivity.api.insertCliente(txtNome.getText().toString(), txtCpf.getText().toString());
                }

                response = request.execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("APIMYSQL", e.getMessage());
            }

            if (!response.isSuccessful()) {
                if (id > 0)
                    Toast.makeText(this, "Não foi encontrado nenhum cliente com esse id para atualizar ou a atualização não foi permitida.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "Não foi possível salvar esse cliente.", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Cliente salvo com sucesso!", Toast.LENGTH_LONG).show();
        }
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
