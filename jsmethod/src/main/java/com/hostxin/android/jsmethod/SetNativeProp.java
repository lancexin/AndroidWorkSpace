package com.hostxin.android.jsmethod;


import org.json.JSONException;
import org.json.JSONObject;

public class SetNativeProp extends JsMethod {

	public SetNativeProp(JsMethodManager manager) {
		super(manager);
		setMethodName("setNativeProp");
		setHasCallback(false);
	}

	@Override
	public void exec(String callbackId, String data) throws JsExctption {
		try {
			JSONObject o = new JSONObject(data);
			String name = null;
			String value = null;
			if (o.has("name")) {
				name = o.getString("name");
			}else{
				throw new JsExctption("can not find name !");
			}
			if (o.has("value")) {
				value = o.getString("value");
			}else{
				throw new JsExctption("can not find value !");
			}

			if(value.equals("undefined")){
				value = "";
			}

			
			o.put("success", true);
			getManager().methodCallback(callbackId,o.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JsExctption("paruse json object error!");
		}
	}
}
