import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/*
boton1 --> botonNuevaPartida
boton2 --> botonRetomarPartida
boton3 --> botonSalir
boton4 --> botonAtras
boton5 --> botonGuardarSalir

boton6 --> botonMoverse
boton7 --> botonAtacar
boton8 --> botonQuedarse
boton9 --> botonSeleccionar
boton10 --> botonBuscar

radioBoton1 --> radioBotonMoverse
radioBoton2 --> radioBotonAtacar
radioBoton3 --> radioBotonQuedarse
radioBoton4 --> radioBotonBuscar
radioBoton5 --> radioBotonElegirArma


etiqueta1 --> etiquetaElegirArma
etiqueta2 --> etiquetaElegirCasilla
etiqueta3 --> etiquetaCoordenadaXY


*/




public class VentanaJuego extends JFrame {
    private JPanel panel, panelJuego, panelRetomar, panelTablero;
    private JButton botonNuevaPartida, botonRetomarPartida, botonSalir, botonAtras, botonGuardarSalir;
    private JButton botonMoverse, botonAtacar, botonQuedarse, botonSeleccionar, botonBuscar;
    private JLabel etiqueta1, etiquetaElegirArma, etiquetaTurnos, etiquetaElegirCasilla, etiquetaCoordenadaXY;
    private JComboBox<String> listaArmas, listaArmas2, listaActivas;
    private JButton[][] botones;
    private final int filasColumnas = 10;
    private final int numZombies = 3;
    private int contadorTurnos = 0;
    private int coordenadaXSeleccionada = 0;
    private int coordenadaYSeleccionada = 0;
    private Set<Zombi> zombies;

    private Image backgroundImage;

    public VentanaJuego() {
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Zombie Game");
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/zombie2.png"));
        setIconImage(icon.getImage());

        zombies = new HashSet<>();
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

    private void colocarEtiquetasMain() {
        JLabel etiqueta = new JLabel("Zombie Game", SwingConstants.CENTER);
        etiqueta.setBounds(25, 30, 400, 65);
        etiqueta.setForeground(Color.white);
        etiqueta.setFont(new Font("chiller", Font.BOLD, 80));
        etiqueta.setOpaque(false);
        panel.add(etiqueta);
    }

    private void colocarBotonesMain() {
        //BOTON NUEVA PARTIDA
        botonNuevaPartida = new JButton();
        botonNuevaPartida.setBounds(125, 150, 200, 40);
        botonNuevaPartida.setBackground(Color.cyan);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/NuevaPartida.png"));
        botonNuevaPartida.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        botonNuevaPartida.setOpaque(false);
        botonNuevaPartida.setBorderPainted(false);
        botonNuevaPartida.addActionListener(e -> colocarPanelJuego());
        panel.add(botonNuevaPartida);

        //BOTON RETORMAR PARTIDA
        botonRetomarPartida = new JButton();
        botonRetomarPartida.setBounds(125, 200, 200, 40);
        botonRetomarPartida.setBackground(Color.green);
        ImageIcon imagen2 = new ImageIcon(getClass().getResource("/resources/RetomarPartida.png"));
        botonRetomarPartida.setIcon(new ImageIcon(imagen2.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        botonRetomarPartida.setOpaque(false);
        botonRetomarPartida.setBorderPainted(false);
        botonRetomarPartida.addActionListener(e -> retomarPartida());
        panel.add(botonRetomarPartida);
        ActionListener botonRetomarPartida = new ActionListener() {
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
                    coordenadaXSeleccionada = estado.getCoordenadaX();
                    coordenadaYSeleccionada = estado.getCoordenadaY();

                    // actualizar  interfaz
                    colocarPanelJuego();
                    actualizarEtiquetaTurnos(contadorTurnos);
                    for (Zombi zombie : zombies) {
                        botones[zombie.getX()][zombie.getY()].setIcon(new ImageIcon(getClass().getResource("/resources/zombie2.png")));
                    }
                    System.out.println("Partida cargada correctamente.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error al cargar la partida.");
                }
            }
        };

        botonSalir = new JButton();
        botonSalir.setBounds(125, 250, 200, 40);
        botonSalir.setBackground(Color.red);
        ImageIcon imagen3 = new ImageIcon(getClass().getResource("/resources/Salir.png"));
        botonSalir.setIcon(new ImageIcon(imagen3.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        botonSalir.setOpaque(false);
        botonSalir.setBorderPainted(false);
        botonSalir.addActionListener(e -> System.exit(0));
        panel.add(botonSalir);
    }

    private void colocarPanelJuego() {
        panelJuego = new BackgroundPanel("/resources/BackJuego.png");
        panelJuego.setLayout(null);
        this.getContentPane().remove(panel);
        this.getContentPane().add(panelJuego);

        this.revalidate();
        this.repaint();

        colocarEtiquetasJuego();
        colocarBotonesJuego();
        colocarTablero();
        colocarRadioBotones();
    }

    private void colocarEtiquetasJuego() {
        JLabel etiqueta = new JLabel("Zombie Game", SwingConstants.CENTER);
        etiqueta.setBounds(25, 30, 400, 65);
        etiqueta.setForeground(Color.white);
        etiqueta.setFont(new Font("chiller", Font.BOLD, 80));
        etiqueta.setOpaque(false);
        panelJuego.add(etiqueta);

        contadorTurnos = 1;
        etiquetaTurnos = new JLabel("Turno: " + contadorTurnos, SwingConstants.CENTER);
        etiquetaTurnos.setBounds(30, 105, 100, 35);
        etiquetaTurnos.setForeground(Color.black);
        etiquetaTurnos.setFont(new Font("chiller", Font.BOLD, 25));
        etiquetaTurnos.setOpaque(false);
        panelJuego.add(etiquetaTurnos);
    }

    private void colocarBotonesJuego() {
        botonAtras = new JButton();
        botonAtras.setBounds(20, 360, 40, 40);
        botonAtras.setBackground(Color.red);
        ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
        botonAtras.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        botonAtras.setOpaque(false);
        botonAtras.setBorderPainted(false);
        botonAtras.addActionListener(e -> colocarPanelMain());
        panelJuego.add(botonAtras);

        botonGuardarSalir = new JButton();
        botonGuardarSalir.setBounds(70, 360, 200, 40);
        botonGuardarSalir.setBackground(Color.blue);
        ImageIcon imagen5 = new ImageIcon(getClass().getResource("/resources/SalirGuardar.png"));
        botonGuardarSalir.setIcon(new ImageIcon(imagen5.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        botonGuardarSalir.setOpaque(false);
        botonGuardarSalir.setBorderPainted(false);
        botonGuardarSalir.addActionListener(e -> guardarYSalir());
        panelJuego.add(botonGuardarSalir);
    }

    private void colocarRadioBotones() {
        JRadioButton radioBotonMoverse = new JRadioButton("Moverse", false);
        radioBotonMoverse.setBounds(30, 140, 150, 15);
        radioBotonMoverse.setOpaque(false);
        panelJuego.add(radioBotonMoverse);

        JRadioButton radioBotonAtacar = new JRadioButton("Atacar", false);
        radioBotonAtacar.setBounds(30, 160, 150, 15);
        radioBotonAtacar.setOpaque(false);
        panelJuego.add(radioBotonAtacar);

        JRadioButton radioBotonQuedarse = new JRadioButton("Quedarse", false);
        radioBotonQuedarse.setBounds(30, 180, 150, 15);
        radioBotonQuedarse.setOpaque(false);
        panelJuego.add(radioBotonQuedarse);

        JRadioButton radioBotonBuscar = new JRadioButton("Buscar", false);
        radioBotonBuscar.setBounds(30, 200, 150, 15);
        radioBotonBuscar.setOpaque(false);
        panelJuego.add(radioBotonBuscar);

        JRadioButton radioBotonElegirArma = new JRadioButton("Elegir arma", false);
        radioBotonElegirArma.setBounds(30, 220, 150, 15);
        radioBotonElegirArma.setOpaque(false);
        panelJuego.add(radioBotonElegirArma);

        ButtonGroup grupoRadioBotones = new ButtonGroup();
        grupoRadioBotones.add(radioBotonMoverse);
        grupoRadioBotones.add(radioBotonAtacar);
        grupoRadioBotones.add(radioBotonQuedarse);
        grupoRadioBotones.add(radioBotonBuscar);
        grupoRadioBotones.add(radioBotonElegirArma);
    }

    private void colocarTablero() {
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(10, 10));
        panelTablero.setBounds(190, 115, 210, 210);
        panelJuego.add(panelTablero);

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        botones = new JButton[filasColumnas][filasColumnas];

        for (int i = 0; i < filasColumnas; i++) {
            for (int j = 0; j < filasColumnas; j++) {
                JButton button = new JButton();
                button.setBorder(border);
                button.setBackground(Color.red);
                button.setOpaque(false);
                botones[i][j] = button;

                final int fila = i;
                final int columna = j;

                button.addActionListener(e -> {
                    coordenadaXSeleccionada = fila;
                    coordenadaYSeleccionada = columna;
                    actualizarEtiquetaCoordenadas(fila, columna);
                });
                panelTablero.add(button);
            }
        }
        colocarZombiesInicio();
    }

    private void colocarPanelRetomar() {

        panelRetomar = new BackgroundPanel("/resources/BackJuego.png");
        panelRetomar.setLayout(null);
        panelRetomar.setBackground(Color.green);
        panelRetomar.setBounds(0, 0, 450, 450);
        
        this.getContentPane().remove(panel); 
        this.getContentPane().add(panelRetomar);

        this.revalidate();
        this.repaint();  
        
        colocarEtiquetasRetomar();
        colocarBotonesRetomar();
    }
    
    private void colocarEtiquetasRetomar(){
        JLabel etiqueta1 = new JLabel("Zombie Game", SwingConstants.CENTER); // Creamos etiqueta
        etiqueta1.setBounds(25, 30, 400, 65);
        etiqueta1.setOpaque(true); // Asi podemos poner background
        etiqueta1.setForeground(Color.white);
        etiqueta1.setFont(new Font("chiller", Font.BOLD, 80)); // estableze el font se puede usar 0123 para typo de letra
        panelRetomar.add(etiqueta1);
        etiqueta1.setOpaque(false);
    }    
    
    private void colocarBotonesRetomar(){
       // Atras
       botonAtras = new JButton();
       botonAtras.setBounds(20, 360, 40, 40);
       botonAtras.setEnabled(true);
       botonAtras.setBackground(Color.red);
       ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
       botonAtras.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       botonAtras.setOpaque(false);
       panelRetomar.add(botonAtras);
       botonAtras.setBorderPainted(false);

       ActionListener accionBoton4 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               colocarPanelMain();
           }
       };
       botonAtras.addActionListener(accionBoton4);
       // Salir Y Guardar
       botonGuardarSalir = new JButton();
       botonGuardarSalir.setBounds(70, 360, 200, 40);
       botonGuardarSalir.setEnabled(true);
       botonGuardarSalir.setBackground(Color.blue);
       ImageIcon imagen5 = new ImageIcon(getClass().getResource("/resources/SalirGuardar.png"));
       botonGuardarSalir.setIcon(new ImageIcon(imagen5.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
       botonGuardarSalir.setOpaque(false);
       panelJuego.add(botonGuardarSalir);
       botonGuardarSalir.setBorderPainted(false);

       ActionListener accionBoton5 = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               try {
            // Crear el objeto EstadoJuego con los datos actuales
                    EstadoJuego estado = new EstadoJuego(contadorTurnos, zombies, coordenadaXSeleccionada, coordenadaYSeleccionada);
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
       botonGuardarSalir.addActionListener(accionBoton5); 
       
    }

    private void actualizarEtiquetaCoordenadas(int x, int y) {
        if (etiquetaCoordenadaXY != null) {
            etiquetaCoordenadaXY.setText("X: " + x + "          Y: " + y);
        }
    }

    private void guardarYSalir() {/*
        try {
            EstadoJuego estado = new EstadoJuego(contadorTurnos, zombies, coordenadaXSeleccionada, coordenadaYSeleccionada);
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
        System.exit(0);*/
    }

    private void retomarPartida() {/*
        try {
            FileInputStream fileIn = new FileInputStream("partidaGuardada.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            EstadoJuego estado = (EstadoJuego) in.readObject();
            in.close();
            fileIn.close();

            contadorTurnos = estado.getTurno();
            zombies = estado.getZombies();
            coordenadaXSeleccionada = estado.getCoordenadaX();
            coordenadaYSeleccionada = estado.getCoordenadaY();

            colocarPanelJuego();
            actualizarEtiquetaTurnos(contadorTurnos);
            for (Zombie zombie : zombies) {
                botones[zombie.getX()][zombie.getY()]
                        .setIcon(new ImageIcon(getClass().getResource("/resources/zombie2.png")));
            }
            System.out.println("Partida cargada correctamente.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error al cargar la partida.");
        }*/
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

    private class BackgroundPanel extends JPanel {
        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
    
    public void colocarZombiesInicio(){
        Random rand = new Random();
        Set<Point> posicionesUsadas = new HashSet<>();
        ImageIcon IconoZombi = new ImageIcon(getClass().getResource("/resources/zombie2.png"));
        
        for (int i = 0; i < numZombies; i++) {
                        
            Zombi nuevoZombie;
            do{
                 nuevoZombie = Zombi.crearZombiAleatorio();
            } while (posicionesUsadas.contains(new Point(nuevoZombie.getX(), nuevoZombie.getY()))); 
                    
            zombies.add(nuevoZombie);
            posicionesUsadas.add(new Point(nuevoZombie.getX(), nuevoZombie.getY()));
            
            
            botones[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombi.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        }
    }
    
    public void colocarZombieFinDeRonda(){
        Random rand = new Random();
        Set<Point> posicionesUsadas = new HashSet<>();
        ImageIcon IconoZombi = new ImageIcon(getClass().getResource("/resources/zombie2.png"));
               
        Zombi nuevoZombie;
        do{
            nuevoZombie = Zombi.crearZombiAleatorio();
        } while (posicionesUsadas.contains(new Point(nuevoZombie.getX(), nuevoZombie.getY()))); 
                    
        zombies.add(nuevoZombie);
        posicionesUsadas.add(new Point(nuevoZombie.getX(), nuevoZombie.getY()));
            
            
        botones[nuevoZombie.getX()][nuevoZombie.getY()].setIcon(new ImageIcon(IconoZombi.getImage().getScaledInstance(20,20,Image.SCALE_AREA_AVERAGING)));
        
    }
    
    public void funcionMoverse() {
        // Texto Elegir Casilla:
        etiquetaElegirCasilla = new JLabel("Elegir Casilla:", SwingConstants.CENTER);
        etiquetaElegirCasilla.setBounds(10, 245, 150, 30);
        etiquetaElegirCasilla.setOpaque(true);
        etiquetaElegirCasilla.setForeground(Color.black);
        etiquetaElegirCasilla.setFont(new Font("arial", Font.BOLD, 13));
        panelJuego.add(etiquetaElegirCasilla);

        // Texto X y Y
        etiquetaCoordenadaXY = new JLabel("X: " + coordenadaXSeleccionada + " Y: " + coordenadaYSeleccionada, SwingConstants.CENTER);
        etiquetaCoordenadaXY.setBounds(25, 275, 150, 20);
        etiquetaCoordenadaXY.setOpaque(true);
        etiquetaCoordenadaXY.setForeground(Color.black);
        etiquetaCoordenadaXY.setFont(new Font("arial", Font.BOLD, 15));
        panelJuego.add(etiquetaCoordenadaXY);

        // Botón Moverse
        botonMoverse = new JButton();
        botonMoverse.setBounds(55, 300, 100, 30);
        botonMoverse.setEnabled(true);
        botonMoverse.setBackground(Color.red);
        try {
            ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Moverse.png"));
            botonMoverse.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 30, Image.SCALE_AREA_AVERAGING)));
        } catch (Exception e) {
            System.out.println("No se encontró la imagen. Usando texto predeterminado.");
            botonMoverse.setText("Moverse");
        }
        botonMoverse.setOpaque(false);
        botonMoverse.setBorderPainted(false);
        panelJuego.add(botonMoverse);

        // Acción del botón
        botonMoverse.addActionListener(e -> {
            System.out.println("Moviéndose");
            contadorTurnos++;
            colocarZombieFinDeRonda();
            etiquetaTurnos.setText("Turno: " + contadorTurnos);

            // Actualización de etiquetas
            etiquetaCoordenadaXY.setText("X: " + coordenadaXSeleccionada + " Y: " + coordenadaYSeleccionada);
        });

        // Actualización del panel
        panelJuego.revalidate();
        panelJuego.repaint();
    }


    public void funcionAtacar(){
        // Texto Elegir Arma:
        etiquetaElegirArma = new JLabel("Elegir Arma:", SwingConstants.CENTER); // Creamos etiqueta
        etiquetaElegirArma.setBounds(30, 245, 100, 30);
        etiquetaElegirArma.setOpaque(true); // Asi podemos poner background
        etiquetaElegirArma.setForeground(Color.black);
        etiquetaElegirArma.setFont(new Font("arial", Font.BOLD, 15)); // estableze el font se puede usar 0123 para typo de letra
        panelJuego.add(etiquetaElegirArma);
        etiquetaElegirArma.setOpaque(false);
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
        botonAtacar = new JButton();
        botonAtacar.setBounds(55, 300, 62, 26);
        botonAtacar.setEnabled(true);
        botonAtacar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Atacar.png"));
        botonAtacar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(62, 26, Image.SCALE_AREA_AVERAGING)));
        botonAtacar.setOpaque(false);
        panelJuego.add(botonAtacar);
        botonAtacar.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Atacando");
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
           }
        };
        botonAtacar.addActionListener(accionBoton4);
    }
            
    public void funcionQuedarse(){
        // Boton Moverse
        botonQuedarse = new JButton();
        botonQuedarse.setBounds(25, 275, 120, 25);
        botonQuedarse.setEnabled(true);
        botonQuedarse.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/SiguienteTurno.png"));
        botonQuedarse.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(120, 25, Image.SCALE_AREA_AVERAGING)));
        botonQuedarse.setOpaque(false);
        panelJuego.add(botonQuedarse);
        botonQuedarse.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Siguiente Turno");
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
           }
        };
        botonQuedarse.addActionListener(accionBoton4);
    }
    
    public void funcionBuscar(){
        // boton Buscar
        botonBuscar = new JButton();
        botonBuscar.setBounds(35, 265, 100, 40);
        botonBuscar.setEnabled(true);
        botonBuscar.setBackground(Color.red);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/Buscar.png"));
        botonBuscar.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(100, 40, Image.SCALE_AREA_AVERAGING)));
        botonBuscar.setOpaque(false);
        panelJuego.add(botonBuscar);
        botonBuscar.setBorderPainted(false);

        ActionListener accionBoton4 = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   System.out.println("Armas Seleccionadas");//Aqui hay q poner que el booleano en el inventario o arma sea true (armaActiva = true)
                   contadorTurnos++;
                   colocarZombieFinDeRonda();
                   etiquetaTurnos.setText("Turno: "+ contadorTurnos);
           }
        };
        botonBuscar.addActionListener(accionBoton4);
    }
    
    public void funcionElegirArma(Superviviente superviviente) {
        // Texto Elegir Arma:
        etiquetaElegirArma = new JLabel("Elegir Armas activas:", SwingConstants.CENTER);
        etiquetaElegirArma.setBounds(28, 245, 150, 30);
        etiquetaElegirArma.setOpaque(true);
        etiquetaElegirArma.setForeground(Color.black);
        etiquetaElegirArma.setFont(new Font("arial", Font.CENTER_BASELINE, 12));
        panelJuego.add(etiquetaElegirArma);
        etiquetaElegirArma.setOpaque(false);

        // Obtener inventario del superviviente
        Equipo inventario = superviviente.getInventario();
        String[] opcionesArmas = inventario.obtenerNombres().toArray(new String[0]);

        // Primer combo box para seleccionar el arma activa 1
        listaArmas = new JComboBox<>(opcionesArmas);
        listaArmas.setBounds(30, 275, 100, 20);
        listaArmas.setSelectedItem(null); // No se selecciona nada por defecto
        panelJuego.add(listaArmas);

        // Segundo combo box para seleccionar el arma activa 2
        listaArmas2 = new JComboBox<>(opcionesArmas);
        listaArmas2.setBounds(150, 275, 100, 20);
        listaArmas2.addItem("Ninguna"); // Opción adicional para desactivar la segunda arma
        listaArmas2.setSelectedItem("Ninguna");
        panelJuego.add(listaArmas2);

        // Acción para activar el arma seleccionada en listaArmas
        listaArmas.addActionListener(e -> {
            String seleccion = (String) listaArmas.getSelectedItem();
            for (Equipo item : inventario.obtenerObjetos()) {
                if (item instanceof Arma) {
                    Arma arma = (Arma) item;
                    // Activa el arma seleccionada y desactiva las demás
                    arma.setActiva(arma.getNombre().equals(seleccion));
                    if (arma.isActiva()) {
                        System.out.println("Arma activada: " + arma.getNombre());
                    }
                }
            }
        });

        // Acción para activar/desactivar el arma seleccionada en listaArmas2
        listaArmas2.addActionListener(e -> {
            String seleccion = (String) listaArmas2.getSelectedItem();
            for (Equipo item : inventario.obtenerObjetos()) {
                if (item instanceof Arma) {
                    Arma arma = (Arma) item;
                    // Activa el arma seleccionada si no es "Ninguna"
                    arma.setActiva(arma.getNombre().equals(seleccion) && !"Ninguna".equals(seleccion));
                    if (arma.isActiva()) {
                        System.out.println("Arma activada: " + arma.getNombre());
                    }
                }
            }
        });

        // Botón para confirmar la selección
        botonSeleccionar = new JButton();
        botonSeleccionar.setBounds(75, 300, 150, 30);
        botonSeleccionar.setEnabled(true);
        botonSeleccionar.setBackground(Color.red);
        ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/Seleccionar.png"));
        botonSeleccionar.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(150, 30, Image.SCALE_AREA_AVERAGING)));
        botonSeleccionar.setOpaque(false);
        botonSeleccionar.setBorderPainted(false);
        panelJuego.add(botonSeleccionar);

        // Acción del botón Seleccionar
        botonSeleccionar.addActionListener(e -> {
            System.out.println("Selección de armas completada.");
            contadorTurnos++;
            colocarZombieFinDeRonda();
            etiquetaTurnos.setText("Turno: " + contadorTurnos);
        });
    }

    
    public void limpiarPanel() {
    // Limpiar Moverse
        if (botonMoverse != null) {
            panelJuego.remove(botonMoverse);
        }
        if (etiquetaElegirCasilla != null) {
            panelJuego.remove(etiquetaElegirCasilla);
        }
        if (etiquetaCoordenadaXY != null) {
            panelJuego.remove(etiquetaCoordenadaXY);
        }
    // Limpiar Atacar
        if (botonAtacar != null) {
            panelJuego.remove(botonAtacar);
        }
        if (etiquetaElegirArma != null) {
            panelJuego.remove(etiquetaElegirArma);
        }
        if (listaActivas != null) {
            panelJuego.remove(listaActivas);
        }

    // Limpiar Quedarse
        if (botonQuedarse != null) {
            panelJuego.remove(botonQuedarse);
        }
    // Limpiar buscar
        if (botonBuscar != null) {
            panelJuego.remove(botonBuscar);
        }
    // Limpiar Elegir
        if (botonSeleccionar != null) {
            panelJuego.remove(botonSeleccionar);
        }
        if (etiquetaElegirArma != null) {
            panelJuego.remove(etiquetaElegirArma);
        }
        if (listaArmas != null) {
            panelJuego.remove(listaArmas);
        }
        if (listaArmas2 != null) {
            panelJuego.remove(listaArmas2);
        }
    // Actualizar la interfaz
    panelJuego.revalidate();
    panelJuego.repaint();
    }
     
    public Arma getArmaSeleccionada() {
        if (listaActivas != null) {
            return (Arma) listaActivas.getSelectedItem();
        }
        return null; // Si no hay ninguna arma seleccionada
    }
    
    
    public void atacarZombieSeleccionado(Arma armaSeleccionada) {
        if (coordenadaXSeleccionada == -1 || coordenadaYSeleccionada == -1) {
            System.out.println("Por favor, selecciona una casilla antes de atacar.");
            return;
        }

        Zombi zombieAtacado = buscarZombie(coordenadaXSeleccionada, coordenadaYSeleccionada);
        if (zombieAtacado != null) {
            if (armaSeleccionada != null) {
                int exitos = armaSeleccionada.lanzarDados();
                if (exitos * armaSeleccionada.getPotencia() >= zombieAtacado.getAguante()) {
                    System.out.println("¡Zombie eliminado!");
                    botones[coordenadaXSeleccionada][coordenadaYSeleccionada].setIcon(null);
                    zombies.remove(zombieAtacado);
                } else {
                    System.out.println("El zombie sigue vivo.");
                }
            } else {
                System.out.println("Por favor, selecciona un arma válida.");
            }
        } else {
            System.out.println("No hay un zombie en esa casilla.");
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
      
    

    
    
}
