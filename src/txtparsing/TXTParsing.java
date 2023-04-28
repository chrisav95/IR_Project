package txtparsing;

import utils.IO;
import java.util.ArrayList;
import java.util.List;

public class TXTParsing {

    public static List<MyDoc> parseDocs(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("///");
            System.out.println("Read: "+docs.length + " docs");

            //Parse each document from the txt file
            List<MyDoc> parsed_docs= new ArrayList<MyDoc>();
            for (String doc:docs){
                String trimmed = doc.trim();
                //The first string is the code of the Doc
                String[] fields1 = trimmed.split("\n",2);
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

    public static List<MyQuery> parseQueries(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] queries = txt_file.split("///");
            System.out.println("Read: "+queries.length + " queries");
            System.out.println("----------------------------");

            //Parse each query from the txt file
            List<MyQuery> parsed_queries= new ArrayList<MyQuery>();
            for (String q:queries){
                String trimmed = q.trim();
                //The first string is the code of the Query
                String[] fields = trimmed.split("\n");
                String code = fields[0];

                //The string after that is the Query
                String query = fields[1];

                MyQuery myquery = new MyQuery(code,query);
                parsed_queries.add(myquery);
            }

            return parsed_queries;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }

    }
}
