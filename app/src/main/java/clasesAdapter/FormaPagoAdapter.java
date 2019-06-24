package clasesAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.birobidsa.admmovil.com.R;

import java.util.ArrayList;

import misclases.MedioPago;

public class FormaPagoAdapter extends ArrayAdapter<MedioPago> {

    private Context contexto;
    private ArrayList<MedioPago> alMedioPago;
    private TextView nombre;
    private TextView descripcion;
    private MedioPago i;


    public FormaPagoAdapter(Context context, ArrayList<MedioPago> almediopago)
    {
        super(context,-1,almediopago);
        this.contexto=context;
        this.alMedioPago=almediopago;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutMedioPago = inflater.inflate(R.layout.layout_list_view_ver_forma_pagos, parent,false);


        nombre = (TextView)layoutMedioPago.findViewById(R.id.lblNombre);
        descripcion = (TextView)layoutMedioPago.findViewById(R.id.lblDescripcion);

        i = alMedioPago.get(position);

        nombre.setText(i.getNombre());
        descripcion.setText(" Codigo: "+i.getCodigo());
        nombre.setTextColor(Color.parseColor("#000000"));
        descripcion.setTextColor(Color.parseColor("#000000"));

        return layoutMedioPago;

    }


}
