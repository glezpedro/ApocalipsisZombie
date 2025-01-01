
import java.util.ArrayList;

public class Ataque {
    private int[] dados;
    private String resultados;
    private VentanaJuego ventana;
    
    public Ataque(String armaSeleccionada1, Superviviente supervivienteActual, int x, int y, VentanaJuego ventana) {
        if (armaSeleccionada1 == null || armaSeleccionada1.trim().isEmpty()) {
            System.out.println("No se ha seleccionado un arma.");
            ventana.actualizarEtiqueta("No se ha seleccionado un arma.");
            return;
        }

        System.out.println("Atacando con " + armaSeleccionada1);

        Arma arma = supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);

        if (ventana.tablero.tablero[x][y].tieneZombie()) {
            ArrayList<Zombi> zombies = (ArrayList<Zombi>) ventana.tablero.tablero[x][y].getZombies();
            
            if (!zombies.isEmpty()) {
                Zombi objetivo = zombies.get(0); // Obtener el primer zombie
                int distancia = ventana.calcularDistancia(x, y, objetivo.getX(), objetivo.getY());
                
                objetivo.reaccionAtaques(arma, distancia);
                System.out.println(supervivienteActual.getNombre() + " atacó al zombie en (" + x + ", " + y + ") con " + armaSeleccionada1);

                ventana.atacarZombieSeleccionado(arma);
                
                supervivienteActual.gastarAccion();
                System.out.println("Acciones restantes para " + supervivienteActual.getNombre() + ": " + supervivienteActual.getAccionesDisponibles());
                ventana.accionesTotales++;
            } else {
                System.out.println("No hay zombies en esta casilla.");
                ventana.actualizarEtiqueta("No hay zombies en esta casilla.");
            }
        } else {
            System.out.println("No hay zombies en la casilla seleccionada.");
            ventana.actualizarEtiqueta("No hay zombies en la casilla seleccionada.");
        }

        if (ventana.accionesTotales == 12) {
            ventana.actualizarTurno(); 
        }
    }
}
