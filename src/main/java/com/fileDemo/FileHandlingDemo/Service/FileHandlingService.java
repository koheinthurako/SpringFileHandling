package com.fileDemo.FileHandlingDemo.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileHandlingService {
	
//	File ကို upload လုပ်ခြင်း
	public String upload(MultipartFile file, String type) throws IOException;
	
//	Upload လုပ်ပြီးသား file ကိုထုတ်ကြည့်ခြင်း
	public byte[] getFile(String fileName) throws IOException;

}
