

public class Provisiones extends Equipo {
    private String nombre;
    private int valorEnergetico;
    private String caducidad; // Se cambia a String para manejar fechas simples

    public Provisiones(String nombre, int valorEnergetico, String caducidad) {
        this.nombre = nombre;
        this.valorEnergetico = valorEnergetico;
        this.caducidad = caducidad;
    }

    // Getters
    @Override
    public String getNombre() {
        return nombre;
    }

    public int getValorEnergetico() {
        return valorEnergetico;
    }

    public String getCaducidad() {
        return caducidad;
    }
}
