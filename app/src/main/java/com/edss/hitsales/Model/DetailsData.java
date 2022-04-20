package com.edss.hitsales.Model;

import androidx.annotation.Nullable;

public class DetailsData {
    @Nullable
    String ID, Description;

    @Nullable
    public String getID() {
        return ID;
    }

    public void setID(@Nullable String ID) {
        this.ID = ID;
    }

    @Nullable
    public String getDescription() {
        return Description;
    }

    public void setDescription(@Nullable String description) {
        Description = description;
    }
}
