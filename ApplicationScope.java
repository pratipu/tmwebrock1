package com.thinking.machines.webrock;
import javax.servlet.*;
import javax.servlet.http.*;

public class ApplicationScope{
private ServletContext application;

public ApplicationScope(ServletContext application){
this.application=application;
}

public void setAttribute(String key, Object value){
System.out.println("from setAttribute method of ApplicationScope : setting value in Application scope ....");
application.setAttribute(key, value);
}

public Object getAttribute(String key){
Object value=null;
value=application.getAttribute(key);
return value;
}

}