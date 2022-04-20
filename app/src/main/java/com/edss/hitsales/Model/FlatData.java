package com.edss.hitsales.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class FlatData  implements Parcelable {
    String ID;
    @Nullable
    int Sold,OnHold,NotToSell;


    protected FlatData(Parcel in) {
        ID = in.readString();
        Sold = in.readInt();
        OnHold = in.readInt();
        NotToSell = in.readInt();
    }

    public static final Creator<FlatData> CREATOR = new Creator<FlatData>() {
        @Override
        public FlatData createFromParcel(Parcel in) {
            return new FlatData(in);
        }

        @Override
        public FlatData[] newArray(int size) {
            return new FlatData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeInt(Sold);
        dest.writeInt(OnHold);
        dest.writeInt(NotToSell);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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
}
