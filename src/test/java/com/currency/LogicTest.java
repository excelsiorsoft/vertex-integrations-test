package com.currency;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.currency.Logic.Tuple;

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
		assertEquals(ratesCache.get("EURUSD").toString(), logic.rate(new Tuple("EURUSD").valueOf(),ratesCache));
		
	}
	
	@Test
	public void testSEKNOKPair() {

		Logic logic = new Logic();
		
		Tuple pair = new Tuple("SEKNOK");
		
		logic.rate(pair.valueOf(), ratesCache);
	
		assertEquals("0.9761", logic.rate(pair.valueOf(), ratesCache));
	}	
	
	@Test
	public void testBRLUSDPair() {

		Logic logic = new Logic();
	
		assertEquals("0.3209", logic.rate("BRLUSD", ratesCache));
	}	
	
	
	
	
	@Test
	public void testJPYUSDPair() {

		Logic logic = new Logic();

		assertEquals("0.0083", logic.rate("JPYUSD", ratesCache));
		
	}
	
	@Test
	public void testNOKSEKPair() {

		Logic logic = new Logic();
		assertEquals("1.0281", logic.rate("NOKSEK",ratesCache));
		
	}
	
	@Test
	public void testEURJPYPair() {

		Logic logic = new Logic();
		assertEquals("128.7159", logic.rate("EURJPY",ratesCache));
		
	}
	
	@Test
	public void testEURCADPair() {

		Logic logic = new Logic();
		assertEquals("1.3465", logic.rate("EURCAD",ratesCache));
		
	}	
	
	@Test
	public void testNotAvailablePair() {

		Logic logic = new Logic();
		assertEquals("Not available", logic.rate("USDAUD",ratesCache));
		
	}	
	
	

}
