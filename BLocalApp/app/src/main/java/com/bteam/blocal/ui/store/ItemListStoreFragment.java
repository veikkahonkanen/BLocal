package com.bteam.blocal.ui.store;

import android.os.Bundle;

import androidx.navigation.fragment.NavHostFragment;

import com.bteam.blocal.R;
import com.bteam.blocal.ui.shared.item_list.ItemListFragment;

/**
 * We need to create this implementation so that the NavigationComponent can use it
 * Since we reuse this fragment for the User and the Store, when Navigation Component generates
 * the NavDirections it doesn't know how to generate the actions and arguments (it does it in order
 * so user NavDirection might be generated or store NavDirection might be). This is because
 * NavComonent generates directions based on the Fragment name
 * -- ItemListFragment -> ItemListDirections.
 * <p>
 * This is a workaround until NavComponent introduces a way to separate reused fragments
 */
public class ItemListStoreFragment extends ItemListFragment {

    @Override
    protected int getNavigationGraphId() {
        return R.navigation.store_navigation;
    }

    @Override
    protected Bundle getDetailArgsBundle(String itemUid) {
        return getDirections(itemUid).getArguments();
    }

    @Override
    protected int getDetailNavigationId() {
        return R.id.itemDetailFragment;
    }

    @Override
    protected void navigateToDetail(String uid) {
        NavHostFragment.findNavController(this).navigate(getDirections(uid));
    }

    private ItemListStoreFragmentDirections.ShowItemDetail getDirections(String uid) {
        return ItemListStoreFragmentDirections.showItemDetail(uid);
    }
}
