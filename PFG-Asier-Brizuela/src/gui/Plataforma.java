package gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import algoritmos.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Plataforma extends JFrame {

	private static final long serialVersionUID = 1L;

	public Plataforma() {
		// Configuración de la ventana principal
		setTitle("Plataforma demostradora");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);

		ArrayList<ArrayList<HashMap<Integer, List<Integer>>>> conjuntosAlfabetos = new ArrayList<>();
		JComboBox<String> seleccionAlfabetosD = new JComboBox<String>();
		DefaultListModel modeloListaAlfabetos = new DefaultListModel();

		// Creacion de paneles
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel preCifradoSustitucionPanel = new JPanel(new GridLayout(4, 2));
		JPanel postCifradoSustitucionPanel = new JPanel(new GridLayout(0, 2));
		postCifradoSustitucionPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JPanel cifradoTransposicionPanel = new JPanel(new GridLayout(4, 2));
		JPanel descifradoSustitucionPanel = new JPanel(new GridLayout(4, 2));
		JPanel descifradoTransposicionPanel = new JPanel(new GridLayout(4, 2));
		
		JPanel listarAlfabetoPanel = new JPanel(new BorderLayout());
		
		JTabbedPane pestañasCifrarSustitucion = new JTabbedPane();
		pestañasCifrarSustitucion.addTab("Cifrar mensaje", preCifradoSustitucionPanel);

		JScrollPane scrollPostCifrado = new JScrollPane(postCifradoSustitucionPanel);
		scrollPostCifrado.getVerticalScrollBar().setUnitIncrement(16);

		// Crear y configurar el menú
		JMenuBar menu = new JMenuBar();

		JMenu sustitucionMenu = new JMenu("Sustitución");
		JMenu transposicionMenu = new JMenu("Transposición");

		JMenuItem opcionAlfabetos = new JMenuItem("Alfabetos");
		JMenuItem opcionCifrarSustitucion = new JMenuItem("Cifrar");
		JMenuItem opcionDescifrarSustitucion = new JMenuItem("Descifrar");
		JMenuItem opcionCifrarTransposicion = new JMenuItem("Cifrar");
		JMenuItem opcionDescifrarTransposicion = new JMenuItem("Descifrar");
		JMenuItem opcionSalir = new JMenuItem("Salir");

		opcionSalir.addActionListener(e -> System.exit(0));
		opcionCifrarSustitucion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(pestañasCifrarSustitucion, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		opcionDescifrarSustitucion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(descifradoSustitucionPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		opcionCifrarTransposicion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(cifradoTransposicionPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		opcionDescifrarTransposicion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(descifradoTransposicionPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		opcionAlfabetos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(listarAlfabetoPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});

		sustitucionMenu.add(opcionCifrarSustitucion);
		sustitucionMenu.add(opcionAlfabetos);
		sustitucionMenu.add(opcionDescifrarSustitucion);
		transposicionMenu.add(opcionCifrarTransposicion);
		transposicionMenu.add(opcionDescifrarTransposicion);

		menu.add(sustitucionMenu);
		menu.add(transposicionMenu);
		menu.add(opcionSalir);

		setJMenuBar(menu);
//----------------------------

		// Crear el panel para cifrar por sustitución
		JLabel textoPlanoSustitucionLabel = new JLabel("Texto plano:", JLabel.CENTER);
		JTextArea textoPlanoSustitucion = new JTextArea();
		JScrollPane scrollTextoPlanoSustitucion = new JScrollPane(textoPlanoSustitucion);
		JLabel numAlfabetosLabel = new JLabel("Cantidad de alfabetos", JLabel.CENTER);
		JSpinner numAlfabetos = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 10.0, 1.0));
		((JSpinner.DefaultEditor) numAlfabetos.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
		JLabel textoCifradoSustitucionLabel = new JLabel("Texto cifrado:", JLabel.CENTER);
		JTextArea textoCifradoSustitucion = new JTextArea();
		textoCifradoSustitucion.setEditable(false);
		JButton botonCifrarSustitucion = new JButton("Cifrar");
		botonCifrarSustitucion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				HashMap<Integer, Integer> frecuencias = Sustitucion.contarFrecuencia(textoPlanoSustitucion.getText());
				int num = ((Double) numAlfabetos.getValue()).intValue();
				ArrayList<HashMap<Integer, List<Integer>>> alfabetos = new ArrayList<>();
				for (int i = 0; i < num; i++) {
					alfabetos.add(Sustitucion.generarAlfabeto(textoPlanoSustitucion.getText(), frecuencias));
				}
				conjuntosAlfabetos.add(alfabetos);
				modeloListaAlfabetos.addElement("Conjunto" + (modeloListaAlfabetos.size() + 1));
				seleccionAlfabetosD.addItem("Conjunto " + (conjuntosAlfabetos.size()));

				textoCifradoSustitucion.setText(Sustitucion.cifrarMensaje(textoPlanoSustitucion.getText(), alfabetos));

				preCifradoSustitucionPanel.remove(botonCifrarSustitucion);
				preCifradoSustitucionPanel.add(textoCifradoSustitucionLabel);
				preCifradoSustitucionPanel.add(textoCifradoSustitucion);
				pestañasCifrarSustitucion.addTab("Alfabetos generados", scrollPostCifrado);
				postCifradoSustitucionPanel.removeAll();
				preCifradoSustitucionPanel.add(botonCifrarSustitucion);

				for (int alfabeto = 0; alfabeto < alfabetos.size(); alfabeto++) {
					String[] titulos = { "Caracter", "Opciones de sustitución" };
					Object[][] datos = new Object[alfabetos.get(alfabeto).size()][2];
					int fila = 0;
					for (int caracter = Sustitucion.MIN_ASCII; caracter <= Sustitucion.MAX_ASCII; caracter++) {
						if (alfabetos.get(alfabeto).containsKey(caracter)) {
							datos[fila][0] = (char) caracter;
							ArrayList<Character> listaDeSustitucion = new ArrayList<Character>();
							for (int c : alfabetos.get(alfabeto).get(caracter)) {
								listaDeSustitucion.add((char) c);
							}
							datos[fila][1] = listaDeSustitucion;
							fila++;
						}

					}
					JLabel l = new JLabel("Alfabeto " + (alfabeto + 1), JLabel.CENTER);
					l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					postCifradoSustitucionPanel.add(l);
					JTable t = new JTable(datos, titulos);
					t.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					postCifradoSustitucionPanel.add(t);

				}

				revalidate();
				repaint();
			}
		});

		preCifradoSustitucionPanel.add(textoPlanoSustitucionLabel);
		preCifradoSustitucionPanel.add(scrollTextoPlanoSustitucion);
		preCifradoSustitucionPanel.add(numAlfabetosLabel);
		preCifradoSustitucionPanel.add(numAlfabetos);
		preCifradoSustitucionPanel.add(botonCifrarSustitucion);
//---------------------------------

		// Crear el panel para visualizar los alfabetos
		JPanel panelTablas = new JPanel(new GridLayout(0, 2));
		panelTablas.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		JScrollPane scrollListado = new JScrollPane(panelTablas);
		scrollPostCifrado.getVerticalScrollBar().setUnitIncrement(16);

		for (int i = 0; i < conjuntosAlfabetos.size(); i++) {
			modeloListaAlfabetos.addElement("Conjunto" + (i + 1));
		}
		JList<String> listaConjuntos = new JList<String>();
		listaConjuntos.setModel(modeloListaAlfabetos);

		listaConjuntos.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				panelTablas.removeAll();
				ArrayList<HashMap<Integer, List<Integer>>> alfabetos = conjuntosAlfabetos
						.get(listaConjuntos.getSelectedIndex());
				for (int alfabeto = 0; alfabeto < alfabetos.size(); alfabeto++) {
					String[] titulos = { "Caracter", "Opciones de sustitución" };
					Object[][] datos = new Object[alfabetos.get(alfabeto).size()][2];
					int fila = 0;
					for (int caracter = Sustitucion.MIN_ASCII; caracter <= Sustitucion.MAX_ASCII; caracter++) {
						if (alfabetos.get(alfabeto).containsKey(caracter)) {
							datos[fila][0] = (char) caracter;
							ArrayList<Character> listaDeSustitucion = new ArrayList<Character>();
							for (int c : alfabetos.get(alfabeto).get(caracter)) {
								listaDeSustitucion.add((char) c);
							}
							datos[fila][1] = listaDeSustitucion;
							fila++;
						}

					}
					JLabel l = new JLabel("Alfabeto " + (alfabeto + 1), JLabel.CENTER);
					l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					panelTablas.add(l);
					JTable t = new JTable(datos, titulos);
					t.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					panelTablas.add(t, BorderLayout.CENTER);
				}

				revalidate();
				repaint();
			}
		});

		listarAlfabetoPanel.add(listaConjuntos, BorderLayout.WEST);
		listarAlfabetoPanel.add(scrollListado, BorderLayout.CENTER);

//----------------------------

		// Crear el panel para descifrar por sustitucion
		JLabel textoCifradoSustitucionLabelD = new JLabel("Texto cifrado:", JLabel.CENTER);
		JTextArea textoCifradoSustitucionD = new JTextArea();
		JScrollPane scrollTextoCifradoSustitucion = new JScrollPane(textoCifradoSustitucionD);
		JLabel seleccionAlfabetosLabelD = new JLabel("Alfabetos", JLabel.CENTER);
		JButton botonDescifrarSustitucion = new JButton("Descifrar");
		JLabel textoDescifradoSustitucionLabel = new JLabel("Texto plano:", JLabel.CENTER);
		JTextArea textoDescifradoSustitucion = new JTextArea();
		textoDescifradoSustitucion.setEditable(false);

		botonDescifrarSustitucion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textoDescifradoSustitucion.setText(Sustitucion.descifrarMensaje(textoCifradoSustitucionD.getText(),
						conjuntosAlfabetos.get(seleccionAlfabetosD.getSelectedIndex())));

				descifradoSustitucionPanel.remove(botonDescifrarSustitucion);
				descifradoSustitucionPanel.add(textoDescifradoSustitucionLabel);
				descifradoSustitucionPanel.add(textoDescifradoSustitucion);
				descifradoSustitucionPanel.add(botonDescifrarSustitucion);
				revalidate();
				repaint();

			}
		});

		descifradoSustitucionPanel.add(textoCifradoSustitucionLabelD);
		descifradoSustitucionPanel.add(scrollTextoCifradoSustitucion);
		descifradoSustitucionPanel.add(seleccionAlfabetosLabelD);
		descifradoSustitucionPanel.add(seleccionAlfabetosD);
		descifradoSustitucionPanel.add(botonDescifrarSustitucion);

//-----------------------------------

		// Crear el panel para cifrar por transposición
		JLabel textoPlanoTransposicionLabel = new JLabel("Texto plano", JLabel.CENTER);
		JTextArea textoPlanoTransposicion = new JTextArea("Hola mundo");
		JScrollPane scrollTextoPlanoTransposicion = new JScrollPane(textoPlanoTransposicion);
		JLabel claveTransposicionLabel = new JLabel("Clave", JLabel.CENTER);
		JTextField claveTransposicion = new JTextField("DEUSTO");
		JLabel numTransposicionesLabel = new JLabel("Cantidad de transposiciones", JLabel.CENTER);
		JSpinner numTransposiciones = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 10.0, 1.0));
		((JSpinner.DefaultEditor) numTransposiciones.getEditor()).getTextField()
				.setHorizontalAlignment(JTextField.CENTER);
		JLabel textoCifradoTransposicionLabel = new JLabel("Texto cifrado", JLabel.CENTER);
		JTextField textoCifradoTransposicion = new JTextField(
				TransposicionColumnarMultiple.cifrar(textoPlanoTransposicion.getText(), claveTransposicion.getText(),
						((Double) numTransposiciones.getValue()).intValue()));

		textoCifradoTransposicion.setEditable(false);

		DocumentListener actualizarCifradoTransposicionTextFields = new DocumentListener() {

			public void actualizar() {
				try {
					numTransposiciones.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				if (!textoPlanoTransposicion.getText().equals("") && !claveTransposicion.getText().equals("")) {
					textoCifradoTransposicion
							.setText(TransposicionColumnarMultiple.cifrar(textoPlanoTransposicion.getText(),
									claveTransposicion.getText(), ((Double) numTransposiciones.getValue()).intValue()));
				} else {
					textoCifradoTransposicion.setText("");
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				actualizar();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				actualizar();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				actualizar();
			}
		};

		ChangeListener actualizarCifradoTransposicionSpinner = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					numTransposiciones.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				if (!textoPlanoTransposicion.getText().equals("") && !claveTransposicion.getText().equals("")) {
					textoCifradoTransposicion
							.setText(TransposicionColumnarMultiple.cifrar(textoPlanoTransposicion.getText(),
									claveTransposicion.getText(), ((Double) numTransposiciones.getValue()).intValue()));
				} else {
					textoCifradoTransposicion.setText("");
				}
			}
		};

		textoPlanoTransposicion.getDocument().addDocumentListener(actualizarCifradoTransposicionTextFields);
		claveTransposicion.getDocument().addDocumentListener(actualizarCifradoTransposicionTextFields);
		numTransposiciones.addChangeListener(actualizarCifradoTransposicionSpinner);

		cifradoTransposicionPanel.add(textoPlanoTransposicionLabel);
		cifradoTransposicionPanel.add(scrollTextoPlanoTransposicion);
		cifradoTransposicionPanel.add(claveTransposicionLabel);
		cifradoTransposicionPanel.add(claveTransposicion);
		cifradoTransposicionPanel.add(numTransposicionesLabel);
		cifradoTransposicionPanel.add(numTransposiciones);
		cifradoTransposicionPanel.add(textoCifradoTransposicionLabel);
		cifradoTransposicionPanel.add(textoCifradoTransposicion);
//-------------------------------

		// Crear el panel para descifrar por transposición
		JLabel textoCifradoTransposicionLabelD = new JLabel("Texto cifrado", JLabel.CENTER);
		JTextArea textoCifradoTransposicionD = new JTextArea("HUONM AO  LD");
		JScrollPane scrollTextoCifradoTransposicion = new JScrollPane(textoCifradoTransposicionD);
		JLabel claveTransposicionLabelD = new JLabel("Clave", JLabel.CENTER);
		JTextField claveTransposicionD = new JTextField("DEUSTO");
		JLabel numTransposicionesLabelD = new JLabel("Cantidad de transposiciones", JLabel.CENTER);
		JSpinner numTransposicionesD = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 10.0, 1.0));
		((JSpinner.DefaultEditor) numTransposicionesD.getEditor()).getTextField()
				.setHorizontalAlignment(JTextField.CENTER);
		JLabel textoDescifradoTransposicionLabel = new JLabel("Texto descifrado", JLabel.CENTER);
		JTextField textoDescifradoTransposicion = new JTextField(
				TransposicionColumnarMultiple.descifrar(textoCifradoTransposicionD.getText(),
						claveTransposicionD.getText(), ((Double) numTransposicionesD.getValue()).intValue()));
		textoDescifradoTransposicion.setEditable(false);

		DocumentListener actualizarDescifradoTransposicionTextFields = new DocumentListener() {

			public void actualizar() {
				try {
					numTransposicionesD.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				if (!textoCifradoTransposicionD.getText().equals("") && !claveTransposicionD.getText().equals("")) {
					textoDescifradoTransposicion.setText(TransposicionColumnarMultiple.descifrar(
							textoCifradoTransposicionD.getText(), claveTransposicionD.getText(),
							((Double) numTransposicionesD.getValue()).intValue()));
				} else {
					textoDescifradoTransposicion.setText("");
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				actualizar();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				actualizar();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				actualizar();
			}
		};

		ChangeListener actualizarDescifradoTransposicionSpinner = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					numTransposicionesD.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				if (!textoCifradoTransposicionD.getText().equals("") && !claveTransposicionD.getText().equals("")) {
					textoDescifradoTransposicion.setText(TransposicionColumnarMultiple.descifrar(
							textoCifradoTransposicionD.getText(), claveTransposicionD.getText(),
							((Double) numTransposicionesD.getValue()).intValue()));
				} else {
					textoDescifradoTransposicion.setText("");
				}
			}
		};

		textoCifradoTransposicionD.getDocument().addDocumentListener(actualizarDescifradoTransposicionTextFields);
		claveTransposicionD.getDocument().addDocumentListener(actualizarDescifradoTransposicionTextFields);
		numTransposicionesD.addChangeListener(actualizarDescifradoTransposicionSpinner);

		descifradoTransposicionPanel.add(textoCifradoTransposicionLabelD);
		descifradoTransposicionPanel.add(scrollTextoCifradoTransposicion);
		descifradoTransposicionPanel.add(claveTransposicionLabelD);
		descifradoTransposicionPanel.add(claveTransposicionD);
		descifradoTransposicionPanel.add(numTransposicionesLabelD);
		descifradoTransposicionPanel.add(numTransposicionesD);
		descifradoTransposicionPanel.add(textoDescifradoTransposicionLabel);
		descifradoTransposicionPanel.add(textoDescifradoTransposicion);
//-------------------------------------

		add(mainPanel);
		setSize(800, 600);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Plataforma program = new Plataforma();
			program.setVisible(true);
		});
	}
}
