package com.alex.services;

import com.alex.model.ComponentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class StatusService {

    @Value("${status.file.location}")
    private String fileLocation;
    @Value("${status.file.name}")
    private String fileName;

    public void reportStatus(ComponentStatus status) {
        switch (status) {
            case SUCCESS:
                processSuccess();
                break;
            case FAIL:
                processFail();
                break;
        }
    }

    private void processSuccess() {
        new File(fileLocation).mkdir();

        loadComponentName().forEach(s -> {
            try {
                File statusFile = new File(createLocation(s));
                statusFile.createNewFile();
                statusFile.setWritable(true);
                FileOutputStream fos = new FileOutputStream(statusFile);
                fos.write(ComponentStatus.SUCCESS.toString().getBytes());
                fos.close();
            } catch (IOException e) {
                log.error("Can't write application status {}, application {}", ComponentStatus.SUCCESS, s);
            }
        });
    }

    private List<String> loadComponentName() {
        return Collections.singletonList(fileName);
    }

    private void processFail() {
        loadComponentName().forEach(s -> {
            File statusFile = new File(createLocation(s));
            statusFile.delete();
        });
    }

    private String createLocation(String component) {
        return fileLocation.concat(component);
    }
}
