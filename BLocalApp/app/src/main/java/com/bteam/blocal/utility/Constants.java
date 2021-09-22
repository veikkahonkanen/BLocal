package com.bteam.blocal.utility;

import com.bumptech.glide.request.RequestOptions;
import com.bteam.blocal.R;

public class Constants {

    private static RequestOptions itemDefaultOptions;

    public static RequestOptions getItemDefaultOptions() {
        if (Constants.itemDefaultOptions == null)
            itemDefaultOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                    .error(R.drawable.ic_baseline_shopping_basket_24);
        return itemDefaultOptions;
    }

    private static RequestOptions storeDefaultOptions;

    public static RequestOptions getStoreDefaultOptions() {
        if (Constants.storeDefaultOptions == null)
            storeDefaultOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_baseline_storefront_24)
                    .error(R.drawable.ic_baseline_storefront_24);
        return storeDefaultOptions;
    }
}
