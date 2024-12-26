import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Equipo {
    private static final int MAX_OBJETOS = 5; // Límite del inventario general
    private static final int MAX_ARMAS = 3; // Límite de armas en el inventario
    private List<Object> objetos = new ArrayList<>(); // Inventario general, puede contener Arma, Provisiones, etc.
    private List<Arma> armasActivas = new ArrayList<>(); // Solo almacena armas activas

    public Equipo() {
        armasActivas = new ArrayList<>();
        objetos = new ArrayList<>();
    }

    // Método para obtener las armas activas
    public List<Arma> obtenerArmasActivas() {
        List<Arma> armasActivas = new ArrayList<>();
        for (Arma arma : this.armasActivas) {
            if (arma.isActiva()) {
                armasActivas.add(arma); // Solo agrega las armas que están activas
            }
        }
        return armasActivas;
    }
    
    // Método para obtener las armas no activas
    public List<Arma> obtenerArmasNA() {
        List<Arma> armasNA = new ArrayList<>();
        for (Object item : objetos) { // Iterar sobre todos los objetos
            if (item instanceof Arma) { // Comprobar si el objeto es una instancia de Arma
                Arma arma = (Arma) item; // Convertir a tipo Arma
                if (!arma.isActiva()) { // Comprobar si no está activa
                    armasNA.add(arma); // Añadir al resultado
                }
            }
        }
        return armasNA; // Devolver la lista de armas no activas
    }

    // Método para activar un arma
    public void activarArma(String nombreArma) {
        for (Arma arma : obtenerArmasNA()) {
            if (arma.getNombre().equals(nombreArma)) {
                arma.setActiva(true); // Activar el arma
                armasActivas.add(arma);
                System.out.println("Arma " + nombreArma + " activada.");
            } else System.out.println("No se pudo activar.");
        }
    }

    // Método para agregar un item al inventario
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
            objetos.add(itemRandom);
            System.out.println("Se añadió al inventario: " + itemRandom.getClass().getSimpleName());
        }
    }

    // Método para obtener los objetos del inventario
    public List<Object> obtenerObjetos() {
        return new ArrayList<>(objetos); // Devuelve una copia del inventario
    }

    // Obtener los nombres de las armas no activas
    public List<String> obtenerNombresArmasNA() {
        List<String> nombresArmasNA = new ArrayList<>();
        List<Arma> armasNA = obtenerArmasNA();
        for (Arma arma : armasNA) { // Iterar sobre las armas no activas
            nombresArmasNA.add(arma.getNombre()); // Obtener el nombre de cada arma
        }
        return nombresArmasNA;
    }

    // Obtener los nombres de las armas activas
    public List<String> obtenerNombresArmasActivas() {
        List<String> nombresArmasActivas = new ArrayList<>();
        List<Arma> armasActivas = obtenerArmasActivas();
        for (Arma arma : armasActivas) { // Iterar sobre las armas activas
            nombresArmasActivas.add(arma.getNombre()); // Obtener el nombre de cada arma
        }
        return nombresArmasActivas;
    }

    // Obtener los nombres de todos los objetos (armas y provisiones)
    public List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        for (Object item : objetos) {
            if (item instanceof Arma) {
                nombres.add(((Arma) item).getNombre()); // Si es un arma, agregamos su nombre
            } else if (item instanceof Provisiones) {
                nombres.add(((Provisiones) item).getNombre()); // Si es una provisión, agregamos su nombre
            }
        }
        return nombres;
    }
}
