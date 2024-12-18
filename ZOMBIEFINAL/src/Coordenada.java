
public class Coordenada {
    private int fila;
    private int columna;
    VentanaJuego ventana;
    
    public Coordenada(VentanaJuego ventanaMain) {
        this.ventana= ventanaMain;
        this.fila = ventana.tablero.getCoordenadaXSeleccionada();
        this.columna = ventana.tablero.getCoordenadaYSeleccionada();
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
