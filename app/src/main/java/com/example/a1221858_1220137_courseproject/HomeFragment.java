package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvWelcome = view.findViewById(R.id.tv_welcome_user);
        LinearLayout rootContainer = view.findViewById(R.id.home_root_container);

        if (getContext() != null) {
            Animation entryAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            rootContainer.startAnimation(entryAnimation);
        }

        return view;
    }
}