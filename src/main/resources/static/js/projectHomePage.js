<!--//多行内容一行显示  点击就显示多行-->
    var tableMethod = {
        showMore:function(obj){
            if($(obj).css('white-space') == 'normal'){
                $(obj).css('white-space','nowrap')
            }else{
                $(obj).css('white-space','normal')
            }
        }
    }
    $(function() {
        $('.txt').bind('click', function () {
            tableMethod.showMore(this);
        })
    })

    // 根据状态过滤任务，使页面只显示给定状态的任务
    function filterStatus(){
        var statusFilter =  getCheckedFilter("statusFilter");
        var significanceFilter = getCheckedFilter("significanceFilter");
        $.ajax({
              type: "POST",
              data: {
                  status: statusFilter,
                  significance: significanceFilter,
                  projectName:$("#chooseProject option:selected").val(),
                  owner: $('#userName').text()
              },
              url: "/task/filterStatusForProject",
              success: function (data) {
                  $('#table').bootstrapTable('load', data);
              },
              error: function (data) {
                  alert("filterStatus 失败");
              }
        });
    }

    function chooseProjectChange(team){
        filterStatus();
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
        // 获取project list
//        getTeams();

        // 根据用户设置默认的project
//        setDefaultTeam();

        // 刷新task list
        filterStatus()
    });
