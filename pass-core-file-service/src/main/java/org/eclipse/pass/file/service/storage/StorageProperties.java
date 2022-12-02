package org.eclipse.pass.file.service.storage;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.file-service")
public class StorageProperties {
    private final String rootdir;

    public StorageProperties(String rootDir){
        this.rootdir = rootDir;
    }

    public String getStorageRootDir(){return rootdir;}

}
