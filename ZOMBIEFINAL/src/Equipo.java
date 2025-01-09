import java.io.Serializable;
import java.util.*;


public class Equipo implements Serializable {
    private static final int MAX_OBJETOS = 5; // Límite del inventario general
    private static final int MAX_ARMAS = 2; // Límite de armas activas
    private List<Object> objetos = new ArrayList<>(); // Inventario general
    private List<Arma> armasActivas = new ArrayList<>(); // Armas activas

    public Equipo() {}

    public List<Arma> obtenerArmasActivas() {
        return new ArrayList<>(armasActivas); // Devuelve una copia para evitar modificaciones directas
    }

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

    public void agregarItem() {
        if (objetos.size() >= MAX_OBJETOS) {
            System.out.println("Inventario lleno. No se pueden agregar más objetos.");
            return;
        }

        List<Object> posiblesItems = List.of(
            new Arma("Espada", 1, 0, 1, 2),
            new Arma("Pistola", 2, 1, 3, 3),
            new Arma("Rifle", 3, 2, 5, 5),
            new Provisiones("Agua", 0, "2025-12-31"),
            new Provisiones("Barrita Energética", 250, "2024-08-15"),
            new Provisiones("Galletas de avena", 150, "2026-01-10")
        );

        Random random = new Random();
        Object itemRandom;

        while (true) {
            itemRandom = posiblesItems.get(random.nextInt(posiblesItems.size()));

            if (itemRandom instanceof Arma) {
                Arma nuevaArma = (Arma) itemRandom;
                boolean armaRepetida = objetos.stream()
                    .filter(obj -> obj instanceof Arma)
                    .map(obj -> (Arma) obj)
                    .anyMatch(arma -> arma.getNombre().equals(nuevaArma.getNombre()));

                if (armaRepetida) {
                    System.out.println("El arma " + nuevaArma.getNombre() + " ya está en el inventario. Seleccionando otro ítem...");
                    continue;
                }
            }
            break;
        }

        // Agregar el ítem
        objetos.add(itemRandom);
        System.out.println("Se añadió al inventario: " + 
            (itemRandom instanceof Arma ? ((Arma) itemRandom).getNombre() : ((Provisiones) itemRandom).getNombre()));
    }

    public List<String> obtenerNombresArmasNA() {
        List<String> nombres = new ArrayList<>();
        for (Arma arma : obtenerArmasNA()) {
            nombres.add(arma.getNombre());
        }
        return nombres;
    }
    
    public Arma obtenerArmaPorNombre(String nombreArma) {
        for (Arma arma : armasActivas) {
            if (arma.getNombre().equals(nombreArma)) {
                return arma; 
            }
        }
        return null; 
    }

    public List<String> obtenerNombresArmasActivas() {
        List<String> nombres = new ArrayList<>();
        for (Arma arma : armasActivas) {
            nombres.add(arma.getNombre());
        }
        return nombres;
    }
    
    public List<Object> obtenerObjetos() {
        return new ArrayList<>(objetos); // Devuelve una copia del inventario
    }

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
    
    public void desactivarArma(String nombreArma) {
        Arma arma = obtenerArmaPorNombre(nombreArma);
        if (arma != null) {
            arma.setActiva(false);
            armasActivas.remove(arma);
            System.out.println("Arma " + nombreArma + " desactivada.");
        } else {
            System.out.println("No se encontró el arma activa con el nombre " + nombreArma);
        }
    }

}
