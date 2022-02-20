package com.joinsdn.sprout.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.joinsdn.sprout.databinding.FragmentHomeBinding;
import com.joinsdn.sprout.services.ProximityService;

public class HomeFragment extends Fragment {

    Context ctx;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ctx = getContext();

        startService();

        return root;
    }

    @Override
    public void onDestroyView() {
        stopService();
        super.onDestroyView();
        binding = null;
    }

    public void startService() {
        Intent serviceIntent = new Intent(getActivity(), ProximityService.class);
        ContextCompat.startForegroundService(getActivity(), serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(getActivity(), ProximityService.class);
        ctx.stopService(serviceIntent);
    }

}