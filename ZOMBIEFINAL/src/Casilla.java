
public class Casilla {
    private Coordenada posicion;
    private boolean explorada;
    private boolean hayZombie;
    private boolean haySuperviviente;


    public Casilla(Coordenada posicion) {
        this.posicion = posicion;
    }

    public void setHayZombie(boolean nuevoZombie) {
        this.hayZombie = nuevoZombie;
    }
    
    public boolean tieneZombie() {
        return hayZombie;
    }
}
