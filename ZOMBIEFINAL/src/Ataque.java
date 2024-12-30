
import java.util.ArrayList;

public class Ataque {
    private int[] dados;
    private String resultados;
    private VentanaJuego ventana;
    
    public Ataque(String armaSeleccionada1, Superviviente supervivienteActual, int x, int y, VentanaJuego ventana) {
        // Verificar si no se seleccionó un arma
        if (armaSeleccionada1 == null || armaSeleccionada1.trim().isEmpty()) {
            System.out.println("No se ha seleccionado un arma.");
            ventana.actualizarEtiqueta("No se ha seleccionado un arma.");
            return;
        }

        System.out.println("Atacando con " + armaSeleccionada1);

        // Obtener el arma seleccionada
        Arma arma = supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);

        // Verificar si hay un zombie en la casilla seleccionada
        if (ventana.tablero.tablero[x][y].tieneZombie()) {
            // Obtener la lista de zombies en la casilla
            ArrayList<Zombi> zombies = (ArrayList<Zombi>) ventana.tablero.tablero[x][y].getZombies();
            
            // Verificar que la lista no esté vacía antes de acceder al primer zombie
            if (!zombies.isEmpty()) {
                Zombi objetivo = zombies.get(0); // Obtener el primer zombie
                int distancia = ventana.calcularDistancia(x, y, objetivo.getX(), objetivo.getY());
                
                // Ejecutar el ataque al zombie
                objetivo.reaccionAtaques(arma, distancia);
                System.out.println(supervivienteActual.getNombre() + " atacó al zombie en (" + x + ", " + y + ") con " + armaSeleccionada1);

                ventana.atacarZombieSeleccionado(arma);
                
                // Gastar acción
                supervivienteActual.gastarAccion();
                System.out.println("Acciones restantes para " + supervivienteActual.getNombre() + ": " + supervivienteActual.getAccionesDisponibles());
                ventana.accionesTotales++;
            } else {
                // Si no hay zombies en la casilla
                System.out.println("No hay zombies en esta casilla.");
                ventana.actualizarEtiqueta("No hay zombies en esta casilla.");
            }
        } else {
            // Si no hay zombies en la casilla seleccionada
            System.out.println("No hay zombies en la casilla seleccionada.");
            ventana.actualizarEtiqueta("No hay zombies en la casilla seleccionada.");
        }

        // Verificar si el turno debe finalizar
        if (ventana.accionesTotales == 12) {
            ventana.actualizarTurno(); 
        }
    }
}
