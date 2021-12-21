 # <font face="Black Italic">__IgniterBiu__</font>

[![BLE5](https://img.shields.io/badge/%E6%94%AF%E6%8C%81-BLE5-blue)](BLE5)
[![Android](https://img.shields.io/badge/Android-8.0+-blue)](Android)
[![distance](https://img.shields.io/badge/BLE5理论距离-300M-BrightGreen?style=flat)](distance)
[![license](https://img.shields.io/badge/License-GLPv3-orange)](LICENSE)
[![version](https://img.shields.io/badge/Version-1.0.103__beta-color=Green?style=flat)](version)
[![standard-readme
compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

<img src="https://github.com/bitrefactor/igniterBiu/blob/master/app/src/main/res/drawable-nodpi/logosoft.png?raw=true" width="100" alt="logo" /> 

安卓蓝牙远程点火应用,应用于模型火箭的点火操作,这是安卓端源码。蓝牙端原理源码[点击这里](https://github.com/bitrefactor/igniterBLE5)

其他语言 [English](https://github.com/bitrefactor/igniterBiu/blob/master/README_en.md?raw=true)

<html>
    <table style="margin-left: auto; margin-right: auto;">
        <tr>
            <td>
                <img src="https://github.com/bitrefactor/igniterBiu/blob/master/.image/scan.jpg?raw=true" width="200"
            alt="scan" />
            </td>
            <td>
                <img src="https://github.com/bitrefactor/igniterBiu/blob/master/.image/connect.jpg?raw=true" width="200"
            alt="connect" />
            </td>
            <td>
                <img src="https://github.com/bitrefactor/igniterBiu/blob/master/.image/home.jpg?raw=true" width="200"
            alt="homepage" />
            </td>
        </tr>
    </table>
</html>

## 目录

- [权限](#权限)
- [设计初衷](#设计初衷)
- [安装](#安装)
- [用法](#用法)
- [原理](#原理)
- [许可证](#许可证)

## 权限

#### 定位权限、蓝牙权限说明
手机开启蓝牙需要授予定位权限、管理蓝牙的权限,授予权限仅用于该应用的蓝牙相关。离线应用，数据不可迁移

## 设计初衷

模型火箭的点火时,为了保障安全,通常要保证和模型火箭有足够的安全距离,并且有线部署比较繁琐。离线为最佳的选择。为什么选择wifi,首先手机作为客户端连接点火装置,这样会导致手机的网络受限；第二点，手机作为热点，点火装置端配网操作比较繁琐；第三点，蓝牙低功耗版本5支持300m以内的连接，连接比较简易。

## 安装
#### 第一种方式([下载安装](https://github.com/bitrefactor/igniterBiu/releases/download/v1.0.103_beta/v1.0.103_beta.apk))


#### 第二种方式(编译源码方式安装)
1.下载源码
```
git clone https://github.com/bitrefactor/igniterBiu.git   #克隆源码到本地

或者

wget https://github.com/bitrefactor/igniterBiu/archive/refs/tags/v1.0.103_beta.zip
```
2.下载[AndriodStudio](https://developer.android.google.cn/studio)

3.加载项目至AndroidStudio进行编译。 编译错误? [issues](https://github.com/bitrefactor/igniterBiu/issues) : fasle

<img src="https://github.com/bitrefactor/igniterBiu/blob/master/.image/as_cut.png?raw=true"
            alt="as_cut" />

4.手机打开开发者模式，连接你的电脑，编译安装。

## 用法

```
```

## 原理

```
```

## 许可证

[GNU General Public License v3.0 ](../LICENSE)