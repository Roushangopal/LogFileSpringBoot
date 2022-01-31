package com.dharbor.caseassignment.log.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dharbor.caseassignment.log.services.LogService;

@Controller
public class logController {
	private static final String OUTPUT_ZIP_FILE = "D:/logs/log.zip";
	
	@Autowired
	private LogService logService;
	
	@RequestMapping("/logs")
	@ResponseBody
	public void show(@RequestParam("date") String date, HttpServletResponse response) throws IOException {
		
		  List<String> fileNames = logService.getFileName(date);
		  if(fileNames.isEmpty()) {
			    response.setHeader("Custom-Header", "foo");
			    response.setStatus(200);
			    response.getWriter().println("No Files Available For the specified date");
		  } else {
			  logService.CreateZip(fileNames);
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
	}
}