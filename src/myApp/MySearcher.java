package myApp;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymGraphFilterFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import txtparsing.MyQuery;
import txtparsing.TXTParsing;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class MySearcher {

    private String queriestxt;
    private int k;

    public MySearcher(String queriestxt, int k){
        try{
            this.queriestxt = queriestxt;
            this.k = k;
            String indexLocation = ("index"); //define where the index is stored
            String field = "contents"; //define which field will be searched

            //Access the index using indexReaderFSDirectory.open(Paths.get(index))
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation))); //IndexReader is an abstract class, providing an interface for accessing an index.
            IndexSearcher indexSearcher = new IndexSearcher(indexReader); //Creates a searcher searching the provided index, Implements search over a single IndexReader.
            indexSearcher.setSimilarity(new ClassicSimilarity());

            TXTParsing.parseWordNetFile("C:\\Users\\chris\\Desktop\\IntelliJ Workspace\\IR_Project\\src\\wn_s.pl");

            // parse queries txt using TXT parser
            List<MyQuery> queries = TXTParsing.parseQueries(queriestxt);

            //create a txt for the results
            String filename = "IR2023\\results_"+k+".txt";
            File res = new File(filename);

            if(res.exists()){
                res.delete();
            }
            res.createNewFile();

            //search the index for every query
            for (MyQuery q : queries){
                //Search the index using indexSearcher
                search(indexSearcher, field, q, k, res);
            }

            //Close indexReader
            indexReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Searches the index given a specific query.
     */
    private void search(IndexSearcher indexSearcher, String field, MyQuery q, int k, File res){
        try{

            // define which analyzer to use for the normalization of user's query
            // replacing EnglishAnalyzer with a custom for query expansion with synonyms
            CustomAnalyzer canalyzer = customAnalyzerWithSynonyms();

            // create a query parser on the field "contents"
            QueryParser qparser = new QueryParser(field, canalyzer);

            // read user's query from stdin
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // true to append in existing file
            FileWriter fw = new FileWriter(res,true);
            BufferedWriter writer = new BufferedWriter(fw);

            // parse the query according to QueryParser
            Query query = qparser.parse(q.getQuery());
            System.out.println("Searching for: " + q.toString()+ " " + query.toString());

            // search the index using the indexSearcher
            TopDocs results = indexSearcher.search(query, k);
            ScoreDoc[] hits = results.scoreDocs;

            long numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");

            //display results & write in file
            for(int i=0; i<hits.length; i++){
                Document hitDoc = indexSearcher.doc(hits[i].doc);
                System.out.println("\tScore: "+hits[i].score+" \tCode:"+hitDoc.get("code")+" \tTitle="+hitDoc.get("title"));

                //write doc in the form: query id, iteration, document number, rank, sim, run_id
                writer.append(q.getCode()+" Q0 "+hitDoc.get("code")+" 0 "+hits[i].score+" search");
                writer.newLine();
            }

            writer.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static CustomAnalyzer customAnalyzerWithSynonyms() throws IOException {
        //Read synonyms from wn_s.pl file
        Map<String, String> sffargs = new HashMap<>();
        //sffargs.put("synonyms", "wn_s.pl");
        sffargs.put("synonyms", "synonyms_wn.txt");
        sffargs.put("format", "wordnet");

        //    Create custom analyzer for analyzing query text.
        //    Custom analyzer should analyze query text like the EnglishAnalyzer and have
        //    an extra filter for finding the synonyms of each token from the Map sffargs
        //    and add them to the query.
        CustomAnalyzer.Builder builder = CustomAnalyzer.builder()
                .withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(StandardFilterFactory.class)
                .addTokenFilter(EnglishPossessiveFilterFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(StopFilterFactory.class)
                .addTokenFilter(PorterStemFilterFactory.class)
                //expanding the filters of the EnglishAnalyzer with synonyms
                .addTokenFilter(SynonymGraphFilterFactory.class, sffargs);
        CustomAnalyzer analyzer = builder.build();
        return analyzer;
    }


}
