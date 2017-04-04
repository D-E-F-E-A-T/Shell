package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Javier Riveros <walter.riveros@unillanos.edu.co>
 */
public class BasicData {
  private String currentRoute;
  
  private LinkedList<String> commands;
  private static int commandCounter = 1;
  
  private static final String COMMAND_NOT_FOUND = "El comando introducido no es válido";
  
  public BasicData() {
    this.commands = new LinkedList<>();
    this.currentRoute = System.getProperty("user.home");
  }
  
  public String processCommand(String c) {
    this.storeCommand(c);
    
    String response = null;
    String command = c.split(" ")[0].toLowerCase();
    System.out.println(command);
    
    switch (command) {
      case "ruta":
        response = this.currentRoute;
        break;
        
      case "historia":
        response = getCommandsList();
        break;
      
      case "ejecutar":
        response = execute(c);
        break;
        
      case "ir":
        String route = c.split(" ")[1];
        try {
          File f = new File(currentRoute.concat("/"+route));
          
          if (f.exists()) {
            this.currentRoute = f.getCanonicalPath();
            response = String.format("Estás en %s", this.getCurrentDirectory());
          } else {
            response = "La ruta especificada no existe";
          }
          
        } catch (IOException ex) {
          response = String.format("Ha ocurrido un error al intentar ir a %s", route);
        }

        break;
      
      case "directorio":
        response = execCommand(String.format("java -jar /bin/Directorio.jar '%s'", c.split(" ")[1]));
        break;
      
      case "informacion":
        response = execCommandd(String.format("java -jar /home/javier/Documentos/Informacion.jar '%s'", c.split(" ")[1]));
        break;
        
      case "salir":
        System.exit(0);
        break;
      
      default:
        response = COMMAND_NOT_FOUND;
    }
    
    return response;
  }
  
  private String execCommandd(String c) {
    String info = "";
    try {
      Process awk = new ProcessBuilder("/home/javier/jdk1.8.0_111/bin/java", c).start();
      System.out.println(awk.getErrorStream());
      
      InputStream inputStream = awk.getErrorStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String s;
      while ((s = br.readLine()) != null)
        info += s + "\n";
    } catch(IOException e) {
      System.out.println(e.getMessage());
    }
    return info;
  }
  
  private String execute(String p) {
    String[] c = p.split(" ");
    String s = fusionar(p.split(c[0]));
    return execCommand(s);
  }
  
  private String fusionar(String[] c) {
    String s = "";
    for(String y : c)
      s += y;
    return s;
  }
  
  private String execCommand(String c) {
    String info = "";
    
    try {
      Process p = Runtime.getRuntime().exec(c);
      InputStream inputStream = p.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String s;
      while ((s = br.readLine()) != null)
        info += s + "\n";
      
    } catch(IOException e) {
      return e.getMessage();
    }
    
    return info;
  }
  
  private void storeCommand(String c) {
    this.commands.addLast(String.format("%d %s", commandCounter, c));
    this.incrementCommandCounter();
  }
  
  private void incrementCommandCounter() {
    commandCounter++;
  }
  
  private String getCommandsList() {
    Iterator i = this.commands.iterator();
    String out = "";
    
    while (i.hasNext()) {
      out += i.next().toString().concat("\n");
    }
    return out;
  }
  
  public String getInitMessage() {
    return String.format("%s@%s:~%s$ ", getUsername(), getOSName(), getCurrentDirectory());
  }
  
  private String getOSName() {
    return System.getProperty("os.name");
  }
  
  private String getUsername() {
    return System.getProperty("user.name");
  }
  
  private String getCurrentDirectory() {
    String[] dir = this.currentRoute.split("/");
    return dir[dir.length-1];
  }
}
