package fr.bergit;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.apigateway.IResource;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.ResourceProps;
import software.amazon.awscdk.services.cognito.UserPool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApiGatewayInfraAppStack extends Stack {
    public ApiGatewayInfraAppStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public ApiGatewayInfraAppStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);


        final EndpointConfiguration endpointConfigurationProperty = EndpointConfiguration.builder().types(Arrays.asList(EndpointType.REGIONAL)).build();
        final RestApi restApi = RestApi.Builder.create(this, "cfn-lccd-rest-api").endpointConfiguration(endpointConfigurationProperty).build();

        /**
         * ResponseModels
         */

        Map<String,Model> responseModel200 = new HashMap<>();
        responseModel200.put("application/json", Model.Builder.create(this, "modelresponse200").schema(JsonSchema.builder().build()).contentType("application/json").restApi(restApi).build());
        final MethodResponse methodResponse200 = MethodResponse.builder().responseModels(responseModel200).statusCode("200").build();

        Map<String,Model> responseModel500 = new HashMap<>();
        responseModel500.put("application/json", Model.Builder.create(this, "modelresponse500").schema(JsonSchema.builder().build()).contentType("application/json").restApi(restApi).build());
        final MethodResponse methodResponse500 = MethodResponse.builder().responseModels(responseModel500).statusCode("500").build();

        /**
         * RequestTemplates
         */
        Map<java.lang.String, java.lang.String> requestTemplate200 = new HashMap<>();
        requestTemplate200.put("application/json", "{\"statusCode\": 200, \"message\": \"HI BERGUIGA\"}");

        /**
         * ResponseTemplates
         */
        Map<java.lang.String, java.lang.String> responseTemplates200 = new HashMap<>();
        responseTemplates200.put("application/json", "{\"message\": \"Helo BERGUIGA I work correctly\"}");

        Map<java.lang.String, java.lang.String> responseTemplates500 = new HashMap<>();
        responseTemplates500.put("application/json", "{\"message\": \"Custom Error: Internal Server Error\"}");

        String regex200 = "2".concat("\\").concat("d{2}");
        String regex500 = "5".concat("\\").concat("d{2}");

        /**
         * Integration
         */
        final MockIntegration mockIntegration = MockIntegration.Builder.create()
                .integrationResponses(Arrays.asList( IntegrationResponse.builder().responseTemplates(responseTemplates200).selectionPattern(regex200).statusCode("200").build(),
                                                     IntegrationResponse.builder().responseTemplates(responseTemplates500).selectionPattern(regex500).statusCode("500").build()
                ))
                .requestTemplates(requestTemplate200)
                .passthroughBehavior(PassthroughBehavior.WHEN_NO_TEMPLATES)
                .connectionType(ConnectionType.INTERNET)
                .build();

        /**
         * ApiGatewayResource
         */
        final Resource resource = Resource.Builder.create(this, "cfn-lccd-iresource")
                .parent(restApi.getRoot())
                .pathPart("up-status")
                .build();
        /**
         * ApiGatewayMethod
         */
        final Method getMethod = Method.Builder.create(this, "cfn-lccd-rest-fake-method")
                .httpMethod("GET")
                .resource(resource)
                .integration(mockIntegration)
                .options(MethodOptions.builder().methodResponses(Arrays.asList(methodResponse200, methodResponse500)).build())
                .build();



        //Deployment
        final CfnDeployment deployment = CfnDeployment.Builder.create(this, "Lccd-deployement").stageName("developStageName").restApiId(restApi.getRestApiId()).build();


        //Affect stage
        final CfnStage cfnStage = CfnStage.Builder.create(this, "Lccd-stage").deploymentId(deployment.getRef()).stageName("develop").restApiId(restApi.getRestApiId()).build();




        //Affect Authorizers
        final String userPoolArn = Fn.importValue("userPoolArn");

        final CfnAuthorizer cfnAuthorizer = CfnAuthorizer.Builder.create(this, "Lccd-auth")
                .name("lccd_authorizer")
                .restApiId(restApi.getRestApiId())
                .type("COGNITO_USER_POOLS")
                .providerArns(Arrays.asList(userPoolArn))
                .identitySource("method.request.header.Authorization")
                .build();


        exportAttr(restApi);
    }


    private void exportAttr(RestApi restApi) {
        CfnOutput.Builder.create(this, "outpout")
                .description("ID of the created Api Gateway")
                .value(restApi.getRestApiId())
                .exportName(this.getStackName()+"-ApiId")
                .build();
    }

}

/**
 * const cfnAPI= new apigw.CfnRestApi(this, 'testcfnApi', {
 *    name: 'testfnrestapi',
 *
 *   parameters:{
 *     endpointConfigurationTypes: EndpointType.REGIONAL,
 *
 *     ignore: 'documentation'
 *   },
 *   endpointConfiguration: {
 *          types: [EndpointType.REGIONAL]
 *   },
 *   bodyS3Location: {
 *         bucket: 'testswaggerapi',
 *         key: 'Test-AWS-APIGateway.postman_collection.json-Swagger20.json'
 *    }
 *
 * });
 */


//CREATE API REST
/*        final CfnRestApi.EndpointConfigurationProperty endpointConfigurationProperty = CfnRestApi.EndpointConfigurationProperty.builder().types(Arrays.asList("REGIONAL")).build();
        final CfnRestApi restApi = CfnRestApi.Builder.create(this, "cfn-lccd-rest-api")
                                                .name("LCDD-REST-API")
                                                .endpointConfiguration(endpointConfigurationProperty)
*//*                                                .bodyS3Location(CfnRestApi.S3LocationProperty.builder()
                                                        .bucket("TODO:Bucket where swagger file exist").key("TODO:swagger file name")
                                                        .build())*//*
                                                .build();*/