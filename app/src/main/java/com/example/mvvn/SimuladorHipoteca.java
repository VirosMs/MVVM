package com.example.mvvn;

/**
 * Clase SimuladorHipoteca que contiene el metodo calcular
 * que calcula la cuota de la hipoteca
 * y que implementa la interfaz Callback
 */
public class SimuladorHipoteca {

    /**
     * Clase Solicitud que contiene el capital y el plazo
     */
    public static class Solicitud {
        public double capital;
        public int plazo;

        /**
         * Constructor de la clase Solicitud
         * @param capital double capital
         * @param plazo int plazo
         */
        public Solicitud(double capital, int plazo) {
            this.capital = capital;
            this.plazo = plazo;
        }
    }

    /**
     * Interfaz que implementa el callback
     */
    interface Callback {
        void cuandoEsteCalculadaLaCuota(double cuota);
        void cuandoHayaErrorDeCapitalInferiorAlMinimo(double capitalMinimo);
        void cuandoHayaErrorDePlazoInferiorAlMinimo(int plazoMinimo);

        void cuandoEmpieceElCalculo();
        void cuandoFinaliceElCalculo();
    }

    /**
     * Crea la idea de que se esta calculando la cuota
     * llamando a un callback para utilizarlo en el ViewModel
     *
     * @param solicitud Solicitud
     * @param callback Callback
     */
    public void calcular(Solicitud solicitud, Callback callback) {
        // hace un callback para indicar que ha empezado el calculo
        callback.cuandoEmpieceElCalculo();

        //
        double interes = 0;
        double capitalMinimo = 0;
        int plazoMinimo = 0;

        // simular operacion de larga duracion (10s)
        try {

            Thread.sleep(10000);   // simular operacion de larga duracion (10s)
            interes = 0.01605; // 1.605% TAE
            capitalMinimo = 1000; // 1000€
            plazoMinimo = 2; // 2 meses
        } catch (InterruptedException e) {}

        boolean error = false;

        // comprueba si el capital es inferior al minimo y muestra el error
        if ( solicitud.capital < capitalMinimo){
            callback.cuandoHayaErrorDeCapitalInferiorAlMinimo(capitalMinimo);
            error = true;
        }

        // comprueba si el plazo es inferior al minimo y muestra el error
        if ( solicitud.plazo < plazoMinimo){
            callback.cuandoHayaErrorDePlazoInferiorAlMinimo(plazoMinimo);
            error = true;
        }

        // si no hay error calcula la cuota
        if(!error){
            callback.cuandoEsteCalculadaLaCuota(solicitud.capital*interes/12/(1-Math.pow(1+(interes/12),-solicitud.plazo*12)));
        }

        // hace un callback para indicar que ha finalizado el calculo
        callback.cuandoFinaliceElCalculo();
    }
}
