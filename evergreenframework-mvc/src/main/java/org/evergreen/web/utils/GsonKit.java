package org.evergreen.web.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonKit implements ExclusionStrategy{
	
	String[] keys;

	public GsonKit(String[] keys) {
		this.keys = keys;
	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes arg0) {
		for (String key : keys) {
			if(key.equals(arg0.getName())){
				return true;
			}
		}
		return false;
	}

}
