package com.oracle.interview;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TMSApplication extends Application<TMSConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TMSApplication().run(args);
    }

    @Override
    public String getName() {
        return "TMS";
    }

    @Override
    public void initialize(final Bootstrap<TMSConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final TMSConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
