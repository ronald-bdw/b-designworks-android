Android app skeleton [![Circle CI](https://circleci.com/gh/fs/b-designworks-android.png?style=shield&circle-token=4f747ae99f6db60e3e2125833690de99897d368b)](https://circleci.com/gh/AdMeGroup/AdMe-Android)

=======================================
##Prerequisites
* JDK 8
* `JAVA_HOME` pointing to your jdk8

##Plugins for Android Studio for comfortable work
* [Parcelable generator](https://github.com/mcharmas/android-parcelable-intellij-plugin)

##What's included:
* [Staging and Production](https://github.com/fs/android-base/blob/master/app/build.gradle#L29-L38) build flavors with different package names ([read more](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Product-flavors))
* Logger configuration [supporting `Exception` logging](https://github.com/fs/android-base/blob/master/app/src/main/java/com/flatstack/android/App.java#L24-L26) ([read more](https://github.com/JakeWharton/timber))
* [Robolectric support and configuration](https://github.com/fs/android-base/blob/master/app-tests/build.gradle) ([read more](http://blog.blundell-apps.com/android-gradle-app-with-robolectric-junit-tests/))
* *Android Lint* [configuration](https://github.com/fs/android-base/blob/master/app/build.gradle#L56-L61)
* *Travis CI* and *CircleCI* build [script1](https://github.com/fs/android-base/blob/master/.travis.yml) [script2](https://github.com/fs/android-base/blob/master/circle.yml):
    * Downloading an *Android SDK*
    * Building
    * Running *Android Lint*
    * Running *Robolectric* tests
    * Hook up your continuous deployment target in [`after_success`](https://github.com/fs/android-base/blob/master/.travis.yml#L40) for travis and in ['deployment'](https://github.com/fs/android-base/blob/master/circle.yml#L20) for CircleCi
* Release build signing and naming configuration

##What's not included
* [Crashlytics](crashlytics.com): they live in their own world, and including their plugin in template project just fails the build, if `apikey` is not specified. Also, getting `apikey` without an IDE plugin is impossible. You can get it [here](https://crashlytics.com/downloads/android-studio)
* Test coverage: still in the process of figuring out what's the best way to enable unit test coverage for Android with Robolectric. Any suggestions will be highly appreciated

##Setup
 1. Clone application as new project with original remote named "android-base"

    	git clone --depth 1 git://github.com/fs/android-base.git --origin android-base [MY-NEW-PROJECT]

    **Note: we use depth parameter here in order to not copy the history of changes in base project**

 2. Create your new repository on the GitHub and push master into it. Make sure master branch is tracking origin repo.

        cd [MY-NEW-PROJECT]
    	git remote add origin git@github.com:[MY-GITHUB-ACCOUNT]/[MY-NEW-PROJECT].git
    	git push -u origin master

 3. Import the project into your favourite IDE (only [Android Studio](https://developer.android.com/sdk/installing/studio.html) and [IntelliJ IDEA](http://www.jetbrains.com/idea/) are supported).
Just select the root `build.gradle` and your IDE will do the rest.
It will ask you to change the language level - do it, we're using Java 8 now

###Configuration
* Change your app's package by either [renaming the folder structure for Java sources](https://github.com/fs/android-base/tree/master/app/src/main/java/com/flatstack/android) or by just changing this [constant](https://github.com/fs/android-base/blob/master/app/build.gradle#L5) in `build.gradle`

###Making a release build
* Just uncomment [these lines](https://github.com/fs/b-designworks-android/blob/master/app/build.gradle#L41-L48) and fill them up with your credentials

[<img src="http://www.flatstack.com/logo.svg" width="100"/>](http://www.flatstack.com)
