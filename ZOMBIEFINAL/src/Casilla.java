import java.util.ArrayList;
import java.util.List;


public class Casilla {
    private Coordenada posicion;
    private boolean explorada;
    private boolean hayZombie;
    private boolean haySuperviviente;
    private List<Superviviente> supervivientes = new ArrayList<>();
    private List<Zombi> zombies = new ArrayList<>();

    
    public Casilla(Coordenada posicion) {
        this.posicion = posicion;
    } 
    
    public void setHaySuperviviente(boolean haySuperviviente) {
        this.haySuperviviente = haySuperviviente;
    }

    public void setHayZombie(boolean nuevoZombie) {
        this.hayZombie = nuevoZombie;
    }

    public boolean tieneSuperviviente() {
        return haySuperviviente;
    }
    
    public boolean tieneZombie() {
        return hayZombie;
    }
    
    
    public List<Superviviente> getSupervivientes() {
        return supervivientes;
    }

    public void agregarSuperviviente(Superviviente s) {
        supervivientes.add(s);
    }

    public void eliminarSuperviviente(Superviviente s) {
        supervivientes.remove(s);
    }

    public boolean haySupervivientes() {
        return !supervivientes.isEmpty();
    }
    
    public List<Zombi> getZombies() {
        return zombies;
    }

    public void agregarZombie(Zombi z) {
        zombies.add(z);
    }

    public void eliminarZombie(Zombi z) {
        zombies.remove(z);
    }

    public boolean hayZombies() {
        return !zombies.isEmpty();
    }
    
    
}
