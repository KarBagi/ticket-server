package ConfigReader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ConfigReader {
    File file ;

    public ConfigReader(){
        this.file = new File("C:\\Studia\\jp\\lab06_pop\\src\\ConfigReader\\config.txt");
    }


    public  String[] getConfig(){

        Scanner scanner = null;
        try{
            scanner = new Scanner(file);
            String[] config = scanner.nextLine().split(";");
            return config;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
