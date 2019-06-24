package clasesAdapter;

import com.birobidsa.admmovil.com.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewMenuClienteAdapter extends BaseAdapter {
	
	Context context;
	String[] titulos;
	int[] imagenes;
	LayoutInflater inflater;
	
	public ListViewMenuClienteAdapter(Context context, String[] titulos, int[] imagenes)
	{
		this.context = context;
		this.titulos = titulos;
		this.imagenes = imagenes;
	}

	@Override
	public int getCount() {
		return titulos.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			TextView txtTitle;
			ImageView imgImg;
			
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View itemView = inflater.inflate(R.layout.layout_list_view_menu_clientes,parent,false);
			
			txtTitle = (TextView) itemView.findViewById(R.id.txtListRowMenu);
			imgImg = (ImageView) itemView.findViewById(R.id.imagenListRowMenu);
			
			txtTitle.setText(titulos[position]);
			imgImg.setImageResource(imagenes[position]);
			
			return itemView;
	}

}