
import java.io.Serializable;






public class Berseker extends Zombi implements Serializable{
    public Berseker(TipoZombie tipo, int x, int y) {
        super(tipo, x , y, "BERSEKER");
    }
}
