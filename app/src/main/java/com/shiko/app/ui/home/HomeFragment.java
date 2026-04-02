package com.shiko.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shiko.app.R;
import com.shiko.app.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final int LANG_AZ = 0;
    private static final int LANG_EN = 1;
    private static final int LANG_RU = 2;

    private static final String KEY_FROM = "lang_from";
    private static final String KEY_TO   = "lang_to";

    private static final int[] FLAG_RES = {
        R.drawable.flag_az,
        R.drawable.flag_en,
        R.drawable.flag_ru
    };

    private FragmentHomeBinding binding;
    private int fromLang = LANG_AZ;
    private int toLang   = LANG_EN;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (savedInstanceState != null) {
            fromLang = savedInstanceState.getInt(KEY_FROM, LANG_AZ);
            toLang   = savedInstanceState.getInt(KEY_TO,   LANG_EN);
        }

        binding.btnFrom.setOnClickListener(v -> showPicker(v, true));
        binding.btnTo.setOnClickListener(v -> showPicker(v, false));
        binding.btnSwap.setOnClickListener(v -> swap());

        updateFlags();
        return binding.getRoot();
    }

    private void showPicker(View anchor, boolean isFrom) {
        List<Integer> options = new ArrayList<>();
        for (int lang = LANG_AZ; lang <= LANG_RU; lang++) {
            if (isFrom || lang != fromLang) {
                options.add(lang);
            }
        }

        ListPopupWindow popup = new ListPopupWindow(requireContext());
        popup.setAnchorView(anchor);
        popup.setWidth(anchor.getWidth());
        popup.setModal(true);
        popup.setAdapter(new LangAdapter(options));
        popup.setOnItemClickListener((parent, view, position, id) -> {
            int picked = options.get(position);
            if (isFrom) {
                fromLang = picked;
                if (toLang == fromLang) {
                    toLang = (fromLang == LANG_AZ) ? LANG_EN : LANG_AZ;
                }
            } else {
                toLang = picked;
            }
            updateFlags();
            popup.dismiss();
        });
        popup.show();
    }

    private void swap() {
        int tmp = fromLang;
        fromLang = toLang;
        toLang = tmp;
        updateFlags();
    }

    private void updateFlags() {
        binding.flagFrom.setImageResource(FLAG_RES[fromLang]);
        binding.flagTo.setImageResource(FLAG_RES[toLang]);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_FROM, fromLang);
        outState.putInt(KEY_TO, toLang);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ── Inline adapter ────────────────────────────────────────────────────────

    private class LangAdapter extends ArrayAdapter<Integer> {

        LangAdapter(List<Integer> langs) {
            super(requireContext(), R.layout.item_lang_popup, langs);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_lang_popup, parent, false);
            }
            int lang = Objects.requireNonNull(getItem(position));
            ((ImageView) convertView.findViewById(R.id.item_flag))
                    .setImageResource(FLAG_RES[lang]);
            ((TextView) convertView.findViewById(R.id.item_lang_name))
                    .setText(getLangName(lang));
            return convertView;
        }

        private String getLangName(int lang) {
            switch (lang) {
                case LANG_AZ: return getString(R.string.lang_az);
                case LANG_EN: return getString(R.string.lang_en);
                case LANG_RU: return getString(R.string.lang_ru);
                default:      return "";
            }
        }
    }
}
