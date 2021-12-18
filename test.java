import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Ece_Kazan_2017510049 {
	public static void main(String[] args) {

		 int x=25; 
		 int p=2; 
		 int d=2; 
		 int B=100;
	 	 int c=6;
	 	 int t=2;
		 
         System.out.println("DP Results: "+(DynamicProfit(B,c,t,x) - DynamicCost(p,d,x)));
         System.out.println("Greedy Results: "+(GreedyProfit(B,c,t,x) - GreedyCost(p,d,x)));
	}

	/*
	 * Dynamic program for cost
	 * In the dynamic program, it aims to produce the vehicles requested at the least cost after the xth month. 
	 * In doing so, he calculates how much the cost would be if he produced more cars for the next months and kept them in the garage. 
     * These costs are all included in the two-dimensional series according to the number of cars in the garage.
	 */
    public static int DynamicCost(int p,int d,int x) {
      	 int[] month_demand=month_demand_read(x);
   	 	 int total_demand=0;
  		 int []garage_cost=garage_cost_read();
  		 
  		 for(int i=0;i<month_demand.length;i++)
  			total_demand=total_demand+month_demand[i];
  		 
		 int[][]cost=new int[x+1][total_demand+1];

		 for(int month=0;month<cost.length;month++) { 
			 if(month==0) {
				 for(int i=0;i<cost[month].length;i++)
					 cost[month][i]=garage_cost[i];
			 }
			 else {
				 for(int demand=0;demand<cost[month].length;demand++) { 
					int tempMin=Integer.MAX_VALUE;				
					for(int temp_demand=0;temp_demand<cost[month].length;temp_demand++) {
						if(month_demand[month]==p) {
							if(demand<temp_demand) {
								cost[month][demand]=tempMin;
								break;
							}								
							else{
								int result=cost[month-1][temp_demand]+(demand-temp_demand)*d+garage_cost[demand];
								if(result<tempMin) {
									tempMin=result;
									cost[month][demand]=tempMin;
								}
							}
						}
						else if(month_demand[month]>p){
							if(month_demand[month]-p+demand<temp_demand) {
								cost[month][demand]=tempMin;
								break;
							}								
							else{
								int result=cost[month-1][temp_demand]+(month_demand[month]-p-temp_demand+demand)*d+garage_cost[demand];
								if(result<tempMin) {
									tempMin=result;
									cost[month][demand]=tempMin;
								}
							}
						}
						else if(month_demand[month]<p) {
							if(p-month_demand[month]+demand<temp_demand) {
								cost[month][demand]=tempMin;
								break;
							}								
							else{
								int result=cost[month-1][temp_demand]+(temp_demand)*d+garage_cost[demand];
								if(result<tempMin) {
									tempMin=result;
									cost[month][demand]=tempMin;
								}
							}
						}
				    }
				}
			}
		}
    	return (cost[x][0]);
    }
		
    /*
     * Greedy algorithm for cost
     * Since the Greedy algorithm thinks instantly, in this problem, no more cars are produced to put cars in the garage or more than desired. 
     * Because there will be garage or worker costs. Since Greedy algorithm does not think of the future, 
     * it is sufficient if the desired number of vehicles is produced at that moment.
     * So the cost was kept in a one-dimensional array.
     */
    public static int GreedyCost(int p,int d,int x){
    	 int[] month_demand=month_demand_read(x);
   		 int[] garage_cost=garage_cost_read();	 
   		 
		 int[]cost=new int[x+1];
		 cost[0]=0;
		 for(int month=1;month<month_demand.length;month++){			
			 if(month_demand[month]>p) 
				cost[month]=cost[month-1]+(month_demand[month]-p)*d;
			 else
				cost[month]=cost[month-1];
   		 }
    	return (cost[cost.length-1]);
    }
    
    /*
     *  Dynamic program for profit
     *  When it is the next month in the dynamic program algorithm, all investment holds additional high profits from companies. 
     *  It calculates the best profit for that month for all companies and calculates these values in a two-dimensional array.
     */
    public static double DynamicProfit(int B,int c,int t,int x) {
    	 int[] month_demand=month_demand_read(x);
		 double[][]profit=new double[x+1][c+1];
		 int[][]investment=investment_read(x,c);
		 double tempMax=0;
		 
		 for(int month=1;month<investment.length;month++) {
			 for(int invst=1;invst<investment[month].length;invst++) {
				 if(month==1) 
					 profit[month][invst]=(double)(((double)(B*month_demand[month])/2)*(double)investment[month][invst]/100+(B*month_demand[month])/2);
				 else{
					 double maxProfit=0;
					  tempMax=0;
					 for(int cc=1;cc<investment[month].length;cc++) {
						 if(invst==cc) 
							 tempMax=(double)((double)(profit[month-1][cc]+B*month_demand[month-1]/2+B*month_demand[month]/2)*investment[month][cc]/100+
							          (double)(profit[month-1][cc]+B*month_demand[month-1]/2+B*month_demand[month]/2));
						 else 
							 tempMax=(double)((double)((profit[month-1][cc]-profit[month-1][cc]*t/100+B*month_demand[month-1]/2+B*month_demand[month]/2)*investment[month][invst]/100)+
						             (double)((profit[month-1][cc]-profit[month-1][cc]*t/100+B*month_demand[month-1]/2+B*month_demand[month]/2)));
						 if(maxProfit<tempMax)
							 maxProfit=tempMax;
					 }
					 profit[month][invst]=maxProfit;
				 }
			 }
		 }
		 double findMaxProfit=0;
		 for(int tem=1;tem<c+1;tem++) {
			 if(profit[x][tem]>findMaxProfit)
				 findMaxProfit=profit[x][tem];
		 }
    	 return (double)(findMaxProfit+B*month_demand[month_demand.length-1]/2);
    }
    
    /*
     *  Greedy algorithm for profit
     *  Since the greedy algorithm makes an instant decision, it switches to the investment with the highest profit for that month.
     *  When the next month is passed, as a result of the profit to be obtained from the investment companies,
     *  the total profit goes to the investment company, without taking into account the next months.
     *  Therefore, one-dimensional array is sufficient when calculating with greedy algorithm.
     */
    public static double GreedyProfit(int B,int c,int t,int x) {
    	 int[] month_demand=month_demand_read(x);
   		 double[]profit=new double[x+1];
   		 profit[0]=0;
   		 int[][]investment=investment_read(x,c);
   		 int tempInvst=0;
		 int maxInvst=0;
		 
   		 for(int month=1;month<investment.length;month++){
   			double tempProfit=0.0;
   			double maxProfit=0;
   			 for(int invst=1;invst<investment[month].length;invst++){							 
   				 if(month==1) {
   					tempProfit=(double)(((double)(B*month_demand[month])/2)*investment[month][invst]/100+(double)(B*month_demand[month])/2);
   					if(tempProfit>maxProfit) {
   						maxProfit=(double)tempProfit;
   						tempInvst=invst;
   					}
   					maxInvst=tempInvst;	
   				 }
   				 else {
   					 if(maxInvst==invst) 
   						tempProfit=(double)(profit[month-1]+B*month_demand[month-1]/2+B*month_demand[month]/2)*(double)investment[month][invst]/100+
   							 (double)(profit[month-1]+B*month_demand[month-1]/2+(double)B*month_demand[month]/2);   					 
   					 else 
   						tempProfit=(double)((double)((profit[month-1]-profit[month-1]*t/100+B*month_demand[month-1]/2+B*month_demand[month]/2)*investment[month][invst]/100)+
   							  (double)((profit[month-1]-profit[month-1]*t/100+(double)B*month_demand[month-1]/2+B*month_demand[month]/2)));  					 
   					 if(tempProfit>maxProfit) {
   						maxProfit=(double)tempProfit;
   						tempInvst=invst;
   					 }
   				 }
   			 }
   			maxInvst=tempInvst;
			profit[month]=(double)maxProfit; 
   		 }
    	return (double)(profit[profit.length-1]+B*month_demand[month_demand.length-1]/2);
    }
    
    /*
     *  "garage_cost.txt" was read and placed in the array in this method
     */
	public static int[] garage_cost_read(){
        String str;
        int arrlen=0;
		try (BufferedReader readerDelimiter = Files.newBufferedReader(Paths.get("garage_cost.txt"))) {
            while ((str = readerDelimiter.readLine()) != null) 
            	arrlen++;
        }
		catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
		
		int[]garage_cost=new int[arrlen];
		int i=0;
		try (BufferedReader readerDelimiter = Files.newBufferedReader(Paths.get("garage_cost.txt"))) {

            while ((str = readerDelimiter.readLine()) != null) {
            	if(i==0) 
            		garage_cost[i]=0;
            	else {
            		String [] line=str.split("\t");
            		garage_cost[i]=Integer.parseInt(line[1]);
            	}
            	i++;
            }
            return garage_cost;
        }
		catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
		return null;	
	}
	
	/*
	 *  "investment.txt" was read and placed in the two-dimensional array in this method
	 */
	public static int[][] investment_read(int x,int c) {
		try (BufferedReader readerDelimiter = Files.newBufferedReader(Paths.get("investment.txt"))) {
            String str;
            int[][]investment=new int[x+1][c+1];
            int i=0;
            while ((str = readerDelimiter.readLine()) != null) {
            	if(i==x+1)
            		return investment;
            	if(i==0) {
            		for(int j=0;j<c+1;j++)
            			investment[i][j]=0;
            	}
            	else {
            		String[] s=str.split("\t");
            		investment[i][0]=0;
            		for(int j=1;j<c+1;j++)
            			investment[i][j]=Integer.parseInt(s[j]);
            	}
            	i++;
            }
            return investment;
        }
		catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
		return null;
	}
	
	/*
	 *  "month_demand.txt" was read and placed in the array in this method
	 */
	public static int[] month_demand_read(int x) {
		try (BufferedReader readerDelimiter = Files.newBufferedReader(Paths.get("month_demand.txt"))) {
            String str;
            int[]month_demand=new int[x+1];
            int i=0;
            while ((str = readerDelimiter.readLine()) != null) {
            	if(month_demand.length==i)
            		return month_demand;
            	if(i==0)
            		month_demand[i]=0;
            	else {
            		String[] s=str.split("\t");
            		month_demand[i]=Integer.parseInt(s[1]);
            	}
            	i++;
            }
            return month_demand;
          }
		catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
		return null;
	}
}