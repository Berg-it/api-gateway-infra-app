# Welcome to your CDK Java project!

this project aims to create __AWS GATEWAY API__ using CDK. By default I added only one Endpoint where `Integration type` is equal to `Mock` in order to test that your resource work correctly.

under the class __ApiGatewayInfraAppStack__, I import the ARN of my current user pool using the code as bellow
  `Fn.importValue("userPoolArn");` 
So if you need to configure `CfnAuthorizer`, you must change the passed value by your exported arn value of user pool.


The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!


## License
[MIT](https://choosealicense.com/licenses/mit/)
