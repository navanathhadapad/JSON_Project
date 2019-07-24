package com.tagcor.tagcor.RetrofitHelpers;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("userregister")
    @FormUrlEncoded
    Call<Object> User_Registration(@Field("fname") String fname,
                                   @Field("lname") String lname,
                                   @Field("email") String email,
                                   @Field("mobile") String mobile,
                                   @Field("user_password") String user_password,
                                   @Field("gender") String gender,
                                   @Field("token") String token
    );

    @POST("userlogin")
    @FormUrlEncoded
    Call<Object> login(@Field("email") String email,
                       @Field("user_password") String passwrod);

    @GET("homepagemaincategories")
    Call<Object> Categories();

    @GET("homepageads")
    Call<Object> RecentPostProduct();

    @GET("maincatrelatedsubcat/{clsmid}")
    Call<Object> SubCategories(@Path("clsmid") String clsmid);

    @GET("maincatrelatedproducts/{mcat_id}")
    Call<Object> MainCategoriesReletedPost(@Path("mcat_id") String clsmid);

    @GET("productinfo/{id}")
    Call<Object> FullDetailsInfo(@Path("id") String clsid);

    @GET("productinfomultiimages/{id}")
    Call<Object> ProductMutipleImgs(@Path("id") String clsid);

    @GET("subcatrelatedproducts/{scat_id}")
    Call<Object> SubCatReleatedProduct(@Path("scat_id") String clsid);

    @GET("allcountries")
    Call<Object> AllCountryList();

    @GET("allstates/{country_id}")
    Call<Object> CountryReleatedState(@Path("country_id") String clsid);

    @GET("allcities/{state_id}")
    Call<Object> StateReleatedCities(@Path("state_id") String clsid);

    @GET("alladsproducts/{user_id}")
    Call<Object> AllPostedAdsList(@Path("user_id") String clsid);

    @GET("allactiveadsproducts/{user_id}")
    Call<Object> ActivePostedAdsList(@Path("user_id") String clsid);

    @GET("allinactiveadsproducts/{user_id}")
    Call<Object> InActivePostedAdsList(@Path("user_id") String clsid);

    @GET("alladsactivestatus/{id}")
    Call<Object> getChangeStatusActive(@Path("id") String clsid);

    @GET("alladsinactivestatus/{id}")
    Call<Object> getChangeStatusInactive(@Path("id") String clsid);


    //B2B Model Code

    @GET("b2bhomepagerecentproduct")
    Call<Object> RecentProduct();

    @GET("b2bmaincat")
    Call<Object> B2bMainCat();

    @GET("b2bdefaultsubcat")
    Call<Object> B2bDefaultSubcat();

    @GET("b2brelatedsubcat/{m_cat_id}")
    Call<Object> MaincatReleatedSubcat(@Path("m_cat_id") String clsmid);

    @GET("b2brelatedsubsubcat/{s_cat_id}")
    Call<Object> SubcatReleatedSubSubCat(@Path("s_cat_id") String clsmid);

    @GET("b2brelatedssscatproducts/{brand}")
    Call<Object> SubSubcatReleatedProducts(@Path("brand") String clsmid);

    @GET("b2bssscatproductinfo/{product_id}")
    Call<Object> ProductFullInfo(@Path("product_id") String clsmid);

    @GET("b2bssscatproductinfomultimg/{product_id}")
    Call<Object> ProductMutlipleImages(@Path("product_id") String clsmid);

    @GET("b2buseraddress/{user_id}")
    Call<Object> B2BDefaultAddress(@Path("user_id") String user_id);

    @GET("b2buseralladdress/{user_id}")
    Call<Object> B2BAllAddressList(@Path("user_id") String user_id);

    @GET("b2buser_defaultaddresedit/{user_address_id}")
    Call<Object> EditAddressList(@Path("user_address_id") String user_address_id);

    @POST("b2buseraddress_updateaddres/{user_address_id}")
    @FormUrlEncoded
    Call<Object> B2BUserAddressInfoUpdate(@Path("user_address_id") String user_address_id,
                                     @Field("user_fullname") String user_fullname,
                                     @Field("user_mobile") String user_mobile,
                                     @Field("user_address") String user_address,
                                     @Field("user_city") String user_city,
                                     @Field("user_state") String user_state,
                                     @Field("user_pincode") String user_pincode);

    @POST("add_b2buseraddaddres")
    @FormUrlEncoded
    Call<Object> B2BUserAddressInfoAdd(@Field("user_id") String user_id,
                                          @Field("user_fullname") String user_fullname,
                                          @Field("user_mobile") String user_mobile,
                                          @Field("user_address") String user_address,
                                          @Field("user_city") String user_city,
                                          @Field("user_state") String user_state,
                                          @Field("user_pincode") String user_pincode);

    @GET("delete_b2buseraddress/{user_address_id}")
    Call<Object> DeleteAddressList(@Path("user_address_id") String user_address_id);

    @GET("b2buser_defaultaddrescheck/{user_id}")
    Call<Object> B2BDefultAddrCheck(@Path("user_id") String user_id);

    @GET("defaultaddress_change/{user_id}/{user_address_id}")
    Call<Object> B2BDefultAddrCheck(@Path("user_id") String user_id,
                                    @Path("user_address_id") String user_address_id);

    @POST("b2baddtocart")
    @FormUrlEncoded
    Call<Object> B2BAddToCartProduct(@Field("user_id") String user_id,
                                       @Field("item_id") String item_id,
                                       @Field("item_price") String item_price,
                                       @Field("delcharge") String delcharge,
                                       @Field("item_quantity") String item_quantity,
                                       @Field("minorder") String minorder);

    @GET("getcartitemdetails/{user_id}")
    Call<Object> GetAddedCartItems(@Path("user_id") String user_id);

    @GET("b2bdelete_caritem/{cart_id}")
    Call<Object> DeleteCartItems(@Path("cart_id") String cart_id);


    //Jobs Model Code

    @GET("userprofile/{user_id}")
    Call<Object> getUserProfJobs(@Path("user_id") String clsid);

    @GET("userprofilesummery/{user_id}")
    Call<Object> getUserProfJobsSummary(@Path("user_id") String clsid);

    @GET("userprofilesummeryedit/{user_id}")
    Call<Object> getUserProfJobSummaryEdit(@Path("user_id") String clsid);

    @POST("update_userjobsummary")
    @FormUrlEncoded
    Call<Object> UserProfileSummaryEdit(@Field("user_id") String user_id,
                                   @Field("summary") String summary);

    @GET("userprofileedit/{user_id}")
    Call<Object> getUserProfileInfoEdit(@Path("user_id") String clsid);

    @GET("userprofileeditcompanies")
    Call<Object> getCompanyListEdit();

    @POST("updateuprofile/{user_id}")
    @FormUrlEncoded
    Call<Object> UserProfileInfoEdit(@Path("user_id") String id,
                                     @Field("fname") String ufname,
                                     @Field("lname") String ulname,
                                     @Field("job_profile_title") String uprofileTitle,
                                     @Field("job_current_company") String spCurrenComp,
                                     @Field("user_currentcity") String uCurrentLoc,
                                     @Field("user_mobile") String uPhone,
                                     @Field("user_dob") String user_dob,
                                     @Field("user_gender") String spGender,
                                     @Field("marital_status") String marital_status,
                                     @Field("language_known") String language_known,
                                     @Field("notification_status") String alert,
                                     @Field("image") String image);


    /*@POST("updateuprofile")
    @FormUrlEncoded
    Call<Object> UserProfileInfoEdit(@Field("user_id") String uId);*/

    @GET("userprofileexperience/{user_id}")
    Call<Object> getUserExpInfo(@Path("user_id") String user_id);

    @GET("userprofileeducationdetails/{user_id}")
    Call<Object> getUserEducInfo(@Path("user_id") String user_id);

    @GET("userprofileskills/{user_id}")
    Call<Object> getUserSkillsInfo(@Path("user_id") String user_id);

    @GET("userprofileappliedjobs/{user_id}")
    Call<Object> getAppliedJobsList(@Path("user_id") String user_id);

    @GET("userprofileexperienceedit/{id}")
    Call<Object> getUserExpEdit(@Path("id") String id);

    @POST("updateuserprofileexperience/{id}")
    @FormUrlEncoded
    Call<Object> UpdateUserExp(@Path("id") String id,
                               @Field("user_id") String user_id,
                               @Field("company_name") String company_name,
                               @Field("position") String position,
                               @Field("exep_year") String exep_year,
                               @Field("exep_month") String exep_month,
                               @Field("description") String description);

    @GET("userprofileexperiencedelete/{id}")
    Call<Object> getUserExpDelete(@Path("id") String id);


    @GET("userprofileeduedit/{id}")
    Call<Object> getUserEditEdu(@Path("id") String id);


    @POST("updateuserprofileeducation/{id}")
    @FormUrlEncoded
    Call<Object> UpdateUserEducation(@Path("id") String id,
                               @Field("user_id") String user_id,
                               @Field("course") String course,
                               @Field("specilization") String specilization,
                               @Field("university") String university,
                               @Field("year") String year,
                               @Field("marks") String marks);

    @GET("userprofileeducationdelete/{id}")
    Call<Object> getUserEducationDelete(@Path("id") String id);


    @GET("userprofileskillsedit/{id}")
    Call<Object> getUserSkills(@Path("id") String id);

    @POST("updateuserprofileskills/{id}")
    @FormUrlEncoded
    Call<Object> UpdateUserSkills(@Path("id") String id,
                                     @Field("user_id") String user_id,
                                     @Field("skill_title") String skill_title,
                                     @Field("skill_version") String skill_version,
                                     @Field("skillexep_year") String skillexep_year,
                                     @Field("skillexep_month") String skillexep_month);

    @GET("userprofileskillsdelete/{id}")
    Call<Object> getUserSkillsDelete(@Path("id") String id);


    @GET("userprofileappliedjobsfeedback/{can_id}/{job_id}/user")
    Call<Object> getUserAppliedJobsFeedback(@Path("can_id") String can_id,
                                            @Path("job_id") String job_id);

    @GET("userprofileappliedjobsdelete/{jac_id}")
    Call<Object> getUserAppliedJobDelete(@Path("jac_id") String jac_id);

    @GET("userprofileprojects/{user_id}")
    Call<Object> getUserProjectsList(@Path("user_id") String user_id);

    @GET("userprofileprojectedit/{id}")
    Call<Object> UpdateUserProjects(@Path("id") String id);


    @POST("updateuserprofileprojects/{id}")
    @FormUrlEncoded
    Call<Object> UpdateProjectDetails(@Path("id") String id, @Field("user_id") String user_id,
                                      @Field("project_title") String project_title,
                                      @Field("website") String website,
                                      @Field("client") String client,
                                      @Field("location") String location,
                                      @Field("role") String role,
                                      @Field("from_durationp") String from_durationp,
                                      @Field("to_durationp") String to_durationp,
                                      @Field("team_size") String  team_size,
                                      @Field("skills_used[]") String skills_used,
                                      @Field("details") String details,
                                      @Field("emp_type") String emp_type);

    /*@Field("skills_used") ArrayList<String> skills_used*/

    @GET("userprofileprojectsdelete/{id}")
    Call<Object> ProjectJobDelete(@Path("id") String id);

    @GET("userprofileresumelist/{user_id}")
    Call<Object> getUserResumeList(@Path("user_id") String user_id);

    @GET("homepagerecentjobs")
    Call<Object> getHomepageRecentJobs();

    @GET("homepagejobsfullview/{id}")
    Call<Object> getJobsFullDetailsView(@Path("id") String id);

    @GET("companyprofile/{recru_id}")
    Call<Object> getCompanyDetails(@Path("recru_id") String id);

    @GET("companyprofilerelatedjobs/{recru_id}")
    Call<Object> getCompanyRelJobs(@Path("recru_id") String id);

    @GET("jobapply/{resume_id}/{id}/{candidate_id}/user")
    Call<Object> getClickApplyJobs(@Path("resume_id") String resumeid,
                                   @Path("id") String id,
                                   @Path("candidate_id") String candidate_id);



    //Professional model all Urls

    @GET("professionalposts/{user_id}")
    Call<Object> getProfessionalPostHome(@Path("user_id") String user_id);

    @GET("professionalpostlike/{post_id}/{request_accept}/{user_id}")
    Call<Object> getProfessionalPostlike(@Path("post_id") String post_id,
                                         @Path("request_accept") String request_accept,
                                         @Path("user_id") String user_id);

    @GET("professionalpostunlike/{post_id}/{request_accept}/{user_id}")
    Call<Object> getProfessionalPostUnlike(@Path("post_id") String post_id,
                                         @Path("request_accept") String request_accept,
                                         @Path("user_id") String user_id);

    @GET("homepagehideprofessionalposts/{login_id}/{post_id}")
    Call<Object> getProfessionalPostHideMe(@Path("login_id") String user_id,
                                           @Path("post_id") String post_id);

    @GET("deleteprofessionalposts/{post_id}")
    Call<Object> getProfessionalPostDelete(@Path("post_id") String post_id);

    @GET("professionalreport/{login_id}/{post_id}")
    Call<Object> getProfessionalPostReport(@Path("login_id") String user_id,
                                           @Path("post_id") String post_id);

    @POST("professionalpost")
    @FormUrlEncoded
    Call<Object> PostProfessionalStatus(@Field("user_id") String user_id,
                                     @Field("description") String description,
                                     @Field("post_option") String post_option,
                                        @Field("image") String image);

    @Multipart
    @POST("professionalpost")
    Call<ResponseBody> uploadImageTest(@Part MultipartBody.Part file, @Part("user_id") RequestBody requestBody);


    //@GET("demousers/{fname}")
    //Call<Object> getProfessiSearchList(@Path("fname") String fname);

    @GET("professionalusers/{fname}")
    Call<Object> getProfessiSearchList(@Path("fname") String fname);

    @GET("professionalfrndconnexoinfo/{user_id}")
    Call<Object> getProfessionalConnexo(@Path("user_id") String user_id);

    @GET("professionalfrndconnexocount/{user_id}")
    Call<Object> getProfessionalConnexoCount(@Path("user_id") String user_id);

    @GET("professionalpostlikenotific/{user_id}")
    Call<Object> getProfessionalLikeNotifCount(@Path("user_id") String user_id);

    @GET("professionalpostlikenotificinfo/{user_id}")
    Call<Object> getProfessitionlNotiListInfo(@Path("user_id") String user_id);

    @GET("professionalfriendrequistnotificinfo/{user_id}")
    Call<Object> getProfessitionlFriendRequestList(@Path("user_id") String user_id);

    @GET("professionalfriendrequistaccept/{request_sent}/{request_accept}/{friend_id}")
    Call<Object> getProfeFriendRequestAccept(@Path("request_sent") String request_sent,
                                             @Path("request_accept") String request_accept,
                                             @Path("friend_id") String friend_id);

    @GET("professionalfriendrequistdelete/{friend_id}")
    Call<Object> getProfeFriendRequestReject(@Path("friend_id") String friend_id);

    @GET("professionalfrndrequeststatus/{user_id}/{friend_id}")
    Call<Object> getAddFriSentReqAcceptReq(@Path("user_id") String friend_id,
                                           @Path("friend_id") String user_id);

    @GET("professionalfrndrequestsent/{request_sent}/{request_accept}")
    Call<Object> getFriendRequestSent(@Path("request_sent") String request_sent,
                                           @Path("request_accept") String request_accept);

    @GET("professionalpostlikenotificinfoupdate/{post_id}/{user_id}")
    Call<Object> UpdateLikeUnlikeNotification(@Path("post_id") String post_id,
                                      @Path("user_id") String user_id);

}
