
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.*;



public class Tablero implements Serializable{
    private Coordenada dimension;
    private Coordenada objetivo;
    public Casilla[][]tablero;
    private final int filasColumnas = 10;
    public int coordenadaXSeleccionada = 0; 
    public int coordenadaYSeleccionada = 0;
    private final VentanaJuego ventana;
    public JButton[][] botonesTablero;


    public Tablero(VentanaJuego ventanaMain) {
        this.ventana= ventanaMain;
    }
    public void colocarTablero(){
        ventana.panelTablero = new JPanel();
        ventana.panelTablero.setLayout(new GridLayout(10,10));
        ventana.panelTablero.setBounds(190,115,210,210);
        ventana.panelJuego.add(ventana.panelTablero);
        

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        
        tablero = new Casilla[filasColumnas][filasColumnas];
        botonesTablero = new JButton[filasColumnas][filasColumnas];
        
        for (int i = 0; i < filasColumnas; i++) {
            for (int j = 0; j < filasColumnas; j++) {
                JButton button = new JButton();
                button.setBorder(border);
                button.setBackground(Color.red);
                button.setOpaque(false); 
                botonesTablero[i][j] = button;
                tablero[i][j] = new Casilla(new Coordenada(i, j));
                final int fila = i;
                final int columna = j;
                
                // Al hacer clic, guardar las coordenadas seleccionadas
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        coordenadaXSeleccionada = fila;
                        coordenadaYSeleccionada = columna;
                        System.out.println("Coordenadas seleccionadas: X=" + fila + ", Y=" + columna);
                        MuestraDatos();
                        ventana.statusCasilla();
                        ventana.actualizarEtiquetaCoordenadas(coordenadaXSeleccionada, coordenadaYSeleccionada);
                    }
                });
                ventana.panelTablero.add(button);
            }     
        }
        ventana.colocarZombiesInicio();
    }
    
    public void colocarTablero2(){
        ventana.panelTablero = new JPanel();
        ventana.panelTablero.setLayout(new GridLayout(10,10));
        ventana.panelTablero.setBounds(190,115,210,210);
        ventana.panelSimular.add(ventana.panelTablero);
        

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        
        tablero = new Casilla[filasColumnas][filasColumnas];
        botonesTablero = new JButton[filasColumnas][filasColumnas];
        
        for (int i = 0; i < filasColumnas; i++) {
            for (int j = 0; j < filasColumnas; j++) {
                JButton button = new JButton();
                button.setBorder(border);
                button.setBackground(Color.red);
                button.setOpaque(false); 
                botonesTablero[i][j] = button;
                tablero[i][j] = new Casilla(new Coordenada(i, j));
                final int fila = i;
                final int columna = j;
                
                // Al hacer clic, guardar las coordenadas seleccionadas
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        coordenadaXSeleccionada = fila;
                        coordenadaYSeleccionada = columna;
                        System.out.println("Coordenadas seleccionadas: X=" + fila + ", Y=" + columna);
                        MuestraDatos();
                        ventana.statusCasillaSim();
                        ventana.actualizarEtiquetaCoordenadas(coordenadaXSeleccionada, coordenadaYSeleccionada);
                    }
                });
                ventana.panelTablero.add(button);
            }     
        }
        //ventana.colocarZombiesInicio();
    }
    /*
    public void colocarTablero2(){
        ventana.panelTablero2 = new JPanel();
        ventana.panelTablero2.setLayout(new GridLayout(10,10));
        ventana.panelTablero2.setBounds(190,115,210,210);
        ventana.panelSimular.add(ventana.panelTablero2);

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        
        tablero = new Casilla[filasColumnas][filasColumnas];
        botonesTablero = new JButton[filasColumnas][filasColumnas];
        
        for (int i = 0; i < filasColumnas; i++) {
            for (int j = 0; j < filasColumnas; j++) {
                JButton button = new JButton();
                button.setBorder(border);
                button.setBackground(Color.red);
                button.setOpaque(false); 
                botonesTablero[i][j] = button;
                tablero[i][j] = new Casilla(new Coordenada(i, j));
                final int fila = i;
                final int columna = j;
                
                // Al hacer clic, guardar las coordenadas seleccionadas
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        coordenadaXSeleccionada = fila;
                        coordenadaYSeleccionada = columna;
                        System.out.println("Coordenadas seleccionadas: X=" + fila + ", Y=" + columna);
                        MuestraDatos();
                        ventana.statusCasilla();
                        ventana.actualizarEtiquetaCoordenadas(coordenadaXSeleccionada, coordenadaYSeleccionada);
                    }
                });
                ventana.panelTablero2.add(button);
            }     
        }
    }
    */
    public int getCoordenadaXSeleccionada(){
        return coordenadaXSeleccionada;
    }
    
    public int getCoordenadaYSeleccionada(){
        return coordenadaYSeleccionada;
    }
    
    public boolean esPosicionValida(int x, int y){
        return(x >= 0 && y >= 0 && x <=9 && y <= 9);
    }
    
    public void MuestraDatos() {
    int x = getCoordenadaXSeleccionada();
    int y = getCoordenadaYSeleccionada();

        if (tablero[x][y].tieneZombie()) {
            System.out.println("Hay un Zombie en la casilla seleccionada");
        } else {
            System.out.println("No hay un Zombie en la casilla seleccionada");
        }
    }  
}
