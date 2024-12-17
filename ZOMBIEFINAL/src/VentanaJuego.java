
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

public class VentanaJuego extends JFrame {
    private JPanel panel, panelJuego, panelRetomar, panelTablero;
    private JButton boton1, boton2, boton3, boton4, boton5;
    private JButton boton6, boton7, boton8, boton9, boton10;
    private JLabel etiquetaTurnos, etiqueta1, etiqueta2, etiqueta3;
    private JComboBox<String> listaArmas, listaArmas2, listaActivas;
    private JButton[][] botones;
    private final int filasColumnas = 10;
    private final int numZombies = 3;
    private int contadorTurnos;
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
        boton1 = new JButton();
        boton1.setBounds(125, 150, 200, 40);
        boton1.setBackground(Color.cyan);
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/resources/NuevaPartida.png"));
        boton1.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        boton1.setOpaque(false);
        boton1.setBorderPainted(false);
        boton1.addActionListener(e -> colocarPanelJuego());
        panel.add(boton1);

        boton2 = new JButton();
        boton2.setBounds(125, 200, 200, 40);
        boton2.setBackground(Color.green);
        ImageIcon imagen2 = new ImageIcon(getClass().getResource("/resources/RetomarPartida.png"));
        boton2.setIcon(new ImageIcon(imagen2.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        boton2.setOpaque(false);
        boton2.setBorderPainted(false);
        boton2.addActionListener(e -> retomarPartida());
        panel.add(boton2);

        boton3 = new JButton();
        boton3.setBounds(125, 250, 200, 40);
        boton3.setBackground(Color.red);
        ImageIcon imagen3 = new ImageIcon(getClass().getResource("/resources/Salir.png"));
        boton3.setIcon(new ImageIcon(imagen3.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        boton3.setOpaque(false);
        boton3.setBorderPainted(false);
        boton3.addActionListener(e -> System.exit(0));
        panel.add(boton3);
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
        boton4 = new JButton();
        boton4.setBounds(20, 360, 40, 40);
        boton4.setBackground(Color.red);
        ImageIcon imagen4 = new ImageIcon(getClass().getResource("/resources/Atras.png"));
        boton4.setIcon(new ImageIcon(imagen4.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        boton4.setOpaque(false);
        boton4.setBorderPainted(false);
        boton4.addActionListener(e -> colocarPanelMain());
        panelJuego.add(boton4);

        boton5 = new JButton();
        boton5.setBounds(70, 360, 200, 40);
        boton5.setBackground(Color.blue);
        ImageIcon imagen5 = new ImageIcon(getClass().getResource("/resources/SalirGuardar.png"));
        boton5.setIcon(new ImageIcon(imagen5.getImage().getScaledInstance(200, 40, Image.SCALE_AREA_AVERAGING)));
        boton5.setOpaque(false);
        boton5.setBorderPainted(false);
        boton5.addActionListener(e -> guardarYSalir());
        panelJuego.add(boton5);
    }

    private void colocarRadioBotones() {
        JRadioButton radioBoton1 = new JRadioButton("Moverse", false);
        radioBoton1.setBounds(30, 140, 150, 15);
        radioBoton1.setOpaque(false);
        panelJuego.add(radioBoton1);

        JRadioButton radioBoton2 = new JRadioButton("Atacar", false);
        radioBoton2.setBounds(30, 160, 150, 15);
        radioBoton2.setOpaque(false);
        panelJuego.add(radioBoton2);

        JRadioButton radioBoton3 = new JRadioButton("Quedarse", false);
        radioBoton3.setBounds(30, 180, 150, 15);
        radioBoton3.setOpaque(false);
        panelJuego.add(radioBoton3);

        JRadioButton radioBoton4 = new JRadioButton("Buscar", false);
        radioBoton4.setBounds(30, 200, 150, 15);
        radioBoton4.setOpaque(false);
        panelJuego.add(radioBoton4);

        JRadioButton radioBoton5 = new JRadioButton("Elegir arma", false);
        radioBoton5.setBounds(30, 220, 150, 15);
        radioBoton5.setOpaque(false);
        panelJuego.add(radioBoton5);

        ButtonGroup grupoRadioBotones = new ButtonGroup();
        grupoRadioBotones.add(radioBoton1);
        grupoRadioBotones.add(radioBoton2);
        grupoRadioBotones.add(radioBoton3);
        grupoRadioBotones.add(radioBoton4);
        grupoRadioBotones.add(radioBoton5);
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

    private void colocarZombiesInicio() {
        Random rand = new Random();
        Set<Point> posicionesUsadas = new HashSet<>();
        ImageIcon IconoZombi = new ImageIcon(getClass().getResource("/resources/zombie2.png"));

        for (int i = 0; i < numZombies; i++) {
            Zombi nuevoZombie;
            do {
                nuevoZombie = Zombi.crearZombiAleatorio();
            } while (posicionesUsadas.contains(new Point(nuevoZombie.getX(), nuevoZombie.getY())));

            zombies.add(nuevoZombie);
            posicionesUsadas.add(new Point(nuevoZombie.getX(), nuevoZombie.getY()));

            botones[nuevoZombie.getX()][nuevoZombie.getY()]
                    .setIcon(new ImageIcon(IconoZombi.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING)));
        }
    }

    private void actualizarEtiquetaCoordenadas(int x, int y) {
        if (etiqueta3 != null) {
            etiqueta3.setText("X: " + x + "          Y: " + y);
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

    private void actualizarEtiquetaTurnos(int turno) {
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
}
