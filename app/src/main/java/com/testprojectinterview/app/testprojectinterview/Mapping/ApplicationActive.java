package com.testprojectinterview.app.testprojectinterview.Mapping;

import android.graphics.drawable.Drawable;

/**
 * Created by skyshi on 12/01/17.
 */

public class ApplicationActive {
    private String applicationName;
    private Drawable applicationIcon;
    private String applicationPackage;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Drawable getApplicationIcon() {
        return applicationIcon;
    }

    public void setApplicationIcon(Drawable applicationIcon) {
        this.applicationIcon = applicationIcon;
    }

    public String getApplicationPackage() {
        return applicationPackage;
    }

    public void setApplicationPackage(String applicationPackage) {
        this.applicationPackage = applicationPackage;
    }
}
