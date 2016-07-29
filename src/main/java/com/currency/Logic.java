/**
 * 
 */
package com.currency;

//import static com.currency.Currencies.buildGraph;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Sets.cartesianProduct;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.currency.Currencies.Graph;
import com.currency.Currencies.Pathfinder;

/**
 * @author sleyzerzon
 *
 */
public class Logic {
	
	
	private static final int PAIR_LENGTH = 6;
	//private Map<String, Double> pairsCache = new HashMap<>();
	
	public final static class Tuple{
		
		public static final int SPLIT_POINT = 3;
		
		public String first;
		public String second;
		
		public Tuple(String first, String second) {
			this.first = first;
			this.second = second;
		}
		
		public Tuple(String pair) {
			
			this.first = pair.substring(0,SPLIT_POINT);
			this.second = pair.substring(SPLIT_POINT, pair.length());
		}

		/*@Override
		public String toString() {
			return "Tuple [first=" + first + ", second=" + second + "]";
		}*/
		
		public String valueOf() {
			return this.first+this.second;
		}
	}
	
	
	public final static class RateTuple {
		
		public double num;
		public double denom;
		
		
		public RateTuple(double num, double denom) {
			this.num = num;
			this.denom = denom;
		}
	} 
	
	public String rate(String pair, Map<String, Double> pairsCache/*, Currencies currencies*/) {
		
		validate(pair);

		
		
		double result = 0;
		DecimalFormat formatter = new DecimalFormat("#.####");
		
		System.out.println("processing: "+pair);
		
		Tuple originalPair = new Tuple(pair);
		
		Double directValue = pairsCache.get(originalPair.valueOf());
		
		if(directValue != null) {
			result= directValue;
			return formatter.format(result);
		} 
		
		//try inversion first
		List<Double> rates = new ArrayList<>();
		
		boolean inverseInCache = pairsCache.get(originalPair.second+originalPair.first) != null;
		if(inverseInCache) {
			result = 1.0/(pairsCache.get(originalPair.second+originalPair.first));
			rates.add(result);
		}

		
		List<RateTuple> derived = findByDerivation(originalPair, pairsCache/*, currencies*/);
		
		if (derived.size()>0){
			
			for(RateTuple rate: derived) {
				rates.add(rate.num/rate.denom);
			}
			result = Collections.max(rates);
			//return formatter.format(result);
		} else /*if(rates.size()<0 && derived.size()<0)*/{
			Currencies currencies = new Currencies();
			Graph<String> graph = currencies.buildGraph(pairsCache);
			Pathfinder<String> pathfinder = new Pathfinder<String>(graph);
			String from = originalPair.first;
			String to = originalPair.second;
			List<List<String>> paths = pathfinder.getAllPaths(from, to);
			if(paths.size()>0) {
				List<Double> xrates = findByChaining(paths, graph);
				
				result = Collections.max(xrates);
				rates.add(result);
			}
			//return formatter.format(result);
			
		} 
		
		
		/*else {

			result = 1.0/(pairsCache.get(originalPair.second+originalPair.first));
			return formatter.format(result);
		}*/
		if (rates.size()<=0) return "Not available";
		return formatter.format(Collections.max(rates));
		
	}


	/**
	 * @param pair
	 */
	private void validate(String pair) {
		if(pair == null) throw new IllegalArgumentException();
		if(pair.length() != PAIR_LENGTH) throw new IllegalArgumentException();
	}
	
	
	@SuppressWarnings("unchecked")
	private List<RateTuple> findByDerivation(Tuple originalPair, Map<String, Double> ratesCache/*, Currencies currencies*/) {
		
		List<RateTuple> result = new ArrayList<>();
		
		Currencies currencies = new Currencies();currencies.buildCurrencies(ratesCache);
		
		Iterator<List<String>> firstIterator = cartesianProduct(of(originalPair.first), currencies.getCurrencies()).iterator();
		Iterator<List<String>> secondIterator =  cartesianProduct(of(originalPair.second), currencies.getCurrencies()).iterator();
		
		while(firstIterator.hasNext() && secondIterator.hasNext()) {
					
					List<String> firstDeriv = firstIterator.next();
					List<String> secondDeriv = secondIterator.next();
					
					String firstDerivValue = new Tuple(firstDeriv.get(0),firstDeriv.get(1)).valueOf();
					String secondDerivValue = new Tuple(secondDeriv.get(0),secondDeriv.get(1)).valueOf();
					
					
					System.out.println(new Tuple(firstDeriv.get(0),firstDeriv.get(1)).valueOf() +"=>"+ratesCache.get(firstDerivValue));
					System.out.println(new Tuple(secondDeriv.get(0),secondDeriv.get(1)).valueOf() +"=>"+ratesCache.get(secondDerivValue));
					
					
					if(ratesCache.get(firstDerivValue) != null && ratesCache.get(secondDerivValue) != null) {
						result.add(new RateTuple(ratesCache.get(firstDerivValue), ratesCache.get(secondDerivValue)));
					}
					
					/*if(ratesCache.get(firstDerivValue) != null && ratesCache.get(secondDerivValue) == null) {
						ratesCache.put(secondDerivValue, Double.valueOf(rate(secondDerivValue, ratesCache, currencies)));
					}*/
					
					System.out.println("both derivatives are present: " +(ratesCache.get(firstDerivValue) != null && ratesCache.get(secondDerivValue) != null)); 

				}
		
		return result;
	}



	private List<Double> findByChaining(List<List<String>> paths, Graph<String> currenciesGraph){
		
		List<Double> xrates = new ArrayList<>();
			
		for(Iterator<List<String>> pathsIt = paths.iterator(); pathsIt.hasNext();) {
				
				
				int counter = 0; 
				double coefficient = 1;
				List<String> curLst = pathsIt.next();
				for(Iterator<String > it = curLst.iterator(); it.hasNext();) {
					String key = it.next();
					System.out.println("key: " + key);
					int nxt = ++counter;
					if(nxt < curLst.size()) {
						String nextKey = curLst.get(nxt);
						System.out.println("next key: "+ nextKey);
						
						System.out.println("into: " + currenciesGraph.valueOf().get(key));
						
						double intoXrate = currenciesGraph.valueOf().get(key).get(nextKey);
						System.out.println(key+nextKey+" rate: " + currenciesGraph.valueOf().get(key).get(nextKey));
						coefficient *= intoXrate;
						
						
					}
					
				}
				System.out.println("final rate: "+coefficient);
				xrates.add(coefficient);
				System.out.println("rates: "+xrates);
				
				
				
		}
		return xrates;
	}

}
