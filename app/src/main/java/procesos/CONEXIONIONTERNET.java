package procesos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import dialogos.Dialogos; 

public class CONEXIONIONTERNET {
	Context contexto;
	Dialogos dialogos = new Dialogos();
	ConnectivityManager connectivity;
	@SuppressWarnings("rawtypes")
	ArrayList alretorna = new ArrayList();
	
	public CONEXIONIONTERNET(Context con,ConnectivityManager connectivity)
	{
		this.contexto = con;
		this.connectivity = connectivity;
	}
	
	public CONEXIONIONTERNET(Context con)
	{
		this.contexto = con;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> estaConectado()
	{
		if(conectadoWifi())
		{
			alretorna.clear();
			alretorna.add(true);
			alretorna.add("WIFI");
			alretorna.add("Tu Dispositivo tiene Conexión a Wifi.");
			return alretorna;
		}
		else
		{
			if(conectadoRedMovil())
			{
				alretorna.clear();
				alretorna.add(true);
				alretorna.add("MOVIL");
				alretorna.add("Tu Dispositivo tiene Conexión Móvil.");
				return alretorna;
			}else
			{
				alretorna.clear();
				alretorna.add(false);
				alretorna.add("NO");
				alretorna.add("Tu Dispositivo no tiene Conexión a Internet.");
				return alretorna;
			}
		}
	}
	
	public Boolean conectadoWifi()
	{
		if (connectivity != null)
		{
			//ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null)
			{
				if (info.isConnected())
				{
					return true;
				}
			}
		}
		return false;
	}
		 
	public Boolean conectadoRedMovil()
	{
		if (connectivity != null)
		{
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null)
			{
				if (info.isConnected())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean executeCmd(String cmd, boolean sudo){
	    boolean tieneAccesoIP = false;
		try {
	        Process p;
	        if(!sudo)
	            p= Runtime.getRuntime().exec(cmd);
	        else{
	            p= Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
	        }
	        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        @SuppressWarnings("unused")
			String s;
	        while ((s = stdInput.readLine()) != null) {
	        	tieneAccesoIP = true;
	        }
	        p.destroy();
	        return tieneAccesoIP;
	    } catch (Exception e) {
	    	//e.printStackTrace();
	    	tieneAccesoIP = false;
	    	return tieneAccesoIP;
	    }
	}
}