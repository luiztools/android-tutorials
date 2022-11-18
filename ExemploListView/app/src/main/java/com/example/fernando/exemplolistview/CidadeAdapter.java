package com.example.fernando.exemplolistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CidadeAdapter extends ArrayAdapter<Cidade> {
    private List<Cidade> items;

    public CidadeAdapter(Context context, int textViewResourceId, List<Cidade> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            Context ctx = getContext();
            LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_modelo, null);
        }
        Cidade cidade = items.get(position);
        if (cidade != null) {
            ((TextView) v.findViewById(R.id.lblCidade)).setText(cidade.getNome());
            ((TextView) v.findViewById(R.id.lblUF)).setText("/" + cidade.getUf());
            ((TextView) v.findViewById(R.id.lblDescricao)).setText(cidade.getDescricao());
            ((ImageView) v.findViewById(R.id.imgCidade)).setImageResource(cidade.getIdImagem());
        }
        return v;
    }
}
