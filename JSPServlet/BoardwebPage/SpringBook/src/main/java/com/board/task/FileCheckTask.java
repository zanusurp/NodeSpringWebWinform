package com.board.task;

import java.io.Console;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.board.domain.BoardAttachVO;
import com.board.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {
	
	@Setter(onMethod_=@Autowired)
	private BoardAttachMapper attachMapper;
	
	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		log.info("===========���� �ð�  millis ��: "+ cal.getTimeInMillis());
		cal.add(Calendar.DATE, -1);
		String str = sdf.format(cal.getTime());
		log.info("============Ķ���� �ð� ���  : cal.getTime :"+str);
		return str.replace("-", File.separator);
		
	}
	
	@Scheduled(cron = "0 0 2 * * *") //��, ��, ��, ��, ��, ����,(��) : 2�ð����� üũ
	public void checkFiles() throws Exception{
		log.warn("File Check Task run ==================================");
		log.warn(new Date());
		//��� �ִ� �� ����Ʈ��
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		//���� ����Ʈ ���丮 üũ
		List<Path> fileListPaths = fileList.stream()
				.map(vo->Paths.get("c:\\upload", vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName()))
				.collect(Collectors.toList());
		//�̹��� ������ Ȯ��
		fileList.stream().filter(vo -> vo.isFileType() == true) //�ִ�?
		.map(vo -> Paths.get("c:\\upload",vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName()))
		.forEach(p->fileListPaths.add(p));//������ �߰� ��Ŵ
		log.info("=====================================");
		fileListPaths.forEach( file -> log.warn("���� �̸���  : "+file));
		//���� ����
		File targetDir = Paths.get("c:\\upload",getFolderYesterDay()).toFile();
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath())==false);
		log.warn("----------------------");
		for(File file:removeFiles) {
			log.warn("���� ���ϵ�  : "+file.getAbsolutePath());
			file.delete();
			log.info("���� ���� �Ϸ� ");
		}
		
	}
}
