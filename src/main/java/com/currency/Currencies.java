/**
 * 
 */
package com.currency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.currency.Logic.Tuple.SPLIT_POINT;

/**
 * @author sleyzerzon
 *
 */
public final class Currencies {

	private  Set<String> currencies = new HashSet<>();
	private static Graph<String> currenciesGraph;

	public  Set<String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Set<String> currencies) {
		this.currencies = currencies;
	}
	
	public  Set<String> buildCurrencies(Map<String, Double> ratesCache){
		
		for(String key:ratesCache.keySet()) {
			String left = key.substring(0,SPLIT_POINT);
			String right = key.substring(SPLIT_POINT, key.length());
			
			currencies.add(left);
			currencies.add(right);
		}
		
		return currencies;
	}
	
	public  Graph<String> buildGraph(Map<String, Double> ratesCache){
		
		buildCurrencies(ratesCache);
		
		currenciesGraph = new Graph<String>();

		for(String currency: currencies) {
			currenciesGraph.addNode(currency);
		}
		
		for(Map.Entry<String, Double> entry: ratesCache.entrySet()) {
			String key = entry.getKey();
			String from = key.substring(0,SPLIT_POINT);
			String to = key.substring(SPLIT_POINT, key.length());
			Double rate = entry.getValue();
			currenciesGraph.addEdge(from, to, rate);
		}
		
		return currenciesGraph;
	}
	
	
	public final static class Graph<T> implements Iterable<T>{

		/* Internal representation
		 * 
		 * A map from nodes in the graph to sets of outgoing edges.  Each
	     * set of edges is represented by a map from edges to doubles.
	     */
	    private final Map<T, Map<T, Double>> graph = new HashMap<>();
		
		@Override
		public Iterator<T> iterator() {
			return graph.keySet().iterator();
		}
		
		/**
	     *  Adds a new node to the graph. A no-op for already existing node.
	     * 
	     * @param node  Adds to a graph. No-op if the node is null.
	     * @return      true if node is added, false otherwise.
	     */
	    public boolean addNode(T node) {
	        if (node == null) throw new NullPointerException("The node cannot be null.");
	        
	        if (graph.containsKey(node)) return false;

	        graph.put(node, new HashMap<T, Double>());
	        return true;
	    }
	    
	    /**
	     * Adding an edge from source to destination node. 
	     * If edge already exists then the value would be updated 
	     *  
	     * @param source                    the source node.
	     * @param destination               the destination node.
	     * @param rate                    rate 
	     * @throws NullPointerException     if source or destination is null.
	     * @throws NoSuchElementException   if either source of destination does not exists. 
	     */
	    public void addEdge (T source, T destination, double rate) {
	        if (source == null || destination == null)
	            throw new NullPointerException("Source and Destination, both should be non-null.");
	       
	        if (!graph.containsKey(source) || !graph.containsKey(destination))
	            throw new NoSuchElementException("Source and Destination, both should be part of graph");
	        
	        
	        graph.get(source).put(destination, rate);
	    }
	    
	    /**
	     * Given a node, returns an immutable map of outgoing edges from that node
	     * 
	     * @param node The node whose edges should be queried.
	     * @return An immutable view of the edges leaving that node.
	     * @throws NullPointerException   when node is null.
	     * @throws NoSuchElementException when node is not in the graph.
	     */
	    public Map<T, Double> edgesFrom(T node) {
	        if (node == null)
	            throw new NullPointerException("The node should not be null.");
	        
	        Map<T, Double> edges = graph.get(node);
	        if (edges == null) 
	            throw new NoSuchElementException("Source node does not exist.");
	       
	        return Collections.unmodifiableMap(edges);
	    }

		@Override
		public String toString() {
			return "Graph [graph=" + graph + "]";
		}
		
		public Map<T, Map<T, Double>> valueOf(){
			return this.graph;
		}
	}
	    
	    /**
	     * Given a connected directed graph, find all paths between any two input points.
	     */
	    public final static class Pathfinder<T> {

	        private final Graph<T> graph;


	        public Pathfinder(Graph<T> graph) {
	            if (graph == null) throw new NullPointerException("The input graph cannot be null.");
	            
	            this.graph = graph;
	        }

	        /** Does what is says
	         * 
	         * @param source
	         * @param destination
	         */
	        private void validate (T source, T destination) {

	            if (source == null) throw new NullPointerException("The source: " + source + " cannot be  null.");
	           
	            if (destination == null) throw new NullPointerException("The destination: " + destination + " cannot be  null.");
	            
	            if (source.equals(destination)) throw new IllegalArgumentException("The source and destination: " + source + " cannot be the same.");
	            
	        }

	        /**
	         * Returns the list of paths, where path itself is a list of nodes.
	         * 
	         * @param source            the source node
	         * @param destination       the destination node
	         * @return                  List of all paths
	         */
	        public List<List<T>> getAllPaths(T source, T destination) {
	            validate(source, destination);

	            List<List<T>> paths = new ArrayList<List<T>>();
	            recurse(source, destination, new LinkedHashSet<T>(), paths);
	            return paths;
	        }

	       
	        private void recurse (T current, T destination, LinkedHashSet<T> accum, List<List<T>> result) {
	            accum.add(current);

	            //end case
	            if (current.equals(destination)) {
	                result.add(new ArrayList<T>(accum));
	                accum.remove(current);
	                return;
	            }

	            final Set<T> edges  = graph.edgesFrom(current).keySet();

	            for (T t : edges) {
	                if (!accum.contains(t)) { // ignore cycles.
	                    recurse (t, destination, accum, result);
	                }
	            }

	            accum.remove(current);
	        }    
		
	}

		public static Graph<String> getCurrenciesGraph() {
			return currenciesGraph;
		}

		
	
	
}
