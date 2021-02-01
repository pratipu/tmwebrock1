package com.thinking.machines.webrock;
import javax.servlet.*;
import javax.servlet.http.*;

public class SessionScope{
private HttpSession session;

public SessionScope(HttpSession session){
this.session=session;
}

public void setAttribute(String key, Object value){
System.out.println("from setAttribute method of SessionScope : setting value in session scope ...");
session.setAttribute(key, value);
}

public Object getAttribute(String key){
Object value=null;
value=session.getAttribute(key);
return value;
}

}