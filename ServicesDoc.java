package com.thinking.machines.webrock;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

public class ServicesDoc{

public static Map<String, Service> serviceMap=new LinkedHashMap<>();
public static Set<Service> runOnStartupServices=new TreeSet<Service>((s1, s2)->s1.getPriority()-s2.getPriority());
public static String servicePackagePrefixPath=null;

public static void main(String[] args){

//String applicationRoot=args[0];
//String classDirPath=applicationRoot+File.separator+"WEB-INF"+File.separator+"classes";
servicePackagePrefixPath=args[0];

File servicePackageRootDir=new File(servicePackagePrefixPath);

try{
scanDirectory(servicePackageRootDir);
System.out.println("there are "+serviceMap.size()+" services in the service package");

System.out.println("run on startup services are : "+runOnStartupServices);
WebRockModel webRockModel=new WebRockModel();
webRockModel.setServiceMap(serviceMap);
//config.getServletContext().setAttribute("webRockModel", webRockModel);

Iterator itr=runOnStartupServices.iterator();
while(itr.hasNext()){
//processService((Service)itr.next());
}

createServicesInfoPdf();

}catch(Exception exception){
exception.printStackTrace();
}
}
/***************************************************************************************************/
public static void scanDirectory(File directory) throws Exception{
File[] fileList=directory.listFiles();

String packageName;
BufferedReader fileReader=null;
PrintWriter fileWriter=null;
File serviceClassFile=null;
Class serviceClass=null;
Method[] serviceMethods=null;
//String servicePackagePrefix=config.getInitParameter("SERVICE_PACKAGE_PREFIX");
String serviceKey=null;
String classPathValue=null;
String methodPathValue=null;
String forwardToValue=null;
int priority=-1;

int i=-1;

for(File file:fileList){

if(file.isDirectory()){
scanDirectory(file);
}else{
String fileName=file.getName();
//System.out.println("file name is : "+fileName);
packageName=file.getPath();

if(fileName.indexOf(".class") != -1){

String className=fileName.substring(0, fileName.indexOf(".class"));

serviceClassFile=new File(className+".class");

fileReader=new BufferedReader(new FileReader(file));
fileWriter=new PrintWriter(serviceClassFile);

while(true){
i=fileReader.read();
if(i == -1){
fileReader.close();
break;
}
fileWriter.write(i);
}
fileWriter.close();
//System.out.println("serviceClassFile.exists() : "+serviceClassFile.exists());
System.out.println("class file path is : "+serviceClassFile.getPath());
System.out.println("package is : "+packageName+" length : "+packageName.length());
System.out.println("prefix path is  is : "+servicePackagePrefixPath+" length : "+servicePackagePrefixPath.length());
packageName=packageName.substring(servicePackagePrefixPath.length()+1);
packageName=packageName.substring(0, packageName.indexOf(fileName));
System.out.println("package name is : "+packageName);
String packageNameOrig=packageName.replace('\\','.');
System.out.println("original package name is : "+packageNameOrig);
System.out.println("fully qualified class name is : "+packageNameOrig+className);
serviceClass=Class.forName(packageNameOrig+className);

if(serviceClass.getAnnotation(Path.class) != null){

System.out.println("service class name is : "+serviceClass.getName()+"\n===========================================================================");

/***********************************************/

SecuredAccess securedAccess=null;
boolean isClassSecuredAccess=false;
boolean isServiceSecuredAccess=false;
String checkPostClassName=null;
String guardMethodName=null;
Class checkPostClass=null;
Method guardMethod=null;

isClassSecuredAccess=(serviceClass.getAnnotation(SecuredAccess.class) != null);
System.out.println("isClassSecuredAccess : "+isClassSecuredAccess);
if(isClassSecuredAccess){
securedAccess=(SecuredAccess)serviceClass.getAnnotation(SecuredAccess.class);
checkPostClassName=securedAccess.checkPost();
guardMethodName=securedAccess.guard();
System.out.println("checkPostName : "+checkPostClassName+" ..... guardName : "+guardMethodName);
checkPostClass=Class.forName(checkPostClassName);
guardMethod=checkPostClass.getMethod(guardMethodName, RequestScope.class, SessionScope.class, ApplicationScope.class, ApplicationDirectory.class);
System.out.println("checkPostClass : "+checkPostClass.getName()+" ..... guardMethod() : "+guardMethod.getName());
}

/***********************************************/

Field[] fields=serviceClass.getDeclaredFields();
String fieldName=null;
String fieldTypeName=null;
Class fieldType=null;
boolean isInjectRequestParameter=false;
boolean isAutoWired=false;
String requestFieldParameterName=null;
String autoWiredAttributeName=null;
String setterMethodName=null;
String getterMethodName=null;
Method setterMethod=null;
Method getterMethod=null;
InjectRequestParameter injectRequestParameter=null;
AutoWired autoWired=null;
Map<String, FieldDescriptor> fieldDescription=new LinkedHashMap<String, FieldDescriptor>();
FieldDescriptor fieldDescriptor=null;
int fieldCount=fields.length;
System.out.println("service class "+serviceClass.getName()+" has "+fieldCount+" fields");
System.out.println("fields details : \n------------------------------------------------------------------------");

if(fieldCount != 0){

for(Field field:fields){
fieldDescriptor=new FieldDescriptor();
fieldName=field.getName();
fieldTypeName=field.getType().getName();
/*************/
switch(fieldTypeName){
case "long":
fieldType=long.class;
break;

case "int":
fieldType=int.class;
break;

case "short":
fieldType=short.class;
break;

case "byte":
fieldType=byte.class;
break;

case "double":
fieldType=double.class;
break;

case "float":
fieldType=float.class;
break;

case "boolean":
fieldType=boolean.class;
break;

case "char":
fieldType=char.class;
break;

default:
fieldType=Class.forName(fieldTypeName);
}

/*************/
isInjectRequestParameter=(field.getAnnotation(InjectRequestParameter.class) != null);
isAutoWired=(field.getAnnotation(AutoWired.class) != null);

if(isInjectRequestParameter){
injectRequestParameter=field.getAnnotation(InjectRequestParameter.class);
requestFieldParameterName=injectRequestParameter.name();
setterMethodName=injectRequestParameter.setter();
getterMethodName=injectRequestParameter.getter();
setterMethod=serviceClass.getMethod(setterMethodName, fieldType);
getterMethod=serviceClass.getMethod(getterMethodName);
}

if(isAutoWired){
autoWired=field.getAnnotation(AutoWired.class);
autoWiredAttributeName=autoWired.name();
setterMethodName=autoWired.setter();
getterMethodName=autoWired.getter();
setterMethod=serviceClass.getMethod(setterMethodName, fieldType);
getterMethod=serviceClass.getMethod(getterMethodName);
}
System.out.println("name : "+fieldName+"...type : "+fieldTypeName+"...isInjectRequestParameter : "+isInjectRequestParameter+"...requestFieldParameterName : "+requestFieldParameterName+" isAutoWired : "+isAutoWired+" ... autoWiredAttributeName : "+autoWiredAttributeName);
fieldDescriptor.setField(field);
fieldDescriptor.setFieldTypeName(fieldTypeName);
fieldDescriptor.setFieldType(fieldType);
fieldDescriptor.setIsInjectRequestParameter(isInjectRequestParameter);
fieldDescriptor.setSetterMethodName(setterMethodName);
fieldDescriptor.setGetterMethodName(getterMethodName);
fieldDescriptor.setSetterMethod(setterMethod);
fieldDescriptor.setGetterMethod(getterMethod);
fieldDescriptor.setIsAutoWired(isAutoWired);
fieldDescriptor.setRequestParameterName(requestFieldParameterName);
fieldDescriptor.setAutoWiredAttributeName(autoWiredAttributeName);
fieldDescription.put(fieldName, fieldDescriptor);
}
System.out.println("size of fieldDescription map is : "+fieldDescription.size());
}

/***********************************************/

Path path=(Path)serviceClass.getAnnotation(Path.class);
classPathValue=path.value();
//System.out.println(serviceClass.getName()+" has Path annotation applied on");
serviceMethods=serviceClass.getDeclaredMethods();

boolean injectApplicationDirectory=(serviceClass.getAnnotation(InjectApplicationDirectory.class) != null);
boolean injectApplicationScope=(serviceClass.getAnnotation(InjectApplicationScope.class) != null);
boolean injectSessionScope=(serviceClass.getAnnotation(InjectSessionScope.class) != null);
boolean injectRequestScope=(serviceClass.getAnnotation(InjectRequestScope.class) != null);

boolean getAppliedOnClass=(serviceClass.getAnnotation(GET.class) != null);
boolean postAppliedOnClass=(serviceClass.getAnnotation(POST.class) != null);

System.out.println("the application class "+serviceClass.getName()+" has the following injection annotations : ");
System.out.println("ApplicationDirectory : "+injectApplicationDirectory+"...ApplicationScope : "+injectApplicationScope+"...SessionScope : "+injectSessionScope+"... RequestScope : "+injectRequestScope);

for(Method method:serviceMethods){

if(method.getAnnotation(Path.class) != null){
System.out.println(method.getName()+" of "+serviceClass.getName()+" is a service");

path=(Path)method.getAnnotation(Path.class);
methodPathValue=path.value();
serviceKey=classPathValue+methodPathValue;
System.out.println(serviceKey);

boolean isReturnType=(method.getAnnotation(ServiceReturnType.class) != null);
ServiceReturnType serviceReturnType=null;
ReturnTypes returnTypeValue=null;
if(isReturnType){
serviceReturnType=(ServiceReturnType)method.getAnnotation(ServiceReturnType.class);
returnTypeValue=serviceReturnType.value();
}

Service service=new Service();
service.setServiceClass(serviceClass);
service.setService(method);
service.setPath(serviceKey);

System.out.println("setting field description of service class "+serviceClass.getName()+" has "+fieldDescription.size()+" fields");
service.setFieldDescription(fieldDescription);

service.setServiceReturnType(returnTypeValue);
service.setIsClassSecuredAccess(isClassSecuredAccess);
service.setCheckPostClassName(checkPostClassName);
service.setGuardMethodName(guardMethodName);
service.setCheckPostClass(checkPostClass);
service.setGuardMethod(guardMethod);
/********************/
isServiceSecuredAccess=(method.getAnnotation(SecuredAccess.class) != null);
System.out.println("isServiceSecuredAccess : "+isServiceSecuredAccess);
service.setIsServiceSecuredAccess(isServiceSecuredAccess);

if(isServiceSecuredAccess){
securedAccess=(SecuredAccess)method.getAnnotation(SecuredAccess.class);
checkPostClassName=securedAccess.checkPost();
guardMethodName=securedAccess.guard();
System.out.println("service checkPostName : "+checkPostClassName+" .....service guardName : "+guardMethodName);
checkPostClass=Class.forName(checkPostClassName);
guardMethod=checkPostClass.getMethod(guardMethodName, RequestScope.class, SessionScope.class, ApplicationScope.class, ApplicationDirectory.class);
System.out.println("service checkPostClass : "+checkPostClass.getName()+" .....service guardMethod() : "+guardMethod.getName());

service.setCheckPostClassName(checkPostClassName);
service.setGuardMethodName(guardMethodName);
service.setCheckPostClass(checkPostClass);
service.setGuardMethod(guardMethod);
}
/********************/

service.setInjectApplicationDirectory(injectApplicationDirectory);
service.setInjectApplicationScope(injectApplicationScope);
service.setInjectSessionScope(injectSessionScope);
service.setInjectRequestScope(injectRequestScope);

if(method.getAnnotation(Forward.class) != null){
System.out.println(method.getName()+" of  "+serviceClass.getName()+" has got Forward annotation applied on it");
Forward forward=(Forward)method.getAnnotation(Forward.class);
forwardToValue=forward.value();
service.setForwardTo(forwardToValue);
}

if(method.getAnnotation(OnStartup.class) != null){
Class returnType=method.getReturnType();
String methodReturnType=returnType.getName();
Class[] methodParameterTypes=method.getParameterTypes();
System.out.println(method.getName()+" of  "+serviceClass.getName()+" has got OnStartup annotation applied on it and its return type is : "+methodReturnType+" and it has "+methodParameterTypes.length+" parameters");

if(methodReturnType.equals("void") && (methodParameterTypes.length == 0)){
OnStartup onStartup=(OnStartup)method.getAnnotation(OnStartup.class);
priority=onStartup.priority();
service.setRunOnStartup(true);
service.setPriority(priority);
runOnStartupServices.add(service);
}else{
System.out.println("method "+method.getName()+" of  "+serviceClass.getName()+" does not deserve to be included in the runOnStartupServices set as has got OnStartup annotation applied on it and its return type is : "+methodReturnType+" and it has "+methodParameterTypes.length+" parameters");
}
}

boolean getAppliedOnMethod=(method.getAnnotation(GET.class) != null);
boolean postAppliedOnMethod=(method.getAnnotation(POST.class) != null);

if(getAppliedOnClass){
service.setClassRequestType(Service.REQUEST_TYPE_GET);
}
if(postAppliedOnClass){
service.setClassRequestType(Service.REQUEST_TYPE_POST);
}
if((getAppliedOnClass == false) && (postAppliedOnClass == false)){
service.setClassRequestType(Service.REQUEST_TYPE_ANY);
}

if(getAppliedOnMethod){
service.setMethodRequestType(Service.REQUEST_TYPE_GET);
}
if(postAppliedOnMethod){
service.setMethodRequestType(Service.REQUEST_TYPE_POST);
}
if((getAppliedOnMethod == false) && (postAppliedOnMethod == false)){
service.setMethodRequestType(Service.REQUEST_TYPE_ANY);
}

System.out.println("class request type : "+service.getClassRequestType());
System.out.println("method request type : "+service.getMethodRequestType());


Parameter[] parameters=method.getParameters();
System.out.println("request parameter information : \n--------------------------------------------");
int parameterCount=method.getParameterCount();
System.out.println("method.getParameterCount() : "+parameterCount+" parameters.length : "+parameters.length);
String parameterName=null;
boolean isRequestParameter=false;
RequestParameter requestParameter=null;
String requestParameterName=null;
String parameterType=null;
Map<String, ParameterDescriptor> parameterDescription=new LinkedHashMap<String, ParameterDescriptor>();
ParameterDescriptor parameterDescriptor;

if(parameterCount != 0){

for(Parameter parameter:parameters){

parameterDescriptor=new ParameterDescriptor();
isRequestParameter=(parameter.getAnnotation(RequestParameter.class) != null);
parameterName=parameter.getName();

if(isRequestParameter){
requestParameter=parameter.getAnnotation(RequestParameter.class);
requestParameterName=requestParameter.value();
parameterType=parameter.getType().getName();
System.out.println("name : "+parameter.getName()+"is a request parameter : request variable name : "+requestParameterName+" parameter type : "+parameterType);
parameterDescriptor.setIsRequestParameter(isRequestParameter);
parameterDescriptor.setRequestParameterName(requestParameterName);
parameterDescriptor.setParameterType(parameterType);
parameterDescriptor.setParameterTypeClass(parameter.getType());
parameterDescription.put(parameterName, parameterDescriptor);
}else{
System.out.println("name : "+parameter.getName()+"is a not a request parameter :  parameter type : "+parameterType);
parameterType=parameter.getType().getName();
parameterType=parameter.getType().getName();
parameterDescriptor.setIsRequestParameter(isRequestParameter);
parameterDescriptor.setRequestParameterName(null);
parameterDescriptor.setParameterType(parameterType);
parameterDescriptor.setParameterTypeClass(parameter.getType());
parameterDescription.put(parameterName, parameterDescriptor);
}
}
service.setParameterDescription(parameterDescription);
}
serviceMap.put(serviceKey, service);
}
}
}else{
//System.out.println(serviceClass.getName()+" does not have Path annotation applied on");
}

}// end of if block .class file 

}// end of else block for is Directory

}// end of for loop on file array
//System.out.println("from scan method size of the map is : "+serviceMap.size());
}// end of scanDirectory method

/*******************************************************************************/

public static void createServicesInfoPdf(){
try{
String filePath="F:\\thinking machines\\classes\\bobby.pdf";
PdfWriter pdfWriter=new PdfWriter(filePath);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument);
String para=null;
para="Services Information";
Paragraph paragraph=new Paragraph(para);
document.add(paragraph);
Table table=null;
Cell cell=null;
float[] dimensions={350f, 350f};

Set<String> serviceKeys=serviceMap.keySet();
Iterator itr=serviceKeys.iterator();
String serviceKey=null;
Service service=null;

while(itr.hasNext()){

serviceKey=(String)itr.next();
service=serviceMap.get(serviceKey);

table=new Table(dimensions);
table.setMarginBottom(10f);

cell=new Cell();
paragraph=new Paragraph("Service Path");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(service.getPath());
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/
cell=new Cell();
paragraph=new Paragraph("Service Method Name");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(service.getService().getName());
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Service Class Name");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(service.getServiceClass().getName());
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Service Return Type");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getServiceReturnType()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Forward To : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
String forwardTo="";
forwardTo=service.getForwardTo();
if(forwardTo==null) forwardTo="";
paragraph=new Paragraph(forwardTo);
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Is Class Secured Access : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getIsClassSecuredAccess()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Is Service Secured Access : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getIsServiceSecuredAccess()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Check Post Class : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
Class checkPostClass=service.getCheckPostClass();
String checkPostClassName="";
if(checkPostClass != null){
checkPostClassName=checkPostClass.getName();
}
paragraph=new Paragraph(checkPostClassName);
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Check Post Method : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
Method guardMethod=service.getGuardMethod();
String guardMethodName="";
if(guardMethod != null){
guardMethodName=guardMethod.getName();
}
paragraph=new Paragraph(guardMethodName);
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Request Scope Dependency : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getInjectRequestScope()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Session Scope Dependency : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getInjectSessionScope()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Application Scope Dependency : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getInjectApplicationScope()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Application Directory Dependency : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getInjectApplicationDirectory()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Is Run on Startup Service : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getRunOnStartup()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Priority : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();
paragraph=new Paragraph(String.valueOf(service.getPriority()));
cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Service Class Request Type : ");
cell.add(paragraph);
table.addCell(cell);
cell=new Cell();

switch(service.getClassRequestType()){
case 0:
paragraph=new Paragraph("ANY");
break;

case 1:
paragraph=new Paragraph("GET");
break;

case -1:
paragraph=new Paragraph("POST");
break;
}

cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Service Request Type : ");
cell.add(paragraph);
table.addCell(cell);
cell=new Cell();

switch(service.getMethodRequestType()){
case 0:
paragraph=new Paragraph("ANY");
break;

case 1:
paragraph=new Paragraph("GET");
break;

case -1:
paragraph=new Paragraph("POST");
break;
}

cell.add(paragraph);
table.addCell(cell);
/*****************************************************/

cell=new Cell();
paragraph=new Paragraph("Service Parameters : ");
cell.add(paragraph);
table.addCell(cell);

cell=new Cell();

Table tbl=new Table(new float[]{55f, 55f, 55f, 55f});
Map<String, ParameterDescriptor> parameterDescription=service.getParameterDescription();
Cell paramCell=null;

if(parameterDescription != null){
Set parameterKeySet=parameterDescription.keySet();
Iterator paramKeyItr=parameterKeySet.iterator();
String parameterName=null;
ParameterDescriptor parameterDescriptor=null;

paramCell=new Cell();
paramCell.add(new Paragraph("ParameterName"));
tbl.addCell(paramCell);

paramCell=new Cell();
paramCell.add(new Paragraph("Parameter Type"));
tbl.addCell(paramCell);

paramCell=new Cell();
paramCell.add(new Paragraph("Is Request Parameter"));
tbl.addCell(paramCell);

paramCell=new Cell();
paramCell.add(new Paragraph("Request Parameter Name"));
tbl.addCell(paramCell);

while(paramKeyItr.hasNext()){
parameterName=(String)paramKeyItr.next();
parameterDescriptor=parameterDescription.get(parameterName);

paramCell=new Cell();
paramCell.add(new Paragraph(parameterName));
tbl.addCell(paramCell);

paramCell=new Cell();
paramCell.add(new Paragraph(parameterDescriptor.getParameterType()));
tbl.addCell(paramCell);

paramCell=new Cell();
paramCell.add(new Paragraph(String.valueOf(parameterDescriptor.getIsRequestParameter())));
tbl.addCell(paramCell);

String requestParameterName="Not Applicable";
if(parameterDescriptor.getIsRequestParameter()){
requestParameterName=parameterDescriptor.getRequestParameterName();
}
paramCell=new Cell();
paramCell.add(new Paragraph(requestParameterName));
tbl.addCell(paramCell);

}
}else{
paramCell=new Cell();
paramCell.add(new Paragraph("service has no parameters"));
tbl.addCell(paramCell);
}

cell.add(tbl);
table.addCell(cell);
/*****************************************************/

document.add(table);
}

document.close();
System.out.println("Service information pdf generated");

}catch(Exception e){
e.printStackTrace();
}
}

}//end of servlet class 