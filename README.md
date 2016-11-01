PairUp [![Circle CI](https://circleci.com/gh/fs/b-designworks-android.png?style=shield&circle-token=4f747ae99f6db60e3e2125833690de99897d368b)](https://circleci.com/gh/fs/b-designworks-android)

=======================================
##Prerequisites
* JDK 8
* `JAVA_HOME` pointing to your jdk8
* Android SDK

####For following terminal commands you need to be at the project root folder

###Tests
Run `./gradlew check`

###Build process
1. Open terminal in project root folder
2. Run `./gradlew assembleProductionRelease`
3. Apk will be at `$projectRoot/app/build/outputs/apk/app-production-release.apk` signed with production keystore.

###Distributation
run `./gradlew assembleProductionRelease crashlyticsUploadDistributionProductionRelease`

###Other
There is no difference between production and staging flavors at this moment (1 Nov 2016).

[<img src="http://www.flatstack.com/logo.svg" width="100"/>](http://www.flatstack.com)
