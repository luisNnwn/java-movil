package com.example.cualma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class onBoardingAdaptador extends RecyclerView.Adapter<onBoardingAdaptador.OnboardingViewHolder> {

    // Lista correcta
    private List<onBoardingObjetos> onboardingItems;

    // Constructor correcto
    public onBoardingAdaptador(List<onBoardingObjetos> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        // Ahora recibe onBoardingObjetos
        holder.setData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSlide;
        TextView title, description;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlide = itemView.findViewById(R.id.imgSlide);
            title = itemView.findViewById(R.id.txtTitulo);
            description = itemView.findViewById(R.id.txtDescripcion);
        }

        void setData(onBoardingObjetos item) {
            imageSlide.setImageResource(item.image);
            title.setText(item.title);
            description.setText(item.description);
        }
    }
}
