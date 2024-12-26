import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class Superviviente implements Activable {
    private String nombre;
    private int salud;
    private Equipo inventario; // Inventario que contiene armas
    private int maxHeridas; // Máximo de heridas antes de quedar fuera
    private int coordenadaX;
    private int coordenadaY;
    private boolean envenenado;
    private boolean isEliminado;
    private final int NUM_SUPERVIVIENTES = 4;
    private static final String[] COLORES = {"Rojo", "Azul", "Verde", "Amarillo"};
    private static final int[][] COORDENADAS = {{0, 0}, {9, 0}, {0, 9}, {9, 9}};
    private static int index = 0; // Controla el progreso de los colores
    private static final Random random = new Random(4);
    private static int[] coordenadaSeleccionada = null;



    public Superviviente(String nombre, int salud, int maxHeridas) {
        this.nombre = nombre;
        this.salud = salud;
        this.maxHeridas = maxHeridas;
        this.inventario = new Equipo(); // Inicializa un inventario vacío
        this.envenenado = false; // Estado inicial
        this.coordenadaX = 0; // Posición inicial
        this.coordenadaY = 0;
        this.isEliminado = false; // Al crearse no esta eliminado
    }
    
    public void recibirHerida(int n) {
        salud -= n;
        if (salud <= 0) {
            System.out.println(nombre + " ha sido eliminado.");
        } else {
            System.out.println(nombre + " ha recibido " + n + " heridas. Salud restante: " + salud);
        }
    }

    public void envenenar() {
        envenenado = true;
        System.out.println(nombre + " ha sido envenenado.");
    }

    public void aplicarEfectos() {
        if (envenenado) {
            recibirHerida(1); // Pierde 1 de salud por turno
            System.out.println(nombre + " sufre daño por veneno.");
        }
    }

    @Override
    public int[] getCoordenadas() {
        return new int[]{coordenadaX, coordenadaY};
    }

    public Equipo getInventario() {
        return inventario;
    }

    public int getSalud() {
        return salud;
    }
    public String getNombre() {
        return nombre;
    }
    
    
    public int getX(){
        return coordenadaX;
    }
    
    public int getY(){
        return coordenadaY;
    }

    public boolean isIsEliminado() {
        return isEliminado;
    }
    
    
    @Override
    public void atacar(List<Superviviente> supervivientes) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void atacar(List<Zombi> zombis, Arma arma) {
        for (Zombi zombi : zombis) {
            int distancia = Math.abs(zombi.getX() - coordenadaX) + Math.abs(zombi.getY() - coordenadaY);
            if (distancia <= arma.getAlcance()) {
                System.out.println(nombre + " ataca al zombi " + zombi.getX() + "," + zombi.getY() + " con " + arma.getNombre());
                if (zombi.reaccionAtaques(arma, distancia) == 1) {
                    System.out.println("¡El zombi ha sido eliminado!");
                }
                return;
            }
        }
        System.out.println("No hay zombis en el alcance.");
    }
    
    public static void seleccionarCoordenadasAleatorias() {
        if (coordenadaSeleccionada == null) {
            // Si no se ha seleccionado aún, elige una coordenada aleatoria entre las 4 posibles
            coordenadaSeleccionada = COORDENADAS[random.nextInt(COORDENADAS.length)];
        }
    }

    // Nuevo método para obtener una lista de 4 supervivientes
    public static List<Superviviente> crearSupervivientes() {
        List<Superviviente> supervivientes = new ArrayList<>();

        // Seleccionamos las coordenadas aleatorias si no se han seleccionado
        if (coordenadaSeleccionada == null) {
            seleccionarCoordenadasAleatorias();
        }

        // Creamos 4 supervivientes con las mismas coordenadas
        for (int i = 0; i < 4; i++) {
            String nombre = COLORES[i];
            int salud = random.nextInt(50) + 50;
            int maxHeridas = random.nextInt(3) + 3;

            Superviviente superviviente = new Superviviente(nombre, salud, maxHeridas);
            superviviente.coordenadaX = coordenadaSeleccionada[0];  // Usamos la misma coordenada para todos
            superviviente.coordenadaY = coordenadaSeleccionada[1];

            supervivientes.add(superviviente);
        }

        return supervivientes;
    }
    
    public void buscar(Tablero tablero) {
        
    }
    
    @Override
    public void moverse(int nuevaX, int nuevaY) {
        // Validación opcional: Verificar que el movimiento es válido según el tablero
        if (nuevaX < 0 || nuevaY < 0) {
            System.out.println("Movimiento fuera de los límites.");
            return;
        }

        // Actualizar coordenadas
        this.coordenadaX = nuevaX;
        this.coordenadaY = nuevaY;

        // Mensaje de confirmación
        System.out.println(nombre + " se ha movido a la posición (" + nuevaX + ", " + nuevaY + ").");
    }


    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Si es el mismo objeto, son iguales
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Si es null o no es de la misma clase, no son iguales
        }
        Superviviente that = (Superviviente) obj;
        if (this.nombre == null) {
            return that.nombre == null; // Ambos nombres son null
        }
        return this.nombre.equals(that.nombre); // Compara los nombres manualmente
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    
}
