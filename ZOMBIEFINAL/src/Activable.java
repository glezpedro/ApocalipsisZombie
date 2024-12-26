
import java.util.List;


public interface Activable {
    void moverse(int x, int y);
    void atacar(List<Superviviente> supervivientes);
    int[] getCoordenadas();
}
