
const app = getApp()
Page({
  data: {
    StartTime: '17:30',
    EndTime: '19:30',
    num: 0,
    time:'',
    openId: '1',
    text: '',
    state: true,
    loginstate: false,
    name:'',
    jobnumber:'',
    psw:'',
    deptId:'',
    roleId:'',
    departmenttext:'',
    positiontext:'',
    departmentstate:false,
    positionstate: false,
    bmlist: [],
    zwlist: [],
  },
  login() {
    let self = this
    wx.login({
      success(res) {
        console.log(res)
        if (res.code) {
          // 发起网络请求
          wx.request({
            url: app.data.API +`/staff/getWxToken/${res.code}`, //仅为示例，并非真实的接口地址
            header: {
              'content-type': 'application/json' // 默认值
            },
            success (res) {
              console.log(res)
              self.setData({
                openId : res.data.data.openid,
              })
              wx.request({
                url: app.data.API +`/staff/loginStaff/${res.data.data.openid}`,
                header: {
                  'content-type': 'application/json' // 默认值
                },
                success(res){
                  console.log(res)
                    self.setData({
                      bmlist : res.data.data.departmentList,
                      zwlist : res.data.data.roleList,
                    })
                }
              })
              wx.request({
                url: app.data.API +`/staff/checkStaff/${res.data.data.openid}`,
                header: {
                  'content-type': 'application/json' // 默认值
                },
                success(res){
                  console.log(res)
                  if(res.data.data.resultCode == 1){
                    self.setData({
                      loginstate : true
                    })
                  } else if (res.data.data.resultCode == 2) {
                    wx.setStorage({
                      key: 'userid',
                      data: {
                        id: self.data.openId
                      },
                    })
                  }
                }
              })
            },
          })
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    })
  },
  register: function () {
    let self = this
    console.log(self.data.openId)
    wx.request({
      url: app.data.API +`/staff/registStaff`,
      method: 'POST',
      data: {
        name: self.data.name,
        deptId: self.data.deptId,
        roleId: self.data.roleId,
        deptname: self.data.departmenttext,
        roleName: self.data.positiontext,
        opendId: self.data.openId,
        account: self.data.jobnumber,
        pwd: self.data.psw
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        console.log(res)
        if (res.data.code == "0010") {
          wx.setStorage({
            key: 'userid',
            data: {
              id: self.data.openId
            },
          })
          self.setData({
            loginstate: false
          })
          wx.showToast({
            title: "成功",
            icon: 'success',//图标，支持"success"、"loading" 
            duration: 1500,//提示的延迟时间，单位毫秒，默认：1500 
          })
        }
      }
    })
  },
  department: function () {
    console.log(1)
    this.setData({
      departmentstate: !this.data.departmentstate,
      positionstate: false
    })
  },
  departmentclick: function (e) {
    console.log(2)
    let val = e.currentTarget.dataset.text
    let id = e.currentTarget.dataset.id
    console.log(val)
    this.setData({
      deptId: id,
      departmenttext: val,
      departmentstate: !this.data.departmentstate
    })
    console.log(this.data.departmenttext)
  },
  position: function () {
    console.log(this.data.positionstate)
    this.setData({
      positionstate: !this.data.positionstate,
      departmentstate: false
    })
    console.log(this.data.positionstate)
  },
  positionclick: function (e) {
    let val = e.currentTarget.dataset.text
    let id = e.currentTarget.dataset.id
    this.setData({
      roleId: id,
      positiontext: val,
      positionstate: !this.data.positionstate
    })
  },
  ipt: function (e) {
    this.setData({
      name: e.detail.value
    })
  },
  ghipt: function (e) {
    this.setData({
      jobnumber: e.detail.value
    })
  },
  mmipt: function (e) {
    this.setData({
      psw: e.detail.value
    })
  },
  Querygo:function(){
    wx.redirectTo({
      url: '../query/query'
    })
  },
  Indexgo:function(){
    wx.redirectTo({
      url: '../index/index'
    })
  },
  getdata:function(){
    var data = new Date();
    let year = data.getFullYear()
    let month = (data.getMonth() + 1) >= 10 ? (data.getMonth() + 1) : '0' + (data.getMonth() + 1)
    let day = data.getDate() >= 10 ? data.getDate() : '0' + data.getDate()
    console.log(year + '-' + month + '-' + day)
    this.setData({
      StartTime: year + '-' + month + '-' + day,
      EndTime: year  + '-' + month + '-' + day,
      time: year + 1 + '-' + month + '-' + day,
    })
    console.log(this.data.StartTime)
    console.log(this.data.EndTime)
  },
  bindTimeStart: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      StartTime: e.detail.value
    })
  },
  bindTimeEnd: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      EndTime: e.detail.value
    })
  },
  textnum: function (e) {
    // this.num = this.text.length
    this.setData({
      num: e.detail.value.length,
      text: e.detail.value
    })
  },
  btn: function () {
    if (this.data.state) {
      this.setData({
        state: false
      })
      let self = this
      let start = self.data.StartTime + ' 00:00:00'
      let end = self.data.EndTime + ' 00:00:00'
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
          stuas: 2,
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
              text: '',
              num:0
            })
            wx.showToast({
              title: "成功",
              icon: 'success',//图标，支持"success"、"loading" 
              duration: 1500,//提示的延迟时间，单位毫秒，默认：1500 
            })
            wx.redirectTo({
              url: '../index/index'
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
    let self = this
    self.getdata()
    wx.getStorage({
      key: 'userid',
      success: function (res) {
        self.setData({
          openId: res.data.id
        })
      },
      fail(err) {
        self.login()
      }
    })
  }
})