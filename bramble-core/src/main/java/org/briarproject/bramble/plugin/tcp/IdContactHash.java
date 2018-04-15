package org.briarproject.bramble.plugin.tcp;

import java.util.HashMap;

/**
 * Created by winterhart on 4/9/18.
 * Ensure to share Contact ID and Contact Name to TCP process
 */

public class IdContactHash extends HashMap {
	private static IdContactHash instance = null;
	protected IdContactHash(){};

	public static IdContactHash getInstance(){
		if(instance == null){
			instance= new IdContactHash();
		}
		return instance;
	}
}
