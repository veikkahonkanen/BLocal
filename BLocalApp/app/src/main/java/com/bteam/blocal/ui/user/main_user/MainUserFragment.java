package com.bteam.blocal.ui.user.main_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bteam.blocal.R;
import com.bteam.blocal.utility.IToolbarHandler;
import com.bteam.blocal.utility.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainUserFragment extends Fragment implements IToolbarHandler {
    private static final String TAG = "MainUserFragment";
    private NavController navController;

    private MainUserViewModel mainUserViewModel;
    private TextView txtCurrentUser, txtEmail;
    private ImageView userProfileImage;
    private Toolbar toolbar;

    public MainUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainUserViewModel =
                new ViewModelProvider(this).get(MainUserViewModel.class);

        // Set up the toolbar for the whole user part of the app
        toolbar = view.findViewById(R.id.user_nav_toolbar_main);
        DrawerLayout drawer = view.findViewById(R.id.user_drawer_layout);

        navController = ((NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.user_nav_host_fragment)).getNavController();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Set<Integer> topLevelDestinations = new HashSet<>(Arrays.asList(R.id.navigation_store_list,
                R.id.navigation_store_analytics, R.id.navigation_maps,
                R.id.navigation_my_store_fragment));
        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(topLevelDestinations);
        // If you are not using tablet, add drawer hamburger icon
        if (!SizeUtils.isTablet(getContext())) builder.setOpenableLayout(drawer);
        AppBarConfiguration appBarConfiguration = builder.build();

        // Set up the header card
        NavigationView userNavView = view.findViewById(R.id.user_nav_view);
        View headerView = userNavView.getHeaderView(0);

        txtCurrentUser = headerView.findViewById(R.id.user_nav_header_username);
        txtEmail = headerView.findViewById(R.id.user_nav_header_email);
        userProfileImage = headerView.findViewById(R.id.user_nav_header_icon);

        mainUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(),
                user -> {
                    if (user != null) {
                        txtEmail.setText(user.getEmail());
                        txtCurrentUser.setText(user.getDisplayName());
                        Glide.with(this).load(user.getPhotoUrl())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.ic_outline_account_circle_24)
                                        .error(R.drawable.ic_outline_account_circle_24))
                                .into(userProfileImage);
                    }

                });

        NavigationUI.setupWithNavController(userNavView, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Clear the menu that might have been added by the IToolbarHandler interface
            toolbar.getMenu().clear();
            // Restore the visibility of the toolbar
            toolbar.setVisibility(View.VISIBLE);

            if (topLevelDestinations.contains(destination.getId())) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
        listenToBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        backNavCallback.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        backNavCallback.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        backNavCallback.setEnabled(false);
        backNavCallback.remove();
    }

    void listenToBackStack() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                backNavCallback);
    }

    private final OnBackPressedCallback backNavCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
            if (navController.getCurrentDestination().getId() == navController.getGraph()
                    .getStartDestination()) {
                /*
                    Disable this callback because calls OnBackPressedDispatcher
                     gets invoked  calls this callback  gets stuck in a loop
                 */
                setEnabled(false);
                requireActivity().onBackPressed();
                setEnabled(true);

            } else if (isVisible()) {
                navController.navigateUp();
            }
        }
    };

    @Override
    public void setOptionsMenu(Integer menuRes) {
        toolbar.inflateMenu(menuRes);
    }

    @Override
    public void setMenuListener(Toolbar.OnMenuItemClickListener callback) {
        toolbar.setOnMenuItemClickListener(callback);
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }
}