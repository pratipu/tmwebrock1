package com.thinking.machines.webrock;
import javax.servlet.*;
import javax.servlet.http.*;

public class RequestScope{
private HttpServletRequest request;

public RequestScope(HttpServletRequest request){
System.out.println("creating object of RequestScope type...");
this.request=request;
}

public void setAttribute(String key, Object value){
System.out.println("from setAttribute method of RequestScope class : key is : "+key+" value is : "+value);
request.setAttribute(key, value);
}

public Object getAttribute(String key){
Object value=null;
value=request.getAttribute(key);
System.out.println("from getAttribute of RequestScope : key : "+key+", and value : "+value);
return value;
}

}