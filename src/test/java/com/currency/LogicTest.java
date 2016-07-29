package com.currency;

import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Sets.cartesianProduct;
import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.currency.Logic.Tuple;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class LogicTest {

	
	private static Currencies currencies;
	private static Set<String> tradeableCurrencies;
	private static Map<String, Double> ratesCache;
	
	@BeforeClass
	public static void init() {

		currencies = new Currencies();
		tradeableCurrencies = new HashSet<>();
		tradeableCurrencies.add("EUR");
		tradeableCurrencies.add("USD");
		tradeableCurrencies.add("NOK");
		tradeableCurrencies.add("CHF");
		tradeableCurrencies.add("SEK");
		tradeableCurrencies.add("JPY");
		tradeableCurrencies.add("MXN");
		tradeableCurrencies.add("BRL");
		tradeableCurrencies.add("CAD");
		tradeableCurrencies.add("GBP");
		System.out.println(tradeableCurrencies);
		
		currencies.setCurrencies(tradeableCurrencies);
		
		 ratesCache = new HashMap<>();
		 ratesCache.put("EURUSD", 1.0621);
		 ratesCache.put("GBPUSD", 1.4928);
		 ratesCache.put("NOKCHF", 1.3701);
		 ratesCache.put("SEKCHF", 1.3373);
		 ratesCache.put("USDJPY", 121.19);
		 ratesCache.put("USDMXN", 15.376);
		 ratesCache.put("BRLMXN", 4.9344);
		 ratesCache.put("JPYCAD", 0.010461);
		 ratesCache.put("NOKSEK", 1.0281);
	}
	
	
	@Test
	public void testExistingPair() {

		Logic logic = new Logic();
		//logic.setCache(ratesCache);
		assertEquals(ratesCache.get("EURUSD").toString(), logic.rate(new Tuple("EURUSD").valueOf(),ratesCache, currencies));
		
	}
	
	@Test
	public void testSEKNOKPair() {

		Logic logic = new Logic();
		
		Tuple pair = new Tuple("SEKNOK");
		
		logic.rate(pair.valueOf(), ratesCache, currencies);
	
		assertEquals("0.9761", logic.rate(pair.valueOf(), ratesCache, currencies));
	}	
	
	@Test
	public void testBRLUSDPair() {

		Logic logic = new Logic();
		
		logic.rate("BRLUSD", ratesCache, currencies);
	
		assertEquals("0.3209", logic.rate("BRLUSD", ratesCache, currencies));
	}	
	
	/*@Test
	public void testJPYUSDPair() {

		Logic logic = new Logic();
		//logic.setCache(ratesCache);
		logic.rate(new Tuple("JPYUSD").valueOf(), ratesCache, currencies);
		

		Set<List<String>> firstCartesian = Sets.cartesianProduct(ImmutableSet.of("JPY"), currencies.getCurrencies());
		Set<List<String>> secondCartesian = Sets.cartesianProduct(ImmutableSet.of("USD"), currencies.getCurrencies());
		
		Iterator firstIterator = firstCartesian.iterator();
		
		Iterator secondIterator = secondCartesian.iterator();
		
		
		System.out.println(firstCartesian);
		System.out.println(secondCartesian);
		
	}	*/
	
	
	@Test
	public void testJPYUSDPair() {

		Logic logic = new Logic();
		
		logic.rate("JPYUSD", ratesCache, currencies);
	
		assertEquals("0.0083", logic.rate("JPYUSD", ratesCache, currencies));
		
	}
	
	@Test
	public void testNOKSEKPair() {

		Logic logic = new Logic();
		assertEquals("1.0281", logic.rate("NOKSEK",ratesCache, currencies));
		
	}
	
	@Test
	public void testEURJPYPair() {

		Logic logic = new Logic();
		assertEquals("128.7159", logic.rate("EURJPY",ratesCache, currencies));
		
	}
	
	@Test
	public void testEURCADPair() {

		Logic logic = new Logic();
		assertEquals("1.3465", logic.rate("EURCAD",ratesCache, currencies));
		
	}	
	
	@Test
	public void testNotAvailablePair() {

		Logic logic = new Logic();
		assertEquals("Not available", logic.rate("USDAUD",ratesCache, currencies));
		
	}	
	
	

}
