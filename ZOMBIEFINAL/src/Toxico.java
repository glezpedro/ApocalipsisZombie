
import java.io.Serializable;



public class Toxico extends Zombi implements Serializable{
    public Toxico(TipoZombie tipo, int x, int y) {
        super(tipo, x , y, "TOXICO");
    }
}
