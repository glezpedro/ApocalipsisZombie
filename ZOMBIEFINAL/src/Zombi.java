import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Zombi implements Serializable, Activable {
    private static final Random random = new Random();
    private static int contador = 1;

    private int identificador;
    private int aguante;
    private int activaciones;
    private TipoZombie tipo;
    private boolean vivo;
    private int coordenadaX;
    private int coordenadaY;
    private String categoria;

    public Zombi(TipoZombie tipo, int x, int y, String categoria) {
        this.identificador = contador++;
        this.tipo = tipo;
        this.vivo = true;
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.categoria = categoria;
        asignarAtributosSegunTipo(tipo);
    }

    private void asignarAtributosSegunTipo(TipoZombie tipo) {
        switch (tipo) {
            case CAMINANTE:
                this.aguante = 1;
                this.activaciones = 1;
                break;
            case CORREDOR:
                this.aguante = 1;
                this.activaciones = 2;
                break;
            case ABOMINACION:
                this.aguante = 3;
                this.activaciones = 1;
                break;
        }
    }

    public static Zombi crearZombiAleatorio() {
        String categoria = seleccionarCategoriaAleatoria();
        TipoZombie tipoZombi = seleccionarTipoAleatorio();
        return new Zombi(tipoZombi, random.nextInt(10), random.nextInt(10), categoria);
    }

    private static String seleccionarCategoriaAleatoria() {
        int probabilidad = random.nextInt(3); // Tres categorías equiprobables
        switch (probabilidad) {
            case 0: return "NORMAL";
            case 1: return "BERSERKER";
            default: return "TOXICO";
        }
    }

    private static TipoZombie seleccionarTipoAleatorio() {
        int probabilidad = random.nextInt(100);
        if (probabilidad < 60) {
            return TipoZombie.CAMINANTE;
        } else if (probabilidad < 90) {
            return TipoZombie.CORREDOR;
        } else {
            return TipoZombie.ABOMINACION;
        }
    }

    public void moverse(List<Superviviente> supervivientes) {
        if (supervivientes.isEmpty()) {
            System.out.println("No hay supervivientes en el tablero.");
            return;
        }

        // Encontrar al superviviente más cercano
        Superviviente objetivo = null;
        int distanciaMinima = Integer.MAX_VALUE;

        for (Superviviente s : supervivientes) {
            if (s.isIsEliminado()) continue; // Ignorar supervivientes eliminados
            int distancia = Math.abs(this.coordenadaX - s.getX()) + Math.abs(this.coordenadaY - s.getY());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                objetivo = s;
            }
        }

        if (objetivo == null) {
            System.out.println("No hay objetivos válidos para el zombi " + identificador);
            return;
        }

        System.out.println("El zombi " + identificador + " se dirige hacia el superviviente en (" + objetivo.getX() + ", " + objetivo.getY() + ").");

        // Mover el zombi según su número de activaciones
        for (int i = 0; i < activaciones; i++) {
            if (this.coordenadaX < objetivo.getX()) this.coordenadaX++;
            else if (this.coordenadaX > objetivo.getX()) this.coordenadaX--;

            if (this.coordenadaY < objetivo.getY()) this.coordenadaY++;
            else if (this.coordenadaY > objetivo.getY()) this.coordenadaY--;

            // Si ya alcanzó al objetivo, detener movimiento
            if (this.coordenadaX == objetivo.getX() && this.coordenadaY == objetivo.getY()) {
                System.out.println("El zombi " + identificador + " ha alcanzado al superviviente en (" + coordenadaX + ", " + coordenadaY + ").");
                break;
            }
        }

        System.out.println("El zombi " + identificador + " ahora está en (" + coordenadaX + ", " + coordenadaY + ").");
    }


    @Override
    public void atacar(List<Superviviente> supervivientes) {
        for (Superviviente s : supervivientes) {
            if (this.coordenadaX == s.getX() && this.coordenadaY == s.getY()) {
                int danio = (this.tipo == TipoZombie.ABOMINACION) ? 3 : 1;
                System.out.println("¡El zombi " + identificador + " ha mordido al superviviente causando " + danio + " puntos de daño!");
                s.aplicarEfectos();
                return;
            }
        }
        System.out.println("El zombi " + identificador + " no encuentra a ningún superviviente.");
    }

    @Override
    public int[] getCoordenadas() {
        return new int[]{coordenadaX, coordenadaY};
    }

    public int reaccionAtaques(Arma arma, int distancia) {
        if (distancia > arma.getAlcance()) return 0;

        int potenciaTotal = arma.getPotencia();
        if (potenciaTotal >= aguante) {
            vivo = false;
            System.out.println("¡El zombi " + identificador + " ha sido eliminado!");
            if(getCategoria().equals("TOXICO")) return 2;
            else return 1;
        }
        return 0;
    }


    public int getX() {
        return coordenadaX;
    }

    public int getY() {
        return coordenadaY;
    }

    public int getIdentificador() {
        return identificador;
    }

    public int getAguante() {
        return aguante;
    }

    public TipoZombie getTipo() {
        return tipo;
    }
    
    public String getCategoria() {
        return categoria;
    }

    
    public boolean estaVivo() {
        return vivo;
    }

    @Override
    public void moverse(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    
}
