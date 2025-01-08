import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ataque implements Serializable {
    
    private VentanaJuego ventana;

    public Ataque(VentanaJuego ventana) {
        if (ventana == null) {
            throw new IllegalArgumentException("La ventana no puede ser null");
        }
        this.ventana = ventana;
    }
    
    public void atacarZombieF(Arma armaSeleccionada, Superviviente supervivienteActual, int coordenadaXSeleccionada, int coordenadaYSeleccionada, Zombi zombieAtacado){
        
        List<Integer> valoresDados = new ArrayList<>();

        int exitos = armaSeleccionada.lanzarDados(valoresDados);
        System.out.println("Éxitos iniciales: " + exitos);
        
        
        
        while (exitos > 0) {
            if (coordenadaXSeleccionada != -1 && coordenadaYSeleccionada != -1) {
                int distancia = ventana.calcularDistancia(supervivienteActual.getX(), supervivienteActual.getY(), zombieAtacado.getX(), zombieAtacado.getY());

                if (zombieAtacado != null) {
                    int resultado = zombieAtacado.reaccionAtaques(armaSeleccionada, distancia);
                    String resultadoTexto = "";

                    switch (resultado) {
                        case 0:
                            resultadoTexto = "El zombie sigue vivo, el arma no tiene alcance o potencia suficiente.";
                            ventana.actualizarEtiqueta("El zombie sigue vivo, el arma no tiene alcance o potencia suficiente.");
                            exitos--;
                            break;
                        case 1:
                            resultadoTexto = "¡Zombie eliminado!";
                            ventana.actualizarEtiqueta("¡Zombie eliminado!");
                            eliminarZombieDeTablero(coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado);
                            exitos--;
                            ventana.registroAtaques.add(new Almacen_Ataques(
                                supervivienteActual.getNombre(),
                                zombieAtacado.getIdentificador(),
                                new ArrayList<>(valoresDados), 
                                resultadoTexto
                            ));
                            break;
                        case 2:
                            resultadoTexto = "¡Zombie tóxico eliminado!";
                            ventana.actualizarEtiqueta("¡Zombie tóxico eliminado!");
                            eliminarZombieDeTablero(coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado);
                            if (supervivienteActual.getX() == coordenadaXSeleccionada && supervivienteActual.getY() == coordenadaYSeleccionada) {
                                resultadoTexto += " El superviviente ha recibido daño por sangre tóxica.";
                                supervivienteActual.envenenar();
                                supervivienteActual.aplicarEfectos();
                            }
                            ventana.registroAtaques.add(new Almacen_Ataques(
                                supervivienteActual.getNombre(),
                                zombieAtacado.getIdentificador(),
                                new ArrayList<>(valoresDados), 
                                resultadoTexto
                            ));
                            exitos--;
                            break;
                    }
                    
                    

                    System.out.println(resultadoTexto);
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
    
    
    
    
    
    private void eliminarZombieDeTablero(int x, int y, Zombi zombie) {
        ventana.tablero.botonesTablero[x][y].setIcon(null);
        ventana.tablero.tablero[x][y].setHayZombie(false);
        ventana.tablero.tablero[x][y].eliminarZombie(zombie);
        ventana.zombies.remove(zombie);
    }
}
