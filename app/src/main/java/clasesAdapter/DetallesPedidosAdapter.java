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

import misclases.DetallesPedidos;

@SuppressLint("ViewHolder")
public class  DetallesPedidosAdapter extends ArrayAdapter<DetallesPedidos> {
	private Context contexto;
	private ArrayList<DetallesPedidos> alDetPed;
	private DetallesPedidos d;
	private TextView nombre;
	private TextView descripcion;
	private DecimalFormat df;
	private double precioTotalItem;
	
	public DetallesPedidosAdapter(Context context, ArrayList<DetallesPedidos> aldetalle)
	{
		super(context,-1,aldetalle);
		this.contexto=context;
		this.alDetPed=aldetalle;
	}

	@Override
	public int getCount() {
		return alDetPed.size();
	}

	@Override
	public DetallesPedidos getItem(int position) {
		return alDetPed.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.layout_list_view_detalles_pedidos, parent,false);
		
		nombre = (TextView)layoutClientes.findViewById(R.id.lblNombreItem);
		descripcion = (TextView)layoutClientes.findViewById(R.id.lblDetalleItem);
		
		d = alDetPed.get(position);
		
		df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales
		
		this.precioTotalItem = d.getNeto()/d.getTotalUnidades();				

		nombre.setText(d.getNombre());
		descripcion.setText(d.getItem()+"; Cajas: "+d.getCajas()+"; Uni: "+d.getUnidades()+"; PVP: "+String.valueOf(df.format(this.precioTotalItem))+"; Neto: "+ String.valueOf(df.format(d.getNeto()))+"; Es cambio: "+ d.getEsCambio());
		return layoutClientes;
	}
}