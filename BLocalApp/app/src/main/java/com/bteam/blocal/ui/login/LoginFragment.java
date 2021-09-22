package com.bteam.blocal.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bteam.blocal.R;
import com.bteam.blocal.data.IOnCompleteCallback;
import com.bteam.blocal.data.model.StoreModel;
import com.bteam.blocal.data.model.errors.NoDocumentException;
import com.bteam.blocal.data.repository.StoreRepository;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 42;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                _backNavCallback);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            navigateUserLoggedIn();
        }
        else{
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.Theme_Blocal)
                            .build(),
                    RC_SIGN_IN);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
               navigateUserLoggedIn();
            } else {
                getActivity().finish();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }


    }
    private void navigateUserLoggedIn(){
        StoreRepository.getInstance().updateMyStore(new IOnCompleteCallback<StoreModel>() {

            @Override
            public void onSuccess(StoreModel data) {
                NavController navHostController = Navigation.findNavController(getActivity(),
                        R.id.main_nav_host_fragment);
                navHostController.navigate(LoginFragmentDirections
                        .actionLoginToMainStoreFragment());
            }

            @Override
            public void onError(Throwable err) {
                if(err instanceof NoDocumentException){
                    NavController navHostController = Navigation.findNavController(getActivity(),
                            R.id.main_nav_host_fragment);
                    navHostController.navigate(LoginFragmentDirections
                            .actionLoginToMainUserFragment());
                }
                else{
                    //TODO: Handle unexpected error
                }

            }

        });
    }

    private final OnBackPressedCallback _backNavCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
          getActivity().finish();
        }
    };
}