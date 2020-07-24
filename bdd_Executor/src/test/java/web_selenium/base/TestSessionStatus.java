package web_selenium.base;

public class TestSessionStatus {

    public String id;

    public int currentDataRecordIndex;

    public int currentIteration;

    public int currentSegmentIndex;

    public int currentTestIndex;

    public String currentTestName;

    public String currentTestPath;
    
    public String environment;

    public TestSessionStatus(String testSessionId) {
        this.id = testSessionId;
        this.currentDataRecordIndex = -1;
        this.currentIteration = 1;
        this.currentSegmentIndex = -1;
        this.currentTestIndex = -1;
        this.currentTestName = null;
        this.currentTestPath = null;
        this.environment = null;
    }

    public TestSessionStatus(TestSessionStatus session) {
        this.id = session.id;
        this.currentSegmentIndex = session.currentSegmentIndex;
        this.currentTestIndex = session.currentTestIndex;
        this.currentTestName = session.currentTestName;
        this.currentTestPath = session.currentTestPath;
        this.environment = session.environment;
    }
}
