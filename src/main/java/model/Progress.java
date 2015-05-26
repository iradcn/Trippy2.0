package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Progress {
	

	private int read;
	private int total;
	private static Lock lock = new ReentrantLock();
	
	public Progress(int total){
		this.read=0;
		this.total = total;
	}
	public static JSONObject Load(){
		JSONObject obj = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			//write converted json data to a file named "file.json"
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				FileReader fr = new FileReader("progress.json");
				obj = (JSONObject) parser.parse(fr);
				fr.close();
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 
		return obj;
	}
	public void Save(){
		JSONObject obj = new JSONObject();
		
		obj.put("read", this.getRead());
		obj.put("total", getTotal());
		try {
			//write converted json data to a file named "file.json"
			if (lock.tryLock(10, TimeUnit.SECONDS)){
				//write converted json data to a file named "file.json"
				FileWriter writer = new FileWriter("progress.json");
				writer.write(obj.toJSONString());
				writer.close();
			}
		}
		catch(InterruptedException e){
	           e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		
		} 


	}
	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
		this.Save();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	
}
