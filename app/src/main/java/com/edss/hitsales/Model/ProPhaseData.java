package com.edss.hitsales.Model;

import androidx.annotation.Nullable;

public class ProPhaseData {
    @Nullable
    String ID,Name,DatePlanned,DateFinished,TaxCode,TaxCode1;
    @Nullable
    boolean Finished;
    @Nullable
    double Percentage,Amount,ApplicableValue;
    @Nullable
    int CostType;

    @Nullable
    public String getID() {
        return ID;
    }

    public void setID(@Nullable String ID) {
        this.ID = ID;
    }

    @Nullable
    public String getName() {
        return Name;
    }

    public void setName(@Nullable String name) {
        Name = name;
    }

    @Nullable
    public String getDatePlanned() {
        return DatePlanned;
    }

    public void setDatePlanned(@Nullable String datePlanned) {
        DatePlanned = datePlanned;
    }

    @Nullable
    public String getDateFinished() {
        return DateFinished;
    }

    public void setDateFinished(@Nullable String dateFinished) {
        DateFinished = dateFinished;
    }

    @Nullable
    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(@Nullable String taxCode) {
        TaxCode = taxCode;
    }

    @Nullable
    public String getTaxCode1() {
        return TaxCode1;
    }

    public void setTaxCode1(@Nullable String taxCode1) {
        TaxCode1 = taxCode1;
    }

    public boolean isFinished() {
        return Finished;
    }

    public void setFinished(boolean finished) {
        Finished = finished;
    }

    public double getPercentage() {
        return Percentage;
    }

    public void setPercentage(double percentage) {
        Percentage = percentage;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getCostType() {
        return CostType;
    }

    public void setCostType(int costType) {
        CostType = costType;
    }

    public double getApplicableValue() {
        return ApplicableValue;
    }

    public void setApplicableValue(double applicableValue) {
        ApplicableValue = applicableValue;
    }
}
