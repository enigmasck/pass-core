package org.eclipse.pass.file.service.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfiguration {
    @Autowired
    private StorageProperties storageProperties;

    public String getStorageRootDir() {
        return storageProperties.getStorageRootDir();
    }

    public String getStorageWorkDir() {
        return storageProperties.getStorageWorkDir();
    }

    public String getStorageTempDir() {
        return storageProperties.getStorageTempDir();
    }

    public String getStorageType() {
        return storageProperties.getStorageType();
    }

}
