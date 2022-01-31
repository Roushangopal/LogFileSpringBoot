package com.dharbor.caseassignment.log.services;

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

import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

	private static final String folderPath="D:/logs";
	private static final String OUTPUT_ZIP_FILE = "D:/logs/log.zip";
	
	@Override
	public List<String> getFileName(String date) {
		// TODO Auto-generated method stub
    	List<String> fileList= new ArrayList<String>();
    	File[] files = new File(folderPath).listFiles();
    	 for (File filename : files) {
//    		 System.out.println("File: "+ filename.getName());
    		 String fname = filename.getName();
    		 String time = GettingLastmodifiedTime(folderPath+"/"+fname);
    		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    		 int result;
			try {
				result = (formatter.parse(date)).compareTo(formatter.parse(time));
	    		 if(result == 0) {
	    			 fileList.add(fname);
	    		 }
			} catch (ParseException e) {
				e.printStackTrace();
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


	@Override
	public void CreateZip(List<String> fileName) {
		// TODO Auto-generated method stub
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
