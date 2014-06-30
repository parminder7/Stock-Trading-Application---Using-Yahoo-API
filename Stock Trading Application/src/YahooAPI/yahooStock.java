package YahooAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class yahooStock implements yahooAPI 
{
	public float getQuote(String stockName)
	{
		try 
		{
			URL yahooURL = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + stockName + "&f=sl1d1t1c1ohgv&e=.csv");
			URLConnection conn = yahooURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String readResponse = "";
			
			
			if((readResponse = in.readLine() )!=null)
			{
				String[] readStockInfo = readResponse.split(",");
                //yahooStockBean stockInfo = new yahooStockBean();
                //stockInfo.setStockName(readStockInfo[0].replaceAll("\"", ""));
                //stockInfo.setStockPrice(Float.valueOf(readStockInfo[1]));
                //return stockInfo;
				return Float.valueOf(readStockInfo[1]);
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Internal URL Problem.");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception encountered while retrieving stock price.");
			//e.printStackTrace();
		}
		
		return -1;
	}
}
