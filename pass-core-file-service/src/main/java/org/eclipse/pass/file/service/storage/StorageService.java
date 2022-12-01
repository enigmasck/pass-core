package org.eclipse.pass.file.service.storage;


import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.nio.file.Path;
import java.security.SecureRandom;


public interface StorageService {

    void init();

    File store(MultipartFile mFile, String fileName);

    Path load(String fileId);

    Resource loadAsResource(String fileId);

    void delete(String fileId);

    String generateId();

}
