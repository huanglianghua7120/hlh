package saaf.common.fmw.message.model.entities;

import java.util.List;

public class NewsChildMessageBean {
    private List<ArticleBean> articles;

    public NewsChildMessageBean() {
        super();
    }

    public void setArticles(List<ArticleBean> articles) {
        this.articles = articles;
    }

    public List<ArticleBean> getArticles() {
        return articles;
    }
}
