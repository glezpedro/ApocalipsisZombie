import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ataque implements Serializable {
    private int[] dados;
    private String resultados;
    private VentanaJuego ventana;

    // Constructor para inicializar ventana
    public Ataque(VentanaJuego ventana) {
        if (ventana == null) {
            throw new IllegalArgumentException("La ventana no puede ser null");
        }
        this.ventana = ventana;
    }

    public void atacarZombie(Arma armaSeleccionada, Superviviente supervivienteActual, int coordenadaXSeleccionada, int coordenadaYSeleccionada) {
        List<Integer> valoresDados = new ArrayList<>();
        int exitos = armaSeleccionada.lanzarDados(valoresDados);
        System.out.println("Éxitos iniciales: " + exitos);

        while (exitos > 0) {
            if (coordenadaXSeleccionada != -1 && coordenadaYSeleccionada != -1) {
                Zombi zombieAtacado = ventana.buscarZombie(coordenadaXSeleccionada, coordenadaYSeleccionada);

                if (zombieAtacado != null) {
                    int distancia = ventana.calcularDistancia(supervivienteActual.getX(), supervivienteActual.getY(), zombieAtacado.getX(), zombieAtacado.getY());
                    int resultado = zombieAtacado.reaccionAtaques(armaSeleccionada, distancia);
                    manejarResultadoAtaque(coordenadaXSeleccionada, coordenadaYSeleccionada, valoresDados, supervivienteActual, zombieAtacado, resultado);
                    exitos--;
                } else {
                    System.out.println("No hay un zombie en la casilla seleccionada.");
                    ventana.actualizarEtiqueta("No hay un zombie en la casilla seleccionada.");
                    break;
                }
            } else {
                System.out.println("Por favor, selecciona una casilla válida antes de atacar.");
                ventana.actualizarEtiqueta("Por favor, selecciona una casilla válida antes de atacar.");
                break;
            }
        }

        ventana.actualizarIconos();
        ventana.panelJuego.revalidate();
        ventana.panelJuego.repaint();
        System.out.println("El ataque ha finalizado. Éxitos restantes: " + exitos);
    }

    private void manejarResultadoAtaque(int x, int y, List<Integer> valoresDados, Superviviente superviviente, Zombi zombie, int resultado) {
        String resultadoTexto = "";
        Superviviente supervivienteActual = ventana.supervivientes.get(ventana.indiceActual);
        switch (resultado) {
            case 0:
                resultadoTexto = "El zombie sigue vivo, el arma no tiene alcance o potencia suficiente.";
                ventana.actualizarEtiqueta("El zombie sigue vivo, el arma no tiene alcance o potencia suficiente.");
                break;
            case 1:
                resultadoTexto = "¡Zombie eliminado!";
                ventana.actualizarEtiqueta("¡Zombie eliminado!");
                eliminarZombieDeTablero(x, y, zombie);
                break;
            case 2:
                resultadoTexto = "¡Zombie tóxico eliminado!";
                ventana.actualizarEtiqueta("¡Zombie tóxico eliminado!");
                eliminarZombieDeTablero(x, y, zombie);
                if (superviviente.getX() == x && superviviente.getY() == y) {
                    resultadoTexto += " El superviviente ha recibido daño por sangre tóxica.";
                    ventana.actualizarEtiqueta("¡Zombie tóxico eliminado!, El superviviente ha recibido daño por sangre tóxica.");
                    superviviente.envenenar();
                    superviviente.aplicarEfectos();
                }
                break;
        }
        ventana.accionesTotales++;
        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
        if(ventana.accionesTotales == 12) ventana.actualizarTurno();
        if (supervivienteActual.getAccionesDisponibles() == 0) {
            ventana.indiceActual--;
            if (ventana.indiceActual >= ventana.supervivientes.size()) ventana.indiceActual = 3;
            Superviviente siguienteSuperviviente = ventana.supervivientes.get(ventana.indiceActual);
            siguienteSuperviviente.resetearAcciones();
            System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
            ventana.etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
            ventana.panelJuego.revalidate();
            ventana.panelJuego.repaint();
        }
        ventana.actualizarIconos();
        ventana.registroAtaques.add(new Almacen_Ataques(superviviente.getNombre(), zombie.getIdentificador(), new ArrayList<>(valoresDados), resultadoTexto));
        System.out.println(resultadoTexto);
    }

    private void eliminarZombieDeTablero(int x, int y, Zombi zombie) {
        ventana.tablero.botonesTablero[x][y].setIcon(null);
        ventana.tablero.tablero[x][y].setHayZombie(false);
        ventana.tablero.tablero[x][y].eliminarZombie(zombie);
        ventana.zombies.remove(zombie);
    }
}
