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

public class CargaDatosAdapter extends BaseAdapter  {
	Context context;
	String[] titulos;
	String[] descripcion;
	int[] imagenes;
	LayoutInflater inflater;
	
	public CargaDatosAdapter(Context context, String[] titulos, String[] descripcion, int[] imagenes)
	{
		this.context = context;
		this.titulos = titulos;
		this.descripcion=descripcion;
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
			TextView txtTitle,txtDescripcion;
			ImageView imgImg;
			
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View itemView = inflater.inflate(R.layout.layout_list_view_carga_datos,parent,false);
			
			txtTitle = (TextView) itemView.findViewById(R.id.txtListRowMenu);
			txtDescripcion = (TextView) itemView.findViewById(R.id.txtListDescripcion);
			imgImg = (ImageView) itemView.findViewById(R.id.imagenListRowMenu);
			
			txtTitle.setText(titulos[position]);
			txtDescripcion.setText(descripcion[position]);
			imgImg.setImageResource(imagenes[position]);
			
			return itemView;
	}
}
