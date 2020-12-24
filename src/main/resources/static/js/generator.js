$(function () {
    $("#jqGrid").jqGrid({
        url: 'tableSchema/list',
        datatype: "json",
        colModel: [
			{ label: '表名', name: 'tableName', width: 100, key: true },
			{ label: '表备注', name: 'tableComment', width: 100 },
			{ label: '创建时间', name: 'createTime', width: 100 }
        ],
		viewrecords: true,
        height: 1000,
        rowNum: 1000,
		rowList : [10,30,50,100,200],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        jsonReader : {
            root: "data"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
            keywords: null
		}
	},
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'tableName': vm.q.tableName},
                page:1
            }).trigger("reloadGrid");
		},
		generator: function() {
			var tableNames = getSelectedRows();
			if(tableNames == null){
				return ;
			}
			location.href = "tableSchema/generatorCode?tables=" + tableNames.join();
		}
	}
});

