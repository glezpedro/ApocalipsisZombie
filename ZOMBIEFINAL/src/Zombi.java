import java.io.Serializable;
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

    public Zombi(TipoZombie tipo, int x , int y) {
        this.identificador = contador++;
        this.tipo = tipo;
        this.vivo = true;
        this.coordenadaX = x;
        this.coordenadaY = y;
        asignarAtributosSegunTipo(tipo);
    }

    //zombi.getTipo()     caminante, corredor o abominacion
    //zombie.getClass().getSimpleName();     normal, toxico o berseker
    
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
        int categoriaSeleccionada = random.nextInt(3); // 0 = Normal, 1 = Berseker, 2 = Toxico
        TipoZombie tipoZombi = seleccionarTipoAleatorio();

        switch (categoriaSeleccionada) {
            case 0:
                return new Normal(tipoZombi, random.nextInt(10), random.nextInt(10));
            case 1:
                return new Berseker(tipoZombi, random.nextInt(10), random.nextInt(10));
            case 2:
                return new Toxico(tipoZombi, random.nextInt(10), random.nextInt(10));
            default:
                throw new IllegalStateException("Categoría inválida");
        }
    }

    private static TipoZombie seleccionarTipoAleatorio() {
        int probabilidad = random.nextInt(100); 

        if (probabilidad < 60) {
            return TipoZombie.CAMINANTE; // 60%
        } else if (probabilidad < 90) {
            return TipoZombie.CORREDOR; // 30%
        } else {
            return TipoZombie.ABOMINACION; // 10%
        }
    }
   

    @Override
    public void moverse() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int atacar(Arma arma, int distancia) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int[] getCoordenadas() {
        return new int[]{coordenadaX, coordenadaY};
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