import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     public void agregarItem() {
    if (objetos.size() >= MAX_OBJETOS) {
        System.out.println("Número máximo de objetos alcanzado. No se pueden agregar más elementos.");
    } else {
        // Crear armas y provisiones
        Arma espada = new Arma("Espada", 1, 0, 1, 2);
        Arma pistola = new Arma("Pistola", 2, 1, 3, 3);
        Arma rifle = new Arma("Rifle", 3, 2, 5, 5);

        Provisiones agua = new Provisiones("Agua", 0, "2025-12-31");
        Provisiones barritaEnergetica = new Provisiones("Barrita Energética", 250, "2024-08-15");
        Provisiones galletas = new Provisiones("Galletas de avena", 150, "2026-01-10");

        // Crear lista de objetos posibles
        List<Object> posiblesItems = new ArrayList<>();
        posiblesItems.add(espada);
        posiblesItems.add(pistola);
        posiblesItems.add(rifle);
        posiblesItems.add(agua);
        posiblesItems.add(barritaEnergetica);
        posiblesItems.add(galletas);

        // Seleccionar un objeto aleatorio
        Random random = new Random();
        int indexAleatorio = random.nextInt(posiblesItems.size());
        Object itemRandom = posiblesItems.get(indexAleatorio);

        // Agregar al inventario si aún hay espacio
        objetos.add((Equipo) itemRandom);
        System.out.println("Se añadio al inventario " + objetos.getLast().getNombre());
    }
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
