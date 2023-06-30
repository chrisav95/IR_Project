package myApp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import txtparsing.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class MyIndex {

    private String txtfile;

    /**
     * Configures IndexWriter.
     * Creates a lucene's inverted index.
     */
    public MyIndex(String txtfile) throws Exception{

        this.txtfile = txtfile;
        String indexLocation = ("index"); //define were to store the index

        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexLocation + "'...");
            //define in which directory to store the index
            Directory dir = FSDirectory.open(Paths.get(indexLocation));
            // define which analyzer to use for the normalization of documents
            Analyzer analyzer = new EnglishAnalyzer();
            // define retrieval model
            // ClassicsSimilarity is an implementation of TFIDFSimilarity which uses the Vector Space Model
            Similarity similarity = new ClassicSimilarity();
            // configure IndexWriter
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setSimilarity(similarity);

            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);

            // create the IndexWriter with the configuration as above
            IndexWriter indexWriter = new IndexWriter(dir, iwc);

            // parse txt document using TXT parser and index it
            List<MyDoc> docs = TXTParsing.parseDocs(txtfile);
            for (MyDoc doc : docs){
                indexDoc(indexWriter, doc);
            }

            System.out.println("Indexed the docs successfully!");

            indexWriter.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
            System.out.println("----------------------------");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }


    }

    /**
     * Creates a Document by adding Fields in it and
     * indexes the Document with the IndexWriter
     *
     * @param indexWriter the indexWriter that will index Documents
     * @param mydoc the document to be indexed
     *
     */
    private void indexDoc(IndexWriter indexWriter, MyDoc mydoc){

        try {

            // make a new, empty document
            Document doc = new Document();

            // create the fields of the document and add them to the document
            StringField code = new StringField("code", mydoc.getCode(), Field.Store.YES);
            doc.add(code);
            TextField title = new TextField("title", mydoc.getTitle(), Field.Store.YES);
            doc.add(title);
            TextField body = new TextField("body", mydoc.getBody(), Field.Store.YES);
            doc.add(body);
            //adding a field that will be searched
            String searchableText =  mydoc.getTitle() + " " + mydoc.getBody();
            TextField contents = new TextField("contents", searchableText, Field.Store.NO);
            doc.add(contents);

            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
                //System.out.println("adding " + mydoc);
                indexWriter.addDocument(doc);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
