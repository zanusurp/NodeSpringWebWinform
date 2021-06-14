package com.board.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {//������ ������ mvc
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());	
			return contentType.startsWith("image");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	private String getFolder() {
		log.info("���� ��¥�� �˾� ���� ���� ����� ����");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("���ε� �� ");
	}
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		String uploadFolder = "c:\\upload";
		for(MultipartFile multipartFile : uploadFile) {
			log.info("�ɷε���-----------------------------------------");
			log.info("uploadfile �̸� : "+multipartFile.getOriginalFilename());
			log.info("uploadfile ũ�� : "+multipartFile.getSize());
			
			File saveFile = new File(uploadFolder,multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("upload ajax post.  . . . . ");
		String uploadFolder = "c:\\upload";
		//��������
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("���ε� ��� : "+uploadPath);
		
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("Update==================================================================");
			log.info("���ε� ���� �̸�"+multipartFile.getOriginalFilename());
			log.info("���� ������ : "+multipartFile.getSize());
			String uploadFileName= multipartFile.getOriginalFilename();
			
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("���� �̸���" +uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			try {
				File saveFile = new File(uploadPath,uploadFileName);
				multipartFile.transferTo(saveFile);
				
				if(checkImageType(saveFile)) {
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath,"s_"+uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,100,100);
					thumbnail.close();
				}				
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
		}
		
	}
	//�̹����� ���� ���е� �־ �̰� �� ����
	@PostMapping(value = "/uploadAjaxAction2", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost2(MultipartFile[] uploadFile){
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "c:\\upload";
		
		String uploadFolderPath = getFolder(); //��¥�� ����ȭ �̰� ���� ����
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("���� ���"+uploadPath);
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
			log.info("��δ� ���������");
		}
		log.info("��δ� �̹� ����������Ƿ� ���� ����");
		for (MultipartFile multipartFile : uploadFile) {
			log.info("���� �Է� ����.................................");
			AttachFileDTO attachDTO = new AttachFileDTO();
			log.info("���� �̸� ���� �κ� �ʱ� �����ϳ�");
			String uploadFileName = multipartFile.getOriginalFilename();
			log.info("���� ���� �̸� :  "+uploadFileName);
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("����Ʈ�� �� ���ε� ���� �̸� : "+uploadFileName);
			attachDTO.setFileName(uploadFileName);
			UUID uuid = UUID.randomUUID(); //���������� ��ġ�� ���������� �װ͸����� �ߺ����������� 
			
			uploadFileName = uuid.toString()+"_"+uploadFileName;
			
			try {
				File saveFile = new File(uploadPath,uploadFileName);
				log.info(saveFile);
				multipartFile.transferTo(saveFile);
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				if(checkImageType(saveFile)) {
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath,"s_"+uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,100,100);
					thumbnail.close();
				}
				list.add(attachDTO);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
		
		
	}
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("���÷����� ���� ���̸� : " + fileName);
		File file = new File("c:\\upload\\"+fileName);
		log.info("���÷����� ���� �������� �ҷ����°� Ȯ�� : file : "+ file);
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header  = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	//�ٿ�ε� ����
	@GetMapping(value = "/download", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ResponseBody
	public ResponseEntity<Resource> donwloadFile(String fileName){
		log.info("�ٿ�ε� �θ�  : "+fileName);
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		log.info("���ҽ�  ; "+resource);
		String resourceName = resource.getFilename();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Content-Disposition","attachment;filename="+ new String(resourceName.getBytes("UTF-8"),"ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
	}
	//�ٿ�ε� ie ó�� �߰� 
	@GetMapping(value = "/download2", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ResponseBody
	public ResponseEntity<Resource> donwloadFile2(@RequestHeader("User-Agent") String userAgent ,String fileName){
		log.info("�ٿ�ε� �θ�  : "+fileName);
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		log.info("���ҽ�  ; "+resource);
		
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
				
		String resourceName = resource.getFilename();
		log.info("���� �̸� : "+ resourceName);
		
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		log.info("���� ���� �̸� ���� ����" + resourceOriginalName); //�տ� s �� ���� �����ް� �ϱ� ����
		HttpHeaders headers = new HttpHeaders();
		log.info("http ���  : "+headers);
		try {
			String downloadName = null;
			if(userAgent.contains("Trident")) {
				log.info("�ͽ�");
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8").replace("\\+", " ");
			}else if(userAgent.contains("Edge")) {
				log.info("����");
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
				log.info("���� �̸� : "+downloadName);
			}else {
				log.info("ũ�� �����");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1");
			}
			log.info("�ٿ�ε� �� ���� �̸�  : "+downloadName);
			headers.add("Content-Disposition","attachment;filename="+ downloadName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
		
	}
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
		log.info("������ ���� : "+fileName);
		File file;
		
		try {
			file = new File("c:\\upload\\"+URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			if(type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("������ ���� ���� : "+largeFileName);
				file = new File(largeFileName);
				file.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("deleted",HttpStatus.OK);
	}
}
