'use strict'

import { NativeModules } from 'react-native'
// name as defined via ReactContextBaseJavaModule's getName
const HumaniqApiLib = NativeModules.HumaniqApiLib
const HumaniqProfileApiLib = NativeModules.HumaniqProfileApiLib

export {HumaniqApiLib, HumaniqProfileApiLib}
