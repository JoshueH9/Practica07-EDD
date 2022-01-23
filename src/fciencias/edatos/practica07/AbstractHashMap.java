package fciencias.edatos.practica07;

import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
//import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

/**
* Implementación de un mapa.
* @author Pintor Muñoz Pedro Joshue 420053796.
* @version 1.0 Enero 2022.
* @since Estructuras de Datos 2022-1.
*/
public class AbstractHashMap<K,V> implements Map<K,V>{

	/** Cantidad de moleculas */	//Universal para los compuestos, se reutiliza
	private static int molecula;

	/** Colisiones */
	private static int colision;

	/** Arreglo de elementos. */
	private V[] table;

	/** Capacidad de la tabla. */
	private int capacity;

	/** Factor primo para calcular longitudes. */
	private int prime;

	/** Cantidad del cambio y escala. */
	private long scale, shift;

	/** Cantidad de elementos */ 
	private int tamanio;

	/**
	* Crea un nuevo AbstractHashMap. 
	* @param cap la capacidad de la tabla.
	* @param p el factor primo.
	*/
	public AbstractHashMap(int cap, int p){
		table = (V[]) new Object[cap];
		prime = p;
		capacity = cap;
		Random rn = new Random();
		scale = rn.nextInt(prime-1) + 1;
		shift = rn.nextInt(prime);
	}

	/**
	* Crea un nuevo AbstractHashMap.
	* @param cap la capacidad de la tabla.
	*/
	public AbstractHashMap(int cap){
		this(cap, 109345121);
	}

	/**
	* Crea un nuevo AbstractHashMap.
	*/
	public AbstractHashMap(){
		this(17);
	}

	@Override
	public int size(){
		return tamanio;
	}

	@Override
	public V get(K key){
		int pos = hashFuction(key);
		return table[pos];
	}

	@Override
	public V put(K key, V value){
		int pos = hashFuction(key);
		//System.out.println("Valor: "+value+"\nPosicion: "+pos);
		V oldValue = table[pos];
		table[pos] = value;

		if(oldValue != null)
			colision++;
		else
			tamanio++;

		return oldValue;
	}

	@Override
	public V remove(K key){
		int pos = hashFuction(key);
		V oldValue = table[pos];
		table[pos] = null;
		tamanio--;
		return oldValue;
	}

	@Override
	public boolean isEmpty(){
		return (tamanio == 0) ? true : false ;
	}

	/**
	 * Funcion hash
	 * @param k la clave
	 * @return un entero asociado a la clave dentro de un rango válido
	 */
	private int hashFuction(K k){
		int hashCode = (int) (Math.abs(k.hashCode() * scale + shift) % prime);
		return hashCode % capacity;
	}

	/**
	 * Método que lee y almacena los datos en un mapa.
	 * @param rutaArchivo la ruta del archivo txt.
	 * @param map el mapa donde se almacenaran los elementos.
	 * */
	public void elementos(String rutaArchivo, AbstractHashMap map){

        String linea = null;     

        try{
            Reader reader = new FileReader(rutaArchivo);                                //Reader permite leer el archivo como un objeto de tipo File
            BufferedReader lector = new BufferedReader(reader);                         //BufferedReader permite leer el contenido en forma de cadenas 
            while((linea = lector.readLine()) != null){                                 //es por eso que el buffer recibe como parametro el reader anterior
                if(linea != null){                                                      //Para toma los datos de ese objeto para ser leidos se usa el método readLine().  
                    String[] cadena = linea.split("[,]");                                 //split Corta las lineas de codigo cada que encuentra un $.
					map.put(cadena[1],Double.parseDouble(cadena[2])); //RECORDATORIO: Integer.parseInt(cadena[1]) ---- Para Que convierta la cadena en un entero.  
                }                                                                       
            }   
            lector.close();            
        }catch(FileNotFoundException fnfe){
            System.out.println("No se encuentra el archivo");
        }catch(IOException ioe){
            System.out.println("Error en la lectura del archivo");
        }

	}

	/**
	 * Método que calcula la masa atomica
	 * @param cadena Los elementos quimicos
	 * */
	public double masaAtomica(String cadena, AbstractHashMap map){

		String[] desglosa = cadena.split("[.]");
		int cantidadElementos = desglosa.length;
		double masa = 0;

		for(int i=0 ; i<cantidadElementos ; i++){			

			if(map.get(simbolo(desglosa[i])) != null){
				Double val = ((Number) map.get(simbolo(desglosa[i]))).doubleValue()*molecula;
				masa = masa + val;
				molecula = 1;

			} else {

				System.out.println("CARACTERES INVALIDOS");
			}

		}	

		return masa;

	}

	public static void main(String[] args){

		String directorio = "src/fciencias/edatos/practica07/tabla-periodica.txt";

		AbstractHashMap<String, Double> map = new AbstractHashMap<>(10000);		

		map.elementos(directorio, map);

		String rojo = "\u001B[31m", verde = "\u001B[32m", amarillo = "\u001B[33m", morado = "\u001B[35m", blanco = "\u001B[37m";
        Scanner sc = new Scanner(System.in);
        System.out.println("PRACTICA 07 --- Tablas de dispercion\n\n");
        System.out.println("──▒▒▒▒▒▒───▄████▄\n─▒─▄▒─▄▒──███▄█▀ \n─▒▒▒▒▒▒▒─▐████──█─█ \n─▒▒▒▒▒▒▒──█████▄ \n─▒─▒─▒─▒───▀████▀ \n");
        System.out.println("\nPresiona Enter para comenzar.");
        sc.nextLine();

		int opcion = 0;

        do{
	
		    System.out.println("\n"+verde+"HOLA BIENVENIDO AL MENU."+blanco+"\n");

		    System.out.println(verde+"Elije una de las siguientes opciones:"+blanco+"\n\n "
				       +amarillo+"1) "+verde+" Calcular masa molecular"+blanco+"\n "
				       +amarillo+"2) "+verde+" Cantidad de elementos en la tabla"+blanco+"\n "
				       +amarillo+"3) "+verde+" Cantidad de colisiones"+blanco+"\n\n" //Aqui tiene 2 saltos de linea para que resalte mejor lo de salir del menú.
				       +amarillo+"4) "+verde+"Salir del menú"+blanco);
		    
		    try{  
				System.out.print(blanco+"\n"+amarillo+" Opcion: "+blanco+"\n");
				opcion = sc.nextInt();
			}catch(InputMismatchException ime){
				System.out.println(rojo+"ERROR: Ingresa un numero."+blanco);
				sc.nextLine();
			}catch(Exception e){
				System.out.println(rojo+"\n\nERROR INESPERADO EN EL CODIGO.\n\n");
			}

			switch(opcion){

				case 1:

					System.out.println("La forma de ingresar los datos es separando cada elemento con puntos.\n"+
						"En caso de querer ingresar mas de un atomo de un mismo elemento \nse deberá ingresar el numero a su derecha antes del punto.\n\n"+
						rojo+"Ejempĺos:   H2.O  ///   O2   ///  Na.S.O2"+blanco);
					
					sc.nextLine();
					String compuesto = sc.nextLine();

					if(map.masaAtomica(compuesto, map)!=0){
						System.out.println("Masa atomica de "+rojo+compuesto+blanco+": "+verde+map.masaAtomica(compuesto, map)+blanco);
						try{
	            			Thread.sleep(3000);
	        			}catch(InterruptedException ie){} 
					} else {
						try{
	            			Thread.sleep(3000);
	        			}catch(InterruptedException ie){} 
					}

					break;

				case 2:

					System.out.println("Cantidad de elementos dentro del mapa: "+rojo+map.size()+blanco);
			    	try{
	            		Thread.sleep(3000);
	        		}catch(InterruptedException ie){} 

					break;

				case 3:

					System.out.println("Trabajando con mapas puede existir la posibilidad de "+
						"que existan colisiones.\n\n En esta ocasión hubo "+rojo+colision+blanco+" colisiones");
			    	try{
	            		Thread.sleep(3000);
	        		}catch(InterruptedException ie){} 

					break;

			}

		}while(opcion!=4);	

	}


	/** METODOS AUXILIARES */

	/**
	 * Metodo que separa numeros de letras
	 * @param cadena La cadena a separar
	 * */
	private static String simbolo(String cadena){

		String[] numeros = {"0","1","2","3","4","5","6","7","8","9"};
		String nueva = cadena;
		int tamanio = cadena.length();

		for(int k=0; k<10 ; k++){
			if(cadena.contains(numeros[k])){	//Solo entra cuando tiene numeros

				for(int i=0; i<tamanio ; i++){ //itera entre caracteres de la cadena
					for(int j=0; j<10 ; j++){ //compara con los numeros
						if((cadena.charAt(i)+"").equals(numeros[j])){ //corta la cadena

							if(numero(cadena.substring(i,tamanio)) == 0){
								return nueva;
							}
							else{
								molecula = numero(cadena.substring(i,tamanio));								
								nueva = cadena.substring(0,i);
								return nueva;
							}
						}
					}
				}

			}
		}

		return nueva;
	}

	/** Metodo auxiliar para simbolo (la segunda cadena es un numero)
	 * */
	private static int numero(String cadenaNum){

		String[] numeros = {"0","1","2","3","4","5","6","7","8","9"};
		int numero = 0;
		int tamanio = cadenaNum.length();

		for(int i=0; i<tamanio ; i++){

			if(cadenaNum.charAt(i)=='0' || cadenaNum.charAt(i)=='1' || cadenaNum.charAt(i)=='2' || 
				cadenaNum.charAt(i)=='3' || cadenaNum.charAt(i)=='4' || cadenaNum.charAt(i)=='5' || 
				cadenaNum.charAt(i)=='6' || cadenaNum.charAt(i)=='7' || cadenaNum.charAt(i)=='8' || 
				cadenaNum.charAt(i)=='9'){
					
			}else{
					return 0;
			}			
		}
		return Integer.parseInt(cadenaNum);
	}
}
