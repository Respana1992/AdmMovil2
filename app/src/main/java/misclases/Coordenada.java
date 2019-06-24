package misclases;

public  class Coordenada {
    //@SerializedName("id")
    private String lat;
    private String lng;
    private String usuario;
    private boolean visit;
    private String codcliente;

    public String getLat() {
        return lat;
    }
    public String getLng() {
        return lng;
    }
    public String getUsuario() {
        return usuario;
    }
    public boolean getVisita() {return visit;}
    public String getCodCliente() {
        return codcliente;
    }

    public void setLat(String _lat) {
        lat = _lat;
    }
    public void setLng(String _lng) {
        this.lng = _lng;
    }
    public void setUsuario(String _usuario) {
        lat = _usuario;
    }
    public void setVisita(boolean _visit) { visit = _visit;}
    public void setCodcliente(String _codcliente) {
        codcliente = _codcliente;
    }


    public Coordenada(String usuario, String codcliente,  boolean visit, String lat, String lng){
        this.usuario = usuario;
        this.codcliente = codcliente;
        this.visit = visit;
        this.lat = lat;
        this.lng = lng;

    }


}
