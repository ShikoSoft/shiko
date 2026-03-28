package com.shiko.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shiko.app.R;
import com.shiko.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final String KEY_SELECTED_LANG = "selected_lang";
    private static final int LANG_AZ = 0;
    private static final int LANG_EN = 1;
    private static final int LANG_RU = 2;

    private FragmentHomeBinding binding;
    private int selectedLang = LANG_AZ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (savedInstanceState != null) {
            selectedLang = savedInstanceState.getInt(KEY_SELECTED_LANG, LANG_AZ);
        }

        binding.flagAz.setOnClickListener(v -> setSelectedLang(LANG_AZ));
        binding.flagEn.setOnClickListener(v -> setSelectedLang(LANG_EN));
        binding.flagRu.setOnClickListener(v -> setSelectedLang(LANG_RU));

        applySelection();

        return binding.getRoot();
    }

    private void setSelectedLang(int lang) {
        selectedLang = lang;
        applySelection();
    }

    private void applySelection() {
        binding.flagAz.setBackgroundResource(selectedLang == LANG_AZ ? R.drawable.bg_flag_selected : 0);
        binding.flagEn.setBackgroundResource(selectedLang == LANG_EN ? R.drawable.bg_flag_selected : 0);
        binding.flagRu.setBackgroundResource(selectedLang == LANG_RU ? R.drawable.bg_flag_selected : 0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_LANG, selectedLang);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
