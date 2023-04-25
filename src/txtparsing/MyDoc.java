package txtparsing;

public class MyDoc {

    private String code;
    private String title;
    private String body;


    public MyDoc(String code, String title, String body) {
        this.code = code;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        String ret = "MyDoc{"
                + "\n\tCode: " + code
                + "\n\tTitle: " + title
                + "\n\tBody: " + body;
        return ret + "\n}";
    }

    //---- Getters & Setters definition ----
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() { return body; }

    public void setBody(String body) {
        this.body = body;
    }


}
