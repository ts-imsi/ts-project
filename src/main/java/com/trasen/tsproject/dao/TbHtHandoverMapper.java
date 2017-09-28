package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.util.MyMapper;

public interface TbHtHandoverMapper extends MyMapper<TbHtHandover> {

    TbHtHandover getHandoverToHtNo(String htNo);

    int insertHandover(TbHtHandover tbHtHandover);

    int updateHandover(TbHtHandover tbHtHandover);

    int submitHandover(TbHtHandover tbHtHandover);
}