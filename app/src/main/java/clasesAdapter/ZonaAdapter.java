package clasesAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.birobidsa.admmovil.com.R;

import java.util.ArrayList;

import misclases.Banco;

public class ZonaAdapter extends ArrayAdapter<Banco> {
	private Context contexto;

	private TextView cod;
	private TextView nombre;
	private Banco c;
	private Banco deuda;
	private ArrayList<Banco> alDeuda;

	public ZonaAdapter(Context context, ArrayList<Banco> aldeuda)
	{
		super(context,-1,aldeuda);
		this.contexto=context;
		this.alDeuda=aldeuda;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.activity_main_ver_bancos_rp, parent,false);



		cod = (TextView)layoutClientes.findViewById(R.id.lblNumDoc);
		nombre = (TextView)layoutClientes.findViewById(R.id.lblFechaVen);
		//saldo = (TextView)layoutClientes.findViewById(R.id.lblSaldo);


		deuda = alDeuda.get(position);

		cod.setText("Código: " + String.valueOf(deuda.getCodigo()));
		nombre.setText("Nombre: "+deuda.getNombre());
		//saldo.setText("Saldo: "+ String.valueOf(deuda.getSaldo()));
		//nombre.setTextColor(Color.parseColor("#000000"));
		//descripcion.setTextColor(Color.parseColor("#000000"));
		return layoutClientes;
	}
}
