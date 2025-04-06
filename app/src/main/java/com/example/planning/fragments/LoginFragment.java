package com.example.planning.fragments;

import static com.example.planning.MainActivity.ID_KEY;
import static com.example.planning.MainActivity.navController;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.planning.PlanningActivity;
import com.example.planning.R;
import com.example.planning.databinding.FragmentLoginBinding;
import com.example.planning.utils.PasswordVisibility;
import com.example.planning.viewmodels.DataViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private DataViewModel dataViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setupViews();
        loadSavedCredentials();
        setupListeners();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupViews() {
        PasswordVisibility.managePasswordVisibility(binding.edtPasswordLayout, binding.edtPassword);
    }

    private void loadSavedCredentials() {
        String savedLogin = sharedPreferences.getString("login", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedLogin.isEmpty() && !savedPassword.isEmpty()) {
            binding.edtLogin.setText(savedLogin);
            binding.edtPassword.setText(savedPassword);
            binding.cbxRemember.setChecked(true);
        }
    }

    private void setupListeners() {
        binding.txtRegister.setOnClickListener(v ->
                navController.navigate(R.id.inscriptionFragment)
        );

        binding.btnLogin.setOnClickListener(v ->
                attemptLogin()
        );

        binding.cbxRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                editor.putString("login", "");
                editor.putString("password", "");
                editor.apply();
            }
        });
    }

    private void attemptLogin() {
        String login = binding.edtLogin.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (validateInput(login, password)) {
            showLoading(true);
            authenticateUser(login, password);
        }
    }

    private boolean validateInput(String login, String password) {
        boolean isValid = true;

        if (login.isEmpty()) {
            binding.edtLogin.setError("Please enter your login");
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.edtPassword.setError("Please enter your password");
            isValid = false;
        }

        return isValid;
    }

    private void authenticateUser(String login, String password) {
        dataViewModel.checkLogin(login, password);
        dataViewModel.getLiveDataCheckLogin().observe(getViewLifecycleOwner(), userId -> {
            if (userId == null) return;

            showLoading(false);

            if (userId < 0) {
                binding.edtLogin.setError("Invalid login or password");
                Toast.makeText(requireActivity(), "Invalid login or password", Toast.LENGTH_SHORT).show();
            } else {
                handleSuccessfulLogin(login, password, Math.toIntExact(userId));
            }

            // Remove the observer after getting the result
            dataViewModel.getLiveDataCheckLogin().removeObservers(getViewLifecycleOwner());
        });
    }

    private void handleSuccessfulLogin(String login, String password, int userId) {
        if (binding.cbxRemember.isChecked()) {
            saveCredentials(login, password);
        }

        saveUserId(userId);
        navigateToPlanningActivity();
    }

    private void saveCredentials(String login, String password) {
        editor.putString("login", login);
        editor.putString("password", password);
        editor.apply();
    }

    private void saveUserId(int userId) {
        editor.putInt(ID_KEY, userId);
        editor.apply();
    }

    private void navigateToPlanningActivity() {
        Intent intent = new Intent(requireActivity(), PlanningActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showLoading(boolean isLoading) {
        binding.progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}