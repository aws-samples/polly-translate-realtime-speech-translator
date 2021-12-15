# Speech to Speech Translator Demo
----

This is a sample Speech to Speech Translator demo application which can be used to translate your voice in real-time to various target languages using the AWS AI services Amazon Translate and Amazon Polly.

**This project is intended for education purposes only and not for production usage.**


## Supported Regions

The app has been tested in the ap-south-1 AWS region indicated in the deployment instructions below. Additional regions may be supported depending on [service availability][1] and having the Demo application's deployment resources staged to an S3 bucket in the targeted region.

## Getting Started
---

![Android UI][android_ui]


## Architecture
---
![Architecture](./images/architecture.png?raw=true "Architecture")

## Getting Started
---


IMPORTANT NOTE: Deploying this demo application in your AWS account will create and consume AWS resources, which may cost money. Therefore, to avoid ongoing charges and to clean up all data, be sure to follow all clean up instructions and shutdown/remove all resources by deleting the CloudFormation stack once you are finished and no longer need the resources.

## Pre-requisites
---

* Java 1.8 or higher
* Maven 3.x
* Gradle
* AWS CLI v2


## Deploying the Backend
---

Check out the code from github.

```bash
git clone <insert repository url here>
```

In order to make the deployment simple,a shell script is provided which will automate the deployment of the backend infrastructure using [AWS CloudFormation](https://aws.amazon.com/cloudformation/).

**Make sure that you have the AWS CLI configured correctly with the requisite permissions to create resources**

If you're on Linux or MacOS, run the below script:

```bash
./deploy/deploy.sh
```


## Building the Android App
---
See the [Android Readme](./android/AWSVoiceTranslator/README.md) for more details.


## Cleanup
---

Once you are done and no longer need the resources, use the below command to clean-up the resources created:

```bash
./deploy/cleanup.sh
```

## Known Issues
---
* The application was written for demonstration purposes and not for production use.
* Make sure your CloudFormation stack name uses all lowercase letters.
* Currently only tested in the AWS regions provided in the deployment instructions above. The only limitation for deploying into other regions is [availability of all required services](https://aws.amazon.com/about-aws/global-infrastructure/regional-product-services/).


## Reporting Bugs
---

If you encounter a bug, please create a new issue with as much detail as possible and steps for reproducing the bug. See the [Contributing Guidelines](./CONTRIBUTING.md) for more details.

## Contributors
 - Darshit Vora
 - Neil DCruz
 - Dinesh Sharma
 

## License Summary
---

This sample code is made available under a modified MIT license. See the [LICENSE](./LICENSE) file.



[1]: https://aws.amazon.com/about-aws/global-infrastructure/regional-product-services/
[android_ui]: ./images/android_ui.png
