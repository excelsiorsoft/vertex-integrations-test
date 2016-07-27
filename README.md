# vertex-integrations-test

Problem – Currency Conversion
---------------------------------
© Vertex Integration LLC, 2015 

Background
---

Currencies can be bought and sold and the exchange rates are quoted in the Foreign
Exchange (‘FX’) Market. For instance, you can purchase Euros (EUR) with US Dollars (USD) at
a rate of 1.0621 Euros per Dollar (EURUSD=1.0621). Thus if you had USD 1000, you could
buy EUR 941.53. Exchange rates for the commonly traded currencies can change multiple
times per second per trading day.

There are approximately 160 global currencies traded in the market, and a trader may wish to
convert money between any two currencies. Not all conversions are directly quoted, and thus
the trader has to derive the exchange rate between the two currencies. There may be more
than one exchange rate for a stated currency pair depending on the path taken.

Problem
---

The trader wishes to have a program assist them with these calculations. Given a set of
currency pairs and rates (‘quotes’) the trader wishes to find the best rate for any arbitrary pair
of currencies.

Quotes will be provided and entered in the following format:

CUR1CUR2,RATE

(where CUR1 and CUR2 are 3 letter ISO currency codes). Quotes can be entered/loaded from
wherever you see fit but only in the format specified above.

Sample Input Data
===
EURUSD,1.0621

GBPUSD,1.4928

NOKCHF,1.3701

SEKCHF,1.3373


USDJPY,121.19

USDMXN,15.376

BRLMXN,4.9344

JPYCAD,0.010461

NOKSEK,1.0281

The trader will enter requests of the program in the following format:

CUR1CUR2<return>

and receive a response to 4DP of precision.

Expected Output
===
1. Trader asks for EURUSD, return 1.0621
2. Trader asks for USDAUD, return “Not available”
3. Trader asks for JPYUSD, return 0.0083
4. Trader asks for EURJPY, return 128.7159
5. Trader asks for EURCAD, return 1.3465
6. Trader asks for BRLUSD, return 0.3209
7. Trader asks for NOKSEK, return 1.0281
8. Trader asks for SEKNOK, return 0.9761

Deliverable
===
A set of Java source files (do not include any compiled class files or libraries).

