/**
 * 视频管理管理初始化
 */
var VideoInfo = {
    id: "VideoInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
VideoInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '视频名称', field: 'videoName', visible: true, align: 'center', valign: 'middle'},
            {title: '视频地址', field: 'videoUrl', visible: true, align: 'center', valign: 'middle'},
            {title: '创建人', field: 'serviceCreator', visible: true, align: 'center', valign: 'middle'},
            {title: '照片拥有者', field: 'consumer', visible: true, align: 'center', valign: 'middle'},
            {title: '所属平台用户', field: 'account', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createDate', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
VideoInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        VideoInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加视频管理
 */
VideoInfo.openAddVideoInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加视频管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/videoInfo/videoInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看视频管理详情
 */
VideoInfo.openVideoInfoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '视频管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/videoInfo/videoInfo_update/' + VideoInfo.seItem.id
        });
        this.layerIndex = index;
    }
};
/**
 * 打开上传窗口
 */
VideoInfo.openVideoInfoUpload = function () {
    var index = layer.open({
        type: 2,
        title: '视频管理上传',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/videoInfo/videoInfo_upload',
        end : function() {
            console.log("回调成功")
            window.VideoInfo.table.refresh();
        }

    });
    this.layerIndex = index;
};


/**
 * 删除视频管理
 */
VideoInfo.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/videoInfo/delete", function (data) {
            Feng.success("删除成功!");
            VideoInfo.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("videoInfoId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 上传视频管理
 */
VideoInfo.upload = function () {
    console.log("触发文件上传方法");
    var avatarUp = new $WebUpload("avatar");
    avatarUp.init();
};



/**
 * 查询视频管理列表
 */
VideoInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    VideoInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = VideoInfo.initColumn();
    var table = new BSTable(VideoInfo.id, "/videoInfo/list", defaultColunms);
    table.setPaginationType("client");
    VideoInfo.table = table.init();



});
