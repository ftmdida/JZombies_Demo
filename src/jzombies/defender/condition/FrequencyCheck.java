package jzombies.defender.condition;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import jzombies.Request;

/**
 * This class will verify if the number of requests per user
 * less than certain number within one second.
 * @author User-Zero
 *
 */
public class FrequencyCheck extends SubCondition{
	int maxNumberOfReqests;
	Object mylock;
	HashMap<String,Integer> reqCounter; 
	/**
	 * 
	 * @param maxNumberOfReqests The maximum number of requests per 
	 * user within one second.
	 */
	public FrequencyCheck(int maxNumberOfReqests) {
		// TODO Auto-generated constructor stub
		mylock = new Object();
		reqCounter = new HashMap<String,Integer>();
		this.maxNumberOfReqests = maxNumberOfReqests;
		
		Timer timer = new Timer();  //At this line a new Thread will be created
	    timer.schedule(new ScheduledTask(mylock,reqCounter), 1000); //delay in milliseconds
	}
	
	public class ScheduledTask extends TimerTask {
		Object mylock;
		HashMap<String, Integer> reqCounter;
		
		public ScheduledTask(Object mylock, HashMap<String, Integer> reqCounter ) {
			// TODO Auto-generated constructor stub
			this.mylock = mylock;
			this.reqCounter = reqCounter;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (mylock) {	
				reqCounter.clear();
			}
		}
		
	}
	
	@Override
	public boolean check(Request r) {
		// TODO Auto-generated method stub
		synchronized (mylock) {
			if(reqCounter.containsKey(r.getSourceIP())){
				int numberOfReq = reqCounter.get(r.getSourceIP());
				if(numberOfReq >= maxNumberOfReqests | numberOfReq+1 >= maxNumberOfReqests)
					return false;
				
				reqCounter.put(r.getSourceIP(), ++numberOfReq);
			}else
				reqCounter.put(r.getSourceIP(),1);
		}
		return true;
	}

}
