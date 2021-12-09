package com.oracle.interview;

import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.ActivityRepositoryImpl;
import com.oracle.interview.db.entity.Activity;
import com.oracle.interview.resources.ActivityController;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

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

        bootstrap.addBundle(new SwaggerBundle<TMSConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TMSConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.addBundle(hibernateBundle);

        // Other init code
        bootstrap.addBundle(new AssetsBundle("/apidocs", "/apidocs", "index.html")
        );
    }

    @Override
    public void run(final TMSConfiguration configuration, final Environment environment) {
        final ActivityRepository dao = new ActivityRepositoryImpl(hibernateBundle.getSessionFactory());
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
