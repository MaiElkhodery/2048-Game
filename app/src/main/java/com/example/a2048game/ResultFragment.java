package com.example.a2048game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a2048game.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {

    private static final String ARG_PARAM = "result";
    private String resultMSG;
    FragmentResultBinding binding;
    SetonClickAgain listener;

    public static ResultFragment newInstance(String resultMSG) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, resultMSG);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resultMSG = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentResultBinding.inflate(inflater, container, false);
        binding.resultTextView.setText(resultMSG+"!");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.newGameButton.setOnClickListener(view1 -> listener.again(this));
    }

    interface SetonClickAgain{
        void again(ResultFragment fragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SetonClickAgain)
            listener=(SetonClickAgain) context;
    }
}