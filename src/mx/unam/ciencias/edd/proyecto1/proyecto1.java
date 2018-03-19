package mx.unam.ciencias.edd.proyecto1;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Comparator;
import mx.unam.ciencias.edd.Lista;
import java.text.Collator;

/**
  *<p>Proyecto 1:ordenamiento lexicografico.</p>
  
  <p>El programa es capaz de recibir varios archivos,
  por medio de la entrada estandar o la línea de comandos.</p>
  
  <p>
     @author Mario Angel García Domínguez.
	 @version 1.0.
	 @since 23/03/2018.
  </p>
**/

public class proyecto1{
	
	/** Agrega las lineas de una archivo. */
	private Lista<miString> lineas;
	/** Agrega diferentes archivos si es que asi es la entrada. */
	private Lista<String> archivos;
	/** Booleano que nos deice si la bandera reversa esta activada */
	private boolean reversa;
	/** Booleano que nos deice si la bandera ignora blancos esta activada */
	private boolean ignora;
	/** Nombre del archivo donde guardara la salida */
	private String nombreArchivo;
	
	public proyecto1(){
		lineas = new Lista<>();
		archivos = new Lista<>();
		reversa = false;
		ignora = false;
		nombreArchivo = null;
		nombreArchivo = null;
	}
	
	public static void main(String[] args){
		proyecto1 p = new proyecto1();
		p.checaBanderas(args);
		BufferedReader file = null;
		try{
			if(args.length == 0 || (args.length == 1 && p.reversa)){
				file = new BufferedReader(new InputStreamReader(System.in));
				p.leeArchivo(file);
			}else{
				for(String archivo : p.archivos){
					file = new BufferedReader(new FileReader(archivo));
					p.leeArchivo(file);
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("No se encontro el archivo");
		}finally{
			try{
				if(file != null)
					file.close();
			}catch(IOException e){
				System.out.println("Hubo un problema al cerrar el archivo");
			}
		}
		p.lineas = Lista.mergeSort(p.lineas);
		if(p.reversa)
			p.lineas = p.lineas.reversa();
		if(p.nombreArchivo == null)
			p.imprime();
		else
			p.guarda();
	}
	
	/**
	 * Lee el archivo e imprime el orden lexicografico.
	 */
	private void leeArchivo(BufferedReader buffer){
		try{
			String linea;
			do{
				linea = buffer.readLine();
				if(linea != null)
					lineas.agrega(new miString(linea));
			}while(linea != null);
		}catch(IOException e){
			System.out.println("Hubo un error al leer el archivo");
		}
	}
	
	/**
	 * Imprime el archivo ordenado.
	*/
	private void imprime(){
		for(miString str : lineas)
			System.out.println(str.toString());
	}
	
	/**
	 * En caso de que la bandera -o esta activada, guarda la salida del programa
	 * en un archivo.
	*/
	private void guarda(){
		FileWriter file = null;
		PrintWriter editor = null;
		try{
			file = new FileWriter(nombreArchivo);
			editor = new PrintWriter(file);
			for(miString str : lineas)
				editor.println(str.toString());
		}catch(IOException e){
			System.out.println("Hubo un problema al escribir el archivo");
		}finally{
			try{
				if(file != null)
					file.close();
			}catch(IOException e){
				System.out.println("Hubo un error al cerrar el archivo");
			}
		}
	}
	
	/**
	 * Reviza los parametros que recibe de entrada la clase
	*/
	private void checaBanderas(String[] args){
		for(int i=0;i<args.length;i++)
			if(args[i].equals("-r"))
				reversa = true;
			else if(args[i].equals("-b"))
				ignora = true;
			else if(args[i].equals("-o"))
				nombreArchivo = args[++i];
			else
				archivos.agrega(args[i]);
	}
	
	public class miString implements Comparable<miString> {
		private String str;
		
		public miString(String cadena){
			this.str = cadena;
		}
		
		@Override public String toString(){
			return str;
		}
		
		@Override public int compareTo(miString cadena){
			Collator collator = Collator.getInstance();
			collator.setStrength(Collator.PRIMARY);
			String string = cadena.toString();
			if(!Character.isLetter(string.charAt(0)))
				string = string.substring(1);
			if(ignora)
				return collator.compare(str.replaceAll("\\s",""),string.replaceAll("\\s",""));
			return collator.compare(str,string.toString());
		}
	}
}