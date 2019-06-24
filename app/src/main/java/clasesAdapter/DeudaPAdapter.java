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

import misclases.Deuda;

public class DeudaPAdapter extends ArrayAdapter<Deuda> {
	private Context contexto;

	private TextView numdoc;
	private TextView fechaven,saldo,tipodoc;
	private Deuda c;
	private Deuda deuda;
	private ArrayList<Deuda> alDeuda;

	public DeudaPAdapter(Context context, ArrayList<Deuda> aldeuda)
	{
		super(context,-1,aldeuda);
		this.contexto=context;
		this.alDeuda=aldeuda;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.activity_main_ver_deudas_rp, parent,false);



		numdoc = (TextView)layoutClientes.findViewById(R.id.lblNumDoc);
		fechaven = (TextView)layoutClientes.findViewById(R.id.lblFechaVen);
		saldo = (TextView)layoutClientes.findViewById(R.id.lblSaldo);
		tipodoc= (TextView)layoutClientes.findViewById(R.id.lbl_tipodocfac);

		deuda = alDeuda.get(position);

		numdoc.setText("Número: " + String.valueOf(deuda.getNumero()));
		fechaven.setText("Fecha Vencimiento: "+deuda.getFechaVen());
		saldo.setText("Saldo: "+ String.valueOf(deuda.getSaldo()));
		tipodoc.setText("Doc: "+ String.valueOf(deuda.getTipo()));
		//nombre.setTextColor(Color.parseColor("#000000"));
		//descripcion.setTextColor(Color.parseColor("#000000"));
		return layoutClientes;
	}
}
