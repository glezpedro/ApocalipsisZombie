import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class EstadoJuego implements Serializable {
    private int turno;
    private Set<Zombi> zombies;
    private List<Superviviente> supervivientes;
    private int indiceActual;
    private List<Almacen_Ataques> registroAtaques; 
    private Tablero tablero;
    private int metaX;
    private int metaY;
    private int accionesTotales;

    public EstadoJuego( List<Superviviente> supervivientes, int turno, Set<Zombi> zombies, int indiceActual, List<Almacen_Ataques> registroAtaques, int metaX, int metaY, int accionesTotales) {
        //this.tablero = tablero;
        this.supervivientes = supervivientes;
        this.turno = turno;
        this.zombies = zombies;
        this.indiceActual = indiceActual;
        this.registroAtaques = registroAtaques;
        this.metaX = metaX;
        this.metaY = metaY;
        this.accionesTotales = accionesTotales;
    }

    public int getTurno() {
        return turno;
    }

    public Set<Zombi> getZombies() {
        return zombies;
    }

    public int getMetaX() {
        return metaX;
    }

    public int getMetaY() {
        return metaY;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getAccionesTotales() {
        return accionesTotales;
    }

    public List<Superviviente> getSupervivientes() {
        return supervivientes;
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public List<Almacen_Ataques> getRegistroAtaques() {
        return registroAtaques;
    }

    public void mostrarRegistroAtaques() {
        System.out.println("Registro de ataques:");
        for (Almacen_Ataques ataque : registroAtaques) {
            System.out.println(ataque);
        }
    }
}
