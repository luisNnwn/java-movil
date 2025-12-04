package com.example.cualma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class onboardingCualMa extends AppCompatActivity {

    ViewPager2 viewPager;
    LinearLayout dotsLayout;
    TextView[] dots;
    Button btnNext;

    List<onBoardingObjetos> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding_cual_ma);

        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dots);
        btnNext = findViewById(R.id.btnNext);

        setupOnboardingItems();
        setupViewPager();
        setupDots(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setupDots(position);

                if (position == items.size() - 1) {
                    btnNext.setText("Empezar");
                } else {
                    btnNext.setText("Siguiente");
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < items.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                saveOnboardingCompleted();
                startActivity(new Intent(onboardingCualMa.this, seleccionRol.class));
                finish();
            }
        });
    }

    private void setupOnboardingItems() {
        items = new ArrayList<>();

        items.add(new onBoardingObjetos(
                R.drawable.ob1,
                "Control Total de Alumnos y Materias",
                "Registra alumnos y asignaturas con facilidad. Agrega códigos, nombres y docentes."
        ));

        items.add(new onBoardingObjetos(
                R.drawable.ob2,
                "Edita y Actualiza Cuando Quieras",
                "Modifica materias, horarios y docentes sin complicaciones."
        ));

        items.add(new onBoardingObjetos(
                R.drawable.ic4,
                "Visualiza la información que te interesa",
                "Mira los docentes y materias de tu carrera y facultad"
        ));
    }

    private void setupViewPager() {
        onBoardingAdaptador adapter = new onBoardingAdaptador(items);
        viewPager.setAdapter(adapter);
    }

    private void setupDots(int position) {
        dots = new TextView[items.size()];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText("•");
            dots[i].setTextSize(35);
            dots[i].setTextColor(i == position ? 0xFF000000 : 0x55000000);
            dotsLayout.addView(dots[i]);
        }
    }

    private void saveOnboardingCompleted() {
        SharedPreferences prefs = getSharedPreferences("onboarding", MODE_PRIVATE);
        prefs.edit().putBoolean("done", true).apply();
    }
}
