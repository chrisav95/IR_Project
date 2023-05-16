package txtparsing;

public class MyQuery {

    private String code;
    private String query;

    public MyQuery(String code, String query){
        this.code = code;
        this.query = query;
    }

    @Override
    public String toString() {
        String ret = "MyQuery{Code: " + code + "\tQuery: " + query;
        return ret + "}";
    }

    //---- Getters & Setters definition ----
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
