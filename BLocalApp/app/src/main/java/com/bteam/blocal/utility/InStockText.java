package com.bteam.blocal.utility;

import com.bteam.blocal.R;

public class InStockText {
    public static int isInStockText(boolean isInStock){
        return isInStock ? R.string.lbl_in_stock : R.string.lbl_out_stock;
    }
}
