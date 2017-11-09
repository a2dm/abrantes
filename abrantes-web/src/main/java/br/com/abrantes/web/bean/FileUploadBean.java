package br.com.abrantes.web.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import br.com.abrantes.cmn.vo.UploadedImage;

public class FileUploadBean {

	private ArrayList<UploadedImage> files = new ArrayList<UploadedImage>();
	private int uploadsAvailable = 5;
	private boolean autoUpload = false;
	private boolean useFlash = false;

	public int getSize() {
		if (getFiles().size() > 0) {
			return getFiles().size();
		} else {
			return 0;
		}
	}

	public FileUploadBean() {
		
	}

	public void paint(OutputStream stream, Object object) throws IOException {
		stream.write(getFiles().get((Integer) object).getData());
	}

	public void listener(FileUploadEvent event) throws Exception {
		UploadedFile item = event.getUploadedFile();
		UploadedImage file = new UploadedImage();
		file.setLength(item.getData().length);
		file.setName(item.getName());
		file.setData(item.getData());
		files.add(file);
	}

	public String clearUploadData() {
		files.clear();
		setUploadsAvailable(5);
		return null;
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public ArrayList<UploadedImage> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadedImage> files) {
		this.files = files;
	}

	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}
}