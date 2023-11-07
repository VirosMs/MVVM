package com.example.mvvn;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MiHipotecaViewModel extends AndroidViewModel {
    Executor executor;

    SimuladorHipoteca sH;

    MutableLiveData<Double> cuota = new MutableLiveData<>();
    MutableLiveData<Double> errorCapital = new MutableLiveData<>();
    MutableLiveData<Integer> errorPlazos = new MutableLiveData<>();
    MutableLiveData<Boolean> calculando = new MutableLiveData<>();
    public MiHipotecaViewModel(@NonNull Application application) {
        super(application);

        executor = Executors.newSingleThreadExecutor();

        sH = new SimuladorHipoteca();
    }

    public void calcular(double capital, int plazo) {

        final SimuladorHipoteca.Solicitud solicitud = new SimuladorHipoteca.Solicitud(capital, plazo);

        //Codigo con lambda (simplificado) que implementa la interfaz Callback de SimuladorHipoteca
        // y que se pasa como parametro al metodo calcular de SimuladorHipoteca
        executor.execute(() -> sH.calcular(solicitud, new SimuladorHipoteca.Callback() {
            /**
             * Cuando este calculada la cuota resultante
             * @param cuotaResultante double cuota resultante
             */
            @Override
            public void cuandoEsteCalculadaLaCuota(double cuotaResultante) {
                errorCapital.postValue(null);
                errorPlazos.postValue(null);
                cuota.postValue(cuotaResultante);
            }


            /**
             * Cuando haya error de capital inferior al minimo
             * @param capitalMinimo double capital minimo
             */
            @Override
            public void cuandoHayaErrorDeCapitalInferiorAlMinimo(double capitalMinimo) {
                errorCapital.postValue(capitalMinimo);
            }

            /**
             * Cuando haya error de plazo inferior al minimo
             * @param plazoMinimo int plazo minimo
             */
            @Override
            public void cuandoHayaErrorDePlazoInferiorAlMinimo(int plazoMinimo) {
                errorPlazos.postValue(plazoMinimo);
            }

            /**
             * Cuando empiece el calculo
             */
            @Override
            public void cuandoEmpieceElCalculo() {
                calculando.postValue(true);
            }

            /**
             * Cuando finalice el calculo
             */
            @Override
            public void cuandoFinaliceElCalculo() {
                calculando.postValue(false);
            }
        }));

        //Codigo sin lambda
        /*executor.execute(new Runnable() {
            @Override
            public void run() {

                simulador.calcular(solicitud, new SimuladorHipoteca.Callback() {
                    @Override
                    public void cuandoEsteCalculadaLaCuota(double cuotaResultante) {
                        errorCapital.postValue(null);
                        errorPlazos.postValue(null);
                        cuota.postValue(cuotaResultante);
                    }

                    @Override
                    public void cuandoHayaErrorDeCapitalInferiorAlMinimo(double capitalMinimo) {
                        errorCapital.postValue(capitalMinimo);
                    }

                    @Override
                    public void cuandoHayaErrorDePlazoInferiorAlMinimo(int plazoMinimo) {
                        errorPlazos.postValue(plazoMinimo);
                    }

                     @Override
                    public void cuandoEmpieceElCalculo() {
                        calculando.postValue(true);
                    }

                    @Override
                    public void cuandoFinaliceElCalculo() {
                        calculando.postValue(false);
                    }
                });
            }
        });*/
    }
}
