//<!--根据taskID，查询task信息，并返回到页面显示-->
    function showMymodal(taskId){
        getAllMembers();// 获取所有的members，同步ajax函数。
        $.ajax({
            type: "POST",
            data: {taskId: taskId},
            url: "/task/getTaskById",
            success: function (data) {
                $('#taskId').val(data.taskId);
                $('#projectId').val(data.projectId);
                $('#taskName').val(data.taskName);
                $('#subTaskName').val(data.subTaskName);
                $('#taskDesc').val(data.taskDesc);
                $('#progress').val(data.progress);
                if(data == "")  {$('#progress').val(0);}// 如果没有找到task，进度默认设置为0.
                $('#startTime').val(data.startTime);
                $('#endTime').val(data.endTime);
                $('#progressDesc').val(data.progressDesc);
                $('#owner').val(data.owner);
                $('#others').val(data.others);
                $('#resource').val(data.resource);
                $('#significance').val(data.significance);
                $('#status').val(data.status);
                $('#priority').val(data.priority);
                $('#vote').val(data.vote);
            },
            error: function (data) {
                alert("showMymodal 失败");
            }
        });
    }

    <!--更新/修改task相关内容-->
    function updateMymodal(){
        $.ajax({
            type: "POST",
            data: {
                taskId: $('#taskId').val(),
                projectId: $('#projectId').val(),
                taskName: $('#taskName').val(),
                subTaskName: $('#subTaskName').val(),
                taskDesc: $('#taskDesc').val(),
                progress: $('#progress').val(),
                startTime: $('#startTime').val(),
                endTime: $('#endTime').val(),
                progressDesc: $('#progressDesc').val(),
                owner: $('#owner').val(),
                others: $('#others').val(),
                resource: $('#resource').val(),
                significance: $('#significance').val(),
                status: $('#status').val(),
                priority: $('#priority').val(),
                vote: $('#vote').val()
                },
            url: "/task/updateTask",
            success: function (data) {
                filterStatus();// 更新完之后使用过滤状态的函数刷新任务列表。
                setTimeout(function(){
                    $('#modifyTaskModal').modal('hide');
                    $(".modal-backdrop.fade").hide();
                    // $("#myModal").trigger("click");
                    },200)
                //window.location.reload();
                //location.reload();
            },
            error: function (data) {
                alert("updateMymodal 失败");
            }
        });
    }

    // 根据状态过滤任务，使页面只显示给定状态的任务
    function filterStatus(){
        var statusFilter =  getCheckedFilter("statusFilter");
        var significanceFilter = getCheckedFilter("significanceFilter");
        var user = $("#chooseMember option:selected").val();
        if(user == "成员")
        {
            user = $('#userName').text();
        }

        $.ajax({
              type: "POST",
              data: {
                  status: statusFilter,
                  significance: significanceFilter,
                  owner: user,
              },
              url: "/task/filterStatus",
              success: function (data) {
                  $('#table').bootstrapTable('load', data);
              },
              error: function (data) {
                  alert("filterStatus 失败");
              }
        });
    }

    function sendMailButtonForm(mailList, mailContent){
        var href = "mailto:" + mailList + "?subject=xxx_yth_week_report&body=" + mailContent;
        document.getElementById("sendUserWeeklyReport").setAttribute("href", href);
        return;
    }

    //生成个人周报
    function makeUserWeeklyReport(){
        $.ajax({
            type: "POST",
            data: {
                userName: $("#userName").text()
            },
            url: "/report/getWeeklyReport",
            success: function (data) {
                $('#mailList').val(data.mailList);
                $('#mailContent').val(data.mailContent);
                sendMailButtonForm(data.mailList, data.mailContent);
            },
            error: function (data) {
                alert("makeUserWeeklyReport 失败");
            }
        });
    }

    function updateMailList(){
        $.ajax({
            type: "POST",
            data: {
                userName: $("#userName").val(),//什么时候用text，什么时候用val?
                mailList: $("#mailList").val()
            },
            url: "/report/updateMailList",
            success: function (data) {
                alert("update mail list success.")
            },
            error: function (data) {
                alert("update mail list failed.");
            }
        });
    }

    function advanceFilterButton(){
        var advanceFilterLabel = document.getElementById('advanceFilterLabel');
        if(advanceFilterLabel.hidden){
            showAdvanceFilter();
            getTeams();
        }else{
            hideAdvanceFilter();
            //filterStatus(); // 重新刷新任务列表
        }

    }
    function showAdvanceFilter(){
        var advanceFilterLabel = document.getElementById('advanceFilterLabel');
        var chooseTeamLabel = document.getElementById('chooseTeamLabel');
        var chooseUserLabel = document.getElementById('chooseMemberLabel');
        advanceFilterLabel.hidden = false;
        chooseTeamLabel.hidden = false;
        chooseUserLabel.hidden = false;
    }

    function hideAdvanceFilter(){
        var advanceFilterLabel = document.getElementById('advanceFilterLabel');
        var chooseTeamLabel = document.getElementById('chooseTeamLabel');
        var chooseUserLabel = document.getElementById('chooseMemberLabel');
        var chooseTeam = document.getElementById('chooseTeam');
        var chooseMember = document.getElementById('chooseMember');

        advanceFilterLabel.hidden = true;
        chooseTeamLabel.hidden = true;
        chooseUserLabel.hidden = true;
        chooseTeam.options.length = 1;// 仅保留第一个默认option
        chooseMember.options.length = 1;// 仅保留第一个默认option
    }

    function chooseTeamChange(team){
        $.ajax({
                type: "POST",
                data: {teamName:team.value},
                url: "/user/getTeamMembers",
                success: function (data) {
                    var chooseMember = document.getElementById('chooseMember');
                    chooseMember.options.length = 1;// 仅保留默认option，然后再添加
                    for(var i=0; i<data.length; i++){
                        chooseMember.options.add(new Option(data[i], data[i])); //这个兼容IE与firefox
    //                    chooseMember.add(new Option(data[i], data[i])); //这个只能在IE中有效
                    }
                },
                error: function (data) {
                    alert("chooseTeamOnChange failed.");
                }
            });
    }

    function chooseMemberChange(team){
            filterStatus()
    }

    function getTeams(){
             $.ajax({
                 type: "POST",
                 data: {},
                 url: "/user/getTeams",
                 success: function (data) {
                     var chooseTeam = document.getElementById('chooseTeam');
                     for(var i=0; i<data.length; i++){
                         chooseTeam.options.add(new Option(data[i], data[i])); //这个兼容IE与firefox
                     //  chooseTeam.add(new Option(data[i], data[i])); //这个只能在IE中有效
                     }
                 },
                 error: function (data) {
                     alert("getTeams failed.");
                 }
             });
         }

        function getAllMembers(){
            $.ajax({
                type: "POST",
                data: {},
                async:false,// 同步执行
                url: "/user/getAllMembers",
                success: function (data) {
                    var chooseTeam = document.getElementById('owner');
                    for(var i=0; i<data.length; i++){
                        chooseTeam.options.add(new Option(data[i], data[i])); //这个兼容IE与firefox
                    }
                },
                error: function (data) {
                    alert("getAllMembers failed.");
                }
            });
        }

    $("#modifyTaskModalForm").submit(function(){
        updateMymodal();
        return false;// 执行后不进行页面刷新
    })

    // 当选择的status发生变化时，刷新task table。
    $('input[type=radio][name=statusFilter]').change(function(){
        filterStatus();
    });

    // 当选择的significance发生变化时，刷新task table。
    $('input[type=radio][name=significanceFilter]').change(function(){
        filterStatus();
    });

    // 页面加载完后初始化table
    $(document).ready(function(){
        filterStatus()
    });