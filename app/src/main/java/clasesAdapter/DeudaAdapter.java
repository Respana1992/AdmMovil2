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

public class DeudaAdapter extends ArrayAdapter<Deuda>  {
	private Context contexto;
	private ArrayList<Deuda> alDeuda;
	private TextView tipodoc,numdoc,fechaemi,monto,saldo;
	private Deuda deuda;
	
	public DeudaAdapter(Context context, ArrayList<Deuda> aldeuda)
	{
		super(context,-1,aldeuda);
		this.contexto=context;
		this.alDeuda=aldeuda;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutClientes = inflater.inflate(R.layout.layout_list_view_deuda, parent,false);
		
		tipodoc = (TextView)layoutClientes.findViewById(R.id.lblTipoDoc);
		numdoc = (TextView)layoutClientes.findViewById(R.id.lblNumDoc);
		fechaemi = (TextView)layoutClientes.findViewById(R.id.lblFechaEmi);
		monto = (TextView)layoutClientes.findViewById(R.id.lblVPago);
		saldo = (TextView)layoutClientes.findViewById(R.id.lblSaldo);
		
		deuda = alDeuda.get(position);
		
		tipodoc.setText("Doc: " + deuda.getTipo());
		numdoc.setText("; Número: " + String.valueOf(deuda.getNumero()));
		monto.setText("Monto: " + String.valueOf(deuda.getMonto()));
		saldo.setText("; Saldo: " + String.valueOf(deuda.getSaldo()));
		fechaemi.setText("Fecha Emisión: " + deuda.getFechaEmi());
		return layoutClientes;
	}
}