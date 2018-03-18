package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
			this.anterior = null;
			this.siguiente = null;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            this.siguiente = cabeza;
			this.anterior = null;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            if(this.siguiente != null)
				return true;
			return false;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
			if(this.siguiente == null)
				throw new NoSuchElementException();
			this.anterior = this.siguiente;
			this.siguiente = this.siguiente.siguiente;
			return this.anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            if(this.anterior != null)
				return true;
			return false;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(this.anterior == null)
				throw new NoSuchElementException();
			this.siguiente = this.anterior;
			this.anterior = this.anterior.anterior;
			return this.siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
			this.siguiente = cabeza;
			this.anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
			this.siguiente = null;
			this.anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
		if(getElementos() == 0)
			return true;
		return false;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
		agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
		if(elemento == null)
			throw new IllegalArgumentException();
		Nodo n = new Nodo(elemento);
		if(esVacia())
			cabeza = rabo = n;
		else{
			rabo.siguiente = n;
			n.anterior = rabo;
			rabo = n;
		}
		longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if(elemento == null)
			throw new IllegalArgumentException();
		Nodo n = new Nodo(elemento);
		if(esVacia())
			rabo = cabeza = n;
		else{
			cabeza.anterior = n;
			n.siguiente = cabeza;
			cabeza = n;
		}
		longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
		if(elemento == null)
			throw new IllegalArgumentException();
		if(esVacia())
			agrega(elemento);
		else if(i <= 0)
			agregaInicio(elemento);
		else if(i >= getElementos())
			agregaFinal(elemento);
		else{
			Iterador it = new Iterador();
			int count = 0;
			while(count < i){
				it.next();
				count++;
			}
			Nodo n = new Nodo(elemento);
			Nodo ant = it.anterior;
			Nodo sig = it.siguiente;
			ant.siguiente = n;
			n.anterior = ant;
			sig.anterior = n;
			n.siguiente = sig;
			longitud++;
		}
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
		Nodo n = buscaNodo(cabeza,elemento);
		if(n != null){
			if(cabeza == rabo){
				cabeza = rabo = null;
			}else if(cabeza == n){
				cabeza = cabeza.siguiente;
				cabeza.anterior = null;
			} else if(rabo == n){
				rabo = rabo.anterior;
				rabo.siguiente = null;
			} else {
				n.siguiente.anterior = n.anterior;
				n.anterior.siguiente = n.siguiente; 
			}
			longitud--;
		}
	}


    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
	public T eliminaPrimero() {
        if(esVacia())
			throw new NoSuchElementException();
		T e = cabeza.elemento;
		if(getElementos() == 1)
			cabeza = rabo = null;
		else{
			cabeza = cabeza.siguiente;
			cabeza.anterior = null;
		}
		longitud--;
		return e;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(esVacia())
			throw new NoSuchElementException();
		T e = rabo.elemento;
		if(getElementos() == 1)
			rabo = cabeza = null;
		else{
			rabo = rabo.anterior;
			rabo.siguiente = null;
		}
		longitud--;
		return e;
    }

	private Nodo buscaNodo(Nodo n, T elem){
		if(n == null)
			return null;
        if(n.elemento.equals(elem))
			return n;
        return buscaNodo(n.siguiente, elem);
	}
	
    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(buscaNodo(cabeza,elemento) != null)
			return true;
		return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
		Lista<T> l = new Lista<T>();
		Iterador i = new Iterador();
		i.end();
		if(esVacia()){
			l.cabeza = l.rabo = null;
			return l;
		}
		while(i.hasPrevious())
			l.agrega(i.previous());
		return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> l = new Lista<T>();
		Nodo n = cabeza;
		if(esVacia()){
			l.cabeza = l.rabo = null;
		}
		while(n != null){
			l.agrega(n.elemento);
			n = n.siguiente;
		}
		return l;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
		cabeza = rabo = null;
		longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
		if(esVacia())
			throw new NoSuchElementException();
		return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if(esVacia())
			throw new NoSuchElementException();
		return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
		if(esVacia())
			throw new NoSuchElementException();
		if(i < 0 || i >= getElementos())
			throw new ExcepcionIndiceInvalido();
		int count = 0;
		Nodo n = cabeza;
		while(count < i){
			count++;
			n = n.siguiente;
		}
		return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
		if(!contiene(elemento))
			return (-1);
		Nodo n = cabeza;
		int count = 0;
		while(n.elemento != elemento){
			count++;
			n = n.siguiente;
		}
		return count;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
		if(esVacia())
			return "[]";
		StringBuilder str = new StringBuilder();
		str.append("[");
        Nodo n = cabeza;
        while(n != null){
            str.append(n.elemento.toString());
            n = n.siguiente;
            if(n != null)
				str.append(", ");
        }
        str.append("]");
        return str.toString();
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        if(getLongitud() != lista.getLongitud())
			return false;
		Nodo n = cabeza;
		Nodo m = lista.cabeza;
		while(n != null && m != null){
			if(!n.elemento.equals(m.elemento))
				return false;
			n = n.siguiente;
			m = m.siguiente;
		}
		return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
		if(getLongitud() <= 1)
			return copia();
		Lista<T> li = new Lista<>(), ld = new Lista<>(); 
		int n = (int) getElementos()/2;
		int cont = 0;
		for(T e : this){
			Lista<T> l = (cont++ < n) ? li : ld;
			l.agrega(e);
		}
		li = li.mergeSort(comparador);
		ld = ld.mergeSort(comparador);
		return mezcla(li,ld,comparador);
    }
	
	private Lista<T> mezcla(Lista<T> li,Lista<T> ld,Comparator<T> c){
		int l1 = li.getLongitud(), l2 = ld.getLongitud();
		Lista<T> l = new Lista<>();
		int i=0, j=0;
		while(i < l1 && j < l2){
			if(c.compare(li.get(i),ld.get(j)) <= 0){
				l.agrega(li.get(i));
				i++;
			}else{
				l.agrega(ld.get(j));
				j++;
			}
		}
		
		while(i < l1){
			l.agrega(li.get(i));
			i++;
		}
		while(j < l2){
			l.agrega(ld.get(j));
			j++;
		}
		return l;
	}

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
		return lista.mergeSort ((a, b) -> a. compareTo (b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
		return contiene(elemento);
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
