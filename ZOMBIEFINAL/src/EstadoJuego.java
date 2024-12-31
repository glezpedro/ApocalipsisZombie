


import java.io.Serializable;
import java.util.List;
import java.util.Set;



public class EstadoJuego implements Serializable {
    private int turno;
    private Set<Zombi> zombies; 
    private int coordenadaX;
    private int coordenadaY;
    private List<Superviviente> supervivientes;
    private int indiceActual;
    
    public EstadoJuego(List<Superviviente> supervivientes, int turno, Set<Zombi> zombies, int x, int y, int indiceActual) {
        this.supervivientes = supervivientes;
        this.turno = turno;
        this.zombies = zombies;
        this.coordenadaX = x;
        this.coordenadaY = y;
    }

    public int getTurno() { return turno; }
    public Set<Zombi> getZombies() { return zombies; }
    public int getCoordenadaX() { return coordenadaX; }
    public int getCoordenadaY() { return coordenadaY; }
}

