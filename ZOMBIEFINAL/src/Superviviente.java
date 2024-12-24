import java.util.ArrayList;
import java.util.List;
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
    public void moverse() {   
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

    public static List<Superviviente> crearSupervivientes() {
        List<Superviviente> supervivientes = new ArrayList<>();
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        int[][] coordenadas = {
            {0, 0}, {9, 0}, {0, 9}, {9, 9}
        };

        Random random = new Random(4);

        int[] coordenadaSeleccionada = coordenadas[random.nextInt(coordenadas.length)];

        for (String nombre : colores) {
            int salud = random.nextInt(50) + 50;
            int maxHeridas = random.nextInt(3) + 3;
            Superviviente superviviente = new Superviviente(nombre, salud, maxHeridas);

            superviviente.coordenadaX = coordenadaSeleccionada[0];
            superviviente.coordenadaY = coordenadaSeleccionada[1];

            supervivientes.add(superviviente);
        }

        return supervivientes;
    }

    
    
    
    @Override
    public void atacar(List<Superviviente> supervivientes) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
