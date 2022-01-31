package com.dharbor.caseassignment.log.services;

import java.util.List;

public interface LogService {
	
	public List<String> getFileName(String date);
	
	public void CreateZip(List<String> fileName);
}
