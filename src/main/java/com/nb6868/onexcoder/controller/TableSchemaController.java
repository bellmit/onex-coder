package com.nb6868.onexcoder.controller;

import com.nb6868.onexcoder.entity.DbConfigRequest;
import com.nb6868.onexcoder.service.TableSchemaService;
import com.nb6868.onexcoder.utils.Result;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("list")
    public Result<?> list(@RequestBody DbConfigRequest request) {
        try {
            return new Result<>().success(tableSchemaService.queryList(request));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>().error();
        }
    }

    /**
     * 生成代码
     */
    @PostMapping("/generateCode")
    public void generateCode(HttpServletResponse response, @RequestBody DbConfigRequest request) throws Exception {
        byte[] data = tableSchemaService.generateCode(request);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getTableNames() + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 生成数据库文档
     */
    @RequestMapping("/generateDoc")
    public void generateDoc(HttpServletResponse response, @RequestBody DbConfigRequest request) {
        tableSchemaService.generateDoc(request);
    }
}
