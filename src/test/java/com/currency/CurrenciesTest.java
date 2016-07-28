package com.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.currency.Currencies.Graph;
import com.currency.Currencies.Pathfinder;

public class CurrenciesTest {

	@SuppressWarnings("unused")
	@Test
	public void pathFindingTest() {
		
		Graph<String> currenciesGraph = new Graph<String>();
		currenciesGraph.addNode("EUR");
		currenciesGraph.addNode("USD");
		currenciesGraph.addNode("JPY");
		currenciesGraph.addNode("CAD");
		currenciesGraph.addNode("GBP");
		currenciesGraph.addNode("MXN");
		currenciesGraph.addNode("CHF");
		currenciesGraph.addNode("NOK");
		currenciesGraph.addNode("SEK");
		currenciesGraph.addNode("BRL");
		
		
		currenciesGraph.addEdge("EUR", "USD", 1.0621);
		currenciesGraph.addEdge("USD", "JPY", 121.19);
		currenciesGraph.addEdge("JPY", "CAD", 0.010461);
		
		currenciesGraph.addEdge("GBP", "USD", 1.4928);
		currenciesGraph.addEdge("USD", "MXN", 15.376);
		
		currenciesGraph.addEdge("BRL", "MXN", 4.9344);
		
		currenciesGraph.addEdge("NOK", "CHF", 1.3701);
		currenciesGraph.addEdge("NOK", "SEK", 1.0281);
		
		currenciesGraph.addEdge("SEK", "CHF", 1.3373);
		
		System.out.println("currenciesGraph:" +currenciesGraph);
		
		Pathfinder<String> pathfinder = new Pathfinder<String>(currenciesGraph);
		
		
		List<List<String>> eurCadPath = pathfinder.getAllPaths("EUR", "CAD");
		System.out.println("EURCAD full path: " + eurCadPath);
		
		//=======
		List<Double> xrates = new ArrayList<>();
		for(Iterator<List<String>> pathsIt = eurCadPath.iterator(); pathsIt.hasNext();) {
			
			
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
					System.out.println("final rate: "+coefficient);
					
				}
				
			}
			
			xrates.add(coefficient);
			System.out.println("rates: "+xrates);
		}
	}
	
	@Test
	public void buildCurrenciesViaBuilderTest() {
		
		Map<String, Double >ratesCache = new HashMap<>();
		 ratesCache.put("EURUSD", 1.0621);
		 ratesCache.put("GBPUSD", 1.4928);
		 ratesCache.put("NOKCHF", 1.3701);
		 ratesCache.put("SEKCHF", 1.3373);
		 ratesCache.put("USDJPY", 121.19);
		 ratesCache.put("USDMXN", 15.376);
		 ratesCache.put("BRLMXN", 4.9344);
		 ratesCache.put("JPYCAD", 0.010461);
		 ratesCache.put("NOKSEK", 1.0281);
		 
		 Currencies currencies = new Currencies();
		 Currencies.buildGraph(ratesCache);
		 System.out.println("ratesCache: "+currencies.getCurrencies());
		 System.out.println("curGraph: "+currencies.getCurrenciesGraph());
		 
		 Pathfinder<String> pathfinder = new Pathfinder<String>(currencies.getCurrenciesGraph());
			
			
			List<List<String>> eurCadPath = pathfinder.getAllPaths("EUR", "CAD");
			System.out.println("EURCAD full path: " + eurCadPath);
	}
	
	@Test
	public void buildCurrenciesTest() {
		
		Graph<String> currenciesGraph = new Graph<String>();
		currenciesGraph.addNode("EUR");
		currenciesGraph.addNode("USD");
		currenciesGraph.addNode("JPY");
		currenciesGraph.addNode("CAD");
		currenciesGraph.addNode("GBP");
		currenciesGraph.addNode("MXN");
		currenciesGraph.addNode("CHF");
		currenciesGraph.addNode("NOK");
		currenciesGraph.addNode("SEK");
		currenciesGraph.addNode("BRL");
		
		
		currenciesGraph.addEdge("EUR", "USD", 1.0621);
		currenciesGraph.addEdge("USD", "JPY", 121.19);
		currenciesGraph.addEdge("JPY", "CAD", 0.010461);
		
		currenciesGraph.addEdge("GBP", "USD", 1.4928);
		currenciesGraph.addEdge("USD", "MXN", 15.376);
		
		currenciesGraph.addEdge("BRL", "MXN", 4.9344);
		
		currenciesGraph.addEdge("NOK", "CHF", 1.3701);
		currenciesGraph.addEdge("NOK", "SEK", 1.0281);
		
		currenciesGraph.addEdge("SEK", "CHF", 1.3373);
		
		System.out.println("currenciesGraph:" +currenciesGraph);

		 
		 Pathfinder<String> pathfinder = new Pathfinder<String>(currenciesGraph);
			
			
			List<List<String>> eurCadPath = pathfinder.getAllPaths("EUR", "CAD");
			System.out.println("EURCAD full path: " + eurCadPath);
	}

}
