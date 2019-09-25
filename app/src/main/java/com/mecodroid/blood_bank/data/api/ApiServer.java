package com.mecodroid.blood_bank.data.api;

import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.categories.Categories;
import com.mecodroid.blood_bank.data.model.cities.CityDataModel;
import com.mecodroid.blood_bank.data.model.contact.Contact;
import com.mecodroid.blood_bank.data.model.donationRequestCreate.DonationRequestsCreate;
import com.mecodroid.blood_bank.data.model.donationRequestNotifications.DonationRequestNotifications;
import com.mecodroid.blood_bank.data.model.donationRequests.DonationRequests;
import com.mecodroid.blood_bank.data.model.favourite.FavouriteModel;
import com.mecodroid.blood_bank.data.model.generalResponse.GeneralResponse;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;
import com.mecodroid.blood_bank.data.model.login.Login;
import com.mecodroid.blood_bank.data.model.newpassword.NewPassword;
import com.mecodroid.blood_bank.data.model.notifications.Notifications;
import com.mecodroid.blood_bank.data.model.notificationsSettings.NotificationsSettings;
import com.mecodroid.blood_bank.data.model.notifications_count.NotificationsCount;
import com.mecodroid.blood_bank.data.model.posts.Posts;
import com.mecodroid.blood_bank.data.model.profile.Profile;
import com.mecodroid.blood_bank.data.model.profileedit.ProfileEdit;
import com.mecodroid.blood_bank.data.model.register.Register;
import com.mecodroid.blood_bank.data.model.resetpassword.ResetPassword;
import com.mecodroid.blood_bank.data.model.setting.Setting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServer {

    @POST("signup")
    @FormUrlEncoded
    Call<Register> addNewAcount(@Field("name") String name,
                                @Field("email") String email,
                                @Field("birth_date") String birth_date,
                                @Field("city_id") String city_id,
                                @Field("phone") String phone,
                                @Field("donation_last_date") String donation_last_date,
                                @Field("password") String password,
                                @Field("password_confirmation") String password_confirmation,
                                @Field("blood_type_id") String blood_type);

    @POST("login")
    @FormUrlEncoded
    Call<Login> addLogin(@Field("phone") String phone,
                         @Field("password") String password);

    @POST("reset-password")
    @FormUrlEncoded
    Call<ResetPassword> resetPassword(@Field("phone") String phone);

    @POST("new-password")
    @FormUrlEncoded
    Call<NewPassword> inputNewPassword(@Field("password") String password,
                                       @Field("password_confirmation") String password_confirmation,
                                       @Field("pin_code") String pin_code,
                                       @Field("phone") String phone);

    @GET("governorates")
    Call<Governorates> getGovernorates();

    @GET("cities")
    Call<CityDataModel> getCities(@Query("governorate_id") long governorate_id);

    @GET("blood-types")
    Call<BloodTypes> getBloodTypes();

    @GET("posts")
    Call<Posts> getPosts(@Query("api_token") String api_token,
                         @Query("page") int page);



    @GET("posts")
    Call<Posts> getPostFilter(@Query("api_token") String api_token,
                               @Query("page") int page,
                               @Query("keyword") String keyword,
                               @Query("category_id") Integer category_id);

    @GET("categories")
    Call<Categories> getCategories();

    @POST("post-toggle-favourite")
    @FormUrlEncoded
    Call<FavouriteModel> setFavourite(@Field("post_id") int post_id,
                                      @Field("api_token") String api_token);


    @GET("my-favourites")
    Call<Posts> getMyFavourite(@Query("api_token") String api_token,
                               @Query("page") int page);

    @GET("donation-requests")
    Call<DonationRequests> getDonationRequests(@Query("api_token") String api_token,
                                               @Query("page") int page);


    @GET("donation-requests")
    Call<DonationRequests> getDonationRequestsFilter(@Query("api_token") String api_token,
                                                     @Query("blood_type_id") int blood_type_id,
                                                     @Query("governorate_id") int governorate_id,
                                                     @Query("page") int page);


    @POST("donation-request/create")
    @FormUrlEncoded
    Call<DonationRequestsCreate> CreateDonationRequests(@Field("api_token") String api_token,
                                                        @Field("patient_name") String patient_name,
                                                        @Field("patient_age") String patient_age,
                                                        @Field("blood_type_id") String blood_type_id,
                                                        @Field("bags_num") String bags_num,
                                                        @Field("hospital_name") String hospital_name,
                                                        @Field("hospital_address") String hospital_address,
                                                        @Field("city_id") String city_id,
                                                        @Field("phone") String phone,
                                                        @Field("notes") String notes,
                                                        @Field("latitude") double latitude,
                                                        @Field("longitude") double longitude);


    @GET("donation-request")
    Call<DonationRequestNotifications> getDonationDetails(@Query("api_token") String api_token,
                                             @Query("donation_id") String donation_id);
    @POST("profile")
    @FormUrlEncoded
    Call<ProfileEdit> onUpdate(@Field("name") String name,
                               @Field("email") String email,
                               @Field("birth_date") String birth_date,
                               @Field("city_id") String city_id,
                               @Field("phone") String phone,
                               @Field("donation_last_date") String donation_last_date,
                               @Field("password") String password,
                               @Field("password_confirmation") String password_confirmation,
                               @Field("blood_type_id") String blood_type_id,
                               @Field("api_token") String api_token);

    @POST("profile")
    @FormUrlEncoded
    Call<Profile> getProfile(@Field("api_token") String api_token);


    @GET("notifications")
    Call<Notifications> getNotifications(@Query("api_token") String api_token);

    @GET("notifications")
    Call<Notifications> getNotificationsList(@Query("api_token") String api_token,
                                                 @Query("page") int page);


    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationsSettings> getNotificationsSettings(@Field("api_token") String api_token);

    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationsSettings> ChangeNotificationsSettings(@Field("api_token") String api_token,
                                                            @Field("governorates[]") List<Integer> governorates,
                                                            @Field("blood_types[]") List<Integer> blood_types);

    @POST("contact")
    @FormUrlEncoded
    Call<Contact> SendContact(@Field("api_token") String api_token,
                              @Field("title") String title,
                              @Field("message") String message);

    @GET("notifications-count")
    Call<NotificationsCount> getNotificationsCount(@Query("api_token") String api_token);


    @POST("remove-token")
    @FormUrlEncoded
    Call<GeneralResponse> RemoveToken(@Field("token") String token,
                                      @Field("api_token") String api_token);

    @POST("signup-token")
    @FormUrlEncoded
    Call<GeneralResponse> getRegisterNotificationToken(@Field("token") String token,
                                                       @Field("api_token") String api_token,
                                                       @Field("type") String platform);
    @GET("settings")
    Call<Setting> getSettings(@Query("api_token") String api_token);

}
