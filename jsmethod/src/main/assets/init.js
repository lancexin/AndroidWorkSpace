(function(){
	console.log("start load init.js");
	
	if(window.nativeExec){
		console.log("init.js has load so return");
		return false;
	}
	var eventCache = [];
	function str2obj(json){
		var temp = eval('('+json+')');
		return temp;
	};

	function obj2str(o){  
	    var r = [];  
	    if(typeof o =="string"){
	    	return o;  
	    } 	
	    if(typeof o =="undefined"){
	    	return "undefined";  
	    } 	
	    
	    if(typeof o == "object"){  
	        if(o===null){
	        	return "null";  
	        }else if(!o.sort){  
	            for(var i in o){
	            	r.push(i+":"+obj2str(o[i]));  
	            }  
	                
	            r="{"+r.join()+"}";  
	        }else{  
	            for(var i =0;i<o.length;i++){
	              r.push(obj2str(o[i]));
	            }    
	            r="["+r.join()+"]";  
	        }  
	        return r;  
	    }  
	    return o.toString();  
	};
	
	window.removeAtArray=function(dx,array) { 
	    if(dx == null || dx > this.length){
	    	return false;
	    } 
	    for(var i=0,n=0;i<array.length;i++) { 
	        if(array[i]!= array[dx]) { 
	            array[n++]=array[i]; 
	        } 
	    } 
	    array.length-=1;
	}; 
	
	
	window.findCallbackById = function(id){
		for(var c in eventCache){
			if(eventCache[c].callbackId == id){
				var b = eventCache[c];
				removeAtArray(c,eventCache);
				return b;
			}
		}
	};
	
	window.newCallbackId = function(){
		console.log("newCallbackId");
		return window._nativeMe.newCallbackId();
	};
	
	window.nativeExec = function (method,params,success,error){
		console.log("nativeExec");
		var cid = newCallbackId();
		var c = {};
		var p = obj2str(params);
		c.callbackId = cid;
		c.success = success;
		c.error = error;
		if(success != null){
			eventCache.push(c);
		}
		
		window._nativeMe.exec(method,p,c.callbackId);
	};
	
	
	window.nativeCallback = function(callbackId,c){
		var f = findCallbackById(callbackId);
		if(f != null){
			f.success(c);
		}
	};
	
	window.nativeError = function(callbackId,c){
		
		var f = findCallbackById(callbackId);
		if(f != null){
			f.error(c);
		}
	};


	window.setNativeProp = function(params,success,error){
		nativeExec("setNativeProp",params,success,error);
	};
	
	window.getNativeProp = function(params,success,error){
		nativeExec("getNativeProp",params,success,error);
	};
	
	window.closeBrowser = function(){
		nativeExec("closeBrowser");
	};
	
	if(window.onNativeJsReady){
		window.onNativeJsReady();
	};
	console.log("end load init.js");
	
})();