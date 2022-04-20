package com.edss.hitsales.Model;

import androidx.annotation.Nullable;

public class Project {
    @Nullable
    String ID,Description,ProjectManager;

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

    @Nullable
    public String getProjectManager() {
        return ProjectManager;
    }

    public void setProjectManager(@Nullable String projectManager) {
        ProjectManager = projectManager;
    }

    public Project(@Nullable String ID, @Nullable String description, @Nullable String projectManager) {
        this.ID = ID;
        Description = description;
        ProjectManager = projectManager;
    }
}
