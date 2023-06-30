package txtparsing;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.err;

public class TXTParsing {

    public static List<MyDoc> parseDocs(String file) throws Exception {
        try{

            //Parse txt file of documents
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter("\\A"); //\\A stands for :start of a string
            String txt_file = scanner.next(); //Reading entire file into a String

            String[] docs = txt_file.split("///");
            System.out.println("Read: "+docs.length + " docs");

            //Parse each document from the txt file
            List<MyDoc> parsed_docs= new ArrayList<MyDoc>();
            for (String doc:docs){
                String trimmed = doc.trim();
                //The first string is the code of the Doc
                String[] fields1 = trimmed.split("\n",2);
                String code = fields1[0].trim();

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
            //Parse txt file of queries
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter("\\A"); //\\A stands for :start of a string
            String txt_file = scanner.next();
            String[] queries = txt_file.split("///");
            System.out.println("Read: "+queries.length + " queries");
            System.out.println("----------------------------");

            //Parse each query from the txt file
            List<MyQuery> parsed_queries= new ArrayList<MyQuery>();
            for (String q:queries){
                String trimmed = q.trim();
                //The first string is the code of the Query
                String[] fields = trimmed.split("\n");
                String code = fields[0].trim();

                //The string after that is the Query
                String query = fields[1].trim();

                MyQuery myquery = new MyQuery(code,query);
                parsed_queries.add(myquery);
            }

            return parsed_queries;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }

    }

    public static void parseWordNetFile(String file) throws Exception {
        try{
            Reader reader = new FileReader(file);

            if (! (new File(file)).canRead()) {
                err.println("Error: cannot read Prolog file: " + file);
                System.exit(1);
            }

            //Parse txt file of WordNet synonyms
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter("\\A"); //\\A stands for :start of a string
            String txt_file = scanner.next();
            String[] synonyms = txt_file.split("\n");
            System.out.println("Read: "+synonyms.length + " synonyms.");
            System.out.println("----------------------------");

            //create a new txt that only contains synonyms of nouns
            String filename = "C:\\Users\\chris\\Desktop\\IntelliJ Workspace\\IR_Project\\src\\synonyms_wn.txt";
            File syn = new File(filename);

            if(syn.exists()){
                syn.delete();
            }
            syn.createNewFile();

            // true to append in existing file
            FileWriter fw = new FileWriter(syn,true);
            BufferedWriter writer = new BufferedWriter(fw);

            int count = 0;
            //Parse each line from the txt file
            for (String s:synonyms){
                String trimmed = s.trim();
                // storing the type of the word
                String[] fields = trimmed.split(",");
                String type = fields[3].trim();
                // re-writing only the noun-synonyms
                if (type.equals("n")){
                    //write s in new txt
                    writer.append(trimmed);
                    writer.newLine();
                    count++;
                }
            }

            System.out.println("Kept: "+count+" synonyms.");
            System.out.println("----------------------------");

            writer.close();
            return;

        } catch (Throwable err) {
            err.printStackTrace();
            return;
        }

    }

}


