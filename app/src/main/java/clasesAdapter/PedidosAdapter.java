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
import misclases.Pedidos;

public class PedidosAdapter extends ArrayAdapter<Pedidos>{
	private Context contexto;
	private ArrayList<Pedidos> alPed;
	private Pedidos p;
	private TextView TVNombreCliente,TVSubtitulo1,TVSubtitulo2;
	private DecimalFormat df;

	public PedidosAdapter(Context context, ArrayList<Pedidos> alpedido)
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
	public Pedidos getItem(int position) {
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
		View layoutPedido = inflater.inflate(R.layout.layout_list_view_pedidos, parent,false);
		
		TVNombreCliente = (TextView)layoutPedido.findViewById(R.id.lblNombreCliente);
		TVSubtitulo1 = (TextView)layoutPedido.findViewById(R.id.lblSubtitulo1);
		TVSubtitulo2 = (TextView)layoutPedido.findViewById(R.id.lblSubtitulo2);
		
		p = alPed.get(position);
		
		df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales
		
		TVNombreCliente.setText(p.getNombreCliente());
		TVSubtitulo1.setText("Pedido No: " + p.getNumero() + " - Neto: $"+String.valueOf(df.format(p.getNeto())));
		TVSubtitulo2.setText("Fecha: "+p.getFecha());
		return layoutPedido;
	}
}
