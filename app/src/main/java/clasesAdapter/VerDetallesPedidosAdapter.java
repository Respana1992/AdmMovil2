package clasesAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.birobidsa.admmovil.com.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import misclases.DetallesPedidos;

public class VerDetallesPedidosAdapter extends ArrayAdapter<DetallesPedidos> {
	private Context contexto;
	private ArrayList<DetallesPedidos> alDetPed;
	private DetallesPedidos d;
	private DecimalFormat df;
	private TextView nombre;
	private TextView descripcion1;
	private TextView descripcion2;
	private TextView descripcion3;
	private TextView descripcion4;
	
	public VerDetallesPedidosAdapter(Context context, ArrayList<DetallesPedidos> aldetalle)
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
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.layout_lis_view_ver_detalles_pedidos, parent,false);
		
		nombre = (TextView)layoutClientes.findViewById(R.id.lblNombreItem);
		descripcion1 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo1);
		descripcion2 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo2);
		descripcion3 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo3);
		descripcion4 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo4);
		
		d = alDetPed.get(position);
		df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales
		
		nombre.setText(d.getItem() + " - " + d.getNombre());
		descripcion1.setText("Línea: "+ d.getLinea() + ";  Precio Unit: $" +d.getPrecio());
		descripcion2.setText("Caj: "+ d.getCajas() +"; Uni: "+d.getUnidades()+"; Total Ud.: "+d.getTotalUnidades());
		descripcion3.setText("Sub: $"+df.format(d.getSubtotal())+"; Des: S"+ df.format(d.getDescuento()) +"; IVA: $"+df.format(d.getIva())+"; PorDes: %"+df.format(d.getPordes()));
		descripcion4.setText("Neto: $"+ df.format(d.getNeto()) +  "; Forma Vta: "+ d.getFormaVenta());
		return layoutClientes;
	}
}
