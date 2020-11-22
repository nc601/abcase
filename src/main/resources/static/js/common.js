//<!--该文件存放公共的Java scripts函数 -->
    // 初始化任务名，使其链接到对应的task主页
    function taskNameInit(value, row, index) {
        var color;
        if(row.significance == "致命"){
            color = "#FF0000";
        }else if(row.significance == "严重"){
            color = "#FF0099";
        }else if(row.significance == "提示"){
            color = "gray";
        }else{
            color = "#333333";
        }
        return [
            '<a href="/task/task?taskId='+row.taskId+'" style="color:'+color+'"',
            '>'+row.taskName,
            '</a>'
        ].join('');
    }

    // 修改行样式
    function rowStyle(row, index) {
        return highLightUpdatedRow(row, index);
        // 下面可以修改其它的行样式，需要把几个样式的内容进行合并返回。
    }

    // 显示进度条
    function progressInit(value, row, index) {
        var res = row.progress;
        var color = 'blue';
        if(res > 50){
//            color = 'red';
        }
        return ["<div class='progress'> <div class='progress-bar progress-bar-success' role='progressbar' ",
        "aria-valuenow='50' aria-valuemin='0' aria-valuemax='100' style='width:"+res+"%;color:"+color+"'>"+res+"%</div> </div>"].join("");
        }

    // 问题单初始化，当前仅支持192.168.0.74 问题单库
    function resourceInit(value, row, index){
        if((value!=null) && (value!='') && !isNaN(value))
        {
            return ["<a href='http://192.168.0.74/redmine/issues/"+value+"'>"+value+"</a>"]
        }
        return []
    }

    // 操作列初始化
    function operateFormatter(value, row, index) {
        return [
            '<button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#modifyTaskModal"',
            'onclick="showMymodal('+row.taskId+')">修改',
            '</button>'
        ].join('')
    }

    // 如果任务有更新，高亮显示
    function highLightUpdatedRow(row, index){
        var updateTime=new Date(row.updateTime.replace(/-/g, '/'));
        var curDate = new Date();
        var  prevDay= new Date(curDate.getTime() - 24*60*60*1000); // 昨天这个时候
        var prev2Day = new Date(curDate.getTime() - 48*60*60*1000); // 今天这个时候
        if(updateTime < prev2Day){
            return {classes: 'danger'};
        }else if(updateTime < prevDay){
            return {classes: 'active'};
        } else{
            return {classes: 'info'};
        }
    }

    //获取当前选择的过滤器
    function getCheckedFilter(filter){
        var taskFilter = document.getElementsByName(filter);
        for (var i = 0; i < taskFilter.length; i++) {
            if (taskFilter[i].checked == true) {
                return taskFilter[i].value;
            }
        }
    }




