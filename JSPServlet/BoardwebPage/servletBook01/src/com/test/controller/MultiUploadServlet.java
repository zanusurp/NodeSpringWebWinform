package com.test.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


@WebServlet("/upload2.do")
public class MultiUploadServlet extends HttpServlet {
	
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String savePath = "upload";
		int upLoadFileSizeLimit = 5*1024*1024;
		String encType = "UTF-8";
		
		ServletContext context  = getServletContext();
		String uploadFilePath = context.getRealPath(savePath);
		System.out.println(uploadFilePath);
		try {
			MultipartRequest multi = new MultipartRequest(
					request,
					uploadFilePath,
					upLoadFileSizeLimit,
					encType,
					new DefaultFileRenamePolicy()
					);
			Enumeration files = multi.getFileNames();
			while(files.hasMoreElements()) {
				String file = (String)files.nextElement();
				String file_name = multi.getFilesystemName(file);
				String ori_file_name = multi.getOriginalFileName(file);
				out.print("<br>���ε�� ���ϸ� : "+file_name);
				out.print("<br>���� ���ϸ� : "+ori_file_name);
				out.print("<hr>");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
