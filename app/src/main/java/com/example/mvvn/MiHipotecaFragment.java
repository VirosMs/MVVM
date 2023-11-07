package com.example.mvvn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvn.databinding.FragmentMiHipotecaBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiHipotecaFragment extends Fragment {
    private FragmentMiHipotecaBinding binding;
    private MiHipotecaViewModel miHipotecaViewModel;
    private double capital;
    private int plazo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMiHipotecaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Crea el ViewModel y lo guarda en una variable
        MiHipotecaViewModel miHipotecaViewModel = new ViewModelProvider(this)
                .get(MiHipotecaViewModel.class);

        // Intenta transformar el capital y el plazo a double e int respectivamente
        // y si no se puede muestra un error
        binding.calcular.setOnClickListener(view1 -> {
            boolean error = false;

            try {
                capital = Double.parseDouble(binding.capital.getText().toString());
            } catch (Exception e){
                binding.capital.setError("Introduzca un número");
                error = true;
            }

            try {
                plazo = Integer.parseInt(binding.plazo.getText().toString());
            } catch (Exception e){
                binding.plazo.setError("Introduzca un número");
                error = true;
            }

            if (!error) {
                miHipotecaViewModel.calcular(capital, plazo);
            }
        });

        // Muestra la cuota
        miHipotecaViewModel.cuota.observe(getViewLifecycleOwner(), cuota ->
                binding.cuota.setText(String.format("%.2f €/mes", cuota)));


        // Comprueba si se ha introducido un capital inferior al mínimo y muestra el error
        miHipotecaViewModel.errorCapital.observe(getViewLifecycleOwner(), errorCapital ->
            binding.capital.setError(
                    errorCapital == null ? null : "El capital mínimo es de " + errorCapital + " €"));

        // Comprueba si se ha introducido un plazo inferior al mínimo y muestra el error
        miHipotecaViewModel.errorPlazos.observe(getViewLifecycleOwner(), errorPlazos ->
            binding.plazo.setError(
                    errorPlazos == null ? null : "El plazo mínimo es de " + errorPlazos + " años"));


        // Comprueba si se está calculando la cuota y deshabilita el botón de calcular
        // y muestra el mensaje de calculando
        miHipotecaViewModel.calculando.observe(getViewLifecycleOwner(),
                (Observer<Boolean>) calculando -> {
                    if(calculando) {
                        binding.calcular.setEnabled(false);
                        binding.calculando.setVisibility(View.VISIBLE);
                        binding.cuota.setVisibility(View.GONE);
                    } else {
                        binding.calcular.setEnabled(true);
                        binding.calculando.setVisibility(View.GONE);
                        binding.cuota.setVisibility(View.VISIBLE);
                    }
        });
    }
}
