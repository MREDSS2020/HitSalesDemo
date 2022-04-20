package com.edss.hitsales.Model;

import androidx.annotation.Nullable;

public class CategoryData {
    @Nullable
    int RowID,Type;

    @Nullable
    Double TaxAmount,TaxAmount1;

    @Nullable
    String Name,SalesItemID,ItemNo,Designation,AmountExclTax,TaxCode,DetailedDescription,TaxCode1;

    public int getRowID() {
        return RowID;
    }

    public void setRowID(int rowID) {
        RowID = rowID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    @Nullable
    public Double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(@Nullable Double taxAmount) {
        TaxAmount = taxAmount;
    }

    @Nullable
    public Double getTaxAmount1() {
        return TaxAmount1;
    }

    public void setTaxAmount1(@Nullable Double taxAmount1) {
        TaxAmount1 = taxAmount1;
    }

    @Nullable
    public String getName() {
        return Name;
    }

    public void setName(@Nullable String name) {
        Name = name;
    }

    @Nullable
    public String getSalesItemID() {
        return SalesItemID;
    }

    public void setSalesItemID(@Nullable String salesItemID) {
        SalesItemID = salesItemID;
    }

    @Nullable
    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(@Nullable String itemNo) {
        ItemNo = itemNo;
    }

    @Nullable
    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(@Nullable String designation) {
        Designation = designation;
    }

    @Nullable
    public String getAmountExclTax() {
        return AmountExclTax;
    }

    public void setAmountExclTax(@Nullable String amountExclTax) {
        AmountExclTax = amountExclTax;
    }

    @Nullable
    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(@Nullable String taxCode) {
        TaxCode = taxCode;
    }

    @Nullable
    public String getDetailedDescription() {
        return DetailedDescription;
    }

    public void setDetailedDescription(@Nullable String detailedDescription) {
        DetailedDescription = detailedDescription;
    }

    @Nullable
    public String getTaxCode1() {
        return TaxCode1;
    }

    public void setTaxCode1(@Nullable String taxCode1) {
        TaxCode1 = taxCode1;
    }
}
