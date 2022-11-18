package br.com.luiztools.cronometro;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by luizfduartejr on 06/08/17.
 */
public class HistoryHolder extends RecyclerView.ViewHolder {

    public TextView tempo, data;

    public HistoryHolder(View itemView) {
        super(itemView);
        tempo = (TextView) itemView.findViewById(R.id.tempo);
        data = (TextView) itemView.findViewById(R.id.data);
    }
}
