var UploadUtil = {
    fs: window.Electron.require('fs'),
    mime: window.Electron.require('mime'),
    test: function(){
        var path = "C:\\Users\\elvis\\Downloads\\404错误.log";
        var uploadUri = "/upload.do";
        this.readAndUpload(path, uploadUri);
    },
    // 读取并上传文件
    readAndUpload: function(path, uploadUri){
        var _self=this;
        _self.getFileStats(path, function(err, stats){
            if(err) return;
            // 读取文件 --begin
            var readStream = _self.fs.createReadStream(path);
            var blobParts;
            readStream.on('open', function(fd){
                blobParts = new Array();
                console.log("Read \'"+path+"\' open");
            });
            readStream.on('data', function(data){
                var blob = new Blob([data], {type: stats.type});
                blobParts.push(blob);
            });
            readStream.on('end', function(){
                console.log("Read \'"+path+"\' end");
                ajaxUpload(blobParts, stats);
            });
            readStream.on('close', function(){
                console.log("Read \'"+path+"\' close");
            });
            readStream.on('error', function(err){
                console.info('[!ERR!] Read \''+path+'\' failed!');
                // 读取过程中出错了，清空数据
                blobParts.splice(0, blobParts.length);
            });
            // 读取文件 --end
        });

        function ajaxUpload(blobParts, stats) {
            if(!blobParts || blobParts.length <= 0) return;
            var xhr = new XMLHttpRequest();
            xhr.open("POST", uploadUri, true);

            // 封装file对象
            var file = new File(blobParts, stats.name);
            file.path = stats.path;
            var formdata = new FormData();
            formdata.append('enctype', 'multipart/form-data')
            formdata.append('Filedata', file);

            xhr.onload = function (evt) {
                console.log("上传完成");
            }

            xhr.send(formdata);
        }
    },

    // 读取文件信息
    getFileStats:function(path, callback){
        var _self=this;
        _self.fs.stat(path, function(err, stats){
            if(typeof callback != 'function') return;
            if(err) callback(err);
            else{
                var index = path.lastIndexOf('\\');
                var len = path.length;
                var filename = index != -1? path.substring(index+1, len):path;
                var mimetype = _self.mime.lookup(path)
                callback(null, {size:stats.size, name:filename, path:path, type:mimetype});
            }
        });
    }
};