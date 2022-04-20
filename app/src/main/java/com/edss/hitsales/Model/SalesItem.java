package com.edss.hitsales.Model;

import androidx.annotation.Nullable;

public class SalesItem {

    @Nullable
    String ID, CustomerID, CustName, Wing,OnHoldDate,FloorID,ShortName ,ProjectID,OriginalSalesItemID,DiscountReason,CategorySelected ;
    @Nullable
    boolean Sold,OnHold,ApprovalStatus;
    @Nullable
    Double TotalContract,CarpetArea,SurfaceArea,SalesRate,TotalBasicContractExTax,Discount;//TotalSupplement,

    @Nullable
    public String getID() {
        return ID;
    }

    public void setID(@Nullable String ID) {
        this.ID = ID;
    }

    @Nullable
    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(@Nullable String customerID) {
        CustomerID = customerID;
    }

    @Nullable
    public String getCustName() {
        return CustName;
    }

    public void setCustName(@Nullable String custName) {
        CustName = custName;
    }

    @Nullable
    public String getWing() {
        return Wing;
    }

    public void setWing(@Nullable String wing) {
        Wing = wing;
    }

    public boolean isSold() {
        return Sold;
    }

    public void setSold(boolean sold) {
        Sold = sold;
    }

    @Nullable
    public String getOnHoldDate() {
        return OnHoldDate;
    }

    public void setOnHoldDate(@Nullable String onHoldDate) {
        OnHoldDate = onHoldDate;
    }

    @Nullable
    public String getFloorID() {
        return FloorID;
    }

    public void setFloorID(@Nullable String floorID) {
        FloorID = floorID;
    }

    @Nullable
    public String getShortName() {
        return ShortName;
    }

    public void setShortName(@Nullable String shortName) {
        ShortName = shortName;
    }

    @Nullable
    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(@Nullable String projectID) {
        ProjectID = projectID;
    }

    @Nullable
    public String getOriginalSalesItemID() {
        return OriginalSalesItemID;
    }

    public void setOriginalSalesItemID(@Nullable String originalSalesItemID) {
        OriginalSalesItemID = originalSalesItemID;
    }

    @Nullable
    public String getDiscountReason() {
        return DiscountReason;
    }

    public void setDiscountReason(@Nullable String discountReason) {
        DiscountReason = discountReason;
    }

    public boolean isOnHold() {
        return OnHold;
    }

    public void setOnHold(boolean onHold) {
        OnHold = onHold;
    }

    public boolean isApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    @Nullable
    public Double getTotalContract() {
        return TotalContract;
    }

    public void setTotalContract(@Nullable Double totalContract) {
        TotalContract = totalContract;
    }

//    @Nullable
//    public Double getTotalSupplement() {
//        return TotalSupplement;
//    }
//
//    public void setTotalSupplement(@Nullable Double totalSupplement) {
//        TotalSupplement = totalSupplement;
//    }

    @Nullable
    public Double getCarpetArea() {
        return CarpetArea;
    }

    public void setCarpetArea(@Nullable Double carpetArea) {
        CarpetArea = carpetArea;
    }

    @Nullable
    public Double getSurfaceArea() {
        return SurfaceArea;
    }

    public void setSurfaceArea(@Nullable Double surfaceArea) {
        SurfaceArea = surfaceArea;
    }

    @Nullable
    public Double getSalesRate() {
        return SalesRate;
    }

    public void setSalesRate(@Nullable Double salesRate) {
        SalesRate = salesRate;
    }

    @Nullable
    public Double getTotalBasicContractExTax() {
        return TotalBasicContractExTax;
    }

    public void setTotalBasicContractExTax(@Nullable Double totalBasicContractExTax) {
        TotalBasicContractExTax = totalBasicContractExTax;
    }

    @Nullable
    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(@Nullable Double discount) {
        Discount = discount;
    }

    @Nullable
    public String getCategorySelected() {
        return CategorySelected;
    }

    public void setCategorySelected(@Nullable String categorySelected) {
        CategorySelected = categorySelected;
    }
}
