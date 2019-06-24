package procesos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import misclases.CEDULAECUATORIANA;

public class VARIOS {
	String scriptSQL = "";
	SQLiteDatabase db;
	Cursor cur_sec;
	Context contexto;
	
	public VARIOS()
	{
	}
	
	public VARIOS(Context c)
	{
		this.contexto=c;
	}
	
	public VARIOS(SQLiteDatabase db)
	{
		this.db=db;
	}
	
	public VARIOS(Context c,SQLiteDatabase db)
	{
		this.contexto=c;
		this.db=db;
	}
	
	public int getSecuencialPedidoXUsuario(String usuario)
	{
		int secuencial;
		scriptSQL = "select secuencial+1 secuencial from usuario_secuenciales where codigo='"+usuario+"'";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			secuencial = cur_sec.getInt(0);
		}
		else
		{
			secuencial = 0;
		}
		return secuencial;
	}
	
	public int getBodegaAutoventa(String vendedor)
	{
		int bodega;
		scriptSQL = "SELECT bodega FROM USUARIO where codigo='"+vendedor+"'";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			bodega = cur_sec.getInt(0);
		}
		else
		{
			bodega = 10;
		}
		return bodega;
	}
	
	public int getNumeroPedido()
	{
		int numped;
		scriptSQL = "select nopedido+1 nopedido from PARAMETROBODEGA";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			numped = cur_sec.getInt(0);
		}
		else
		{
			numped = 0;
		}
		return numped;
	}


	public int getNumeroPago()
	{
		int numpag;
		scriptSQL = "select numero+1 numero from SECUENCIALPAGO";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			numpag = cur_sec.getInt(0);
		}
		else
		{
			scriptSQL = "insert into SECUENCIALPAGO(secuencial,numero,estado) "+
					"values('0','0','A')";
			db.execSQL(scriptSQL);
			numpag = 1;
		}
		return numpag;
	}


	public int getSecuencialPago()
	{
		int secpag;
		scriptSQL = "select secuencial+1 secuencial from SECUENCIALPAGO";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			secpag = cur_sec.getInt(0);
		}
		else
		{
			secpag = 1;
		}
		return secpag;
	}


	public int getBodegaFac()
	{
		int bodega;
		scriptSQL = "SELECT BODFAC FROM PARAMETROS";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			bodega = cur_sec.getInt(0);
		}
		else
		{
			bodega = 10;
		}
		return bodega;
	}


	
	public int getValorIva()
	{
		int iva;
		scriptSQL = "SELECT IVA FROM PARAMETROS";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			iva = cur_sec.getInt(0);
		}
		else
		{
			iva = 12;
		}
		return iva;
	}
	
	public String getNombreCia()
	{
		String cia;
		scriptSQL = "SELECT NOMBRE_CIA FROM PARAMETROS";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			if(cur_sec.getString(0)==null)
			{
				cia = "";
			}
			else
			{
				cia = cur_sec.getString(0);
			}
		}
		else
		{
			cia = "";
		}
		return cia;
	}
	
	public String getPaginaWeb()
	{
		String pagina;
		scriptSQL = "SELECT PAGINAWEB FROM PARAMETROS";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			if(cur_sec.getString(0)==null)
			{
				pagina = "";
			}
			else
			{
				pagina = cur_sec.getString(0);
			}
		}
		else
		{
			pagina = "";
		}
		return pagina;
	}
	
	public String getNombreImpresora()
	{
		String impresora;
		scriptSQL = "SELECT IMPRESORA FROM PARAMETROS";
		cur_sec = db.rawQuery(scriptSQL, null);
		if(cur_sec.moveToFirst())
		{
			if(cur_sec.getString(0)==null)
			{
				impresora = "BlueTooth Printer";
			}
			else
			{
				impresora = cur_sec.getString(0);
			}
		}
		else
		{
			impresora = "BlueTooth Printer";
		}
		return impresora;
	}
	
	@SuppressWarnings("deprecation")
	public String dameFechaSistema()
	{
		Date fechaHora;
		String fecha;
		fechaHora = new Date();
		fecha = fechaHora.getDate() +"/"+ (fechaHora.getMonth()+1) +"/"+ (fechaHora.getYear()+1900);
		return fecha;
	}
	
	@SuppressWarnings("deprecation")
	public String dameHoraSistema()
	{
		Date fechaHora;
		String hora;
		fechaHora = new Date();
		hora = fechaHora.getHours() +":"+ fechaHora.getMinutes() +":"+ fechaHora.getSeconds();
		return hora;
	}
	
	public ArrayList<CEDULAECUATORIANA> validaCedulaEcuatoriana(String cedula)
	{
		ArrayList<CEDULAECUATORIANA> alRetorna = new ArrayList<CEDULAECUATORIANA>();
		int sumapar = 0;
		int sumaimpar = 0;
		int sumatotal = 0;
		int multiplicaimpar = 0;
		int decenasuperior = 0;
		int digitoverificador = 0;
		int verificadigitoverificador = 0;
		
		if(cedula.length()==0 || cedula==null)
		{
			alRetorna.add(new CEDULAECUATORIANA(false,"Por favor ingrese un número de cédula."));
		}
		else if(cedula.length()>0 && cedula.length()<10)
		{
			alRetorna.add(new CEDULAECUATORIANA(false,"Número de cédula incompleto, debe ingresar un número de 10 dígitos"));
		}
		else
		{
			for(int i=0; i<(cedula.length()-1); i++) //pongo menos 1 ya que el �ltimo d�gito no se calcula solo se verifica
			{
				if((i+1)%2==0)
					sumapar = sumapar + (Integer.parseInt(String.valueOf(cedula.charAt(i))) * 1);	//cuando el digito sea par
				else	//cuando el digito sea impar
				{
					multiplicaimpar = (Integer.parseInt(String.valueOf(cedula.charAt(i))) * 2);
					if(multiplicaimpar > 9)
						multiplicaimpar = multiplicaimpar - 9;
					sumaimpar = sumaimpar + multiplicaimpar;
				}
			}
			sumatotal = sumapar + sumaimpar;
			decenasuperior = (Integer.parseInt(String.valueOf(String.valueOf(sumatotal).charAt(0))) + 1) * 10;	//Para tomar la decena superior
			verificadigitoverificador = decenasuperior - sumatotal;
			digitoverificador = Integer.parseInt(String.valueOf(cedula.charAt(cedula.length()-1)));
			if(verificadigitoverificador == 10)
			{
				verificadigitoverificador = verificadigitoverificador - 10;
			}
			if(verificadigitoverificador == digitoverificador)
				alRetorna.add(new CEDULAECUATORIANA(true,"Correcto"));
			else
				alRetorna.add(new CEDULAECUATORIANA(false,"Cédula registrada es incorrecta, vuelva a ingresarla."));
		}
		return alRetorna;
	}
	
	public ArrayList<CEDULAECUATORIANA> validaRUCEcuatoriana(String ruc)
	{
		ArrayList<CEDULAECUATORIANA> alRetorna = new ArrayList<CEDULAECUATORIANA>();
		String cedula,digitosRuc;
		cedula = ruc.substring(0, 10);
		digitosRuc = ruc.substring(10,13);
		if(digitosRuc.equals("001") == false)
			alRetorna.add(new CEDULAECUATORIANA(false, "Número del RUC registrado " + digitosRuc + " es incorrecto, vuelva a ingresarla"));
		else
			alRetorna = this.validaCedulaEcuatoriana(cedula);
		return alRetorna;
	}
	
	public boolean existeCedula(String cedula)
	{
		String scriptSQL = "select * from cliente where numero_identificacion='"+cedula+"'";
		Cursor c;
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String esPrecioCajasUnidades()
	{
		String scriptSQL = "SELECT EPRECIO FROM parametros";
		Cursor c;
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			return c.getString(0);
		}
		else
		{
			return "N";
		}
	}
}