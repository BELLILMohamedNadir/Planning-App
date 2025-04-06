package com.example.planning.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.planning.MainActivity.ID_KEY;
import static com.example.planning.MainActivity.navController;
import static com.example.planning.PlanningActivity.navController2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.planning.R;
import com.example.planning.databinding.FragmentProfilBinding;
import com.example.planning.models.Information;
import com.example.planning.viewmodels.DataViewModel;

public class ProfilFragment extends Fragment {

    private FragmentProfilBinding binding;
    private DataViewModel dataViewModel;
    private SharedPreferences sharedPreferences;
    private String fragmentSource = "inscriptionFragment";

    public ProfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfilBinding.inflate(inflater, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        parseArguments();
        setupViews();
        loadUserInformation();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void parseArguments() {
        if (getArguments() != null) {
            fragmentSource = getArguments().getString("source", fragmentSource);
        }
    }

    private void setupViews() {
        disableAllInputs();
        setupBackButton();
    }

    private void disableAllInputs() {
        disableEditText(binding.edtLogin);
        disableEditText(binding.edtName);
        disableEditText(binding.edtLastName);
        disableEditText(binding.edtEmail);
        disableEditText(binding.edtNumberPhone);
        disableEditText(binding.edtBirthday);

        disableCheckBox(binding.cbxLecture);
        disableCheckBox(binding.cbxMusic);
        disableCheckBox(binding.cbxSport);
    }

    private void setupBackButton() {
        binding.imgBack.setOnClickListener(v -> navigateBackToSource());
    }

    private void navigateBackToSource() {
        switch (fragmentSource) {
            case "inscriptionFragment":
                navController.popBackStack(R.id.inscriptionFragment, false);
                break;
            case "planningFragment":
                navController2.popBackStack(R.id.planningFragment, false);
                break;
        }
    }

    private void loadUserInformation() {
        long userId = sharedPreferences.getInt(ID_KEY, -1);
        if (userId != -1) {
            observeUserInformation(userId);
        }
    }

    private void observeUserInformation(long userId) {
        dataViewModel.getInformation(userId);
        dataViewModel.getLiveDataGetInformation().observe(getViewLifecycleOwner(), information -> {
            if (information != null) {
                bindUserInformation(information);
                dataViewModel.reinitialiseLiveDataGetInformation();
            }
        });
    }

    private void bindUserInformation(Information information) {
        binding.edtLogin.setText(information.getLogin());
        binding.edtName.setText(information.getName());
        binding.edtLastName.setText(information.getLastName());
        binding.edtEmail.setText(information.getEmail());
        binding.edtNumberPhone.setText(information.getNumberPhone());
        binding.edtBirthday.setText(information.getBirthday());
        binding.cbxLecture.setChecked(information.isLecture());
        binding.cbxMusic.setChecked(information.isMusic());
        binding.cbxSport.setChecked(information.isSport());
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private void disableCheckBox(CheckBox checkBox) {
        checkBox.setClickable(false);
        checkBox.setFocusable(false);
    }
}