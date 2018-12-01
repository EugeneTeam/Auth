package interfaces;


import models.Data;
import models.Repository;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import java.util.List;

public interface UserClient {
    @GET("user")
    Call<Data> getUser(@Header("Authorization") String auth);

//    @GET("users")
//    Call<Object> getUsers(@Header("Authorization") String auth);

    @GET("users/{username}/repos")
    Call<List<Repository>> getRepository(@Header("Authorization") String auth, @Path("username") String username);
 }
