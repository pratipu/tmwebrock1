package com.thinking.machines.webrock;
import java.io.*;

public class ApplicationDirectory{
private File applicationDirectory;

public ApplicationDirectory(File applicationDirectory){
this.applicationDirectory=applicationDirectory;
}

public File getDirectory(){
return this.applicationDirectory;
}

}