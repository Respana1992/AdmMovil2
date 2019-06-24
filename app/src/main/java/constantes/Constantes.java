/**
 * 
 */
package constantes;

import android.Manifest;

public class Constantes {
	public String tituloVentana = "ADM MOVIL";
	public String DATABASE_NAME = "ADMMOVIL.db";
	public int DATABASE_VERSION = 23;
	public String tipoAdmin = "ADMIN";
	public String tipoVendedor = "VEN";
	public String IPWS = "";
	public String IPWSB = "";
	public String IPAPI = "";
	public String URLWS = "";
	public String RUC = "";
	public String SUCURSAL = "";
	public String SECUENCIAL = "";
	public String FECHAEMI = "";
	public String FECHAVEN = "";
	public String MONTO = "";
	public String SALDO = "";


	public String NAMESPACE = "http://birobid.com/webservice/admmovil";

	public static String[] PERMISOS_UBICACION = new String[]{
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};

	public static final int MULTIPLE_PERMISSIONS = 10;
}
