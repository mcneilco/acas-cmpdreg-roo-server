package com.labsynch.cmpdreg.dto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;

import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.MimeTypeUtil;

@RooJavaBean
@RooToString
@RooJson
public class FileSaveSendDTO{


	private static final Logger logger = LoggerFactory.getLogger(FileSaveSendDTO.class);


	private Boolean ie;

	private String subdir; // added 

	private List<String> description = new ArrayList<String>();

	@Transient 
	private List<MultipartFile> file = new ArrayList<MultipartFile>();



	public List<MultipartFile> getFile() { 
		return file; 
	} 

	// save file to disk, save filename , file size to database  
	public List<FileSaveReturnDTO> saveFile() { 

		List<MultipartFile> fileList = this.file;

		List<String> descriptionList = this.description;

		List<FileSaveReturnDTO> fileSaveArray = new ArrayList<FileSaveReturnDTO>();

		logger.debug("FileSaveSendDTO: Number of files to save: " + fileList.size());
		for (int i = 0; i < fileList.size(); i++){
			logger.debug("current file number: " + i); 
			MultipartFile file = fileList.get(i);
			logger.debug("is the file empty: " + file.isEmpty());

			if (!file.isEmpty()){
				String description = descriptionList.get(i);
				FileSaveReturnDTO fileSaveReturn = new FileSaveReturnDTO();
				try { 
					InputStream in = file.getInputStream(); 
					String rootSavePath = Configuration.getConfigInfo().getServerSettings().getNotebookSavePath(); 
					logger.debug(rootSavePath);
					String savePath = rootSavePath + this.getSubdir() +  File.separator ;
					logger.debug(savePath);
					String saveFileName = savePath + file.getOriginalFilename();            
					logger.debug(saveFileName);
					boolean createdDir = new File(savePath).mkdirs();
					if (createdDir){
						logger.debug("new directory created " + savePath);
					} else {
						logger.error("unable to create the directory " + savePath);
					}
					logger.debug(" Saving file: " + file.getOriginalFilename() + " to  " + saveFileName); 
					String urlString = "getFile?fileUrl=" + saveFileName;
					logger.debug("url string " + urlString);

					FileOutputStream f = new FileOutputStream(saveFileName); 

					int ch = 0; 
					while ((ch = in.read()) != -1) { 
						f.write(ch); 
					} 
					f.flush(); 
					f.close();   


					String contentType = null;
					contentType = MimeTypeUtil.getContentTypeFromExtension(file.getOriginalFilename());
					if (contentType==null){
//						logger.debug("content type was null -- try to get it from the file itself");
//						contentType = file.getContentType();
						logger.debug("unknown content type -- set to default binary type");
						contentType = MimeTypeUtil.getContentTypeFromExtension("fileName.defaultbin");

					}

					fileSaveReturn.setName(file.getOriginalFilename());
					fileSaveReturn.setSize(file.getSize());
					fileSaveReturn.setType(contentType);
					fileSaveReturn.setUploaded(true);
					fileSaveReturn.setUrl(urlString);
					fileSaveReturn.setDescription(description);
					fileSaveReturn.setIe(this.getIe());
					fileSaveReturn.setSubdir(this.getSubdir());

					logger.debug("Ready to save the file " + fileSaveReturn.getType() + "  " + fileSaveReturn.getName() + " " + fileSaveReturn.getUrl());

					fileSaveArray.add(fileSaveReturn);

				} catch (FileNotFoundException e) { 
					// TODO Auto-generated catch block 
					e.printStackTrace(); 
				} catch (IOException e) { 
					// TODO Auto-generated catch block 
					e.printStackTrace(); 
				}     			
			}
		}


		return fileSaveArray;

	}
	

	}
