package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbHtChangeMapper;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/12
 */
@Service
public class TbHtChangeService {

    private static final Logger logger = LoggerFactory.getLogger(TbHtChangeService.class);

    @Autowired
    private TbHtChangeMapper tbHtChangeMapper;

    @Autowired
    private Environment env;

    public PageInfo<TbHtChange> getHtChangeList(int page,int rows,TbHtChange tbHtChange){
        PageHelper.startPage(page,rows);
        List<TbHtChange> tbHtChangeList=tbHtChangeMapper.getHtChangeList(tbHtChange);
        PageInfo<TbHtChange> pagehelper = new PageInfo<TbHtChange>(tbHtChangeList);
        return pagehelper;
    }

    public boolean applySubmit(TbHtChange tbHtChange){
        boolean boo=false;
        //TODO 启动流程
        String process_start=env.getProperty("process_start").replace("{key}","addChange");
        if(process_start==null){
            logger.info("交接单提交获取process_start失败");
            return false;
        }
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("htNo",tbHtChange.getHtNo());
        param.put("htOwner",tbHtChange.getHtOwner());

        String parameterJson = JSONObject.toJSONString(param);
        String json= HttpUtil.connectURL(process_start,parameterJson,"POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        String process_id=null;
        if(dataJson.getInteger("code")==1){
            JSONObject jsonObject=dataJson.getJSONObject("processInstance");
            process_id=jsonObject.getString("id");
        }else{
            logger.info("交接单流程启动失败");
            return false;
        }
        tbHtChange.setProcessId(process_id);
        tbHtChange.setStatus(1);
        tbHtChangeMapper.saveHtChange(tbHtChange);
        logger.info("合同变更，合同变更id"+tbHtChange.getPkid());
        boo=true;
        return boo;
    }


}
