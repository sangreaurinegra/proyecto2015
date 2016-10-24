/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.proyecto.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Opens a zip file for output and writes the text, native files, and exceptions
 * into it.
 */
public class ZipFileWriter {
	Logger log = Logger.getLogger("log_file");
    private String zipFileName;
    private ZipOutputStream zipOutputStream;
    OutputStream os;
    
    public static String OUTPUT_DIR = "/output/"; //"/home/gabriel/Escritorio/proyecto/repo/proyecto2015/resources/entrada/";
    
    
    public ZipFileWriter(String name) {
    	zipFileName =  OUTPUT_DIR + name + ".zip";
    }

    public void setup(Configuration conf) {
    	FileSystem fs;
    	try {
    		fs = FileSystem.get(conf);
    		Path path = new Path(zipFileName);
    		os = fs.create(path);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	log.info("CREADO " + zipFileName);
    }

    public void openZipForWriting() throws IOException {
    	
        //fileOutputStream = new FileOutputStream(zipFileName);
        zipOutputStream = new ZipOutputStream(new BufferedOutputStream(os));
        log.info("Abierto:  " + zipFileName);
    }

    public void closeZip() throws IOException {
        zipOutputStream.close();
        os.close();
    }

    public void addTextFile(String entryName, String textContent) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipOutputStream.putNextEntry(zipEntry);
        if (textContent == null) {
            textContent = "No text extracted";
        }
        zipOutputStream.write(textContent.getBytes());
        log.info("write:  " + "entryName " + entryName + " textContent " + textContent);
    }

    public void addBinaryFile(String entryName, byte[] fileContent, int length) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(fileContent, 0, length);
        log.info("addBinaryFile: entryName " + entryName + " length "+ length );
    }

    public String getZipFileName() {
        return zipFileName;
    }
    
    public ZipOutputStream getZipOutputStream(){
        return zipOutputStream;
    }
    
}
