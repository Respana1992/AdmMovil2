package misclases;

public class Empresa {
    String ruc;
    String sucursal;

    public Empresa(String ruc,String sucursal){
        this.ruc = ruc;
        this.sucursal = sucursal;
    }

    public String getRuc()
    {
        return this.ruc;
    }
    public void setRuc(String ruc)
    {
        this.ruc=ruc;
    }

    public  String getSucursal(){return  this.sucursal;}
    public  void setSucursal(String sucursal){this.sucursal=sucursal;}
}
