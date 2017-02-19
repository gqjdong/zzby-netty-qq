package qq.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserSessionMap<K,T> {
	
	Map<K,T> userSession = new HashMap<K,T>();
	
	Map<String,K> userMap = new HashMap<String,K>();
	
	Lock lock = new ReentrantLock();
	
	public T put(K key,T value,String cn){
		lock.lock();
		try{
			if(!userMap.containsKey(cn)){
				userMap.put(cn, key);
				return userSession.put(key, value);
			}
			return userSession.put(userMap.get(cn), value);
		}finally{
			lock.unlock();
		}
	}
	
	public T get(K key){
		lock.lock();
		try{
			return userSession.get(key);
		}finally{
			lock.unlock();
		}
	}
	
	public T get(String cn){
		lock.lock();
		try{
			return userSession.get(userMap.get(cn));
		}finally{
			lock.unlock();
		}
	}
	
	public void remove(String cn){
		lock.lock();
		try{
			userSession.remove(userMap.get(cn));
			userMap.remove(cn);
		}finally{
			lock.unlock();
		}
	}
	
	public void remove(T value){
		lock.lock();
		try{
			for(K key : userSession.keySet()){
				if(userSession.get(key) == value){
					userSession.remove(key);
					for(String cn : userMap.keySet()){
						if(userMap.get(cn) == key){
							userMap.remove(cn);
						}
					}
				}
			}
		}finally{
			lock.unlock();
		}
	}
	
	public int size(){
		lock.lock();
		try{
			return userSession.size();
		}finally{
			lock.unlock();
		}
	}
	
	public boolean containsKey(String cn){
		lock.lock();
		try{
			return userSession.containsKey(userMap.get(cn));
		}finally{
			lock.unlock();
		}
	}
	
	public Set<K> keySet(){
		lock.lock();
		try{
			return userSession.keySet();
		}finally{
			lock.unlock();
		}
	}
	
}
