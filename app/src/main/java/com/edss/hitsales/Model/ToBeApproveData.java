package com.edss.hitsales.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class ToBeApproveData implements Parcelable {
    @Nullable
    String ObjectID,StatusComment,UserOrGrpID,CreatorID,DateRequest,DateModification,ApprovedBy,ApprovalDeadLineDate,Comment,AdditionalInfo,LastApprovedBy;
    @Nullable
    int ApprovalType,Status,Level;
    int RowID;
//    @Nullable
//    String ID,FloorID,FlatType,CategorySelected,WingID;


    protected ToBeApproveData(Parcel in) {
        ObjectID = in.readString();
        StatusComment = in.readString();
        UserOrGrpID = in.readString();
        CreatorID = in.readString();
        DateRequest = in.readString();
        DateModification = in.readString();
        ApprovedBy = in.readString();
        ApprovalDeadLineDate = in.readString();
        Comment = in.readString();
        AdditionalInfo = in.readString();
        LastApprovedBy = in.readString();
        ApprovalType = in.readInt();
        Status = in.readInt();
        Level = in.readInt();
        RowID = in.readInt();
//        ID = in.readString();
//        FloorID = in.readString();
//        FlatType = in.readString();
//        CategorySelected = in.readString();
//        WingID = in.readString();
    }

    public static final Creator<ToBeApproveData> CREATOR = new Creator<ToBeApproveData>() {
        @Override
        public ToBeApproveData createFromParcel(Parcel in) {
            return new ToBeApproveData(in);
        }

        @Override
        public ToBeApproveData[] newArray(int size) {
            return new ToBeApproveData[size];
        }
    };

    @Nullable
    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(@Nullable String objectID) {
        ObjectID = objectID;
    }

    @Nullable
    public String getStatusComment() {
        return StatusComment;
    }

    public void setStatusComment(@Nullable String statusComment) {
        StatusComment = statusComment;
    }

    @Nullable
    public String getUserOrGrpID() {
        return UserOrGrpID;
    }

    public void setUserOrGrpID(@Nullable String userOrGrpID) {
        UserOrGrpID = userOrGrpID;
    }

    @Nullable
    public String getCreatorID() {
        return CreatorID;
    }

    public void setCreatorID(@Nullable String creatorID) {
        CreatorID = creatorID;
    }

    @Nullable
    public String getDateRequest() {
        return DateRequest;
    }

    public void setDateRequest(@Nullable String dateRequest) {
        DateRequest = dateRequest;
    }

    @Nullable
    public String getDateModification() {
        return DateModification;
    }

    public void setDateModification(@Nullable String dateModification) {
        DateModification = dateModification;
    }

    @Nullable
    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(@Nullable String approvedBy) {
        ApprovedBy = approvedBy;
    }

    @Nullable
    public String getApprovalDeadLineDate() {
        return ApprovalDeadLineDate;
    }

    public void setApprovalDeadLineDate(@Nullable String approvalDeadLineDate) {
        ApprovalDeadLineDate = approvalDeadLineDate;
    }

    @Nullable
    public String getComment() {
        return Comment;
    }

    public void setComment(@Nullable String comment) {
        Comment = comment;
    }

    @Nullable
    public String getAdditionalInfo() {
        return AdditionalInfo;
    }

    public void setAdditionalInfo(@Nullable String additionalInfo) {
        AdditionalInfo = additionalInfo;
    }

    @Nullable
    public String getLastApprovedBy() {
        return LastApprovedBy;
    }

    public void setLastApprovedBy(@Nullable String lastApprovedBy) {
        LastApprovedBy = lastApprovedBy;
    }

    public int getApprovalType() {
        return ApprovalType;
    }

    public void setApprovalType(int approvalType) {
        ApprovalType = approvalType;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getRowID() {
        return RowID;
    }

    public void setRowID(int rowID) {
        RowID = rowID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(ObjectID);
        dest.writeString(StatusComment);
        dest.writeString(UserOrGrpID);
        dest.writeString(CreatorID);
        dest.writeString(DateRequest);
        dest.writeString(DateModification);
        dest.writeString(ApprovedBy);
        dest.writeString(ApprovalDeadLineDate);
        dest.writeString(Comment);
        dest.writeString(AdditionalInfo);
        dest.writeString(LastApprovedBy);
        dest.writeInt(ApprovalType);
        dest.writeInt(Status);
        dest.writeInt(Level);
        dest.writeInt(RowID);
//        dest.writeString(ID);
//        dest.writeString(FloorID);
//        dest.writeString(FlatType);
//        dest.writeString(CategorySelected);
//        dest.writeString(WingID);
    }

//    @Nullable
//    public String getID() {
//        return ID;
//    }
//
//    public void setID(@Nullable String ID) {
//        this.ID = ID;
//    }
//
//    @Nullable
//    public String getFloorID() {
//        return FloorID;
//    }
//
//    public void setFloorID(@Nullable String floorID) {
//        FloorID = floorID;
//    }
//
//    @Nullable
//    public String getFlatType() {
//        return FlatType;
//    }
//
//    public void setFlatType(@Nullable String FlatType) {
//        FlatType = FlatType;
//    }
//
//    @Nullable
//    public String getCategorySelected() {
//        return CategorySelected;
//    }
//
//    public void setCategorySelected(@Nullable String categorySelected) {
//        CategorySelected = categorySelected;
//    }
//
//    @Nullable
//    public String getWingID() {
//        return WingID;
//    }
//
//    public void setWingID(@Nullable String wingID) {
//        WingID = wingID;
//    }
}
