# coupon
淘客个人领券App，支持淘宝、京东、唯品会、拼多多、美团。

页面很简洁，仅支持个人使用。代码仅供参考

App下载地址：<br>
[下载地址1](http://101.200.52.208/local/优惠券.apk)<br>
[下载地址2](https://raw.githubusercontent.com/leeyoshinari/coupon/main/app/version/优惠券.apk)


## 使用方法

### 外卖配置
`该App使用Java原生开发，仅支持Android。`
* 注册好京东联盟、唯品会联盟、美团联盟、淘宝联盟和多多进宝
* 分别注册各联盟的个人开发者
* 分别到各联盟里拿到推广链接
* 由于限制，京东和唯品会使用的是第三方淘客平台提供的接口，详情请阅读代码

### 如不想安装京东App、拼多多App、唯品会App、美团App，可以使用微信小程序同样也可购买，需搭配[coupon_minapp](https://github.com/leeyoshinari/coupon_minapp)使用。
在该app上点击需要购买的商品，如果手机没用安装对应平台的app，则会自动复制商品的小程序购买链接，然后去微信打开小程序（[coupon_minapp](https://github.com/leeyoshinari/coupon_minapp)），粘贴刚刚自动复制的链接去购买即可。

### 开发
代码比较简单，也写的比较乱，有些设置项写在代码里，适合有一点点debug能力的朋友使用

