package com.edss.hitsales.Utils;

import com.edss.hitsales.Model.CategoryData;
import com.edss.hitsales.Model.DetailsData;
import com.edss.hitsales.Model.FlatData;
import com.edss.hitsales.Model.SalesItem;
import com.edss.hitsales.Model.FlatStatus;
import com.edss.hitsales.Model.GetUser;
import com.edss.hitsales.Model.Login;
import com.edss.hitsales.Model.ProPhaseData;
import com.edss.hitsales.Model.Project;
import com.edss.hitsales.Model.ToBeApproveData;
import com.edss.hitsales.Model.UserRole_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface Api {

//    String BASE_URL = "http://192.168.2.250:8081/";//test URL for Raspberry Pi
//    String BASE_URL ="http://192.168.225.222:8081/";//test URL for PC
//    String BASE_URL ="http://192.168.2.177:8081/";//test URL for PC
    String BASE_URL ="http://192.168.1.203:8081/";//InquiryMngt/";//test URL for Madhavi PC

//-----------------------------------

//   String BASE_URL = "http://123.108.59.99:8081/";//Conventional developers

 //-----------------------------------

    @POST("/Sales/Login")
    Call<GetUser> UserLogin(@Body Login data);

    @FormUrlEncoded
    @POST("InquiryMngt/ProjectList")
    Call <List<Project>> getProjectList(@Field("UserId") String UserId,@Field("CompanyId") String CompanyId);

    @FormUrlEncoded
    @POST("Sales/WingsList")
    Call <List<DetailsData>> getWingList(@Field("UserId") String UserId, @Field("CompanyId") String CompanyId, @Field("SelectedId") String SelectedId, @Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("Sales/FloorList")
    Call <List<DetailsData>> getFloorList(@Field("CompanyId") String CompanyId, @Field("UserId") String UserId, @Field("SelectedId") String SelectedId, @Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("Sales/FlatList")
    Call <List<SalesItem>> getFlatList(@Field("CompanyId") String CompanyId, @Field("UserId") String UserId, @Field("SelFloorId") String SelFloorId, @Field("SelWingId") String SelWingId, @Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("Sales/TypeList")
    Call <List<DetailsData>> getTypeList(@Field("CompanyId") String CompanyId,  @Field("UserId") String UserId,@Field("SelectedId") String SelectedId, @Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("Sales/CategoryList")
    Call<List<CategoryData>> getCategoryList(@Field("CompanyId") String CompanyId, @Field("UserId") String UserId, @Field("SelectedId") String SelectedId, @Field("SelectedFlatId") String SelectedFlatId, @Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("Sales/ProPhaseList")
    Call<List<ProPhaseData>> getProPhaseList(@Field("CompanyId") String CompanyId, @Field("UserId") String UserId, @Field("SelectedId") String SelectedId, @Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("Sales/FlatAvailabilityList")
    Call<List<FlatData>> getFlatAvailabilityList(@Field("DBName") String DBName, @Field("CompanyId") String CompanyId );

    @FormUrlEncoded
    @POST("Sales/FlatAvailabilityCount")
    Call<List<FlatStatus>> getFlatAvailabilityCount(@Field("DBName") String DBName, @Field("CompanyId") String CompanyId );

    @FormUrlEncoded
    @POST("Sales/UserRole")
    Call <List<UserRole_data>> getUserRole(@Field("UserId") String UserId, @Field("CompanyId") String CompanyId, @Field("ProjectId") String SelectedProjId, @Field("BuildingId") String SelectedBuildId);

    @FormUrlEncoded
    @POST("Sales/SaveFlatQuotation")
    Call<String> SaveFlatQuotation(@Field("UserId") String UserId, @Field("CompanyId") String CompanyId, @Field("SelectedFlatId") String SelectedId,
                                   @Field("DBName") String DBName, @Field("Amount") String Amount,//@Field("Discount") String Discount,@Field("DiscountReason") String DiscountReason,
                                   @Field("FinalAmount") String FinalAmount, @Field("ObjectID") String ObjectID, @Field("DateToday") String DateToday,
                                   @Field("CategorySelected")String CategorySelected,@Field("Role")String Role);//,@Field("RowID")int RowID);

    @FormUrlEncoded
    @POST("Sales/FlatsToBeApprove")
    Call <List<ToBeApproveData>> getFlatsToBeApprove(@Field("CompanyId") String CompanyId, @Field("UserId") String UserId);

    @FormUrlEncoded
    @POST("Sales/FlatDetails")
    Call <List<SalesItem>> getFlatDetails(@Field("CompanyId") String CompanyId, @Field("UserId") String UserId,
                                    @Field("SelectedFlatId") String SelectedId,@Field("DBName") String DBName);

    @FormUrlEncoded
    @POST("/Sales/UpdateApprovalStatus")
    Call<String> UpdateApprovalStatus(@Field("UserID") String UserId, @Field("CompanyId") String CompanyId, @Field("SelectedFlatId") String SelectedId,
                                      @Field("DBName") String DBName, @Field("Amount") String Amount,@Field("Discount") String Discount,
                                      @Field("DiscountReason") String DiscountReason,@Field("FinalAmount") String FinalAmount,
                                      @Field("ObjectID") String ObjectID, @Field("DateToday") String DateToday,@Field("Status")String Status,
                                      @Field("Role")String Role,@Field("RowID")int RowID,@Field("Creator")String Creator,@Field("StatusComment") String StatusComment);
}
