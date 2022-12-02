package org.eclipse.pass.file.service.storage;

import liquibase.repackaged.org.apache.commons.lang3.SystemUtils;
import liquibase.util.file.FilenameUtils;
import org.eclipse.pass.file.service.PassFileServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wisc.library.ocfl.core.storage.filesystem.FileSystemStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileSystemStorageService implements StorageService{

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemStorageService.class);
    private final Path rootLoc;
    private final int idLength = 20;
    private final String idCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final String storageType = "FILE_SYSTEM";

    @Autowired
    private StorageProperties storageProperties;

    //TODO might delete, don't think it will be necessary for user to delete
    enum OsType {
        WINDOWS,
        LINUX,
        UNIX,
        UNKNOWN
    }

    //TODO implement constructor, gets .env prop for files system storage
    public FileSystemStorageService(){
        this.rootLoc = Paths.get(storageProperties.getStorageRootDir());
    }

    @Override
    public void init() {
        try{

            if(!Files.isReadable(rootLoc)){
                throw new FileSystemException("File Service: No permission to read root directory.");
            }
            if(!Files.exists(rootLoc)){
                try{
                    Files.createDirectory(rootLoc);
                } catch (IOException e){
                    throw new FileSystemException("File Service: Root directory could not be created: " + e.toString());
                }
            }
        } catch(FileSystemException e){
            LOG.error(e.toString());
        }
    }

    @Override
    public File store(MultipartFile mFile, String fileName) {
        //TODO implement OCFL storage using UW Madison implementation
        File file = new File(
                generateId(),
                fileName,
                mFile.getContentType(),
                storageType,
                mFile.getSize(),
                getExtensionFromFile(mFile.getOriginalFilename()).toString()
        );

        return file;
    }

    @Override
    public Path load(String fileId) {
        //TODO implement OCFL storage using UW Madison implementation
        return null;
    }

    @Override
    public Resource loadAsResource(String fileId) {
        //TODO implement OCFL storage using UW Madison implementation
        return null;
    }

    @Override
    public void delete(String fileId) {
        //TODO implement OCFL storage using UW Madison implementation
    }

    @Override
    public String generateId(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(idLength);
        for(int i = 0; i < idLength; i++)
            sb.append(idCharSet.charAt(secureRandom.nextInt(idCharSet.length())));
        return sb.toString();
    }

    public Optional<String> getExtensionFromFile(String fn) {
        return Optional.ofNullable(fn)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fn.lastIndexOf(".") + 1));
    }

    //TODO might remove this - should the OS type be explicit set by the user? Could cause issues trying to infer the
    // OS type?
    private OsType getOsType(){
        Pattern winPattern = Pattern.compile("windows", Pattern.CASE_INSENSITIVE);
        Pattern linuxPattern = Pattern.compile("linux", Pattern.CASE_INSENSITIVE);
        Pattern unixPattern = Pattern.compile("unix", Pattern.CASE_INSENSITIVE);
        Matcher winMatch = winPattern.matcher(SystemUtils.OS_NAME);
        Matcher linuxMatch = linuxPattern.matcher(SystemUtils.OS_NAME);
        Matcher unixMatch = unixPattern.matcher(SystemUtils.OS_NAME);
        if(winMatch.find()){ return OsType.WINDOWS; }
        if(linuxMatch.find()){ return OsType.LINUX; }
        if(unixMatch.find()){ return OsType.UNIX; }
        return OsType.UNKNOWN;
    }
}
