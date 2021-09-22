package com.bteam.blocal.ui.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
public class ItemListUserFragment extends ItemListFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemListUserFragmentArgs args = ItemListUserFragmentArgs.fromBundle(getArguments());
        itemListViewModel.setStoreUid(args.getStoreUid());
    }

    @Override
    protected int getNavigationGraphId() {
        return R.navigation.user_navigation;
    }

    @Override
    protected Bundle getDetailArgsBundle(String uid) {
        return getDirection(uid).getArguments();
    }

    @Override
    protected int getDetailNavigationId() {
        return R.id.navigation_item_detail;
    }

    @Override
    protected void navigateToDetail(String uid) {

        NavHostFragment.findNavController(this).navigate(getDirection(uid));
    }

    private ItemListUserFragmentDirections.ShowItemDetail getDirection(String uid){
        return ItemListUserFragmentDirections.showItemDetail(uid, itemListViewModel.getStoreUid());
    }
}
