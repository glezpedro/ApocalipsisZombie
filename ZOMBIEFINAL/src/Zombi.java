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

    public Zombi(TipoZombie tipo, int x, int y) {
        this.identificador = contador++;
        this.tipo = tipo;
        this.vivo = true;
        this.coordenadaX = x;
        this.coordenadaY = y;
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
        TipoZombie tipoZombi = seleccionarTipoAleatorio();
        return new Zombi(tipoZombi, random.nextInt(10), random.nextInt(10));
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

    @Override
    public void moverse() {
        /*if (supervivientes.isEmpty()) return;

        Superviviente objetivo = supervivientes.get(random.nextInt(supervivientes.size()));
        if (this.coordenadaX < objetivo.getX()) coordenadaX++;
        else if (this.coordenadaX > objetivo.getX()) coordenadaX--;

        if (this.coordenadaY < objetivo.getY()) coordenadaY++;
        else if (this.coordenadaY > objetivo.getY()) coordenadaY--;

        System.out.println("El zombi " + identificador + " se mueve a (" + coordenadaX + ", " + coordenadaY + ").");*/
    }

    @Override
    public void atacar(List<Superviviente> supervivientes) {
        for (Superviviente s : supervivientes) {
            if (this.coordenadaX == s.getX() && this.coordenadaY == s.getY()) {
                System.out.println("¡El zombi " + identificador + " ha mordido al superviviente!");
                s.recibirHerida(this.tipo == TipoZombie.ABOMINACION ? 3 : 1);
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
            return 1;
        }
        return 0;
    }



    
    public int getX(){
        return coordenadaX;
    }
    public int getY(){
        return coordenadaY;
    }
    public int getAguante(){
        return aguante;
    }
    
}


/*
    //devuelve 0-sigue viuvo, 1-esta muerto, 2-es toxico
    public int reaccionAtaques(Armas arma, int distancia) {
      
        if (distancia > arma.getAlcance()) {
            return 0; // Fuera de alcance
        }

        
        if (esBerserker() && distancia > 0) {
            return 0; // Inmune a distancia
        }

        
        int exitos = arma.lanzarDados();
        int potenciaTotal = exitos * arma.getPotencia();

        // Aplicar daño y verificar si el zombie es eliminado
        if (recibirAtaque(potenciaTotal)) {
            if (esToxico() && distancia == 0) {
                return 2; 
            } else {
                return 1; 
            }
        }


        return 0; // No eliminado
    }
    
    private boolean recibirAtaque(int potencia) {
        if (potencia >= aguante) {
            this.vivo = false;
            return true;
        }
        return false;
    }

    public boolean estaVivo() {
        return vivo;
    }

    private boolean esBerserker() {
        return tipo == TipoZombie.CAMINANTE_BERSERKER || tipo == TipoZombie.CORREDOR_BERSERKER || tipo == TipoZombie.ABOMINACION_BERSERKER;
    }

    private boolean esToxico() {
        return tipo == TipoZombie.CAMINANTE_TOXICO || tipo == TipoZombie.CORREDOR_TOXICO || tipo == TipoZombie.ABOMINACION_TOXICO;
    }

    
    public boolean puedeSerEliminado(int potenciaArma){
        return potenciaArma >= aguante;
    }
    
*/