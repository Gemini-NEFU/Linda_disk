package com.dao;

import com.beans.LogGroupInfo;
import com.beans.LogInfo;

import java.util.List;

public interface HiveDao {
    List<LogInfo> getLogList(String userName);

    List<LogGroupInfo> getLogGroupList(String userName);
}
