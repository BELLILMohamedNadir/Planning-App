package com.example.planning.fragments;

import static com.example.planning.MainActivity.ID_KEY;
import static com.example.planning.PlanningActivity.navController2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planning.R;
import com.example.planning.databinding.FragmentNoteBinding;
import com.example.planning.interfaces.OnClick;
import com.example.planning.models.Note;
import com.example.planning.recyclerviews.DateRecyclerView;
import com.example.planning.utils.DateUtils;
import com.example.planning.viewmodels.DataViewModel;

import java.util.List;
import java.util.stream.IntStream;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private DataViewModel dataViewModel;
    private Note existingNote;
    private String currentDate;
    private List<String> dateList;
    private DateRecyclerView dateRecyclerView;
    private SharedPreferences sharedPreferences;


    public NoteFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        setupViews();
        setupObservers();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupViews() {
        setupSpinner();
        setupDateRecyclerView();
        handleExistingNote();
        setupClickListeners();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupDateRecyclerView() {
        dateList = DateUtils.generateYearDates();
        dateRecyclerView = new DateRecyclerView(requireActivity(), dateList, this::onDateSelected);

        binding.rvDate.setAdapter(dateRecyclerView);
        binding.rvDate.setLayoutManager(new LinearLayoutManager(
                requireActivity(),
                RecyclerView.HORIZONTAL,
                false
        ));
        binding.rvDate.setHasFixedSize(true);
    }

    private void onDateSelected(String date) {
        currentDate = date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleExistingNote() {
        Bundle args = getArguments();
        if (args != null && args.getParcelable("NOTE_KEY") != null) {
            binding.imgDelete.setVisibility(View.VISIBLE);
            existingNote = args.getParcelable("NOTE_KEY");

            binding.edtTitle.setText(existingNote.getTitle());
            binding.edtDescription.setText(existingNote.getDescription());
            currentDate = existingNote.getDate();

            setSpinnerSelection(existingNote.getTime());
        } else {
            currentDate = DateUtils.getCurrentDate();
        }

        scrollToCurrentDate();
    }

    private void setSpinnerSelection(String time) {
        switch (time) {
            case "8h - 10h": binding.spinner.setSelection(0); break;
            case "10h - 12h": binding.spinner.setSelection(1); break;
            case "14h - 16h": binding.spinner.setSelection(2); break;
            case "16h - 18h": binding.spinner.setSelection(3); break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupClickListeners() {
        binding.imgCheck.setOnClickListener(v -> handleNoteSave());
        binding.imgDelete.setOnClickListener(v -> handleNoteDelete());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleNoteSave() {
        String title = binding.edtTitle.getText().toString().trim();
        String description = binding.edtDescription.getText().toString().trim();
        int userId = sharedPreferences.getInt(ID_KEY, -1);

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userId == -1){
            Toast.makeText(requireContext(), "there is a problem", Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note(
                currentDate,
                binding.spinner.getSelectedItem().toString(),
                title,
                description,
                userId
        );

        if (existingNote != null) {
            note.setDate(existingNote.getDate()); // For update case
            dataViewModel.insertNote(note);
        } else {
            dataViewModel.insertNote(note);
        }
    }

    private void handleNoteDelete() {
        if (existingNote != null) {
            dataViewModel.deleteNote(existingNote);
        }
    }

    private void setupObservers() {
        dataViewModel.getLiveDataInsertedNote().observe(getViewLifecycleOwner(), success -> {
            if (success != null) {
                if (success) {
                    navController2.popBackStack();
                } else {
                    showToast("Insertion failed");
                }
                dataViewModel.reinitialiseLiveDataInsertedNote();
            }
        });

        dataViewModel.getLiveDataDeleteNote().observe(getViewLifecycleOwner(), success -> {
            if (success != null) {
                if (success) {
                    navController2.popBackStack();
                    showToast("Note deleted");
                } else {
                    showToast("Deletion failed");
                }
                dataViewModel.reinitialiseLiveDataDeleteNote();
            }
        });
    }

    private void scrollToCurrentDate() {
        int dateIndex = IntStream.range(0, dateList.size())
                .filter(i -> dateList.get(i).equals(currentDate))
                .findFirst()
                .orElse(-1);

        if (dateIndex != -1) {
            binding.rvDate.smoothScrollToPosition(dateIndex);
            dateRecyclerView.setSelectedPosition(dateIndex);
            dateRecyclerView.notifyDataSetChanged();
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}