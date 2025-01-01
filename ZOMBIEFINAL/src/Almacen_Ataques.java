import java.util.List;

public class Almacen_Ataques {
    private String atacante;
    private int objetivo;
    private List<Integer> valoresDados;
    private String resultado;

    public Almacen_Ataques(String atacante, int objetivo, List<Integer> valoresDados, String resultado) {
        this.atacante = atacante;
        this.objetivo = objetivo;
        this.valoresDados = valoresDados;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Atacante: " + atacante +
               ", Objetivo: " + objetivo +
               ", Valores de Dados: " + valoresDados +
               ", Resultado: " + resultado;
    }
}

