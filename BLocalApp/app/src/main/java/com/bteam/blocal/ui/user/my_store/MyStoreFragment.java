package com.bteam.blocal.ui.user.my_store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bteam.blocal.R;

public class MyStoreFragment extends Fragment {
    public MyStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnCreate = view.findViewById(R.id.btn_create_store);
        btnCreate.setOnClickListener(view1 -> navigateCreateStore());
    }

    private void navigateCreateStore() {
        NavHostFragment.findNavController(this)
                .navigate(MyStoreFragmentDirections
                        .actionNavigationMyStoreFragmentToCreateStoreFragment());
    }
}