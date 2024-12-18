import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private static final int MAX_OBJETOS = 5; // Límite del inventario general
    private static final int MAX_ARMAS = 3; // Límite de armas en el inventario
    private List<Equipo> objetos = new ArrayList<>(); // Inventario general del superviviente
    private List<Arma> armas = new ArrayList<>(); // Solo almacena armas activas

    public Equipo() {
        armas = new ArrayList<>();
        objetos = new ArrayList<>();

    }

    // Método para obtener las armas activas
    public List<Arma> obtenerArmasActivas() {
        List<Arma> armasActivas = new ArrayList<>();
        for (Arma arma : armas) {
            if (arma.isActiva()) {
                armasActivas.add(arma); // Solo agrega las armas que están activas
            }
        }
        return armasActivas;
    }

    // Método para activar un arma en particular
    public void activarArma(String nombreArma) {
        for (Arma arma : armas) {
            if (arma.getNombre().equals(nombreArma)) {
                arma.setActiva(true); // Activar el arma
                System.out.println("Arma " + nombreArma + " activada.");
            }
        }
    }

    // Método para añadir un objeto al inventario
    public void agregarItem(Equipo item) {
        // Crear armas y agregarlas al inventario
        Arma espada = new Arma("Espada", 1, 0, 1, 2);
        Arma pistola = new Arma("Pistola", 2, 1, 3, 3);
        Arma rifle = new Arma("Rifle", 3, 2, 5, 5);

        // Agregar las armas a la lista de armas activas
        armas.add(espada);
        armas.add(pistola);
        armas.add(rifle);

        // Crear provisiones y agregarlas al inventario
        Provisiones agua = new Provisiones("Agua", 0, "2025-12-31");
        Provisiones barritaEnergetica = new Provisiones("Barrita Energética", 250, "2024-08-15");
        Provisiones galletas = new Provisiones("Galletas de avena", 150, "2026-01-10");

        objetos.add(agua);
        objetos.add(barritaEnergetica);
        objetos.add(galletas);
    }

    // Método que será sobrescrito en clases hijas para obtener el nombre del equipo
    public String getNombre() {
        return "Equipo genérico"; // Placeholder
    }

    // Método para obtener los objetos del inventario
    public List<Equipo> obtenerObjetos() {
        return new ArrayList<>(objetos); // Devuelve una copia del inventario
    }

    // Método para obtener los nombres de los objetos del inventario
    public List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        for (Equipo item : objetos) {
            nombres.add(item.getNombre());
        }
        return nombres;
    }
}
