package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;

public class FieldDescriptor{

private Field field;
private String fieldTypeName;
private Class fieldType;
private boolean isInjectRequestParameter;
private boolean isAutoWired;
private String requestParameterName;
private String autoWiredAttributeName;
private String setterMethodName;
private String getterMethodName;
private Method setterMethod;
private Method getterMethod;


public void setFieldType(Class fieldType){
this.fieldType=fieldType;
}
public Class getFieldType(){
return this.fieldType;
}

public void setSetterMethodName(String setterMethodName){
this.setterMethodName=setterMethodName;
}
public String getSetterMethodName(){
return this.setterMethodName;
}

public void setGetterMethodName(String getterMethodName){
this.getterMethodName=getterMethodName;
}
public String getGetterMethodName(){
return this.getterMethodName;
}

public void setSetterMethod(Method setterMethod){
this.setterMethod=setterMethod;
}
public Method getSetterMethod(){
return this.setterMethod;
}

public void setGetterMethod(Method getterMethod){
this.getterMethod=getterMethod;
}
public Method getGetterMethod(){
return this.getterMethod;
}

public void setField(Field field){
this.field=field;
}
public Field getField(){
return this.field;
}

public void setIsAutoWired(boolean isAutoWired){
this.isAutoWired=isAutoWired;
}
public boolean getIsAutoWired(){
return this.isAutoWired;
}

public void setAutoWiredAttributeName(String autoWiredAttributeName){
this.autoWiredAttributeName=autoWiredAttributeName;
}
public String getAutoWiredAttributeName(){
return this.autoWiredAttributeName;
}

public void setFieldTypeName(String fieldTypeName){
this.fieldTypeName=fieldTypeName;
}
public String getFieldTypeName(){
return this.fieldTypeName;
}

public void setIsInjectRequestParameter(boolean isInjectRequestParameter){
this.isInjectRequestParameter=isInjectRequestParameter;
}
public boolean getIsInjectRequestParameter(){
return this.isInjectRequestParameter;
}

public void setRequestParameterName(String requestParameterName){
this.requestParameterName=requestParameterName;
}
public String getRequestParameterName(){
return this.requestParameterName;
}

}