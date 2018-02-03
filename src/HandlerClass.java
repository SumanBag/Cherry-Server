/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SUMAN
 */
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HandlerClass extends Thread {
    private Socket client;
    private BufferedReader rd;
    private BufferedWriter wd;
    private File file;
    private FileInputStream fp;
    private String root="./publicHTML";
    private String repH,repD;
    private String[] reqH;
    private int ch;
    
    HandlerClass(Socket sc)
    {
        try{
            client=sc;
            setHEADER();
            rd=new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            wd=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            
            start();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        
    }
    
    public void run()
    {
        while(!client.isClosed())
        {
            try{
               
                getREQ();
                sendREP();
                rd.close(); 
                client.close();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void setHEADER()
    {
            repH="HTTP/1.1 200 Document follows \r\n";
            repH+="MIME-Version: 1.0 \r\n";
            repH+="Server: CherryHTTPServer 1.0 \r\n";
            repH+="Connection: close \r\n";
            repH+="Content-Type: text/html \r\n";
            repH+="Content-Length: 0 \r\n";
            repH+="\r\n";
    }
    private void getREQ()throws Exception
    {
             String req="";
             while(rd.ready())
            {
                if((ch=rd.read())!=-1)
                {
                   req+=(char)ch;
                }
                else
                {
                    
                    break;
                }
            }
            System.out.println(req);
            reqH=req.split("\r\n");
           // for(String a:reqH)
            //System.out.print(req.equals(""));
            if(req.equals(""))
                reqH[0]="GET / HTTP/1.1";
            
            System.out.println(client.getInetAddress().toString()+"|"+client.getPort()+" "+reqH[0]);
             
    }
    
    private void sendREP()throws Exception
    {
             
            String method=reqH[0].split(" ")[0];
            if(method.equals("GET"))
            {
                
                String path=reqH[0].split(" ")[1];
                readPage(path);
            }
            
            if(method.equals("POST"))
            {
                String path=reqH[0].split(" ")[1];
                readPage(path);
            }
             
            String send=repH+repD;
            
            wd.write(send.toCharArray());
            wd.close();
    }
    
    private void readPage(String path)
    {
        
        int a;
        repD="";
        if(path.equals("/"))
        {
            path=root+path+"index.html";
        }
        else{
            path=root+path;
        }
       
        File f=new File(path);
        try{
        FileInputStream fp=new FileInputStream(f);
        String con="Content-Length: "+f.length()+" \r\n\r\n";
        repH=repH.replace("Content-Length: 0 \r\n\r\n",con);
        while(fp.available()>0)
        {
            a=fp.read();
            repD+=(char)a;
        }
        
        }catch(FileNotFoundException e)
        {
            repH=repH.replace("200","404");
            
        }catch(Exception e)
        {
            repH=repH.replace("200","500");
            e.printStackTrace();
        }
        
    }
    
    
    
    
}
