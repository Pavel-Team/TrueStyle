package ru.dm.android.truestyle.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import ru.dm.android.truestyle.api.request.*
import ru.dm.android.truestyle.api.response.*
import ru.dm.android.truestyle.model.Registration

interface Api {

    //API проверки актуальной версии
    @GET("version/info")
    suspend fun getCurrentAppVersion(): Response<AppVersion>

    //API регистрации
    @POST("auth/signup")
    suspend fun registerUser(@Body registration: Registration): Response<TextMessage>

    @GET("auth/check/username")
    suspend fun checkUsername(@Query("username") username: String): Response<TextMessage>

    @GET("auth/check/email")
    suspend fun checkEmail(@Query("username") username: String): Response<TextMessage>

    //API авторизации
    @POST("auth/signin")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<Auth>

    @POST("user/resetPassword")
    suspend fun resetPassword(@Query("email") email: String): Response<TextMessage>

    @POST("user/savePassword")
    suspend fun savePassword(@Body newPassword: NewPasswordRequest): Response<TextMessage>

    //API рекомендаций (главная страница)
    @GET("quote/random")
    suspend fun getRandomQuote(@Header("Authorization") token: String): Response<Quote>

    @GET("clothes/recommended")
    suspend fun getRecommendedClothes(@Header("Authorization") token: String): Response<List<Stuff>>

    @GET("art/recommended/five")
    suspend fun getRecommendedArticles(@Header("Authorization") token: String): Response<List<Article>>

    //API статей
    @GET("art/recommended/tree")
    suspend fun getSliderArticles(@Header("Authorization") token: String): Response<List<Article>>

    @GET("art/{id}")
    suspend fun getArticleById(@Header("Authorization") token: String,
                               @Path("id") id: Long): Response<Article>

    @GET("art/all")
    suspend fun getAllArticles(@Header("Authorization") token: String): Response<List<Article>>

    //API профиля
    @GET("user/get/setting")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<User>

    @GET("user/get/styleuser")
    suspend fun getUserStyle(@Header("Authorization") token: String): Response<StyleUser>

    @GET("user/get/allphrases")
    suspend fun getStyles(@Header("Authorization") token: String): Response<List<StyleUser>>

    @POST("user/set/styleuser")
    suspend fun setUserStyle(@Header("Authorization") token: String,
                             @Query("id") id: Long): Response<List<String>>

    @POST("user/changeUsername")
    suspend fun setUsername(@Header("Authorization") token: String,
                            @Query("username") username: String): Response<NewToken>

    @POST("user/set/setting")
    suspend fun setUserInfo(@Header("Authorization") token: String,
                            @Body settingRequest: SettingRequest): Response<TextMessage>

    //API гардероба
    @GET("wardrobe/{season}")
    suspend fun getUserClothesBySeason(@Header("Authorization") token: String,
                                       @Path("season") season: String): Response<WardrobeResponse>

    @POST("wardrobe/add/shopstuff")
    suspend fun addClothesInWardrobe(@Header("Authorization") token: String,
                                     @Query("id") id: Long): Response<TextMessage>

//    @Multipart
//    @POST("wardrobe/add/userstuff")
//    suspend fun addUserStuff(@Part stuffInfo: MultipartBody.Part,
//                             @Part img: MultipartBody.Part,
//                             @Header("Authorization") token: String) : Response<TextMessage>

    @Multipart
    @POST("wardrobe/add/userstuff")
    suspend fun addUserStuff(@Part("info") stuffInfo: RequestBody,
                             @Part img: MultipartBody.Part,
                             @Header("Authorization") token: String) : Response<TextMessage>

    @POST("wardrobe/delete/shopstuff")
    suspend fun deleteClothesInWardrobe(@Header("Authorization") token: String,
                                        @Query("id") id: Long): Response<TextMessage>

    @POST("wardrobe/delete/userstuff")
    suspend fun deleteUserStuff(@Header("Authorization") token: String,
                                @Query("id") id: Long): Response<TextMessage>

    @GET("wardrobe/check/{type}")
    suspend fun checkClothesInWardrobe(@Header("Authorization") token: String,
                                       @Path("type") type: String,
                                       @Query("id") id: Long): Response<TextMessage>

    //API поиска одежды
    @POST("clothes/get/cv")
    suspend fun getCvStuff(@Body stuffData: ShopStuffCVData,
                           @Header("Authorization") token: String): Response<List<Stuff>>
}