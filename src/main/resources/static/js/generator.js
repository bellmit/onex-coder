$(function () {
    $("#jqGrid").jqGrid({
        url: 'tableSchema/list',
        datatype: "json",
        ajaxGridOptions: {
            contentType: "application/json",
        },
        mtype: "POST",
        serializeGridData: function(postData) {
            return JSON.stringify(postData);
        },
        colModel: [
            {label: '表名', name: 'tableName', width: 100, key: true},
            {label: '表备注', name: 'tableComment', width: 100},
            {label: '创建时间', name: 'createTime', width: 100}
        ],
        viewrecords: true,
        height: 1000,
        rowNum: 1000,
        rowList: [10, 30, 50, 100, 200],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        jsonReader: {
            root: "data"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            tableNames: null,
            driverClassName: 'com.mysql.cj.jdbc.Driver',
            dbUrl: 'jdbc:mysql://127.0.0.1:3306/test',
            dbUsername: 'root',
            dbPassword: 'root',
        }
    },
    methods: {
        // 查询
        query: function () {
            $("#jqGrid").jqGrid('setGridParam', {postData: vm.q}).trigger("reloadGrid");
        },
        // 生成代码
        generateCode: function () {
            var tableNames = getSelectedRows();
            if (tableNames == null) {
                return;
            }
            location.href = "tableSchema/generateCode?tableNames=" + tableNames.join();
        },
        // 生成文档
        generateDoc: function () {
            var tableNames = getSelectedRows();
            if (tableNames == null) {
                return;
            }
            location.href = "tableSchema/generateDoc?tableNames=" + tableNames.join();
        }
    }
});

