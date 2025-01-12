import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ataque implements Serializable {
    public static Map<String, List<String>> zombisEliminados = new HashMap<>();
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
        
        if (exitos == 0) ventana.actualizarEtiqueta("Ataque sin exito.");
        
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
                            agregarZombiEliminado(zombieAtacado, supervivienteActual.getNombre());
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
                            agregarZombiEliminado(zombieAtacado, supervivienteActual.getNombre());
                            eliminarZombieDeTablero(coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado);
                            if (supervivienteActual.getX() == coordenadaXSeleccionada && supervivienteActual.getY() == coordenadaYSeleccionada) {
                                resultadoTexto += " El superviviente ha recibido daño por sangre tóxica.";
                                ventana.actualizarEtiqueta("¡Zombie tóxico eliminado! El superviviente ha recibido daño por sangre tóxica.");
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
    
    public void atacarZombieFSim(Arma armaSeleccionada, Superviviente supervivienteActual, int coordenadaXSeleccionada, int coordenadaYSeleccionada, Zombi zombieAtacado){
        
        List<Integer> valoresDados = new ArrayList<>();

        int exitos = armaSeleccionada.lanzarDados(valoresDados);
        System.out.println("Éxitos iniciales: " + exitos);
        
        if (exitos == 0) ventana.actualizarEtiquetaSim("Ataque sin exito.");
        
        while (exitos > 0) {
            if (coordenadaXSeleccionada != -1 && coordenadaYSeleccionada != -1) {
                int distancia = ventana.calcularDistancia(supervivienteActual.getX(), supervivienteActual.getY(), zombieAtacado.getX(), zombieAtacado.getY());

                if (zombieAtacado != null) {
                    int resultado = zombieAtacado.reaccionAtaques(armaSeleccionada, distancia);
                    String resultadoTexto = "";

                    switch (resultado) {
                        case 0:
                            resultadoTexto = "El zombie sigue vivo, el arma no tiene alcance o potencia suficiente.";
                            ventana.actualizarEtiquetaSim("El zombie sigue vivo, el arma no tiene alcance o potencia suficiente.");
                            exitos--;
                            break;
                        case 1:
                            resultadoTexto = "¡Zombie eliminado!";
                            ventana.actualizarEtiquetaSim("¡Zombie eliminado!");
                            agregarZombiEliminado(zombieAtacado, supervivienteActual.getNombre());
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
                            ventana.actualizarEtiquetaSim("¡Zombie tóxico eliminado!");
                            agregarZombiEliminado(zombieAtacado, supervivienteActual.getNombre());
                            eliminarZombieDeTablero(coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado);
                            if (supervivienteActual.getX() == coordenadaXSeleccionada && supervivienteActual.getY() == coordenadaYSeleccionada) {
                                resultadoTexto += " El superviviente ha recibido daño por sangre tóxica.";
                                ventana.actualizarEtiquetaSim("¡Zombie tóxico eliminado! El superviviente ha recibido daño por sangre tóxica.");
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
                    ventana.actualizarEtiquetaSim("No hay un zombie en la casilla seleccionada.");
                    break;
                }
            } else {
                System.out.println("Por favor, selecciona una casilla válida antes de atacar.");
                ventana.actualizarEtiquetaSim("Por favor, selecciona una casilla válida antes de atacar.");
                break;
            }
        }
        

        ventana.actualizarIconosSim();
        ventana.panelSimular.revalidate();
        ventana.panelSimular.repaint();

        System.out.println("El ataque ha finalizado. Éxitos restantes: " + exitos);
    }
    
    
    public static void agregarZombiEliminado(Zombi zombieAtacado, String nombreSuperviviente) {
        zombisEliminados.putIfAbsent(nombreSuperviviente, new ArrayList<>());
        String zombiInfo = "Zombi: " + zombieAtacado.getIdentificador() +", Categoria: " + zombieAtacado.getCategoria();

        if (!zombisEliminados.get(nombreSuperviviente).contains(zombiInfo)) {
            zombisEliminados.get(nombreSuperviviente).add(zombiInfo);
        }
        zombisEliminados.get(nombreSuperviviente).sort(Comparator.comparing(z -> z.split(": ")[1]));
    }

    
    public static String obtenerZombisEliminadosPorSuperviviente(String nombreSuperviviente) {
        if (zombisEliminados.containsKey(nombreSuperviviente)) {
            return "Zombis eliminados por " + nombreSuperviviente + ": " + String.join(", ", zombisEliminados.get(nombreSuperviviente));
        } else {
            return nombreSuperviviente + " no ha eliminado ningún zombi aún.";
        }
    }

    private void eliminarZombieDeTablero(int x, int y, Zombi zombie) {
        ventana.tablero.botonesTablero[x][y].setIcon(null);
        ventana.tablero.tablero[x][y].setHayZombie(false);
        ventana.tablero.tablero[x][y].eliminarZombie(zombie);
        ventana.zombies.remove(zombie);
    }
}
