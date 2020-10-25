package saaf.common.fmw.message.model.entities;

import java.util.List;

public class ConcernUserCounts {
    private List<ConcernUserCount> list;

    public ConcernUserCounts() {
        super();
    }

    public void setList(List<ConcernUserCount> concernUserCounts) {
        this.list = concernUserCounts;
    }

    public List<ConcernUserCount> getList() {
        return list;
    }
}
