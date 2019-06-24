package dialogos;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Dialogos extends DialogFragment  {
	public boolean estado = false;
	
	public void miDialogoToastCorto(Context contexto, String mensaje)
	{
		Toast.makeText(contexto, mensaje ,Toast.LENGTH_SHORT).show();
	}


	public void miDialogoToastLargo(Context contexto, String mensaje)
	{
		Toast.makeText(contexto, mensaje ,Toast.LENGTH_LONG).show();
	}

	public void dialogoAceptar(Context contexto,String mensaje,String titulo)
	{
		AlertDialog.Builder builder = new Builder(contexto);
		builder.setTitle(titulo)
				.setMessage(mensaje)
				.setNeutralButton("Aceptar", 
					new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int id) {
                    		dialog.cancel();
                    	}
                	});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void dialogoConfirmacion(Context contexto,String mensaje,String titulo)
	{
		AlertDialog.Builder builder = new Builder(contexto);
		builder.setTitle(titulo)
				.setMessage(mensaje)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	@SuppressWarnings("deprecation")
	public void dialogoOK(Context context, String title, String message, Boolean status)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		//alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
		
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}
}
