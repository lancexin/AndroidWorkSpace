package com.hostxin.android.jsmethod;



import com.hostxin.android.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class GetNativeProp extends JsMethod {

	public GetNativeProp(JsMethodManager manager) {
		super(manager);
		setMethodName("getNativeProp");
		setHasCallback(false);
	}

	@Override
	public void exec(String callbackId, String data) throws JsExctption {
		String name = data;
		if(StringUtils.isEmpty(name)){
			throw new JsExctption("name can not be null!");
		}
		String value = "testvalue "+data;
		if(StringUtils.isEmpty(value)){
			throw new JsExctption("can not find prop for name:"+name);
		}
		
		try {
			JSONObject jso = new JSONObject();
			jso.put("name", name);
			jso.put("value", value);
			getManager().methodCallback(callbackId,jso.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JsExctption("set json prop error!");
		}
	}
}
