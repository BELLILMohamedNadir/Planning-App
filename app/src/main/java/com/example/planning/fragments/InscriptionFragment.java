package com.example.planning.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.planning.MainActivity.ID_KEY;
import static com.example.planning.MainActivity.navController;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.planning.R;
import com.example.planning.databinding.FragmentInscriptionBinding;
import com.example.planning.models.Information;
import com.example.planning.utils.PasswordVisibility;
import com.example.planning.viewmodels.DataViewModel;
import java.util.Calendar;

public class InscriptionFragment extends Fragment {

    private FragmentInscriptionBinding binding;
    private DataViewModel dataViewModel;
    private SharedPreferences sharedPreferences;

    public InscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInscriptionBinding.inflate(inflater, container, false);
        dataViewModel = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        setupViews();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        cleanFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupViews() {
        // Password visibility toggle
        PasswordVisibility.managePasswordVisibility(binding.edtPasswordLayout, binding.edtPassword);
        PasswordVisibility.managePasswordVisibility(binding.edtConfirmPasswordLayout, binding.edtConfirmPassword);

        // Date picker
        binding.edtBirthday.setOnClickListener(v -> showDatePickerDialog(requireContext()));

        // Save button
        binding.btnSave.setOnClickListener(v -> {
            if (validateFields()) {
                checkLoginAvailability();
            }
        });
    }

    private void checkLoginAvailability() {
        // Remove previous observers
        dataViewModel.getLiveDataGetLogin().removeObservers(getViewLifecycleOwner());

        // Observe login availability
        dataViewModel.getLiveDataGetLogin().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String existingLogin) {
                if (existingLogin != null && !existingLogin.isEmpty()) {
                    binding.edtLogin.setError("Login already exists");
                    dataViewModel.getLiveDataGetLogin().removeObserver(this);
                } else {
                    dataViewModel.getLiveDataGetLogin().removeObserver(this);
                    createAndSaveUser();
                }
            }
        });

        dataViewModel.getLogin(binding.edtLogin.getText().toString());
    }

    private void createAndSaveUser() {
        Information information = new Information(
                binding.edtLogin.getText().toString(),
                binding.edtPassword.getText().toString(),
                binding.edtName.getText().toString(),
                binding.edtLastName.getText().toString(),
                binding.edtBirthday.getText().toString(),
                binding.edtNumberPhone.getText().toString(),
                binding.edtEmail.getText().toString(),
                binding.cbxSport.isChecked(),
                binding.cbxMusic.isChecked(),
                binding.cbxLecture.isChecked()
        );

        // Observe insertion result
        Observer<Long> insertionObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long insertedId) {
                if (insertedId != null) {
                    dataViewModel.getLiveDataInsertedInformation().removeObserver(this);

                    if (insertedId != -1) {
                        saveUserIdAndNavigate(insertedId);
                    }
                }
            }
        };

        dataViewModel.getLiveDataInsertedInformation()
                .observe(getViewLifecycleOwner(), insertionObserver);
        dataViewModel.insertInformation(information);
    }

    private void saveUserIdAndNavigate(long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID_KEY, Math.toIntExact(userId));
        editor.apply();

        Bundle bundle = new Bundle();
        bundle.putString("source", "inscriptionFragment");
        navController.navigate(R.id.profilFragment, bundle);
        dataViewModel.reinitialiseLiveDataInsertedInformation();
    }

    private void showDatePickerDialog(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, yearSelected, monthSelected, daySelected) -> {
                    String selectedDate = daySelected + "/" + (monthSelected + 1) + "/" + yearSelected;
                    binding.edtBirthday.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean validateFields() {
        boolean isValid = true;

        String login = binding.edtLogin.getText().toString().trim();
        if (!login.matches("^[a-zA-Z][a-zA-Z0-9]{0,9}$")) {
            binding.edtLogin.setError("Le login doit commencer par une lettre et contenir max 10 caractères");
            isValid = false;
        }

        String password = binding.edtPassword.getText().toString().trim();
        if (password.length() < 6) {
            binding.edtPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
            isValid = false;
        }

        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();
        if (!confirmPassword.equals(password)) {
            binding.edtConfirmPassword.setError("Les mots de passe ne correspondent pas");
            isValid = false;
        }

        String name = binding.edtName.getText().toString().trim();
        if (!name.matches("^[a-zA-Z]+$")) {
            binding.edtName.setError("Le nom ne doit contenir que des lettres");
            isValid = false;
        }

        String lastName = binding.edtLastName.getText().toString().trim();
        if (!lastName.matches("^[a-zA-Z]+$")) {
            binding.edtLastName.setError("Le prénom ne doit contenir que des lettres");
            isValid = false;
        }

        String birthday = binding.edtBirthday.getText().toString().trim();
        if (birthday.isEmpty()) {
            binding.edtBirthday.setError("Veuillez entrer votre date de naissance");
            isValid = false;
        }

        String phone = binding.edtNumberPhone.getText().toString().trim();
        if (!phone.matches("^[0-9]{10}$")) {
            binding.edtNumberPhone.setError("Le numéro doit contenir 10 chiffres");
            isValid = false;
        }

        String email = binding.edtEmail.getText().toString().trim();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Email invalide");
            isValid = false;
        }

        return isValid;
    }

    private void cleanFields() {
        binding.edtBirthday.setText("");
        binding.edtLogin.setText("");
        binding.edtName.setText("");
        binding.edtEmail.setText("");
        binding.edtPassword.setText("");
        binding.edtConfirmPassword.setText("");
        binding.edtLastName.setText("");
        binding.edtNumberPhone.setText("");
        binding.cbxSport.setChecked(true);
        binding.cbxLecture.setChecked(false);
        binding.cbxMusic.setChecked(false);
    }
}