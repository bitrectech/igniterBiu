# <font face="Black Italic">__IgniterBiu__</font>

[![BLE5](https://img.shields.io/badge/Support-BLE5-blue)](BLE5)
[![Android](https://img.shields.io/badge/Android-8.0+-blue)](Android)
[![distance](https://img.shields.io/badge/BLE5_Theory_Distance-300M-BrightGreen?style=flat)](distance)
[![license](https://img.shields.io/badge/License-GLPv3-orange)](LICENSE)
[![version](https://img.shields.io/badge/Version-1.0.103__beta-color=Green?style=flat)](version)
[![standard-readme
compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

<img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/app/src/main/res/drawable-nodpi/logosoft.png" width="100" alt="logo" />  

Android Bluetooth remote ignition application is applied to the ignition operation of model rocket. This is the Android end source code. Bluetooth principle and source code [click here](https://github.com/bitrefactor/igniter_ble50)

Languages [简体中文](https://github.com/bitrefactor/igniterBiu/blob/master/README.md)

<html>
    <table style="margin-left: auto; margin-right: auto;">
        <tr>
            <td>
                <img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/.image/scan.jpg" width="200" alt="scan" />
            </td>
            <td>
                <img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/.image/connect.jpg" width="200" alt="connect" />
            </td>
            <td>
                <img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/.image/home.jpg" width="200" alt="homepage" />
            </td>
        </tr>
    </table>
</html>

## Table of Contents

- [Security](#Security)
- [Background](#Background)
- [Install](#Install)
- [Usage](#Usage)
- [Principle](#Principle)
- [License](#License)

## Security

#### Location Permissions, Bluetooth Permission Description
The mobile phone is turned on to grant positioning permissions, managing Bluetooth permissions, granting permissions only for Bluetooth associated with the application. Offline applications, data cannot be migrated

## Background

When the model rocket is ignition, in order to ensure safety, it is usually guaranteed to have sufficient safety distances and model rockets, and wired deployment is more cumbersome. Offline is the best choice. Why not choose WiFi, first, the mobile phone is connected to the client to connect the ignition device, which will cause the mobile phone to be limited; the second point, the mobile phone is a hot spot, the ignition device is more cumbersome; third, Bluetooth low power version 5 support Connections within 300M, the connection is relatively simple.

## Install

#### FirstWay (Download [Github](https://github.com/bitrefactor/igniterbiu/release/download/v1.0.103_beta/v1.0.103_beta.apk))

#### SecondWay (compilation source mode installation)
1.Download the source code
```
git clone https://github.com/bitrefactor/igniterbiu.git  #cloned source to the local

or

wget https://github.com/bitrefactor/githiterbiu/archive/refs/tags/v1.0.103_beta.zip
```
2. Download [andriodstudio](https://develop/studio)

3. Load the project to AndroidStudio to compile. Compilation error? [Issues](https://github.com/bitrefactor/igniterbiu/issues): Fasle

<img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/.image/as_cut.png?raw=true"
            alt="as_cut" />

4. Mobile phone open developer mode, connect your computer, compile and install.

## Usage

##### VIEW. The following is the use flow chart. Please contact me if you don't know

<img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/.image/igniter_usage_en.png?raw=true"
            alt="usage" />



## Principle

##### VIEW. The following is the schematic diagram. Please contact me if you don't know

<img src="https://cdn.jsdelivr.net/gh/bitrefactor/igniterBiu/.image/Igniter_contributing_en.png?raw=true"
            alt="contributing" />


## Licenses

[GNU General Public License v3.0](../LICENSE)