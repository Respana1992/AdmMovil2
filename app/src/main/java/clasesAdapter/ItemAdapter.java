package clasesAdapter;

import com.birobidsa.admmovil.com.R;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
//import android.widget.TextView;
import misclases.Item;

public class ItemAdapter extends ArrayAdapter<Item> {
	private Context contexto;
	private ArrayList<Item> alItem;
	private TextView nombre;
	private TextView descripcion;
	private Item i;
	
	public ItemAdapter(Context context, ArrayList<Item> alitem)
	{
		super(context,-1,alitem);
		this.contexto=context;
		this.alItem=alitem;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutItem = inflater.inflate(R.layout.layout_list_view_ver_clientes, parent,false);
		
		nombre = (TextView)layoutItem.findViewById(R.id.lblNombre);
		descripcion = (TextView)layoutItem.findViewById(R.id.lblDescripcion);
		
		i = alItem.get(position);
		
		nombre.setText(i.getNombre());
		descripcion.setText(i.getItem() + "; Stock: " + i.getStock() + " ud; Precio: $" + i.getPrecioPed());
		nombre.setTextColor(Color.parseColor("#000000"));
		descripcion.setTextColor(Color.parseColor("#000000"));
		
		return layoutItem;
	}
}