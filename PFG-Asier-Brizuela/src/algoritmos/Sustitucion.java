package algoritmos;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Sustitucion {
	public final static int MIN_ASCII = 32;
	public final static int MAX_ASCII = 254;
	final static int NUM_ALFABETOS = 3;

	// Genera un mapa que relaciona cada caracter con la cantidad de veces que
	// aparece en el texto plano
	public static HashMap<Integer, Integer> contarFrecuencia(String textoPlano) {
		HashMap<Integer, Integer> frecuencias = new HashMap<Integer, Integer>();
		for (char c : textoPlano.toCharArray()) {
			if (frecuencias.containsKey((int) c)) {
				frecuencias.put((int) c, frecuencias.get((int) c) + 1);
			} else {
				frecuencias.put((int) c, 1);
			}
		}
		return frecuencias;
	}

	// Genera alfabetos de sustitución homofónicos
	public static HashMap<Integer, List<Integer>> generarAlfabeto(String textoPlano,
			HashMap<Integer, Integer> frecuencias) {
		HashMap<Integer, List<Integer>> asignaciones = new HashMap<Integer, List<Integer>>();
		HashSet<Integer> caracteres = new HashSet<>(frecuencias.keySet());
		String alfabeto = "";
		for (int i = 0; i < textoPlano.length() && i < MAX_ASCII - MIN_ASCII; i++) {
			alfabeto += (char) (MAX_ASCII - i);
		}
		if (textoPlano.length() <= MAX_ASCII - MIN_ASCII) {
			// Caso de que la longitud del texto plano sea inferior a la cantidad de
			// caracteres usados para el cifrado. (Es posible asignar un unico valor a cada
			// caracter del texto plano)
			for (Integer c : caracteres) {
				Random rand = new Random();
				List<Integer> lista = new ArrayList<>();
				int seleccionado;
				for (int j = 0; j < frecuencias.get(c) && alfabeto.length() > 0; j++) {
					seleccionado = rand.nextInt(alfabeto.length());
					lista.add((int) alfabeto.charAt(seleccionado));
					alfabeto = alfabeto.replace(String.valueOf((char) alfabeto.charAt(seleccionado)), "");
				}
				asignaciones.put(c, lista);

			}
		} else {
			// Caso de que la longitud del texto plano sea superior (Se asignan en función
			// de cuantas veces aparece cada una)
			ArrayList<ArrayList<Float>> matrizDeFrecuencias = new ArrayList<>();
			// System.out.println("Frecuencias de caracteres: "+frecuencias);
			int posicion = 0;
			// Se genera la matriz. Cada fila es un caracter que aparece en el texto plano.
			// La primera columna contiene la información del caracter.
			// A partir de la segunda columna contiene la cantidad de veces que aparece
			// dividida entre 1, 2, 3...
			for (int i = MIN_ASCII; i <= MAX_ASCII; i++, posicion++) {
				if (frecuencias.containsKey(i)) {
					matrizDeFrecuencias.add(new ArrayList<>());
					matrizDeFrecuencias.get(posicion).add((float) i);
					for (int j = MIN_ASCII; j <= MAX_ASCII; j++) {
						matrizDeFrecuencias.get(posicion).add((float) frecuencias.get(i) / (j - MIN_ASCII + 2));
					}
					// System.out.println("Matriz de frecuencias:
					// "+matrizDeFrecuencias.get(posicion));
				} else {
					posicion--;
				}
			}
			Random rand = new Random();
			List<Integer> lista = new ArrayList<>();
			int seleccionado;
			// Se realiza una primera vuelta, para asegurar de que cada caracter del texto
			// plano tenga al menos una asignación posible
			for (int fila = 0; fila < matrizDeFrecuencias.size(); fila++) {
				seleccionado = rand.nextInt(alfabeto.length());
				if (asignaciones.containsKey(Math.round(matrizDeFrecuencias.get(fila).get(0)))) {
					asignaciones.get(Math.round(matrizDeFrecuencias.get(fila).get(0)))
							.add((int) alfabeto.charAt(seleccionado));
				} else {
					lista.add((int) alfabeto.charAt(seleccionado));
					asignaciones.put(Math.round(matrizDeFrecuencias.get(fila).get(0)), lista);
					lista = new ArrayList<>();
				}

				alfabeto = alfabeto.replace(String.valueOf((char) alfabeto.charAt(seleccionado)), "");

			}
			// Se realizan mas vueltas escogiendo los valores mas altos de la matriz.
			// Este metodo de selección está inspirado en el Sistema D'Hont
			while (alfabeto.length() > 0) {
				int fila = 0;
				int columna = 0;
				double n = 0.0;
				for (int i = 0; i < matrizDeFrecuencias.size(); i++) {
					for (int j = 1; j < matrizDeFrecuencias.get(i).size(); j++) {
						if (matrizDeFrecuencias.get(i).get(j) > n) {
							fila = i;
							columna = j;
							n = matrizDeFrecuencias.get(i).get(j);
						}
					}
				}

				seleccionado = rand.nextInt(alfabeto.length());
				if (asignaciones.containsKey(Math.round(matrizDeFrecuencias.get(fila).get(0)))) {
					asignaciones.get(Math.round(matrizDeFrecuencias.get(fila).get(0)))
							.add((int) alfabeto.charAt(seleccionado));
				} else {
					lista.add((int) alfabeto.charAt(seleccionado));
					asignaciones.put(Math.round(matrizDeFrecuencias.get(fila).get(0)), lista);
					lista = new ArrayList<>();
				}

				alfabeto = alfabeto.replace(String.valueOf((char) alfabeto.charAt(seleccionado)), "");
				matrizDeFrecuencias.get(fila).set(columna, (float) 0.0);
			}
		}
		return asignaciones;
	}

	// Se usan los alfabetos de sustitución generados para cifrar el mensaje
	public static String cifrarMensaje(String textoPlano, ArrayList<HashMap<Integer, List<Integer>>> alfabetos) {
		String cifrado = "";
		int i = 1;
		for (char c : textoPlano.toCharArray()) {
			Random rand = new Random();
			List<Integer> lista = alfabetos.get(i % alfabetos.size()).get((int) c);
			cifrado += (char) (lista.get(rand.nextInt(lista.size()))).intValue();
			i++;
		}
		return cifrado;
	}

	// Se usan los alfabetos de sustitución generados para descifrar el mensaje
	public static String descifrarMensaje(String textoCifrado, ArrayList<HashMap<Integer, List<Integer>>> alfabetos) {
		String textoDescifrado = "";
		int i = 1;
		for (char c : textoCifrado.toCharArray()) {
			List<Integer> lista;
			HashSet<Integer> claves = new HashSet<>(alfabetos.get(i % alfabetos.size()).keySet());
			for (Integer clave : claves) {
				lista = alfabetos.get(i % alfabetos.size()).get(clave);
				if (lista.contains((int) c)) {
					textoDescifrado += (char) clave.intValue();
				}
			}
			i++;
		}
		return textoDescifrado;
	}

	public static void main(String[] args) {
		String textoPlano;
		// textoPlano="Hola mundo";
		// textoPlano = "Al igual que el Cifrado Cesar, este tipo de cifrado sustituye
		// los caracteres del texto plano por otros a la hora de cifrarlo. Los
		// algoritmos se pueden clasificar en tipos en función de si sustituyen los
		// caracteres de manera individual (sustitución simple), o si por el contrario
		// lo hacen en bloques de caracteres de un tamaño concreto (poligráfico), y
		// monoalfabético en función de si utiliza un único alfabeto de sustitución para
		// la asignación de los caracteres correspondientes a los del texto plano o
		// polialfabético si hace uso de varias sustituciones en diferentes puntos del
		// cifrado. Dado que el cifrado simple presenta un riesgo a ser descifrado
		// mediante el uso de cálculos de frecuencia de aparición de los caracteres del
		// texto cifrado, se incorporó el cifrado homofónico, en la que cada carácter
		// del texto plano puede ser sustituida por una de varias opciones en el proceso
		// de cifrado. Si a la hora de determinar que caracteres sustituirán a otros
		// durante la creación del alfabeto a usar, se realiza un cálculo de
		// frecuencias, se puede lograr que todos los caracteres del texto cifrado
		// aparezcan con la misma regularidad, dificultando el uso de este tipo de
		// técnicas para descifrarlo. El cifrado de Vigenère es un algoritmo de cifrado
		// poliafabético que utiliza una clave durante los procesos de cifrado y
		// descifrado, comparando los caracteres del texto plano con los de la clave
		// repetida en bucle. De esta manera se utilizan una cantidad de alfabetos de
		// sustitución igual a la longitud de la clave usada. Al igual que con el
		// cifrado simple, es posible tratar de descifrar los mensajes analizando la
		// frecuencia de aparición. El atacante debe primero averiguar cuantas letras
		// tiene la clave, para después aplicar un análisis de frecuencias a cada
		// alfabeto usado. Para poder determinar el numero de alfabetos se deben buscar
		// grupos de letras que se repitan periódicamente. Mediante el uso del método
		// Kasiski, que busca palabras repetidas y mide la distancia entre ellas para
		// deducir la cantidad de caracteres de la clave usada.";
		textoPlano = "Buenos dias Borja. Que tal estas?";
		textoPlano = textoPlano.toUpperCase();
		HashMap<Integer, Integer> frecuencias = contarFrecuencia(textoPlano);

		ArrayList<HashMap<Integer, List<Integer>>> alfabetos = new ArrayList<>();

		for (int i = 0; i < NUM_ALFABETOS; i++) {
			alfabetos.add(i, generarAlfabeto(textoPlano, frecuencias));
		}
		System.out.println(alfabetos);
		String textoCifrado = cifrarMensaje(textoPlano, alfabetos);
		System.out.println("Texto Cifrado: " + textoCifrado);
		String textoDescifrado = descifrarMensaje(textoCifrado, alfabetos);
		System.out.println("Texto Descifrado: " + textoDescifrado);
	}
}
