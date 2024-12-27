import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Equipo {
    private static final int MAX_OBJETOS = 5; // Límite del inventario general
    private static final int MAX_ARMAS = 3; // Límite de armas activas
    private List<Object> objetos = new ArrayList<>(); // Inventario general
    private List<Arma> armasActivas = new ArrayList<>(); // Armas activas

    public Equipo() {}

    // Obtener las armas activas
    public List<Arma> obtenerArmasActivas() {
        return new ArrayList<>(armasActivas); // Devuelve una copia para evitar modificaciones directas
    }

    // Obtener las armas no activas
    public List<Arma> obtenerArmasNA() {
        List<Arma> armasNA = new ArrayList<>();
        for (Object item : objetos) {
            if (item instanceof Arma) {
                Arma arma = (Arma) item;
                if (!arma.isActiva()) {
                    armasNA.add(arma);
                }
            }
        }
        return armasNA;
    }

    // Activar un arma si no se ha alcanzado el límite de armas activas
    public void activarArma(String nombreArma) {
        if (armasActivas.size() >= MAX_ARMAS) {
            System.out.println("Límite de armas activas alcanzado. No se puede activar más armas.");
            return;
        }

        for (Arma arma : obtenerArmasNA()) {
            if (arma.getNombre().equals(nombreArma)) {
                arma.setActiva(true);
                armasActivas.add(arma);
                System.out.println("Arma " + nombreArma + " activada.");
                return;
            }
        }

        System.out.println("No se encontró el arma " + nombreArma + " o ya está activa.");
    }

    // Agregar un item al inventario
    public void agregarItem() {
        if (objetos.size() >= MAX_OBJETOS) {
            System.out.println("Inventario lleno. No se pueden agregar más objetos.");
            return;
        }

        // Lista predefinida de posibles objetos
        List<Object> posiblesItems = List.of(
            new Arma("Espada", 1, 0, 1, 2),
            new Arma("Pistola", 2, 1, 3, 3),
            new Arma("Rifle", 3, 2, 5, 5),
            new Provisiones("Agua", 0, "2025-12-31"),
            new Provisiones("Barrita Energética", 250, "2024-08-15"),
            new Provisiones("Galletas de avena", 150, "2026-01-10")
        );

        // Selección aleatoria
        Random random = new Random();
        Object itemRandom = posiblesItems.get(random.nextInt(posiblesItems.size()));

        objetos.add(itemRandom);
        System.out.println("Se añadió al inventario: " + itemRandom.getClass().getSimpleName());
    }

    // Obtener los nombres de armas no activas
    public List<String> obtenerNombresArmasNA() {
        List<String> nombres = new ArrayList<>();
        for (Arma arma : obtenerArmasNA()) {
            nombres.add(arma.getNombre());
        }
        return nombres;
    }
    
    // Buscar un arma por su nombre
    public Arma obtenerArmaPorNombre(String nombreArma) {
        // Buscar en armas activas
        for (Arma arma : armasActivas) {
            if (arma.getNombre().equals(nombreArma)) {
                return arma; // Devuelve el arma activa encontrada
            }
        }
        
        return null; // Devuelve null si no se encuentra el arma
    }


    // Obtener los nombres de armas activas
    public List<String> obtenerNombresArmasActivas() {
        List<String> nombres = new ArrayList<>();
        for (Arma arma : armasActivas) {
            nombres.add(arma.getNombre());
        }
        return nombres;
    }
    
    // Método para obtener los objetos del inventario
    public List<Object> obtenerObjetos() {
        return new ArrayList<>(objetos); // Devuelve una copia del inventario
    }

    // Obtener todos los nombres de objetos
    public List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        for (Object item : objetos) {
            if (item instanceof Arma) {
                nombres.add(((Arma) item).getNombre());
            } else if (item instanceof Provisiones) {
                nombres.add(((Provisiones) item).getNombre());
            }
        }
        return nombres;
    }
}
