package com.mjrafg.springsecurestarterkit.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.mjrafg.springsecurestarterkit.app.base.file.FileEntity;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

import static com.mjrafg.springsecurestarterkit.config.app.AppConfig.THUMB_SIZE;


public class FileUtils {
//	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);



	private static String USER_PATH="/storage/user";


	private static String SIGN_PATH = "/storages/user/sign";

	private static String PROFILE_PATH="/storages/user/profile";

	private static String FILE_PATH="/storages/file";

	private static String DEFAULT_PATH ="/storages";
	private static String calcPath(String uploadPath) {
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

		makeDir(uploadPath, datePath);

		return datePath;
	}

	// 송효섭 추가
	private static void makeDir(String uploadPath, String path) {
		File dirPath = new File(uploadPath + path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
	
	// 송효섭 추가
	public static int getOrientation(File file) throws IOException {
		int orientation = 1;
		Metadata metadata;
		Directory directory;

		try {
			metadata = ImageMetadataReader.readMetadata(file);
			directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if (directory != null) {
				orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
		} catch (ImageProcessingException e) {
			e.printStackTrace();
		} catch (MetadataException e) {
			e.printStackTrace();
		}
		
		return orientation;
	}

	// 송효섭 추가
	public static BufferedImage transformImageRotate(int orientation, BufferedImage scaledImg) {
		BufferedImage rotatedImg = scaledImg;
		if (orientation > 1) {
			Rotation correction = null;
			if (orientation == 8) {
				correction = Rotation.CW_270;
			} else if (orientation == 3) {
				correction = Rotation.CW_180;
			} else if (orientation == 6) {
				correction = Rotation.CW_90;
			}

			rotatedImg = Scalr.rotate(scaledImg, correction);
		}

		return rotatedImg;
	}

	// 송효섭 추가
	private static String makeThumbnail(String uploadPath, String savedPath, String fileName) throws Exception {
		File originImg = new File(uploadPath + savedPath, fileName);
		BufferedImage sourceImg = ImageIO.read(originImg);
		BufferedImage scaledImg;
		BufferedImage rotatedImg;
		
		if(sourceImg.getWidth() >= sourceImg.getHeight()){
			if(sourceImg.getWidth() > THUMB_SIZE) {
				scaledImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, THUMB_SIZE);
			}
			else {
				scaledImg = sourceImg;
			}
		}
		else{
			if(sourceImg.getHeight() > THUMB_SIZE) {
				scaledImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, THUMB_SIZE);
			}
			else {
				scaledImg = sourceImg;
			}
		}
		
		rotatedImg = transformImageRotate(getOrientation(originImg), scaledImg);
		
		String thumbnailName = uploadPath + savedPath + File.separator + "s_" + fileName;	// 썸네일 파일 이름

		File newFile = new File(thumbnailName);
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

		ImageIO.write(rotatedImg, formatName.toUpperCase(), newFile);
		return thumbnailName;
	}
	
	// 송효섭 추가
	private static String makeIcon(String uploadPath, String savedPath, String fileName) throws Exception {
		String iconName = uploadPath + savedPath + File.separator + fileName;
		return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}
	


	public static FileEntity uploadFile(String type, String originalName, byte[] fileData, boolean dateBase) throws Exception {
		String uploadPath = getFilePath(type);
		UUID uid = UUID.randomUUID();

		// Removing special characters from the original filename and prepending a unique identifier
		String sanitizedOriginalName = StringUtils.cleanPath(originalName).replaceAll("[^a-zA-Z0-9.\\-_]", "");
		String savedName = uid + "_" + sanitizedOriginalName;
		String savedPath = "";
		if(dateBase)
			savedPath = calcPath(uploadPath);

		Path targetPath = Paths.get(uploadPath, savedPath, savedName);
		Files.createDirectories(targetPath.getParent()); // Ensure the directory structure exists

		// Save the file
		Files.write(targetPath, fileData);

		String formatName = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
		String uploadedFileName;

		if (MediaUtils.getMediaType(formatName) != null) {
			// Fix image orientation, if necessary
			fixImageOrientation(targetPath, formatName);

			// Create thumbnail and return its path
			uploadedFileName = makeThumbnail(uploadPath, savedPath, savedName);
		} else {
			uploadedFileName = makeIcon(uploadPath, savedPath, savedName);
		}
		FileEntity fileEntity = new FileEntity();
		fileEntity.setDownloadPath("/api/file/download?type="+type+"&fileName="+savedName);
		fileEntity.setThumbnailPath("/api/file/display?type="+type+"&fileName=s_"+savedName);
		fileEntity.setSavedName(savedName);
		fileEntity.setOriginalName(originalName);
		fileEntity.setType(type);
		fileEntity.setSize((long) fileData.length);
		return fileEntity;
	}
    public static void deleteFile(String fileName,String type){
		String sFilePath = getFilePath(type);

		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
		MediaType mType = MediaUtils.getMediaType(formatName);
		//file is image and must delete thumbnail
		if (CommonUtils.isNotEmpty(mType)) {
			String front = fileName.substring(0, 12);
			String end = fileName.substring(14);
			new File(sFilePath +"/"+ (front + end).replace('/', File.separatorChar)).delete();
		}
		String path = sFilePath +"/"+ fileName;
		path = path.replace('/', File.separatorChar);
		new File(path).delete();
	}
	public static String getFilePath(String type) {
		String sFilePath = "c:/";
		if (type.equals("FILE")) sFilePath += FILE_PATH;
		else if (type.equals("SIGN")) sFilePath += SIGN_PATH;
		else if (type.equals("PROFILE")) sFilePath += PROFILE_PATH;
		else if (type.equals("USER")) sFilePath += USER_PATH;
		else sFilePath += DEFAULT_PATH;
		return sFilePath;
	}
	private static void fixImageOrientation(Path imagePath, String formatName) throws IOException {
		BufferedImage sourceImg = ImageIO.read(imagePath.toFile());
		int orientation = getOrientation(imagePath.toFile());
		if (orientation > 1) {
			BufferedImage rotatedImg = transformImageRotate(orientation, sourceImg);

			// Save the rotated image over the original one
			try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(imagePath.toFile())) {
				final ImageWriter writer = ImageIO.getImageWritersByFormatName(formatName).next();
				writer.setOutput(imageOutputStream);
				writer.write(rotatedImg);
				writer.dispose();
			}
		}
	}
}