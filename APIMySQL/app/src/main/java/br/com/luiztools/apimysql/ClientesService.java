package br.com.luiztools.apimysql;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by luizfduartejr on 11/08/17.
 */

public interface ClientesService {
    @GET("clientes/")
    Call<List<Cliente>> selectClientes();

    @GET("clientes/{id}")
    Call<List<Cliente>> selectCliente(@Path("id") int id);

    @FormUrlEncoded
    @POST("clientes")
    Call<Cliente> insertCliente(@Field("nome") String nome, @Field("cpf") String cpf);

    @FormUrlEncoded
    @PATCH("clientes/{id}")
    Call<Cliente> updateCliente(@Path("id") int id, @Field("nome") String nome, @Field("cpf") String cpf);

    @DELETE("clientes/{id}")
    Call<Cliente> deleteCliente(@Path("id") int id);
}
