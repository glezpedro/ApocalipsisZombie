

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private static final int MAX_OBJETOS = 5; // Límite del inventario
    private List<Equipo> objetos = new ArrayList<>(); // Inventario del superviviente

    // Método para añadir un objeto al inventario
    public boolean agregarItem(Equipo item) {
        if (objetos.size() < MAX_OBJETOS) {
            objetos.add(item);
            System.out.println("Agregado: " + item.getNombre());
            return true;
        } else {
            System.out.println("El inventario está lleno. No se puede agregar: " + item.getNombre());
            return false;
        }
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

    // Método que será sobrescrito en clases hijas para obtener el nombre del equipo
    public String getNombre() {
        return "Equipo genérico"; // Placeholder
    }
}