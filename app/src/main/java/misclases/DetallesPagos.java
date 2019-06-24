package misclases;

public class DetallesPagos {
    Integer secuencial;
    String tipo;
    Integer numero;
    String tipopag;
    double monto;
    String banco;
    String cuenta;
    String numchq;
    String fechaven;
    double indice;
    String vendedor;

    public DetallesPagos()
    {
    }

    public DetallesPagos(String tipo, int numero, String tpopag, double monto
            , String banco, String cuenta, String numchq, String fehaven, double indice)
    {
        this.tipo=tipo;
        this.numero=numero;
        this.tipopag=tpopag;
        this.monto=monto;
        this.banco=banco;
        this.cuenta=cuenta;
        this.numchq=numchq;
        this.fechaven=fehaven;
        this.indice=indice;
    }

    public DetallesPagos(int seciencial, String tipo, int numero, String tpopag, double monto
            , String banco, String cuenta, String numchq, String fehaven, double indice)
    {
        this.secuencial=seciencial;
        this.tipo=tipo;
        this.numero=numero;
        this.tipopag=tpopag;
        this.monto=monto;
        this.banco=banco;
        this.cuenta=cuenta;
        this.numchq=numchq;
        this.fechaven=fehaven;
        this.indice=indice;
    }

    public DetallesPagos(int seciencial, String tipo, int numero, String tpopag, double monto
            , String banco, String cuenta, String numchq, String fehaven, double indice, String vendedor)
    {
        this.secuencial=seciencial;
        this.tipo=tipo;
        this.numero=numero;
        this.tipopag=tpopag;
        this.monto=monto;
        this.banco=banco;
        this.cuenta=cuenta;
        this.numchq=numchq;
        this.fechaven=fehaven;
        this.indice=indice;
        this.vendedor = vendedor;
    }


    public int getSecuencial() {
        return (int) this.secuencial;
    }

    public String getTipo() {
        return this.tipopag;
    }

    public double getMonto() {
        return  this.monto;
    }

    public String getBanco() {
        return this.banco;
    }

    public String getCuenta() {
        return this.cuenta;
    }

    public String getNumchq() {
        return this.numchq;
    }

    public String getFecha() {
        return this.fechaven;
    }

}
