package com.bteam.blocal.data.model;

public class DashboardButtonModel {
    private int textId;
    private int iconId;
    private int actionId;

    public DashboardButtonModel(int textId, int iconId, int actionId) {
        this.textId = textId;
        this.iconId = iconId;
        this.actionId = actionId;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
}
