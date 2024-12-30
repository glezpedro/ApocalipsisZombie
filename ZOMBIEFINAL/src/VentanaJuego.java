
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class VentanaJuego extends JFrame{
    public JPanel panel, panelJuego, panelSimular, panelTablero;
    private JButton NuevaPartida, RetomarPartida, Salir, Atras, SalirGuardar, Simular;
    private int contadorTurnos;
    private final int numZombies = 3;
    private JButton Moverse, Atacar, SiguienteTurno, Seleccionar, Buscar;
    private JLabel etiqueta1, etiqueta2, etiqueta3, etiquetaTurnos;
    private JComboBox<String> listaArmas,listaArmas2, listaActivas;  
    private JTextArea etiquetaStatus;
    private JScrollPane scrollPanel;
    public Set<Zombi> zombies;
    Set<Point> posicionesUsadas = new HashSet<>();
    Tablero tablero;
    private List<Superviviente> supervivientes = new ArrayList<>();
    private int metaX;
    private int metaY;
    private int indiceActual = 3;
    public int accionesTotales = 0;
    private final VentanaJuego ventana;
    private Arma armaSeleccionada; // Agregamos esta variable para el arma seleccionada

    public VentanaJuego(){
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

        this.revalidate();
        this.repaint();  
        
        colocarEtiquetasSimular();
        colocarBotonesSimular();
        tablero.colocarTablero2();
        colocarRadioBotonesSim();
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
                    // leer el archvo guardado
                    FileInputStream fileIn = new FileInputStream("partidaGuardada.dat");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    EstadoJuego estado = (EstadoJuego) in.readObject();
                    in.close();
                    fileIn.close();

                    // rrestaurar  datos guardados
                    contadorTurnos = estado.getTurno();
                    zombies = estado.getZombies();
                    tablero.coordenadaXSeleccionada = estado.getCoordenadaX();
                    tablero.coordenadaYSeleccionada = estado.getCoordenadaY();

                    // actualizar  interfaz
                    colocarPanelJuego();
                    for (Zombi zombie : zombies) {
                        tablero.botonesTablero[zombie.getX()][zombie.getY()].setIcon(new ImageIcon(getClass().getResource("/resources/zombiN.png")));
                    }
                    System.out.println("Partida cargada correctamente.");
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
            // Crear el objeto EstadoJuego con los datos actuales
                    EstadoJuego estado = new EstadoJuego(contadorTurnos, zombies, tablero.coordenadaXSeleccionada, tablero.coordenadaYSeleccionada);
                    FileOutputStream fileOut = new FileOutputStream("partidaGuardada.dat");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(estado);
                    out.close();
                    fileOut.close();
                    
                    System.out.println("Partida guardada correctamente.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error al guardar la partida.");
                }
                System.exit(0); // Salir del juego

               }
       };
       SalirGuardar.addActionListener(accionBoton5);
    }
    
    private void colocarRadioBotones(){
       JRadioButton radioBoton1 = new JRadioButton("Moverse", false); // El booleano es para saber si aparece seleccionado o no
       radioBoton1.setBounds(30, 140, 150, 15);
       radioBoton1.setOpaque(false);
       ActionListener accionMoverse = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               //FALTA PONER QUE SE MUEVA
               limpiarPanel();
               panelJuego.revalidate(); //para que se muestre en pantalla
               panelJuego.repaint();
               funcionMoverse();
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
                // Limpia el panel de opciones
                limpiarPanel();
                panelJuego.revalidate();
                panelJuego.repaint();
                funcionAtacar();
                
                Arma armaSeleccionada = getArmaSeleccionada();

                if (armaSeleccionada != null) {
                    atacarZombieSeleccionado(armaSeleccionada); 
                } else {
                    limpiarPanel();
                    panelJuego.revalidate();
                    panelJuego.repaint();
                    funcionAtacar();
                    System.out.println("Por favor, selecciona un arma antes de atacar.");
                }

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
               //FALTA PONER QUE SEA TURNO DE ZOMBI
               limpiarPanel();
               panelJuego.revalidate(); //para que se muestre en pantalla
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
               //FALTA PONER QUE SEA TURNO DE ZOMBI
               limpiarPanel();
               panelJuego.revalidate(); //para que se muestre en pantalla
               panelJuego.repaint();
               funcionBuscar();
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
               //FALTA PONER QUE SEA TURNO DE ZOMBI
               limpiarPanel();
               panelJuego.revalidate(); //para que se muestre en pantalla
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
       JRadioButton radioBoton10 = new JRadioButton("Añadir Zombi", false); // El booleano es para saber si aparece seleccionado o no
       radioBoton10.setBounds(20, 140, 150, 15);
       radioBoton10.setOpaque(false);
       ActionListener accionAñadirZombi = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               limpiarPanel();
               panelSimular.revalidate(); //para que se muestre en pantalla
               panelSimular.repaint();
           }
       };
       panelSimular.add(radioBoton10);
       JRadioButton radioBoton11 = new JRadioButton("Añadir Superviviente", false);
       radioBoton11.setBounds(20, 160, 130, 15);
       radioBoton11.setOpaque(false);
       ActionListener accionAñadirSuperviviente = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpia el panel de opciones
                limpiarPanel();
                panelSimular.revalidate();
                panelSimular.repaint();
            }
       };
       panelSimular.add(radioBoton11);
       JRadioButton radioBoton12 = new JRadioButton("Atacar", false);
       radioBoton12.setBounds(20, 180, 150, 15);
       radioBoton12.setOpaque(false);
       ActionListener accionAtacar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpia el panel de opciones
                limpiarPanel();
                panelSimular.revalidate();
                panelSimular.repaint();
                funcionAtacar2();
                    }
        };
        radioBoton12.addActionListener(accionAtacar);
        panelSimular.add(radioBoton12);
        
        ButtonGroup grupoRadioBotonesSim = new ButtonGroup();
       grupoRadioBotonesSim.add(radioBoton10);
       grupoRadioBotonesSim.add(radioBoton11);
       grupoRadioBotonesSim.add(radioBoton12);
     }
    
    private void colocarEtiquetasSimular(){
        JLabel etiqueta1 = new JLabel("Zombie Game", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(25, 30, 400, 65);
        etiqueta1.setForeground(Color.white);
        etiqueta1.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta1);
        etiqueta1.setOpaque(false);
        JLabel etiqueta2 = new JLabel("Simulación", SwingConstants.CENTER);
        etiqueta2.setBounds(35, 105, 100, 35);
        etiqueta2.setForeground(Color.black);
        etiqueta2.setFont(new Font("chiller", Font.BOLD, 31)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta2);
        etiqueta2.setOpaque(false);
        
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
       panelSimular.add(Atras);
       Atras.setBorderPainted(false);

       ActionListener accionBoton4 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               colocarPanelMain();
           }
       };
       Atras.addActionListener(accionBoton4);
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

    public void actualizarTurno() {
        System.out.println("Iniciando un nuevo turno...");
        
        accionesTotales = 0;
        
        for (Superviviente s : supervivientes) {
            s.resetearAcciones();
        }

        indiceActual = 3;

        panelJuego.revalidate();
        panelJuego.repaint();
        System.out.println("Turno reiniciado. Es el turno de " + supervivientes.get(indiceActual).getNombre());
        etiquetaTurnos.setText("Turno " + supervivientes.get(indiceActual).getNombre());
    }
    
    public void funcionMoverse(){
        // Texto Elegir Casila:
        etiqueta2 = new JLabel("Elegir Casilla:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta2.setBounds(10, 245, 150, 30);
        etiqueta2.setOpaque(true); // Asi podemos poner background
        etiqueta2.setForeground(Color.black);
        etiqueta2.setFont(new Font("arial", Font.BOLD, 13)); // estableze el font se puede usar 0123 para typo de letra
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
            
            if (supervivienteActual.gastarAccion() && movimientoValido == true) {
                tablero.tablero[viejaXY[0]][viejaXY[1]].eliminarSuperviviente(supervivienteActual);
                tablero.tablero[nuevaX][nuevaY].agregarSuperviviente(supervivienteActual);
                supervivienteActual.moverse(nuevaX, nuevaY);

                actualizarIconos(viejaXY[0], viejaXY[1]);
                actualizarIconos(nuevaX, nuevaY);
                tablero.tablero[viejaXY[0]][viejaXY[1]].setHaySuperviviente(false);
                tablero.tablero[nuevaX][nuevaY].setHaySuperviviente(true);
                
                
                System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                accionesTotales++;
            } else {
                System.out.println("Movimiento no válido o acciones agotadas.");
                actualizarEtiqueta("Movimiento no válido o acciones agotadas.");
            }
            
            if(accionesTotales == 12){
                actualizarTurno();
                System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                accionarZombies();
                colocarZombieFinDeRonda();
            }

            if (supervivienteActual.getAccionesDisponibles() == 0) {
                indiceActual--;
                if (indiceActual >= supervivientes.size()) {
                    indiceActual = 3;
                }
                Superviviente siguienteSuperviviente = supervivientes.get(indiceActual);
                siguienteSuperviviente.resetearAcciones();
                System.out.println("Es el turno de " + siguienteSuperviviente.getNombre());
                etiquetaTurnos.setText("Turno " + siguienteSuperviviente.getNombre());
                panelJuego.revalidate();
                panelJuego.repaint();
            }

            
        }};
        Moverse.addActionListener(accionBoton4);
    }
    
    private void actualizarIconos(int x, int y) {
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
        listaActivas.addItem(" ");
        listaActivas.setSelectedItem(" ");
        ActionListener accionLista = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                armaSeleccionada1 = (String) listaActivas.getSelectedItem();
                System.out.println("Arma seleccionada: " + armaSeleccionada1);
            }
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

        ActionListener accionBoton4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ataque ataque = new Ataque(armaSeleccionada1,supervivienteActual,x,y, ventana);
                Arma armaSeleccionada = supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);
                if (armaSeleccionada1 == null || armaSeleccionada1.trim().isEmpty()) {
                            System.out.println("No se seleccionó un arma válida.");
                            return; // Salir del método si no hay selección válid
                        }
                if (supervivienteActual.gastarAccion()) {
                        
                        atacarZombieSeleccionado(armaSeleccionada);
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Movimiento no válido o acciones agotadas.");
                    }
                
                if (accionesTotales == 12) {
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                        accionarZombies();
                        colocarZombieFinDeRonda();
                        actualizarTurno(); 
                    }

                if (supervivienteActual.getAccionesDisponibles() == 0) {
                    indiceActual--;
                    if (indiceActual >= supervivientes.size()) {
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
        };
        Atacar.addActionListener(accionBoton4);
    }

    public int calcularDistancia(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    
    public void funcionAtacar2(){
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
        listaActivas.addItem(" ");
        listaActivas.setSelectedItem(" ");
        ActionListener accionLista = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                armaSeleccionada2 = (String) listaActivas.getSelectedItem();
                System.out.println("Arma seleccionada: " + armaSeleccionada2);
            }
        };
        listaActivas.addActionListener(accionLista);
        panelSimular.add(listaActivas); 
        // Atacar
        Atacar = new JButton();
        Atacar.setBounds(55, 300, 62, 26);
        Atacar.setEnabled(true);
        Atacar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Atacar.png"));
        Atacar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(62, 26, Image.SCALE_AREA_AVERAGING)));
        Atacar.setOpaque(false);
        panelSimular.add(Atacar);
        Atacar.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                Ataque ataque = new Ataque(armaSeleccionada2,supervivienteActual,x,y, ventana);
                Arma armaSeleccionada = supervivienteActual.getInventario().obtenerArmaPorNombre(armaSeleccionada1);
                if (armaSeleccionada2 == null || armaSeleccionada2.trim().isEmpty()) {
                            System.out.println("No se seleccionó un arma válida.");
                            return; 
                        }
                if (supervivienteActual.gastarAccion()) {
                        
                        atacarZombieSeleccionado(armaSeleccionada);
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Movimiento no válido o acciones agotadas.");
                    }
                
                if (accionesTotales == 12) {
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                        accionarZombies();
                        colocarZombieFinDeRonda();
                        actualizarTurno(); 
                    }

                if (supervivienteActual.getAccionesDisponibles() == 0) {
                    indiceActual--;
                    if (indiceActual >= supervivientes.size()) {
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
        };
        Atacar.addActionListener(accionBoton4);
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
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                        accionarZombies();
                        colocarZombieFinDeRonda();
                        actualizarTurno(); 
                    }

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual >= supervivientes.size()) {
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
                   buscar();
                   
                   Superviviente supervivienteActual = supervivientes.get(indiceActual);
                   
                   if (supervivienteActual.gastarAccion()) {
                        accionesTotales++;
                        System.out.println("Le quedan a " + supervivienteActual.getNombre() + " " + supervivienteActual.getAccionesDisponibles() + " acciones.");
                    } else {
                        System.out.println("Acción no valida o acciones agotadas.");
                    }
                   
                    if (accionesTotales == 12) {
                        System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                        accionarZombies();
                        colocarZombieFinDeRonda();
                        actualizarTurno(); 
                    } 

                    if (supervivienteActual.getAccionesDisponibles() == 0) {
                        indiceActual--;
                        if (indiceActual >= supervivientes.size()) {
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
        };
        Buscar.addActionListener(accionBoton4);
    }
    
    private String armaSeleccionada1 = "";
    private String armaSeleccionada2 = "";

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
        Superviviente superviviente = buscarSuperviviente(x, y);

        // ---------------------------------------------------------------
        if (superviviente != null) {
            if (!superviviente.getInventario().obtenerObjetos().isEmpty()) {
                // Obtener las opciones de armas para el JComboBox
                String[] opcionesArmas = superviviente.getInventario().obtenerNombresArmasNA().toArray(new String[0]);

                // JComboBox 1: Armas activas
                listaArmas = new JComboBox(opcionesArmas);
                listaArmas.setBounds(30, 275, 50, 20);
                listaArmas.addItem(" "); // Añadir un espacio para la opción "ninguna"
                listaArmas.setSelectedItem(" ");
                panelJuego.add(listaArmas);

                // JComboBox 2: Segunda opción para elegir arma
                listaArmas2 = new JComboBox(opcionesArmas);
                listaArmas2.setBounds(90, 275, 50, 20);
                listaArmas2.addItem("Ninguna"); // Añadir la opción "Ninguna"
                listaArmas2.setSelectedItem("Ninguna");
                panelJuego.add(listaArmas2);

                // Botón Seleccionar
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
                            listaArmas2.removeItem(armaSeleccionada1);// Limpiar ese elemento
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
                            System.out.println("Moviendo Zombis y colocando nuevo Zombie"); 
                            accionarZombies();
                            colocarZombieFinDeRonda();
                            actualizarTurno(); 
                        }
                        
                        if (supervivienteActual.getAccionesDisponibles() == 0) {
                            indiceActual--;
                            if (indiceActual >= supervivientes.size()) {
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
        int x = tablero.getCoordenadaXSeleccionada();
        int y = tablero.getCoordenadaYSeleccionada();

        Superviviente superviviente = buscarSuperviviente(x, y);
        
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
    panelJuego.revalidate();
    panelJuego.repaint();
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
     
    public void atacarZombieSeleccionado(Arma armaSeleccionada) {
        int coordenadaXSeleccionada = tablero.getCoordenadaXSeleccionada();
        int coordenadaYSeleccionada = tablero.getCoordenadaYSeleccionada();

        Superviviente supervivienteActual = supervivientes.get(indiceActual);
        
        if (coordenadaXSeleccionada != -1 && coordenadaYSeleccionada != -1) {
            Zombi zombieAtacado = buscarZombie(coordenadaXSeleccionada, coordenadaYSeleccionada);
            if (zombieAtacado != null) {
                
                int distancia = calcularDistancia(coordenadaXSeleccionada, coordenadaYSeleccionada, zombieAtacado.getX(), zombieAtacado.getY());

                int resultado = zombieAtacado.reaccionAtaques(armaSeleccionada, distancia );
                switch (resultado) {
                    case 0:
                        System.out.println("El zombie sigue vivo.");
                        actualizarEtiqueta("El zombie sigue vivo.");
                        break;
                    case 1:
                        System.out.println("¡Zombie eliminado!");
                        actualizarEtiqueta("¡Zombie eliminado!");
                        tablero.botonesTablero[coordenadaYSeleccionada][coordenadaXSeleccionada].setIcon(null);
                        tablero.tablero[coordenadaXSeleccionada][coordenadaYSeleccionada].setHayZombie(false);
                        tablero.tablero[coordenadaXSeleccionada][coordenadaYSeleccionada].eliminarZombie(zombieAtacado);
                        zombies.remove(zombieAtacado);
                        panelJuego.revalidate();
                        panelJuego.repaint();
                        break;
                    case 2:
                        System.out.println("¡Zombie eliminado, pero su sangre tóxica causó daño!");
                        actualizarEtiqueta("¡Zombie eliminado, pero su sangre tóxica causó daño!");
                        if(supervivienteActual.getX() == coordenadaXSeleccionada && supervivienteActual.getY() == coordenadaYSeleccionada){
                            supervivienteActual.envenenar();
                            supervivienteActual.aplicarEfectos();
                        }
                        tablero.botonesTablero[coordenadaYSeleccionada][coordenadaXSeleccionada].setIcon(null);
                        tablero.tablero[coordenadaXSeleccionada][coordenadaYSeleccionada].setHayZombie(false);
                        tablero.tablero[coordenadaXSeleccionada][coordenadaYSeleccionada].eliminarZombie(zombieAtacado);
                        zombies.remove(zombieAtacado);
                        panelJuego.revalidate();
                        panelJuego.repaint();
                        break;
                }
            } else {
                System.out.println("No hay un zombie en esa casilla.");
            }
        } else {
            System.out.println("Por favor, selecciona una casilla antes de atacar.");
        }

        panelJuego.revalidate();
        panelJuego.repaint();
    }

    private Zombi buscarZombie(int x, int y) {
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
            if (casilla.tieneZombie()) contenido += "\n\n"; 
                Superviviente superviviente = buscarSuperviviente(x, y); 
                contenido += "Contiene: " + superviviente.getNombre() + "\nVida: " + superviviente.getSalud() +
                     "\nInventario: " + superviviente.getInventario().obtenerNombres();
        }

        if (!casilla.tieneZombie() && !casilla.tieneSuperviviente() && !esMeta) {
            contenido = "Casilla Vacia.";
        }
        actualizarEtiqueta(contenido);  
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
    
    public void accionarZombies() {
        for (Zombi zombi : zombies) {
            for (int i = 0; i < zombi.getActivaciones(); i++) {
                int x = zombi.getX();
                int y = zombi.getY();

                Casilla casilla = tablero.tablero[x][y];
                List<Superviviente> supervivientesEnCasilla = casilla.getSupervivientes();

                // mira si hay supervivientes en la casilla actual
                if (!supervivientesEnCasilla.isEmpty() && zombi.getCategoria().equals("TOXICO")) {
                    Superviviente primerSuperviviente = supervivientesEnCasilla.get(0);
                    primerSuperviviente.envenenar();
                    primerSuperviviente.recibirHerida();

                    System.out.println("¡El zombi mordió a " + primerSuperviviente.getNombre() + "!");
                    if (primerSuperviviente.getSalud() == 0) {
                        tablero.tablero[x][y].eliminarSuperviviente(primerSuperviviente);
                        tablero.tablero[x][y].setHaySuperviviente(false);
                        tablero.botonesTablero[x][y].setIcon(null);
                        finJuego();
                    }
                }

                // busca el superviviente cercano
                Superviviente objetivo = encontrarSupervivienteCercano(x, y);
                if (objetivo != null) {
                    int xObjetivo = objetivo.getX();
                    int yObjetivo = objetivo.getY();

                    // calcula la posición del zombi
                    int nuevaX = x + Integer.signum(xObjetivo - x);
                    int nuevaY = y + Integer.signum(yObjetivo - y);

                    // mueve al zombi si la posición es válida
                    if (tablero.esPosicionValida(nuevaX, nuevaY)) {
                        tablero.tablero[nuevaX][nuevaY].agregarZombie(zombi);
                        tablero.tablero[nuevaX][nuevaY].setHayZombie(true);

                        actualizarIconosZombie(nuevaX, nuevaY, zombi);

                        tablero.tablero[x][y].eliminarZombie(zombi);
                        tablero.tablero[x][y].setHayZombie(false);
                        tablero.botonesTablero[x][y].setIcon(null);

                        zombi.moverse(nuevaX, nuevaY);

                        System.out.println("Zombi movido de (" + x + ", " + y + ") a (" + nuevaX + ", " + nuevaY + ")");
                    }
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            panelJuego.revalidate();
            panelJuego.repaint();
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
    
    private void actualizarIconosZombie(int x, int y, Zombi zombi) {
        Casilla casilla = tablero.tablero[x][y];
        
        
        ImageIcon IconoZombiN = new ImageIcon(getClass().getResource("/resources/zombiN.png"));
        ImageIcon IconoZombiNA = new ImageIcon(getClass().getResource("/resources/zombiNA.png"));
        ImageIcon IconoZombiNC = new ImageIcon(getClass().getResource("/resources/zombiNC.png"));
        ImageIcon IconoZombiT = new ImageIcon(getClass().getResource("/resources/zombiT.png"));
        ImageIcon IconoZombiTA = new ImageIcon(getClass().getResource("/resources/zombiTA.png"));
        ImageIcon IconoZombiTC = new ImageIcon(getClass().getResource("/resources/zombiTC.png"));
        ImageIcon IconoZombiB = new ImageIcon(getClass().getResource("/resources/zombiB.png"));
        ImageIcon IconoZombiBA = new ImageIcon(getClass().getResource("/resources/zombiBA.png"));
        ImageIcon IconoZombiBC = new ImageIcon(getClass().getResource("/resources/zombiBC.png"));
        
        

        if (casilla.tieneZombie() && !casilla.getZombies().isEmpty()) {
            

            if (zombi != null) {
                if (zombi.getCategoria().equals("NORMAL") && zombi.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiN.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("TOXICO") && zombi.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiT.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("BERSERKER") && zombi.getTipo() == TipoZombie.CAMINANTE) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiB.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("NORMAL") && zombi.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiNC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("TOXICO") && zombi.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiTC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("BERSERKER") && zombi.getTipo() == TipoZombie.CORREDOR) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiBC.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("NORMAL") && zombi.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiNA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("TOXICO") && zombi.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiTA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
                if (zombi.getCategoria().equals("BERSERKER") && zombi.getTipo() == TipoZombie.ABOMINACION) tablero.botonesTablero[x][y].setIcon(new ImageIcon(IconoZombiBA.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
            }
        }
        tablero.botonesTablero[x][y].revalidate();
        tablero.botonesTablero[x][y].repaint();
        tablero.tablero[x][y].setHayZombie(true); 
        panelJuego.revalidate();
        panelJuego.repaint();

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
}