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
import misclases.Item;

public class ItemAdapterStock extends ArrayAdapter<Item>  {
	private Context contexto;
	private ArrayList<Item> alItem;
	private TextView nombre;
	private TextView descripcion,descripcion1;
	private Item i;
	private int cajas,unidades;
	
	public ItemAdapterStock(Context context, ArrayList<Item> alitem)
	{
		super(context,-1,alitem);
		this.contexto=context;
		this.alItem=alitem;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutItem = inflater.inflate(R.layout.layout_list_view_informes_stock, parent,false);
		
		nombre = (TextView)layoutItem.findViewById(R.id.lblNombre);
		descripcion = (TextView)layoutItem.findViewById(R.id.lblDescripcion);
		descripcion1 = (TextView)layoutItem.findViewById(R.id.lblDescripcion1);
		
		i = alItem.get(position);
		cajas = i.getStock()/i.getFactor();
		unidades = i.getStock()%i.getFactor();
		
		nombre.setText(i.getNombre());
		descripcion.setText(i.getItem() + " - " + "; Stock: " + i.getStock() + " ud.");
		descripcion1.setText("Cajas: " + cajas + "; Unidades: " + unidades);
		nombre.setTextColor(Color.parseColor("#000000"));
		descripcion.setTextColor(Color.parseColor("#000000"));
		descripcion1.setTextColor(Color.parseColor("#000000"));
		
		return layoutItem;
	}
}