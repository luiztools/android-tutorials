package br.com.luiztools.forcavendas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    EditText txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        txtData = (EditText)findViewById(R.id.txtData);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtData.setText(sdf.format(new Date()));
    }
}
