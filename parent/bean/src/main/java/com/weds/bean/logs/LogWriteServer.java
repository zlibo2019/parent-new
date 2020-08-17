package com.weds.bean.logs;

public interface LogWriteServer {
    void handleInfoLogs(LogEntity logEntity);

    void handleErrorLogs(LogEntity logEntity);
}
