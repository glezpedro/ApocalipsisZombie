import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Arma extends Equipo implements Serializable{
    private String nombre;
    private int potencia;
    private int alcance;
    private int numDados;
    private int valorExito;
    private boolean activa;

    public Arma(String nombre, int potencia, int alcance, int numDados, int valorExito) {
        this.nombre = nombre;
        this.potencia = potencia;
        this.alcance = alcance;
        this.numDados = numDados;
        this.valorExito = valorExito;
        this.activa = false; 
    }

    public String getNombre() {
        return nombre;
    }

    public int getPotencia() {
        return potencia;
    }

    public int getAlcance() {
        return alcance;
    }

    public int getNumDados() {
        return numDados;
    }

    public int getValorExito() {
        return valorExito;
    }

    public boolean isActiva() {
        return activa;
    }

    // Método para activar el arma
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
   

    public int lanzarDados(List<Integer> valoresDados) {
        Random random = new Random();
        int exitos = 0;

        for (int i = 0; i < numDados; i++) {
            int resultado = random.nextInt(6) + 1;
            valoresDados.add(resultado); // Guardar los valores de los dados
            if (resultado >= valorExito) {
                exitos++;
            }
        }
        return exitos;
    }
    
    

}
