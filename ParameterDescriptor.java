package com.thinking.machines.webrock.pojo;

public class ParameterDescriptor{
private boolean isRequestParameter;
private String requestParameterName;
private String parameterType;
private Class parameterTypeClass;

public void setParameterTypeClass(Class parameterTypeClass){
this.parameterTypeClass=parameterTypeClass;
}
public Class getParameterTypeClass(){
return this.parameterTypeClass;
}

public void setIsRequestParameter(boolean isRequestParameter){
this.isRequestParameter=isRequestParameter;
}
public boolean getIsRequestParameter(){
return this.isRequestParameter;
}

public void setRequestParameterName(String requestParameterName){
this.requestParameterName=requestParameterName;
}
public String getRequestParameterName(){
return this.requestParameterName;
}

public void setParameterType(String parameterType){
this.parameterType=parameterType;
}
public String getParameterType(){
return this.parameterType;
}



}