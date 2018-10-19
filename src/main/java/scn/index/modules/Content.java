package scn.index.modules;

import java.util.Map;

public class Content {
    private String type;
    private String store;
    private String termVector;
    private String analyzer;
    private String searchAnalyzer;
    private String includeInAll;
    private long boost;

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public String getStore() { return store; }
    public void setStore(String value) { this.store = value; }

    public String getTermVector() { return termVector; }
    public void setTermVector(String value) { this.termVector = value; }

    public String getAnalyzer() { return analyzer; }
    public void setAnalyzer(String value) { this.analyzer = value; }

    public String getSearchAnalyzer() { return searchAnalyzer; }
    public void setSearchAnalyzer(String value) { this.searchAnalyzer = value; }

    public String getIncludeInAll() { return includeInAll; }
    public void setIncludeInAll(String value) { this.includeInAll = value; }

    public long getBoost() { return boost; }
    public void setBoost(long value) { this.boost = value; }
}
