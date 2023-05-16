public class IRmain {
    public static void main(String[] args){
        try {
            String documentstxt =  "IR2023//documents.txt"; //txt file of documents to be parsed and indexed
            MyIndex myIndex = new MyIndex(documentstxt);

            String queriestxt = "IR2023//queries.txt"; //txt file of queries
            MySearcher mySearcher1 = new MySearcher(queriestxt,20);
            MySearcher mySearcher2 = new MySearcher(queriestxt,30);
            MySearcher mySearcher3 = new MySearcher(queriestxt,50);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
