package com.bteam.blocal.ui.store.main_store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bteam.blocal.R;
import com.bteam.blocal.ui.store.ItemListStoreFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Navigation inspired by https://github.com/SmartToolFactory/NavigationComponents-Tutorials/blob/master/Tutorial1-3Navigation-NestedNavHost/src/main/java/com/smarttoolfactory/tutorial1_3navigation_nestednavhost/navhost/HomeNavHostFragment.kt
// and by https://proandroiddev.com/handle-complex-navigation-flow-with-single-activity-and-android-jetpacks-navigation-component-6ad988602902
// https://stackoverflow.com/questions/50730494/new-navigation-component-from-arch-with-nested-navigation-graph
public class MainStoreFragment extends Fragment {
    public MainStoreFragment() {
        // Required empty public constructor
    }

    private NavController _navController;
    private BottomNavigationView _bottomNavView;
    private FloatingActionButton _floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _bottomNavView = view.findViewById(R.id.nav_view);
        _navController = ((NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.nav_store_host_fragment)).getNavController();
        _floatingActionButton = view.findViewById(R.id.flt_add_item);

        NavigationUI.setupWithNavController(_bottomNavView, _navController);

        _floatingActionButton.setOnClickListener(view1 -> _navController
                .navigate( ItemListStoreFragmentDirections.goToEdit(null)));
        listenToBackStack();

        _navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.my_store_item_list:
                    setMainUIVisibility(true, true);
                    break;
                case R.id.navigation_store_settings:
                case R.id.navigation_store_analytics:
                case R.id.navigation_item_finder:
                    setMainUIVisibility(true, false);
                    break;
                default:
                    setMainUIVisibility(false, false);
                    break;
            }
        });
    }

    private void setMainUIVisibility(boolean visible, boolean hasFloating) {
        _bottomNavView.setVisibility(visible ? View.VISIBLE : View.GONE);
        if(visible){
            if(hasFloating){
                _floatingActionButton.show();
            }
            else{
                _floatingActionButton.hide();
            }

        }
        else{
            _floatingActionButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        _backNavCallback.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        _backNavCallback.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _backNavCallback.setEnabled(false);
        _backNavCallback.remove();
    }

    void listenToBackStack() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), _backNavCallback);

    }

    private final OnBackPressedCallback _backNavCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
            if (_navController.getCurrentDestination().getId() == _navController.getGraph()
                    .getStartDestination()) {
                /*
                    Disable this callback because calls OnBackPressedDispatcher
                     gets invoked  calls this callback  gets stuck in a loop
                 */
                setEnabled(false);
                requireActivity().onBackPressed();
                setEnabled(true);

            } else if (isVisible()) {
                _navController.navigateUp();
            }
        }
    };
}