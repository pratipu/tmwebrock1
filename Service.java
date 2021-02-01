package com.thinking.machines.webrock.pojo;
import com.thinking.machines.webrock.annotations.*;
import java.lang.reflect.*;
import java.util.*;

public class Service{

public static final int REQUEST_TYPE_ANY=0;
public static final int REQUEST_TYPE_GET=1;
public static final int REQUEST_TYPE_POST=-1;

private Class serviceClass;
private Method service;
private String path;

private ReturnTypes serviceReturnType;
private String forwardTo;
private boolean isGetAppliedOnClass;
private boolean isPostAppliedOnClass;
private boolean isGetAppliedOnMethod;
private boolean isPostAppliedOnMethod;
private int classRequestType;
private int methodRequestType;
private boolean runOnStartup;
private int priority;
private boolean injectApplicationDirectory;
private boolean injectApplicationScope;
private boolean injectSessionScope;
private boolean injectRequestScope;
private boolean hasRequestParameter;
private boolean isClassSecuredAccess;
private boolean isServiceSecuredAccess;
private String checkPostClassName;
private Class checkPostClass;
private String guardMethodName;
private Method guardMethod;
private Map<String, ParameterDescriptor> parameterDescription;
private Map<String, FieldDescriptor> fieldDescription;


public void setServiceReturnType(ReturnTypes serviceReturnType){
this.serviceReturnType=serviceReturnType;
}
public ReturnTypes getServiceReturnType(){
return this.serviceReturnType;
}

public void setCheckPostClassName(String checkPostClassName){
this.checkPostClassName=checkPostClassName;
}
public String getCheckPostClassName(){
return this.checkPostClassName;
}

public void setCheckPostClass(Class checkPostClass){
this.checkPostClass=checkPostClass;
}
public Class getCheckPostClass(){
return this.checkPostClass;
}

public void setGuardMethodName(String guardMethodName){
this.guardMethodName=guardMethodName;
}
public String getGuardMethodName(){
return this.guardMethodName;
}

public void setGuardMethod(Method guardMethod){
this.guardMethod=guardMethod;
}

public Method getGuardMethod(){
return this.guardMethod;
}

public void setIsClassSecuredAccess(boolean isClassSecuredAccess){
this.isClassSecuredAccess=isClassSecuredAccess;
}
public boolean getIsClassSecuredAccess(){
return this.isClassSecuredAccess;
}

public void setIsServiceSecuredAccess(boolean isServiceSecuredAccess){
this.isServiceSecuredAccess=isServiceSecuredAccess;
}
public boolean getIsServiceSecuredAccess(){
return this.isServiceSecuredAccess;
}

public void setFieldDescription(Map<String, FieldDescriptor> fieldDescription){
this.fieldDescription=fieldDescription;
}
public Map<String, FieldDescriptor> getFieldDescription(){
return this.fieldDescription;
}


public void setHasRequestParameter(boolean hasRequestParameter){
this.hasRequestParameter=hasRequestParameter;
}
public boolean getHasRequestParameter(){
return this.hasRequestParameter;
}

public void setParameterDescription(Map<String, ParameterDescriptor> parameterDescription){
this.parameterDescription=parameterDescription;
}
public Map<String, ParameterDescriptor> getParameterDescription(){
return this.parameterDescription;
}


public void setClassRequestType(int classRequestType){
this.classRequestType=classRequestType;
}
public int getClassRequestType(){
return this.classRequestType;
}

public void setMethodRequestType(int methodRequestType){
this.methodRequestType=methodRequestType;
}
public int getMethodRequestType(){
return this.methodRequestType;
}

public void setInjectRequestScope(boolean injectRequestScope){
this.injectRequestScope=injectRequestScope;
}
public boolean getInjectRequestScope(){
return this.injectRequestScope;
}

public void setInjectSessionScope(boolean injectSessionScope){
this.injectSessionScope=injectSessionScope;
}
public boolean getInjectSessionScope(){
return this.injectSessionScope;
}

public void setInjectApplicationScope(boolean injectApplicationScope){
this.injectApplicationScope=injectApplicationScope;
}
public boolean getInjectApplicationScope(){
return this.injectApplicationScope;
}

public void setInjectApplicationDirectory(boolean injectApplicationDirectory){
this.injectApplicationDirectory=injectApplicationDirectory;
}
public boolean getInjectApplicationDirectory(){
return this.injectApplicationDirectory;
}

public void setPriority(int priority){
this.priority=priority;
}
public int getPriority(){
return this.priority;
}

public void setRunOnStartup(boolean runOnStartup){
this.runOnStartup=runOnStartup;
}
public boolean getRunOnStartup(){
return this.runOnStartup;
}

public void setIsGetAppliedOnClass(boolean isGetAppliedOnClass){
this.isGetAppliedOnClass=isGetAppliedOnClass;
}
public boolean getIsGetAppliedOnClass(){
return this.isGetAppliedOnClass;
}

public void setIsPostAppliedOnClass(boolean isPostAppliedOnClass){
this.isPostAppliedOnClass=isPostAppliedOnClass;
}
public boolean getIsPostAppliedOnClass(){
return this.isPostAppliedOnClass;
}

public void setIsGetAppliedOnMethod(boolean isGetAppliedOnMethod){
this.isGetAppliedOnMethod=isGetAppliedOnMethod;
}
public boolean getIsGetAppliedOnMethod(){
return this.isGetAppliedOnMethod;
}

public void setIsPostAppliedOnMethod(boolean isPostAppliedOnMethod){
this.isPostAppliedOnMethod=isPostAppliedOnMethod;
}
public boolean getIsPostAppliedOnMethod(){
return this.isPostAppliedOnMethod;
}

public void setServiceClass(Class serviceClass){
this.serviceClass=serviceClass;
}
public Class getServiceClass(){
return this.serviceClass;
}

public void setService(Method service){
this.service=service;
}
public Method getService(){
return this.service;
}

public void setPath(String path){
this.path=path;
}
public String getPath(){
return this.path;
}

public void setForwardTo(String forwardTo){
this.forwardTo=forwardTo;
}
public String getForwardTo(){
return this.forwardTo;
}

public String toString(){
return this.service.getName();
}

}