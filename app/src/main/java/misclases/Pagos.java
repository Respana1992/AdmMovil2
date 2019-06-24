package misclases;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import procesos.MENSAJEEXCEPCIONES;

public class Pagos {
    Integer secuencial;
    String cliente;
    String tipo;
    Integer numero;
    String tipopago;
    double monto;
    String operador;
    String observacion;
    String fecha;
    String vendedor;
    String noguia;
    String hora;
    String banco;
    double numfact;
    String estado;
    private String nombreCliente;


    public Pagos()
    {
    }

    public ArrayList<DetallesPagos> agregarDetalle(String tipo, int numero, String tpopag, double monto
            , String banco, String cuenta, String numchq, String fehaven, double indice, ArrayList<DetallesPagos> alDetallesPag){
        alDetallesPag.add(new DetallesPagos(tipo,numero,tpopag,monto,banco,cuenta,
                numchq,fehaven,indice));
        return alDetallesPag;


    }

    public Pagos(int secuencial, int numero, String cliente, String tipopago, double monto, String fecha)
    {
        this.secuencial = secuencial;
        this.numero = numero;
        this.cliente = cliente;
        this.tipopago = tipopago;
        this.monto = monto;
        this.fecha = fecha;
    }

    public Pagos(int secuencial, String cliente, String tipo, int numero, double monto, String operador,
                 String observacion, String fecha,
                 String vendedor, String noguia, String hora, int numfact, String estado)
    {
        this.secuencial = secuencial;
        this.cliente = cliente;
        this.tipopago = tipopago;
        this.numero = numero;
        this.operador = operador;
        this.observacion = observacion;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.noguia = noguia;
        this.hora = noguia;
        this.numfact = numfact;
        this.estado = estado;
        this.monto = monto;
        this.fecha = fecha;
    }


    public MENSAJEEXCEPCIONES eliminaPago(SQLiteDatabase db, double secpago, String vendedor, int numfact, double montant)
    {
        String scriptSQL;
        MENSAJEEXCEPCIONES me = new MENSAJEEXCEPCIONES();
        try{

            scriptSQL = "delete from pagos where secuencial = '"+secpago+"' and vendedor = '"+vendedor+"'";
            db.execSQL(scriptSQL);

            scriptSQL = "delete from detpagos where secuencial = '"+secpago+"'";
            db.execSQL(scriptSQL);


            scriptSQL = "update deuda set credito=credito-'"+montant+"' where numero = '"+numfact+"'";
            db.execSQL(scriptSQL);

            scriptSQL = "update deuda set saldo=saldo+'"+montant+"' where numero = '"+numfact+"'";
            db.execSQL(scriptSQL);


            me = new MENSAJEEXCEPCIONES("",true);
        }catch(Exception e){
            me = new MENSAJEEXCEPCIONES("Excepción al eliminar el pago con secuencial "+secpago+" del vendedor "+vendedor+". Clase: Pagos; Método: eliminaPago --> " + e.getMessage(),false);
        }finally{
            return me;
        }
    }

    public MENSAJEEXCEPCIONES restableceEstadoPago(SQLiteDatabase db,int secpago,String vendedor)
    {
        String scriptSQL;
        MENSAJEEXCEPCIONES me = new MENSAJEEXCEPCIONES();
        try{
            scriptSQL = "update pagos set estado = 'A' where secuencial='"+ secpago +"' and vendedor = '"+ vendedor +"'";
            db.execSQL(scriptSQL);

            scriptSQL = "update detpagos set estado = 'A' where secuencial='"+ secpago +"'";
            db.execSQL(scriptSQL);
            me = new MENSAJEEXCEPCIONES("",true);
        }catch(Exception e){
            me = new MENSAJEEXCEPCIONES("Excepción al querer restablecer el pago con secuencial "+secpago+" del vendedor "+vendedor+". Clase: Pedidos; Método: eliminaPeago --> " + e.getMessage(),false);
        }finally{
            return me;
        }
    }

    public int getSecpago()
    {
        return this.secuencial;
    }
    public void setSecpago(int secauto)
    {
        this.secuencial=secauto;
    }

    public String getCliente()
    {
        return this.cliente;
    }
    public void setCliente(String cliente)
    {
        this.cliente=cliente;
    }

    public String getTipoPago()
    {
        return this.tipopago;
    }
    public void setTipoPago(String tipopago)
    {
        this.tipopago=tipo;
    }

    public double getMonto()
    {
        return this.monto;
    }
    public void setMonto(double iva)
    {
        this.monto=monto;
    }

    public String getFecha()
    {
        return this.fecha;
    }
    public void setFecha(String fecha)
    {
        this.fecha=fecha;
    }

    public int getNumero()
    {
        return this.numero;
    }
    public void setNumero(int numero)
    {
        this.numero=numero;
    }

    public double getNumfact()
    {
        return this.numfact;
    }
    public void setNumfact(double numfact)
    {
        this.numfact=numfact;
    }

    public String getNombreCliente()
    {
        return this.nombreCliente;
    }
    public void setNombreCliente(String nombrecli)
    {
        this.nombreCliente=nombrecli;
    }

}
