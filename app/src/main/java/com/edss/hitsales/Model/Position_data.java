package com.edss.hitsales.Model;

public class Position_data {
    String ID,Name,Unit,PosType,PosNum,PosText,TitleGrade,Status;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getPosType() {
        return PosType;
    }

    public void setPosType(String posType) {
        PosType = posType;
    }

    public String getPosNum() {
        return PosNum;
    }

    public void setPosNum(String posNum) {
        PosNum = posNum;
    }

    public String getPosText() {
        return PosText;
    }

    public void setPosText(String posText) {
        PosText = posText;
    }

    public String getTitleGrade() {
        return TitleGrade;
    }

    public void setTitleGrade(String titleGrade) {
        TitleGrade = titleGrade;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Position_data(String ID, String name, String unit, String posType, String posNum, String posText, String titleGrade, String status) {
        this.ID = ID;
        Name = name;
        Unit = unit;
        PosType = posType;
        PosNum = posNum;
        PosText = posText;
        TitleGrade = titleGrade;
        Status = status;
    }
}
