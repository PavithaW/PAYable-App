package com.mpos.connection;

public interface PaginationHandler {

    public void loadData(int pageId, int pageSize, int callerId);

}
