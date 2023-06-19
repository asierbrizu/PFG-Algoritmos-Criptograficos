package algoritmos;

import java.util.ArrayList;

public class TransposicionColumnarMultiple {

	final static int NUM_TRANSPOSICIONES = 1;

	// Cifra el texto plano usando la clave. Con n se indica cuantas transposiciones
	// se deben realizar
	public static String cifrar(String textoPlano, String clave, int n) {
		// Caso base
		if (n == 1) {
			textoPlano = textoPlano.toUpperCase();
			clave = clave.toUpperCase();
			ArrayList<ArrayList<Character>> matriz = new ArrayList<>();// Matriz de caracteres para realizar las
																		// columnas
			int numFilas = (int) Math.ceil((double) textoPlano.length() / (double) clave.length());
			ArrayList<Integer> ordenacionClave = new ArrayList<>();
			// Inicialización de cada fila de la matriz. i:Filas matriz
			for (int i = 0; i < numFilas; i++) {
				matriz.add(new ArrayList<>());
			}
			// Introduccion de los caracteres del texto plano en la matriz. i:Filas matriz,
			// j:Columnas matriz
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < clave.length(); j++) {
					if (i * clave.length() + j < textoPlano.length()) {
						matriz.get(i).add(textoPlano.charAt(i * clave.length() + j));
					} else {
						matriz.get(i).add(' ');
					}
				}
			}
			// System.out.println(matriz);
			// Creación de la ordenacion de las columnas. i:Caracteres mayúsculas,
			// j:Columnas matriz
			for (int i = 65; i <= 90; i++) {
				for (int j = 0; j < clave.length(); j++) {
					if (clave.charAt(j) == (char) i) {
						ordenacionClave.add(j);
					}
				}
			}
			// System.out.println("Ordenación: " + ordenacionClave);

			String textoCifrado = "";
			// Creación del texto cifrado. i:Columnas matriz, j:Filas matriz
			for (int i = 0; i < ordenacionClave.size(); i++) {
				for (int j = 0; j < numFilas; j++) {
					textoCifrado += matriz.get(j).get(ordenacionClave.get(i));
				}
			}
			return textoCifrado;
		} else {
			// Caso recursivo
			return cifrar(cifrar(textoPlano, clave, 1), clave, n - 1);
		}
	}

	// Descifra el texto con la clave, realizando n transposiciones
	public static String descifrar(String textoCifrado, String clave, int n) {
		// Caso base
		if (n == 1) {
			textoCifrado = textoCifrado.toUpperCase();
			clave = clave.toUpperCase();
			ArrayList<ArrayList<Character>> matriz = new ArrayList<>();
			int numFilas = (int) Math.ceil((double) textoCifrado.length() / (double) clave.length());
			ArrayList<Integer> ordenacionClave = new ArrayList<>();
			// Inicialización de la matriz con espacios vacios. i:Filas matriz, j:Columnas
			// matriz
			for (int i = 0; i < numFilas; i++) {
				matriz.add(new ArrayList<>());
				for (int j = 0; j < clave.length(); j++) {
					matriz.get(i).add(' ');
				}
			}
			// Creación de la ordenacion de las columnas. i:Caracteres mayúsculas,
			// j:Columnas matriz
			for (int i = 65; i <= 90; i++) {
				for (int j = 0; j < clave.length(); j++) {
					if (clave.charAt(j) == (char) i) {
						ordenacionClave.add(j);
					}
				}
			}
			// System.out.println("Ordenación descifrado: " + ordenacionClave);

			// Introducción del texto cifrado en la matriz. i:Columnas matriz, j:Filas
			// matriz
			for (int i = 0; i < ordenacionClave.size(); i++) {
				for (int j = 0; j < numFilas; j++) {
					if (i * numFilas + j < textoCifrado.length()) {
						matriz.get(j).set(ordenacionClave.get(i), textoCifrado.charAt(i * numFilas + j));
					}

				}
			}
			// System.out.println("Matriz de descifrado: " + matriz);
			String textoPlano = "";
			// Creción del texto plano. i:Filas matriz, j:Columnas matriz
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < clave.length(); j++) {
					textoPlano += matriz.get(i).get(j);
				}
			}
			return textoPlano;
			// Caso recursivo
		} else {
			return descifrar(descifrar(textoCifrado, clave, 1), clave, n - 1);
		}
	}

	public static void main(String[] args) {
		String textoPlano = "Hola mundo";
		String clave = "DEUSTO";
		String textoCifrado = cifrar(textoPlano, clave, NUM_TRANSPOSICIONES);
	}

}
