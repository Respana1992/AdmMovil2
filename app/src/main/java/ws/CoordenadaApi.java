package ws;

import misclases.Coordenada;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CoordenadaApi {




    @POST("api/map")
    Call<Coordenada> RegistrarCordenada(@Body Coordenada coordenada);

}