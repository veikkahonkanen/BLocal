package com.bteam.blocal.ui.user;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bteam.blocal.ui.shared.item_detail.ItemDetailFragment;
import com.bteam.blocal.utility.IToolbarHandler;

/**
 * We need to create this implementation so that the NavigationComponent can use it
 * Since we reuse this fragment for the User and the Store, when Navigation Component generates
 * the NavDirections it doesn't know how to generate the actions and arguments (it does it in order
 * so user NavDirection might be generated or store NavDirection might be). This is because
 * NavComonent generates directions based on the Fragment name
 * -- ItemListFragment -> ItemListDirections.
 *
 * This is a workaround until NavComponent introduces a way to separate reused fragments
 */
public class ItemDetailUserFragment extends ItemDetailFragment {
    private IToolbarHandler toolbarHandler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemDetailUserFragmentArgs args = ItemDetailUserFragmentArgs.fromBundle(getArguments());
        vm.setItemUid(args.getItemUid());
        vm.setStoreUid(args.getStoreUid());
        if(!getIsTablet()){
            toolbarHandler = (IToolbarHandler)getParentFragment().getParentFragment();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton.setVisibility(View.GONE);
        if(toolbarHandler != null){
            toolbarHandler.hideToolbar();
        }
    }
}
