package com.bteam.blocal.ui.store.item_finder;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bteam.blocal.R;
import com.bteam.blocal.ui.store.CameraFragment;
import com.google.android.material.snackbar.Snackbar;

public class ItemFinderFragment extends CameraFragment {
    private ItemFinderViewModel vm;
    private boolean hasScanned;
    private NavController controller;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(ItemFinderViewModel.class);
        hasScanned = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tlb.setVisibility(View.GONE);
        controller = NavHostFragment.findNavController(this);
    }

    @Override
    protected void barCodeScanned(String data) {
        if (hasScanned) return;
        hasScanned = true;
        vm.findItemUid(data).observe(getViewLifecycleOwner(), result -> {
            switch (result.status) {
                case SUCCESS:
                    if (result.data.size() > 0) {
                        ItemFinderFragmentDirections.GoToItem dirs = ItemFinderFragmentDirections
                                .goToItem(result.data.get(0).getUid());
                        controller.navigate(dirs);
                    } else {
                        noItemFound();
                    }
                    break;
                case ERROR:
                    noItemFound();
                    break;
            }
        });
    }

    private void noItemFound() {
        Snackbar.make(getView(), R.string.err_item_not_found, Snackbar.LENGTH_SHORT).show();
        controller.navigateUp();
    }
}
