/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SUMAN
 */

import java.util.Scanner;
import java.net.Socket;
import java.net.ServerSocket;


public class HTTPreqServer implements Runnable {
    
    private ServerSocket srvr;
    private Socket sc;
    private Scanner kb;
    private String kbimp;
    private Thread T;
    private boolean EXIT;
    
    
  
    HTTPreqServer()
    {
      
      EXIT=false;
      kb=new Scanner(System.in);
      T=new Thread(this);
      T.start();
      
    }
    
    public void run()
    {
        while(!EXIT)
        {
           try{
            if(!srvr.isClosed())
            {
                sc=srvr.accept();
                HandlerClass hc=new HandlerClass(sc);   
            }
            Thread.sleep(1000);
           }catch(Exception e)
           {
                   
           }    
        }
    }
    
    
    
    private void startServer()
    {
        try{
            System.out.println("[$] Starting Server....");
            srvr=new ServerSocket(80);
            System.out.println("[$] Server Online (port:"+srvr.getLocalPort()+")");
       }catch(Exception e)
       {
           e.printStackTrace();
       }
    }
    
    private void stopServer()
    {
        try{
            srvr.close();
            System.out.println("[$] Server Stopped....");   
       }catch(Exception e)
       {
           e.printStackTrace();
       }
    }
    public void startConsole()
    {
        System.out.println("...CHERRY HTTP SERVER...\n@SUMAN");
        System.out.println("------------------------");
        while(true)
        {
            kbimp=kb.next();
            if(kbimp.equals("start"))
            {
                startServer();
            }
            else if(kbimp.equals("stop"))
            {
                stopServer();
            }
            else if(kbimp.equals("exit"))
            {
                stopServer();
                EXIT=true;
                System.out.println("[$] Good Bye..");
                break;
            }
            else
            {
                System.out.println("\n----------HELP----------");
                System.out.println("Commands:");
                System.out.println("  start : Start Server.");
                System.out.println("  stop  : Stop Server.");
                System.out.println("  exit  : Exit program.");
                System.out.println("------------------------");
                        
            }
        }
    }   
    public static void main(String[] args) {
        // TODO code application logic here
        
        new HTTPreqServer().startConsole();
    }
    
}
