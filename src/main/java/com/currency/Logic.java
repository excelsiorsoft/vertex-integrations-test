/**
 * 
 */
package com.currency;

import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Sets.cartesianProduct;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author sleyzerzon
 *
 */
public class Logic {
	
	
	private static final int PAIR_LENGTH = 6;
	//private Map<String, Double> pairsCache = new HashMap<>();
	
	public final static class Tuple{
		
		private static final int SPLIT_POINT = 3;
		
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
	
	public String rate(String pair, Map<String, Double> pairsCache, Currencies currencies) {
		
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

		
		List<RateTuple> derived = findByDerivation(originalPair, pairsCache, currencies);
		
		if (derived.size()>0){
			
			for(RateTuple rate: derived) {
				rates.add(rate.num/rate.denom);
			}
			result = Collections.max(rates);
			return formatter.format(result);
		} /*else {

			result = 1.0/(pairsCache.get(originalPair.second+originalPair.first));
			return formatter.format(result);
		}*/
		return formatter.format(result);
		
		
		
		
	}


	/**
	 * @param pair
	 */
	private void validate(String pair) {
		if(pair == null) throw new IllegalArgumentException();
		if(pair.length() != PAIR_LENGTH) throw new IllegalArgumentException();
	}
	
	
	@SuppressWarnings("unchecked")
	private List<RateTuple> findByDerivation(Tuple originalPair, Map<String, Double> ratesCache, Currencies currencies) {
		
		List<RateTuple> result = new ArrayList<>();
		
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


	/*public Map<String, Double> getCache() {
		return pairsCache;
	}


	public void setCache(Map<String, Double> cache) {
		this.pairsCache = cache;
	}*/
	

}
