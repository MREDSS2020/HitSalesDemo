package com.edss.hitsales.Model;

public class FlatStatus {
    int Sold,OnHold,NotToSell,Total;

    public int getSold() {
        return Sold;
    }

    public void setSold(int sold) {
        Sold = sold;
    }

    public int getOnHold() {
        return OnHold;
    }

    public void setOnHold(int onHold) {
        OnHold = onHold;
    }

    public int getNotToSell() {
        return NotToSell;
    }

    public void setNotToSell(int notToSell) {
        NotToSell = notToSell;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
