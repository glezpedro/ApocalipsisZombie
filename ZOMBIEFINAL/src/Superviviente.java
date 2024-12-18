import java.util.ArrayList;
import java.util.List;



public class Superviviente {
    private String nombre;
    private int salud;
    private Equipo inventario; // Inventario del tipo Equipo
    private int maxHeridas; // Máximo de heridas antes de quedar fuera

    public Superviviente(String nombre, int salud, int maxHeridas) {
        this.nombre = nombre;
        this.salud = salud;
        this.maxHeridas = maxHeridas;
        this.inventario = new Equipo(); // Inicializa un inventario vacío
    }
    public Equipo getInventario() {
        return inventario;
    }
    
    public void recibirHerida() {
        salud--;
        if (salud <= 0) {
            System.out.println(nombre + " ha sido eliminado.");
            //borrarlo ACABAR
        } else {
            System.out.println(nombre + " ha recibido una herida. Salud restante: " + salud);
        }
    }
}
