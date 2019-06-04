package com.veloeye.api;

import com.veloeye.model.data.AddBikeResponse;
import com.veloeye.model.data.Bike;
import com.veloeye.model.data.DeleteResponse;
import com.veloeye.model.data.ForgetPasswordResponse;
import com.veloeye.model.data.LoginResponse;
import com.veloeye.model.data.Manufacturer;
import com.veloeye.model.data.RegisterResponse;
import com.veloeye.model.data.ScanResponse;
import com.veloeye.model.data.StatusUpdateResponse;
import com.veloeye.model.data.StolenBike;
import com.veloeye.model.data.StolenBikeLocation;
import com.veloeye.model.data.StolenScansResponse;
import com.veloeye.model.data.SurveyResponse;
import com.veloeye.model.data.TransferBikeToNEWResponse;
import com.veloeye.model.data.TransferCheckResponse;
import com.veloeye.model.data.UpdateBikeImageResponse;
import com.veloeye.model.data.UpdateBikeResponse;
import com.veloeye.model.data.UpdateProfileResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Florin Mihalache on 14.09.2015.
 */
public interface VeloeyeAPI {

    @POST("/login.php")
    void login(
            @Query("email") String email,
            @Query("password") String password,
            Callback<List<LoginResponse>> callback);

    @POST("/login_police_account.php")
    void loginForPolice(
            @Query("email") String email,
            @Query("password") String password,
            Callback<List<LoginResponse>> callback);

    @POST("/create_account.php")
    void createAccount(
            @Query("firstname") String firstname,
            @Query("surname") String surname,
            @Query("email") String email,
            @Query("gender") String gender,
            @Query("mobile") String mobile,
            @Query("password") String password,
            Callback<List<RegisterResponse>> callback);

    @POST("/create_police_account.php")
    void createAccountForPolice(
            @Query("firstname") String firstname,
            @Query("surname") String surname,
            @Query("email") String email,
            @Query("mobile") String mobile,
            @Query("password") String password,
            Callback<List<RegisterResponse>> callback);

    @POST("/getbikes.php")
    void getBikes(
            @Query("userid") String userid,
            Callback<List<Bike>> callback);

    @POST("/getbike.php")
    void getBikes(
            @Query("userid") String userid,
            @Query("bikeid") String bikeid,
            Callback<List<Bike>> callback);

    @POST("/updatebike.php")
    void updateBike(
            @Query("stickercode") String stickercode,
            @Query("bikeid") String bikeid,
            @Query("userid") String userid,
            @Query("status") String status,
            Callback<List<UpdateBikeResponse>> callback);

    @POST("/bikestatusupdate.php")
    void updateBikeStatus(
            @Query("userid") String userid,
            @Query("qrcode") String qrcode,
            @Query("status") String status,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            Callback<List<StatusUpdateResponse>> callback);

    @POST("/survey.php")
    void sendSurvey(
            @Query("userid") String userid,
            @Query("q1") int q1,
            @Query("q2") int q2,
            @Query("q3") int q3,
            @Query("q4") int q4,
            Callback<List<SurveyResponse>> callback);

    @POST("/scan.php")
    void sendQrCode(
            @Query("userid") String userid,
            @Query("qrcode") String qrcode,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("auth") int auth,
            Callback<List<ScanResponse>> callback);

    @POST("/addbike.php")
    void addBike(
            @Query("stickercode") String stickercode,
            @Query("userid") String userid,
            @Query("man") String man,
            @Query("model") String model,
            @Query("color") String color,
            @Query("year") String year,
            @Query("framenumber") String framenumber,
            Callback<List<AddBikeResponse>> callback);

    @POST("/getmanufacturers.php")
    void getManufacturers(Callback<List<Manufacturer>> callback);

    @Multipart
    @POST("/imguploader.php")
    void updateBikeImage(
            @Part("code") String stickercode,
           // @Part("userid") String userid,
            @Part("bike") String bikeid,
           // @Part("image") TypedFile image,
            @Part("img") TypedFile imagePart ,
            Callback<List<UpdateBikeImageResponse>> callback);
           // Callback<JSONObject> callback);

    @POST("/update_profile.php")
    void updateProfile(
            @Query("userid") String userid,
            @Query("fname") String firstname,
            @Query("sname") String surname,
            @Query("email") String email,
            @Query("gender") String gender,
            @Query("mobile") String mobile,
            @Query("password") String password,
            Callback<List<UpdateProfileResponse>> callback);
    @POST("/transfer_usercheck.php")
    void transferBike(
            @Query("userid") String userid,
            @Query("bike") String bike,

            Callback<List<TransferCheckResponse>> callback);

    @POST("/updatebike.php")
    void deleteBike(
            @Query("stickercode") String stickercode,
            @Query("bikeid") String bikeid,
            @Query("userid") String userid,
            @Query("man") Integer man,
            @Query("model") String model,
            @Query("color") String color,
            @Query("year") String year,
            @Query("framenumber") String framenumber,
            @Query("status") String status,
            Callback<List<DeleteResponse>> callback);



    @POST("/transfer_code.php")
    void transferBikeToNEW(
            @Query("userid") String userid,
            @Query("txcode") String transfercode,
            Callback<List<TransferBikeToNEWResponse>> callback);

    @POST("/stolenscans.php")
    void stolenscans(
            @Query("userid") String userid,
            @Query("qrcode") String qrcode,
            @Query("longitude") double longitude,
            @Query("latitude")double latitude,
            Callback<List<StolenScansResponse>> callback);

    @POST("/stolen_local.php")
    void stolenlocal(
            @Query("userid") String userid,
            @Query("long") double longitude,
            @Query("lat")double latitude,
            Callback<List<StolenBike>> callback);

    @POST("/bikestatusupdate.php")
    void apiCallForStolenBike(
            @Query("userid") String userid,
            @Query("status") String status,
            @Query("qrcode") String qrcode,
            @Query("long") double longitude,
            @Query("lat")double latitude,
            Callback<List<StolenBikeLocation>> callback);

    @POST("/forgot.php")

    void apiCallForForgetpassword(
            @Query("number") String password,

            Callback<List<ForgetPasswordResponse>> callback);
}
