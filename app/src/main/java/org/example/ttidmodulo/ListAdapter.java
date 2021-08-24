package org.example.ttidmodulo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter {
    Activity context;
    private final List<String> ListaTAGS;
    private final List<String> ListaTagIso;
    private final List<String> ListaTID;
//    private final List<String> ListaTagSaldo;

    public ListAdapter(Activity context, List<String> ListaTAGS, List<String> ListaTagIso, List<String> ListaTID) {
        super(context, R.layout.moves_list_item, ListaTAGS);
        this.context = context;
        this.ListaTAGS = ListaTAGS;
        this.ListaTagIso = ListaTagIso;
        this.ListaTID = ListaTID;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView=inflater.inflate(R.layout.tags, null,true);

        TextView NumeroTAG = (TextView) rowView.findViewById(R.id.txtTagNumber);
        TextView IsoTag = (TextView)rowView.findViewById(R.id.txtProtocoloISO);
        TextView txtTID = (TextView)rowView.findViewById(R.id.txtTID);


        NumeroTAG.setText(ListaTAGS.get(position));
        IsoTag.setText(ListaTagIso.get(position));
        txtTID.setText(ListaTID.get(position));
//        TagSaldo.setText(ListaTagSaldo.get(position));
//        TagEstado.setText(ListaTagEstado.get(position));
//        TagTipo.setText(ListaTagTipo.get(position));

        return rowView;

    };
}
