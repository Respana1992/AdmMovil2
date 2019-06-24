package misclases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dialogos.Dialogos;

public class Clientes {
	//PROPIEDADES
	String codigo;
	String nombre;
	String negocio;
	String representa;
	String tipo_identificacion;
	String numero_identificacion;
	String direccion;
	String telefonos;
	String email;
	String tipo_cliente;
	String provincia;
	String canton;
	String parroquia;
	String sector;
	String ruta;
	String cupo;
	String grupo_descuento;
	int orden;
	String cod_frecuencia_visita;
	String credito;
	int dia;
	String fecha_desde;
	String fecha_ingreso;
	String hora_ingreso;
	String fecha_ult_com;
	String td_credito;
	int dias_credito;
	String usuario; //es el vendedor
	String formapag;
	String iva;
	String con_final;
	String clase;
	String observacion;
	String tipo_negocio;
	String ejex;
	String ejey;
	String vendedor_aux;
	String fecha_modifica;
	String hora_modifica;
	String usuario_modifica;
	String fecha_elimina;
	String hora_elimina;
	String usuario_elimina;
	String estado;
	String cltenuevo;
	String categoria;
	String registrado;
	String zona;
	String tipoctaclte;
	String fax;
	String codigoSistema;
	String descargado;
	String clavefe;
	String referencia;

	
	public Clientes()
	{
	}
	
	//CONSTRUCTORES
	public Clientes(String codigo,String nombre,String negocio,String representa,String tipoiden,String numiden,String direccion,
					String telefono,String email,String tipocli,String provincia,String canton,String parroquia,String sector,
					String ruta,String cupo,String grupodscto,int orden,String frecvis,String credito,int dia,String fecdesde,
					String fecingreso,String horaingreso,String fecultcom,String tdcredito,int diascredito,String usuario,String formapag,
					String iva,String confinal,String clase,String observacion,String tiponego,String ejex,String ejey,String venaux,
					String fecmodifica,String horamodifica,String usumodifica,String fecelimina,String horaelimina,String usuelimina,
					String estado,String clientenuevo,String descargado,String clavefe)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.negocio=negocio;
		this.representa=representa;
		this.tipo_identificacion=tipoiden;
		this.numero_identificacion=numiden;
		this.direccion=direccion;
		this.telefonos=telefono;
		this.email=email;
		this.tipo_cliente=tipocli;
		this.provincia=provincia;
		this.canton=canton;
		this.parroquia=parroquia;
		this.sector=sector;
		this.ruta=ruta;
		this.cupo=cupo;
		this.grupo_descuento=grupodscto;
		this.orden=orden;
		this.cod_frecuencia_visita=frecvis;
		this.credito=credito;
		this.dia=dia;
		this.fecha_desde=fecdesde;
		this.fecha_ingreso=fecingreso;
		this.hora_ingreso=horaingreso;
		this.fecha_ult_com=fecultcom;
		this.td_credito=tdcredito;
		this.dias_credito=diascredito;
		this.usuario=usuario;
		this.formapag=formapag;
		this.iva=iva;
		this.con_final=confinal;
		this.clase=clase;
		this.observacion=observacion;
		this.tipo_negocio=tiponego;
		this.ejex=ejex;
		this.ejey=ejey;
		this.vendedor_aux=venaux;
		this.fecha_modifica=fecmodifica;
		this.hora_modifica=horamodifica;
		this.usuario_modifica=usumodifica;
		this.fecha_elimina=fecelimina;
		this.hora_elimina=horaelimina;
		this.usuario_elimina=usuelimina;
		this.estado=estado;
		this.cltenuevo=clientenuevo;
		this.descargado=descargado;
		this.clavefe=clavefe;
	}
	
	
	public Clientes(String codigo,String nombre,String negocio,String numiden,String direccion,
			String telefono,String tipocli,String categoria,String provincia,String canton,String parroquia,String sector,
			String ruta,String cupo,String grupodscto,int orden,String frecvis,String credito,int dia,String fecdesde,
			String estado,String tdcredito,int diascredito,String usuario,String iva,
			String observacion,String tiponego,String registrado,String fechatrabajo,String zona,String email,String clientenuevo,
			String ejex,String ejey,String codigosistema,String tipoctaclte,String fax,String formapag)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.negocio=negocio;
		this.numero_identificacion=numiden;
		this.direccion=direccion;
		this.telefonos=telefono;
		this.tipo_cliente=tipocli;
		this.categoria=categoria;
		this.provincia=provincia;
		this.canton=canton;
		this.parroquia=parroquia;
		this.sector=sector;
		this.ruta=ruta;
		this.cupo=cupo;
		this.grupo_descuento=grupodscto;
		this.orden=orden;
		this.cod_frecuencia_visita=frecvis;
		this.credito=credito;
		this.dia=dia;
		this.fecha_desde=fecdesde;
		this.estado=estado;
		this.td_credito=tdcredito;
		this.dias_credito=diascredito;
		this.usuario=usuario;
		this.iva=iva;
		this.observacion=observacion;
		this.tipo_negocio=tiponego;
		this.registrado=registrado;
		this.fecha_ingreso=fechatrabajo;
		this.zona=zona;
		this.email=email;
		this.cltenuevo=clientenuevo;
		this.ejex=ejex;
		this.ejey=ejey;
		this.codigoSistema=codigosistema;
		this.tipoctaclte=tipoctaclte;
		this.fax=fax;
		this.formapag=formapag;
	}

	public Clientes(String codigo,String nombre,String negocio,String numiden,String direccion,
					String telefono,String tipocli,String categoria,String provincia,String canton,String parroquia,String sector,
					String ruta,String cupo,String grupodscto,int orden,String frecvis,String credito,int dia,String fecdesde,
					String estado,String tdcredito,int diascredito,String usuario,String iva,
					String observacion,String tiponego,String registrado,String fechatrabajo,String zona,String email,String clientenuevo,
					String ejex,String ejey,String codigosistema,String tipoctaclte,String fax,String formapag,String referencia)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.negocio=negocio;
		this.numero_identificacion=numiden;
		this.direccion=direccion;
		this.telefonos=telefono;
		this.tipo_cliente=tipocli;
		this.categoria=categoria;
		this.provincia=provincia;
		this.canton=canton;
		this.parroquia=parroquia;
		this.sector=sector;
		this.ruta=ruta;
		this.cupo=cupo;
		this.grupo_descuento=grupodscto;
		this.orden=orden;
		this.cod_frecuencia_visita=frecvis;
		this.credito=credito;
		this.dia=dia;
		this.fecha_desde=fecdesde;
		this.estado=estado;
		this.td_credito=tdcredito;
		this.dias_credito=diascredito;
		this.usuario=usuario;
		this.iva=iva;
		this.observacion=observacion;
		this.tipo_negocio=tiponego;
		this.registrado=registrado;
		this.fecha_ingreso=fechatrabajo;
		this.zona=zona;
		this.email=email;
		this.cltenuevo=clientenuevo;
		this.ejex=ejex;
		this.ejey=ejey;
		this.codigoSistema=codigosistema;
		this.tipoctaclte=tipoctaclte;
		this.fax=fax;
		this.formapag=formapag;
		this.referencia=referencia;
	}
	
	public Clientes(String codigo,String nombre,String negocio,String direccion,String tiponego,String tipoiden,String numiden,
			String email,String tipocli,String grupodscto,String frecvis,String ruta,String estado,String fecingreso,String horaingreso,
			String clienteNuevo,String confinal)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.negocio=negocio;
		this.direccion=direccion;
		this.tipo_negocio=tiponego;
		this.tipo_identificacion=tipoiden;
		this.numero_identificacion=numiden;
		this.email=email;
		this.tipo_cliente=tipocli;
		this.grupo_descuento=grupodscto;
		this.cod_frecuencia_visita=frecvis;
		this.ruta=ruta;
		this.estado=estado;
		this.fecha_ingreso=fecingreso;
		this.hora_ingreso=horaingreso;
		this.cltenuevo=clienteNuevo;
		this.con_final=confinal;
	}
	public Clientes(String codigo,String nombre,String direccion)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.direccion=direccion;
	}
	public Clientes(String codigo,String nombre,String negocio,String tipo, String numeroid, String telef)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.negocio=negocio;
		this.tipo_cliente=tipo;
		this.numero_identificacion=numeroid;
		this.telefonos=telef;
	}
	
	//METODOS GETTER Y SETTER
	public String getCodigo()
	{
		return this.codigo;
	}
	public void setCodigo(String codigo)
	{
		this.codigo=codigo;
	}
	
	
	public String getNombre()
	{
		return this.nombre;
	}
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	
	public String getNegocio()
	{
		return this.negocio;
	}
	public void setNegocio(String negocio)
	{
		this.negocio=negocio;
	}
	
	
	public String getRepresenta()
	{
		return this.representa;
	}
	public void setRepresenta(String representa)
	{
		this.representa=representa;
	}
	
	
	public String getTipoIdentificacion()
	{
		return this.tipo_identificacion;
	}
	public void setTipoIdentificacion(String tipo_iden)
	{
		this.tipo_identificacion=tipo_iden;
	}
	
	
	public String getNumeroIdentificacion()
	{
		return this.numero_identificacion;
	}
	public void setNumeroIdentificacion(String num_iden)
	{
		this.numero_identificacion=num_iden;
	}
	
	
	public String getDireccion()
	{
		return this.direccion;
	}
	public void setDireccion(String direccion)
	{
		this.direccion=direccion;
	}
	
	
	public String getTelefono()
	{
		return this.telefonos;
	}
	public void setTelefono(String telefono)
	{
		this.telefonos=telefono;
	}
	
	
	public String getEmail()
	{
		return this.email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	
	
	public String getTipoCliente()
	{
		return this.tipo_cliente;
	}
	public void setTipoCliente(String tipocli)
	{
		this.tipo_cliente=tipocli;
	}
	
	
	public String getProvincia()
	{
		return this.provincia;
	}
	public void setProvincia(String provincia)
	{
		this.provincia=provincia;
	}
	
	
	public String getCanton()
	{
		return this.canton;
	}
	public void setCanton(String canton)
	{
		this.canton=canton;
	}
	
	
	public String getParroquia()
	{
		return this.parroquia;
	}
	public void setParroquia(String parroquia)
	{
		this.parroquia=parroquia;
	}
	
	
	public String getSector()
	{
		return this.sector;
	}
	public void setSector(String sector)
	{
		this.sector=sector;
	}
	
	
	public String getRuta()
	{
		return this.ruta;
	}
	public void setRuta(String ruta)
	{
		this.ruta=ruta;
	}
	
	
	public String getCupo()
	{
		return this.cupo;
	}
	public void setCupo(String cupo)
	{
		this.cupo=cupo;
	}
	
	
	public String getGrupoDescuento()
	{
		return this.grupo_descuento;
	}
	public void setGrupoDescuento(String grupo_desc)
	{
		this.grupo_descuento=grupo_desc;
	}
	
	
	public int getOrden()
	{
		return this.orden;
	}
	public void setOrden(int orden)
	{
		this.orden=orden;
	}
	
	
	public String getFrecuenciaVisita()
	{
		return this.cod_frecuencia_visita;
	}
	public void setFrecuenciaVisita(String frec_vis)
	{
		this.cod_frecuencia_visita=frec_vis;
	}
	
	
	public String getCredito()
	{
		return this.credito;
	}
	public void setCredito(String credito)
	{
		this.credito=credito;
	}
	
	
	public int getDia()
	{
		return this.dia;
	}
	public void setDia(int dia)
	{
		this.dia=dia;
	}
	
	
	public String getFechaDesde()
	{
		return this.fecha_desde;
	}
	public void setFechaDesde(String fesdesde)
	{
		this.fecha_desde=fesdesde;
	}
	
	
	public String getFechaIngreso()
	{
		return this.fecha_ingreso;
	}
	public void setFechaIngreso(String fecing)
	{
		this.fecha_ingreso=fecing;
	}
	
	
	public String getHoraIngreso()
	{
		return this.hora_ingreso;
	}
	public void setHoraIngreso(String horing)
	{
		this.hora_ingreso=horing;
	}
	
	
	public String getFechaUltimaCompra()
	{
		return this.fecha_ult_com;
	}
	public void setFechaUltimaCompra(String fecultcom)
	{
		this.fecha_ult_com=fecultcom;
	}
	
	
	public String getTdCredito()
	{
		return this.td_credito;
	}
	public void setTdCredito(String tdcre)
	{
		this.td_credito=tdcre;
	}
	
	
	public int getDiasCredito()
	{
		return this.dias_credito;
	}
	public void setDiasCredito(int diascre)
	{
		this.dias_credito=diascre;
	}
	
	
	public String getUsuario()
	{
		return this.usuario;
	}
	public void setUsuario(String usuario)
	{
		this.usuario=usuario;
	}
	
	
	public String getFormaPago()
	{
		return this.formapag;
	}
	public void setFormaPago(String forpag)
	{
		this.formapag=forpag;
	}
	
	
	public String getIva()
	{
		return this.iva;
	}
	public void setIva(String iva)
	{
		this.iva=iva;
	}
	
	
	public String getConFinal()
	{
		return this.con_final;
	}
	public void setConFinal(String confinal)
	{
		this.con_final=confinal;
	}
	
	
	public String getClase()
	{
		return this.clase;
	}
	public void setClase(String clase)
	{
		this.clase=clase;
	}
	
	
	public String getObservacion()
	{
		return this.observacion;
	}
	public void setObservacion(String observacion)
	{
		this.observacion=observacion;
	}
	
	
	public String getTipoNegocio()
	{
		return this.tipo_negocio;
	}
	public void setTipoNegocio(String tipo_nego)
	{
		this.tipo_negocio=tipo_nego;
	}
	
	
	public String getEjex()
	{
		return this.ejex;
	}
	public void setEjex(String ejex)
	{
		this.ejex=ejex;
	}
	
	
	public String getEjey()
	{
		return this.ejey;
	}
	public void setEjey(String ejey)
	{
		this.ejey=ejey;
	}
	
	
	public String getVendedorAux()
	{
		return this.vendedor_aux;
	}
	public void setVendedorAux(String venaux)
	{
		this.vendedor_aux=venaux;
	}
	
	
	public String getFechaModifica()
	{
		return this.fecha_modifica;
	}
	public void setFechaModifica(String fecmod)
	{
		this.fecha_modifica=fecmod;
	}
	
	
	public String getHoraModifica()
	{
		return this.hora_modifica;
	}
	public void setHoraModifica(String horamod)
	{
		this.hora_modifica=horamod;
	}
	
	
	public String getUsuarioModifica()
	{
		return this.usuario_modifica;
	}
	public void setUsuarioModifica(String usumod)
	{
		this.usuario_modifica=usumod;
	}
	
	
	public String getFechaElimina()
	{
		return this.fecha_elimina;
	}
	public void setFechaElimina(String feceli)
	{
		this.fecha_elimina=feceli;
	}
	
	
	public String getHoraElimina()
	{
		return this.hora_elimina;
	}
	public void setHoraElimina(String horeli)
	{
		this.hora_elimina=horeli;
	}
	
	
	public String getUsuarioElimina()
	{
		return this.usuario_elimina;
	}
	public void setUsuarioElimina(String usueli)
	{
		this.usuario_elimina=usueli;
	}
	
	
	public String getEstado()
	{
		return this.estado;
	}
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	public String getClienteNuevo()
	{
		return this.cltenuevo;
	}
	public void setClienteNuevo(String clienteNuevo)
	{
		this.cltenuevo=clienteNuevo;
	}
	
	public String getDescargado()
	{
		return this.descargado;
	}
	public void setDescargado(String descargado)
	{
		this.descargado=descargado;
	}
	
	public String getClavefe()
	{
		return this.clavefe;
	}
	public void setClavefe(String clavefe)
	{
		this.clavefe=clavefe;
	}

	public String getReferencia()
	{
		return this.referencia;
	}
	public void setReferencia(String referencia)
	{
		this.referencia=referencia;
	}


	public String getZona()
	{
		return this.zona;
	}
	public void setZona(String zona)
	{
		this.zona=zona;
	}
	
	
	
	@SuppressWarnings("finally")
	public int getNumClienteXUsuario(Context contexto,SQLiteDatabase db, String usuario)
	{
		Dialogos dialogo = new Dialogos();
		int n = 0;
		try{
			String scriptSQL;
			Cursor c;
			scriptSQL = "select count(*) reg from cliente where usuario = '"+usuario+"'";
			c = db.rawQuery(scriptSQL,null);
			if(c.moveToFirst())
			{
				if(c.getString(0)=="" || c.getString(0)==null)
					n = 0;
				else
					n = Integer.parseInt(c.getString(0));	
			}
			else
				n = 0;			
		}catch(Exception e){
			dialogo.miDialogoToastCorto(contexto, e.getMessage());
			n = -1;
		}finally{
			return n;
		}
	}
	
	@SuppressWarnings("finally")
	public boolean getClienteTienePrecioXItem(Context contexto,SQLiteDatabase db, String usuario,String fecha)
	{
		String[] tmp;
		String fechatabla;
		tmp = fecha.split("/");
		if(Integer.parseInt(tmp[1]) < 10)
			tmp[1]= "0"+tmp[1];
		fechatabla = tmp[2]+""+tmp[1]+""+tmp[0];
		
		Dialogos dialogo = new Dialogos();
		boolean tiene = false;
		try{
			String scriptSQL;
			Cursor c;
			scriptSQL = "SELECT * FROM ITEMXCLIENTE WHERE " + 
						"(substr(FECHADESDE,7,4) || substr(FECHADESDE,4,2) ||substr(FECHADESDE,1,2))<='"+ fechatabla +"' " +
						"AND (substr(FECHAHASTA,7,4) || substr(FECHAHASTA,4,2) ||substr(FECHAHASTA,1,2))>='"+ fechatabla +"'";
			c = db.rawQuery(scriptSQL,null);
			if(c.moveToFirst())
			{
				tiene = true;
			}
			else
				tiene = false;
		}catch(Exception e){
			dialogo.miDialogoToastCorto(contexto, e.getMessage());
			tiene = false;
		}finally{
			return tiene;
		}
	}
}