//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    msg:'',
    ruleId:'',
    deptId:'',
    roleId:'',
    openId:'1',
    name:'',
    jobnumber:'',
    psw:'',
    imgsrc:'',
    Week:'',
    date:'',
    time:'',
    timer:'',
    wifiName:'',
    latitude:'',
    longitude:'', //经度
    departmenttext:'',
    positiontext:'',
    positionstate:0,
    cdstate:false,
    clickstate:false,
    Obtaintext:'请点击获取当前位置',
    remindstate:false,
    rankstate:false,
    wzstate:false,
    wzstate1:false,
    wfstate:false,
    loginstate:false,
    departmentstate:false,
    positionstate:false,
    dakastate:false,
    bmlist:[],
    zwlist:[],
    dklist:[],
    ranklist:[]
  },
  //事件处理函数
  Querygo:function(){
    wx.redirectTo({
      url: '../query/query'
    })
  },
  Leavego:function(){
    wx.redirectTo({
      url: '../leave/leave'
    })
  },
  department:function(){
    console.log(1)
    this.setData({
      departmentstate:!this.data.departmentstate,
      positionstate:false
    })
  },
  departmentclick:function(e){
    console.log(2)
    let val = e.currentTarget.dataset.text
    let id = e.currentTarget.dataset.id
    console.log(val)
    this.setData({
      deptId:id,
      departmenttext: val,
      departmentstate: !this.data.departmentstate
    })
    console.log(this.data.departmenttext)
  },
  position:function(){
    console.log(this.data.positionstate)
    this.setData({
      positionstate: !this.data.positionstate,
      departmentstate:false
    })
    console.log(this.data.positionstate)
  },
  positionclick:function(e){
    let val = e.currentTarget.dataset.text
    let id = e.currentTarget.dataset.id
    this.setData({
      roleId:id,
      positiontext: val,
      positionstate: !this.data.positionstate
    })
  },
  AppealgoId:function(e){
    let id = e.currentTarget.dataset.id
    clearInterval(this.data.timer)
    wx.navigateTo({
      url: `../Appeal/Appeal?id=${id}`
    })
  },
  Appealgo: function (){
    this.setData({
      remindstate:false
    })
    clearInterval(this.data.timer)
    wx.navigateTo({
      url: `../Appeal/Appeal?id=${this.data.ruleId}`
    })
  },
  getdata: function (){
    console.log(this.data.Obtaintext)
    let self = this
    let data = new Date()
    let year = data.getFullYear()
    let month = (data.getMonth()+1)>=10?(data.getMonth()+1):'0'+(data.getMonth()+1)
    let day = data.getDate()>=10?data.getDate():'0'+data.getDate()
    let week = data.getDay()
    let h = data.getHours()>=10?data.getHours():'0'+data.getHours()
    let m = data.getMinutes()>=10?data.getMinutes():'0'+data.getMinutes()
    let s = data.getSeconds()>=10?data.getSeconds():'0'+data.getSeconds()
    self.setData({
      date:year + '年' +month+'月'+day+'日',
      time : h+':'+m+':'+s
    })
    switch (week){
      case 0:
        self.setData({
            Week:'星期天',
          })
        break;
      case 1:
        self.setData({
            Week:'星期一',
          })
        break;
      case 2:
        self.setData({
            Week:'星期二',
          })
        break;
      case 3:
        self.setData({
            Week:'星期三',
          })
        break;
      case 4:
        self.setData({
            Week:'星期四',
          })
        break;
      case 5:
        self.setData({
            Week:'星期五',
          })
        break;
      case 6:
        self.setData({
            Week:'星期六',
          })
        break;
    }
    wx.request({
      url: app.data.API+`/kq/getdakapaixingbang`,
      method: 'POST',
      data: {
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res) {
        console.log(res)
        if (res.data.code == '0010') {
          let arr = res.data.data
          for (let i = 0; i < arr.length; i++) {
            arr[i].time = self.timestampToTime(arr[i].time)
          }
          self.setData({
            ranklist: arr
          })
        }
      }
    })


  },
  gettime: function (){
    let self = this
    self.data.timer = setInterval(function(){
      let data = new Date()
      let h = data.getHours()>=10?data.getHours():'0'+data.getHours()
      let m = data.getMinutes()>=10?data.getMinutes():'0'+data.getMinutes()
      let s = data.getSeconds()>=10?data.getSeconds():'0'+data.getSeconds()
      self.setData({
        time : h+':'+m+':'+s
      })
    },1000)
  },
      wz: function (){
        let self = this
        self.setData({
          Obtaintext : '请稍后...',
          clickstate : true
        })
        wx.getLocation({
          type: 'gcj02',
          altitude:true,
          success (res) {
            console.log(res)
            self.setData({
                Obtaintext : '成功',
                clickstate : false,
                wzstate: true,
                wzstate1: false,
                latitude: res.latitude,
                longitude: res.longitude
            })
            wx.setStorage({
              key: 'wz',
              data: {
                latitude: res.latitude,
                longitude: res.longitude,
                time: Date.parse(new Date())
              },
            })
            
          },
          fail(err){
              self.setData({
                Obtaintext : '请点击获取当前位置',
                clickstate : false,
                wzstate1:true
              })
            }
          })
        // self.wf()
      },
      wf: function (){
        // wx.stopWifi({
        //   success (res) {
        //     console.log(res.errMsg)
        //   }
        // })
        let self = this
        wx.startWifi({
          success (res) {
            console.log(res)
            wx.getConnectedWifi({
              success (res) {
            console.log(456)
                console.log(res)
                  self.setData({
                    wifiName: res.wifi.SSID,
                    wfstate:true
                  })
                wx.setStorage({
                  key: 'wf',
                  data: {
                    wifiName: res.wifi.SSID,
                    time: Date.parse(new Date())
                  },
                })
                  console.log(self.data.wifiName)
              },
              fail(err){
                console.log(err)
                if(err.errCode == '12005'){
                  wx.showToast({
                           title:"请连接专用WIFI",
                           icon: 'none',//图标，支持"success"、"loading" 
                           duration: 1000,//提示的延迟时间，单位毫秒，默认：1500 
                         })
                }
                else if(err.errCode == '12006'){
                  wx.showToast({
                           title:"请连接专用WIFI",
                           icon: 'none',//图标，支持"success"、"loading" 
                           duration: 1000,//提示的延迟时间，单位毫秒，默认：1500 
                         })
                }
              }
            })
          }
        })
      },
      clock: function (e){
        let id = e.currentTarget.dataset.id
        console.log(this.data.longitude+','+this.data.latitude)
        let self = this
        let newtime = Date.parse(new Date())
        if (self.data.wifiName == '' && self.data.latitude == ''){
          self.wf()
          wx.showToast({
            title: "获取wifi名字成功",
            icon: 'success',//图标，支持"success"、"loading" 
            duration: 2000,//提示的延迟时间，单位毫秒，默认：1500 
          })
        }else{
          // !self.data.wfstate && !self.data.wzstate &&
          if (!self.data.dakastate) {
            wx.showToast({
              title: "请连接指定WIFI",
              icon: 'none',//图标，支持"success"、"loading" 
              duration: 2000,//提示的延迟时间，单位毫秒，默认：1500 
            })
          } else {
            self.setData({
              dakastate: false
            })
            wx.request({
              url: app.data.API + '/kq/daka', //仅为示例，并非真实的接口地址
              method: 'POST',
              data: {
                ruleId: id,
                wifiName: self.data.wifiName,
                longitudeLatitude: self.data.longitude + ',' + self.data.latitude,
                openId: self.data.openId
              },
              header: {
                'content-type': 'application/x-www-form-urlencoded' // 默认值
              },
              success(res) {
                console.log(res)

                if (res.data.code == '0050') {
                  self.setData({
                    dakastate: true
                  })

                  wx.showToast({
                    title: "打卡失败",
                    icon: 'none',//图标，支持"success"、"loading" 
                    duration: 2500,//提示的延迟时间，单位毫秒，默认：1500 
                  })
                }
                if (res.data.data[0] == '0' || res.data.data[0] == '2' || res.data.data[0] == '3') {
                  wx.showToast({
                    title: "成功",
                    icon: 'success',//图标，支持"success"、"loading" 
                    duration: 1500,//提示的延迟时间，单位毫秒，默认：1500 
                  })
                  self.setData({
                    ruleId: id,
                  })
                  self.getrule()
                  self.getdata()
                  if (res.data.data[0] == '2') {
                    self.setData({
                      cdstate: true
                    })
                  }
                } else {
                  self.setData({
                    remindstate: true,
                    ruleId: id,
                    msg: '打卡异常，请前往申诉',
                    dakastate: true
                  })
                }
              },
              fail(err) {
                self.setData({
                  dakastate: true
                })
              }
            })
          }
        }
      },
      remindstateout: function (){
        this.setData({
          remindstate : false
        })
      },
      cdstateout: function () {
        this.setData({
          cdstate: false
        })
      },
      cdclcik:function(){

      },
      rankcilck: function (){
        this.setData({
          rankstate : !this.data.rankstate
        })
      },
      clockclick: function(){
      },
      overtimego: function(){
        clearInterval(this.data.timer)
        wx.navigateTo({
          url: '../overtime/overtime'
        })
      },
  login(){
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
              
              self.getrule()
              wx.request({
                url: app.data.API +`/staff/loginStaff/${res.data.data.openid}`,
                header: {
                  'content-type': 'application/json' // 默认值
                },
                success(res) {
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
                  } else if (res.data.data.resultCode == 2){
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
  ipt:function(e){
    this.setData({
      name:e.detail.value
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
  register:function (){
    let self = this
    console.log(self.data.openId)
    wx.request({
      url: app.data.API +`/staff/registStaff`,
      method:'POST',
      data:{
        name:self.data.name,
        deptId:self.data.deptId,
        roleId:self.data.roleId,
        deptname:self.data.departmenttext,
        roleName:self.data.positiontext,
        opendId:self.data.openId,
        account:self.data.jobnumber,
        pwd:self.data.psw
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res){
        console.log(res)
        if(res.data.code == "0010"){
          wx.setStorage({
            key: 'userid',
            data: {
              id: self.data.openId
            },
          })
        self.setData({
          loginstate:false
        })
        wx.showToast({
                 title:"成功",
                 icon: 'success',//图标，支持"success"、"loading" 
                 duration: 1500,//提示的延迟时间，单位毫秒，默认：1500 
               })
        } else {
          wx.showToast({
            title: "失败",
            icon: 'none',//图标，支持"success"、"loading" 
            duration: 2500,//提示的延迟时间，单位毫秒，默认：1500 
          })
        }
      }
    })
  },
  getrule:function(){
    let self = this
    wx.request({
      url: app.data.API +`/kq/getrulelist`,
      method:'POST',
      data:{
        openId: self.data.openId
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res){
        console.log(res)
        if(res.data.code =='0010'){
          let arr = res.data.data
          for (let i = 0; i < arr.length; i++) {
            arr[i].sj = self.timestampToTime(arr[i].time)
          }
          self.setData({
            dklist: arr,
            dakastate:true
          })
          console.log(self.data.dklist)
        }

      }
    })
  },
  timestampToTime:function (timestamp) {
    var date = new Date(parseInt(timestamp));//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    // var Y = date.getFullYear() + '-';
    // var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    // var D = date.getDate() + ' ';
    var h = (date.getHours() >= 10 ? date.getHours() : '0' + date.getHours()) + '：';
    var m = (date.getMinutes() >= 10 ? date.getMinutes() : '0' + date.getMinutes());
    var s = (date.getSeconds() >= 10 ? date.getSeconds() : '0' + date.getSeconds());
    return  h + m ;
  },
  onLoad: function () {
    let self = this
    self.getdata()
    self.gettime()
    wx.getStorage({
      key: 'userid',
      success: function (res) {
        self.setData({
          openId: res.data.id
        })
        self.getrule()
      },
      fail(err) {
        self.login()
      }
    })
    wx.getStorage({
      key: 'wz',
      success: function (res) {
        console.log(Date.parse(new Date()) - res.data.time)
        if (Date.parse(new Date()) - res.data.time <= 900000) {
          self.setData({
            Obtaintext: '成功',
            latitude: res.data.latitude,
            longitude: res.data.longitude
          })
        }
      },
      fail(err) {
      }
    })
    wx.getStorage({
      key: 'wf',
      success: function (res) {
        if (Date.parse(new Date()) - res.data.time <= 900000) {
          self.setData({
            wifiName: res.data.wifiName
          })
        }else{
          self.wf()
        }
      },
      fail(err) {
        self.wf()
      }
    })
  },
  onUnload (){
    clearInterval(this.data.timer)
  }
})
