package clasesAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.birobidsa.admmovil.com.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import misclases.Pagos;



public class PagosAdapter extends ArrayAdapter<Pagos> {
    private Context contexto;
    private ArrayList<Pagos> alPed;
    private Pagos p;
    private TextView TVNombreCliente,TVSubtitulo1,TVSubtitulo2;
    private DecimalFormat df;


    public  PagosAdapter(Context context, ArrayList<Pagos> alpedido)
    {
        super(context,-1,alpedido);
        this.contexto=context;
        this.alPed=alpedido;
    }

    @Override
    public int getCount() {
        return alPed.size();
    }

    @Override
    public Pagos getItem(int position) {
        return alPed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutPedido = inflater.inflate(R.layout.layout_list_view_ver_pagos, parent,false);

        TVNombreCliente = (TextView)layoutPedido.findViewById(R.id.lblNombreCliente);
        TVSubtitulo1 = (TextView)layoutPedido.findViewById(R.id.lblSubtitulo1);
        TVSubtitulo2 = (TextView)layoutPedido.findViewById(R.id.lblSubtitulo2);

        p = alPed.get(position);

        df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales

        TVNombreCliente.setText(p.getCliente());
        TVSubtitulo1.setText("Pago No: " + p.getNumero() + " - Monto: $"+String.valueOf(df.format(p.getMonto())));
        TVSubtitulo2.setText("Fecha: "+p.getFecha());
        return layoutPedido;
    }
}
