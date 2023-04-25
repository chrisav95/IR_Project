package txtparsing;

import utils.IO;
import java.util.ArrayList;
import java.util.List;

public class TXTParsing {

    public static List<MyDoc> parse(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("/// \r\n");
            System.out.println("Read: "+docs.length + " docs");

            //Parse each document from the txt file
            List<MyDoc> parsed_docs= new ArrayList<MyDoc>();
            for (String doc:docs){
                //The first string is tha code of the Doc
                String[] fields1 = doc.split("\n",2);
                String code = fields1[0];

                //The string after that and before the ":" is the Title of the Doc
                String[] fields2 = fields1[1].split(":",2);
                String title = fields2[0];

                //The remaining string is the Body of the Doc
                String body = fields2[1];

                MyDoc mydoc = new MyDoc(code,title,body);
                parsed_docs.add(mydoc);
            }

            return parsed_docs;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }
        
    }

}
