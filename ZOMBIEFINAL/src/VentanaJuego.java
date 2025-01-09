import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.time.*;
import java.time.format.DateTimeFormatter;



public class VentanaJuego extends JFrame implements Serializable{
    public JPanel panel, panelJuego, panelSimular, panelTablero, panelTablero2;
    private JButton NuevaPartida, RetomarPartida, Salir, Atras, SalirGuardar,Simulacion, Simular, Ataques;
    private int contadorTurnos;
    private final int numZombies = 3;
    private JButton Moverse, Atacar, SiguienteTurno, Seleccionar, Buscar, zombisEliminadosOrde;
    private JLabel etiqueta1, etiqueta2, etiqueta3;
    public JLabel etiquetaTurnos;
    private JComboBox<String> listaArmas,listaArmas2, listaActivas, añadirZombi, listaSuper;  
    private JTextArea etiquetaStatus;
    private JScrollPane scrollPanel;
    public Set<Zombi> zombies;
    Set<Point> posicionesUsadas = new HashSet<>();
    Tablero tablero;
    public List<Superviviente> supervivientes;
    private int metaX;
    private int metaY;
    public int indiceActual = 3;
    public int accionesTotales = 0;
    private final VentanaJuego ventana;
    private Arma armaSeleccionada; // Agregamos esta variable para el arma seleccionada
    public List<Almacen_Ataques> registroAtaques;

    public VentanaJuego(){
        this.registroAtaques = new ArrayList<>();
        this.supervivientes = new ArrayList<>();
        setSize(450,450);
        setLocationRelativeTo(null); //Pone la ventana en el centro
        setResizable(false); //Si podemos o no hacerla mas grade o pequeña
        setMinimumSize(new Dimension(450,450)); //el tamaño minimo de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Zombie Game");
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/zombie2.png"));
        setIconImage(icon.getImage());
        zombies = new HashSet<>();
        tablero = new Tablero(this);
        this.ventana = this;
        colocarPanelMain();
    }
    
    private void colocarPanelMain() {
        this.getContentPane().removeAll();

        panel = new BackgroundPanel("/resources/woods.png"); 
        panel.setLayout(null);
        this.getContentPane().add(panel);
        
        this.revalidate();
        this.repaint();
     
        colocarEtiquetasMain();
        colocarBotonesMain();
    }
    
    private void colocarPanelJuego() {
        panelJuego = new BackgroundPanel("/resources/BackJuego.png");
        panelJuego.setLayout(null);
        panelJuego.setBackground(Color.cyan);
        panelJuego.setBounds(0, 0, 450, 450); 
        // Borra el panelMain y pone este
        this.getContentPane().remove(panel); 
        this.getContentPane().add(panelJuego);

        //Añade instrucciones
        actualizarEtiqueta("Para moverse o atacar, primero selecciona la casilla deseada y luego presiona el botón correspondiente (Moverse/Atacar).");
        // Recarga el panel para asegurarse que este se muestra
        this.revalidate();
        this.repaint();
        
        colocarEtiquetasJuego();
        colocarBotonesJuego();
        colocarRadioBotones();
        tablero.colocarTablero();
        colocarSupervivientes();
    }

    private void colocarPanelSimular() {

        panelSimular = new BackgroundPanel("/resources/BackJuego.png");
        panelSimular.setLayout(null);
        panelSimular.setBackground(Color.green);
        panelSimular.setBounds(0, 0, 450, 450);
        
        this.getContentPane().remove(panel); 
        this.getContentPane().add(panelSimular);
        
        actualizarEtiqueta("Para moverse o atacar, primero selecciona la casilla deseada y luego presiona el botón correspondiente (Moverse/Atacar).");

        this.revalidate();
        this.repaint();  
        
        colocarEtiquetasSimular();
        colocarBotonesSimular();
        tablero.colocarTablero2();
        colocarRadioBotonesSim();
        colocarMetaSim();
    }

    private void colocarEtiquetasMain (){
        // Etiqueta 1
        JLabel etiqueta = new JLabel("Zombie Game", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta.setBounds(25, 30, 400, 65);
        etiqueta.setOpaque(true); // Asi podemos poner background
        etiqueta.setForeground(Color.white);
        etiqueta.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panel.add(etiqueta); //agregamos la etiqueta al panel
        etiqueta.setOpaque(false);
    }
        
    private void colocarBotonesMain(){
       // Nueva Partida
       NuevaPartida = new JButton();
       NuevaPartida.setBounds(125, 150, 200, 40);
       NuevaPartida.setEnabled(true); // lo puedes apagar o encender 
       NuevaPartida.setBackground(Color.cyan);
       ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/NuevaPartida.png"));
       NuevaPartida.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       NuevaPartida.setOpaque(false);
       NuevaPartida.setBorderPainted(false); // quita la linea blanca en el borde
       panel.add(NuevaPartida);
       ActionListener accionBoton1 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               colocarPanelJuego();
           }
       };
       NuevaPartida.addActionListener(accionBoton1);
       
       // Retomar Partida
       RetomarPartida = new JButton();
       RetomarPartida.setBounds(125, 200, 200, 40);
       RetomarPartida.setEnabled(true); 
       RetomarPartida.setBackground(Color.green);
       ImageIcon imagen2 = new ImageIcon(getClass().getResource("/resources/RetomarPartida.png"));
       RetomarPartida.setIcon(new ImageIcon(imagen2.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       RetomarPartida.setOpaque(false);
       RetomarPartida.setBorderPainted(false);
       panel.add(RetomarPartida);
       ActionListener accionBoton2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Seleccionar partida guardada");
                    int seleccion = fileChooser.showOpenDialog(null);

                    if (seleccion == JFileChooser.APPROVE_OPTION) {
                        File archivoSeleccionado = fileChooser.getSelectedFile();

                        FileInputStream fileIn = new FileInputStream(archivoSeleccionado);
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                        EstadoJuego estado = (EstadoJuego) in.readObject();
                        in.close();
                        fileIn.close();

                        tablero = estado.getTablero();
                        contadorTurnos = estado.getTurno();
                        zombies = estado.getZombies();
                        metaX = estado.getMetaX();
                        metaY = estado.getMetaY();
                        registroAtaques = estado.getRegistroAtaques();
                        accionesTotales = estado.getAccionesTotales();
                        supervivientes = estado.getSupervivientes();
                        indiceActual = estado.getIndiceActual();

                        colocarPanelJuego();
                        actualizarIconos();

                        System.out.println("Partida cargada correctamente desde: " + archivoSeleccionado.getName());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error al cargar la partida.");
                }
            }
        };
        RetomarPartida.addActionListener(accionBoton2);
        
       // Salir
       Salir = new JButton();
       Salir.setBounds(125, 250, 200, 40);
       Salir.setEnabled(true);
       Salir.setBackground(Color.red);
       ImageIcon imagen3 = new ImageIcon(getClass().getResource("/resources/Salir.png"));
       Salir.setIcon(new ImageIcon(imagen3.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       Salir.setOpaque(false);
       Salir.setBorderPainted(false);
       panel.add(Salir);
       ActionListener accionBoton3 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               System.exit(0);
           }
       };
       Salir.addActionListener(accionBoton3);
       
       // Simular
       Simular = new JButton();
       Simular.setBounds(125, 300, 200, 40);
       Simular.setEnabled(true);
       Simular.setBackground(Color.red);
       ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Simular.png"));
       Simular.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       Simular.setOpaque(false);
       Simular.setBorderPainted(false);
       panel.add(Simular);
       ActionListener accionBoton4 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               colocarPanelSimular();
           }
       };
       Simular.addActionListener(accionBoton4);
    }
    
    private void colocarEtiquetasJuego(){
        JLabel etiqueta = new JLabel("Zombie Game", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta.setBounds(25, 30, 400, 65);
        etiqueta.setOpaque(true); // Asi podemos poner background
        etiqueta.setForeground(Color.white);
        etiqueta.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiqueta);
        etiqueta.setOpaque(false);
        // Etiqueta Turnos
        contadorTurnos = 0;
        etiquetaTurnos = new JLabel("Turno Rojo", SwingConstants.CENTER); // Creamos etiqueta
        etiquetaTurnos.setBounds(30, 105, 100, 35);
        etiquetaTurnos.setOpaque(true); // Asi podemos poner background
        etiquetaTurnos.setForeground(Color.black);
        etiquetaTurnos.setFont(new Font("chiller", Font.BOLD, 25)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiquetaTurnos);
        etiquetaTurnos.setOpaque(false);
    }
    
    private String SuperSeleccionado = "";
    
    private void colocarEtiquetasSimular(){
        JLabel etiqueta = new JLabel("Zombie Game", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta.setBounds(25, 30, 400, 65);
        etiqueta.setOpaque(true); // Asi podemos poner background
        etiqueta.setForeground(Color.white);
        etiqueta.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta);
        etiqueta.setOpaque(false);
        // Etiqueta Turnos
        contadorTurnos = 0;
        etiquetaTurnos = new JLabel("Turno Rojo", SwingConstants.CENTER); // Creamos etiqueta
        etiquetaTurnos.setBounds(30, 105, 100, 35);
        etiquetaTurnos.setOpaque(true); // Asi podemos poner background
        etiquetaTurnos.setForeground(Color.black);
        etiquetaTurnos.setFont(new Font("chiller", Font.BOLD, 25)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiquetaTurnos);
        etiquetaTurnos.setOpaque(false);
    }
    
    
    private void colocarBotonesJuego(){
       // Atras
       Atras = new JButton();
       Atras.setBounds(20, 360, 40, 40);
       Atras.setEnabled(true);
       Atras.setBackground(Color.red);
       ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
       Atras.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       Atras.setOpaque(false);
       Atras.setBorderPainted(false);
       panelJuego.add(Atras);

       ActionListener accionBoton4 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               colocarPanelMain();
           }
       };
       Atras.addActionListener(accionBoton4);
       // Salir Y Guardar
       SalirGuardar = new JButton();
       SalirGuardar.setBounds(70, 360, 200, 40);
       SalirGuardar.setEnabled(true);
       SalirGuardar.setBackground(Color.blue);
       ImageIcon imagen5 = new ImageIcon(getClass().getResource("/resources/SalirGuardar.png"));
       SalirGuardar.setIcon(new ImageIcon(imagen5.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       SalirGuardar.setOpaque(false);
       panelJuego.add(SalirGuardar);
       SalirGuardar.setBorderPainted(false);

       ActionListener accionBoton5 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    EstadoJuego estado = new EstadoJuego(
                        tablero,
                        supervivientes,
                        contadorTurnos,
                        zombies,
                        indiceActual,
                        registroAtaques,
                        metaX,
                        metaY,
                        accionesTotales
                    );

                    LocalDateTime ahora = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                    String nombreArchivo = "partida_" + ahora.format(formatter) + ".dat";

                    FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(estado);
                    out.close();
                    fileOut.close();

                    System.out.println("Partida guardada correctamente en: " + nombreArchivo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error al guardar la partida.");
                }
                System.exit(0); // Salir del juego
            }
        };
       SalirGuardar.addActionListener(accionBoton5);
       // Ataques
       Ataques = new JButton();
       Ataques.setBounds(280, 360, 93, 40);
       Ataques.setEnabled(true);
       Ataques.setBackground(Color.red);
       ImageIcon imagen6 = new ImageIcon(getClass().getResource("/resources/Ataques.png"));
       Ataques.setIcon(new ImageIcon(imagen6.getImage().getScaledInstance(93, 40, Image.SCALE_AREA_AVERAGING)));
       Ataques.setOpaque(false);
       Ataques.setBorderPainted(false);
       panelJuego.add(Ataques);
       
       ActionListener accionBoton6 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               
                JFrame nuevaVentana = new JFrame("Lista de Ataques");
                nuevaVentana.setSize(300, 120);
                nuevaVentana.setLocationRelativeTo(null); //Pone la ventana en el centro
                nuevaVentana.setResizable(false);
                nuevaVentana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                nuevaVentana.setLayout(new FlowLayout()); 

                JButton btnSupervivientes = new JButton("Ataques Supervivientes");
                JButton btnZombis = new JButton("Ataques Zombis");
                JButton btnZombisElim = new JButton("Zombis Eliminados");
                
                btnSupervivientes.addActionListener(ev -> {
                    if (registroAtaques.isEmpty()) actualizarEtiqueta("No hay ataques");
                    else actualizarEtiqueta(registroAtaques.toString()); 
                });

                btnZombis.addActionListener(ev -> {
                    if (Zombi.superAtacados.isEmpty()) actualizarEtiqueta("No hay Supervivientes atacados.");
                    else actualizarEtiqueta(Zombi.superAtacados.toString());
                });
                
                btnZombisElim.addActionListener(ev -> {
                    if (Ataque.zombisEliminados.isEmpty()) actualizarEtiqueta("No hay zombis eliminados.");
                    else {
                        limpiarPanel();
                        etiqueta1 = new JLabel("Elegir Superviviente:", SwingConstants.CENTER); // Creamos etiqueta
                        etiqueta1.setBounds(30, 245, 120, 30);
                        etiqueta1.setOpaque(true); // Asi podemos poner background
                        etiqueta1.setForeground(Color.black);
                        etiqueta1.setFont(new Font("arial", Font.BOLD, 10)); // estableze el font se puede usar 0123 para typo de letra
                        panelJuego.add(etiqueta1);
                        etiqueta1.setOpaque(false);
                        
                        JButton zombisEliminadosOrde = new JButton("Por orden");
                        panelJuego.add(zombisEliminadosOrde);
                        zombisEliminadosOrde.setBounds(30, 300, 100, 20);
                        ActionListener accionListaOrd = (ActionEvent a) -> {
                            panelJuego.remove(zombisEliminadosOrde);
                            panelJuego.revalidate();
                            panelJuego.repaint();
                            actualizarEtiqueta(Ataque.zombisEliminados.toString());
                            
                        };
                        zombisEliminadosOrde.addActionListener(accionListaOrd);
                        //
                        List<String> opcionesSuperLista = new ArrayList<>();
                        for (Superviviente s : supervivientes) opcionesSuperLista.add(s.getNombre());
                        String[] opcionesSuper = opcionesSuperLista.toArray(new String[0]);
                        
                        listaSuper = new JComboBox(opcionesSuper);
                        listaSuper.setBounds(30, 275, 100, 20);
                        ActionListener accionLista = (ActionEvent a) -> {
                            panelJuego.remove(zombisEliminadosOrde);
                            panelJuego.revalidate();
                            panelJuego.repaint();
                            SuperSeleccionado = (String) listaSuper.getSelectedItem();
                            System.out.println("Superviviente seleccionado: " + SuperSeleccionado);
                            actualizarEtiqueta(Ataque.obtenerZombisEliminadosPorSuperviviente(SuperSeleccionado));
                        };
                        listaSuper.addActionListener(accionLista);
                        panelJuego.add(listaSuper); 
                    }
                });
                
                nuevaVentana.add(btnSupervivientes);
                nuevaVentana.add(btnZombis);
                nuevaVentana.add(btnZombisElim);
                nuevaVentana.setVisible(true);
           }
       };
       Ataques.addActionListener(accionBoton6);
    }
    
    private void colocarBotonesSimular(){
       // Atras
       Atras = new JButton();
       Atras.setBounds(20, 360, 40, 40);
       Atras.setEnabled(true);
       Atras.setBackground(Color.red);
       ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
       Atras.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       Atras.setOpaque(false);
       Atras.setBorderPainted(false);
       panelSimular.add(Atras);

       ActionListener accionBoton4 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               colocarPanelMain();
           }
       };
       Atras.addActionListener(accionBoton4);
       
       Simulacion = new JButton();
       Simulacion.setBounds(70, 360, 200, 40);
       Simulacion.setEnabled(true);
       Simulacion.setBackground(Color.blue);
       ImageIcon imagen5 = new ImageIcon(getClass().getResource("/resources/Simulacion-removebg-preview.png"));
       Simulacion.setIcon(new ImageIcon(imagen5.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       Simulacion.setOpaque(false);
       panelSimular.add(Simulacion);
       Simulacion.setBorderPainted(false);

       ActionListener accionBoton5 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame nuevaVentanaS = new JFrame("Añadir Elementos");
                nuevaVentanaS.setSize(300,120);
                nuevaVentanaS.setLocationRelativeTo(null); //Pone la ventana en el centro
                nuevaVentanaS.setResizable(false);
                nuevaVentanaS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                nuevaVentanaS.setLayout(new FlowLayout()); 

                JButton btnSupervivientes = new JButton("Añadir Supervivientes");
                JButton btnZombis = new JButton("Añadir Zombis");
                
                 btnSupervivientes.addActionListener(ev -> {
                  colocarSupervivientesSim(); 
                });
                
                nuevaVentanaS.add(btnSupervivientes);
                nuevaVentanaS.add(btnZombis);
                nuevaVentanaS.setVisible(true);
            }
        };
          
       Simulacion.addActionListener(accionBoton5);
       
       // Ataques
       Ataques = new JButton();
       Ataques.setBounds(280, 360, 93, 40);
       Ataques.setEnabled(true);
       Ataques.setBackground(Color.red);
       ImageIcon imagen6 = new ImageIcon(getClass().getResource("/resources/Ataques.png"));
       Ataques.setIcon(new ImageIcon(imagen6.getImage().getScaledInstance(93, 40, Image.SCALE_AREA_AVERAGING)));
       Ataques.setOpaque(false);
       Ataques.setBorderPainted(false);
       panelSimular.add(Ataques);
       
       ActionListener accionBoton6 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               
                
               
                JFrame nuevaVentana = new JFrame("Lista de Ataques");
                nuevaVentana.setSize(300, 120);
                nuevaVentana.setLocationRelativeTo(null); //Pone la ventana en el centro
                nuevaVentana.setResizable(false);
                nuevaVentana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                nuevaVentana.setLayout(new FlowLayout()); 

                JButton btnSupervivientes = new JButton("Ataques Supervivientes");
                JButton btnZombis = new JButton("Ataques Zombis");
                JButton btnZombisElim = new JButton("Zombis Eliminados");
                
                btnSupervivientes.addActionListener(ev -> {
                    if (registroAtaques.isEmpty()) actualizarEtiqueta("No hay ataques");
                    else actualizarEtiqueta(registroAtaques.toString()); 
                });

                btnZombis.addActionListener(ev -> {
                    if (Zombi.superAtacados.isEmpty()) actualizarEtiqueta("No hay Supervivientes atacados.");
                    else actualizarEtiqueta(Zombi.superAtacados.toString());
                });
                
                btnZombisElim.addActionListener(ev -> {
                    if (Ataque.zombisEliminados.isEmpty()) actualizarEtiqueta("No hay zombis eliminados.");
                    else {
                        limpiarPanelSim();
                        etiqueta1 = new JLabel("Elegir Superviviente:", SwingConstants.CENTER); // Creamos etiqueta
                        etiqueta1.setBounds(30, 245, 120, 30);
                        etiqueta1.setOpaque(true); // Asi podemos poner background
                        etiqueta1.setForeground(Color.black);
                        etiqueta1.setFont(new Font("arial", Font.BOLD, 10)); // estableze el font se puede usar 0123 para typo de letra
                        panelSimular.add(etiqueta1);
                        etiqueta1.setOpaque(false);
                        
                        JButton zombisEliminadosOrde = new JButton("Por orden");
                        panelSimular.add(zombisEliminadosOrde);
                        zombisEliminadosOrde.setBounds(30, 300, 100, 20);
                        ActionListener accionListaOrd = (ActionEvent a) -> {
                            panelSimular.remove(zombisEliminadosOrde);
                            panelSimular.revalidate();
                            panelSimular.repaint();
                            actualizarEtiqueta(Ataque.zombisEliminados.toString());
                            
                        };
                        zombisEliminadosOrde.addActionListener(accionListaOrd);
                        //
                        List<String> opcionesSuperLista = new ArrayList<>();
                        for (Superviviente s : supervivientes) opcionesSuperLista.add(s.getNombre());
                        String[] opcionesSuper = opcionesSuperLista.toArray(new String[0]);
                        
                        listaSuper = new JComboBox(opcionesSuper);
                        listaSuper.setBounds(30, 275, 100, 20);
                        ActionListener accionLista = (ActionEvent a) -> {
                            panelSimular.remove(zombisEliminadosOrde);
                            panelSimular.revalidate();
                            panelSimular.repaint();
                            SuperSeleccionado = (String) listaSuper.getSelectedItem();
                            System.out.println("Superviviente seleccionado: " + SuperSeleccionado);
                            actualizarEtiqueta(Ataque.obtenerZombisEliminadosPorSuperviviente(SuperSeleccionado));
                        };
                        listaSuper.addActionListener(accionLista);
                        panelSimular.add(listaSuper); 
                    }
                });
                
                nuevaVentana.add(btnSupervivientes);
                nuevaVentana.add(btnZombis);
                nuevaVentana.add(btnZombisElim);
                nuevaVentana.setVisible(true);
           }
       };
       Ataques.addActionListener(accionBoton6);
    }
    
    private void colocarRadioBotones(){
       JRadioButton radioBoton1 = new JRadioButton("Moverse", false); // El booleano es para saber si aparece seleccionado o no
       radioBoton1.setBounds(30, 140, 150, 15);
       radioBoton1.setOpaque(false);
       ActionListener accionMoverse = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanel();
               funcionMoverse();
               panelJuego.revalidate(); //para que se muestre en pantalla
               panelJuego.repaint();
           }
       };
       radioBoton1.addActionListener(accionMoverse);
       panelJuego.add(radioBoton1);
        
       JRadioButton radioBoton2 = new JRadioButton("Atacar", false);
       radioBoton2.setBounds(30, 160, 150, 15);
       radioBoton2.setOpaque(false);
       ActionListener accionAtacar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarPanel();
                panelJuego.revalidate();
                panelJuego.repaint();
                funcionAtacar();
            }
        };
        radioBoton2.addActionListener(accionAtacar);
        panelJuego.add(radioBoton2);
        
       JRadioButton radioBoton3 = new JRadioButton("Quedarse", false);
       radioBoton3.setBounds(30, 180, 150, 15);
       radioBoton3.setOpaque(false);
       ActionListener accionQuedarse = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanel();
               panelJuego.revalidate(); 
               panelJuego.repaint();
               funcionQuedarse();
           }
       };
       radioBoton3.addActionListener(accionQuedarse);
       panelJuego.add(radioBoton3);
       
       JRadioButton radioBoton4 = new JRadioButton("Buscar", false);
       radioBoton4.setBounds(30, 200, 150, 15);
       radioBoton4.setOpaque(false);
       ActionListener accionBuscar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanel();
               funcionBuscar();
               panelJuego.revalidate(); 
               panelJuego.repaint();
           }
       };
       radioBoton4.addActionListener(accionBuscar);
       panelJuego.add(radioBoton4);
       
       JRadioButton radioBoton5 = new JRadioButton("Elegir arma", false);
       radioBoton5.setBounds(30, 220, 150, 15);
       radioBoton5.setOpaque(false);
       ActionListener accionElegir = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanel();
               panelJuego.revalidate(); 
               panelJuego.repaint();
               funcionElegirArma();
           }
       };
       radioBoton5.addActionListener(accionElegir);
       panelJuego.add(radioBoton5);
        
       ButtonGroup grupoRadioBotones = new ButtonGroup();
       grupoRadioBotones.add(radioBoton1);
       grupoRadioBotones.add(radioBoton2);
       grupoRadioBotones.add(radioBoton3);
       grupoRadioBotones.add(radioBoton4);
       grupoRadioBotones.add(radioBoton5);

    }
    
    private void colocarRadioBotonesSim(){
       JRadioButton radioBoton1 = new JRadioButton("Moverse", false); // El booleano es para saber si aparece seleccionado o no
       radioBoton1.setBounds(30, 140, 150, 15);
       radioBoton1.setOpaque(false);
       ActionListener accionMoverse = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanelSim();
               funcionMoverseSim();
               panelSimular.revalidate(); //para que se muestre en pantalla
               panelSimular.repaint();
           }
       };
       radioBoton1.addActionListener(accionMoverse);
       panelSimular.add(radioBoton1);
        
       JRadioButton radioBoton2 = new JRadioButton("Atacar", false);
       radioBoton2.setBounds(30, 160, 150, 15);
       radioBoton2.setOpaque(false);
       ActionListener accionAtacar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarPanelSim();
                panelSimular.revalidate();
                panelSimular.repaint();
                funcionAtacarSim();
            }
        };
        radioBoton2.addActionListener(accionAtacar);
        panelSimular.add(radioBoton2);
        
       JRadioButton radioBoton3 = new JRadioButton("Quedarse", false);
       radioBoton3.setBounds(30, 180, 150, 15);
       radioBoton3.setOpaque(false);
       ActionListener accionQuedarse = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanelSim();
               panelSimular.revalidate(); 
               panelSimular.repaint();
               funcionQuedarseSim();
           }
       };
       radioBoton3.addActionListener(accionQuedarse);
       panelSimular.add(radioBoton3);
       
       JRadioButton radioBoton4 = new JRadioButton("Buscar", false);
       radioBoton4.setBounds(30, 200, 150, 15);
       radioBoton4.setOpaque(false);
       ActionListener accionBuscar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanelSim();
               funcionBuscarSim();
               panelSimular.revalidate(); 
               panelSimular.repaint();
           }
       };
       radioBoton4.addActionListener(accionBuscar);
       panelSimular.add(radioBoton4);
       
       JRadioButton radioBoton5 = new JRadioButton("Elegir arma", false);
       radioBoton5.setBounds(30, 220, 150, 15);
       radioBoton5.setOpaque(false);
       ActionListener accionElegir = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanelSim();
               panelSimular.revalidate(); 
               panelSimular.repaint();
               funcionElegirArma();
           }
       };
       radioBoton5.addActionListener(accionElegir);
       panelSimular.add(radioBoton5);
        
       ButtonGroup grupoRadioBotones = new ButtonGroup();
       grupoRadioBotones.add(radioBoton1);
       grupoRadioBotones.add(radioBoton2);
       grupoRadioBotones.add(radioBoton3);
       grupoRadioBotones.add(radioBoton4);
       grupoRadioBotones.add(radioBoton5);

    }
    
       
    
   
    
    public void colocarZombiSimulacion(){
        ImageIcon IconoZombiN = new ImageIcon(getClass().getResource("/resources/zombiN.png"));
        ImageIcon IconoZombiNA = new ImageIcon(getClass().getResource("/resources/zombiNA.png"));
        ImageIcon IconoZombiNC = new ImageIcon(getClass().getResource("/resources/zombiNC.png"));
        ImageIcon IconoZombiT = new ImageIcon(getClass().getResource("/resources/zombiT.png"));
        ImageIcon IconoZombiTA = new ImageIcon(getClass().getResource("/resources/zombiTA.png"));
        ImageIcon IconoZombiTC = new ImageIcon(getClass().getResource("/resources/zombiTC.png"));
        ImageIcon IconoZombiB = new ImageIcon(getClass().getResource("/resources/zombiB.png"));
        ImageIcon IconoZombiBA = new ImageIcon(getClass().getResource("/resources/zombiBA.png"));
        ImageIcon IconoZombiBC = new ImageIcon(getClass().getResource("/resources/zombiBC.png"));
        
        
        Zombi crearZombiNormal1 = Zombi.crearZombiElegido(TipoZombie.CAMINANTE, "NORMAL");
        System.out.println("Zombi Creado"+ crearZombiNormal1);
        Zombi crearZombiNormal2 = Zombi.crearZombiElegido(TipoZombie.CORREDOR, "NORMAL");
        System.out.println("Zombi Creado"+ crearZombiNormal2);
        Zombi crearZombiNormal3 = Zombi.crearZombiElegido(TipoZombie.ABOMINACION, "NORMAL");
        System.out.println("Zombi Creado"+ crearZombiNormal3);
        
        Zombi crearZombiBerseker1 = Zombi.crearZombiElegido(TipoZombie.CAMINANTE, "BERSEKER");
        System.out.println("Zombi Creado"+ crearZombiBerseker1);
        Zombi crearZombiBerseker2 = Zombi.crearZombiElegido(TipoZombie.CORREDOR, "BERSEKER");
        System.out.println("Zombi Creado"+ crearZombiBerseker2);
        Zombi crearZombiBerseker3 = Zombi.crearZombiElegido(TipoZombie.ABOMINACION, "BERSEKER");
        System.out.println("Zombi Creado"+ crearZombiBerseker3);
        
        Zombi crearZombiToxico1 = Zombi.crearZombiElegido(TipoZombie.CAMINANTE, "TOXICO");
        System.out.println("Zombi Creado"+ crearZombiToxico1);
        Zombi crearZombiToxico2 = Zombi.crearZombiElegido(TipoZombie.CORREDOR, "TOXICO");
        System.out.println("Zombi Creado"+ crearZombiToxico2);
        Zombi crearZombiToxico3 = Zombi.crearZombiElegido(TipoZombie.ABOMINACION, "TOXICO");
        System.out.println("Zombi Creado"+ crearZombiToxico3);
        
        
        if (crearZombiNormal1.getCategoria().equals("NORMAL") && crearZombiNormal1.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[crearZombiNormal1.getX()][crearZombiNormal1.getY()].setIcon(new ImageIcon(IconoZombiN.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiNormal2.getCategoria().equals("NORMAL") && crearZombiNormal2.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[crearZombiNormal2.getX()][crearZombiNormal2.getY()].setIcon(new ImageIcon(IconoZombiNC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiNormal3.getCategoria().equals("NORMAL") && crearZombiNormal3.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[crearZombiNormal3.getX()][crearZombiNormal3.getY()].setIcon(new ImageIcon(IconoZombiNA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiBerseker1.getCategoria().equals("BERSEKER") && crearZombiBerseker1.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[crearZombiBerseker1.getX()][crearZombiBerseker1.getY()].setIcon(new ImageIcon(IconoZombiB.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiBerseker2.getCategoria().equals("BERSEKER") && crearZombiBerseker2.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[crearZombiBerseker2.getX()][crearZombiBerseker2.getY()].setIcon(new ImageIcon(IconoZombiBC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiBerseker3.getCategoria().equals("BERSERKER") && crearZombiBerseker3.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[crearZombiBerseker3.getX()][crearZombiBerseker3.getY()].setIcon(new ImageIcon(IconoZombiBA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiToxico1.getCategoria().equals("TOXICO") && crearZombiToxico1.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[crearZombiToxico1.getX()][crearZombiToxico1.getY()].setIcon(new ImageIcon(IconoZombiT.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiToxico2.getCategoria().equals("TOXICO") && crearZombiToxico2.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[crearZombiToxico2.getX()][crearZombiToxico2.getY()].setIcon(new ImageIcon(IconoZombiTC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (crearZombiToxico3.getCategoria().equals("TOXICO") && crearZombiToxico3.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[crearZombiToxico3.getX()][crearZombiToxico3.getY()].setIcon(new ImageIcon(IconoZombiTA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));

            List<Zombi> zombis = List.of(
                crearZombiNormal1, crearZombiNormal2, crearZombiNormal3,
                crearZombiBerseker1, crearZombiBerseker2, crearZombiBerseker3,
                crearZombiToxico1, crearZombiToxico2, crearZombiToxico3
                    );

            // Actualizar el estado del tablero para cada zombi
            for (Zombi zombi : zombis) {
               tablero.tablero[zombi.getX()][zombi.getY()].setHayZombie(true);
            }
            
           // Crear el JComboBox con las opciones
String[] opcionesZombis = {
    "Caminante - Normal",
    "Caminante - Berserker",
    "Caminante - Tóxico",
    "Corredor - Normal",
    "Corredor - Berserker",
    "Corredor - Tóxico",
    "Abominación - Normal",
    "Abominación - Berserker",
    "Abominación - Tóxico"
};

// Crear el JComboBox con las opciones
JComboBox<String> comboBoxZombis = new JComboBox<>(opcionesZombis);

// Agregar el JComboBox al frame
panelSimular.add(new JLabel("Selecciona un zombi:"));
panelSimular.add(comboBoxZombis);
comboBoxZombis.setBounds(30, 275, 200, 20); // Asegúrate de ajustar el tamaño

// Agregar el ActionListener al JComboBox
comboBoxZombis.addActionListener((ActionEvent e) -> {
    String seleccion = (String) comboBoxZombis.getSelectedItem();
    Zombi zombiSeleccionado = null;
    
    // Crear el zombi basado en la opción seleccionada
    switch (seleccion) {
        case "Caminante - Normal" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.CAMINANTE, "NORMAL");
        case "Caminante - Berserker" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.CAMINANTE, "BERSEKER");
        case "Caminante - Tóxico" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.CAMINANTE, "TOXICO");
        case "Corredor - Normal" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.CORREDOR, "NORMAL");
        case "Corredor - Berserker" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.CORREDOR, "BERSEKER");
        case "Corredor - Tóxico" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.CORREDOR, "TOXICO");
        case "Abominación - Normal" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.ABOMINACION, "NORMAL");
        case "Abominación - Berserker" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.ABOMINACION, "BERSEKER");
        case "Abominación - Tóxico" -> zombiSeleccionado = Zombi.crearZombiElegido(TipoZombie.ABOMINACION, "TOXICO");
    }
    
    
    if (zombiSeleccionado != null) {
        
        tablero.botonesTablero[zombiSeleccionado.getX()][zombiSeleccionado.getY()].setIcon(new ImageIcon(IconoZombiN.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
        tablero.tablero[zombiSeleccionado.getX()][zombiSeleccionado.getY()].setHayZombie(true); // Actualiza el estado del tablero
        
     
        System.out.println("Zombi Creado: " + zombiSeleccionado);
    }
        });
            panelSimular.setVisible(true);

        

    }
    
    public void colocarZombiesInicio(){
        ImageIcon IconoZombiN = new ImageIcon(getClass().getResource("/resources/zombiN.png"));
        ImageIcon IconoZombiNA = new ImageIcon(getClass().getResource("/resources/zombiNA.png"));
        ImageIcon IconoZombiNC = new ImageIcon(getClass().getResource("/resources/zombiNC.png"));
        ImageIcon IconoZombiT = new ImageIcon(getClass().getResource("/resources/zombiT.png"));
        ImageIcon IconoZombiTA = new ImageIcon(getClass().getResource("/resources/zombiTA.png"));
        ImageIcon IconoZombiTC = new ImageIcon(getClass().getResource("/resources/zombiTC.png"));
        ImageIcon IconoZombiB = new ImageIcon(getClass().getResource("/resources/zombiB.png"));
        ImageIcon IconoZombiBA = new ImageIcon(getClass().getResource("/resources/zombiBA.png"));
        ImageIcon IconoZombiBC = new ImageIcon(getClass().getResource("/resources/zombiBC.png"));
        
        for (int i = 0; i < numZombies; i++) {
                        
            Zombi nuevoZombie;
            do{
                 nuevoZombie = Zombi.crearZombiAleatorio();
            } while (posicionesUsadas.contains(new Point(nuevoZombie.getX(), nuevoZombie.getY()))); 
                    
            zombies.add(nuevoZombie);
            posicionesUsadas.add(new Point(nuevoZombie.getX(), nuevoZombie.getY()));
            System.out.println("Zombie creado: " + nuevoZombie.getCategoria() + ", " + nuevoZombie.getTipo()+", X: "+ nuevoZombie.getX()+", Y: "+ nuevoZombie.getY());

            if (nuevoZombie.getCategoria().equals("NORMAL") && nuevoZombie.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiN.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("TOXICO") && nuevoZombie.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiT.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("BERSERKER") && nuevoZombie.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiB.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("NORMAL") && nuevoZombie.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiNC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("TOXICO") && nuevoZombie.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiTC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("BERSERKER") && nuevoZombie.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiBC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("NORMAL") && nuevoZombie.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiNA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("TOXICO") && nuevoZombie.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiTA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            if (nuevoZombie.getCategoria().equals("BERSERKER") && nuevoZombie.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiBA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));

            tablero.tablero[nuevoZombie.getX()][nuevoZombie.getY()].setHayZombie(true);
            
        }
    }
    
    public void colocarZombieFinDeRonda(){
        ImageIcon IconoZombiN = new ImageIcon(getClass().getResource("/resources/zombiN.png"));
        ImageIcon IconoZombiNA = new ImageIcon(getClass().getResource("/resources/zombiNA.png"));
        ImageIcon IconoZombiNC = new ImageIcon(getClass().getResource("/resources/zombiNC.png"));
        ImageIcon IconoZombiT = new ImageIcon(getClass().getResource("/resources/zombiT.png"));
        ImageIcon IconoZombiTA = new ImageIcon(getClass().getResource("/resources/zombiTA.png"));
        ImageIcon IconoZombiTC = new ImageIcon(getClass().getResource("/resources/zombiTC.png"));
        ImageIcon IconoZombiB = new ImageIcon(getClass().getResource("/resources/zombiB.png"));
        ImageIcon IconoZombiBA = new ImageIcon(getClass().getResource("/resources/zombiBA.png"));
        ImageIcon IconoZombiBC = new ImageIcon(getClass().getResource("/resources/zombiBC.png"));
               
        Zombi nuevoZombie;
        do{
            nuevoZombie = Zombi.crearZombiAleatorio();
        } while (posicionesUsadas.contains(new Point(nuevoZombie.getX(), nuevoZombie.getY()))); 
                    
        zombies.add(nuevoZombie);
        posicionesUsadas.add(new Point(nuevoZombie.getX(), nuevoZombie.getY()));
        System.out.println("Zombie creado: " + nuevoZombie.getCategoria() + ", " + nuevoZombie.getTipo()+", X: "+ nuevoZombie.getX()+", Y: "+ nuevoZombie.getY());

            
        if (nuevoZombie.getCategoria().equals("NORMAL") && nuevoZombie.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiN.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("TOXICO") && nuevoZombie.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiT.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("BERSERKER") && nuevoZombie.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiB.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("NORMAL") && nuevoZombie.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiNC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("TOXICO") && nuevoZombie.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiTC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("BERSERKER") && nuevoZombie.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiBC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("NORMAL") && nuevoZombie.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiNA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("TOXICO") && nuevoZombie.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiTA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        if (nuevoZombie.getCategoria().equals("BERSERKER") && nuevoZombie.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombiBA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));

        tablero.tablero[nuevoZombie.getX()][nuevoZombie.getY()].setHayZombie(true); 
    }
    
    public void colocarSupervivientes() {
        ImageIcon iconoMeta = new ImageIcon(getClass().getResource("/resources/meta.png"));

        supervivientes = Superviviente.crearSupervivientes();
        
        for (Superviviente superviviente : supervivientes) {
            int x = superviviente.getX();
            int y = superviviente.getY();
            String color = superviviente.getNombre().toLowerCase();  
            tablero.tablero[x][y].agregarSuperviviente(superviviente);

            ImageIcon iconoSuperviviente = null;
            switch (color) {
                case "amarillo":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/amarillo.png"));
                    break;
                case "verde":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/verde.png"));
                    break;
                case "azul":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/azul.png"));
                    break;
                case "rojo":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/rojo.png"));
                    break;
            }

            posicionesUsadas.add(new Point(x, y));

            tablero.botonesTablero[x][y].setIcon(new ImageIcon(iconoSuperviviente.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
            JLabel etiquetaSuperviviente = new JLabel(iconoSuperviviente);
            panelJuego.add(etiquetaSuperviviente);

            System.out.println("Superviviente creado: " + superviviente.getNombre() + ", X: " + x + ", Y: " + y);

            tablero.tablero[x][y].setHaySuperviviente(true);

            if (x == 0 && y == 0) {
                tablero.botonesTablero[9][9].setIcon(new ImageIcon(iconoMeta.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
                metaX = 9;
                metaY = 9;
            } else if (x == 9 && y == 9) {
                tablero.botonesTablero[0][0].setIcon(new ImageIcon(iconoMeta.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
                metaX = 0;
                metaY = 0;
            } else if (x == 9 && y == 0) {
                tablero.botonesTablero[0][9].setIcon(new ImageIcon(iconoMeta.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
                metaX = 0;
                metaY = 9;
            } else if (x == 0 && y == 9) {
                tablero.botonesTablero[9][0].setIcon(new ImageIcon(iconoMeta.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
                metaX = 9;
                metaY = 0;
            }
        }

        // Actualizar la interfaz gráfica
        panelJuego.revalidate();
        panelJuego.repaint();
    }
    
    public void colocarSupervivientesSim() {
        
        supervivientes = Superviviente.crearSupervivienteSim();
        
        for (Superviviente superviviente : supervivientes) {
            int x = superviviente.getX();
            int y = superviviente.getY();
            
            tablero.tablero[x][y].agregarSuperviviente(superviviente);
            String color = superviviente.getNombre().toLowerCase();  
            
            
            
            

            ImageIcon iconoSuperviviente = null;
            switch (color) {
                case "amarillo":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/amarillo.png"));
                    break;
                case "verde":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/verde.png"));
                    break;
                case "azul":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/azul.png"));
                    break;
                case "rojo":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/rojo.png"));
                    break;
            }

            posicionesUsadas.add(new Point(x, y));

            tablero.botonesTablero[x][y].setIcon(new ImageIcon(iconoSuperviviente.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
            JLabel etiquetaSuperviviente = new JLabel(iconoSuperviviente);
            panelSimular.add(etiquetaSuperviviente);

            System.out.println("Superviviente creado: " + superviviente.getNombre() + ", X: " + x + ", Y: " + y);

            tablero.tablero[x][y].setHaySuperviviente(true);
            

            
        }

        // Actualizar la interfaz gráfica
        panelSimular.revalidate();
        panelSimular.repaint();
        
        
    }
    
    public void colocarMetaSim(){
        ImageIcon iconoMeta = new ImageIcon(getClass().getResource("/resources/meta.png"));
        tablero.botonesTablero[9][9].setIcon(new ImageIcon(iconoMeta.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
                metaX = 9;
                metaY = 9;
    }

    public void actualizarTurno() {
        System.out.println("Iniciando un nuevo turno...");
        
        accionesTotales = 0;
        
        for (Superviviente s : supervivientes) {
            s.resetearAcciones();
        }
        
        accionarZombies();
        colocarZombieFinDeRonda();
        actualizarIconos();
        
        indiceActual = 3;
        finJuego();
        panelJuego.revalidate();
        panelJuego.repaint();
        System.out.println("Turno reiniciado. Es el turno de " + supervivientes.get(indiceActual).getNombre());
        etiquetaTurnos.setText("Turno " + supervivientes.get(indiceActual).getNombre());
    }
    
    public void actualizarTurnoSim() {
        System.out.println("Iniciando un nuevo turno...");
        
        accionesTotales = 0;
        
        for (Superviviente s : supervivientes) {
            s.resetearAcciones();
        }
       
        actualizarIconos();
        
        indiceActual = 3;
        finJuego();
        panelSimular.revalidate();
        panelSimular.repaint();
        System.out.println("Turno reiniciado. Es el turno de " + supervivientes.get(indiceActual).getNombre());
        etiquetaTurnos.setText("Turno " + supervivientes.get(indiceActual).getNombre());
    }
    
    public void funcionMoverse(){
        // Texto Elegir Casila:
        etiqueta2 = new JLabel("Casilla Seleccionada:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta2.setBounds(10, 245, 150, 30);
        etiqueta2.setOpaque(true); // Asi podemos poner background
        etiqueta2.setForeground(Color.black);
        etiqueta2.setFont(new Font("arial", Font.BOLD, 12)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiqueta2);
        etiqueta2.setOpaque(false);
        // Texto x e y
        etiqueta3 = new JLabel("X: "+tablero.getCoordenadaXSeleccionada()+"          Y: "+tablero.getCoordenadaYSeleccionada(), SwingConstants.CENTER); // Creamos etiqueta
        etiqueta3.setBounds(25, 275, 100, 20);
        etiqueta3.setOpaque(true); // Asi podemos poner background
        etiqueta3.setForeground(Color.black);
        etiqueta3.setFont(new Font("arial", Font.BOLD, 15)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiqueta3);
        etiqueta3.setOpaque(false);
        panelJuego.add(etiqueta3); 
        // Boton Moverse
        Moverse = new JButton();
        Moverse.setBounds(55, 300, 62, 26);
        Moverse.setEnabled(true);
        Moverse.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Moverse.png"));
        Moverse.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(62, 26, Image.SCALE_AREA_AVERAGING)));
        Moverse.setOpaque(false);
        panelJuego.add(Moverse);
        Moverse.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
            @Override    
            public void actionPerformed(ActionEvent e) {
            Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual
            int nuevaX = tablero.getCoordenadaXSeleccionada();
            int nuevaY = tablero.getCoordenadaYSeleccionada();
            int[] viejaXY = supervivienteActual.getCoordenadas();

            boolean movimientoValido = esMovimientoValido(viejaXY[0], viejaXY[1], nuevaX, nuevaY);            
            
            if (movimientoValido == true) {
                if(supervivienteActual.gastarAccion()){
                    tablero.tablero[viejaXY[0]][viejaXY[1]].eliminarSuperviviente(supervivienteActual);
                    tablero.tablero[nuevaX][nuevaY].agregarSuperviviente(supervivienteActual);
                    supervivienteActual.moverse(nuevaX, nuevaY);

                    actualizarIconoSuper(viejaXY[0], viejaXY[1]);
                    actualizarIconoSuper(nuevaX, nuevaY);
                    tablero.tablero[viejaXY[0]][viejaXY[1]].setHaySuperviviente(false);
                    tablero.tablero[nuevaX][nuevaY].setHaySuperviviente(true);

                    actualizarIconos();
                    accionesTotales++; 
                    System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                     
                }else{
                    System.out.println("Acciones agotadas.");
                    actualizarEtiqueta("Acciones agotadas.");
                }
            } else {
                System.out.println("Movimiento no válido.");
                actualizarEtiqueta("Movimiento no válido.");
            }
            
            if(accionesTotales == 12){
                actualizarTurno();
                System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
            }

            if (supervivienteActual.getAccionesDisponibles() == 0) {
                indiceActual--;
                if (indiceActual < 0) {
                    indiceActual = 3;
                }
                Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                siguienteSuperviviente.resetearAcciones();
                System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                panelJuego.revalidate();
                panelJuego.repaint();
            }

            actualizarIconos();
        }};
        Moverse.addActionListener(accionBoton4);
    }
    
    public void funcionMoverseSim(){
        // Texto Elegir Casila:
        etiqueta2 = new JLabel("Casilla Seleccionada:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta2.setBounds(10, 245, 150, 30);
        etiqueta2.setOpaque(true); // Asi podemos poner background
        etiqueta2.setForeground(Color.black);
        etiqueta2.setFont(new Font("arial", Font.BOLD, 12)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta2);
        etiqueta2.setOpaque(false);
        // Texto x e y
        etiqueta3 = new JLabel("X: "+tablero.getCoordenadaXSeleccionada()+"          Y: "+tablero.getCoordenadaYSeleccionada(), SwingConstants.CENTER); // Creamos etiqueta
        etiqueta3.setBounds(25, 275, 100, 20);
        etiqueta3.setOpaque(true); // Asi podemos poner background
        etiqueta3.setForeground(Color.black);
        etiqueta3.setFont(new Font("arial", Font.BOLD, 15)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta3);
        etiqueta3.setOpaque(false);
        panelSimular.add(etiqueta3); 
        // Boton Moverse
        Moverse = new JButton();
        Moverse.setBounds(55, 300, 62, 26);
        Moverse.setEnabled(true);
        Moverse.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Moverse.png"));
        Moverse.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(62, 26, Image.SCALE_AREA_AVERAGING)));
        Moverse.setOpaque(false);
        panelSimular.add(Moverse);
        Moverse.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
            @Override    
            public void actionPerformed(ActionEvent e) {
            Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual
            int nuevaX = tablero.getCoordenadaXSeleccionada();
            int nuevaY = tablero.getCoordenadaYSeleccionada();
            int[] viejaXY = supervivienteActual.getCoordenadas();

            boolean movimientoValido = esMovimientoValido(viejaXY[0], viejaXY[1], nuevaX, nuevaY);            
            
            if (movimientoValido == true) {
                if(supervivienteActual.gastarAccion()){
                    tablero.tablero[viejaXY[0]][viejaXY[1]].eliminarSuperviviente(supervivienteActual);
                    tablero.tablero[nuevaX][nuevaY].agregarSuperviviente(supervivienteActual);
                    supervivienteActual.moverse(nuevaX, nuevaY);

                    actualizarIconoSuper(viejaXY[0], viejaXY[1]);
                    actualizarIconoSuper(nuevaX, nuevaY);
                    tablero.tablero[viejaXY[0]][viejaXY[1]].setHaySuperviviente(false);
                    tablero.tablero[nuevaX][nuevaY].setHaySuperviviente(true);

                    actualizarIconos();
                    accionesTotales++; 
                    System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                     
                }else{
                    System.out.println("Acciones agotadas.");
                    actualizarEtiqueta("Acciones agotadas.");
                }
            } else {
                System.out.println("Movimiento no válido.");
                actualizarEtiqueta("Movimiento no válido.");
            }
            
            if(accionesTotales == 12){
                actualizarTurno();
                System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
            }

            if (supervivienteActual.getAccionesDisponibles() == 0) {
                indiceActual--;
                if (indiceActual < 0) {
                    indiceActual = 3;
                }
                Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                siguienteSuperviviente.resetearAcciones();
                System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                panelSimular.revalidate();
                panelSimular.repaint();
            }

            actualizarIconos();
        }};
        Moverse.addActionListener(accionBoton4);
    }
    
    private void actualizarIconoSuper(int x, int y) {
        List<Superviviente> supervivientesEnCelda = tablero.tablero[x][y].getSupervivientes();
        tablero.botonesTablero[x][y].setIcon(null);

        for (Superviviente s : supervivientesEnCelda) {
            String color = s.getNombre().toLowerCase();
            ImageIcon iconoSuperviviente = null;
            switch (color) {
                case "rojo":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/rojo.png"));
                    break;
                case "azul":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/azul.png"));
                    break;
                case "verde":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/verde.png"));
                    break;
                case "amarillo":
                    iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/amarillo.png"));
                    break;
            }

            if (iconoSuperviviente != null) {
                tablero.botonesTablero[x][y].setIcon(new ImageIcon(iconoSuperviviente.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
            }
        }
    }
    
    private boolean esMovimientoValido(int xActual, int yActual, int nuevaX, int nuevaY) {
        int deltaX = nuevaX - xActual;
        int deltaY = nuevaY - yActual;

        return (Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1);
    }
    
    public void funcionAtacar(){
        // Texto Elegir Arma:
        etiqueta1 = new JLabel("Elegir Arma:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(30, 245, 100, 30);
        etiqueta1.setOpaque(true); // Asi podemos poner background
        etiqueta1.setForeground(Color.black);
        etiqueta1.setFont(new Font("arial", Font.BOLD, 15)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiqueta1);
        etiqueta1.setOpaque(false);
        
        Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual

        int x = tablero.getCoordenadaXSeleccionada();
        int y = tablero.getCoordenadaYSeleccionada();
        
        String[] opcionesArmas = supervivienteActual.getInventario().obtenerNombresArmasActivas().toArray(new String[0]);
        listaActivas = new JComboBox(opcionesArmas);
        listaActivas.setBounds(30, 275, 100, 20);
        listaActivas.addItem("");
        listaActivas.setSelectedItem("");
        ActionListener accionLista = (ActionEvent e) -> {
            armaSeleccionada1 = (String) listaActivas.getSelectedItem();
            System.out.println("Arma seleccionada: " + armaSeleccionada1);
        };
        listaActivas.addActionListener(accionLista);
        panelJuego.add(listaActivas); 
        // BOTON Atacar
        Atacar = new JButton();
        Atacar.setBounds(55, 300, 62, 26);
        Atacar.setEnabled(true);
        Atacar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Atacar.png"));
        Atacar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(62, 26, Image.SCALE_AREA_AVERAGING)));
        Atacar.setOpaque(false);
        panelJuego.add(Atacar);
        Atacar.setBorderPainted(false);

        ActionListener atacar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int coordenadaXSeleccionada = tablero.getCoordenadaXSeleccionada();
                int coordenadaYSeleccionada = tablero.getCoordenadaYSeleccionada();
                Arma armaSeleccionada = supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);

                // Crear una instancia de Ataque y pasar this como referencia de ventana
                Ataque ataque = new Ataque(VentanaJuego.this);
                
                Zombi zombieAtacado = buscarZombie(coordenadaXSeleccionada, coordenadaYSeleccionada);
                
                if (armaSeleccionada1 == null || armaSeleccionada1.trim().isEmpty()) {
                            System.out.println("No se seleccionó un arma válida.");
                            return; 
                        }
                if(!ataqueValido(armaSeleccionada, supervivienteActual, zombieAtacado)){
                        System.out.println("Zombie fuera del alcance.");
                        actualizarEtiqueta("Zombie fuera del alcance.");
                }else{
                    if (ataqueValido(armaSeleccionada, supervivienteActual, zombieAtacado)) {
                        if(supervivienteActual.gastarAccion() ){
                            ataque.atacarZombieF(armaSeleccionada, supervivienteActual, coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado);
                            accionesTotales++;
                            System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                        }else{
                            System.out.println("Acciones agotadas.");
                        }  
                    } else {
                            System.out.println("Movimiento no válido.");
                    }

                    if (accionesTotales == 12) {
                            System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                            accionarZombies();
                            colocarZombieFinDeRonda();
                            actualizarTurno(); 
                        }

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual < 0) {
                            indiceActual = 3;
                        }
                        Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                        siguienteSuperviviente.resetearAcciones();
                        System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                        etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                        panelJuego.revalidate();
                        panelJuego.repaint();
                    } 
                }
                actualizarIconos();
            }
        };
        Atacar.addActionListener(atacar);
        
    }
    
    public void funcionAtacarSim(){
        // Texto Elegir Arma:
        etiqueta1 = new JLabel("Elegir Arma:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(30, 245, 100, 30);
        etiqueta1.setOpaque(true); // Asi podemos poner background
        etiqueta1.setForeground(Color.black);
        etiqueta1.setFont(new Font("arial", Font.BOLD, 15)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta1);
        etiqueta1.setOpaque(false);
        
        Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual

        int x = tablero.getCoordenadaXSeleccionada();
        int y = tablero.getCoordenadaYSeleccionada();
        
        String[] opcionesArmas = supervivienteActual.getInventario().obtenerNombresArmasActivas().toArray(new String[0]);
        listaActivas = new JComboBox(opcionesArmas);
        listaActivas.setBounds(30, 275, 100, 20);
        listaActivas.addItem("");
        listaActivas.setSelectedItem("");
        ActionListener accionLista = (ActionEvent e) -> {
            armaSeleccionada1 = (String) listaActivas.getSelectedItem();
            System.out.println("Arma seleccionada: " + armaSeleccionada1);
        };
        listaActivas.addActionListener(accionLista);
        panelSimular.add(listaActivas); 
        // BOTON Atacar
        Atacar = new JButton();
        Atacar.setBounds(55, 300, 62, 26);
        Atacar.setEnabled(true);
        Atacar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Atacar.png"));
        Atacar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(62, 26, Image.SCALE_AREA_AVERAGING)));
        Atacar.setOpaque(false);
        panelSimular.add(Atacar);
        Atacar.setBorderPainted(false);

        ActionListener atacar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int coordenadaXSeleccionada = tablero.getCoordenadaXSeleccionada();
                int coordenadaYSeleccionada = tablero.getCoordenadaYSeleccionada();
                Arma armaSeleccionada = supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);

                // Crear una instancia de Ataque y pasar this como referencia de ventana
                Ataque ataque = new Ataque(VentanaJuego.this);
                
                Zombi zombieAtacado = buscarZombie(coordenadaXSeleccionada, coordenadaYSeleccionada);
                
                if (armaSeleccionada1 == null || armaSeleccionada1.trim().isEmpty()) {
                            System.out.println("No se seleccionó un arma válida.");
                            return; 
                        }
                if(!ataqueValido(armaSeleccionada, supervivienteActual, zombieAtacado)){
                        System.out.println("Zombie fuera del alcance.");
                        actualizarEtiqueta("Zombie fuera del alcance.");
                }else{
                    if (ataqueValido(armaSeleccionada, supervivienteActual, zombieAtacado)) {
                        if(supervivienteActual.gastarAccion() ){
                            ataque.atacarZombieF(armaSeleccionada, supervivienteActual, coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado);
                            accionesTotales++;
                            System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                        }else{
                            System.out.println("Acciones agotadas.");
                        }  
                    } else {
                            System.out.println("Movimiento no válido.");
                    }

                    if (accionesTotales == 12) {
                            System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                            accionarZombies();
                            colocarZombieFinDeRonda();
                            actualizarTurno(); 
                        }

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual < 0) {
                            indiceActual = 3;
                        }
                        Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                        siguienteSuperviviente.resetearAcciones();
                        System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                        etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                        panelSimular.revalidate();
                        panelSimular.repaint();
                    } 
                }
                actualizarIconos();
            }
        };
        Atacar.addActionListener(atacar);
        
    }

    public boolean ataqueValido(Arma arma, Superviviente supervivienteActual, Zombi zombieAtacado){
        
        int distancia = calcularDistancia(supervivienteActual.getX(), supervivienteActual.getY(), zombieAtacado.getX(), zombieAtacado.getY());
        
        return(distancia < arma.getPotencia() );
        
    }
    
    public int calcularDistancia(int x1, int y1, int x2, int y2) {
        int distancia = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        System.out.println("La distancia calculada es: " + distancia);
        return distancia;
    }
     
    public void funcionQuedarse(){
        // Boton Moverse
        SiguienteTurno = new JButton();
        SiguienteTurno.setBounds(25, 275, 120, 25);
        SiguienteTurno.setEnabled(true);
        SiguienteTurno.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/SiguienteTurno.png"));
        SiguienteTurno.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(120, 25, Image.SCALE_AREA_AVERAGING)));
        SiguienteTurno.setOpaque(false);
        panelJuego.add(SiguienteTurno);
        SiguienteTurno.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual

                    if (supervivienteActual.gastarAccion()) {
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Movimiento no válido o acciones agotadas.");
                    }
                    
                    if (accionesTotales == 12) {
                        actualizarTurno();
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                    }

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual < 0) {
                            indiceActual = 3;
                        }
                        Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                        siguienteSuperviviente.resetearAcciones();
                        System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                        etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                        panelJuego.revalidate();
                        panelJuego.repaint();
                    }
                    actualizarIconos();
           }
        };
        SiguienteTurno.addActionListener(accionBoton4);
    }
    
    public void funcionQuedarseSim(){
        // Boton Moverse
        SiguienteTurno = new JButton();
        SiguienteTurno.setBounds(25, 275, 120, 25);
        SiguienteTurno.setEnabled(true);
        SiguienteTurno.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/SiguienteTurno.png"));
        SiguienteTurno.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(120, 25, Image.SCALE_AREA_AVERAGING)));
        SiguienteTurno.setOpaque(false);
        panelSimular.add(SiguienteTurno);
        SiguienteTurno.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual

                    if (supervivienteActual.gastarAccion()) {
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Movimiento no válido o acciones agotadas.");
                    }
                    
                    if (accionesTotales == 12) {
                        actualizarTurno();
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                    }

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual < 0) {
                            indiceActual = 3;
                        }
                        Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                        siguienteSuperviviente.resetearAcciones();
                        System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                        etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                        panelSimular.revalidate();
                        panelSimular.repaint();
                    }
                    actualizarIconos();
           }
        };
        SiguienteTurno.addActionListener(accionBoton4);
    }
    
    public void funcionBuscar(){
        // boton Buscar
        Buscar = new JButton();
        Buscar.setBounds(35, 265, 100, 40);
        Buscar.setEnabled(true);
        Buscar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Buscar.png"));
        Buscar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 40, Image.SCALE_AREA_AVERAGING)));
        Buscar.setOpaque(false);
        panelJuego.add(Buscar);
        Buscar.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Buscando");
                   
                   
                   Superviviente supervivienteActual = supervivientes.get(indiceActual);
                   
                   if (supervivienteActual.gastarAccion()) {
                        buscar();
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Acción no valida o acciones agotadas.");
                    }
                   
                    if (accionesTotales == 12) {
                        actualizarTurno(); 
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                    } 

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual < 0) {
                            indiceActual = 3;
                        }
                        Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                        siguienteSuperviviente.resetearAcciones();
                        System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                        etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                        panelJuego.revalidate();
                        panelJuego.repaint();
                    }
                    actualizarIconos();
           }
        };
        Buscar.addActionListener(accionBoton4);
    }
    
    public void funcionBuscarSim(){
        // boton Buscar
        Buscar = new JButton();
        Buscar.setBounds(35, 265, 100, 40);
        Buscar.setEnabled(true);
        Buscar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Buscar.png"));
        Buscar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 40, Image.SCALE_AREA_AVERAGING)));
        Buscar.setOpaque(false);
        panelSimular.add(Buscar);
        Buscar.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Buscando");
                   
                   
                   Superviviente supervivienteActual = supervivientes.get(indiceActual);
                   
                   if (supervivienteActual.gastarAccion()) {
                        buscar();
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Acción no valida o acciones agotadas.");
                    }
                   
                    if (accionesTotales == 12) {
                        actualizarTurno(); 
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                    } 

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual < 0) {
                            indiceActual = 3;
                        }
                        Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                        siguienteSuperviviente.resetearAcciones();
                        System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                        etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                        panelSimular.revalidate();
                        panelSimular.repaint();
                    }
                    actualizarIconos();
           }
        };
        Buscar.addActionListener(accionBoton4);
    }
    
    private String armaSeleccionada1 = "";
    private String armaSeleccionada2 = "";
    private String armaSeleccionada3 = ""; //El arma que se desactiva

    public void funcionElegirArma() {
        // Texto Elegir Arma:
        etiqueta1 = new JLabel("Elegir Armas activas:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(28, 245, 120, 30);
        etiqueta1.setOpaque(true); // Así podemos poner background
        etiqueta1.setForeground(Color.black);
        etiqueta1.setFont(new Font("arial", Font.CENTER_BASELINE, 12)); // Establece el font
        panelJuego.add(etiqueta1);
        etiqueta1.setOpaque(false);

        int x = tablero.getCoordenadaXSeleccionada();
        int y = tablero.getCoordenadaYSeleccionada();
        Superviviente superviviente = supervivientes.get(indiceActual); 

        // ---------------------------------------------------------------
        if (superviviente != null) {
            if (superviviente.getInventario().obtenerArmasActivas().size()==2) {
                etiqueta1.setText("Desactivar Arma:");
                // Obtener las opciones de armas para el JComboBox
                String[] opcionesArmas = superviviente.getInventario().obtenerNombresArmasActivas().toArray(new String[0]);

                // primera opcion armas activas
                listaArmas = new JComboBox(opcionesArmas);
                listaArmas.setBounds(30, 275, 100, 20);
                listaArmas.addItem(" ");
                listaArmas.setSelectedItem(" ");
                panelJuego.add(listaArmas);
                
                Seleccionar = new JButton();
                Seleccionar.setBounds(35, 300, 100, 30);
                Seleccionar.setEnabled(true);
                Seleccionar.setBackground(Color.red);
                ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Seleccionar.png"));
                Seleccionar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 30, Image.SCALE_AREA_AVERAGING)));
                Seleccionar.setOpaque(false);
                panelJuego.add(Seleccionar);
                Seleccionar.setBorderPainted(false);
                
                listaArmas.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String seleccion = (String) listaArmas.getSelectedItem();

                        if (!seleccion.equals(" ")) {
                            armaSeleccionada3 = (String) listaArmas.getSelectedItem();
                        }
                    }
                });
                Seleccionar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        superviviente.getInventario().desactivarArma(armaSeleccionada3);  
                            
                        actualizarEtiqueta("Has desactivado "+armaSeleccionada3+".");          
                    }
                });
            }
            else if (!superviviente.getInventario().obtenerObjetos().isEmpty()) {
                // Obtener las opciones de armas para el JComboBox
                String[] opcionesArmas = superviviente.getInventario().obtenerNombresArmasNA().toArray(new String[0]);

                // primera opcion armas activas
                listaArmas = new JComboBox(opcionesArmas);
                listaArmas.setBounds(30, 275, 50, 20);
                listaArmas.addItem(" ");
                listaArmas.setSelectedItem(" ");
                panelJuego.add(listaArmas);

                // segunda opción para elegir arma
                listaArmas2 = new JComboBox(opcionesArmas);
                listaArmas2.setBounds(90, 275, 50, 20);
                listaArmas2.addItem("Ninguna");
                listaArmas2.setSelectedItem("Ninguna");
                panelJuego.add(listaArmas2);

                Seleccionar = new JButton();
                Seleccionar.setBounds(35, 300, 100, 30);
                Seleccionar.setEnabled(true);
                Seleccionar.setBackground(Color.red);
                ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Seleccionar.png"));
                Seleccionar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 30, Image.SCALE_AREA_AVERAGING)));
                Seleccionar.setOpaque(false);
                panelJuego.add(Seleccionar);
                Seleccionar.setBorderPainted(false);

                listaArmas.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String seleccion = (String) listaArmas.getSelectedItem();

                        if (!seleccion.equals(" ")) {
                            armaSeleccionada1 = (String) listaArmas.getSelectedItem();
                            listaArmas2.removeItem(armaSeleccionada1);
                        }
                    }
                });

                listaArmas2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        armaSeleccionada2 = (String) listaArmas2.getSelectedItem();
                        listaArmas.removeItem(armaSeleccionada2);

                    }
                });

                Seleccionar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        superviviente.getInventario().activarArma(armaSeleccionada1);  
                        superviviente.getInventario().activarArma(armaSeleccionada2);
                            
                        actualizarEtiqueta("Has seleccionado "+armaSeleccionada1+" y "+armaSeleccionada2+".");
                        
                        Superviviente supervivienteActual = supervivientes.get(indiceActual);
                        if (supervivienteActual.gastarAccion()) {
                            accionesTotales++;
                            System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                        } else {
                            System.out.println("Movimiento no válido o acciones agotadas.");
                        }

                        if (accionesTotales == 12) {
                            actualizarTurno(); 
                            System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                        }
                        
                        if (supervivienteActual.getAccionesDisponibles() == 0) {
                            indiceActual--;
                            if (indiceActual < 0) {
                                indiceActual = 3;
                            }
                            Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                            siguienteSuperviviente.resetearAcciones();
                            System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                            etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                            panelJuego.revalidate();
                            panelJuego.repaint();
                        }                    
                    }
                });
            } else {
                actualizarEtiqueta("Superviviente sin armas.");
            }
        } else {
            actualizarEtiqueta("Seleccione una casilla con Superviviente.");
        }
    }
    
    
    
    public void buscar (){
        Superviviente superviviente = supervivientes.get(indiceActual); 
        
        if (superviviente != null) {
            if (superviviente.getInventario() != null && superviviente.getInventario().obtenerObjetos().size()<5) {
            superviviente.getInventario().agregarItem();
            actualizarEtiqueta("Se ha encontrado "+ superviviente.getInventario().obtenerNombres().getLast());
        }else actualizarEtiqueta("Inventario lleno.");
        }else {
            actualizarEtiqueta("Seleccione una casilla con Superviviente.");
        }
    }
 
    public void limpiarPanel() {
    // Limpiar Moverse
        if (Moverse != null) {
            panelJuego.remove(Moverse);
        }
        if (etiqueta2 != null) {
            panelJuego.remove(etiqueta2);
        }
        if (etiqueta3 != null) {
            panelJuego.remove(etiqueta3);
        }
    // Limpiar Atacar
        if (Atacar != null) {
            panelJuego.remove(Atacar);
        }
        if (etiqueta1 != null) {
            panelJuego.remove(etiqueta1);
        }
        if (listaActivas != null) {
            panelJuego.remove(listaActivas);
        }
    // Limpiar Quedarse
        if (SiguienteTurno != null) {
            panelJuego.remove(SiguienteTurno);
        }
    // Limpiar buscar
        if (Buscar != null) {
            panelJuego.remove(Buscar);
        }
        if (Seleccionar != null) {
            panelJuego.remove(Seleccionar);
        }
        if (etiqueta1 != null) {
            panelJuego.remove(etiqueta1);
        }
        if (listaArmas != null) {
            panelJuego.remove(listaArmas);
        }
        if (listaArmas2 != null) {
            panelJuego.remove(listaArmas2);
        }
        if (etiquetaStatus != null) {
            panelJuego.remove(etiquetaStatus);
            etiquetaStatus = null;
        }
        
        if (scrollPanel != null) {
            panelJuego.remove(scrollPanel);
            scrollPanel = null;
        }
        if (listaSuper != null) {
            panelJuego.remove(listaSuper);
        }
        if (zombisEliminadosOrde != null) {
            panelJuego.remove(zombisEliminadosOrde);
        }
    panelJuego.revalidate();
    panelJuego.repaint();
    }
    
    public void limpiarPanelSim() {
    // Limpiar Moverse
        if (Moverse != null) {
            panelSimular.remove(Moverse);
        }
        if (etiqueta2 != null) {
            panelSimular.remove(etiqueta2);
        }
        if (etiqueta3 != null) {
            panelSimular.remove(etiqueta3);
        }
    // Limpiar Atacar
        if (Atacar != null) {
            panelSimular.remove(Atacar);
        }
        if (etiqueta1 != null) {
            panelSimular.remove(etiqueta1);
        }
        if (listaActivas != null) {
            panelSimular.remove(listaActivas);
        }
    // Limpiar Quedarse
        if (SiguienteTurno != null) {
            panelSimular.remove(SiguienteTurno);
        }
    // Limpiar buscar
        if (Buscar != null) {
            panelSimular.remove(Buscar);
        }
        if (Seleccionar != null) {
            panelSimular.remove(Seleccionar);
        }
        if (etiqueta1 != null) {
            panelSimular.remove(etiqueta1);
        }
        if (listaArmas != null) {
            panelSimular.remove(listaArmas);
        }
        if (listaArmas2 != null) {
            panelSimular.remove(listaArmas2);
        }
        if (etiquetaStatus != null) {
            panelSimular.remove(etiquetaStatus);
            etiquetaStatus = null;
        }
        if (scrollPanel != null) {
            panelSimular.remove(scrollPanel);
            scrollPanel = null;
        }

    panelSimular.revalidate();
    panelSimular.repaint();
    }
    
    public void actualizarEtiquetaCoordenadas(int x, int y) {
        if (etiqueta3 != null) {
            etiqueta3.setText("X: " + x + "          Y: " + y);
        }
    }
    
    public Arma getArmaSeleccionada() {
        if (listaActivas != null) {
            Superviviente supervivienteActual = supervivientes.get(indiceActual); // Obtener al superviviente actual
            String arma = listaActivas.getSelectedItem().toString();
            return supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);
        }
        return null; 
    }
     
    public Zombi buscarZombie(int x, int y) {
        for (Zombi zombie : zombies) {
            if (zombie.getX() == x && zombie.getY() == y) {
                return zombie;
            }
        }
        return null;
    }  
    
    private Superviviente buscarSuperviviente(int x, int y) {
        for (Superviviente superviviente : supervivientes) {
            if (superviviente.getX() == x && superviviente.getY() == y) {
                return superviviente; 
            }
        }
        return null; 
    } 
    
    public void statusCasilla(){

        int x = tablero.getCoordenadaXSeleccionada();
        int y = tablero.getCoordenadaYSeleccionada();
        boolean esMeta = false;

        String contenido = "";

        Casilla casilla = tablero.tablero[x][y];

        if(metaX == x && metaY == y){
            contenido = "Es la Meta.";
            esMeta = true;
        }
        
        if (casilla.tieneZombie()) {
            Zombi zombi = buscarZombie(x, y);
            contenido += "Contiene: Zombi\nTipo: " + zombi.getTipo()+"\nCategoria: "+ zombi.getCategoria();
        }

        if (casilla.tieneSuperviviente()) {
            List<Superviviente> supervivientesCasilla = tablero.tablero[x][y].getSupervivientes();
            for (Superviviente superviviente : supervivientesCasilla) {
                if (casilla.tieneZombie()) contenido += "\n";
                contenido += "---------------\n";
                contenido += "Contiene: " + superviviente.getNombre() + "\nVida: " + superviviente.getSalud() +
                     "\nInventario: " + superviviente.getInventario().obtenerNombres()+"\n";
            }
            contenido += "---------------";
        }

        if (!casilla.tieneZombie() && !casilla.tieneSuperviviente() && !esMeta) {
            contenido = "Casilla Vacia.";
        }
        actualizarEtiqueta(contenido);  
    }
    
    public void statusCasillaSim(){

        int x = tablero.getCoordenadaXSeleccionada();
        int y = tablero.getCoordenadaYSeleccionada();
        boolean esMeta = false;

        String contenido = "";

        Casilla casilla = tablero.tablero[x][y];

        if(metaX == x && metaY == y){
            contenido = "Es la Meta.";
            esMeta = true;
        }
        
        if (casilla.tieneZombie()) {
            Zombi zombi = buscarZombie(x, y);
            contenido += "Contiene: Zombi\nTipo: " + zombi.getTipo()+"\nCategoria: "+ zombi.getCategoria();
        }

        if (casilla.tieneSuperviviente()) {
            List<Superviviente> supervivientesCasilla = tablero.tablero[x][y].getSupervivientes();
            for (Superviviente superviviente : supervivientesCasilla) {
                if (casilla.tieneZombie()) contenido += "\n";
                contenido += "---------------\n";
                contenido += "Contiene: " + superviviente.getNombre() + "\nVida: " + superviviente.getSalud() +
                     "\nInventario: " + superviviente.getInventario().obtenerNombres()+"\n";
            }
            contenido += "---------------";
        }

        if (!casilla.tieneZombie() && !casilla.tieneSuperviviente() && !esMeta) {
            contenido = "Casilla Vacia.";
        }
        actualizarEtiquetaSim(contenido);  
    }
    
    public void actualizarEtiqueta(String contenido){
        limpiarPanel();
        etiquetaStatus = new JTextArea(contenido);
        etiquetaStatus.setEditable(false);
        etiquetaStatus.setLineWrap(true);
        etiquetaStatus.setWrapStyleWord(true);
        etiquetaStatus.setForeground(Color.black);
        etiquetaStatus.setFont(new Font("Chiller", Font.BOLD, 17));
        etiquetaStatus.setOpaque(false);
        
        scrollPanel = new JScrollPane(etiquetaStatus);
        scrollPanel.setBounds(22, 245, 130, 86);
        scrollPanel.setOpaque(false);
        scrollPanel.getViewport().setOpaque(false);
        scrollPanel.setBorder(null);
        panelJuego.add(scrollPanel);

        panelJuego.revalidate();
        panelJuego.repaint(); 
    } 
    
    public void actualizarEtiquetaSim(String contenido){
        limpiarPanelSim();
        etiquetaStatus = new JTextArea(contenido);
        etiquetaStatus.setEditable(false);
        etiquetaStatus.setLineWrap(true);
        etiquetaStatus.setWrapStyleWord(true);
        etiquetaStatus.setForeground(Color.black);
        etiquetaStatus.setFont(new Font("Chiller", Font.BOLD, 17));
        etiquetaStatus.setOpaque(false);
        
        scrollPanel = new JScrollPane(etiquetaStatus);
        scrollPanel.setBounds(22, 245, 130, 86);
        scrollPanel.setOpaque(false);
        scrollPanel.getViewport().setOpaque(false);
        scrollPanel.setBorder(null);
        panelSimular.add(scrollPanel);

        panelSimular.revalidate();
        panelSimular.repaint(); 
    } 
    
    public void accionarZombies() {
        for (Zombi zombi : zombies) {
            for (int i = 0; i < zombi.getActivaciones(); i++) {
                int x = zombi.getX();
                int y = zombi.getY();

                Casilla casilla = tablero.tablero[x][y];
                List<Superviviente> supervivientesEnCasilla = casilla.getSupervivientes();

                if (!supervivientesEnCasilla.isEmpty() && zombi.getCategoria().equals("TOXICO")) {
                    Superviviente primerSuperviviente = supervivientesEnCasilla.get(0);
                    primerSuperviviente.envenenar();
                    primerSuperviviente.recibirHerida();

                    System.out.println("¡El zombi mordió a " + primerSuperviviente.getNombre() + "!");
                    Zombi.superAtacados.add("Zombi: "+zombi.getIdentificador()+", Categoria: "+zombi.getCategoria()+", Mordió: "+ primerSuperviviente.getNombre()+"\n---------------\n");
                    actualizarEtiqueta("¡El zombi mordió a " + primerSuperviviente.getNombre() + "!");
                    if (primerSuperviviente.getSalud() == 0) {
                        tablero.tablero[x][y].eliminarSuperviviente(primerSuperviviente);
                        tablero.tablero[x][y].setHaySuperviviente(false);
                        tablero.botonesTablero[x][y].setIcon(null);
                        finJuego();
                    }
                }

                Superviviente objetivo = encontrarSupervivienteCercano(x, y);
                if (objetivo != null) {
                    int xObjetivo = objetivo.getX();
                    int yObjetivo = objetivo.getY();

                    int nuevaX = x + Integer.signum(xObjetivo - x);
                    int nuevaY = y + Integer.signum(yObjetivo - y);

                    if (tablero.esPosicionValida(nuevaX, nuevaY)) {
                        tablero.tablero[x][y].eliminarZombie(zombi);
                        tablero.tablero[x][y].setHayZombie(false);
                        tablero.botonesTablero[x][y].setIcon(null);
                        
                        tablero.tablero[nuevaX][nuevaY].agregarZombie(zombi);
                        tablero.tablero[nuevaX][nuevaY].setHayZombie(true);
                        actualizarIconoZombie(nuevaX, nuevaY);

                        zombi.moverse(nuevaX, nuevaY);
                        System.out.println("Zombi movido de (" + x + ", " + y + ") a (" + nuevaX + ", " + nuevaY + ")");
                    }
                }
            }
        }
        actualizarIconos();
        SwingUtilities.invokeLater(() -> {
            panelJuego.revalidate();
            panelJuego.repaint();
        });
    }
    
    public void accionarZombiesSim() {
        for (Zombi zombi : zombies) {
            for (int i = 0; i < zombi.getActivaciones(); i++) {
                int x = zombi.getX();
                int y = zombi.getY();

                Casilla casilla = tablero.tablero[x][y];
                List<Superviviente> supervivientesEnCasilla = casilla.getSupervivientes();

                if (!supervivientesEnCasilla.isEmpty() && zombi.getCategoria().equals("TOXICO")) {
                    Superviviente primerSuperviviente = supervivientesEnCasilla.get(0);
                    primerSuperviviente.envenenar();
                    primerSuperviviente.recibirHerida();

                    System.out.println("¡El zombi mordió a " + primerSuperviviente.getNombre() + "!");
                    Zombi.superAtacados.add("Zombi: "+zombi.getIdentificador()+", Categoria: "+zombi.getCategoria()+", Mordió: "+ primerSuperviviente.getNombre()+"\n---------------\n");
                    actualizarEtiqueta("¡El zombi mordió a " + primerSuperviviente.getNombre() + "!");
                    if (primerSuperviviente.getSalud() == 0) {
                        tablero.tablero[x][y].eliminarSuperviviente(primerSuperviviente);
                        tablero.tablero[x][y].setHaySuperviviente(false);
                        tablero.botonesTablero[x][y].setIcon(null);
                        finJuego();
                    }
                }

                Superviviente objetivo = encontrarSupervivienteCercano(x, y);
                if (objetivo != null) {
                    int xObjetivo = objetivo.getX();
                    int yObjetivo = objetivo.getY();

                    int nuevaX = x + Integer.signum(xObjetivo - x);
                    int nuevaY = y + Integer.signum(yObjetivo - y);

                    if (tablero.esPosicionValida(nuevaX, nuevaY)) {
                        tablero.tablero[x][y].eliminarZombie(zombi);
                        tablero.tablero[x][y].setHayZombie(false);
                        tablero.botonesTablero[x][y].setIcon(null);
                        
                        tablero.tablero[nuevaX][nuevaY].agregarZombie(zombi);
                        tablero.tablero[nuevaX][nuevaY].setHayZombie(true);
                        actualizarIconoZombie(nuevaX, nuevaY);

                        zombi.moverse(nuevaX, nuevaY);
                        System.out.println("Zombi movido de (" + x + ", " + y + ") a (" + nuevaX + ", " + nuevaY + ")");
                    }
                }
            }
        }
        actualizarIconosSim();
        SwingUtilities.invokeLater(() -> {
            panelSimular.revalidate();
            panelSimular.repaint();
        });
    }
    
    private Superviviente encontrarSupervivienteCercano(int x, int y) {
        Superviviente masCercano = null;
        int distanciaMinima = Integer.MAX_VALUE;

        for (Superviviente superviviente : supervivientes) {
            int distancia = calcularDistancia(x, y, superviviente.getX(), superviviente.getY());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                masCercano = superviviente;
            }
        }

        return masCercano;
    }
    
    public void actualizarIconoZombie(int x, int y) {
        Casilla casilla = tablero.tablero[x][y];
        Zombi zombi = buscarZombie(x,y);
        tablero.botonesTablero[x][y].setIcon(null); 
        ImageIcon iconoZombie = null;
        
        if (zombi!=null) {

            switch (zombi.getCategoria()) {
                case "NORMAL":
                    if (zombi.getTipo() == TipoZombie.CAMINANTE) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiN.png"));
                    } else if (zombi.getTipo() == TipoZombie.CORREDOR) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiNC.png"));
                    } else if (zombi.getTipo() == TipoZombie.ABOMINACION) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiNA.png"));
                    }
                    break;

                case "TOXICO":
                    if (zombi.getTipo() == TipoZombie.CAMINANTE) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiT.png"));
                    } else if (zombi.getTipo() == TipoZombie.CORREDOR) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiTC.png"));
                    } else if (zombi.getTipo() == TipoZombie.ABOMINACION) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiTA.png"));
                    }
                    break;

                case "BERSERKER":
                    if (zombi.getTipo() == TipoZombie.CAMINANTE) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiB.png"));
                    } else if (zombi.getTipo() == TipoZombie.CORREDOR) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiBC.png"));
                    } else if (zombi.getTipo() == TipoZombie.ABOMINACION) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiBA.png"));
                    }
                    break;
            }

            if (iconoZombie != null) {
                tablero.botonesTablero[x][y].setIcon(new ImageIcon(
                    iconoZombie.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)
                ));
            }
        }

        tablero.botonesTablero[x][y].revalidate();
        tablero.botonesTablero[x][y].repaint();
        panelJuego.revalidate();
        panelJuego.repaint();
    }
    
    public void actualizarIconoZombieSim(int x, int y) {
        Casilla casilla = tablero.tablero[x][y];
        Zombi zombi = buscarZombie(x,y);
        tablero.botonesTablero[x][y].setIcon(null); 
        ImageIcon iconoZombie = null;
        
        if (zombi!=null) {

            switch (zombi.getCategoria()) {
                case "NORMAL":
                    if (zombi.getTipo() == TipoZombie.CAMINANTE) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiN.png"));
                    } else if (zombi.getTipo() == TipoZombie.CORREDOR) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiNC.png"));
                    } else if (zombi.getTipo() == TipoZombie.ABOMINACION) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiNA.png"));
                    }
                    break;

                case "TOXICO":
                    if (zombi.getTipo() == TipoZombie.CAMINANTE) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiT.png"));
                    } else if (zombi.getTipo() == TipoZombie.CORREDOR) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiTC.png"));
                    } else if (zombi.getTipo() == TipoZombie.ABOMINACION) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiTA.png"));
                    }
                    break;

                case "BERSERKER":
                    if (zombi.getTipo() == TipoZombie.CAMINANTE) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiB.png"));
                    } else if (zombi.getTipo() == TipoZombie.CORREDOR) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiBC.png"));
                    } else if (zombi.getTipo() == TipoZombie.ABOMINACION) {
                        iconoZombie = new ImageIcon(getClass().getResource("/resources/zombiBA.png"));
                    }
                    break;
            }

            if (iconoZombie != null) {
                tablero.botonesTablero[x][y].setIcon(new ImageIcon(
                    iconoZombie.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)
                ));
            }
        }

        tablero.botonesTablero[x][y].revalidate();
        tablero.botonesTablero[x][y].repaint();
        panelSimular.revalidate();
        panelSimular.repaint();
    }
    
    private void finJuego() {
        
        Casilla casillaMeta = tablero.tablero[metaX][metaY];

        List<Superviviente> supervivientesEnMeta = casillaMeta.getSupervivientes();
        List<Superviviente> todosLosSupervivientes = supervivientes;
        
        for (Superviviente superviviente : todosLosSupervivientes) {
            if (superviviente.getSalud()<1) {
                    System.out.println("El superviviente "+superviviente.getNombre()+" ha muerto, fin de la partida.");
                    mostrarGameStatusFinal(false);
                }
        }
        
        if (supervivientesEnMeta.containsAll(todosLosSupervivientes)) {
            // Comprobar que todos los supervivientes tienen al menos una provisión
            boolean todosConProvisiones = true;
            for (Superviviente superviviente : todosLosSupervivientes) {
                int cantidadProvisiones = 0;
                for (Object item : superviviente.getInventario().obtenerObjetos()) {
                    if (item instanceof Provisiones) cantidadProvisiones++;
                }
                if (cantidadProvisiones < 1) {
                    System.out.println("El superviviente "+superviviente.getNombre()+ " no tiene suficientes provisiones.");
                    todosConProvisiones = false;
                    break;
                } 
            }

            if (todosConProvisiones) {
                System.out.println("¡Todos los supervivientes han llegado a la meta con provisiones! ¡Has ganado!");
                mostrarGameStatusFinal(true);
            } else {
                System.out.println("No todos los supervivientes tienen provisiones suficientes.");
                actualizarEtiqueta("No todos los supervivientes tienen provisiones suficientes.");
            }
        }
    }
    
    private void mostrarGameStatusFinal(Boolean status) {
        this.getContentPane().removeAll();
        
        Atras = new JButton();
        Atras.setBounds(20, 360, 40, 40);
        Atras.setEnabled(true);
        Atras.setBackground(Color.red);        
        ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
        Atras.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        Atras.setOpaque(false);
        Atras.setBorderPainted(false);
        this.add(Atras);

        ActionListener accionBoton4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colocarPanelMain();
            }
        };
        Atras.addActionListener(accionBoton4);
       
        panel = new BackgroundPanel("/resources/woods.png"); 
        panel.setLayout(null);
        this.getContentPane().add(panel);
        JLabel gameStatus = new JLabel("", SwingConstants.CENTER);
        gameStatus.setBounds(25, 180, 400, 65);
        gameStatus.setOpaque(true); // Asi podemos poner background
        gameStatus.setForeground(Color.red);
        gameStatus.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panel.add(gameStatus); //agregamos la etiqueta al panel
        gameStatus.setOpaque(false);
        
        if (status) {
            gameStatus.setText("¡Victoria!");
            gameStatus.setForeground(Color.green);
        }
        else gameStatus.setText("Derrota");
        
        panel.add(gameStatus);
        
        panelJuego.revalidate();
        panelJuego.repaint();
        this.revalidate();
        this.repaint();
    }
    
    private void mostrarGameStatusFinalSimular(Boolean status) {
        this.getContentPane().removeAll();
        
        Atras = new JButton();
        Atras.setBounds(20, 360, 40, 40);
        Atras.setEnabled(true);
        Atras.setBackground(Color.red);        
        ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
        Atras.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        Atras.setOpaque(false);
        Atras.setBorderPainted(false);
        this.add(Atras);

        ActionListener accionBoton4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colocarPanelMain();
            }
        };
        Atras.addActionListener(accionBoton4);
       
        panel = new BackgroundPanel("/resources/woods.png"); 
        panel.setLayout(null);
        this.getContentPane().add(panel);
        JLabel gameStatus = new JLabel("", SwingConstants.CENTER);
        gameStatus.setBounds(25, 180, 400, 65);
        gameStatus.setOpaque(true); // Asi podemos poner background
        gameStatus.setForeground(Color.red);
        gameStatus.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panel.add(gameStatus); //agregamos la etiqueta al panel
        gameStatus.setOpaque(false);
        
        if (status) {
            gameStatus.setText("¡Victoria!");
            gameStatus.setForeground(Color.green);
        }
        else gameStatus.setText("Derrota");
        
        panel.add(gameStatus);
        
        panelSimular.revalidate();
        panelSimular.repaint();
        this.revalidate();
        this.repaint();
    }
    
    public void actualizarIconos(){
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (tablero.tablero[x][y].hayZombies()) {
                    for (Zombi zombi : tablero.tablero[x][y].getZombies())
                    actualizarIconoZombie(zombi.getX(), zombi.getY());
                }
                if (tablero.tablero[x][y].haySupervivientes()) actualizarIconoSuper(x, y);
            }
        } 
        panelJuego.revalidate();
        panelJuego.repaint();
    }
    
    public void actualizarIconosSim(){
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (tablero.tablero[x][y].hayZombies()) {
                    for (Zombi zombi : tablero.tablero[x][y].getZombies())
                    actualizarIconoZombie(zombi.getX(), zombi.getY());
                }
                if (tablero.tablero[x][y].haySupervivientes()) actualizarIconoSuper(x, y);
            }
        } 
        panelSimular.revalidate();
        panelSimular.repaint();
    }
    
}