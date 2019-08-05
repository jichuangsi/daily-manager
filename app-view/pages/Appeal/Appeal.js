//logs.js

Page({
  data: {
    num:0,
    imglist:[],
    text:'',
    openId:'1',
    ruleId:'2',
    state:true
  },
  tp: function (){
    let self = this
    if(self.data.imglist.length<5){
      wx.chooseImage({
        count: 5,
        sizeType: ['compressed'], //'original',
        sourceType: ['album', 'camera'],
        success (res) {
          console.log(res)
          // tempFilePath可以作为img标签的src属性显示图片
          self.setData({
            imglist:self.data.imglist.concat(...res.tempFilePaths)
          })
          if (self.data.imglist.length>=5){
            self.setData({
              imglist: self.data.imglist.slice(0,5)
            })
          }
          console.log(self.data.imglist)
          // self.imgsrc = res.tempFilePaths
        }
      })
    }else{
      wx.showToast({
        title: "上传图片不超过5张",
        icon: 'none',//图标，支持"success"、"loading" 
        duration: 2000,//提示的延迟时间，单位毫秒，默认：1500 
      })
    }
  },
  textnum: function (e){
    // this.num = this.text.length
    this.setData({
      num:e.detail.value.length,
      text:e.detail.value
    })
  },
  out: function (e){
    let index = e.currentTarget.dataset.index
    this.data.imglist.splice(index,1)
    this.setData({
      imglist:this.data.imglist
    })
  },
  changex: function (e){
    let self = this
    let index = e.currentTarget.dataset.index
    wx.previewImage({
      current: self.data.imglist[index],  //当前预览的图片
      urls: self.data.imglist,  //所有要预览的图片
    })
  },
  btn:function(){
    let self = this
    let arr = self.data.imglist
    if(self.data.state){
      self.setData({
        state: false
      })
      wx.request({
        url: app.data.API +`/sq/sqqq`,
        method: 'POST',
        data:{
          openId: self.data.openId,
          ruleId: self.data.ruleId,
          msg: self.data.text
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded' // 默认值
        },
        success(res){
          self.setData({
            state: true
          })
          console.log(res)
          if(res.data.code == '0010'){
            self.setData({
              imglist: [],
              text:''
            })
            wx.showToast({
              title: "成功",
              icon: 'success',//图标，支持"success"、"loading" 
              duration: 1500,//提示的延迟时间，单位毫秒，默认：1500 
            })
          for(let i = 0; i<arr.length;i++){
            self.savefile(arr[i],res.data.uuid)
          }
          } else if (res.data.code == '007') {
            self.setData({
              imglist: [],
              text: ''
            })
            wx.showToast({
              title: "不能多次重复申诉",
              icon: 'none',//图标，支持"success"、"loading" 
              duration: 2500,//提示的延迟时间，单位毫秒，默认：1500 
            })
          } else {
            wx.showToast({
              title: "网络不稳定，请重新申请",
              icon: 'none',//图标，支持"success"、"loading" 
              duration: 2000,//提示的延迟时间，单位毫秒，默认：1500 
            })
          }
        }
      })
    }
    
    
  },
  savefile:function(img,uuid){
    let self = this
    wx.compressImage({
      src: img, // 图片路径
      quality: 50 // 压缩质量
    })
    wx.uploadFile({
      url: app.data.API +'/sq/savefile', //仅为示例，非真实的接口地址
      filePath: img,
      name: 'file',
      formData: {
        uuid: uuid,
      },
      success: function (res) {
        console.log(res)
        //do something
      }
    })
  },
  onLoad: function (options) {
    let self = this
    self.setData({
      ruleId: options.id
    })
    // wx.setNavigationBarTitle({title:options.name})
    wx.getStorage({
      key: 'userid',
      success: function(res) {
        self.setData({
          openId:res.data.id
        })
      },
    })
  }
})
