package br.com.luiztools.chatapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    EditText txtMensagem = null;
    ListView mensagens = null;
    ArrayAdapter<String> adapter = null;
    Socket socket = null;

    private final int READ_SOCKET = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMensagem = (EditText)findViewById(R.id.txtMensagem);
        mensagens = (ListView)findViewById(R.id.mensagens);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mensagens.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, READ_SOCKET);
        }
        else
            waitMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null)
            socket.disconnect();
    }

    public void btnEnviarOnClick(View v){
        sendMessage();
    }

    private void waitMessage(){
        try {
            socket = IO.socket("http://10.0.2.2:3000");
            socket.on("chat message", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //coloca a mensagem recebida na lista
                            adapter.add(args[0].toString());
                            adapter.notifyDataSetChanged();

                            // Apenas faz um scroll para o novo item da lista
                            mensagens.smoothScrollToPosition(adapter.getCount() - 1);
                        }
                    });
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private final int WRITE_SOCKET = 2;
    private void sendMessage(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, WRITE_SOCKET);
        }
        else
            socket.emit("chat message", txtMensagem.getText().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_SOCKET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    waitMessage();
                    return;
                }
                break;
            }
            case WRITE_SOCKET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                    return;
                }
                break;
            }
        }

        Toast.makeText(this, "Sem essa permissão o app não irá funcionar. Tente novamente.", Toast.LENGTH_LONG).show();
    }
}
