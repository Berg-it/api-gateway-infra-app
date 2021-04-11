package fr.bergit;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.StageSynthesisOptions;

public final class ApiGatewayInfraAppApp {
    public static void main(final String[] args) {
        App app = new App();
        new ApiGatewayInfraAppStack(app, "ApiGatewayInfraAppStack");

        app.synth();
    }
}
