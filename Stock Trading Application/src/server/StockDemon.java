package server;


import com.Stock;


public class StockDemon implements Runnable{
	
	Stock stock = new Stock();
		
	private void updatingStockList(){
		System.err.println("Server Updating stock list....");
		
		if( stock.saveStockDB() ){
			System.err.println("Updated successfully.");
		}
	}

	@Override
	public void run() {
		updatingStockList();
		
		while (true){
			try{
				//sleep for two minutes
				Thread.sleep(2 * 60 * 1000);
				updatingStockList();
				
			} 
			catch (Exception e) {	
				System.out.println("Exception from StockDemon: "+e.getMessage());
			}
		}
	}

}
