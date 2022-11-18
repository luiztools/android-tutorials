package br.com.luiztools.cronometro;

/**
 * Created by luizfduartejr on 06/08/17.
 */

public class History {

    private String tempo;
    private String data;

    public History(String tempo, String data) {
        this.tempo = tempo;
        this.data = data;
    }

    public String getTempo(){ return this.tempo; }
    public String getData(){ return this.data; }
}

