
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
import java.util.List;

public class VentanaJuego extends JFrame{
    public JPanel panel, panelJuego, panelSimular, panelTablero;
    private JButton NuevaPartida, RetomarPartida, Salir, Atras, SalirGuardar, Simular;
    private int contadorTurnos; 
    private final int numZombies = 3;
    private JButton Moverse, Atacar, SiguienteTurno, Seleccionar, Buscar;
    private JLabel etiqueta1, etiqueta2, etiqueta3, etiquetaTurnos, etiquetaStatus;
    private JComboBox<String> listaArmas,listaArmas2, listaActivas;    
    public Set<Zombi> zombies;
    Set<Point> posicionesUsadas = new HashSet<>();
    Tablero tablero;
    private List<Superviviente> supervivientes;

    
    //private Armas armaSeleccionada; // Agregamos esta variable para el arma seleccionada

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
                    actualizarEtiquetaTurnos(contadorTurnos);
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
        contadorTurnos = 1;
        etiquetaTurnos = new JLabel("Turno: "+ contadorTurnos, SwingConstants.CENTER); // Creamos etiqueta
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
                
/*                Arma armaSeleccionada = getArmaSeleccionada();

                // Verifica que haya un arma seleccionada
                if (armaSeleccionada != null) {
                    atacarZombieSeleccionado(armaSeleccionada); 
                } else {
                    limpiarPanel();
                    panelJuego.revalidate();
                    panelJuego.repaint();
                    funcionAtacar();
                    System.out.println("Por favor, selecciona un arma antes de atacar.");
                }
*/
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
    
    private void colocarEtiquetasSimular(){
        JLabel etiqueta1 = new JLabel("Zombie Game", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(25, 30, 400, 65);
        etiqueta1.setForeground(Color.white);
        etiqueta1.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panelSimular.add(etiqueta1);
        etiqueta1.setOpaque(false);
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
        ImageIcon iconoSuperviviente = new ImageIcon(getClass().getResource("/resources/hombre.png"));
        supervivientes = Superviviente.crearSupervivientes();
        
        for (Superviviente superviviente : supervivientes) {
            posicionesUsadas.add(new Point(superviviente.getX(), superviviente.getY()));
            tablero.botonesTablero[superviviente.getX()][superviviente.getY()].setIcon(new ImageIcon(iconoSuperviviente.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
            JLabel etiquetaSuperviviente = new JLabel(iconoSuperviviente);
                etiquetaSuperviviente.setBounds(superviviente.getX() * 50, superviviente.getY() * 50, 50, 50);
                panelJuego.add(etiquetaSuperviviente);


        panelJuego.repaint(); 
    }
    }



    
    public void actualizarTurno() {
        contadorTurnos++;
        if (etiquetaTurnos != null) {
            actualizarEtiquetaTurnos(contadorTurnos);
        } else {
            System.out.println("Error: etiquetaTurnos no está inicializada.");
        }
    }

    public void actualizarEtiquetaTurnos(int turno) {
        if (etiquetaTurnos != null) {
            etiquetaTurnos.setText("Turno: " + turno);
            panelJuego.revalidate();
            panelJuego.repaint();
        }
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
                   System.out.println("Moviendose");
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
           }
        };
        Moverse.addActionListener(accionBoton4);
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
        // Para elegir una de las dos armas que puedan estar activas habra que hacer un if(inv.arma.getActiva.equals(true))
        //String [] opcionesArmas = {Inventario.getArmaActiva().getNombre(),arma2.getNombre(), arma3.getNombre()}; HAY QUE HACERLO ASI MAS O MENOS
        String [] opcionesArmas = {"ArmaActiva1", "ArmaActiva2"};
        listaActivas = new JComboBox(opcionesArmas);
        listaActivas.setBounds(30, 275, 100, 20);
        listaActivas.addItem(" ");
        listaActivas.setSelectedItem(" ");
        ActionListener accionLista = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String seleccion = (String) listaActivas.getSelectedItem();
                System.out.println(seleccion);
            }
        };
        listaActivas.addActionListener(accionLista);
        panelJuego.add(listaActivas); 
        // Atacar
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
                   System.out.println("Atacando");
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
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
                   System.out.println("Siguiente Turno");
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
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
                   System.out.println("Buscando");//Aqui hay q poner que el booleano en el inventario o arma sea true (armaActiva = true)
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
           }
        };
        Buscar.addActionListener(accionBoton4);
    }
    
    public void funcionElegirArma(){
        // Texto Elegir Arma:
        etiqueta1 = new JLabel("Elegir Armas activas:", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(28, 245, 120, 30);
        etiqueta1.setOpaque(true); // Asi podemos poner background
        etiqueta1.setForeground(Color.black);
        etiqueta1.setFont(new Font("arial", Font.CENTER_BASELINE, 12)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiqueta1);
        etiqueta1.setOpaque(false);
        // Aqui no se crean los supervivientes tiene q haber algo q pille al superviviente jugando
        Superviviente super1 = new Superviviente("Juan", 120, 12);
        // ---------------------------------------------------------------
        String [] opcionesArmas = super1.getInventario().obtenerNombres().toArray(new String[0]);
        listaArmas = new JComboBox(opcionesArmas);
        listaArmas.setBounds(30, 275, 50, 20);
        listaArmas.addItem(" ");
        listaArmas.setSelectedItem(" ");
        //Activa el booleano de ArmaActiva a true
        ActionListener accionLista1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) listaArmas.getSelectedItem();
                for (Equipo item : super1.getInventario().obtenerObjetos()) {
                    if (item instanceof Arma) {
                        Arma arma = (Arma) item;
                        if (arma.getNombre().equals(seleccion)) {
                            arma.setActiva(true);
                            System.out.println("Arma activada: " + arma.getNombre());
                        }
                    }
                }
            }
        };

        listaArmas.addActionListener(accionLista1);
        panelJuego.add(listaArmas); 
        // Segunda opccion activa
        listaArmas2 = new JComboBox(opcionesArmas);
        listaArmas2.setBounds(90, 275, 50, 20);
        listaArmas2.addItem("Ninguna");
        listaArmas2.setSelectedItem("Ninguna");
        ActionListener accionLista2 = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String seleccion = (String) listaArmas.getSelectedItem();
                if (!seleccion.equals("Niguna")){
                    for (Equipo item : super1.getInventario().obtenerObjetos()) {
                        if (item instanceof Arma) {
                            Arma arma = (Arma) item; // Convertir el item a Armas
                            if (arma.getNombre().equals(seleccion)) {
                                arma.setActiva(true);
                                System.out.println("Arma activada: " + arma.getNombre());
                            }
                        }
                    }
                }
            }
        };
        listaArmas2.addActionListener(accionLista2);
        panelJuego.add(listaArmas2);
        // Seleccionar
        Seleccionar = new JButton();
        Seleccionar.setBounds(35, 300, 100, 30);
        Seleccionar.setEnabled(true);
        Seleccionar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Seleccionar.png"));
        Seleccionar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 30, Image.SCALE_AREA_AVERAGING)));
        Seleccionar.setOpaque(false);
        panelJuego.add(Seleccionar);
        Seleccionar.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Armas Seleccionadas");//Aqui hay q poner que el booleano en el inventario o arma sea true (armaActiva = true)
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
           }
        };
        Seleccionar.addActionListener(accionBoton4);
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
    // Limpiar Elegir
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
    // Limpia Status            
        if (etiquetaStatus != null) {
            panelJuego.remove(etiquetaStatus);
        }
    // Actualizar la interfaz
    panelJuego.revalidate();
    panelJuego.repaint();
    }
    
    public void actualizarEtiquetaCoordenadas(int x, int y) {
        if (etiqueta3 != null) {
            etiqueta3.setText("X: " + x + "          Y: " + y);
        }
    }
    // 
    public Arma getArmaSeleccionada() {
        if (listaActivas != null) {
            return (Arma) listaActivas.getSelectedItem();
        }
        return null; // Si no hay ninguna arma seleccionada
    }
     
    public void atacarZombieSeleccionado(Arma armaSeleccionada) {
        int coordenadaXSeleccionada = tablero.getCoordenadaXSeleccionada();
        int coordenadaYSeleccionada = tablero.getCoordenadaYSeleccionada();

        if (coordenadaXSeleccionada != -1 && coordenadaYSeleccionada != -1) {
            Zombi zombieAtacado = buscarZombie(coordenadaXSeleccionada, coordenadaYSeleccionada);
            if (zombieAtacado != null) {
                // Aquí, pasa el arma seleccionada como parte del ataque.
                int distancia = 1;
                if(coordenadaXSeleccionada == zombieAtacado.getX() && coordenadaYSeleccionada == zombieAtacado.getY()){
                    distancia = 0;
                }
                //CALCULAR LA DISTANCIA

                int resultado = zombieAtacado.reaccionAtaques(armaSeleccionada, distancia );
                switch (resultado) {
                    case 0:
                        System.out.println("El zombie sigue vivo.");
                        break;
                    case 1:
                        System.out.println("¡Zombie eliminado!");
                        tablero.botonesTablero[coordenadaYSeleccionada][coordenadaXSeleccionada].setIcon(null);
                        zombies.remove(zombieAtacado);
                        break;
                    case 2:
                        System.out.println("¡Zombie eliminado, pero su sangre tóxica causó daño!");
                        tablero.botonesTablero[coordenadaYSeleccionada][coordenadaXSeleccionada].setIcon(null);
                        zombies.remove(zombieAtacado);
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
    
    private Superviviente buscarSuperviviente(List<Superviviente> supervivientes, int x, int y) {
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

        String contenido = "<html>";

        Casilla casilla = tablero.tablero[x][y];

        if (casilla.tieneZombie()) {
            Zombi zombi = buscarZombie(x, y);
            contenido += "Contiene: Zombi<br>Tipo: " + zombi.getTipo()
                        +"<br>Categoria: "+ zombi.getCategoria();
        }
        else if (casilla.tieneSuperviviente()) {
        Superviviente superviviente = buscarSuperviviente(supervivientes, x, y);
            contenido += "Contiene: Superviviente<br>Vida: " + superviviente.getSalud() +
                        "<br>Inventario: " + superviviente.getInventario();
        }
        else {
            contenido += "Casilla vacia.";
        }
        
        contenido += "</html>";
        
        if (etiquetaStatus == null) {
        etiquetaStatus = new JLabel(contenido, SwingConstants.CENTER);
        etiquetaStatus.setBounds(12, 230, 150, 110);
        etiquetaStatus.setForeground(Color.black);
        etiquetaStatus.setFont(new Font("Chiller", Font.BOLD, 17));
        etiquetaStatus.setOpaque(false);
        etiquetaStatus.setBackground(Color.white);

        panelJuego.add(etiquetaStatus);
    } else {
        etiquetaStatus.setText(contenido);
    }
        panelJuego.revalidate();
        panelJuego.repaint();    
    }
}