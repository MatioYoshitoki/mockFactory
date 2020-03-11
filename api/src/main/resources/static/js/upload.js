function FileUpload(id, options) {
    this.me = $(id);
    const defaultOpt = {};
    this.preview = $(
        '<div id="boxBoard">' +
        '<img class="upload" src="image/upload.png" alt="" title="">' +
        '</div>');
    this.opts=$.extend(true, defaultOpt,{}, options);
    this.init();
    this.callback = this.opts.callback;
}

//定义原型方法
FileUpload.prototype = {
    init:function () {
        this.me.append(this.preview);
        this.cssInit();
        this.eventClickInit();
    },
    cssInit:function () {
        this.me.css({
            'padding':'10px',
            'cursor':'pointer'
        });
        
        this.preview.css({
            'height':'100%',
            'overflow':'hidden'
        });
        
    },
    eventClickInit:function () {
        const self = this;
        this.me.unbind().click(function () {
            self.createImageUploadDialog();
        });
        const dp = this.me[0];
        dp.addEventListener('dragover', function(e) {
            self.onDragover(e);
        });
        dp.addEventListener("drop", function(e) {
            self.onDrop(e);
        });


    },
    onChangeUploadFile:function () {
        const fileInput = this.fileInput;
        const files = fileInput.files;
        const file = files[0];
        const filename = file.name;
        if(this.callback){
            this.callback(files);
        }
    },
    createImageUploadDialog:function () {
        let fileInput = this.fileInput;
        if (!fileInput) {
            fileInput = document.createElement('input');
            fileInput.type = 'file';
            fileInput.name = 'ime-images';
            fileInput.multiple = true;
            fileInput.onchange  = this.onChangeUploadFile.bind(this);
            this.fileInput = fileInput;
        }
        fileInput.click();
    }
}