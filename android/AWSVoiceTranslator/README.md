# AWS Voice Translator app using AWS AI services

This is an example Android Application that uses HTML5 WebSpeech API enabled javascript application hosted using S3-Cloudfront static hosting.
It is then internally rendered in an android app using webview. The application translates input text to target language and talks back.
This is powered by AWS SDK invoking AWS Lambda function which internally calls AWS translate service to convert text into target language.
AWS lambda then calls AWS Polly with translated text to be spoken.
The output wav file is then stored in output S3 bucket and send back to app which play the audio

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:aws/AWSVoiceTranslator.git
```

## Configuration
### Variable Configuration
Change the view to android view. Edit the `cloudfront_url` variable in `res/values/string.xml` and add your cloudfront url which is to be rendered


### Keystores (Only Required in case you want to publish on playstore):
Create `app/keystore.gradle` with the following info:
```gradle
ext.key_alias='...'
ext.key_password='...'
ext.store_password='...'
```
And place both keystores under `app/keystores/` directory:
- `playstore.keystore`
- `stage.keystore`

## Generating unsigned/debug APK
1. Change the view to android view. Edit the cloudfront_url variable in `res/values/string.xml` and add your cloudfront url which is to be rendered
2. ***Build*** menu -> Clean Project
3. ***Build*** menu -> Make Project
4. ***Build*** menu -> Build Bundles/APKs -> Build APK
5. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*


## Build variants
Use the Android Studio *Build Variants* button to choose between **production** and **staging** flavors combined with debug and release build types

## Generating signed APK
From Android Studio:
1. Change the view to android view. Edit the cloudfront_url variable in `res/values/string.xml` and add your cloudfront url which is to be rendered
2. ***Build*** menu
3. ***Generate Signed APK...***
4. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## Contributors
 - Darshit Vora
 - Neil DCruz
 - Dinesh Sharma

## License

This library is licensed under the MIT-0 License. See the LICENSE file.