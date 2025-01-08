import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Zombi implements Serializable {
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

    public int[] getCoordenadas() {
        return new int[]{coordenadaX, coordenadaY};
    }
    
    
    public void moverse(int objetivoX, int objetivoY) {

        for (int i = 0; i < activaciones; i++) {
            if (this.coordenadaX < objetivoX) {
                this.coordenadaX++;
            } else if (this.coordenadaX > objetivoX) {
                this.coordenadaX--;
            }

            if (this.coordenadaY < objetivoY) {
                this.coordenadaY++;
            } else if (this.coordenadaY > objetivoY) {
                this.coordenadaY--;
            }

            if (this.coordenadaX == objetivoX && this.coordenadaY == objetivoY) {
                break;
            }
        }

        System.out.println("El zombi " + identificador + " ahora esta en (" + coordenadaX + ", " + coordenadaY + ").");
    }

    public int reaccionAtaques(Arma arma, int distancia) {
        if (distancia > arma.getAlcance()) {
            System.out.println("El arma esta fuera del alcance");
            return 0;
        }

        int potenciaTotal = arma.getPotencia();
        if (potenciaTotal >= aguante) {
            vivo = false;
            System.out.println("¡El zombi " + identificador + " ha sido eliminado con "+arma.getNombre()+"!");
            if(getCategoria().equals("TOXICO")) return 2;
            else return 1;
        }
        return 0;
    }
    
    
    public int getActivaciones(){
        return activaciones;
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

    

    
    
}
