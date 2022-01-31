package com.dharbor.caseassignment.log.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogController {
	private static final String folderPath="D:/logs";
	private static final String OUTPUT_ZIP_FILE = "D:/logs/log.zip";
	
	@RequestMapping("/logs")
	@ResponseBody
	public void show(@RequestParam("date") String date, HttpServletResponse response) throws IOException, ParseException {
		
		  List<String> fileNames = getFileName(date);
		  CreateZip(fileNames);
		  String fileName = OUTPUT_ZIP_FILE;
		  response.setContentType("application/zip");
	      response.setHeader("Content-Disposition", "attachment; filename=" +fileName);
	      response.setHeader("Content-Transfer-Encoding", "binary");
	      try {
	    	  BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
	    	  FileInputStream fis = new FileInputStream(fileName);
	    	  int len;
	    	  byte[] buf = new byte[1024];
	    	  while((len = fis.read(buf)) > 0) {
	    		  bos.write(buf,0,len);
	    	  }
	    	  fis.close();
	    	  bos.close();
	    	  response.flushBuffer();
	      }
	      catch(IOException e) {
	    	  e.printStackTrace();  
	      }
	      finally{
	    	  File file = new File(OUTPUT_ZIP_FILE);
	    	  file.delete();
	      }
	}
	
    public List<String> getFileName(String date) throws IOException, ParseException{
    	List<String> fileList= new ArrayList<String>();
    	File[] files = new File(folderPath).listFiles();
    	 for (File filename : files) {
//    		 System.out.println("File: "+ filename.getName());
    		 String fname = filename.getName();
    		 String time = GettingLastmodifiedTime(folderPath+"/"+fname);
    		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    		 int result = (formatter.parse(date)).compareTo(formatter.parse(time));
    		 if(result == 0) {
    			 fileList.add(fname);
    		 }
    	 }
    	return fileList;
    }
    
    
    public String GettingLastmodifiedTime(String path) {
    	File file = new File(path);
    	long lastModified = file.lastModified();
    	Date date = new Date(lastModified);
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	String strDate = formatter.format(date);
//    	System.out.println(strDate);
    	return strDate;
    }
    
    
    public void CreateZip(List<String> fileName) throws IOException {

        try {
            
            // create byte buffer
            byte[] buffer = new byte[1024];
 
            FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
 
            ZipOutputStream zos = new ZipOutputStream(fos);
             
            for (int i=0; i < fileName.size(); i++) {
                 
                File srcFile = new File(folderPath+"/"+fileName.get(i));
 
                FileInputStream fis = new FileInputStream(srcFile);
 
                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                 
                int length;
 
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
 
                zos.closeEntry();
 
                // close the InputStream
                fis.close();
                 
            }
 
            // close the ZipOutputStream
            zos.close();
             
        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }

}