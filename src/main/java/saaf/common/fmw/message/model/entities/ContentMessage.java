package saaf.common.fmw.message.model.entities;

public class ContentMessage {
    public ContentMessage() {
        super();
    }
    private String content;

    public ContentMessage(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
