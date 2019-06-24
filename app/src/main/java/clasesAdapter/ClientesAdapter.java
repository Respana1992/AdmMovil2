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

import misclases.Clientes;

public class ClientesAdapter extends ArrayAdapter<Clientes> {
	private Context contexto;
	private ArrayList<Clientes> alClientes;
	private TextView nombre;
	private TextView descripcion;
	private Clientes c;
	
	public ClientesAdapter(Context context, ArrayList<Clientes> alclientes)
	{
		super(context,-1,alclientes);
		this.contexto=context;
		this.alClientes=alclientes;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
 		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.layout_list_view_ver_clientes, parent,false);
		
		nombre = (TextView)layoutClientes.findViewById(R.id.lblNombre);
		descripcion = (TextView)layoutClientes.findViewById(R.id.lblDescripcion);
		
		c = alClientes.get(position);
		
		nombre.setText("Cliente: " +c.getNombre());
		descripcion.setText("Cod: "+c.getCodigo()+"Negocio: "+c.getNegocio()+"Tipo Cliente: "+c.getTipoCliente()+"C.I.: "+c.getNumeroIdentificacion()+"Telfno.: "+c.getTelefono());
		nombre.setTextColor(Color.parseColor("#000000"));
		descripcion.setTextColor(Color.parseColor("#000000"));
		return layoutClientes;
	}
}