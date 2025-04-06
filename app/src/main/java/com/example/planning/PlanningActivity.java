package com.example.planning;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.planning.databinding.ActivityMainBinding;
import com.example.planning.databinding.ActivityPlanningBinding;

public class PlanningActivity extends AppCompatActivity {

    ActivityPlanningBinding binding;
    public static NavController navController2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPlanningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        if (navHostFragment!=null)
            navController2 = navHostFragment.getNavController();

    }
}