package com.bteam.blocal.utility;

import androidx.annotation.MenuRes;
import androidx.appcompat.widget.Toolbar;

public interface IToolbarHandler {
    void setOptionsMenu(@MenuRes Integer menuRes);
    void setMenuListener(Toolbar.OnMenuItemClickListener callback);
    void hideToolbar();
}
