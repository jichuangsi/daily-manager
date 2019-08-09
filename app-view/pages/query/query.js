//logs.js

const app = getApp()
Page({
    data: {
      openId:1,
      navList:[],
      list: [],
      loginstate: false,
      name: '',
      jobnumber:'',
      psw:'',
      deptId: '',
      roleId: '',
      departmenttext: '',
      positiontext: '',
      departmentstate: false,
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
            success(res) {
              console.log(res)
              self.setData({
                openId: res.data.data.openid,
              })
              self.getdata()
              wx.request({
                url: app.data.API +`/staff/loginStaff/${res.data.data.openid}`,
                header: {
                  'content-type': 'application/json' // 默认值
                },
                success(res) {
                  console.log(res)
                  self.setData({
                    bmlist: res.data.data.departmentList,
                    zwlist: res.data.data.roleList,
                  })
                }
              })
              wx.request({
                url: app.data.API +`/staff/checkStaff/${res.data.data.openid}`,
                header: {
                  'content-type': 'application/json' // 默认值
                },
                success(res) {
                  console.log(res)
                  if (res.data.data.resultCode == 1) {
                    self.setData({
                      loginstate: true
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
    Leavego:function(){
      wx.redirectTo({
        url: '../leave/leave'
      })
    },
    Indexgo:function(){
      wx.redirectTo({
        url: '../index/index'
      })
    },
    navclick: function(e){
        let index = e.currentTarget.dataset.index
        let arrs = this.data.navList;
        if (arrs[index].check == false) {
            arrs[index].check = true;
          } else {
            arrs[index].check = false;
          }
          this.setData({
            navList: arrs
          })
  },
  listclick: function (e) {
    let index = e.currentTarget.dataset.index
    let arrs = this.data.list;
    if (arrs[index].check == false) {
      arrs[index].check = true;
    } else {
      arrs[index].check = false;
    }
    this.setData({
      list: arrs
    })
  },
    getdata:function(){
      let self = this
      wx.request({
        url: app.data.API +`/ol/getolrecord`, //仅为示例，并非真实的接口地址
        method: 'post',
        data: {
          openId: self.data.openId,
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
              arr[i].check = false
            }
            self.setData({
              navList: arr
            })
          }
        },
      })
      wx.request({
        url: app.data.API +`/sq/getsqrecord`, //仅为示例，并非真实的接口地址
        method: 'post',
        data: {
          openId: self.data.openId,
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
              arr[i].check = false
            }
            self.setData({
              list: arr
            })
          }
        },
      })
    },
  timestampToTime: function (timestamp) {
    var date = new Date(parseInt(timestamp));//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = date.getDate() + ' ';
    var h = (date.getHours() >= 10 ? date.getHours() : '0' + date.getHours()) + '：';
    var m = (date.getMinutes() >= 10 ? date.getMinutes() : '0' + date.getMinutes());
    var s = (date.getSeconds() >= 10 ? date.getSeconds() : '0' + date.getSeconds());
    return Y + M + D;
  },
    onLoad: function () {
      let self = this
      wx.getStorage({
        key: 'userid',
        success: function (res) {
          self.setData({
            openId:res.data.id
          })
          self.getdata()
        },
        fail(err) {
          self.login()
        }
      })
    }
  })
  