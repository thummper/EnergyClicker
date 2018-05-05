package aronb.energyclicker;

import android.util.Xml;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Aron on 05/05/2018.
 */

public interface RetroApi {


    @GET("current/{long},{lat}")
    Call<ResponseBody> getWeather(@Path("long") String longi, @Path("lat") String lat, @Query("app_id") String appID, @Query("app_key") String appKey);


}
