package edu.cnm.deepdive.diceware.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.diceware.BuildConfig;
import edu.cnm.deepdive.diceware.model.Passphrase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DicewareService {

  @GET("passphrases/")
  Observable<List<Passphrase>> getAll(@Header("Authorization") String token);

  @GET("passphrases/{id}")
  Single<Passphrase> get(@Header("Authorization") String token,
      @Path("id") long id);

  @GET("passphrases/{key}")
  Single<Passphrase> get(@Header("Authorization") String token,
      @Path("key") String key);

  @DELETE("passphrases/{id}")
  Completable delete(@Header("Authorization") String token, @Path("id") long id);

  @PUT("passphrases/{id}")
  Single<Passphrase> put(@Header("Authorization") String token, @Path("id") long id, @Body Passphrase passphrase);

  @POST("passphrases/")
  Single<Passphrase> post(@Header("Authorization") String token, @Body Passphrase passphrase);

  static DicewareService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final DicewareService INSTANCE;
    static {
      // TODO Investigate logging interceptor issues.
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create(gson))
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(DicewareService.class);
    }

  }

}
