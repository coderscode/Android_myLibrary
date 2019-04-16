package virtuosonetsoft.pagingoffline.pagingLibrary.api;




import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;


/**
 * Created by Ahmed Abd-Elmeged on 2/13/2018.
 */
public interface GithubService {
    @FormUrlEncoded
    @POST("AllUserServices?api_type=userHistory")
    Observable<List<HistoryFroimDb>>showPastBookingApi(@Field("auth_key") String auth_key, @Field("key_id") String key_id, @Field("count") String count);
    static GithubService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://freeparking-env.ap-south-1.elasticbeanstalk.com/")
                .client(provideOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GithubService.class);
  }
    static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        return okhttpClientBuilder.build();
    }
}
