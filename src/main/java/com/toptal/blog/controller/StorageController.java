package com.toptal.blog.controller;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.toptal.blog.service.StorageService;

@RestController
@RequestMapping(value = "/api/storage")
public class StorageController {

	@Autowired
	private StorageService storageService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public void uploadFile(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
		storageService.uploadFile(file);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public void downloadFile(@RequestParam(value = "fileName") String fileName, HttpServletResponse response)
			throws IOException {

		InputStream inputStream = storageService.downloadFile(fileName);

		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();

	}

}
