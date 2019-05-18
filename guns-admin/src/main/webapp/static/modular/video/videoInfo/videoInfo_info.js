/**
 * 初始化视频管理详情对话框
 */
var VideoInfoInfoDlg = {
    videoInfoInfoData : {}
};

/**
 * 清除数据
 */
VideoInfoInfoDlg.clearData = function() {
    this.videoInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
VideoInfoInfoDlg.set = function(key, val) {
    this.videoInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
VideoInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
VideoInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.VideoInfo.layerIndex);
}

/**
 * 收集数据
 */
VideoInfoInfoDlg.collectData = function() {
    this
    .set('id')
    .set('videoName')
    .set('videoUrl')
    .set('serviceCreator')
    .set('consumer')
    .set('account')
    .set('createDate');
}

/**
 * 提交添加
 */
VideoInfoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/videoInfo/add", function(data){
        Feng.success("添加成功!");
        window.parent.VideoInfo.table.refresh();
        VideoInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.videoInfoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
VideoInfoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/videoInfo/update", function(data){
        Feng.success("修改成功!");
        window.parent.VideoInfo.table.refresh();
        VideoInfoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.videoInfoInfoData);
    ajax.start();
}

$(function() {
    // 初始化视频上传
    /*
           对于uploader的创建，最好等dom元素也就是下面的div创建好之后再创建，因为里面有用到选择文件按钮，
           不然会创建报错，这是很容易忽视的地方，故这里放到$(function(){}来进行创建*/
    var uploader = WebUploader.create({

        // swf文件路径
        swf: Feng.ctxPath + '/static/js/plugins/webuploader/Uploader.swf',
        // 文件接收服务端。
        server: Feng.ctxPath + '/videoInfo/upload',
        // [默认值：'file']  设置文件上传域的name。
        fileVal:'file',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick:
            {
                multiple: true,
                id: '#picker'
            },

        // 上传并发数。允许同时最大上传进程数[默认值：3]   即上传文件数
        threads: 1,
        // 自动上传修改为手动上传
        //auto: true,
        //是否要分片处理大文件上传。
        //chunked: true,
        // 如果要分片，分多大一片？ 默认大小为5M.
        //chunkSize: 5 * 1024 * 1024,
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false
    });



// 当有文件被添加进队列的时候
    uploader.on( 'fileQueued', function( file ) {
        $("#fileList").append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
            '</div>' );
    });


    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo( $li ).find('.progress-bar');
        }

        $li.find('p.state').text('上传中');

        $percent.css( 'width', percentage * 100 + '%' );
    });
    //点击上传按钮触发事件
    $("#ctlBtn").click(function(){
        uploader.upload();
    });
    uploader.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).find('p.state').text('已上传');

    });

    uploader.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });

    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    })
});
