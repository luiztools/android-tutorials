package br.com.luiztools.cronometro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by luizfduartejr on 06/08/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {

    private final List<History> histories;

    public HistoryAdapter(List<History> clientes) {
        this.histories = clientes;
    }

    public void addHistory(History history){
        histories.add(0, history);
        notifyDataSetChanged();
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        History history = histories.get(position);
        holder.data.setText(history.getData());
        holder.tempo.setText(history.getTempo());
    }

    @Override
    public int getItemCount() {
        return histories != null ? histories.size() : 0;
    }
}