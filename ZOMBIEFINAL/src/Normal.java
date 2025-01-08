
import java.io.Serializable;


public class Normal extends Zombi implements Serializable{
    public Normal(TipoZombie tipo, int x, int y) {
        super(tipo,x ,y, "NORMAL");
    }
}
