package com.shawncockburn.PITools.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.shawncockburn.PITools.data.ImageData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    public static List<ImageData> readCSV(File csv) throws Exception{
        if (csv.getName().substring(csv.getName().lastIndexOf(".")).equals(".csv")){
            List<ImageData> imageDataList = new ArrayList<>();
            try {
                CSVReader reader = new CSVReader(new FileReader(csv));
                String [] nextLine;
                int rowNumber = 0;
                while ((nextLine = reader.readNext()) != null) {
                    if (rowNumber != 0){
                        imageDataList.add(new ImageData(
                                nextLine[0],
                                nextLine[1] + "_" + nextLine[0],
                                nextLine[1]
                        ));
                    }
                    rowNumber++;
                }
            } catch (FileNotFoundException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (CsvValidationException e) {
                throw e;
            }
            return imageDataList;
        }
        return null;
    }
}
