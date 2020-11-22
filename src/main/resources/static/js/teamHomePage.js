    // 根据状态过滤任务，使页面只显示给定状态的任务
    function filterStatus(){
        var statusFilter =  getCheckedFilter("statusFilter");
        var significanceFilter = getCheckedFilter("significanceFilter");
        $.ajax({
              type: "POST",
              data: {
                  status: statusFilter,
                  significance: significanceFilter,
                  teamName:$("#chooseTeam option:selected").val(),
                  owner: $('#userName').text()
              },
              url: "/task/filterStatusForTeam",
              success: function (data) {
                  $('#table').bootstrapTable('load', data);
              },
              error: function (data) {
                  alert("filterStatus 失败");
              }
        });
    }

    function chooseTeamChange(team){
        filterStatus();
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
                }
                setDefaultTeam();
            },
            error: function (data) {
                alert("getTeams failed.");
            }
        });
    }

    function setDefaultTeam(){
        $.ajax({
            type: "POST",
            data: {owner: $('#userName').text()},
            url: "/user/getTeamByUser",
            success: function (data) {
                var chooseTeam = document.getElementById('chooseTeam');
                for(var i=0; i<chooseTeam.length; i++){
                    if(chooseTeam.options[i].value == data){
                        chooseTeam.options[i].selected = true;
                    }else{
                        chooseTeam.options[i].selected = false;
                    }
                }

                // 刷新任务
                filterStatus();
            },
            error: function (data) {
                alert("setDefaultTeam failed.");
            }
        });
    }

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
        // 获取team list
        getTeams();
        // 根据用户设置默认的team
        // 刷新task list
//        filterStatus()
    });
