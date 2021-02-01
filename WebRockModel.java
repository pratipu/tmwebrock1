package com.thinking.machines.webrock.model;
import java.util.*;
import com.thinking.machines.webrock.pojo.Service;

public class WebRockModel{
private Map<String, Service> serviceMap;

public void setServiceMap(Map<String, Service> serviceMap){
this.serviceMap=serviceMap;
}
public Map<String, Service> getServiceMap(){
return this.serviceMap;
}

}