package com.thinking.machines.webrock;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.annotations.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import com.google.gson.*;

public class TMWebRock1 extends HttpServlet{

private String methodType;
private String contentType;
private String serviceClassType;
private String serviceMethodType;
Map<String, Service> serviceMap;
Set<String> serviceKeys;

public void doGet(HttpServletRequest request, HttpServletResponse response){
System.out.println("GET method of TMWebRock");
PrintWriter out=null;
try{
out=response.getWriter();
methodType=request.getMethod();
contentType=request.getContentType();
System.out.println("the method type is : "+methodType);
System.out.println("the content type is : "+contentType);
Enumeration headers=request.getHeaderNames();
String header=null;
System.out.println("header names are : \n-----------------------------------------------------------------");
while(headers.hasMoreElements()){
header=(String)headers.nextElement();
System.out.println(header+"........."+request.getHeader(header));
}

ServletContext application=getServletContext();
WebRockModel webRockModel=(WebRockModel)application.getAttribute("webRockModel");
serviceMap=webRockModel.getServiceMap();
serviceKeys=serviceMap.keySet();

//System.out.println(serviceKeys);
String servletPath=request.getServletPath();
String realPath=request.getRealPath("");
String queryString=request.getQueryString();
String requestURI=request.getRequestURI();
StringBuffer requestURL=request.getRequestURL();

//System.out.println("servlet path : "+servletPath);
//System.out.println("real path : "+realPath);
//System.out.println("queryString : "+queryString);
//System.out.println("request uri : "+requestURI);
//System.out.println("request url : "+requestURL.toString());
String key=null;

Iterator itr=serviceKeys.iterator();

while(itr.hasNext()){

String tempString=(String)itr.next();
if(requestURI.endsWith(tempString)){
key=tempString;
break;
}
}

if(key!=null){

Service service=serviceMap.get(key);
Class serviceClass=service.getServiceClass();
System.out.println("Class name is : "+serviceClass.getName());
Method method=service.getService();
Class serviceReturnType=null;
Object forwardEntity=null;
boolean firstProcessing=true;
Object returnedValue=null;

do{
if(firstProcessing){
returnedValue=processService(service, request, response);
serviceReturnType=method.getReturnType();
System.out.println("return type of "+method.getName()+" service of "+serviceClass.getName()+" service class is : "+serviceReturnType.getName());
firstProcessing =false;
}
forwardEntity=getForwardToService(service);
if(forwardEntity == null){
ReturnTypes serviceReturnTypeValue=service.getServiceReturnType();

switch(serviceReturnTypeValue){

case VOID:
response.setContentType("text/html");
out.println(returnedValue);
break;

case STRING:
response.setContentType("text/html");
out.println(returnedValue);
break;

case JSON:
System.out.println("returnTypeValue is json");
Gson gson=new Gson();
response.setContentType("application/json");
response.setCharacterEncoding("utf-8");
String jsonResponse=gson.toJson(returnedValue);
System.out.println(jsonResponse);
out.print(jsonResponse);
out.flush();
break;

case LONG:
response.setContentType("text/html");
out.println(returnedValue);
break;

case INT:
response.setContentType("text/html");
out.println(returnedValue);
break;

case SHORT:
response.setContentType("text/html");
out.println(returnedValue);
break;

case BYTE:
response.setContentType("text/html");
out.println(returnedValue);
break;

case FLOAT:
response.setContentType("text/html");
out.println(returnedValue);
break;

case DOUBLE:
response.setContentType("text/html");
out.println(returnedValue);
break;

case BOOLEAN:
response.setContentType("text/html");
out.println(returnedValue);
break;

case CHAR:
response.setContentType("text/html");
out.println(returnedValue);
break;
}
break;
}
if(forwardEntity instanceof Service){
service=(Service)forwardEntity;
System.out.println("forward to service is : class name : "+service.getServiceClass().getName()+", method name is : "+service.getService().getName());
returnedValue=processService(service, request, response);
serviceReturnType=service.getService().getReturnType();
System.out.println("return type of "+service.getService().getName()+" service of "+service.getServiceClass().getName()+" service class is : "+serviceReturnType.getName());
}else{
if(forwardEntity instanceof String){
String forwardUri=(String)forwardEntity;
System.out.println("forward to uri is : "+forwardUri);
RequestDispatcher requestDispatcher=request.getRequestDispatcher(forwardUri);
requestDispatcher.forward(request, response);
service = null;
}
}
}while(service != null);

}else{
try{
response.sendError(HttpServletResponse.SC_NOT_FOUND);
}catch(IOException ioException){
//do nothing
}
}//else block of key!= null 

}catch(Exception exception){
exception.printStackTrace();
System.out.println("catch of Exception of doGet TMWebRock1 : exception message : "+exception.getMessage());
try{
response.sendError(500, exception.getMessage());
}catch(IOException ioException){
//do nothing
}
}
}// end of doGet method

public void doPost(HttpServletRequest request, HttpServletResponse response){
System.out.println("POST method of TMWebRock");
doGet(request, response);
}

/****************************************************************************************/

public Object processService(Service service, HttpServletRequest request, HttpServletResponse response) throws Exception, ServiceException{

/***********************************/
boolean isClassSecuredAccess=service.getIsClassSecuredAccess();
boolean isServiceSecuredAccess=service.getIsServiceSecuredAccess();
Class checkPostClass=null;
Method guardMethod=null;
Object checkPostObject=null;

if(isClassSecuredAccess){

checkPostClass=service.getCheckPostClass();
guardMethod=service.getGuardMethod();
checkPostObject=checkPostClass.newInstance();

RequestScope requestScope=new RequestScope(request);
SessionScope sessionScope=new SessionScope(request.getSession());
ApplicationScope applicationScope=new ApplicationScope(getServletContext());
ApplicationDirectory applicationDirectory=new ApplicationDirectory(new File(getServletContext().getRealPath("")));
try{
guardMethod.invoke(checkPostObject, requestScope, sessionScope, applicationScope, applicationDirectory);
}catch(Exception exception){
throw new ServiceException("Invalid Access please login first!!!");
}
}else{
if(isServiceSecuredAccess){

checkPostClass=service.getCheckPostClass();
guardMethod=service.getGuardMethod();
checkPostObject=checkPostClass.newInstance();

RequestScope requestScope=new RequestScope(request);
SessionScope sessionScope=new SessionScope(request.getSession());
ApplicationScope applicationScope=new ApplicationScope(getServletContext());
ApplicationDirectory applicationDirectory=new ApplicationDirectory(new File(getServletContext().getRealPath("")));
try{
guardMethod.invoke(checkPostObject, requestScope, sessionScope, applicationScope, applicationDirectory);
}catch(Exception exception){
throw new ServiceException("Invalid Access please login first!!!");
}
}
}

/***********************************/
Object returnedValue=null;

int classRequestType=service.getClassRequestType();
switch(classRequestType){
case Service.REQUEST_TYPE_GET:
serviceClassType="GET";
break;
case Service.REQUEST_TYPE_POST:
serviceClassType="POST";
break;
case Service.REQUEST_TYPE_ANY:
serviceClassType="ANY";
break;
}

int methodRequestType=service.getMethodRequestType();
switch(methodRequestType){
case Service.REQUEST_TYPE_GET:
serviceMethodType="GET";
break;
case Service.REQUEST_TYPE_POST:
serviceMethodType="POST";
break;
case Service.REQUEST_TYPE_ANY:
serviceMethodType="ANY";
break;
}

boolean injectApplicationDirectory=service.getInjectApplicationDirectory();
boolean injectApplicationScope=service.getInjectApplicationScope();
boolean injectSessionScope=service.getInjectSessionScope();
boolean injectRequestScope=service.getInjectRequestScope();

Class serviceClass=service.getServiceClass();
Method method=service.getService();
Object o=serviceClass.newInstance();
System.out.println("Processing "+method.getName()+" service of "+serviceClass.getName()+" service class");
System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
System.out.println("service class type : "+serviceClassType+"... service method type : "+serviceMethodType);

/****************************/
if(injectRequestScope){
Method setRequestScope=serviceClass.getMethod("setRequestScope", RequestScope.class);
if(setRequestScope != null){
System.out.println("from TMWebRock servlet : setRequestScope is not null setting value by invoking method");
RequestScope requestScope=new RequestScope(request);
setRequestScope.invoke(o, requestScope);
}
}

/****************************/
if(injectSessionScope){
Method setSessionScope=serviceClass.getMethod("setSessionScope", SessionScope.class);
if(setSessionScope != null){
System.out.println("from TMWebRock servlet : setSessionScope is not null setting value by invoking method");
HttpSession session=request.getSession();
SessionScope sessionScope=new SessionScope(session);
setSessionScope.invoke(o, sessionScope);
}
}

/****************************/
if(injectApplicationScope){
Method setApplicationScope=serviceClass.getMethod("setApplicationScope", ApplicationScope.class);
if(setApplicationScope != null){
System.out.println("from TMWebRock servlet : setApplicationScope is not null setting value by invoking method");
ServletContext application=getServletContext();
ApplicationScope applicationScope=new ApplicationScope(application);
setApplicationScope.invoke(o, applicationScope);
}
}

/****************************/
if(injectApplicationDirectory){
Method setApplicationDirectory=serviceClass.getMethod("setApplicationDirectory", ApplicationDirectory.class);
if(setApplicationDirectory != null){
System.out.println("from TMWebRock servlet : setApplicationDirectory is not null setting value by invoking method");
ServletContext application=getServletContext();
ApplicationDirectory applicationDirectory=new ApplicationDirectory(new File(application.getRealPath("")));
setApplicationDirectory.invoke(o, applicationDirectory);
}
}
/****************************/

Map<String, FieldDescriptor> fieldDescription=service.getFieldDescription();
System.out.println("is fieldDescription null : "+(fieldDescription == null));
FieldDescriptor fieldDescriptor=null;
Field field=null;
String fieldName=null;
Method setterMethod=null;
Method getterMethod=null;
String fieldTypeName=null;
Class fieldType=null;
Object fieldValue=null;
boolean isAutoWired=false;
boolean isInjectRequestParameter=false;
AutoWired autoWired=null;
InjectRequestParameter injectRequestParameter=null;
String autoWiredAttribute=null;
String foundValueType;
String injectFieldRequestParameter=null;
int fieldCount=fieldDescription.size();
System.out.println("number of fields is : "+fieldCount);
Set<String> fieldNames=fieldDescription.keySet();
Iterator fieldItr=fieldNames.iterator();

while(fieldItr.hasNext()){
fieldName=(String)fieldItr.next();
fieldDescriptor=fieldDescription.get(fieldName);
field=fieldDescriptor.getField();
fieldTypeName=fieldDescriptor.getFieldTypeName();
fieldType=fieldDescriptor.getFieldType();
isAutoWired=fieldDescriptor.getIsAutoWired();
isInjectRequestParameter=fieldDescriptor.getIsInjectRequestParameter();

/************/
if(isAutoWired){
autoWired=field.getAnnotation(AutoWired.class);
autoWiredAttribute=fieldDescriptor.getAutoWiredAttributeName();
setterMethod=fieldDescriptor.getSetterMethod();
getterMethod=fieldDescriptor.getGetterMethod();

Object autowiredObject=request.getAttribute(autoWiredAttribute);
if(autowiredObject == null){
System.out.println("autowired object not found in request scope!");
autowiredObject=request.getSession().getAttribute(autoWiredAttribute);
if(autowiredObject == null){
System.out.println("autowired object not found in session scope!");
autowiredObject=getServletContext().getAttribute(autoWiredAttribute);
if(autowiredObject == null){
System.out.println("autowired object not found in application scope!");
getServletContext().setAttribute(autoWiredAttribute, null);
}else{
foundValueType=autowiredObject.getClass().getName();
System.out.println("autowired object found in application scope, its type is : "+autowiredObject.getClass().getName());
if(foundValueType.equals(fieldTypeName)){
setterMethod.invoke(o, fieldType.cast(autowiredObject));
}
}
}else{
foundValueType=autowiredObject.getClass().getName();
System.out.println("autowired object found in session scope, its type is : "+autowiredObject.getClass().getName());
if(foundValueType.equals(fieldTypeName)){
setterMethod.invoke(o, fieldType.cast(autowiredObject));
}
}
}else{
foundValueType=autowiredObject.getClass().getName();
System.out.println("autowired object found in request scope, its type is : "+foundValueType);
if(foundValueType.equals(fieldTypeName)){
setterMethod.invoke(o, fieldType.cast(autowiredObject));
}
}
}

/************/
if(isInjectRequestParameter){
injectRequestParameter=field.getAnnotation(InjectRequestParameter.class);
injectFieldRequestParameter=fieldDescriptor.getRequestParameterName();
setterMethod=fieldDescriptor.getSetterMethod();
getterMethod=fieldDescriptor.getGetterMethod();

String paramValue=request.getParameter(injectFieldRequestParameter);
if(paramValue != null){

switch(fieldTypeName){
case "java.lang.String":
fieldValue=(String)paramValue;
//field.set(o, paramValue);
setterMethod.invoke(o, fieldValue);
break;

case "long":
try{
fieldValue=Long.parseLong(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for data type long for field "+fieldName+" of service class "+serviceClass.getName());
}
setterMethod.invoke(o, fieldValue);
break;

case "int":
try{
fieldValue=Integer.parseInt(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for data type int for field "+fieldName+" of service class "+serviceClass.getName());
}
setterMethod.invoke(o, fieldValue);
break;

case "short":
try{
fieldValue=Short.parseShort(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for data type short for field "+fieldName+" of service class "+serviceClass.getName());
}
setterMethod.invoke(o, fieldValue);
break;

case "byte":
try{
fieldValue=Byte.parseByte(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for data type byte for field "+fieldName+" of service class "+serviceClass.getName());
}
setterMethod.invoke(o, fieldValue);
break;

case "double":
try{
fieldValue=Double.parseDouble(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for data type double for field "+fieldName+" of service class "+serviceClass.getName());
}
setterMethod.invoke(o, fieldValue);
break;

case "float":
try{
fieldValue=Float.parseFloat(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for data type float for field "+fieldName+" of service class "+serviceClass.getName());
}
setterMethod.invoke(o, fieldValue);
break;

case "boolean":
fieldValue=Boolean.parseBoolean(paramValue);
setterMethod.invoke(o, fieldValue);
break;

case "char":
fieldValue=paramValue.charAt(0);
setterMethod.invoke(o, fieldValue);
break;

default:
throw new ServiceException("invalid data type for a field with @InjectRequestParameter annotation : "+fieldTypeName);
}
}else{
//field.set(o, paramValue);
}
}
}

/***************************/

boolean isRequestParameter=false;
boolean validJsonService=true;
String parameterName=null;
String requestParameterName=null;
String parameterType=null;
Class parameterTypeClass=null;
Map<String, ParameterDescriptor> parameterDescription=null;
Object[] parametersToBePassed=null;
Set<String> parameterNames=null;
Iterator itr=null;
Parameter[] parameters=method.getParameters();
parameterDescription=service.getParameterDescription();
ParameterDescriptor parameterDescriptor=null;
int parameterCount=0;
if(parameterDescription != null){
parameterCount=parameterDescription.size();
}

if(parameterCount != 0){

System.out.println("service method has some parameters...");
System.out.println("service method has "+parameterCount+" parameters");
parameterNames=parameterDescription.keySet();
itr=parameterNames.iterator();
System.out.println("parameter description : \n----------------------------------------------");

if(contentType.equals("application/x-www-form-urlencoded")){

parametersToBePassed=new Object[parameterCount];
int count=0;

while(itr.hasNext()){
parameterName=(String)itr.next();
parameterDescriptor=parameterDescription.get(parameterName);
isRequestParameter=parameterDescriptor.getIsRequestParameter();
requestParameterName=parameterDescriptor.getRequestParameterName();
parameterType=parameterDescriptor.getParameterType();

if(isRequestParameter){
String paramValue=request.getParameter(requestParameterName);
System.out.print("name : "+requestParameterName+"... is request parameter : "+isRequestParameter+" type : "+parameterType);
System.out.println(" value is : "+paramValue);

switch(parameterType){
case "java.lang.String":
parametersToBePassed[count]=paramValue;
break;

case "long":
try{
parametersToBePassed[count]=Long.parseLong(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for request parameter "+requestParameterName+" of type "+parameterType+" of service "+method.getName()+"of service class "+serviceClass.getName());
}
break;

case "int":
try{
parametersToBePassed[count]=Integer.parseInt(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for request parameter "+requestParameterName+" of type "+parameterType+" of service "+method.getName()+"of service class "+serviceClass.getName());
}
break;

case "short":
try{
parametersToBePassed[count]=Short.parseShort(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for request parameter "+requestParameterName+" of type "+parameterType+" of service "+method.getName()+"of service class "+serviceClass.getName());
}
break;

case "byte":
try{
parametersToBePassed[count]=Byte.parseByte(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for request parameter "+requestParameterName+" of type "+parameterType+" of service "+method.getName()+"of service class "+serviceClass.getName());
}
break;

case "double":
try{
parametersToBePassed[count]=Double.parseDouble(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for request parameter "+requestParameterName+" of type "+parameterType+" of service "+method.getName()+"of service class "+serviceClass.getName());
}
break;

case "float":
try{
parametersToBePassed[count]=Float.parseFloat(paramValue);
}catch(NumberFormatException nfe){
throw new ServiceException("invalid value : "+paramValue+" for request parameter "+requestParameterName+" of type "+parameterType+" of service "+method.getName()+"of service class "+serviceClass.getName());
}
break;

case "boolean":
parametersToBePassed[count]=Boolean.parseBoolean(paramValue);
break;

case "char":
parametersToBePassed[count]=paramValue.charAt(0);
break;
}
}else{
System.out.println("");
System.out.println("name : "+parameterName+"... is request parameter : "+isRequestParameter+" type : "+parameterType);

switch(parameterType){
case "com.thinking.machines.webrock.RequestScope":
RequestScope requestScope=new RequestScope(request);
parametersToBePassed[count]=requestScope;
break;

case "com.thinking.machines.webrock.SessionScope":
HttpSession session=request.getSession();
SessionScope sessionScope=new SessionScope(session);
parametersToBePassed[count]=sessionScope;
break;

case "com.thinking.machines.webrock.ApplicationScope":
ServletContext application=getServletContext();
ApplicationScope applicationScope=new ApplicationScope(application);
parametersToBePassed[count]=applicationScope;
break;

case "com.thinking.machines.webrock.ApplicationDirectory":
ApplicationDirectory applicationDirectory=new ApplicationDirectory(new File(getServletContext().getRealPath("")));
parametersToBePassed[count]=applicationDirectory;
break;

default:
throw new ServiceException("invalid parameter type : "+parameterType+" in service method : "+method.getName()+" of service class : "+serviceClass.getName());
}
}
count++;
}
}else{
System.out.println("when content type is something else like json or multipart form data....");

if(contentType.equals("application/json")){

StringBuffer stringBuffer=new StringBuffer();
String temp=null;
String rawJson=null;
BufferedReader bufferedReader=request.getReader();

while(true){
temp=bufferedReader.readLine();
if(temp == null){
break;
}
stringBuffer.append(temp);
}
rawJson=stringBuffer.toString();
System.out.println(rawJson);

parametersToBePassed=new Object[parameterCount];
int count=0;

while(itr.hasNext()){

parameterName=(String)itr.next();
parameterDescriptor=parameterDescription.get(parameterName);
isRequestParameter=parameterDescriptor.getIsRequestParameter();
requestParameterName=parameterDescriptor.getRequestParameterName();
parameterType=parameterDescriptor.getParameterType();
parameterTypeClass=parameterDescriptor.getParameterTypeClass();

if(isRequestParameter){
throw new ServiceException("@InjectRequestParameter annotation is not applicable for service method parameters with user defined type : "+parameterType);
}

System.out.println("parameter type name : "+parameterType+"...parameter type class : "+parameterTypeClass.getName());

RequestScope requestScope=new RequestScope(request);
SessionScope sessionScope=new SessionScope(request.getSession());
ApplicationScope applicationScope=new ApplicationScope(getServletContext());
ApplicationDirectory applicationDirectory=new ApplicationDirectory(new File(getServletContext().getRealPath("")));

switch(parameterType){

case "com.thinking.machines.webrock.RequestScope":
parametersToBePassed[count]=requestScope;
break;

case "com.thinking.machines.webrock.SessionScope":
parametersToBePassed[count]=sessionScope;
break;

case "com.thinking.machines.webrock.ApplicationScope":
parametersToBePassed[count]=applicationScope;
break;

case "com.thinking.machines.webrock.ApplicationDirectory":
parametersToBePassed[count]=applicationDirectory;
break;

default:
if(validJsonService){
try{
parametersToBePassed[count]=new Gson().fromJson(rawJson, parameterTypeClass);
}catch(JsonSyntaxException jsonSyntaxException){
jsonSyntaxException.printStackTrace();
throw new ServiceException("json data sent from client does not match the underlying service parameter type : "+parameterType);
}
validJsonService=false;
}else{
throw new ServiceException("only one user defined parameter is allowed for services that accecpt json data from client side");
}
}
count++;
}

}
}
}
/**************************************************************************/
if(serviceClassType.equals("ANY")){
if(methodType.equals(serviceMethodType) || serviceMethodType.equals("ANY")){
/*********/
System.out.println("service "+method.getName()+" has "+parameterCount+" parameters");
if(parameterCount == 0){
System.out.println("service method has zero parameters...");
returnedValue=method.invoke(o);
}else{
System.out.println("service method has some parameters...");
System.out.println("calling service "+method.getName()+" of service class "+serviceClass.getName());
returnedValue=method.invoke(o, parametersToBePassed);
}
/*********/
}else{
try{
response.sendError(HttpServletResponse.SC_BAD_REQUEST, "method type of the request does not match with the type of the service : method type : "+methodType+" service type : "+serviceMethodType);
}catch(IOException ioException){
//do nothing
}
}
}else{
if(methodType.equals(serviceClassType)){

/*********/
System.out.println("service "+method.getName()+" has "+parameterCount+" parameters");
if(parameterCount == 0){
System.out.println("service method has zero parameters...");
returnedValue=method.invoke(o);
}else{
System.out.println("service method has some parameters...");
System.out.println("calling service "+method.getName()+" of service class "+serviceClass.getName());
returnedValue=method.invoke(o, parametersToBePassed);
}
/*********/

}else{
try{
response.sendError(HttpServletResponse.SC_BAD_REQUEST, "method type of the request does not match with the type of the service class : method type : "+methodType+" service class type : "+serviceClassType);
}catch(IOException ioException){
//do nothing
}
}
}
/***************************/
System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
return returnedValue;
}// end of processService method

/****************************************************************************************/

public Object getForwardToService(Service service){

Method method=service.getService();
Forward forwardAnnotation=null;
Service forwardToService=null;
String forwardToPath=null;
String forwardToAnnotationValue=null;
boolean isForwardToAnnotation=false;

isForwardToAnnotation=(method.getAnnotation(Forward.class) != null);
if(isForwardToAnnotation){

forwardAnnotation=method.getAnnotation(Forward.class);
forwardToAnnotationValue=forwardAnnotation.value();
Iterator iterator=serviceKeys.iterator();
while(iterator.hasNext()){
String tempStr=(String)iterator.next();
if(tempStr.equals(forwardToAnnotationValue)){
forwardToService=serviceMap.get(tempStr);
break;
}
}

if(forwardToService != null){
return forwardToService;
}else{
return forwardToAnnotationValue;
}
}else{
return null;
}
}//end of getForwardService() method

}//end of class