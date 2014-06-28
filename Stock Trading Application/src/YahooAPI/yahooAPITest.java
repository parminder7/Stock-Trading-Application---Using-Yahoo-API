package YahooAPI;

import static org.junit.Assert.*;

import org.junit.Test;
import YahooAPI.*;

public class yahooAPITest {

	@Test
	public void withValidValues() {
		//fail("Not yet implemented");
		yahooStock stock = new yahooStock();
		float obj = stock.getQuote("GOOG");
		if(obj <= 0.0)
		{
			fail("Invalid Returned: 0.0");
		}
		//System.out.println(obj);	
	}

	@Test
	public void withInvalidValues1() {
		//fail("Not yet implemented");
		yahooStock stock = new yahooStock();
		float obj = stock.getQuote("GO0OG");
		if(obj > 0.0)
		{
			fail("Invalid Returned: 0.0");
		}
		
	}
	

}
