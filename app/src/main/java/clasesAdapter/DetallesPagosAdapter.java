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

import misclases.DetallesPagos;

@SuppressLint("ViewHolder")
public class DetallesPagosAdapter extends ArrayAdapter<DetallesPagos> {
	private Context contexto;
	private ArrayList<DetallesPagos> alDetPag;
	private DetallesPagos d;
	private TextView nombre;
	private TextView descripcion;
	private DecimalFormat df;
	private double precioTotalItem;

	public DetallesPagosAdapter(Context context, ArrayList<DetallesPagos> aldetalle)
	{
		super(context,-1,aldetalle);
		this.contexto=context;
		this.alDetPag=aldetalle;
	}

	@Override
	public int getCount() {
		return alDetPag.size();
	}


	public DetallesPagos getSecuencial(int position) {
		return alDetPag.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.layout_list_view_detalles_pagos, parent,false);
		
		nombre = (TextView)layoutClientes.findViewById(R.id.lblNombreItem);
		descripcion = (TextView)layoutClientes.findViewById(R.id.lblDetalleItem);
		
		d = alDetPag.get(position);
		
		df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales
		
		//this.precioTotalItem = d.getNeto()/d.getTotalUnidades();

		nombre.setText("Noº Pago: "+d.getSecuencial());
		descripcion.setText("Tipo de Pago: "+d.getTipo()+"; Monto: "+d.getMonto()+"; Banco: "+d.getBanco()+"; Cuenta: "+ d.getCuenta()+"; Noº Cheque: "+ d.getNumchq()+"; Fecha Vencimiento de Pago: "+d.getFecha());
		return layoutClientes;
	}
}