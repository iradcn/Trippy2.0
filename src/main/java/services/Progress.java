package services;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Progress {
	

	private static int read;
	private static int total;
	private static boolean inProgress;
	private static boolean error;
	private static Lock lock = new ReentrantLock();

	private int local_read;
	private int local_total_read;
	private boolean local_status_instance;
	private boolean local_error_instance;
	
	private Progress(){
		
		this.local_read = read;
		this.local_total_read = total;
		this.local_status_instance = inProgress;
		this.local_error_instance = error;
	}
	
	
	public static void initGlobal(){
		read = 0;
		total = 0;
		inProgress = false;
		error = false;
	}
	
	public static boolean startTrackingLoading(){
		boolean success = false;
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS))
			{
				if (inProgress)
					success = false;
				else{
					read=0;
					total = 0;
					inProgress = true;
					error = false;
					success = true;
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		return success;
	}
	
	public static Progress getStatus(){
		Progress progrs = null;
		
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				progrs = new Progress();
				
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 
		return progrs;
	}
	public boolean isError(){
		return this.local_error_instance;
	}
	public void save(){
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				read = this.getLocal_read();
				total = this.getLocal_total_read();
				inProgress = this.isLocal_status_instance();
				error = this.isError();
				
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 
	}
	public void setFinish(){
		this.local_read=0;
		this.local_error_instance=false;
		this.local_total_read=0;
		this.local_status_instance=false;
		
		this.save();

	}
	public void setError(){
		this.local_read=0;
		this.local_error_instance=true;
		this.local_total_read=0;
		this.local_status_instance=false;
		
		this.save();
	}
	public int getLocal_read() {
		return local_read;
	}


	public void setLocal_read(int local_read) {
		this.local_read = local_read;
	}


	public int getLocal_total_read() {
		return local_total_read;
	}


	public void setLocal_total_read(int local_total_read) {
		this.local_total_read = local_total_read;
	}


	public boolean isLocal_status_instance() {
		return local_status_instance;
	}


	public void setLocal_status_instance(boolean local_status_instance) {
		this.local_status_instance = local_status_instance;
	}

}
