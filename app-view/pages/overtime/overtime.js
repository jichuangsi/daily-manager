Page({
    data: {
        StartTime: '17:30',
        EndTime: '19:30',
        num:0,
        openId:'1',
        text:'',
        state:true
    },
    bindTimeStart: function(e) {
        console.log('picker发送选择改变，携带值为', e.detail.value)
        this.setData({
            StartTime: e.detail.value
        })
    },
    bindTimeEnd: function(e) {
        console.log('picker发送选择改变，携带值为', e.detail.value)
        this.setData({
            EndTime: e.detail.value
        })
    },
    textnum: function (e){
      // this.num = this.text.length
      this.setData({
        num:e.detail.value.length,
        text: e.detail.value
      })
    },
    btn:function(){
      if (this.data.state){
        this.setData({
          state: false
        })
        let self = this
        let data = new Date()
        let year = data.getFullYear()
        let month = (data.getMonth() + 1) >= 10 ? (data.getMonth() + 1) : '0' + (data.getMonth() + 1)
        let day = data.getDate() >= 10 ? data.getDate() : '0' + data.getDate()
        let start = year + '-' + month + '-' + day + ' ' + self.data.StartTime + ':00'
        let end = year + '-' + month + '-' + day + ' ' + self.data.EndTime + ':00'
        start = start.replace(/-/g, '/');
        var starttime = new Date(start);
        starttime = starttime.getTime();
        end = end.replace(/-/g, '/');
        var endtime = new Date(end);
        endtime = endtime.getTime();
        wx.request({
          url: app.data.API +`/ol/ol`, //仅为示例，并非真实的接口地址
          method: 'post',
          data: {
            openId: self.data.openId,
            stuas: 1,
            msg: self.data.text,
            start: starttime,
            end: endtime
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded' // 默认值
          },
          success(res) {
            console.log(res)
            self.setData({
              state: true
            })
            if (res.data.code == '0010') {
              self.setData({
                text: ''
              })
              wx.showToast({
                title: "成功",
                icon: 'success',//图标，支持"success"、"loading" 
                duration: 1500,//提示的延迟时间，单位毫秒，默认：1500 
              })
            } else {
              wx.showToast({
                title: "网络不稳定，请重新申请",
                icon: 'none',//图标，支持"success"、"loading" 
                duration: 2000,//提示的延迟时间，单位毫秒，默认：1500 
              })
            }
          },
        })
      }
    },
    onLoad: function () {
      let self =this
      wx.getStorage({
        key: 'userid',
        success: function (res) {
          self.setData({
            openId: res.data.id
          })
        },
      })
    }
})