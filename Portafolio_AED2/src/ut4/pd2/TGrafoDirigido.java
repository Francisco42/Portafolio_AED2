package ut4.pd2;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class TGrafoDirigido implements IGrafoDirigido {

    private final Map<Comparable, TVertice> vertices; //lista de vertices del grafo.-

    public TGrafoDirigido(Collection<TVertice> vertices, Collection<TArista> aristas) {
        this.vertices = new HashMap<>();
        for (TVertice vertice : vertices) {
            insertarVertice(vertice.getEtiqueta());
        }
        for (TArista arista : aristas) {
            insertarArista(arista);
        }
    }

    /**
     * Metodo encargado de eliminar una arista dada por un origen y destino. En
     * caso de no existir la adyacencia, retorna falso. En caso de que las
     * etiquetas sean invalidas, retorna falso.
     *
     * @param nomVerticeOrigen
     * @param nomVerticeDestino
     * @return
     */
    @Override
    public boolean eliminarArista(Comparable nomVerticeOrigen, Comparable nomVerticeDestino) {
        if ((nomVerticeOrigen != null) && (nomVerticeDestino != null)) {
            TVertice vertOrigen = buscarVertice(nomVerticeOrigen);
            if (vertOrigen != null) {
                return vertOrigen.eliminarAdyacencia(nomVerticeDestino);
            }
        }
        return false;
    }

    /**
     * Metodo encargado de eliminar un vertice en el grafo. En caso de no
     * existir el v�rtice, retorna falso. En caso de que la etiqueta sea
     * inv�lida, retorna false.
     *
     * @param nombreVertice
     * @return
     */
    @Override
    public boolean eliminarVertice(Comparable nombreVertice) {
        if (nombreVertice != null) {
            getVertices().remove(nombreVertice);
            return getVertices().containsKey(nombreVertice);
        }
        return false;
    }

    /**
     * Metodo encargado de verificar la existencia de una arista. Las etiquetas
     * pasadas por par�metro deben ser v�lidas.
     *
     * @param etiquetaOrigen
     * @param etiquetaDestino
     * @return True si existe la adyacencia, false en caso contrario
     */
    @Override
    public boolean existeArista(Comparable etiquetaOrigen, Comparable etiquetaDestino) {
        TVertice vertOrigen = buscarVertice(etiquetaOrigen);
        TVertice vertDestino = buscarVertice(etiquetaDestino);
        if ((vertOrigen != null) && (vertDestino != null)) {
            return vertOrigen.buscarAdyacencia(vertDestino) != null;
        }
        return false;
    }

    /**
     * Metodo encargado de verificar la existencia de un vertice dentro del
     * grafo.-
     *
     * La etiqueta especificada como par�metro debe ser v�lida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a buscar.-
     * @return True si existe el vertice con la etiqueta indicada, false en caso
     * contrario
     */
    @Override
    public boolean existeVertice(Comparable unaEtiqueta) {
        return getVertices().get(unaEtiqueta) != null;
    }

    /**
     * Metodo encargado de verificar buscar un vertice dentro del grafo.-
     *
     * La etiqueta especificada como parametro debe ser valida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a buscar.-
     * @return El vertice encontrado. En caso de no existir, retorna nulo.
     */
    private TVertice buscarVertice(Comparable unaEtiqueta) {
        return getVertices().get(unaEtiqueta);
    }

    /**
     * Matodo encargado de insertar una arista en el grafo (con un cierto
     * costo), dado su vertice origen y destino.- Para que la arista sea valida,
     * se deben cumplir los siguientes casos: 1) Las etiquetas pasadas por
     * parametros son v�lidas.- 2) Los vertices (origen y destino) existen
     * dentro del grafo.- 3) No es posible ingresar una arista ya existente
     * (miso origen y mismo destino, aunque el costo sea diferente).- 4) El
     * costo debe ser mayor que 0.
     *
     * @param arista
     * @return True si se pudo insertar la adyacencia, false en caso contrario
     */
    @Override
    public boolean insertarArista(TArista arista) {
        boolean tempbool = false;
        if ((arista.getEtiquetaOrigen() != null) && (arista.getEtiquetaDestino() != null)) {
            TVertice vertOrigen = buscarVertice(arista.getEtiquetaOrigen());
            TVertice vertDestino = buscarVertice(arista.getEtiquetaDestino());
            tempbool = (vertOrigen != null) && (vertDestino != null);
            if (tempbool) {
                //getLasAristas().add(arista);
                return vertOrigen.insertarAdyacencia(arista.getCosto(), vertDestino);
            }

        }
        return false;
    }

    /**
     * Metodo encargado de insertar un vertice en el grafo.
     *
     * No pueden ingresarse v�rtices con la misma etiqueta. La etiqueta
     * especificada como par�metro debe ser v�lida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a ingresar.
     * @return True si se pudo insertar el vertice, false en caso contrario
     */
    public boolean insertarVertice(Comparable unaEtiqueta) {
        if ((unaEtiqueta != null) && (!existeVertice(unaEtiqueta))) {
            TVertice vert = new TVertice(unaEtiqueta);
            getVertices().put(unaEtiqueta, vert);
            return getVertices().containsKey(unaEtiqueta);
        }
        return false;
    }

    @Override
    public boolean insertarVertice(TVertice vertice) {
        Comparable unaEtiqueta = vertice.getEtiqueta();
        if ((unaEtiqueta != null) && (!existeVertice(unaEtiqueta))) {
            getVertices().put(unaEtiqueta, vertice);
            return getVertices().containsKey(unaEtiqueta);
        }
        return false;
    }

    public Object[] getEtiquetasOrdenado() {
        TreeMap<Comparable, TVertice> mapOrdenado = new TreeMap<>(this.getVertices());
        return mapOrdenado.keySet().toArray();
    }

    @Override
    public void desvisitarVertices() {
        for (TVertice vertice : this.vertices.values()) {
            vertice.setVisitado(false);
        }
    }

    /**
     * @return the vertices
     */
    @Override
    public Map<Comparable, TVertice> getVertices() {
        return vertices;
    }

    @Override
    public Comparable centroDelGrafo() {
        Double[][] matrizDeFloyd = floyd();
        Double[] vectorExcentricidades = new Double[matrizDeFloyd.length];

        for (int i = 0; i < vectorExcentricidades.length; i++) {
            vectorExcentricidades[i] = 0.0;
        }

        for (int i = 0; i < matrizDeFloyd.length; i++) {
            for (int j = 0; j < matrizDeFloyd.length; j++) {
                if (matrizDeFloyd[i][j] > vectorExcentricidades[j]) {
                    vectorExcentricidades[j] = matrizDeFloyd[i][j];
                }
            }
        }
        return (Comparable) vertices.keySet().toArray()[obtenerMinimoValor(vectorExcentricidades)];
    }

    private int obtenerMinimoValor(Double[] vectorExcentricidades) {
        int posicion = 0;
        Double minimo = vectorExcentricidades[posicion];
        for (int i = 1; i < vectorExcentricidades.length; i++) {
            if (vectorExcentricidades[i] < minimo) {
                minimo = vectorExcentricidades[i];
                posicion = i;
            }
        }
        return posicion;
    }

    @Override
    public Double[][] floyd() {
        Double[][] matrizFloyd = UtilGrafos.obtenerMatrizCostos(vertices);
        //matrizPredecesores = new Double[matrizFloyd.length][matrizFloyd.length];
        for (int i = 0; i < matrizFloyd.length; i++) {
            matrizFloyd[i][i] = 0.0;
        }
        for (int k = 0; k < matrizFloyd.length; k++) {
            for (int i = 0; i < matrizFloyd.length; i++) {
                for (int j = 0; j < matrizFloyd.length; j++) {
                    if ((matrizFloyd[i][k] + matrizFloyd[k][j]) < matrizFloyd[i][j]) {
                        matrizFloyd[i][j] = matrizFloyd[i][k] + matrizFloyd[k][j];
                        //matrizPredecesores[i][j] = k + 0d;
                    }
                }
            }
        }
        return matrizFloyd;
    }

    @Override
    public Comparable obtenerExcentricidad(Comparable etiquetaVertice) {
        Double[][] c = floyd();
        int columnaVertice = 0;
        int contador = 0;
        for (Comparable key : vertices.keySet()) {
            if (key.equals(etiquetaVertice)) {
                columnaVertice = contador;
                break;
            }
            contador += 1;
        }
        double excentricidad = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i][columnaVertice] > excentricidad) {
                excentricidad = c[i][columnaVertice];
            }
        }
        if (excentricidad == Double.MAX_VALUE) {
            excentricidad = -1;
        }
        return excentricidad;
    }

    @Override
    public Collection<TVertice> bpf() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<TVertice> bea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<TVertice> bpf(TVertice vertice) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<TVertice> bpf(Comparable etiquetaOrigen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean[][] warshall() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TCaminos todosLosCaminos(Comparable etiquetaOrigen, Comparable etiquetaDestino) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean tieneCiclo(TCamino camino) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean tieneCiclo(Comparable etiquetaOrigen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean tieneCiclo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
