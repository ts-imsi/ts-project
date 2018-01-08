package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.service.ProjectManagerService;
import com.trasen.tsproject.service.TbPersonnelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/12/25
 */
@RestController
@RequestMapping(value="/projectManager")
public class ProjectManagerController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectManagerController.class);

    @Autowired
    ProjectManagerService projectManagerService;

    @Autowired
    TbPersonnelService tbPersonnelService;

    @RequestMapping(value="/selectProjectManagerList",method = RequestMethod.POST)
    public Map<String,Object> selectProjectManagerList(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try {
            if (param.get("page") == null || param.get("rows") == null) {
                result.put("message", "参数错误");
                result.put("success", false);
                return result;
            }
            TbProjectManager tbProjectManager=new TbProjectManager();
            if(param.get("name")!=null&&!param.get("name").equals("")){
                tbProjectManager.setName(param.get("name"));
            }
            if(param.get("type")!=null&&!param.get("type").equals("")){
                tbProjectManager.setType(param.get("type"));
            }
            PageInfo<TbProjectManager> projectManagerPageInfo=projectManagerService.selectProjectManagerList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbProjectManager);
            logger.info("数据查询条数"+projectManagerPageInfo.getList().size());
            result.put("totalPages",projectManagerPageInfo.getPages());
            result.put("pageNo",projectManagerPageInfo.getPageNum());
            result.put("totalCount",projectManagerPageInfo.getTotal());
            result.put("pageSize",projectManagerPageInfo.getPageSize());
            result.put("list",projectManagerPageInfo.getList());
            result.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }

    @RequestMapping(value="/deleteManager/{pkid}",method = RequestMethod.POST)
    public Result deleteManager(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            projectManagerService.deleteManager(Optional.ofNullable(pkid).orElse(0));
            result.setMessage("数据删除成功");
            result.setSuccess(true);
        }catch (Exception e){
            logger.error("数据删除失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据删除失败");
        }
        return result;
    }

    @RequestMapping(value="/getTreeList",method = RequestMethod.POST)
    public Result getTreeList(){
        Result result=new Result();
        TbTree tree = tbPersonnelService.getParentTree();
        TreeVo treeVo = tbPersonnelService.getTreeList(tree);
        List<TreeVo> list = new ArrayList<>();
        list.add(treeVo);
        result.setObject(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value="/queryPersonById/{perId}",method = RequestMethod.POST)
    public Result queryPersonById(@PathVariable String perId){
        Result result=new Result();
        try{
            TbPersonnel tbPersonnel=tbPersonnelService.queryPersonById(Optional.ofNullable(perId).orElse("0"));
            result.setObject(tbPersonnel);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error("通过组织结构获取人员失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("通过组织结构获取人员失败");
        }
        return result;
    }

    @RequestMapping(value="/saveProjectManager",method = RequestMethod.POST)
    public Result saveProjectManager(@RequestBody TbProjectManager tbProjectManager){
        Result result=new Result();
        try{
            if(tbProjectManager!=null){
                if(tbProjectManager.getPkid()==null){
                    int count=projectManagerService.selectCount(tbProjectManager);
                    if(count>0){
                        result.setSuccess(false);
                        result.setMessage("该用户已经添加，请仔细检查数据");
                    }else{
                        projectManagerService.saveProjectManager(tbProjectManager);
                        result.setSuccess(true);
                        result.setMessage("数据保存成功");
                    }
                }else{
                    projectManagerService.updateProjectManager(tbProjectManager);
                    result.setSuccess(true);
                    result.setMessage("数据更新成功");
                }
            }else{
                result.setSuccess(false);
                result.setMessage("参数传入错误");
            }

        }catch (Exception e){
            logger.error("数据保存或更新失败"+e.getMessage(),e);
            result.setMessage("数据保存或更新失败");
            result.setSuccess(false);
        }
        return  result;
    }
}
