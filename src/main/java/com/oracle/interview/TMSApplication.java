package com.oracle.interview;

import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.ActivityRepositoryImpl;
import com.oracle.interview.db.entity.Activity;
import com.oracle.interview.resources.ActivityController;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
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
        bootstrap.addBundle(new MigrationsBundle<TMSConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TMSConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final TMSConfiguration configuration,
                    final Environment environment) {

        final ActivityRepository dao = new ActivityRepositoryImpl(hibernateBundle.getSessionFactory());

//        environment.healthChecks().register("template", new TemplateHealthCheck(template));
//        environment.admin().addTask(new EchoTask());

//        environment.jersey().register(DateRequiredFeature.class);

//        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
//                .setAuthenticator(new ExampleAuthenticator())
//                .setAuthorizer(new ExampleAuthorizer())
//                .setRealm("SUPER SECRET STUFF")
//                .buildAuthFilter()));
//
//
//        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new ActivityController(dao));
    }


    private final HibernateBundle<TMSConfiguration> hibernateBundle =
            new HibernateBundle<TMSConfiguration>(Activity.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(TMSConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };
}
