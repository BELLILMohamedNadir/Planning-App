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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.planning.R;
import com.example.planning.databinding.FragmentPlanningBinding;
import com.example.planning.interfaces.OnClick;
import com.example.planning.models.Note;
import com.example.planning.recyclerviews.DateRecyclerView;
import com.example.planning.utils.DateUtils;
import com.example.planning.viewmodels.DataViewModel;

import java.util.List;
import java.util.stream.IntStream;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PlanningFragment extends Fragment {

    private FragmentPlanningBinding binding;
    private DataViewModel dataViewModel;
    private DateRecyclerView dateRecyclerView;
    private List<String> dateList;
    private String currentDate;
    private SharedPreferences sharedPreferences;

    // Card visibility states
    private boolean isCard810Visible = false;
    private boolean isCard1012Visible = false;
    private boolean isCard1416Visible = false;
    private boolean isCard1618Visible = false;

    int userId;

    public PlanningFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanningBinding.inflate(inflater, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        initializeViews();
        setupDateRecyclerView();
        setupClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        resetCardVisibility();
        loadNotesForCurrentDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initializeViews() {
        currentDate = DateUtils.getCurrentDate();
        binding.txtDate.setText(currentDate);
    }

    private void setupDateRecyclerView() {
        dateList = DateUtils.generateYearDates();
        dateRecyclerView = new DateRecyclerView(requireActivity(), dateList, this::onDateSelected);

        binding.rvDate.setAdapter(dateRecyclerView);
        binding.rvDate.setLayoutManager(new LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
        ));
        binding.rvDate.setHasFixedSize(true);

        scrollToCurrentDate();
    }

    private void onDateSelected(String date) {
        resetCardVisibility();
        currentDate = date;
        binding.txtDate.setText(date);
        loadNotesForDate(date);
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

    private void setupClickListeners() {
        binding.floatAdd.setOnClickListener(v ->
                navController2.navigate(R.id.action_planningFragment_to_noteFragment)
        );

        binding.imgProfil.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("source", "planningFragment");
            navController2.navigate(R.id.profilFragment2, bundle);
        });

        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        binding.card810.setOnClickListener(v -> navigateToNoteFragment(
                binding.txtTitle810.getText().toString(),
                binding.txtDescription810.getText().toString(),
                0
        ));

        binding.card1012.setOnClickListener(v -> navigateToNoteFragment(
                binding.txtTitle1012.getText().toString(),
                binding.txtDescription1012.getText().toString(),
                1
        ));

        binding.card1416.setOnClickListener(v -> navigateToNoteFragment(
                binding.txtTitle1416.getText().toString(),
                binding.txtDescription1416.getText().toString(),
                2
        ));

        binding.card1618.setOnClickListener(v -> navigateToNoteFragment(
                binding.txtTitle1618.getText().toString(),
                binding.txtDescription1618.getText().toString(),
                3
        ));
    }

    private void navigateToNoteFragment(String title, String description, int timeIndex) {
        Bundle bundle = new Bundle();
        Note note = new Note(
                currentDate,
                getResources().getStringArray(R.array.spinner_items)[timeIndex],
                title,
                description,
                userId
        );
        bundle.putParcelable("NOTE_KEY", note);
        navController2.navigate(R.id.noteFragment, bundle);
    }

    private void loadNotesForCurrentDate() {
        loadNotesForDate(currentDate);
    }

    private void loadNotesForDate(String date) {
        userId = sharedPreferences.getInt(ID_KEY, -1);
        if (userId != -1){
            dataViewModel.getNotes(userId, date);
            dataViewModel.getLiveDataGetNotes().observe(getViewLifecycleOwner(), notes -> {
                if (notes != null) {
                    processNotes(notes);
                }
                updateCardVisibility();
            });
        }
    }

    private void processNotes(List<Note> notes) {
        resetCardVisibility();

        if (notes.isEmpty()) return;

        for (Note note : notes) {
            switch (note.getTime()) {
                case "8h - 10h":
                    bindNoteToCard(note, binding.txtTitle810, binding.txtDescription810);
                    isCard810Visible = true;
                    break;
                case "10h - 12h":
                    bindNoteToCard(note, binding.txtTitle1012, binding.txtDescription1012);
                    isCard1012Visible = true;
                    break;
                case "14h - 16h":
                    bindNoteToCard(note, binding.txtTitle1416, binding.txtDescription1416);
                    isCard1416Visible = true;
                    break;
                case "16h - 18h":
                    bindNoteToCard(note, binding.txtTitle1618, binding.txtDescription1618);
                    isCard1618Visible = true;
                    break;
            }
        }
    }

    private void bindNoteToCard(Note note, TextView titleView, TextView descriptionView) {
        titleView.setText(note.getTitle());
        descriptionView.setText(note.getDescription());
    }

    private void resetCardVisibility() {
        isCard810Visible = false;
        isCard1012Visible = false;
        isCard1416Visible = false;
        isCard1618Visible = false;
    }

    private void updateCardVisibility() {
        binding.card810.setVisibility(isCard810Visible ? View.VISIBLE : View.GONE);
        binding.card1012.setVisibility(isCard1012Visible ? View.VISIBLE : View.GONE);
        binding.card1416.setVisibility(isCard1416Visible ? View.VISIBLE : View.GONE);
        binding.card1618.setVisibility(isCard1618Visible ? View.VISIBLE : View.GONE);
    }
}