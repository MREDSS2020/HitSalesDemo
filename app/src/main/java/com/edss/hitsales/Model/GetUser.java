package com.edss.hitsales.Model;


import androidx.annotation.Nullable;

public class GetUser {
    String ID;
    @Nullable
    String FirstName,LastName,DefaultCompany,GroupId,Designation;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Nullable
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(@Nullable String firstName) {
        FirstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return LastName;
    }

    public void setLastName(@Nullable String lastName) {
        LastName = lastName;
    }

    @Nullable
    public String getDefaultCompany() {
        return DefaultCompany;
    }

    public void setDefaultCompany(@Nullable String defaultCompany) {
        DefaultCompany = defaultCompany;
    }

    @Nullable
    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(@Nullable String groupId) {
        GroupId = groupId;
    }

    @Nullable
    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(@Nullable String designation) {
        Designation = designation;
    }
}
