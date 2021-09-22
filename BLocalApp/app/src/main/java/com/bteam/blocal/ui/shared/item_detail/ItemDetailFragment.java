package com.bteam.blocal.ui.shared.item_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bteam.blocal.R;
import com.bteam.blocal.data.model.ItemModel;
import com.bteam.blocal.utility.Constants;
import com.bteam.blocal.utility.InStockText;
import com.bteam.blocal.utility.SizeUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class ItemDetailFragment extends Fragment {

    private TextView nameTxt, priceTxt, inStockTxt, descriptionTxt;
    protected Toolbar toolbar;
    protected  FloatingActionButton floatingActionButton;
    private ImageView headerImg;
    protected ItemDetailViewModel vm;
    private  AppBarLayout appBar;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(ItemDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());

        nameTxt = view.findViewById(R.id.txt_item_name);
        priceTxt = view.findViewById(R.id.txt_item_price);
        inStockTxt = view.findViewById(R.id.txt_item_stock);
        descriptionTxt = view.findViewById(R.id.txt_item_description);
        headerImg = view.findViewById(R.id.header);
        appBar = view.findViewById(R.id.app_bar);

        floatingActionButton = view.findViewById(R.id.fab_edit);

        if(getIsTablet()){
            toolbar.setVisibility(View.INVISIBLE);
            appBar.setExpanded(true);
            // Block appbarlayout from collapsing.
            // Inspired by https://code.luasoftware.com/tutorials/android/how-to-disable-or-lock-collapsingtoolbarlayout-collapse-or-expand/
            view.findViewById(R.id.nested_scroll_detail).setNestedScrollingEnabled(false);
            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
            if(params.getBehavior() == null){
                params.setBehavior(new AppBarLayout.Behavior());
            }
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
        }


        vm.getItemDetail().observe(getViewLifecycleOwner(), itemModelResource -> {
            switch (itemModelResource.status) {
                case SUCCESS:
                    ItemModel itemModel = itemModelResource.data;
                    nameTxt.setText(itemModel.getName());
                    priceTxt.setText("" + itemModel.getPrice());
                    inStockTxt.setText(InStockText.isInStockText(itemModel.isInStock()));
                    descriptionTxt.setText(itemModel.getDescription());
                    Glide.with(getContext()).load(itemModel.getImageUrl())
                            .apply(Constants.getItemDefaultOptions()).into(headerImg);
                    break;
            }
        });
    }
    protected boolean getIsTablet(){
        return SizeUtils.isTablet(getContext());
    }

}