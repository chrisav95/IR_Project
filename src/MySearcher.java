import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import txtparsing.MyDoc;
import txtparsing.MyQuery;
import txtparsing.TXTParsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

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

            // parse queries txt using TXT parser
            List<MyQuery> queries = TXTParsing.parseQueries(queriestxt);

            //search the index for every query
            for (MyQuery q : queries){
                //Search the index using indexSearcher
                search(indexSearcher, field, q.getQuery(),k);
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
    private void search(IndexSearcher indexSearcher, String field, String q, int k){
        try{
            // define which analyzer to use for the normalization of user's query
            Analyzer analyzer = new EnglishAnalyzer();

            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);

            // read user's query from stdin
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // parse the query according to QueryParser
            Query query = parser.parse(q);
            System.out.println("Searching for: " + query.toString(field));

            // search the index using the indexSearcher
            TopDocs results = indexSearcher.search(query, k);
            ScoreDoc[] hits = results.scoreDocs;


            long numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");

            //display results
            for(int i=0; i<hits.length; i++){
                Document hitDoc = indexSearcher.doc(hits[i].doc);
                System.out.println("\tScore "+hits[i].score +"\ttitle="+hitDoc.get("title")+"\tbody:"+hitDoc.get("body")+"\tcode:"+hitDoc.get("code"));
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }


}