package clasesAdapter;

import java.util.ArrayList;
import com.birobidsa.admmovil.com.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import misclases.Clientes;

public class VerDatosClientesAdapter extends ArrayAdapter<Clientes>  {
	private Context contexto;
	private ArrayList<Clientes> alClientes;
	private TextView nombre,descripcion1,descripcion2,descripcion3;
	private Clientes c;
	
	public VerDatosClientesAdapter(Context context, ArrayList<Clientes> alclientes)
	{
		super(context,-1,alclientes);
		this.contexto=context;
		this.alClientes=alclientes;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.layout_list_view_ver_datos_clientes, parent,false);
		
		nombre = (TextView)layoutClientes.findViewById(R.id.lblNombreCliente);
		descripcion1 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo1);
		descripcion2 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo2);
		descripcion3 = (TextView)layoutClientes.findViewById(R.id.lblSubtitulo3);
		
		c = alClientes.get(position);
		
		nombre.setText(c.getCodigo()+" - "+c.getNombre());
		descripcion1.setText("Dirección: "+c.getDireccion());
		descripcion2.setText("Negocio: "+c.getNegocio()+ "; Teléfono: "+c.getTelefono());
		descripcion3.setText("Cédula: "+c.getNumeroIdentificacion()+ "; Estado: "+c.getEstado());
		nombre.setTextColor(Color.parseColor("#000000"));
		descripcion1.setTextColor(Color.parseColor("#000000"));
		descripcion2.setTextColor(Color.parseColor("#000000"));
		descripcion3.setTextColor(Color.parseColor("#000000"));
		return layoutClientes;
	}
}
