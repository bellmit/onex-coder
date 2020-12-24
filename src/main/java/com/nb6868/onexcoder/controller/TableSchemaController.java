package com.nb6868.onexcoder.controller;

import com.nb6868.onexcoder.service.TableSchemaService;
import com.nb6868.onexcoder.utils.Result;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 表结构
 *
 * @author Charles (zhanngchaoxu@gmail.com)
 */
@Controller
@RequestMapping("/tableSchema")
public class TableSchemaController {

    @Autowired
    private TableSchemaService tableSchemaService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public Result list(@RequestParam(required = false) String tableName) {
        try {
            return Result.ok().put("data", tableSchemaService.queryList(tableName));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 生成代码
     */
    @RequestMapping("/generatorCode")
    public void code(HttpServletResponse response, @RequestParam String tableName) throws Exception {
        byte[] data = tableSchemaService.generatorCode(tableName.split(","));

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + tableName + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
