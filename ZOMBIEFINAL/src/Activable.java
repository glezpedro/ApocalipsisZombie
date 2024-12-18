
import java.util.List;


public interface Activable {
    void moverse();
    void atacar(List<Superviviente> supervivientes);
    int[] getCoordenadas();
}
