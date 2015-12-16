package com.healthcare.common.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/8
 */
public class FileUtils {
    public static final String directory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sensorlog";

    public void writeFile(String fileName){

        File file = new File(FileUtils.directory, fileName);
        File filePath = file.getParentFile();

        if (!filePath.isDirectory() && !filePath.mkdir()){
            //KLog.e("SensorDashbaord", "Could not create directory for log files");
            throw new RuntimeException("could not create directory");
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFileList(){
        return null;
    }


}
