package com.luiztools.geolocalizacao;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LocationActivity extends Activity {

    EditText txtIP;
    TextView lblCountry, lblRegion, lblCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        txtIP = (EditText)findViewById(R.id.txtIP);
        lblCountry = (TextView)findViewById(R.id.lblCountry);
        lblRegion = (TextView)findViewById(R.id.lblRegion);
        lblCity = (TextView)findViewById(R.id.lblCity);
    }

    public void btnCarregarOnClick(View view){
        try {
            String ip = txtIP.getText().toString();
            Localizacao localizacao = ClienteGeoIP.retornarLocalizacaoPorIp(ip);
            lblRegion.setText("Estado: " + localizacao.getRegion());
            lblCity.setText("Cidade: " + localizacao.getCity());
            lblCountry.setText("País: " + localizacao.getCountry());
        }
        catch(Exception ex){
            Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Item Settings selecionado", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(id == R.id.action_novo){
            Toast.makeText(this, "Item Novo selecionado", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
